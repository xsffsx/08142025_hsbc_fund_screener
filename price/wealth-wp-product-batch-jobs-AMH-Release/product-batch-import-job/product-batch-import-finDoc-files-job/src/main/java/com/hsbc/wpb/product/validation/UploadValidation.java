package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.component.UploadService;
import com.dummy.wpb.product.constant.*;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.mapper.DocumentTypeMapper;
import com.dummy.wpb.product.model.FinDocInput;
import com.dummy.wpb.product.model.ProdTypeFinDocRelPo;
import com.dummy.wpb.product.model.SystemParmPo;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.service.AmendmentService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.dummy.wpb.product.utils.FinDocUtils;
import com.dummy.wpb.product.utils.Namefilter;
import com.mongodb.client.model.Filters;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Data
public class UploadValidation extends FinDocValidation {
    private FinDocInput fd;
    private boolean aprvReq;

    private Long fileSize = 0L;
    private String docTypeShtName = "";

    @Value("${finDoc.ftp}")
    private String finDocPath;

    @Value("${finDoc.pdf.rej}")
    private String pdfRejPath;

    @Value("${CHMOD.Script}")
    private String chmodScriptPath;

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public ProductService productService;

    @Autowired
    public ReferenceDataService referenceDataService;
    @Autowired
    public AmendmentService amendmentService;

    @Autowired
    public UploadService uploadService;

    @Autowired
    private DocumentTypeMapper documentTypeMapper;

    public UploadValidation() {
        //Constructor
    }

    public UploadValidation(FinDocInput fd,
                            String version,
                            boolean aprvReq,
                            List<EmailContent> arrLst) {
        this.fd = fd;
        this.version = version;
        this.aprvReq = aprvReq;
        this.rejectRec = arrLst;
    }

    @Override
    public boolean validation() {

        //mandatory field list 
        String[] mandatoryList = { fd.getCtryCde(), fd.getOrgnCde(),
            fd.getDocTypeCde(), fd.getDocSubtypeCde(), fd.getDocId(),
            fd.getLangCatCde(), fd.getFormtTypeCde(), fd.getEffDt(),
            fd.getEffTm(), fd.getDocIncmName()};
        String[] mandatoryListFn = { FinDocConstants.CTRY_CDE,
            FinDocConstants.ORGN_CDE, FinDocConstants.DOC_TYP,
            FinDocConstants.DOC_STP, FinDocConstants.DOC_ID,
            FinDocConstants.LANG, FinDocConstants.FMT_TYP,
            FinDocConstants.EFF_DT, FinDocConstants.EFF_TM,
            FinDocConstants.DOC_NAME};
        
        if ((!chkMandatory(mandatoryList, mandatoryListFn))
            || (!checkCtryCde(fd.getCtryCde()))
            || (!checkOrgnCde(fd.getOrgnCde()))
			|| (!checkDocTypeWithPropFile(fd.getDocTypeCde()))
            || (!checkDocSubtpCde(fd.getCtryCde(), fd.getOrgnCde(), fd.getDocSubtypeCde()))
            || (!checkProdTypRel(fd.getCtryCde(), fd.getOrgnCde(), fd.getDocSubtypeCde(), fd.getDocTypeCde()))
            || (!checkDocId(fd.getDocId()))
            || (!checkLangCatCde(fd.getLangCatCde()))
            || (!checkFormtTypeCde(fd.getFormtTypeCde()))
            || (!checkDocIncmName(fd))
            || (!checkExpirDt(fd.getExpirDt()))
            || (!checkEffDt(fd.getEffDt()))
            || (!checkEffTm(fd.getEffTm()))
            || (!checkDocExplnText(fd.getDocExplnText()))
            || (!checkProdTypeCde(fd.getCtryCde(), fd.getOrgnCde(), fd.getProdTypeCde()))
            || (!checkProdSubtpCde(fd.getCtryCde(), fd.getOrgnCde(), fd.getProdSubtypeCde(), fd.getProdTypeCde()))
            || (!checkProdCde(fd.getProdCde()))
            || (!checkProdExist(fd))
            || (!checkEmailAdrRpyText(fd.getEmailAdrRpyText()))
            || (!checkUrgInd(fd.getUrgInd()))
            || (!checkFileExist(fd)) || (!checkPendApproval(fd))) {
            rejectRec.add(rejectMail(fd, errmsg));
            log.error("Rejected Record: " + fd.getDocTypeCde() + ":"
                    + fd.getDocSubtypeCde() + ":" + fd.getDocId() + " - " + errmsg);
            fileSize = getDocSize(fd.getDocIncmName());
            fd.setDocSzNum(fileSize);
            return false;
        }
        return true;
    }

