package com.dummy.wpb.product.validation;

import com.dummy.wpb.product.constant.EmailContent;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.RecordNotFoundException;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.ReferenceDataService;
import com.dummy.wpb.product.utils.FinDocUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class FinDocValidation {
	private static final Log log = LogFactory.getLog(FinDocValidation.class);
	protected String version;
	protected List<EmailContent> rejectRec;

	protected String errmsg = null;


	@Autowired
	private ReferenceDataService referenceDataService;

	FinDocUtils fdu = new FinDocUtils();

	protected abstract boolean validation() throws IOException;

	protected boolean checkCtryCde(String ctryCde) {
		if (!chkLength(ctryCde, 2)) {
			errmsg = FinDocConstants.CTRY_CDE + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkOrgnCde(String orgnCde) {
		if (!chkLength(orgnCde, 4)) {
			errmsg = FinDocConstants.ORGN_CDE + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkDocSubtpCde(String ctryCde, String orgnCde, String docSubtpCde) throws RecordNotFoundException {
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

	protected boolean checkLangCatCde(String langCatCde) {
		if (!chkLength(langCatCde, 2)) {
			errmsg = FinDocConstants.LANG + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}

		List<String> list = this.getLanguageCdeFromProperties();

		log.info("config language cde is:" + list +" ,the parm is:" +langCatCde);
		if (list.contains(langCatCde)) {
			return true;
		} else {
			errmsg = list + ":" + langCatCde;
			return false;
		}

	}


	protected boolean checkDocId(String docId) {
		if (!chkLength(docId, 30)) {
			errmsg = FinDocConstants.DOC_ID + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkFormtTypeCde(String formtTypeCde) {
		if (!chkLength(formtTypeCde, 3)) {
			errmsg = FinDocConstants.FORMAT + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		if (!fortmtList(formtTypeCde)) {
			errmsg = FinDocConstants.FORMAT + ":" + FinDocConstants.VAL_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkExpirDt(String expirDt) {
		if (!chkStr2Date(expirDt, 8)) {
			errmsg = FinDocConstants.EXPIRY_DT + ":"
					+ FinDocConstants.DT_TM_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkEffDt(String effDt) {
		// chkStr2Date allow null, must check mandatory first
		if (!chkStr2Date(effDt, 8)) {
			errmsg = FinDocConstants.EFF_DT + ":" + FinDocConstants.DT_TM_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkEffTm(String effTm) {
		if (!chkStr2Time(effTm, 6)) {
			errmsg = FinDocConstants.EFF_TM + ":" + FinDocConstants.DT_TM_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkDocExplnText(String docExplnText) {
		if (!chkLength(docExplnText, 200)) {
			errmsg = FinDocConstants.DOC_DESC + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkProdTypeCde(String ctryCde, String orgnCde,
			String prodTypeCde) throws RecordNotFoundException {
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

	protected boolean checkProdSubtpCde(String ctryCde, String orgnCde,
			String prodSubtpCde, String prodTypeCde) throws RecordNotFoundException {
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

	protected boolean checkProdCde(String prodCde) {
		if (!chkLength(prodCde, 30)) {
			errmsg = FinDocConstants.PROD_ID + ":" + FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkEmailAdrRpyText(String emailAdrRpyText) {
		if (version != null && version.equalsIgnoreCase(FinDocConstants.VER_1_1)
				&& (!chkMandatory((new String[] { emailAdrRpyText }), new String[] { FinDocConstants.RPY_EMAIL })))
			return false;
		if (!chkLength(emailAdrRpyText, 100)) {
			errmsg = FinDocConstants.RPY_EMAIL + ":"
					+ FinDocConstants.LEN_ERROR;
			return false;
		}
		return true;
	}

	protected boolean checkUrgInd(String urgInd) {
		if (!chkLength(urgInd, 1)) {
			errmsg = FinDocConstants.URG_IND + ":" + FinDocConstants.LEN_ERROR;
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

	protected boolean chkStr2Date(String str, int len) {
		if (str == null || str.equals(""))
			return true;
		if (str.length() != len)
			return false;
		return fdu.toDate(str) != null;
	}

	protected boolean chkStr2Time(String str, int len) {
		if (str.length() != len)
			return false;

		return fdu.toTime(str) != null;
	}


	protected boolean checkReferData(String ctryCde, String orgnCde,
			String cdvTypeCde, String cdvCde, String dataPrntTypeCde,
			String dataPrntCde) throws RecordNotFoundException {
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryCde)
                .and(Field.grpMembrRecCde).is(orgnCde)
                .and(Field.cdvTypeCde).is(cdvTypeCde)
				.and(Field.cdvCde).is(cdvCde);
		if (StringUtils.isNotBlank(dataPrntTypeCde) && StringUtils.isNotBlank(dataPrntCde)) {
			criteria.and(Field.cdvParntTypeCde).is(dataPrntTypeCde)
					.and(Field.cdvParntCde).is(dataPrntCde);
		}
		try {
			List<ReferenceData> referenceDataList = referenceDataService.referenceDataByFilter(criteria.getCriteriaObject());
			if (!CollectionUtils.isEmpty(referenceDataList)) {
				return true;
			}
		} catch (Exception e) {
			throw new RecordNotFoundException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected boolean fortmtList(String formtTypeCde) {
		List<String> list = new ArrayList<>();

		list.add(FinDocConstants.FMT_PDF);
		list.add(FinDocConstants.FMT_DOC);
		list.add(FinDocConstants.FMT_XLS);
		list.add(FinDocConstants.FMT_TIF);
		list.add(FinDocConstants.FMT_TIFF);
		list.add(FinDocConstants.FMT_GIF);
		list.add(FinDocConstants.FMT_JPEG);
		list.add(FinDocConstants.FMT_JPG);
		list.add(FinDocConstants.FMT_PPT);
		list.add(FinDocConstants.FMT_MP3);
		list.add(FinDocConstants.FMT_MP4);
		list.add(FinDocConstants.FMT_ZIP);
		list.add(FinDocConstants.FMT_TXT);
		list.add(FinDocConstants.FMT_VSD);
		list.add(FinDocConstants.FMT_HTML);
		list.add(FinDocConstants.FMT_XML);

		return list.contains(formtTypeCde);
	}


	private List<String> getLanguageCdeFromProperties() {
		List<String> result = new ArrayList<>(10);

		String temp = "TW|EN|BL|ZH|JP|IN|PT|ES|FR";
		String[] str = temp.split("\\|");
		for (String s : str) {
			if (!StringUtils.isBlank(s)) {
				result.add(s.trim());
			}
		}
		return result;
	}

}
