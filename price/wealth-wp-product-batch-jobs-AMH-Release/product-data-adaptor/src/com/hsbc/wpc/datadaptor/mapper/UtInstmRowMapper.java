package com.dummy.wpc.datadaptor.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdUserDefExtSeg;
import com.dummy.wpc.batch.xml.ProdUserDefOPExtSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.batch.xml.UtTrstInstmSeg;
import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import com.dummy.wpc.datadaptor.EntryPoint;
import com.dummy.wpc.datadaptor.reader.Sheet;
import com.dummy.wpc.datadaptor.util.DateHelper;

public class UtInstmRowMapper extends GnrcProdRowMapper {

    private static Logger log = Logger.getLogger(EntryPoint.class);
	
    @Override
    public MultiWriterObj mapRow(Sheet sheet, Map<String, String> mappings) throws Exception {
		
		 MultiWriterObj multiWriterObj = new MultiWriterObj();
		 //required fields
		 String prodCde=mappings.get("dummy Fund_Code");
		 String prodName=mappings.get("dummy_FundName_EN");
		 String rowIndex=mappings.get("RowIndex");
		 String emptyStr="";
			
	      UtTrstInstm utTrstInstm = new UtTrstInstm();
		 
           if (StringUtils.isBlank(prodCde)||StringUtils.isBlank(prodName)) {
     		
            String msg = "RowIndex "+rowIndex+"  Record item [productCode : "+prodCde+" prodName : "+prodName+"] Missing required fields,please check further.";
            UtInstmRowMapper.log.error(msg);
            TNGMessage.logTNGMsgExInfo("001", "E", TNGMsgConstants.SERVICE_NAME, msg);
            return multiWriterObj;
			}

			ProdKeySeg prodKey = new ProdKeySeg();
			prodKey.setCtryRecCde("HK");
			prodKey.setGrpMembrRecCde("HBAP");
			prodKey.setProdTypeCde("UT");
			prodKey.setProdCde(StringUtils.trimToEmpty(mappings.get("dummy Fund_Code")));
			prodKey.setProdCdeAltClassCde("P");
			utTrstInstm.setProdKeySeg(prodKey);
			
			ProdAltNumSeg prodAltNumSegP= new ProdAltNumSeg();
			prodAltNumSegP.setProdCdeAltClassCde("P");
			prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(mappings.get("dummy Fund_Code")));
			utTrstInstm.addProdAltNumSeg(prodAltNumSegP);
			
//			ProdAltNumSeg prodAltNumSegI= new ProdAltNumSeg();
//			prodAltNumSegI.setProdCdeAltClassCde("I");
//			prodAltNumSegI.setProdAltNum(StringUtils.trimToEmpty(mappings.get("dummy_ISIN")));
//			utTrstInstm.addProdAltNumSeg(prodAltNumSegI);
			
			ProdAltNumSeg prodAltNumSegO= new ProdAltNumSeg();
			prodAltNumSegO.setProdCdeAltClassCde("O");
			prodAltNumSegO.setProdAltNum(StringUtils.trimToEmpty(mappings.get("Mstar_PerformanceId")));
			utTrstInstm.addProdAltNumSeg(prodAltNumSegO);
			
