package com.dummy.wpc.datadaptor.processor;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.EliDctSellPctSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.StockPrcSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.ProductTypeEnum;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class ProdPrcBeanProcessor extends AbstractBeanProcessor<ProdPrc> {
	
	@Override
	public ProdPrc process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		String internalProdTypeCode = getInternalProdTypeCde(entity.getProdTypeCde());
		ProdPrc price = null;
		switch (ProductTypeEnum.retrieveProdType(internalProdTypeCode)) {
			case SEC:
			case WRTS:
				price = convetertSecPrice(entity, prodKey, recDtTm);
				break;
			case BOND:
				price = convetertBondPrice(entity, prodKey, recDtTm);
				break;
			case ELI:
				price = convetertEliPrice(entity, prodKey, recDtTm);
				break;
			default:
				break;
		}
		return price;
	}

	private ProdPrc convetertSecPrice(final ProductEntity entity, final ProdKeySeg prodKey, final RecDtTmSeg recDtTm) {
		// ProdPrc
		ProdPrc secPrice = new ProdPrc();
		secPrice.setProdKeySeg(prodKey);
		// ProdPrcSeg
		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(entity.getStkPrcDt()));
		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PDCY_PRC_CDE));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(entity.getStkPrcCcy()));
		prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getBidPrcAmt())));
		prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
		prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getNomlPrcAmt())));
		// StockPrcSeg
		StockPrcSeg stockPrcSeg = new StockPrcSeg();
		stockPrcSeg.setProdPrcChngPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcChngPct())));
		stockPrcSeg.setProdPrcChngAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcChngAmt())));
		stockPrcSeg.setProdTdyHighPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcMaxAmt())));
		stockPrcSeg.setProdTdyLowPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcMinAmt())));
		stockPrcSeg.setShareTrdCnt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getShareTradeCnt())));
		stockPrcSeg.setProdTrnvrAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getShareTradeAmt())));
		prodPrcSeg.setStockPrcSeg(stockPrcSeg);
		// RecDtTmSeg
		prodPrcSeg.setRecDtTmSeg(recDtTm);
		secPrice.addProdPrcSeg(prodPrcSeg);
		return secPrice;
	}

	private ProdPrc convetertBondPrice(final ProductEntity entity, final ProdKeySeg prodKey, final RecDtTmSeg recDtTm) {
		// ProdPrc
		ProdPrc bondPrice = new ProdPrc();
		bondPrice.setProdKeySeg(prodKey);
		// ProdPrcSeg
		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(entity.getMktPrcDt()));
		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PDCY_PRC_CDE));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(entity.getMktPrcCcy()));
		if (entity.getMarketPrc() == null) {
			prodPrcSeg.setProdMktPrcAmt(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_MKT_PRC_AMT));
		} else {
			prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getMarketPrc())));
		}
		// RecDtTmSeg
		prodPrcSeg.setRecDtTmSeg(recDtTm);
		bondPrice.addProdPrcSeg(prodPrcSeg);
		return bondPrice;
	}

	private ProdPrc convetertEliPrice(final ProductEntity entity, final ProdKeySeg prodKey, final RecDtTmSeg recDtTm) {
		// ProdPrc
		ProdPrc eliPrice = new ProdPrc();
		eliPrice.setProdKeySeg(prodKey);
		// ProdPrcSeg
		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(entity.getPriceDt()));
		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PDCY_PRC_CDE));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(entity.getCrncyCde()));
		eliPrice.addProdPrcSeg(prodPrcSeg);
		
		//eliDctSellPctSeg
		EliDctSellPctSeg eliDctSellPctSeg  = new EliDctSellPctSeg();
		eliDctSellPctSeg.setEliDctSellPercentSeg(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
		eliPrice.setEliDctSellPctSeg(eliDctSellPctSeg);
		
		// RecDtTmSeg
		prodPrcSeg.setRecDtTmSeg(recDtTm);
		
		return eliPrice;
	}
}
