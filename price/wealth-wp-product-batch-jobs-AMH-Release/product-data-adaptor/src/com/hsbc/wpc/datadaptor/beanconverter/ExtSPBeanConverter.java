package com.dummy.wpc.datadaptor.beanconverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.extSP.ComplexElementTypeChoiceItem;
import com.dummy.wpc.batch.extSP.ComplexElementTypeItem;
import com.dummy.wpc.batch.extSP.CplxElt;
import com.dummy.wpc.batch.extSP.SectionsTypeItem;
import com.dummy.wpc.batch.extSP.TemplatED;
import com.dummy.wpc.batch.extSP.TemplateSections;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdProdRelSeg;
import com.dummy.wpc.batch.xml.ProdProdReln;
import com.dummy.wpc.batch.xml.ProdUserDefExtSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.Sid;
import com.dummy.wpc.batch.xml.SidSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;

public class ExtSPBeanConverter extends AbstractBeanConverter {
	
	private String ctryCde;
	private String orgnCde;
	private String emptyStr;
	private String blank = " ";
	private String asian = "AsianGrowthUpOut";
	private String rainbow = "RainbowGrowth";
	private String digit = "DigitalCoupon";
	private String easy = "AutocallEasyTouch";
	private String max = "Autocall MaxMinCoupon";
	private String growth = "AutocallGrowth"	;
	private String daily = "DailyAutocallable"	;
	private String knock = "KnockOutStraddle"	;
	private String cfa = "CFA";
	private static final String gh ="Growth";
	private String none ="None (1/Strike Put)";
	private String cfa_value ="True";
	
	
	private String CURR_B ="B";
	private String CURR_E ="E";
	

