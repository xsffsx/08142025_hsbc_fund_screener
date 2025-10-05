package com.dummy.wpc.datadaptor.beanconverter;

import org.apache.commons.lang.StringUtils;
import com.dummy.wpc.batch.xml.ProdUserDefExtSeg;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.batch.xml.StockInstmSeg;
import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;

public class SRBPSecBeanConverter extends AbstractBeanConverter {

	@Override
	public Object convert(Object source) {
		
		UtTrstInstm ut = (UtTrstInstm) source;
		MultiWriterObj multiWriterObj = new MultiWriterObj();
		
		StockInstm sec = new StockInstm();
		sec.setProdAltNumSeg(ut.getProdAltNumSeg());
		sec.setProdInfoSeg(ut.getProdInfoSeg());
		sec.setProdKeySeg(ut.getProdKeySeg());
		sec.setRecDtTmSeg(ut.getRecDtTmSeg());
		
		sec.getProdKeySeg().setProdTypeCde("SEC");
		sec.getProdInfoSeg().setProdSubtpCde("ETF");
				
		if(StringUtils.isNotBlank(ut.getProdInfoSeg().getProdSubtpCde())){
			ProdUserDefExtSeg extField1 = new ProdUserDefExtSeg();
			extField1.setFieldCde("fundCatCde");
			extField1.setFieldValue(ut.getProdInfoSeg().getProdSubtpCde());
			sec.getProdInfoSeg().addProdUserDefExtSeg(extField1);
		}
				
		if(StringUtils.isNotBlank(ut.getUtTrstInstmSeg().getFundHouseCde())){			
			ProdUserDefExtSeg extField2 = new ProdUserDefExtSeg();
			extField2.setFieldCde("fundHouseCde");
			extField2.setFieldValue(ut.getUtTrstInstmSeg().getFundHouseCde());
			sec.getProdInfoSeg().addProdUserDefExtSeg(extField2);
		}
				
		String str = "";
		String amt="0";
		StockInstmSeg seg = new StockInstmSeg();
		seg.setEarnPerShareAnnlAmt(amt);
		seg.setMrgnTrdInd(str);
		seg.setMrgnSecODPct(str);
		seg.setSuptAuctnTrdInd(str);
		seg.setStopLossMinPct(str);
		seg.setStopLossMaxPct(str);
		seg.setSprdStopLossMinCnt(str);
		seg.setSprdStopLossMaxCnt(str);
		seg.setProdBodLotQtyCnt(str);
		seg.setMktProdTrdCde(str);
		seg.setSuptMipInd(str);
		seg.setInvstMipMinAmt(amt);
		seg.setInvstMipIncrmMinAmt(amt);
		seg.setProdIssQtyTtlCnt(str);
		seg.setBodLotProdInd(str);
		seg.setTrdLimitInd(str);
		seg.setProdMaxIndvOwnrPct(str);
		seg.setProdMaxIndvForgnInvstrPct(str);
		seg.setProdMaxForgnInvstrTtlPct(str);
		seg.setProdExerPrcAmt(amt);
		seg.setPsblProdBorwInd(str);
		seg.setAllowProdLendInd(str);
		seg.setPoolProdInd(str);
		seg.setScripOnlyProdInd(str);
		seg.setSuptAtmcTrdInd(str);
		seg.setSuptNetSetlInd(str);
		seg.setSuptStopLossOrdInd(str);
		seg.setSuptWinWinOrdInd(str);
		seg.setSuptAllIn1OrdInd(str);
		seg.setSuptProdSpltInd(str);
		seg.setMrgnPrcAuctnTrdPct(str);
		seg.setProdAuctnTrdExpirDt(str);
		seg.setLoanProdMrgnTrdPct(str);
		seg.setLoanBdgtProdIPOAmt(amt);
		seg.setLoanProdIPOTtlAmt(amt);
		seg.setExclLimitCalcInd(str);
		seg.setProdMktStatChngLaDt(str);
		seg.setPrcVarCde(str);
		seg.setMethCalcScripChrgCde(str);
		seg.setProdSellMaxQtyCnt(str);
		seg.setProdBuyMaxQtyCnt(str);
		seg.setPrcVarMinAmt(amt);
		seg.setPrcVarMinEffDt(str);
		seg.setMktSegExchgCde(str);
		seg.setCtryProdRegisCde(str);

		sec.setStockInstmSeg(seg);
		multiWriterObj.addObj(sec);
		return multiWriterObj;
	}
}
