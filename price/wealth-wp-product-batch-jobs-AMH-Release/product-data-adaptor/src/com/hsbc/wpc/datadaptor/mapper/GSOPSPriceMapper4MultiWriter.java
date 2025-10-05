package com.dummy.wpc.datadaptor.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSet;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.dao.IProdDAO;
import com.dummy.wpc.datadaptor.to.ProductKeyTO;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class GSOPSPriceMapper4MultiWriter extends AbstractFieldSetMapper {
	
	private static final Logger logger = Logger.getLogger(GSOPSPriceMapper4MultiWriter.class);
	
	private IProdDAO prodDao;

	private static final String DATE_FORMAT = "yyyyMMdd";
	
	private static final String BOND_CD = "BOND";
	
	private static final String UNIT_TRUST = "UT";
	
	private static final BigDecimal PRICE_ZORE = new BigDecimal("0.00000");
	
	private static final BigDecimal PRICE_MIN = new BigDecimal("-99999999.99999");
	
	private static final BigDecimal PRICE_MAX = new BigDecimal("99999999.99999");
	
	private Map<String, String> productMap = new HashMap<String, String>();
	private Map<String, Double> priceMap = new HashMap<String, Double>();
	
	private boolean initialized = false;
	
	private void init() {
		if (initialized == true) {
			return;
		}
		if (initialized == false) {
			initialized = true;
		}
		constructBondProductMapping();
		constructUTProductMapping();
		logger.info("[" + productMap.size() + "] BOND/UT products in DB.");
	}

	private void constructBondProductMapping() {
		String ctryCde = ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde");
		String orgnCde = ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde");
		constructProductMapping(ctryCde, orgnCde, GSOPSPriceMapper4MultiWriter.BOND_CD);
	}
	
	private void constructUTProductMapping() {
		String ctryCde = ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde");
		String orgnCde = ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde");
		constructProductMapping(ctryCde, orgnCde, GSOPSPriceMapper4MultiWriter.UNIT_TRUST);
	}
	
	private void constructProductMapping(String ctryCde, String orgnCde, String prodType) {
		List<ProductKeyTO> productKeyList = prodDao.retrieveProdAltPrimNumByProdType(ctryCde, orgnCde, prodType);
        for (ProductKeyTO productKey : productKeyList) {
        	productMap.put(productKey.getProdAltPrimNum(), productKey.getProdTypeCde());
        	priceMap.put(productKey.getProdAltPrimNum(), productKey.getProdMktPrcAmt());
        }
	}
	
	public Object mapLine(FieldSet fieldSet) {
		
		init();
		
		MultiWriterObj arr = new MultiWriterObj();
		
		String GMTStr = DateHelper.convertTimeZoneToGMTString(
				TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"),
						ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));

		String prodCde = StringUtils.trimToEmpty(fieldSet.readString("bqscid"));
		String prodTypeCde = productMap.get(prodCde);
		if (prodTypeCde == null) {
			logger.info("product [" + prodCde + "] is not BOND/UT, will skip process.");
			return null;
		}

		ProdPrc prodPrc = new ProdPrc();
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
		prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
		prodKey.setProdTypeCde(prodTypeCde);
		prodKey.setProdCde(prodCde);
		prodKey.setProdCdeAltClassCde("P");
		prodPrc.setProdKeySeg(prodKey);
		
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		String priceDate = fieldSet.readString("bqmpdt");
		
		Double prodPrice = priceMap.get(prodCde);

		if (StringUtils.isNotBlank(priceDate)) {
			if(null == prodPrice && StringUtils.equals(BOND_CD, prodKey.getProdTypeCde())) {
				prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(
						DateHelper.parseToDate(priceDate, GSOPSPriceMapper4MultiWriter.DATE_FORMAT),
						DateHelper.DEFAULT_DATE_FORMAT));
			}else {
				if(!"AMHGSOPS.CPT".equals(getJobCode())) {
					if(!StringUtils.equals("99999999", priceDate)
							&& DateHelper.dateCompareTo(DateHelper.parseToDate(priceDate, GSOPSPriceMapper4MultiWriter.DATE_FORMAT), DateHelper.getCurrentDate()) < 0) {
						prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(
								DateHelper.parseToDate(priceDate, GSOPSPriceMapper4MultiWriter.DATE_FORMAT),
								DateHelper.DEFAULT_DATE_FORMAT));
					}else {
						logger.info("product [" + prodCde + "] price date is ["+ priceDate +"], will skip process.");
						return null;
					}
				}else {
					logger.info("product [" + prodCde + "] price had data, will skip process.");
					return null;
				}
			}
			
		}

		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), "pdcy_prc_cde"));
		prodPrcSeg.setPrcInpDt(dateTime);
		
		String CurrencyProductMarketPriceCode = fieldSet.readString("bqcycd");
		if (StringUtils.isNotBlank(CurrencyProductMarketPriceCode)) {
			prodPrcSeg.setCcyProdMktPrcCde(CurrencyProductMarketPriceCode);
		}
		BigDecimal prodMktPrcAmt = fieldSet.readBigDecimal("bqmkpc");
		if (null != prodMktPrcAmt) {
			if (prodMktPrcAmt.compareTo(PRICE_ZORE)<=0 || PRICE_MAX.compareTo(prodMktPrcAmt) == 0) {
				prodPrcSeg.setProdMktPrcAmt(null);
			} else {
				prodPrcSeg.setProdMktPrcAmt(String.valueOf(prodMktPrcAmt));
			}
		}
		
		BigDecimal prodBidPrcAmt = fieldSet.readBigDecimal("bqpvbd");
		if (null != prodBidPrcAmt) {
			if (prodBidPrcAmt.compareTo(PRICE_ZORE)<=0 || PRICE_MAX.compareTo(prodBidPrcAmt) == 0) {
				prodPrcSeg.setProdBidPrcAmt(null);
			} else {
				prodPrcSeg.setProdBidPrcAmt(String.valueOf(prodBidPrcAmt));
				if (StringUtils.equals(UNIT_TRUST, prodKey.getProdTypeCde())) {
					prodPrcSeg.setProdNavPrcAmt(String.valueOf(prodBidPrcAmt));
				}
			}
		}
		
		BigDecimal prodOffrPrcAmt = fieldSet.readBigDecimal("bqofpr");
		if (null != prodOffrPrcAmt) {
			if (prodOffrPrcAmt.compareTo(PRICE_ZORE)<=0  || PRICE_MAX.compareTo(prodOffrPrcAmt) == 0) {
				prodPrcSeg.setProdOffrPrcAmt(null);
			} else {
				prodPrcSeg.setProdOffrPrcAmt(String.valueOf(prodOffrPrcAmt));
			}
		}
		
		if (prodPrcSeg.getProdMktPrcAmt() == null && prodPrcSeg.getProdBidPrcAmt() == null && prodPrcSeg.getProdOffrPrcAmt() == null) {
			logger.info("product [" + prodCde + "] prices are null, will skip process.");
			return null;
		}

		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();

		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);
		prodPrcSeg.setRecDtTmSeg(recDtTmSeg);

		prodPrc.addProdPrcSeg(prodPrcSeg);
		
		if (StringUtils.equals(UNIT_TRUST, prodKey.getProdTypeCde())) {
			arr.addObj(prodPrc);
			arr.addObj(null);
		}
		
		if (StringUtils.equals(BOND_CD, prodKey.getProdTypeCde()) && !"AMHGSOPS.CPT".equals(getJobCode())) {
			arr.addObj(null);
			arr.addObj(prodPrc);
		}
		
		if(StringUtils.equals(BOND_CD, prodKey.getProdTypeCde()) && "AMHGSOPS.CPT".equals(getJobCode())) {
			arr.addObj(prodPrc);
		}
		
		return arr;
	}

	public void setProdDao(IProdDAO prodDao) {
		this.prodDao = prodDao;
	}
	
}