	@Override
	public Object convert(Object source) {
		
		this.ctryCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CTRY_REC_CDE);
        this.orgnCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRP_MEMBR_REC_CDE);
        this.emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.EMPTY_STR);

		TemplatED extSP = (TemplatED) source;
		MultiWriterObj multiWriterObj = new MultiWriterObj();

		Sid sid = new Sid();

        // ProdAltNumSeg
		ProdAltNumSeg[]  alts = new ProdAltNumSeg[2];
        ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
        prodAltNumSeg_P.setProdCdeAltClassCde("P");
        prodAltNumSeg_P.setProdAltNum(getValue(extSP,"Ids.ClientSystemReference",false));       
        alts[0] = prodAltNumSeg_P;
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde("M");
        prodAltNumSeg_M.setProdAltNum(getValue(extSP,"Ids.ClientSystemReference",false));

        alts[1] = prodAltNumSeg_M;
        sid.setProdAltNumSeg(alts);
		
		ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(this.ctryCde);
        prodKey.setGrpMembrRecCde(this.orgnCde);
        prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_TYPE_CDE));
        prodKey.setProdCde(prodAltNumSeg_P.getProdAltNum());
        
        sid.setProdKeySeg(prodKey);
        
        RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
        recDtTmSeg.setTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.TIME_ZONE));
        recDtTmSeg.setRecCreatDtTm(emptyStr);
        recDtTmSeg.setRecUpdtDtTm(emptyStr);
        sid.setRecDtTmSeg(recDtTmSeg);
        
        sid.setProdInfoSeg(convertProdInfoSeg(extSP));
              
        sid.setSidSeg(convertSidSeg(extSP));
        
        String tpl = extSP.getTplName();
           		
		addProdUserExtField(sid,"payoffTypeCde",extSP.getTplName());		
		addProdUserExtField(sid,"trancheCde",getValue(extSP,"TradeInfo.BusinessDescription.TrancheType",false));						
		addProdUserExtField(sid,"spTrdDt",convertDay(getValue(extSP,"TradeInfo.TradeDate",false),1));
		addProdUserExtField(sid,"spStartDt",convertDay(getValue(extSP,"QuoteDescription.Dates.IssueDate",false),1));
		addProdUserExtField(sid,"spStrkDt",convertDay(getValue(extSP,"QuoteDescription.Dates.StrikeDate",false),1));
		addProdUserExtField(sid,"spValnDt",convertDay(getValue(extSP,"QuoteDescription.Dates.FinalValuationDate",false),1));		
		addProdUserExtField(sid,"trancheMaxAmt",getValue(extSP,"TradeInfo.BusinessDescription.MaxTrancheAmount",false));
		addProdUserExtField(sid,"trancheMinAmt",getValue(extSP,"TradeInfo.BusinessDescription.MinTrancheAmount",false));	
		
		//get from default value
		addProdUserExtField(sid,"unwindStartTm",getValue(extSP,"TradeInfo.BusinessDescription.Unwind.Startdate",false));
		addProdUserExtField(sid,"unwindAllow",getValue(extSP,"TradeInfo.BusinessDescription.Unwind.Allowable",false));
		addProdUserExtField(sid,"termRef",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.TERM_REF));	
		addProdUserExtField(sid,"bookBillingTypeCde",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.BOOK_BILLING_TYPE_CDE));	
		addProdUserExtField(sid,"withInterimCoupnInd",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.WITH_INTERIM_COUPN_IND));	
		addProdUserExtField(sid,"overdueCoupnRatePct",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.OVERDUE_COUPN_RATE_PCT));	
		addProdUserExtField(sid,"rateMktPrd",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.RATE_MKT_PRD));			
		addProdUserExtField(sid,"bonusRate",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.BONUS_RATE));			
		addProdUserExtField(sid,"accrRange",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ACCR_RANGE));
		addProdUserExtField(sid,"accrRatePct",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ACCR_RATE_PCT));
		addProdUserExtField(sid,"gurntRatePct",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ACCR_RATE_PCT));		
		addProdUserExtField(sid,"preDtrmCoupnRate",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PRE_DTRM_COUPN_RATE));
		addProdUserExtField(sid,"invstIncrmMinAmt",ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INVST_INCRM_MIN_AMT));
																		
		String currency  = getValue(extSP,"QuoteDescription.Currency",false);
		String currStr_b = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.COUPN_CAL_TYP_CDE_B);
		String currStr_e = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.COUPN_CAL_TYP_CDE_E);
				
		if(checkCurrency(currStr_b,currency)){
			addProdUserExtField(sid,"coupnCalTypCde",CURR_B);	
		} else if(checkCurrency(currStr_e,currency)){
			addProdUserExtField(sid,"coupnCalTypCde",CURR_E);	
		}
								
		String isAutocall = getValue(extSP,"QuoteDescription.PayoffSpecifics.#IsAutocall#",false);
		
		String start = convertDay(getValue(extSP,"TradeInfo.BusinessDescription.Unwind.StartDate",false),1);
				
		String allowable =  getValue(extSP,"TradeInfo.BusinessDescription.Unwind.Allowable",false);				
		if(StringUtils.isNotBlank(start) && !"False".equalsIgnoreCase(allowable)){			
			addProdUserExtField(sid,"unwindStartTm",start + blank + ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.START_TIME));
		}		
		if(!"False".equalsIgnoreCase(allowable)){			
			addProdUserExtField(sid,"unwindCde",getValue(extSP,"TradeInfo.BusinessDescription.Unwind.Frequency",false));
		}
		
		
		if(tpl.equals(asian) || tpl.equals(rainbow) || tpl.equals(knock) || gh.equals(tpl)){
			addProdUserExtField(sid,"participationRatePct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#Leverage#",false));
		} else if(tpl.equals(growth)){
			addProdUserExtField(sid,"participationRatePct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#UpsideLeverage#",false));
		}
		

		if(tpl.equals(cfa)){
			addProdUserExtField(sid,"maxAccruPayoutPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponMax#",false));
		}
				
										
		String outRebate = getValue(extSP,"QuoteDescription.PayoffSpecifics.#OutRebate#",false);
		String minReturn = getValue(extSP,"QuoteDescription.PayoffSpecifics.#MinReturn#",false);
		
		//potentCoupnPct
		if(tpl.equals(asian)){	
			//int total = Integer.parseInt(outRebate) + Integer.parseInt(minReturn);
			BigDecimal outRebateDeci  =  new  BigDecimal(outRebate);
			BigDecimal minReturnDeci  =  new  BigDecimal(minReturn);
			BigDecimal total = outRebateDeci.add(minReturnDeci) ;				
			addProdUserExtField(sid,"potentCoupnPct",String.valueOf(total));			
		} else if (tpl.equals(digit) || tpl.equals(max)) {
			addProdUserExtField(sid,"potentCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponMax#",false));			
		} else if (tpl.equals(easy) || tpl.equals(growth) || tpl.equals(daily)) {
			addProdUserExtField(sid,"potentCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#Rebate#",false));			
		} else if (tpl.equals(knock)){
			addProdUserExtField(sid,"potentCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#OutRebate#",false));						
		}
		
		//minCoupnPct
		if (tpl.equals(asian) ){
			addProdUserExtField(sid,"minCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#MinReturn#",false));			
		} else if ( tpl.equals(digit) || tpl.equals(max)  ){
			addProdUserExtField(sid,"minCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponMin#",false));			
		} else if (tpl.equals(rainbow)){
			addProdUserExtField(sid,"minCoupnPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#MinReturn#",false));
		}
				
		//spCoupnPymtCde
		if (tpl.equals(digit) || tpl.equals(max) || tpl.equals(cfa) || gh.equals(tpl)){
			addProdUserExtField(sid,"spCoupnPymtNum",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponFrequency#",false));
			addProdUserExtField(sid,"spCoupnPymtCde","M");			
			
		}
		
		
		//floorLvlPct
		if (tpl.equals(knock)){
			addProdUserExtField(sid,"floorLvlPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#DownOutLevel#",false));						
		} else if (tpl.equals(cfa)){
			addProdUserExtField(sid,"floorLvlPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponBarrier#",false));
		}
		
		//capLvlPct
		if (tpl.equals(knock)){			
			addProdUserExtField(sid,"capLvlPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#UpOutLevel#",false));						
		}
		
		//spKnockInCde 				
		String kiType = getValue(extSP,"QuoteDescription.PayoffSpecifics.#KIType#",false);         //   would BA confirm ?????  this should be a key , not  Frequency (Months)
		String kiFreq = getValue(extSP,"QuoteDescription.PayoffSpecifics.#KIFrequency#",false);		
		String kiLevel = getValue(extSP,"QuoteDescription.PayoffSpecifics.#KILevel#",false);
		
		if(this.none.equals(kiType)){
			kiFreq = null;
			kiLevel = null;
		}
		
		if (tpl.equals(digit) || tpl.equals(easy) || tpl.equals(max) || tpl.equals(growth)|| tpl.equals(cfa)){       
			addProdUserExtField(sid,"spKnockInCde",kiType);
			//If "Knock-in Frequency Type" == "Daily", then "Knock-in Frequency"  = 1
			if ("Daily".equals(kiType)){
				kiFreq = "1";
			}
			addProdUserExtField(sid,"spKnockInNum",kiFreq);	
		}
		
		//knockInPrcPct 
		if (tpl.equals(digit) || tpl.equals(easy) || tpl.equals(max) || tpl.equals(growth)|| tpl.equals(cfa)){
			addProdUserExtField(sid,"knockInPrcPct",kiLevel);				
		}
		
		
		//spKnockOutCde 
		if (tpl.equals(easy) || tpl.equals(max)  || tpl.equals(growth)){
			addProdUserExtField(sid,"spKnockOutNum",getValue(extSP,"QuoteDescription.PayoffSpecifics.#KOFrequency#",false));
			addProdUserExtField(sid,"spKnockOutCde","M");					
		} else if (tpl.equals(daily) ) {
			addProdUserExtField(sid,"spKnockOutNum",getValue(extSP,"QuoteDescription.PayoffSpecifics.#KOFrequency#",false));
			addProdUserExtField(sid,"spKnockOutCde","D");	
		} else if (tpl.equals(cfa)  && cfa_value.equalsIgnoreCase(isAutocall)){
			addProdUserExtField(sid,"spKnockOutNum",getValue(extSP,"QuoteDescription.PayoffSpecifics.#KOFrequency#",false));
			addProdUserExtField(sid,"spKnockOutCde","M");	
		}
		
		//knockOutPrcPct
		if (tpl.equals(easy) || tpl.equals(max)  || tpl.equals(growth) || tpl.equals(daily) ||
				(tpl.equals(cfa) && cfa_value.equalsIgnoreCase(isAutocall))){
			addProdUserExtField(sid,"knockOutPrcPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#KOLevel#",false));					
		}
		
		//spBarrierCde    
		if (tpl.equals(asian) || tpl.equals(knock)){
			String outType = getValue(extSP,"QuoteDescription.PayoffSpecifics.#OutType#",false);
			String outFreq = getValue(extSP,"QuoteDescription.PayoffSpecifics.#OutFrequency#",false);
			addProdUserExtField(sid,"spBarrierCde",outType);
			//'If "Barrier Frequency Type" == "Daily", then "Barrier Frequency"  = 1
			if("Daily".equals(outType)){
				outFreq = "1";
			}
			addProdUserExtField(sid,"spBarrierNum",outFreq);								
		} else if (tpl.equals(digit) || tpl.equals(max)){			
			String coupType = "Frequency (Months)";
			String coupFreq = getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponFrequency#",false);
			addProdUserExtField(sid,"spBarrierCde",coupType);	
			addProdUserExtField(sid,"spBarrierNum",coupFreq);
		}

		//barrierPrcPct 
		if (tpl.equals(asian)){
			addProdUserExtField(sid,"barrierPrcPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#OutLevel#",false));							
		} else if (tpl.equals(digit) ||  tpl.equals(max) ){
			addProdUserExtField(sid,"barrierPrcPct",getValue(extSP,"QuoteDescription.PayoffSpecifics.#CouponBarrier#",false));							
		}
		
		//autoCallableInd 
		if (tpl.equals(cfa)){
			addProdUserExtField(sid,"autoCallableInd",isAutocall.equalsIgnoreCase("true")?"Y":"N");							
		} else if (tpl.equals(asian) || tpl.equals(rainbow) || tpl.equals(digit) || tpl.equals(knock) || gh.equals(tpl) ){
			addProdUserExtField(sid,"autoCallableInd","N");
		}  else {
			addProdUserExtField(sid,"autoCallableInd","Y");
		}
		
		if (tpl.equals(easy) || tpl.equals(max) || tpl.equals(growth) || tpl.equals(daily) ||
				(tpl.equals(cfa) && cfa_value.equalsIgnoreCase(isAutocall)) ){			
			String koFre = getValue(extSP,"QuoteDescription.PayoffSpecifics.#KOFrequency#",false);
			String nonCall = getValue(extSP,"QuoteDescription.PayoffSpecifics.#NonCallPeriod#",false);			
			if(StringUtils.isNotBlank(koFre) && StringUtils.isNotBlank(nonCall)){
				int total  = Integer.parseInt(koFre) * Integer.parseInt(nonCall) ;
				addProdUserExtField(sid,"nonCallPrdNum",String.valueOf(total));	
				addProdUserExtField(sid,"nonCallPrdCde","M");	
			}											
		}
		
		//coupnTypeCde 
		addProdUserExtField(sid,"coupnTypeCde","F");	
		
		
		// participationRtSp
		if (tpl.equals(asian) || tpl.equals(this.knock) || tpl.equals(growth) || tpl.equals(rainbow)) {
			addProdUserExtField(sid, "participationRtSp", "30");
		}

		// potentialCpPercentSp
		if (tpl.equals(asian) || tpl.equals(this.knock) || tpl.equals(easy) || tpl.equals(max) || tpl.equals(daily) || tpl.equals(digit)) {
			addProdUserExtField(sid, "potentialCpPercentSp", "5");
		}

		// maxAccruPaySp
		if (tpl.equals(cfa)) {
			addProdUserExtField(sid, "maxAccruPaySp", "3");
		}

		// knockInLvlSp
		if (tpl.equals(easy) || tpl.equals(growth) || tpl.equals(max)) {
			addProdUserExtField(sid, "knockInLvlSp", "10");
		}
		
										
		if (tpl.equals(digit) || tpl.equals(max)|| tpl.equals(cfa) || gh.equals(tpl)){
			addProdUserExtField(sid,"coupnPayFirstDt", convertDay(getValue(extSP,"QuoteDescription.PayoffSpecifics.#FirstCouponPaymentDate#",false),1));					
		}
		if (tpl.equals(rainbow)){
			addProdUserExtField(sid,"rainbowWeight", getValue(extSP,"QuoteDescription.PayoffSpecifics.#RainbowWeights#",false));					
		}
		if (tpl.equals(asian) || tpl.equals(rainbow)){
			addProdUserExtField(sid,"observNum",getValue(extSP,"QuoteDescription.PayoffSpecifics.#AsianFrequency#",false));
			addProdUserExtField(sid,"observCde","M");
		}  
		ProdProdReln reln = new ProdProdReln();
		
		List<String> prodRel = getComplexValue(extSP,"QuoteDescription.Underlyings",true);
		
		if(CollectionUtils.isNotEmpty(prodRel)){
			reln.setProdAltNum(getValue(extSP,"Ids.ClientSystemReference",false));    //p CODE	
			reln.setCtryRecCde(ctryCde);
			reln.setGrpMembrRecCde(orgnCde);
			reln.setProdCdeAltClassCde("Y");
			ProdProdRelSeg[] prodProdRelSeg = new ProdProdRelSeg[prodRel.size()];
			for(int i=0; i< prodRel.size(); i++){								
				ProdProdRelSeg ProdProdRel = new ProdProdRelSeg();
				ProdProdRel.setProdAltNumRel(prodRel.get(i));
				ProdProdRel.setProdProdRelCde("UNDL");
				prodProdRelSeg[i] = ProdProdRel;
				reln.setProdProdRelSeg(prodProdRelSeg);
			}			
		}
						
		multiWriterObj.addObj(sid);
		multiWriterObj.addObj(reln);
		
		return multiWriterObj;
	}
	
	
	private String getValue(TemplatED extSP, String fullPath,boolean isComplexType){
		
		List<String> result = findNodeValue(extSP,fullPath,isComplexType);
		if(CollectionUtils.isEmpty(result)){
			return null;
		} else {
			return result.get(0);
		}		
	}
	
	
	private List<String> getComplexValue(TemplatED extSP, String fullPath,boolean isComplexType){
		
		List<String> result = findNodeValue(extSP,fullPath,isComplexType);
		if(CollectionUtils.isEmpty(result)){
			return null;
		} else {
			return result;
		}		
	}
	
	
	
	private void addProdUserExtField(Sid sid,String FieldCde,String value){
		if(StringUtils.isNotBlank(value)){
			ProdUserDefExtSeg extField = new ProdUserDefExtSeg();
			extField.setFieldCde(FieldCde);
			extField.setFieldValue(value);
			sid.getProdInfoSeg().addProdUserDefExtSeg(extField);
		}		
	}
	
	
 
	private String convertDay(String value,int format){
		if(StringUtils.isBlank(value)){
			return null;
		}	
		
		Calendar cal = Calendar.getInstance();
		cal.set(1900,0,1);
		//cal.add(Calendar.DAY_OF_MONTH,Integer.parseInt(value));
		int count  = Integer.parseInt(value) -2 ;   // because GMXML using excel to count day ,it has bug , need add 2 days  to patch. (bugs are : (1) count from 1899-12-31  (2)treating 1900 as leap year)
		
		cal.add(Calendar.DAY_OF_MONTH,count);
		
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) +1;
		int year = cal.get(Calendar.YEAR);	
		if(format==1){
			return day+"/"+month+"/"+year;
		} else {
			return year+"-"+month+"-"+day;
		}
		
	}

	/**
	 * provide the full path to get <elt> tag value
     * @param extSP    : extend Structure product object
     * @param fullPath : full path . start from <section> end with <elt>
	 * @param isComplexType :identify the target node is complex type or not . if target node is complex type  then return all child elements
	 * @return
	 */
	private static List<String> findNodeValue(TemplatED extSP, String fullPath, boolean isComplex) {
		TemplateSections sections = extSP.getTemplateSections();

		String[] nodes = fullPath.split("\\.");
		String sectionKey = nodes[0];
		String eltName = nodes[nodes.length - 1];

		for (int i = 1; i < nodes.length; i++) {
			for (SectionsTypeItem item : sections.getSectionsTypeItem()) {
				if (item.getSection().getKey().equals(sectionKey)) { // section
					for (ComplexElementTypeItem cet : item.getSection().getComplexElementTypeItem()) {
						for (ComplexElementTypeChoiceItem choice : cet.getComplexElementTypeChoice().getComplexElementTypeChoiceItem()) {
							
							if(isComplex){
								if(choice.getCplxElt() != null && choice.getCplxElt().getKey().equals(eltName)){
									List<String> result = new ArrayList<String>();									
									for (ComplexElementTypeItem cetIt : choice.getCplxElt().getComplexElementTypeItem()) {
										for (ComplexElementTypeChoiceItem choiceIt : cetIt.getComplexElementTypeChoice().getComplexElementTypeChoiceItem()) {
											if (choiceIt.getElt() != null && StringUtils.isNotBlank(choiceIt.getElt().getContent())) {
												result.add(choiceIt.getElt().getContent());
											}
											
											//add for iElt
											if (choiceIt.getIElt() != null && StringUtils.isNotBlank(choiceIt.getIElt().getContent())) {
												result.add(choiceIt.getIElt().getContent());
											}
											//add for sElt   
											if (choiceIt.getSElt() != null && StringUtils.isNotBlank(choiceIt.getSElt().getContent())) {
												result.add(choiceIt.getSElt().getContent());
											}
											//add for RElt     
											if (choiceIt.getRElt() != null && StringUtils.isNotBlank(choiceIt.getRElt().getContent())) {
												result.add(choiceIt.getRElt().getContent());
											}
											//add for  fElt  
											if (choiceIt.getFElt() != null && StringUtils.isNotBlank(choiceIt.getFElt().getContent())) {
												result.add(choiceIt.getFElt().getContent());
											}
										}
									}
									
									return result;
								}
								
								if (loopFind(choice.getCplxElt(), eltName,isComplex) != null) {
									return loopFind(choice.getCplxElt(), eltName , isComplex);
								}
								
								
							} else {
								if (choice.getElt() != null && choice.getElt().getKey().equals(eltName)) {
									List<String> result = new ArrayList<String>();
									result.add(choice.getElt().getContent());
									return result;
								}
								
								//add for iElt
								if (choice.getIElt() != null && choice.getIElt().getKey().equals(eltName)) {
									List<String> result = new ArrayList<String>();
									result.add(choice.getIElt().getContent());
									return result;
								}
								
								//add for SElt
								if (choice.getSElt() != null && choice.getSElt().getKey().equals(eltName)) {
									List<String> result = new ArrayList<String>();
									result.add(choice.getSElt().getContent());
									return result;
								}
								//add for FElt
								if (choice.getFElt() != null && choice.getFElt().getKey().equals(eltName)) {
									List<String> result = new ArrayList<String>();
									result.add(choice.getFElt().getContent());
									return result;
								}
								//add for RElt
								if (choice.getRElt() != null && choice.getRElt().getKey().equals(eltName)) {
									List<String> result = new ArrayList<String>();
									result.add(choice.getRElt().getContent());
									return result;
								}
								if (loopFind(choice.getCplxElt(), eltName,isComplex) != null) {
									return loopFind(choice.getCplxElt(), eltName,isComplex);
								}
							}
							
						}
					}
				}
			}
		}
		return null;
	}

	private static List<String> loopFind(CplxElt cplxElt, String eltName,boolean isComplex) {
		if (!isComplex && cplxElt != null ) {
			for (ComplexElementTypeItem cet : cplxElt.getComplexElementTypeItem()) {
				for (ComplexElementTypeChoiceItem choice : cet.getComplexElementTypeChoice().getComplexElementTypeChoiceItem()) {

					if (choice.getElt() != null && choice.getElt().getKey().equals(eltName)) {
						List<String> result = new ArrayList<String>();
						result.add(choice.getElt().getContent());
						return result;
					}
					
					// add for ielt
					if (choice.getIElt() != null && choice.getIElt().getKey().equals(eltName)) {
						List<String> result = new ArrayList<String>();
						result.add(choice.getIElt().getContent());
						return result;
					}
					
					// add for SElt
					if (choice.getSElt() != null && choice.getSElt().getKey().equals(eltName)) {
						List<String> result = new ArrayList<String>();
						result.add(choice.getSElt().getContent());
						return result;
					}
					// add for RElt
					if (choice.getRElt() != null && choice.getRElt().getKey().equals(eltName)) {
						List<String> result = new ArrayList<String>();
						result.add(choice.getRElt().getContent());
						return result;
					}
					// add for FElt
					if (choice.getFElt() != null && choice.getFElt().getKey().equals(eltName)) {
						List<String> result = new ArrayList<String>();
						result.add(choice.getFElt().getContent());
						return result;
					}

					if (choice.getCplxElt() != null && choice.getCplxElt().getComplexElementTypeItemCount() > 0) {
						if (CollectionUtils.isNotEmpty(loopFind(choice.getCplxElt(), eltName , isComplex))) {
							return loopFind(choice.getCplxElt(), eltName ,isComplex);
						}
					}
				}
			}
		}
		
		if (isComplex && cplxElt != null ) {
			for (ComplexElementTypeItem cet : cplxElt.getComplexElementTypeItem()) {
				for (ComplexElementTypeChoiceItem choice : cet.getComplexElementTypeChoice().getComplexElementTypeChoiceItem()) {
					if (choice.getCplxElt() != null && choice.getCplxElt().getComplexElementTypeItemCount() > 0) {
						
						if (choice.getCplxElt().getKey().equals(eltName)) {
							List<String> result = new ArrayList<String>();									
							for (ComplexElementTypeItem cetIt : choice.getCplxElt().getComplexElementTypeItem()) {
								for (ComplexElementTypeChoiceItem choiceIt : cetIt.getComplexElementTypeChoice().getComplexElementTypeChoiceItem()) {
									if (choiceIt.getElt() != null && StringUtils.isNotBlank(choiceIt.getElt().getContent())) {
										result.add(choiceIt.getElt().getContent());
									}
									
									
									//add for Ielt
									if (choiceIt.getIElt() != null && StringUtils.isNotBlank(choiceIt.getIElt().getContent())) {
										result.add(choiceIt.getIElt().getContent());
									}
									
									//add for SElt
									if (choiceIt.getSElt() != null && StringUtils.isNotBlank(choiceIt.getSElt().getContent())) {
										result.add(choiceIt.getSElt().getContent());
									}
									
									//add for RElt
									if (choiceIt.getRElt() != null && StringUtils.isNotBlank(choiceIt.getRElt().getContent())) {
										result.add(choiceIt.getRElt().getContent());
									}
									
									//add for FElt
									if (choiceIt.getFElt() != null && StringUtils.isNotBlank(choiceIt.getFElt().getContent())) {
										result.add(choiceIt.getFElt().getContent());
									}
								}
							}							
							return result;
						}
												
						if (CollectionUtils.isNotEmpty(loopFind(choice.getCplxElt(), eltName , isComplex))) {
							return loopFind(choice.getCplxElt(), eltName ,isComplex);
						}
					}
				}
			}
		}
						
		return null;
	}
	
	private SidSeg convertSidSeg(TemplatED extSP){
		SidSeg sidSeg = new SidSeg();		
		/*if(gh.equals(extSP.getTplName())){
			sidSeg.setCptlProtcPct(getValue(extSP,"QuoteDescription.PayoffSpecifics.#ProtectionLevel#",false));
			
		}else{
			sidSeg.setCptlProtcPct(getValue(extSP,"QuoteDescription.PayoffSpecifics.#Protection#",false));
		}*/
		sidSeg.setCptlProtcPct(getValue(extSP,"QuoteDescription.PayoffSpecifics.#Protection#",false));
		sidSeg.setMktStartDt(convertDay(getValue(extSP,"TradeInfo.BusinessDescription.Subscription.From",false),2));
		sidSeg.setMktEndDt(convertDay(getValue(extSP,"TradeInfo.BusinessDescription.Subscription.To",false),2));
		sidSeg.setLnchProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.LNCH_PROD_IND));
		sidSeg.setProdConvCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_CONV_CDE));
		
		sidSeg.setRtrvProdExtnlInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.RTRV_PROD_EXTNL_IND));
		
		String allowable =  getValue(extSP,"TradeInfo.BusinessDescription.Unwind.Allowable",false);
		
		if("False".equals(allowable)){
			sidSeg.setAllowEarlyRdmInd("N");  	
		} else if ("True".equals(allowable)){
			sidSeg.setAllowEarlyRdmInd("Y");  	
		}
		
		return sidSeg;
	}
	
		
	private ProdInfoSeg convertProdInfoSeg(TemplatED extSP) {
		
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
				
		prodInfoSeg.setMktInvstCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MKT_INVST_CDE));
		prodInfoSeg.setAllowBuyProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_PROD_IND));
		prodInfoSeg.setAllowBuyUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_UT_PROD_IND));
		prodInfoSeg.setAllowBuyAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_AMT_PROD_IND));
		prodInfoSeg.setAllowSellProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_PROD_IND));
		prodInfoSeg.setAllowSellUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_UT_PROD_IND));
		prodInfoSeg.setAllowSellAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_AMT_PROD_IND));
		prodInfoSeg.setAllowSellMipProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.ALLOW_SELL_MIP_PROD_IND));
		prodInfoSeg.setAllowSellMipUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.ALLOW_SELL_MIP_UT_PROD_IND));
		prodInfoSeg.setAllowSellMipAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));		
		prodInfoSeg.setAllowSwInProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_PROD_IND));
		prodInfoSeg.setAllowSwInUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_UT_PROD_IND));
		prodInfoSeg.setAllowSwInAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_AMT_PROD_IND));
		prodInfoSeg.setAllowSwOutProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_OUT_PROD_IND));
		prodInfoSeg.setAllowSwOutUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_OUT_UT_PROD_IND));
		prodInfoSeg.setAllowSwOutAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_OUT_AMT_PROD_IND));
		prodInfoSeg.setIncmCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INCM_CHAR_PROD_IND));
		prodInfoSeg.setCptlGurntProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CPTL_GURNT_PROD_IND));
		prodInfoSeg.setGrwthCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRWTH_CHAR_PROD_IND));
		prodInfoSeg.setPrtyProdSrchRsultNum(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PRTY_PROD_SRCH_RSULT_NUM));
		prodInfoSeg.setAvailMktInfoInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.AVAIL_MKT_INFO_IND));
		prodInfoSeg.setDmyProdSubtpRecInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DMY_PROD_SUBTP_REC_IND));
		prodInfoSeg.setDispComProdSrchInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DISP_COM_PROD_SRCH_IND));
		prodInfoSeg.setMrkToMktInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MRK_TO_MKT_IND));		
		prodInfoSeg.setDivrNum(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DIVR_NUM));		
		prodInfoSeg.setProdSubtpCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_SUB_TP_CDE));		
		prodInfoSeg.setProdStatCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_STAT_CDE));		
		prodInfoSeg.setProdName(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_NAME));
		prodInfoSeg.setProdPllName(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_PLL_NAME));		
		prodInfoSeg.setDcmlPlaceTradeUnitNum(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DCML_PLACE_TRADE_UNIT_NUM));
		prodInfoSeg.setProdLocCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_LOC_CDE));		
		prodInfoSeg.setSuptRcblCashProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.SUPT_RCBL_CASH_PROD_IND));	
		prodInfoSeg.setSuptRcblScripProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.SUPT_RCBL_SCRIP_PROD_IND));		
		prodInfoSeg.setCtryProdTrade1Cde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CTRY_PROD_TRADE_1_CDE));		
		prodInfoSeg.setRiskLvlCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.RISK_LVL_CDE));		
		prodInfoSeg.setQtyTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.QTY_TYPE_CDE));
				
		//format the "Capital Protection Indicator" when process "Capital Protected Level". 
		//If "Captital Protected Level" = 100, then "Capital Protection Indicator" = 'Y'; Else "Capital Protection Indicator" = 'N'
		String protection  = getValue(extSP,"QuoteDescription.PayoffSpecifics.#Protection#",false);
		if(StringUtils.isNotBlank(protection) && Integer.parseInt(protection)==100){
			prodInfoSeg.setCptlGurntProdInd("Y");
		} else {
			prodInfoSeg.setCptlGurntProdInd("N");
		}
		
						
		prodInfoSeg.setTrdFirstDt(convertDay(getValue(extSP,"TradeInfo.BusinessDescription.Subscription.From",false),2));  
			     		
		String currency  = getValue(extSP,"QuoteDescription.Currency",false);		
		String prdProd = getValue(extSP,"QuoteDescription.Dates.Maturity",false);
		
		if(prdProd != null){	
			prodInfoSeg.setPrdProdCde("M");
			if(prdProd.endsWith("Y")){						
				prodInfoSeg.setPrdProdNum(String.valueOf(Integer.parseInt(prdProd.replace("Y", ""))*12));	
				prodInfoSeg.setPrdInvstTnorNum(String.valueOf(Integer.parseInt(prdProd.replace("Y", ""))*12));     // need further confirm
			}else{
				prodInfoSeg.setPrdProdNum(prdProd.replace("M", ""));
				prodInfoSeg.setPrdInvstTnorNum(prdProd.replace("M", ""));                                          // need further confirm
			}			
		}
		
		prodInfoSeg.setProdMturDt(convertDay(getValue(extSP,"QuoteDescription.Dates.MaturityDate",false),2));		
		prodInfoSeg.setProdLnchDt(convertDay(getValue(extSP,"TradeInfo.BusinessDescription.Subscription.To",false),2));
								
		prodInfoSeg.setProdShrtName(emptyStr);
		prodInfoSeg.setProdShrtPllName(emptyStr);
		prodInfoSeg.setRiskLvlCde(emptyStr);
		
		prodInfoSeg.setCcyProdCde(currency);
		prodInfoSeg.setCcyInvstCde(currency);
	
		prodInfoSeg.setCtryProdTrade1Cde(emptyStr);
		prodInfoSeg.setQtyTypeCde(emptyStr);
		prodInfoSeg.setProdLocCde(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
		prodInfoSeg.setAumChrgProdInd(emptyStr);
		
		return prodInfoSeg;
	}
	
	public boolean checkCurrency(String currStr,String curr){
		if (StringUtils.isNotBlank(currStr)){
			String [] result  =  currStr.split(",");
			
			 if(result.length > 0){
    			 return  Arrays.asList(result).contains(curr);
    		 } 			
		}
		
		return false;
	}
		
}
