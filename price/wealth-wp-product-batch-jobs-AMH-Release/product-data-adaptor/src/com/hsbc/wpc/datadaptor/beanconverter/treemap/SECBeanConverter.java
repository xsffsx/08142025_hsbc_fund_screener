package com.dummy.wpc.datadaptor.beanconverter.treemap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.mapping.DefaultFieldSet;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdPerf;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.datadaptor.beanconverter.AbstractBeanConverter;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;

public class SECBeanConverter extends AbstractBeanConverter {
	
	private void putValue(List valueList, String value){
		if(value == null){
			value = "";
		}
		//value = "\"" + value + "\"";
		valueList.add(value);
	}
	
	public Object convert(Object source) {
		if(source == null){
			return null;
		}
		if(!(source instanceof MultiWriterObj)){
			throw new IllegalArgumentException("The source must be a instance of com.dummy.wpc.datadaptor.mapper.MultiWriterObj.");
		}
		MultiWriterObj multiObj = (MultiWriterObj) source;
//		StockInstm result = new StockInstm();
		List valueList = new ArrayList();
		
		if(multiObj.getArray() != null && multiObj.getArray().length == 3){
			StockInstm sec =(StockInstm) multiObj.getArray()[0];
			ProdPrc prc = (ProdPrc)multiObj.getArray()[1];
			ProdPerf perf = (ProdPerf)multiObj.getArray()[2];
			//"CTRY_CDE","ORGN_CDE","PROD_TYPE_CDE","PROD_TYPE_DESC","PROD_TYPE_PLL_DESC","PROD_TYPE_SLL_DESC",
			if(sec != null){
				putValue(valueList,sec.getProdKeySeg().getCtryRecCde());
				putValue(valueList,sec.getProdKeySeg().getGrpMembrRecCde());
				putValue(valueList,sec.getProdKeySeg().getProdTypeCde());
				putValue(valueList,"This is my understanding: this field's name should be...");
				putValue(valueList,"he said:\"I hate you!\"");
				putValue(valueList,"abc\"def");
			}
			//"PROD_SUBTP_CDE","PROD_SUBTP_DESC","PROD_SUBTP_PLL_DESC","PROD_SUBTP_SLL_DESC","PROD_CDE","INT_PROD_ID",
			//"PROD_NAME","PROD_PLL_NAME","PROD_SLL_NAME","SHRT_NAME","SHRT_PLL_NAME","SHRT_SLL_NAME","PROD_DESC",
			//"PROD_PLL_DESC","PROD_SLL_DESC","ASET_CLASS_CDE_1","ASET_CLASS_CDE_1_DESC","ASET_CLASS_CDE_1_PLL_DESC",
			//"ASET_CLASS_CDE_1_SLL_DESC","ASET_CLASS_CDE_2","ASET_CLASS_CDE_2_DESC","ASET_CLASS_CDE_2_PLL_DESC",
			//"ASET_CLASS_CDE_2_SLL_DESC","ASET_CLASS_CDE_3","ASET_CLASS_CDE_3_DESC","ASET_CLASS_CDE_3_PLL_DESC",
			//"ASET_CLASS_CDE_3_SLL_DESC","UDLY_ASET_CDE_1","UDLY_ASET_CDE_1_DESC","UDLY_ASET_CDE_1_PLL_DESC",
			//"UDLY_ASET_CDE_1_SLL_DESC","UDLY_ASET_CDE_2","UDLY_ASET_CDE_2_DESC","UDLY_ASET_CDE_2_PLL_DESC",
			//"UDLY_ASET_CDE_2_SLL_DESC","UDLY_ASET_CDE_3","UDLY_ASET_CDE_3_DESC","UDLY_ASET_CDE_3_PLL_DESC",
			//"UDLY_ASET_CDE_3_SLL_DESC","STAT_CDE","STAT_CDE_DESC","STAT_CDE_PLL_DESC","STAT_CDE_SLL_DESC",
			//"CRNCY_CDE","CRNCY_CDE_DESC","CRNCY_CDE_PLL_DESC","CRNCY_CDE_SLL_DESC","CRNCY_TRADE_CDE_1",
			//"CRNCY_TRADE_CDE_1_DESC","CRNCY_TRADE_CDE_1_PLL_DESC","CRNCY_TRADE_CDE_1_SLL_DESC","CRNCY_TRADE_CDE_2",
			//"CRNCY_TRADE_CDE_2_DESC","CRNCY_TRADE_CDE_2_PLL_DESC","CRNCY_TRADE_CDE_2_SLL_DESC","CRNCY_TRADE_CDE_3",
			//"CRNCY_TRADE_CDE_3_DESC","CRNCY_TRADE_CDE_3_PLL_DESC","CRNCY_TRADE_CDE_3_SLL_DESC","CRNCY_TRADE_CDE_4",
			//"CRNCY_TRADE_CDE_4_DESC","CRNCY_TRADE_CDE_4_PLL_DESC","CRNCY_TRADE_CDE_4_SLL_DESC","CRNCY_TRADE_CDE_5",
			//"CRNCY_TRADE_CDE_5_DESC","CRNCY_TRADE_CDE_5_PLL_DESC","CRNCY_TRADE_CDE_5_SLL_DESC","CRNCY_TRADE_CDE_6",
			//"CRNCY_TRADE_CDE_6_DESC","CRNCY_TRADE_CDE_6_PLL_DESC","CRNCY_TRADE_CDE_6_SLL_DESC","CRNCY_TRADE_CDE_7",
			//"CRNCY_TRADE_CDE_7_DESC","CRNCY_TRADE_CDE_7_PLL_DESC","CRNCY_TRADE_CDE_7_SLL_DESC","CRNCY_TRADE_CDE_8",
			//"CRNCY_TRADE_CDE_8_DESC","CRNCY_TRADE_CDE_8_PLL_DESC","CRNCY_TRADE_CDE_8_SLL_DESC","CRNCY_TRADE_CDE_9",
			//"CRNCY_TRADE_CDE_9_DESC","CRNCY_TRADE_CDE_9_PLL_DESC","CRNCY_TRADE_CDE_9_SLL_DESC","CRNCY_TRADE_CDE_10",
			//"CRNCY_TRADE_CDE_10_DESC","CRNCY_TRADE_CDE_10_PLL_DESC","CRNCY_TRADE_CDE_10_SLL_DESC","CRNCY_TRADE_CDE_11",
			//"CRNCY_TRADE_CDE_11_DESC","CRNCY_TRADE_CDE_11_PLL_DESC","CRNCY_TRADE_CDE_11_SLL_DESC","CRNCY_TRADE_CDE_12",
			//"CRNCY_TRADE_CDE_12_DESC","CRNCY_TRADE_CDE_12_PLL_DESC","CRNCY_TRADE_CDE_12_SLL_DESC","CRNCY_TRADE_CDE_13",
			//"CRNCY_TRADE_CDE_13_DESC","CRNCY_TRADE_CDE_13_PLL_DESC","CRNCY_TRADE_CDE_13_SLL_DESC","CRNCY_TRADE_CDE_14",
			//"CRNCY_TRADE_CDE_14_DESC","CRNCY_TRADE_CDE_14_PLL_DESC","CRNCY_TRADE_CDE_14_SLL_DESC","CRNCY_TRADE_CDE_15",
			//"CRNCY_TRADE_CDE_15_DESC","CRNCY_TRADE_CDE_15_PLL_DESC","CRNCY_TRADE_CDE_15_SLL_DESC","CRNCY_TRADE_CDE_16",
			//"CRNCY_TRADE_CDE_16_DESC","CRNCY_TRADE_CDE_16_PLL_DESC","CRNCY_TRADE_CDE_16_SLL_DESC","CRNCY_TRADE_CDE_17",
			//"CRNCY_TRADE_CDE_17_DESC","CRNCY_TRADE_CDE_17_PLL_DESC","CRNCY_TRADE_CDE_17_SLL_DESC","CRNCY_TRADE_CDE_18",
			//"CRNCY_TRADE_CDE_18_DESC","CRNCY_TRADE_CDE_18_PLL_DESC","CRNCY_TRADE_CDE_18_SLL_DESC","CRNCY_TRADE_CDE_19",
			//"CRNCY_TRADE_CDE_19_DESC","CRNCY_TRADE_CDE_19_PLL_DESC","CRNCY_TRADE_CDE_19_SLL_DESC","CRNCY_TRADE_CDE_20",
			//"CRNCY_TRADE_CDE_20_DESC","CRNCY_TRADE_CDE_20_PLL_DESC","CRNCY_TRADE_CDE_20_SLL_DESC","RISK_LVL_CDE","RISK_LVL_DESC",
			//"RISK_LVL_PLL_DESC","RISK_LVL_SLL_DESC","PRD_PROD_CDE","PRD_PROD_DESC","PRD_PROD_PLL_DESC","PRD_PROD_SLL_DESC",
			//"PRD_PROD_NUM","TENOR_DAY_NUM","LAUNCH_DT","MAT_DT","MKT_INVST_CDE","MKT_INVST_CDE_DESC","MKT_INVST_CDE_PLL_DESC",
			//"MKT_INVST_CDE_SLL_DESC","SECT_CDE","SECT_CDE__DESC","SECT_CDE_PLL_DESC","SECT_CDE_SLL_DESC","ALOW_BUY_IND","ALOW_SELL_IND",
			//"ALOW_BUY_UNIT_IND","ALOW_BUY_AMT_IND","ALOW_SELL_UNIT_IND","ALOW_SELL_AMT_IND","ALOW_MIP_IND","ALOW_MIP_UNIT","ALOW_MIP_AMT",
			//"ALOW_SI_IND","ALOW_SI_UNIT","ALOW_SI_AMT","ALOW_SO_IND","ALOW_SO_UNIT","ALOW_SO_AMT","INCOME_IND","CAPITAL_GURN_IND",
			//"YLD_EHANCE_IND","GROWTH_IND","SRCH_PRTY_NUM","SLST_1_IND","SLST_EFF_1_DT","SLST_2_IND","SLST_EFF_2_DT","SLST_3_IND",
			//"SLST_EFF_3_DT","MKT_INFO_IND","AVG_PRD_RTN","AVE_VOLAT","SUB_TYPE_IND","PRC_DIVR_NUM","MK_TO_MKT_IND","ADD_INFO",
			//"ADD_PLL_INFO","ADD_SLL_INFO","NATURE_CDE","CREAT_DT","CREAT_TM","UPDT_LAST_DT","UPDT_LAST_TM","ISS_CDE","ISS_DESC",
			//"ISS_PLL_DESC","ISS_SLL_DESC","ISS_NUM","ISS_DT","FREQ_COUPN_CDE","COUPN_ANNL_PCT","COUPN_ANNL_PCT_TXT","EXT_COUPN_ANNL_TXT",
			//"PAY_NEXT_COUPN_DT","EXBL_CLBL_IND","CRED_RTNG_1_CDE","CRED_RTNG_1_SEQ_NO","CRED_RTNG_2_CDE","CRED_RTNG_2_SEQ_NO","CRED_RTNG_3_CDE",
			//"INT_IND_ACCR_AMT","INVST_INIT_MIN_CCY","INVST_INIT_MIN_AMT","INVST_INIT_MIN_DP","INVST_INIT_MIN","INVST_ICT_MIN_CCY",
			//"INVST_ICT_MIN_AMT","INVST_ICT_MIN_DP","INVST_ICT_MIN","LOT_SIZE_NUM","MAT_EXT_DT","BID_PRC_CCY","BID_PRC_AMT",
			//"BID_PRC_DP","BID_PRC","OFFER_PRC_CCY","OFFER_PRC_AMT","OFFER_PRC_DP","OFFER_PRC","BID_YLD_TEXT","BID_YTC_TEXT",
			//"BID_YTM_TEXT","OFFER_YLD_TEXT","OFFER_YLD","OFFER_YTC_TXT","OFFER_YTM_TXT","OVDFT_SEC_PCT","SUB_ORDNT","PRFM_DT",
			//"PRFM_YTD_PCT","PRFM_1MO_PCT","PRFM_3MO_PCT","PRFM_6MO_PCT","PRFM_1YR_PCT","PRFM_1YR_PCT_EXT","PRFM_3YR_PCT","PRFM_5YR_PCT",
			//"PRFM_SNC_LNCH_PCT","VOLAT_1YR_PCT","VOLAT_1YR_PCT_EXT","VOLAT_3YR_PCT","VOLAT_3YR_PCT_EXT","UDF_01","UDF_02","UDF_03",
			//"UDF_04","UDF_05","UDF_06","UDF_07","UDF_08","UDF_09","UDF_10","UDF_11","UDF_12","UDF_13","UDF_14","UDF_15"

			
			System.out.println("SEC: " + org.apache.commons.lang.builder.ReflectionToStringBuilder.toString(sec));
			System.out.println("prc: " + org.apache.commons.lang.builder.ReflectionToStringBuilder.toString(prc));
			System.out.println("perf: " + org.apache.commons.lang.builder.ReflectionToStringBuilder.toString(perf));
		}
		String[] values= new String[valueList.size()];
		valueList.toArray(values);
		FieldSet result = new DefaultFieldSet(values);
		return result;
		
	}

}