			ProdInfoSeg prodInfo = new ProdInfoSeg();
			prodInfo.setProdSubtpCde(emptyStr);
			prodInfo.setProdName(StringUtils.trimToEmpty(mappings.get("dummy_FundName_EN")));
			prodInfo.setProdPllName(emptyStr);
			prodInfo.setProdSllName(emptyStr);
			prodInfo.setProdShrtName(emptyStr);
			prodInfo.setProdStatCde(emptyStr);
			prodInfo.setCcyProdCde(emptyStr);
			prodInfo.setMktInvstCde(emptyStr);
			prodInfo.setAllowBuyProdInd(emptyStr);
			prodInfo.setAllowSellProdInd(emptyStr);
			prodInfo.setAllowBuyUtProdInd(emptyStr);
			prodInfo.setAllowBuyAmtProdInd(emptyStr);
			prodInfo.setProdShrtName(emptyStr);
			prodInfo.setAllowSellUtProdInd(emptyStr);
			prodInfo.setAllowSellAmtProdInd(emptyStr);
			prodInfo.setAllowSellMipProdInd(emptyStr);
			prodInfo.setAllowSellMipUtProdInd(emptyStr);
			prodInfo.setAllowSellMipAmtProdInd(emptyStr);
			prodInfo.setAllowSwInProdInd(emptyStr);
			prodInfo.setAllowSwInUtProdInd(emptyStr);
			prodInfo.setAllowSwInAmtProdInd(emptyStr);
			prodInfo.setAllowSwOutProdInd(emptyStr);
			prodInfo.setAllowSwOutUtProdInd(emptyStr);
			prodInfo.setAllowSwOutAmtProdInd(emptyStr);
			prodInfo.setIncmCharProdInd(emptyStr);
			prodInfo.setCptlGurntProdInd(emptyStr);
			prodInfo.setYieldEnhnProdInd(emptyStr);
			prodInfo.setGrwthCharProdInd(emptyStr);
			prodInfo.setRtrnVoltlAvgPct(emptyStr);
			prodInfo.setDivrNum(emptyStr);
			prodInfo.setCtryProdTrade1Cde(emptyStr);
			prodInfo.setIntroProdCurrPrdInd(emptyStr);
			prodInfo.setCcyInvstCde(emptyStr);
			prodInfo.setQtyTypeCde(emptyStr);
			prodInfo.setProdLocCde(emptyStr);
			prodInfo.setSuptRcblCashProdInd(emptyStr);
			prodInfo.setSuptRcblScripProdInd(emptyStr);
			prodInfo.setDcmlPlaceTradeUnitNum(emptyStr);
			prodInfo.setAumChrgProdInd(emptyStr);
			prodInfo.setInvstImigProdInd(emptyStr);
			prodInfo.setRestrInvstrProdInd(emptyStr);
//			if("Y".equals(StringUtils.trimToEmpty(mappings.get("dummy_NoSubscriptionFeeSeries")))){
//				prodInfo.setNoScribFeeProdInd(StringUtils.trimToEmpty(mappings.get("dummy_NoSubscriptionFeeSeries")));
//			}else{
//				prodInfo.setNoScribFeeProdInd("N");
//			}
			prodInfo.setNoScribFeeProdInd(emptyStr);
			if(StringUtils.isNotBlank(mappings.get("dummy_Bestsellers"))){
				prodInfo.setTopSellProdInd("Y");
				prodInfo.setProdTopSellRankNum(StringUtils.trimToEmpty(mappings.get("dummy_Bestsellers")));
			}else{
				prodInfo.setTopSellProdInd("N");
				prodInfo.setProdTopSellRankNum(emptyStr);
			}
			prodInfo.setTopPerfmProdInd(emptyStr);
			
			List<ProdUserDefExtSeg> userExtFieldList = new ArrayList<ProdUserDefExtSeg>();
			userExtFieldList.add(extractUserDefExtField(mappings, "fundShreClsName", "dummy_FUNDCLASSCURRENCY_NAME_ENG"));
			userExtFieldList.add(extractUserDefExtField(mappings, "fundShreClsNamePriLang", "dummy_FUNDCLASSCURRENCY_NAME_CHT"));
			userExtFieldList.add(extractUserDefExtField(mappings, "fundShreClsNameSecLang", "dummy_FUNDCLASSCURRENCY_NAME_CHS"));
//			userExtFieldList.add(extractUserDefExtField(mappings, "priShareClassInd", "dummy_PrimaryShareClass"));
			prodInfo.setProdUserDefExtSeg(userExtFieldList.toArray(new ProdUserDefExtSeg[] {}));
				