    //Check the approval parm
    public void checkAprv(FinDocInput fdi) {
        List<SystemParmPo> fdp = getSysParm(fdi.getCtryCde(), fdi.getOrgnCde(), fdi.getDocTypeCde(),
                fdi.getDocSubtypeCde(), FinDocConstants.SYS_APRV);

        SystemParmPo systemParmPo;
        if (CollectionUtils.isEmpty(fdp)) {
            systemParmPo = new SystemParmPo();
            systemParmPo.setParmValueText(checkGN(fdi, FinDocConstants.SYS_APRV));
        }else {
            systemParmPo = fdp.get(0);
        }

        if (systemParmPo == null || !systemParmPo.getParmValueText().trim().equals(FinDocConstants.NO)) {
            fdi.setAprvReqInd(FinDocConstants.YES);
            this.setAprvReq(true);
        }else {
            fdi.setAprvReqInd(FinDocConstants.NO);
            this.setAprvReq(false);
        }
    }

    public void checkArch(FinDocInput fdi) {
        List<SystemParmPo> fdp = getSysParm(fdi.getCtryCde(), fdi.getOrgnCde(), fdi.getDocTypeCde(),
                fdi.getDocSubtypeCde(), FinDocConstants.SYS_ARCH);

        SystemParmPo systemParmPo;
        if (CollectionUtils.isEmpty(fdp)) {
            systemParmPo = new SystemParmPo();
            systemParmPo.setParmValueText(checkGN(fdi, FinDocConstants.SYS_ARCH));
        }else {
            systemParmPo = fdp.get(0);
        }

        //check AprvReqInd,AprvReqInd:Yes, insertUploadReq
        //check AprvReqInd,AprvReqInd:No, insertSmryRec
        if (systemParmPo == null || !systemParmPo.getParmValueText().trim().equals(FinDocConstants.NO)) {
            fdi.setArchReqInd(FinDocConstants.YES);
        }else {
            fdi.setArchReqInd(FinDocConstants.NO);
        }
    }

    protected String checkGN(FinDocInput fdi, String sysCde) {
        List<SystemParmPo> fdp = getSysParm(fdi.getCtryCde(), fdi.getOrgnCde(), fdi.getDocTypeCde(),
                FinDocConstants.SUBTYPCDE_GN, sysCde);

        SystemParmPo systemParmPo;
        if (CollectionUtils.isEmpty(fdp)) {
            systemParmPo = new SystemParmPo();
            if (StringUtils.equals(sysCde, FinDocConstants.SYS_ARCH)) {
                systemParmPo.setParmValueText(FinDocConstants.DFLT_ARCH_REQ.trim());
            } else if (StringUtils.equals(sysCde, FinDocConstants.SYS_APRV)) {
                systemParmPo.setParmValueText(FinDocConstants.DFLT_APRV_REQ.trim());
            }
        }else {
            systemParmPo = fdp.get(0);
        }
        return systemParmPo.getParmValueText().trim();
    }

