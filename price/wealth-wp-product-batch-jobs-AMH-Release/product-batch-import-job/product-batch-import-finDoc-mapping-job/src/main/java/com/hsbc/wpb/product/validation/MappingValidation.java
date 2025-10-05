/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		MappingValidation
 *
 * Creation Date	Mar 16, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 16, 2006
 *    CMM/PPCR No.		
 *    Programmer		Andy Man
 *    Description
 * 
 */
package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.model.FinDocPo;
import com.dummy.wpb.product.model.ProdTypeFinDocRelPo;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.BsonUtils;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MappingValidation extends FinDocValidation {
	private FinDocMapInput fd;
	@Autowired
    public MongoTemplate mongoTemplate;
	
	@Autowired
    public ProductService productService;

	public MappingValidation() {
		fd = new FinDocMapInput();
	}

	public boolean validation() {
		errmsg = "Record invalid";

		// mandatory field list
		List<String> mandatoryList = new ArrayList<>();
		List<String> mandatoryListFn = new ArrayList<>();
		mandatoryList.add(fd.getActnCde());
		mandatoryListFn.add(FinDocConstants.ACTION_CDE);
		mandatoryList.add(fd.getCtryCde());
		mandatoryListFn.add(FinDocConstants.CTRY_CDE);
		mandatoryList.add(fd.getOrgnCde());
		mandatoryListFn.add(FinDocConstants.ORGN_CDE);
		mandatoryList.add(fd.getProdTypeCde());
		mandatoryListFn.add(FinDocConstants.PROD_TYP);
		mandatoryList.add(fd.getProdSubtpCde());
		mandatoryListFn.add(FinDocConstants.PROD_STP);
		mandatoryList.add(fd.getProdId());
		mandatoryListFn.add(FinDocConstants.PROD_ID);

		mandatoryList.add(fd.getDocTypeCdeP());
		mandatoryListFn.add(FinDocConstants.DOC_TYP);
		mandatoryList.add(fd.getLangCatCdeP());
		mandatoryListFn.add(FinDocConstants.LANG);
		if (!(fd.getActnCde().equals(FinDocConstants.DELETE_ACTION))) {
			mandatoryList.add(fd.getDocSource());
			mandatoryListFn.add(FinDocConstants.DOC_SRC);
		}
		mandatoryList.add(fd.getEmailAdrRpyText());
		mandatoryListFn.add(FinDocConstants.RPY_EMAIL);
		if ((!(fd.getActnCde().equals(FinDocConstants.DELETE_ACTION)))
				&& fd.getDocSource() != null
				&& fd.getDocSource().equals(FinDocConstants.DOC_SRC_TYP_DOC)) {
			mandatoryList.add(fd.getDocTypeCde());
			mandatoryListFn.add(FinDocConstants.DOC_TO_TYP);
			mandatoryList.add(fd.getDocSubtpCde());
			mandatoryListFn.add(FinDocConstants.DOC_TO_STP);
			mandatoryList.add(fd.getDocId());
			mandatoryListFn.add(FinDocConstants.DOC_TO_ID);
			mandatoryList.add(fd.getLangCatCde());
			mandatoryListFn.add(FinDocConstants.LANG_TO);

			fd.setUrl(null);

		}

		if ((!(fd.getActnCde().equals(FinDocConstants.DELETE_ACTION)))
				&& fd.getDocSource() != null
				&& fd.getDocSource().equals(FinDocConstants.DOC_SRC_TYP_URL)) {
			mandatoryList.add(fd.getUrl());
			mandatoryListFn.add(FinDocConstants.URL);
		}
		String[] mandatoryArray = new String[mandatoryList.size()];
		String[] mandatoryArrayFn = new String[mandatoryListFn.size()];
		for (int i = 0; i < mandatoryArray.length; i++) {
			mandatoryArray[i] = mandatoryList.get(i);
			mandatoryArrayFn[i] = mandatoryListFn.get(i);
		}

		try {
			if ((!chkMandatory(mandatoryArray, mandatoryArrayFn))
					|| (!checkActnCde(fd.getActnCde()))
					|| (!checkCtryCde(fd.getCtryCde()))
					|| (!checkOrgnCde(fd.getOrgnCde()))
					|| (!checkProdTypeCde(fd.getCtryCde(), fd.getOrgnCde(),
							fd.getProdTypeCde()))
					|| (!checkProdSubtpCde(fd.getCtryCde(),
					fd.getOrgnCde(),
					fd.getProdSubtpCde(),
					fd.getProdTypeCde()))
					|| (!checkProdId(fd.getProdId()))
//					|| (!checkProdExist(fd))
					|| (!checkDocTypeCde(fd.getCtryCde(), fd.getOrgnCde(),
							fd.getDocTypeCdeP()))
					|| (!checkProdTypRel(fd.getCtryCde(), fd.getOrgnCde(),
							fd.getProdTypeCde(), fd.getDocTypeCdeP()))
					|| (!checkLangCatCdeP(fd.getLangCatCdeP()))
					|| (!checkDocSrc(fd.getDocSource()))
					|| (!checkDocTypeCde(fd.getCtryCde(), fd.getOrgnCde(),
							fd.getDocTypeCde()))
					|| (!checkDocSubtpCde(fd.getCtryCde(), fd.getOrgnCde(),
							fd.getDocSubtpCde()))
					|| (!checkDocId(fd.getDocId()))
					|| (!checkLangCatCde(fd.getLangCatCde()))
					|| (!checkURL(fd.getUrl()))
					|| (!checkEmailAdrRpyText(fd.getEmailAdrRpyText()) || (!checkDocExist(fd)))) {
				rejectRec.add(rejectMail(fd, errmsg, log));
				return false;
			}else if(!checkProdExist(fd)){
				skipRec.add(skipMail(fd, errmsg));
				return false;
			}
		} catch (Exception e) {
			rejectRec.add(rejectMail(fd, e.getMessage(), log));
			return false;
		}
		return true;
	}

	public EmailContent rejectMail(FinDocMapInput fd, String msg, Logger logger) {
		String sub = fd.getInputFileName() + ": Mapping Record Rejected";
		String add = fd.getEmailAdrRpyText();
		String content = "Record" + fd.getRecNum() + " <" + msg
				+ "> was submitted at " + getcurTimeDDMMMYY() + ".";

		logger.error("{}({}:{}:{}:{}) - {} reply to {}", sub, fd.getProdTypeCde(), fd.getProdId(), fd.getDocTypeCdeP(), fd.getLangCatCdeP(), content, add);

		return setMailObj(sub, add, content);
	}
	
	public EmailContent skipMail(FinDocMapInput fd, String msg) {
		String sub = fd.getInputFileName() + ": Mapping Record Skiped";
		String add = fd.getEmailAdrRpyText();
		String content = "Record" + fd.getRecNum() + " <" + msg
				+ "> was submitted at " + getcurTimeDDMMMYY() + ".";

		return setMailObj(sub, add, content);
	}

	public boolean checkActnCde(String actnCde) {
		if (actnCde == null) {
			return true;
		}
		if (!chkLength(actnCde, 1)) {
			errmsg = FinDocConstants.ACTION_CDE + " : "
					+ FinDocConstants.LEN_ERROR;
			return false;
		}
		// check value
		if ((actnCde.trim().equals(FinDocConstants.ADD_ACTION))
				|| (actnCde.trim().equals(FinDocConstants.CHANGE_ACTION))
				|| (actnCde.trim().equals(FinDocConstants.DELETE_ACTION))
				|| (actnCde.trim().equals(FinDocConstants.MODIFY_ACTION))) {
			return true;
		} else {
			errmsg = FinDocConstants.ACTION_CDE + " : "
					+ FinDocConstants.VAL_ERROR;
			return false;
		}
	}

	public boolean checkDocSrc(String docSrc) {
		if (docSrc == null) {
			return true;
		}
		if ((docSrc.trim().equals(FinDocConstants.DOC_SRC_TYP_DOC))
				|| (docSrc.trim().equals(FinDocConstants.DOC_SRC_TYP_URL))) {
			return true;
		} else {
			errmsg = FinDocConstants.DOC_SRC + " : "
					+ FinDocConstants.VAL_ERROR;
			return false;
		}
	}

	public boolean checkURL(String url) {
		if (url == null) {
			return true;
		}
		if (!chkLength(url, 400)) {
			errmsg = FinDocConstants.URL + " : " + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	public boolean checkLangCatCdeP(String langCatCdeP) {
		if (langCatCdeP == null) {
			return true;
		}
		if (!chkLength(langCatCdeP, 2)) {
			errmsg = FinDocConstants.LANG + " : " + FinDocConstants.LEN_ERROR;
			return false;
		}
		List<String> list = this.getLanguageCdeFromProperties();

		if (null != list && !list.isEmpty()) {
			if (list.contains(langCatCdeP)) {
				return true;
			} else {
				errmsg = list.toString() + ":" + langCatCdeP;
				return false;
			}
		}
		errmsg = "please check wpsServerConfigFile.properties --> 'LANGUAGE_CDE' ";
		return false;
	}

	public boolean checkDocExist(FinDocMapInput fd) {
		if (fd.getDocSource() != null && fd.getDocSource().equals(FinDocConstants.DOC_SRC_TYP_URL))
			return true;

		try {
			List<FinDocPo> finDocs = getFinDocPo(fd.getCtryCde(), fd.getOrgnCde(), fd.getDocTypeCde(),
					fd.getDocSubtpCde(), fd.getDocId(), fd.getLangCatCde());

			if (!CollectionUtils.isEmpty(finDocs)) {
				fd.setDocSerNum(finDocs.get(0).getRsrcItemIdFinDoc());
				return true;
			}
		} catch (Exception e) {
			throw new productBatchException(e);
		}

		errmsg = FinDocConstants.MDOC_ERROR;
		return false;
	}
	
	protected List<FinDocPo> getFinDocPo(String ctryCde, String orgnCde, String docType, String docSubType,
			String docId, String langCde) {
		Query query = new Query();
		query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryCde));
		query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(orgnCde));
		query.addCriteria(Criteria.where(Field.docFinTypeCde).is(docType));
		query.addCriteria(Criteria.where(Field.docFinCatCde).is(docSubType));
		query.addCriteria(Criteria.where(Field.docFinCde).is(docId));
		query.addCriteria(Criteria.where(Field.langFinDocCde).is(langCde));
		query.addCriteria(Criteria.where(Field.docStatCde).is("A"));
		query.addCriteria(Criteria.where(Field.docServrStatCde).is("C"));
		return mongoTemplate.find(query, FinDocPo.class);
	}
	
	protected boolean checkProdExist(FinDocMapInput fd) {

		String ctryRecCde = fd.getCtryCde().trim();
		String grpMemberCde = fd.getOrgnCde().trim();
		String prodTypeCde = fd.getProdTypeCde();
		String prodCde = fd.getProdId();

		if (prodCde != null && !prodCde.trim().equals("") && !prodCde.trim().equals("*")) {

			if (prodTypeCde == null || prodTypeCde.trim().equals("")) {
				errmsg = FinDocConstants.PROD_TYP + ":" + FinDocConstants.VAL_ERROR;
				return false;
			}

			Long prodId = 0L;
			ArrayList<Bson> filter = new ArrayList<>();
			filter.add(Filters.eq(Field.ctryRecCde, ctryRecCde));
			filter.add(Filters.eq(Field.grpMembrRecCde, grpMemberCde));
			filter.add(Filters.eq(Field.prodTypeCde, prodTypeCde));
			filter.add(Filters.eq(Field.prodAltPrimNum, prodCde));
			filter.add(Filters.ne(Field.prodStatCde, "D"));
			try {
				List<Document> documents = productService.productByFilters(BsonUtils.toMap(Filters.and(filter)));
				if (!CollectionUtils.isEmpty(documents)) {
					prodId = documents.get(0).getInteger(Field.prodId).longValue();
				}
			} catch (Exception e) {
				throw new productBatchException(e);
			}

			if (prodId == 0) {
				errmsg = FinDocConstants.PROD_ID + ":" + FinDocConstants.PRO_ERROR;

				return false;

			} else {
				fd.setProdRealID(prodId);
			}

		}

		return true;
	}
	
	protected boolean checkProdTypRel(String ctryRecCde, String grpMembrRecCde, String docFinCatCde, String docFinTypeCde) {
		List<ProdTypeFinDocRelPo> prodTypeFinDocRels = getProdTypeFinDocRelPo(ctryRecCde, grpMembrRecCde, docFinTypeCde, docFinCatCde);

		if (!CollectionUtils.isEmpty(prodTypeFinDocRels)) {
			return true;
		}
		List<ProdTypeFinDocRelPo> gnFinDocRels = getProdTypeFinDocRelPo(ctryRecCde, grpMembrRecCde, docFinTypeCde,FinDocConstants.SUBTYPCDE_GN);
		if (!CollectionUtils.isEmpty(gnFinDocRels)) {
			return true;
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

	/**
	 * @return the fd
	 */
	public FinDocMapInput getFd() {
		return fd;
	}

	/**
	 * @param fd
	 *            the fd to set
	 */
	public void setFd(FinDocMapInput fd) {
		this.fd = fd;
	}


}