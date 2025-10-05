package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.*;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.helper.DateHelper;
import com.dummy.wpb.product.model.*;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.service.AmendmentService;
import com.dummy.wpb.product.service.SystemParameterService;
import com.dummy.wpb.product.utils.FinDocUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UploadService {

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public MongoOperations mongoOperations;

    @Value("${finDoc.pdf.rej}")
    private String pdfRejPath;

    @Value("${finDoc.pdf.aprv}")
    private String pdfAprvPath;

    @Value("${finDoc.pdf.chk}")
    private String pdfChkPath;

    @Value("${finDoc.ftp}")
    private String finDocPath;

    @Value("${CHMOD.Script}")
    private String chmodScriptPath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Value("${finDoc.doc.chk}")
    private String finDocChkPath;

    @Autowired
    public AmendmentService amendmentService;

    @Autowired
    public FinDocProdRLService finDocProdRLService;

    public void uploadRec(FinDocInput fdi) throws IOException {
        FinDocSmry fds = setupFDS(fdi);
        PDFFileService pdfServ = new PDFFileService(fds);
        File fupd = null;

        //check AprvReqInd,AprvReqInd:No --> no need to approve, just do update;First insert fin_doc_hist, then insert prod_fin_doc/PROD_TYPE_FIN_DOC/PROD_SUBTP_FIN_DOC, last insert fin_doc_upld
        if (fds.getAprvReqInd().equals(FinDocConstants.NO)) {
            fupd = pdfServ.getFile(finDocPath);

            fds.setDocSizeNum(fupd.length());
            //@1,Insert the record to fin_doc_hist; 2,update docSerNum in sys_parm@
            fds = insertSmryRec(finDocPath, fds);
            //@3,Insert the record to fin_doc_upld;@
            insertUploadReq(fdi, null, null,  fds, true);

            finDocProdRLService.prodToFinDocRLProcess(fdi, fds.getDocSerNum());
        } else {
            //check AprvReqInd,AprvReqInd:Yes, move file to check path, insert fin_doc_upld and amendment
            fupd = pdfServ.fileToChk(finDocPath, pdfChkPath);

            if (fupd != null && fupd.exists()) {
                fds.setUrlSysText(PDFFileService.setFileUrl(fupd, true));
                fds.setDocSizeNum(fupd.length());
            }

            insertUploadReq(fdi, null, null,  fds, true);
            insertPendAppReq(fds);
        }

    }

    public File pdfMove2Rej(FinDocInput fd) {
        try {
            FinDocSmry fds = new FinDocSmry();
            fds.setDocIncmName(fd.getDocIncmName());

            fds.setCreatDt(new Date(new GregorianCalendar().getTime()
                    .getTime()));
            fds.setCreatTm(new Time(fds.getCreatDt().getTime()));

            if (!(fds.getDocIncmName() == null || fds.getDocIncmName()
                    .equals(""))) {

                //remove ack and move pdf file to reject folder
                File file = new File(finDocPath,
                        fds.getDocIncmName());

                if (file.exists()) {
                    ChmodScript.chmodScript(file.getPath(), "774", chmodScriptPath);

                    checkAck(file);

                    PDFFileService pdfS = new PDFFileService(fds);
                    return pdfS.fileToRej(finDocPath, pdfRejPath, fds);
                }
            }
        } catch (IOException e) {
            throw new productBatchException(e);
        }
        return null;
    }

    public boolean checkAck(File file) throws IOException {
        String name = "";
        Map<String,String> map = genMapSuffix();

        for(int i = 0; i <= 15; i++){
            String suffix = map.get(String.valueOf(i));

            if (file.getName().endsWith(suffix)) {
                name = StringUtils.replace(file.getName(), suffix, FinDocConstants.SUFFIX_ACK);
                break;
            } else if (i == 15){
                return false;
            }
        }

        File ackfile = new File(file.getParent(), name);
        if (ackfile.exists()) {
            Files.delete(ackfile.toPath());
            return true;
        }
        return false;
    }

    public Map<String,String> genMapSuffix(){
        Map<String,String> map = new HashMap<>();

        map.put("0",FinDocConstants.SUFFIX_PDF);
        map.put("1",FinDocConstants.SUFFIX_DOC);
        map.put("2",FinDocConstants.SUFFIX_XLS);
        map.put("3",FinDocConstants.SUFFIX_TIF);
        map.put("4",FinDocConstants.SUFFIX_TIFF);
        map.put("5",FinDocConstants.SUFFIX_GIF);
        map.put("6",FinDocConstants.SUFFIX_JPEG);
        map.put("7",FinDocConstants.SUFFIX_JPG);
        map.put("8",FinDocConstants.SUFFIX_PPT);
        map.put("9",FinDocConstants.SUFFIX_MP3);
        map.put("10",FinDocConstants.SUFFIX_MP4);
        map.put("11",FinDocConstants.SUFFIX_ZIP);
        map.put("12",FinDocConstants.SUFFIX_TXT);
        map.put("13",FinDocConstants.SUFFIX_VSD);
        map.put("14",FinDocConstants.SUFFIX_HTML);
        map.put("15",FinDocConstants.SUFFIX_XML);

        return map;
    }

    public FinDocULReqPo checkUploadReq(FinDocInput fdi) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(fdi.getCtryCde()));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(fdi.getOrgnCde()));
        query.addCriteria(Criteria.where(Field.fileRqstName).is(fdi.getFileReqName()));
        query.addCriteria(Criteria.where(Field.docUpldSeqNum).is(fdi.getSeqNum()));
        query.addCriteria(Criteria.where(Field.docStatCde).is(fdi.getStatCde()));
        return mongoTemplate.findOne(query, FinDocULReqPo.class);
    }

    public void insertUploadReq(FinDocInput fdi, String msg, File file, FinDocSmry fds, Boolean flag) {
        FinDocULReqPo finDocULReqPo = setupFDUploadReq(fdi, msg, file, fds, flag);
        if (checkUploadReq(fdi) != null) {
            throw new IllegalArgumentException("insert Record duplicate: " + fdi.getCtryCde() + fdi.getOrgnCde() + fdi.getFileReqName() + fdi.getSeqNum());
        }
        mongoTemplate.insert(finDocULReqPo);
    }

    protected FinDocULReqPo setupFDUploadReq(FinDocInput fdi, String msg, File file, FinDocSmry fds, Boolean flag) {
        FinDocULReqPo fdur = new FinDocULReqPo();
        FinDocUtils fdu = new FinDocUtils();

        fdur.setCtryRecCde(chkStr(fdi.getCtryCde(), 2));
        fdur.setGrpMembrRecCde(chkStr(fdi.getOrgnCde(), 4));
        fdur.setFileRqstName(chkStr(fdi.getFileReqName(), 50));
        fdur.setDocUpldSeqNum(fdi.getSeqNum());
        fdur.setDocFinTypeCde(chkStr(fdi.getDocTypeCde(), 15));
        fdur.setDocFinCatCde(chkStr(fdi.getDocSubtypeCde(), 15));
        fdur.setDocFinCde(chkStr(fdi.getDocId(), 30));
        fdur.setLangFinDocCde(chkStr(fdi.getLangCatCde(), 2));
        fdur.setFormtDocCde(chkStr(fdi.getFormtTypeCde(), 3));
        fdur.setDocRecvName(chkStr(fdi.getDocIncmName(), 50));
        fdur.setDocDesc(chkStr(fdi.getDocExplnText(), 200));
        fdur.setReqUrgntProcInd(chkStr(fdi.getUrgInd(), 1));
        fdur.setEmailAddrRplyText(chkStr(fdi.getEmailAdrRpyText(), 100));
        fdur.setProdTypeCde(chkStr(fdi.getProdTypeCde(), 15));
        fdur.setProdSubtpCde(chkStr(fdi.getProdSubtypeCde(), 15));
        fdur.setProdCde(chkStr(fdi.getProdCde(), 30));

        if (FinDocConstants.NO.equals(fdi.getAprvReqInd())) {
            fdur.setUrlLclSysText(fds.getUrlFsText());
            fdur.setReqAproveInd(fdi.getAprvReqInd());
            fdur.setReqArchInd(fdi.getArchReqInd());
            fdur.setDocSerNum(fds.getDocSerNum());
            fdur.setDocStatCde(fds.getStatCde());
            fdur.setDocServrStatCde(fds.getStatFsCde());
            fdur.setDocArchStatCde(fds.getStatArchCde());
            fdur.setDocSzNum(fds.getDocSizeNum());
        }else if (Boolean.FALSE.equals(flag)) {
            fdur.setDocStatCde(FinDocConstants.REJECT);
            fdur.setReasonRejText(msg);
            if (file != null) {
                fdur.setUrlLclSysText(PDFFileService.setFileUrl(file, true));
            }
            fdur.setDocSzNum(fdi.getDocSzNum());
        }else {
            fdur.setUrlLclSysText(fds.getUrlSysText());
            fdur.setReqAproveInd(fdi.getAprvReqInd());
            fdur.setDocStatCde(FinDocConstants.PENDING);
            fdur.setDocSzNum(fds.getDocSizeNum());
        }

        fdur.setRecCreatDtTm(LocalDateTime.now());
        fdur.setRecUpdtDtTm(LocalDateTime.now());
        fdur.setDocAproveDtTm(LocalDateTime.now());
        fdur.setDocEffDtTm(DateHelper.getLocalDateTimeByDate(fdu.toDate(fdi.getEffDt()), fdu.toTime(fdi.getEffTm())));
        fdur.setDocExpirDt(fdu.toDate(fdi.getExpirDt()) != null ?fdu.toDate(fdi.getExpirDt()).toLocalDate() : null);
        //default fields

        return fdur;
    }

    protected String chkStr(String str, int lh) {
        if (str != null && str.length() > lh) {
            str = str.substring(0, lh);
        }else if (str == null) {
            return null;
        }
        return str;
    }

    public FinDocSmry setupFDS(FinDocInput fd) {
        FinDocSmry fds = new FinDocSmry();
        FinDocUtils fdu = new FinDocUtils();
        Date today = new Date(new GregorianCalendar().getTime()
                .getTime());

        fds.setCtryCde(fd.getCtryCde());
        fds.setOrgnCde(fd.getOrgnCde());
        fds.setDocTypeCde(fd.getDocTypeCde());
        fds.setDocSubtpCde(fd.getDocSubtypeCde());
        fds.setDocId(fd.getDocId());
        fds.setDocFsName(fd.getFileReqName());
        fds.setLangCatCde(fd.getLangCatCde());
        fds.setFormtTypeCde(fd.getFormtTypeCde());
        fds.setDocIncmName(fd.getDocIncmName());
        fds.setDocExplnText(checkStr(fd.getDocExplnText()));
        if (fd.getUrgInd() != null) {
            fds.setUrgInd(fd.getUrgInd());
        }
        fds.setProdTypCde(checkStr(fd.getProdTypeCde()));
        fds.setProdSubtpCde(checkStr(fd.getProdSubtypeCde()));
        fds.setProdCde(checkStr(fd.getProdCde()));

        fds.setEmailAdrRpyText(checkStr(fd.getEmailAdrRpyText()));
        fds.setExpirDt(fdu.toDate(fd.getExpirDt()));
        fds.setEffDt(fdu.toDate(fd.getEffDt()));
        fds.setEffTm(fdu.toTime(fd.getEffTm()));

        fds.setCreatDt(today);
        fds.setCreatTm(new Time(today.getTime()));
        fds.setUpdtLastDt(today);
        fds.setUpdtLastTm(new Time(today.getTime()));
        fds.setAprvDt(today);
        fds.setAprvTm(new Time(today.getTime()));
        fds.setAprvReqInd(fd.getAprvReqInd());
        fds.setArchReqInd(fd.getArchReqInd());
        return fds;
    }

    private void insertPendAppReq(FinDocSmry fds) {
        try {
            FinDocAmendment finDocAmendment = setFinDocAmendment(fds);
            BeanMap finDoc = BeanMap.create(finDocAmendment);

            amendmentService.amendmentCreate(FieldValueConstants.action_add, CollectionName.fin_doc_hist.toString(), finDoc);
        } catch (Exception e) {
            log.error(String.format("Insert record to amendment failed. File name: %s; ErrorMessage: %s", fds.getDocIncmName(), e.getMessage()));
        }
    }

    private FinDocAmendment setFinDocAmendment(FinDocSmry fds) {
        FinDocHistPo finDocHistPo = setFinDocHistPo(fds);
        FinDocAmendment finDocAmendment = new FinDocAmendment();
        BeanUtils.copyProperties(finDocHistPo, finDocAmendment);
        finDocAmendment.setDocAproveDtTm(finDocHistPo.getDocAproveDtTm() !=null ? finDocHistPo.getDocAproveDtTm().toString() : null);
        finDocAmendment.setDocRleasStartDtTm(finDocHistPo.getDocRleasStartDtTm() !=null ? finDocHistPo.getDocRleasStartDtTm().toString() : null);
        finDocAmendment.setDocRleasEndDtTm(finDocHistPo.getDocRleasEndDtTm() !=null ? finDocHistPo.getDocRleasEndDtTm().toString() : null);
        finDocAmendment.setDocArchStartDtTm(finDocHistPo.getDocArchStartDtTm() !=null ? finDocHistPo.getDocArchStartDtTm().toString() : null);
        finDocAmendment.setDocArchEndDtTm(finDocHistPo.getDocArchEndDtTm() !=null ? finDocHistPo.getDocArchEndDtTm().toString() : null);
        finDocAmendment.setDocEffDtTm(finDocHistPo.getDocEffDtTm() !=null ? finDocHistPo.getDocEffDtTm().toString() : null);
        finDocAmendment.setDocExpirDt(finDocHistPo.getDocExpirDt() !=null ? finDocHistPo.getDocExpirDt().toString() : null);
        finDocAmendment.setRecCreatDtTm(finDocHistPo.getRecCreatDtTm() !=null ? finDocHistPo.getRecCreatDtTm().toString() : null);
        finDocAmendment.setRecUpdtDtTm(finDocHistPo.getRecUpdtDtTm() !=null ? finDocHistPo.getRecUpdtDtTm().toString() : null);

        return finDocAmendment;
    }

    //move file to pdf approve path
    public FinDocSmry insertSmryRec(String path, FinDocSmry fds) throws RecordNotFoundException {
        SystemParameterService systemParameterService = new SystemParameterService(mongoOperations);
        String nextDocSerNum = systemParameterService.getNextSequence(fds.getCtryCde(), fds.getOrgnCde(), FinDocComConstants.FDSN);
        fds.setDocSerNum(Long.parseLong(nextDocSerNum));
        fds.setStatCde(FinDocComConstants.APPROVAL);
        fds.setStatFsCde(FinDocComConstants.PENDING);
        fds.setStatArchCde(FinDocComConstants.PENDING);

        PDFFileService pdfServ = new PDFFileService(fds);
        File fupd = null;

        fupd = pdfServ.fileToAprv(path, pdfAprvPath);

        if (fupd != null && fupd.exists())
            fds.setUrlSysText(PDFFileService.setFileUrl(fupd, true));

        //Temp Archive Num
        fds.setDocSerArchNum(FinDocComConstants.WPC + fds.getDocSerNum());

        //insert record to fin_doc_hist
        insertFinDocHist(fds);
        return fds;
    }

    private void insertFinDocHist(FinDocSmry fds) {
        FinDocHistPo fdsPo = setFinDocHistPo(fds);
        mongoTemplate.insert(fdsPo);
    }

    private FinDocHistPo setFinDocHistPo(FinDocSmry fds) {
        FinDocHistPo fdsPo = new FinDocHistPo();

        fdsPo.setCtryRecCde(fds.getCtryCde());
        fdsPo.setGrpMembrRecCde(fds.getOrgnCde());
        fdsPo.setRsrcItemIdFinDoc((long) fds.getDocSerNum());
        fdsPo.setDocFinTypeCde(fds.getDocTypeCde());
        fdsPo.setDocFinCatCde(fds.getDocSubtpCde());

        fdsPo.setDocFinCde(fds.getDocId());
        fdsPo.setLangFinDocCde(fds.getLangCatCde());
        fdsPo.setDocStatCde(fds.getStatCde());
        fdsPo.setFormtDocCde(fds.getFormtTypeCde());
        fdsPo.setDocSzNum((long) fds.getDocSizeNum());

        fdsPo.setDocRecvName(fds.getDocIncmName());
        fdsPo.setDocDesc(fds.getDocExplnText());
        fdsPo.setReqUrgntProcInd(fds.getUrgInd());
        fdsPo.setEmailAddrRplyText(fds.getEmailAdrRpyText());
        fdsPo.setReqAproveInd(fds.getAprvReqInd());

        fdsPo.setDocServrStatCde(fds.getStatFsCde());
        fdsPo.setUrlFileServrText(fds.getUrlFsText());
        fdsPo.setFileDocName(fds.getDocFsName());
        fdsPo.setReqArchInd(fds.getArchReqInd());
        fdsPo.setDocArchStatCde(fds.getStatArchCde());

        fdsPo.setFileArchRqstName(fds.getFileArchName());
        fdsPo.setUrlArchSysText(fds.getUrlArchText());
        fdsPo.setDocArchSerNum(fds.getDocSerArchNum());
        fdsPo.setUrlLclSysText(fds.getUrlSysText());

        fdsPo.setRecCreatDtTm(DateHelper.getLocalDateTimeByDate(fds.getCreatDt(), fds.getCreatTm()));
        fdsPo.setRecUpdtDtTm(DateHelper.getLocalDateTimeByDate(fds.getUpdtLastDt(), fds.getUpdtLastTm()));

        fdsPo.setDocAproveDtTm(DateHelper.getLocalDateTimeByDate(fds.getAprvDt(), fds.getAprvTm()));

        fdsPo.setDocRleasStartDtTm(DateHelper.getLocalDateTimeByDate(fds.getRelStartDt(), fds.getRelStartTm()));
        fdsPo.setDocRleasEndDtTm(DateHelper.getLocalDateTimeByDate(fds.getRelEndDt(), fds.getRelEndTm()));

        fdsPo.setDocArchStartDtTm(DateHelper.getLocalDateTimeByDate(fds.getArchStartDt(), fds.getRelStartTm()));
        fdsPo.setDocArchEndDtTm(DateHelper.getLocalDateTimeByDate(fds.getArchEndDt(), fds.getArchEndTm()));

        fdsPo.setDocEffDtTm(DateHelper.getLocalDateTimeByDate(fds.getEffDt(), fds.getEffTm()));

        fdsPo.setDocExpirDt(fds.getExpirDt() != null? fds.getExpirDt().toLocalDate() : null);

        return fdsPo;
    }

    public String checkStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str;
    }

    public void moveForm(File file, String path) {

        Calendar cal = new GregorianCalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String time = "." + df.format(cal.getTime());
        String name = "";
        if (file.getName()
                .toLowerCase()
                .endsWith(FinDocConstants.SUFFIX_CSV)) {
            name = FinDocUtils.replaceName(file.getName(),
                    FinDocConstants.SUFFIX_CSV,
                    time)
                    + FinDocConstants.SUFFIX_CSV;
        }

        if (file.getName()
                .toLowerCase()
                .endsWith(FinDocConstants.SUFFIX_XLS)) {
            name = FinDocUtils.replaceName(file.getName(), FinDocConstants.SUFFIX_XLS, time) + FinDocConstants.SUFFIX_XLS;
        }

        File curFile = new File(path, file.getName());
        File newFile = null;

        if(path.equals(finDocPath)) {
            newFile = new File(finDocSrcPath, name);
        } else {
            newFile = new File(finDocChkPath, name);
        }

        // Move file to new directory
        boolean success = curFile.renameTo(newFile);

        if (newFile.exists()) {
            ChmodScript.chmodScript(newFile.getPath(), "755", chmodScriptPath);
        }

        if (!success) {
            log.error("File can not Move " + file.getPath());
        }
    }
}