    protected List<SystemParmPo> getSysParm(String ctryRecCde, String grpMembrRecCde, String docFinTypeCde,
                                            String docFinCatCde, String parmCde) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.docFinTypeCde).is(docFinTypeCde));
        query.addCriteria(Criteria.where(Field.docFinCatCde).is(docFinCatCde));
        query.addCriteria(Criteria.where(Field.parmCde).is(parmCde));
        return mongoTemplate.find(query, SystemParmPo.class);
    }

    public EmailContent rejectMail(FinDocInput fd, String msg) {
        String sub = "Document " + fd.getDocIncmName() + " Rejected";
        String emailAdrRpyText = fd.getEmailAdrRpyText();
        String content = msg + " was submitted at "
            + FinDocUtils.getcurTimeDDMMMYY() + ". Please upload again.";
        return new EmailContent(sub, emailAdrRpyText, content);
    }


    protected boolean checkDocTypeWithPropFile(String docTypeCde) {
        if (!chkLength(docTypeCde, 15)) {
            errmsg = FinDocConstants.DOC_TYP + ":" + FinDocConstants.LEN_ERROR;
            return false;
        }
        //retrieve value from Properties File
        String finDocType = documentTypeMapper.getDocType(docTypeCde);
        if (StringUtils.isBlank(finDocType)) {
            errmsg = FinDocConstants.DOC_TYP + ":" + FinDocConstants.VAL_ERROR;
            return false;
        } else {
            docTypeShtName = finDocType;
        }
        return true;
    }
    
    protected boolean checkDocIncmName(FinDocInput fd) {
        if (!chkLength(fd.getDocIncmName(), 50)) {
            errmsg = FinDocConstants.DOC_INCM_NAME + ":"
                + FinDocConstants.LEN_ERROR;
            return false;
        }

        //check value
        String validName = null;
        if(fd.getCtryCde().equals("HK")&& (fd.getOrgnCde().equals("HBAP") ||  fd.getOrgnCde().equals("dummy"))){
         validName = fd.getDocSubtypeCde().trim() + "_"
            + docTypeShtName + "_" + fd.getDocId().trim() + "_"
            + fd.getLangCatCde().trim() + "."
            + fd.getFormtTypeCde().trim();
        }else{
        	validName = fd.getCtryCde().trim()+"_"+ fd.getOrgnCde().trim()+ "_"+ fd.getDocSubtypeCde().trim() + "_"
            + docTypeShtName + "_" + fd.getDocId().trim() + "_"
            + fd.getLangCatCde().trim() + "."
            + fd.getFormtTypeCde().trim();
        }
        if (!fd.getDocIncmName().trim().equalsIgnoreCase(validName)) {
            errmsg = FinDocConstants.DOC_INCM_NAME + ":"
                + FinDocConstants.VAL_ERROR;
            return false;
        }
        return true;
    }

    //check pdf file
    protected boolean checkFileExist(FinDocInput fd) {
        try {
            File srcDir = new File(finDocPath);
            FilenameFilter fnf = new Namefilter(fd.getDocIncmName()
                .trim(), false);
            File[] match = srcDir.listFiles(fnf);

            errmsg = FinDocConstants.FILE_ERROR;

            if (match.length > 0) {
                File newFile = FinDocUtils.chkAndCreate(match[0]);
                if (newFile != null) {
                    ChmodScript.chmodScript(newFile.getPath(), "774", chmodScriptPath);
                }
                return uploadService.checkAck(newFile);
            }
        } catch (IOException e) {
            throw new productBatchException("IOException:" + e.getMessage(), e);
        }
        return false;
    }

    protected Long getDocSize(String name) {
        File file = new File(finDocPath, name);
        if (file.exists()) {
            return file.length();
        }
        return 0L;
    }

    protected boolean checkProdTypRel(String ctryRecCde, String grpMembrRecCde, String docFinCatCde, String docFinTypeCde) throws RecordNotFoundException {
        try {
            List<ProdTypeFinDocRelPo> prodTypeFinDocRels = getProdTypeFinDocRelPo(ctryRecCde, grpMembrRecCde, docFinTypeCde, docFinCatCde);

            if (!CollectionUtils.isEmpty(prodTypeFinDocRels)) {
                return true;
            }
            List<ProdTypeFinDocRelPo> gnFinDocRels = getProdTypeFinDocRelPo(ctryRecCde, grpMembrRecCde, docFinTypeCde,FinDocConstants.SUBTYPCDE_GN);
            if (!CollectionUtils.isEmpty(gnFinDocRels)) {
                return true;
            }
        } catch (Exception e) {
            throw new RecordNotFoundException("prod_type_fin_doc_type_rel record not found.", e);
        }

        errmsg = FinDocConstants.DOC_TYP + ":" + FinDocConstants.VAL_ERROR;
        return false;
    }

    protected List<ProdTypeFinDocRelPo> getProdTypeFinDocRelPo(String ctryRecCde, String grpMembrRecCde, String docFinTypeCde,
                                            String docFinCatCde) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.docFinTypeCde).is(docFinTypeCde));
        query.addCriteria(Criteria.where(Field.prodTypeCde).is(docFinCatCde));
        return mongoTemplate.find(query, ProdTypeFinDocRelPo.class);
    }

    protected boolean checkPendApproval(FinDocInput fd) throws RecordNotFoundException {
        if (!aprvReq)
            return true;

        ArrayList<Bson> filter = new ArrayList<>();
        filter.add(Filters.eq(Field.ctryRecCde,fd.getCtryCde()));
        filter.add(Filters.eq(Field.grpMembrRecCde,fd.getOrgnCde()));
        filter.add(Filters.eq(Field.docType, CollectionName.fin_doc_hist.toString()));
        filter.add(Filters.eq(Field.recStatCde, FieldValueConstants.PENDING));
        filter.add(Filters.eq(Field.doc + "."+ Field.docFinTypeCde,fd.getDocTypeCde()));
        filter.add(Filters.eq(Field.doc + "."+ Field.docFinCatCde,fd.getDocSubtypeCde()));
        filter.add(Filters.eq(Field.doc + "."+ Field.docFinCde,fd.getDocId()));
        filter.add(Filters.eq(Field.doc + "."+ Field.langFinDocCde,fd.getLangCatCde()));
        try {
            List<Document> documents = amendmentService.amendmentByFilters(Filters.and(filter));
            if (CollectionUtils.isEmpty(documents)) {
                return true;
            }
        } catch (Exception e) {
            throw new RecordNotFoundException("amendment record not found.", e);
        }

        errmsg = FinDocConstants.PEN_ERROR;
        return false;
    }
    
    protected boolean checkProdExist(FinDocInput fd) {
    	
    	String ctryRecCde = fd.getCtryCde().trim();
    	String grpMemberCde = fd.getOrgnCde().trim();
    	String prodTypeCde = fd.getProdTypeCde();
    	String prodCde = fd.getProdCde();
    	
    	if (prodCde != null && !prodCde.trim().equals("")
                && !prodCde.trim().equals("*")) {
    		
    		if (prodTypeCde == null || prodTypeCde.trim().equals("")){
    			errmsg = FinDocConstants.PROD_TYP + ":" + FinDocConstants.VAL_ERROR;
    			return false;
    		}
    		
    		Long prodId = 0L;
            ArrayList<Bson> filter = new ArrayList<>();
            filter.add(Filters.eq(Field.ctryRecCde,ctryRecCde));
            filter.add(Filters.eq(Field.grpMembrRecCde,grpMemberCde));
            filter.add(Filters.eq(Field.prodTypeCde,prodTypeCde));
            filter.add(Filters.eq(Field.prodAltPrimNum,prodCde));
            filter.add(Filters.ne(Field.prodStatCde,"D"));
            try {
                List<Document> documents = productService.productByFilters(BsonUtils.toMap(Filters.and(filter)));
                if (!CollectionUtils.isEmpty(documents)) {
                    prodId= Long.valueOf(documents.get(0).getInteger(Field.prodId));
                }
            } catch (Exception e) {
                log.error("checkProdExist: Product not found. prodAltPrimNum: " + prodCde);
            }
			
			if (prodId == 0L) {
				errmsg = FinDocConstants.PROD_ID + ":" + FinDocConstants.PRO_ERROR;
				return false;
			} else {
				fd.setProdId(prodId);
			}
    	}
        return true;
    }
}