			List<ProdUserDefOPExtSeg> userOPExtFieldList = new ArrayList<ProdUserDefOPExtSeg>();
			if(StringUtils.isNotBlank(mappings.get("Suppress UTB"))){
				userOPExtFieldList.add(extractUserDefOPExtField(mappings, "restrOnlScribInd", "Suppress UTB"));
				prodInfo.setProdUserDefOPExtSeg(userOPExtFieldList.toArray(new ProdUserDefOPExtSeg[] {}));
			}
			utTrstInstm.setProdInfoSeg(prodInfo);
			
			UtTrstInstmSeg utTrstInstmSeg = new UtTrstInstmSeg();
			utTrstInstmSeg.setFundHouseCde(emptyStr);
			utTrstInstmSeg.setCloseEndFundInd(emptyStr);
			utTrstInstmSeg.setInvstIncrmMinAmt(emptyStr);
			utTrstInstmSeg.setRdmMinAmt(emptyStr);
			utTrstInstmSeg.setUtRtainMinNum(emptyStr);
			utTrstInstmSeg.setFundRtainMinAmt(emptyStr);
			utTrstInstmSeg.setScribCtoffNextDtTm(emptyStr);
			utTrstInstmSeg.setRdmCtoffNextDtTm(emptyStr);
			utTrstInstmSeg.setFundClassCde(emptyStr);
			utTrstInstmSeg.setInsuLinkUtTrstInd(emptyStr);
			utTrstInstmSeg.setFundSwInMinAmt(emptyStr);
			utTrstInstmSeg.setFundSwOutMinAmt(emptyStr);
			utTrstInstmSeg.setFundSwOutRtainMinAmt(emptyStr);
			utTrstInstmSeg.setUtSwOutRtainMinNum(emptyStr);
			utTrstInstmSeg.setFundSwOutMaxAmt(emptyStr);
			utTrstInstmSeg.setTranMaxAmt(emptyStr);
			utTrstInstmSeg.setIncmHandlOptCde(emptyStr);
			utTrstInstmSeg.setDivYieldPct(emptyStr);
			utTrstInstm.setUtTrstInstmSeg(utTrstInstmSeg);
			
			RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
			String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
			String HKTimeZone="GMT+8";
			recDtTmSeg.setRecCreatDtTm(dateTime);
			recDtTmSeg.setRecUpdtDtTm(dateTime);
			recDtTmSeg.setProdPrcUpdtDtTm(dateTime);
			recDtTmSeg.setRecOnlnUpdtDtTm(dateTime);
			recDtTmSeg.setProdStatUpdtDtTm(dateTime);
			recDtTmSeg.setTimeZone(HKTimeZone);
			utTrstInstm.setRecDtTmSeg(recDtTmSeg);
    	    multiWriterObj.addObj(utTrstInstm);
	        return multiWriterObj;
	}
    
    /**
	 * @param ProdUserDefExtSeg fieldSet
	 * @return
	 */
	private ProdUserDefExtSeg extractUserDefExtField(Map<String, String> mappings, String fieldCde, String readStr) {
		ProdUserDefExtSeg prodUserDefExtSeg = new ProdUserDefExtSeg();
		prodUserDefExtSeg.setFieldCde(fieldCde);
		prodUserDefExtSeg.setFieldValue(StringUtils.trimToEmpty(mappings.get(readStr)));
		return prodUserDefExtSeg;
	}
	
	 /**
	 * @param ProdUserDefOPExtSeg fieldSet
	 * @return
	 */
	private ProdUserDefOPExtSeg extractUserDefOPExtField(Map<String, String> mappings, String fieldCde, String readStr) {
		ProdUserDefOPExtSeg prodUserDefOPExtSeg = new ProdUserDefOPExtSeg();
		prodUserDefOPExtSeg.setFieldCde(fieldCde);
		prodUserDefOPExtSeg.setFieldValue(StringUtils.trimToEmpty(mappings.get(readStr)));
		return prodUserDefOPExtSeg;
	}
    
}
