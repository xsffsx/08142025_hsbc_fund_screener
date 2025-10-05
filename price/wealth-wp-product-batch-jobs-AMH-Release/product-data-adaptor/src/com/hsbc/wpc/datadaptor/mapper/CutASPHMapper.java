package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class CutASPHMapper extends AbstractFieldSetMapper{
	
	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde")));
		
		ProdPrc prodPrc = new ProdPrc();

		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"));
		prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde"));
		prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_type_cde"));
		prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("FUND_CDE")));
		prodKey.setProdCdeAltClassCde("P");
		prodPrc.setProdKeySeg(prodKey);

		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRC_EFF_DT"),DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setPdcyPrcCde(fieldSet.readString("PRC_TYPE_CDE"));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRC_DT"),DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_PRC_CDE")));
		prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(fieldSet.readString("BID_PRC_AMT")));
		prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(fieldSet.readString("OFFER_PRC_AMT")));
		prodPrcSeg.setProdNavPrcAmt(DecimalHelper.trimZero(fieldSet.readString("NAV_PRC_AMT")));

		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);
		prodPrcSeg.setRecDtTmSeg(recDtTmSeg);
		
		prodPrc.addProdPrcSeg(prodPrcSeg);
		
		return prodPrc;
	}
	
}
