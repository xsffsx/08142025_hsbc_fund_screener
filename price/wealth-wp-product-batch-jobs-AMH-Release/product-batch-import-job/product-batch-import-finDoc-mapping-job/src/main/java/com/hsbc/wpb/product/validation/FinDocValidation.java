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
 * Class Name		FinDocValidation
 *
 * Creation Date	Mar 8, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 8, 2006
 *    CMM/PPCR No.
 *    Programmer		Anthony Chan
 *    Description
 *
 */
package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ReferenceDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class FinDocValidation {
	private static final Log log = LogFactory.getLog(FinDocValidation.class);
	protected String version;
	protected List<EmailContent> rejectRec;
	// for testing
	protected List<EmailContent> skipRec;
	protected String errmsg = null;

	public List<EmailContent> getRejectRec() {
		return rejectRec;
	}

	public List<EmailContent> getSkipRec() {
		return skipRec;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setRejectRec(List<EmailContent> rejectRec) {
		this.rejectRec = rejectRec;
	}

	public void setSkipRec(List<EmailContent> skipRec) {
		this.skipRec = skipRec;
	}

	@Autowired
	private ReferenceDataService referenceDataService;

	protected abstract boolean validation() throws IOException;

	/**
	 * @param ctryCde
	 * @return
	 */
	protected boolean checkCtryCde(String ctryCde) {
		if (ctryCde == null) {
			return true;
		}
		if (!chkLength(ctryCde, 2)) {
			errmsg = FinDocConstants.CTRY_CDE + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}

		return true;
	}

	/**
	 * @param orgnCde
	 * @return
	 */
	protected boolean checkOrgnCde(String orgnCde) {
		if (orgnCde == null) {
			return true;
		}
		if (!chkLength(orgnCde, 4)) {
			errmsg = FinDocConstants.ORGN_CDE + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}

		return true;
	}

	/**
	 * @param docTypeCde
	 * @return
	 */
	protected boolean checkDocTypeCde(String ctryCde, String orgnCde, String docTypeCde) {
		if (docTypeCde == null) {
			return true;
		}
		if (!chkLength(docTypeCde, 15)) {
			errmsg = FinDocConstants.DOC_TYP + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		// retrieve value from DB table
		if (!checkReferData(ctryCde, orgnCde, FinDocConstants.FINDOCTYP,
				docTypeCde, null, null)) {
			errmsg = FinDocConstants.DOC_TYP + ":" + FinDocConstants.VAL_ERROR;
			return false;
		}

		return true;
	}


	protected boolean checkDocSubtpCde(String ctryCde, String orgnCde, String docSubtpCde) {
		if (docSubtpCde == null) {
			return true;
		}
		if (!chkLength(docSubtpCde, 15)) {
			errmsg = FinDocConstants.DOC_STP + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		// retrieve value from DB table
		if (!checkReferData(ctryCde, orgnCde, FinDocConstants.FINDOCSTP, docSubtpCde, null, null)) {
			errmsg = FinDocConstants.DOC_STP + ":" + FinDocConstants.VAL_ERROR;
			return false;
		}
		return true;
	}

	/**
	 * @param docId
	 * @return
	 */
	protected boolean checkDocId(String docId) {
		if (!chkLength(docId, 30)) {
			errmsg = FinDocConstants.DOC_ID + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	/**
	 * @param langCatCde
	 * @return
	 */
	protected boolean checkLangCatCde(String langCatCde) {
		if (langCatCde == null) {
			return true;
		}
		if (!chkLength(langCatCde, 2)) {
			errmsg = FinDocConstants.LANG + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}

		List<String> list = this.getLanguageCdeFromProperties();

		log.info("config language cde is:" + list.toString() +" ,the parm is:" +langCatCde);
		if (!list.isEmpty()) {
			if (list.contains(langCatCde)) {
				return true;
			} else {
				errmsg = list.toString() + ":" + langCatCde;
				return false;
			}
		}
		errmsg = "please check wpsServerConfigFile.properties --> 'LANGUAGE_CDE' ";
		return false;

	}

	/**
	 * @param prodTypeCde
	 * @return
	 */
	protected boolean checkProdTypeCde(String ctryCde, String orgnCde, String prodTypeCde) {
		if (prodTypeCde == null || prodTypeCde.trim().equals("")
				|| prodTypeCde.trim().equals(FinDocConstants.SUBTYPCDE_GN))
			return true;
		if (!chkLength(prodTypeCde, 15)) {
			errmsg = FinDocConstants.PROD_TYP + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		if (!checkReferData(ctryCde, orgnCde, FinDocConstants.PRODTYP,
				prodTypeCde, null, null)) {
			errmsg = FinDocConstants.PROD_TYP + ":" + FinDocConstants.VAL_ERROR;
			return false;
		}
		return true;
	}

	/**
	 * @param ctryCde
	 * @param orgnCde
	 * @param prodSubtpCde
	 * @param prodTypeCde
	 * @return
	 */
	protected boolean checkProdSubtpCde(String ctryCde, String orgnCde, String prodSubtpCde, String prodTypeCde) {
		if (prodSubtpCde == null || prodSubtpCde.trim().equals("")
				|| prodSubtpCde.trim().equals("*"))
			return true;
		if (!chkLength(prodSubtpCde, 15)) {
			errmsg = FinDocConstants.PROD_STP + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}

		if (!checkReferData(ctryCde, orgnCde, FinDocConstants.PRODSUBTYP,
				prodSubtpCde, FinDocConstants.PRODTYP, prodTypeCde)) {
			errmsg = FinDocConstants.PROD_STP + ":" + FinDocConstants.VAL_ERROR;
			return false;
		}
		return true;
	}

	/**
	 * @param emailAdrRpyText
	 * @return
	 */
	protected boolean checkEmailAdrRpyText(String emailAdrRpyText) {
		if ((version != null)
				&& version.equalsIgnoreCase(FinDocConstants.VER_1_1) && (!chkMandatory((new String[] { emailAdrRpyText }),
					new String[] { FinDocConstants.RPY_EMAIL }))) {
				return false;
			}
		if (!chkLength(emailAdrRpyText, 100)) {
			errmsg = FinDocConstants.RPY_EMAIL + ":"
					+ FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean chkLength(String in, int len) {
		if (in == null)
			return true;
		return in.length() <= len;
	}

	protected boolean chkMandatory(String[] in, String[] fn) {
		for (int i = 0; i < in.length; i++) {
			if (in[i] == null || in[i].equals("")) {
				errmsg = fn[i] + ":" + FinDocConstants.MAN_ERROR;
				return false;
			}
		}
		return true;
	}

	protected boolean checkReferData(String ctryCde, String orgnCde,
			String cdvTypeCde, String cdvCde, String dataPrntTypeCde,
			String dataPrntCde) {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryCde)
                .and(Field.grpMembrRecCde).is(orgnCde)
                .and(Field.cdvTypeCde).is(cdvTypeCde)
				.and(Field.cdvCde).is(cdvCde)
				.and(Field.cdvParntTypeCde).is(dataPrntTypeCde)
				.and(Field.cdvParntCde).is(dataPrntCde);
		try {
			List<ReferenceData> referenceDataList = referenceDataService.referenceDataByFilter(criteria.getCriteriaObject());
			if (!CollectionUtils.isEmpty(referenceDataList)) {
				return true;
			}
		} catch (Exception e) {
			throw new productBatchException(e);
		}
		return false;
	}

	protected EmailContent setMailObj(String subject, String add, String content) {
		EmailContent email = new EmailContent();
		email.setSubject(subject);
		email.setRecptAdr(add);
		email.setContent(content);
		return email;
	}

	protected String getcurTimeDDMMMYY() {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yy");
		return df.format(cal.getTime()) + " on "
				+ df2.format(cal.getTime());
	}

	protected List<String> getLanguageCdeFromProperties() {
		List<String> result = new ArrayList<>(10);
		String temp = "TW|EN|BL|ZH|JP|IN|PT|ES|FR";

		String[] str = temp.split("\\|");
		if (null != str && str.length > 0) {
			for (String s : str) {
				if (!StringUtils.isBlank(s)) {
					result.add(s.trim());
				}
			}
		}

		return result;
	}
	
	/**
	 * @param prodId
	 * @return
	 */
	protected boolean checkProdId(String prodId) {
		if (!chkLength(prodId, 30)) {
			errmsg = FinDocConstants.PROD_ID + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

}
