package com.dummy.wpc.datadaptor.mapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.dummy.wpc.batch.xml.AsetGeoLocAllocSeg;
import com.dummy.wpc.batch.xml.AsetSicAllocSeg;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdAsetVoltlClassSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdListSeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.ProductInformation;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.ResChanSeg;
import com.dummy.wpc.batch.xml.ResChannelDtl;
import com.dummy.wpc.common.exception.ParseException;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.reader.Sheet;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

/**
 * The Class GnrcProdRowMapper.
 */
public class GnrcProdRowMapper implements RowMapper<MultiWriterObj> {

	private static Logger logger = Logger.getLogger(GnrcProdRowMapper.class);

	/** The job code. */
	private String jobCode;

	private String DEFAULT_CONST_FILE_PATTERN = System
			.getProperty(Constants.CONFIG_PATH) + "/%s_%s/constants.properties";

	private String constantsFilenamePattern = this.DEFAULT_CONST_FILE_PATTERN;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dummy.wpc.datadaptor.mapper.RowMapper#mapRow(com.dummy.wpc.datadaptor
	 * .reader.Sheet, java.util.Map)
	 */
	@Override
	public MultiWriterObj mapRow(final Sheet sheet,
			final Map<String, String> mappings) throws Exception {

		MultiWriterObj multiWriterObj = new MultiWriterObj();

		GnrcProd product = convertGnrcProd(sheet, mappings);

		String[] ctryList = loadReplicaCtryList(getJobCode());
		for (String ctry : ctryList) {
			String grbMebCde = ConstantsPropertiesHelper.getValue(this.jobCode,
					ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + Const.PERIOD
							+ ctry);
			GnrcProd instance = new GnrcProd();
			BeanUtils.copyProperties(product, instance);
			instance.setProdKeySeg(replicaProdKeySeg(instance.getProdKeySeg(),
					ctry, grbMebCde));
			instance.setProdInfoSeg(assignConfigurableValue(
					instance.getProdInfoSeg(), ctry, grbMebCde));
			multiWriterObj.addObj(instance);
		}

		return multiWriterObj;

	}

	protected String[] loadReplicaCtryList(final String jobCode) {
		String replica_ctry_list = ConstantsPropertiesHelper.getValue(jobCode,
				ConfigConstant.REPLICATION_COUNTRY_LIST);

		if (StringUtils.isBlank(replica_ctry_list)) {
			String key = jobCode + "."
					+ ConfigConstant.REPLICATION_COUNTRY_LIST;
			// logger.error("Can't found configure[" + key
			// + "] for replication price files.");
			throw new IllegalArgumentException("Can't found configure[" + key
					+ "]!");
		}
		return StringUtils.split(replica_ctry_list, ",");
	}

	/**
	 * Convert product.
	 * 
	 * @param product
	 *            the product
	 * @param ctry
	 *            the ctry
	 * @return the gnrc prod
	 */
	protected ProdKeySeg replicaProdKeySeg(final ProdKeySeg prodKeySeg,
			final String ctry, final String grbMebCde) {
		ProdKeySeg instance = new ProdKeySeg();
		BeanUtils.copyProperties(prodKeySeg, instance);
		instance.setCtryRecCde(ctry);
		instance.setGrpMembrRecCde(grbMebCde);
		return instance;
	}

	protected ProdInfoSeg assignConfigurableValue(
			final ProdInfoSeg prodInfoSeg, final String ctry,
			final String grbMebCde) {
		ProdInfoSeg instance = new ProdInfoSeg();
		BeanUtils.copyProperties(prodInfoSeg, instance);
		Properties props = loadRegionSpecialConstProps(ctry, grbMebCde);
		if (props != null && !props.isEmpty()) {
			// country product tradable code 1
			instance.setCtryProdTrade1Cde(props.getProperty(getJobCode()
					+ Const.PERIOD
					+ StringUtils.lowerCase(Const.CTRY_PROD_TRADE_1_CDE)));

			// market investment code
			String mktInvstCdeTemp = props
					.getProperty(getJobCode() + Const.PERIOD
							+ StringUtils.lowerCase(Const.MKT_INVST_CDE));

			if (StringUtils.equalsIgnoreCase("null", mktInvstCdeTemp)) {
				// special config
				instance.setMktInvstCde(null);
			} else {
				instance.setMktInvstCde(mktInvstCdeTemp);
			}

		} else {
			throw new IllegalArgumentException(String.format(
					"Fail load constant file for %s%s, pls check file: %s.",
					new Object[] {
							ctry,
							grbMebCde,
							String.format(getConstantsFilenamePattern(),
									new Object[] { ctry, grbMebCde }) }));
		}
		return instance;
	}

	protected Properties loadRegionSpecialConstProps(final String ctry,
			final String grbMebCde) {
		Properties props = new Properties();
		String path = String.format(getConstantsFilenamePattern(),
				new Object[] { ctry, grbMebCde });
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			props.load(is);
		} catch (FileNotFoundException e) {
			GnrcProdRowMapper.logger.error("Constant properties can't found.",
					e);
		} catch (IOException e) {
			GnrcProdRowMapper.logger.error(
					"IOException when load constant properties.", e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dummy.wpc.datadaptor.mapper.RowMapper#setJobCode(java.lang.String)
	 */
	@Override
	public void setJobCode(final String jobCode) {
		this.jobCode = jobCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dummy.wpc.datadaptor.mapper.RowMapper#getJobCode()
	 */
	@Override
	public String getJobCode() {
		return this.jobCode;
	}

	public String getConstantsFilenamePattern() {
		return this.constantsFilenamePattern;
	}

	public void setConstantsFilenamePattern(
			final String constantsFilenamePattern) {
		this.constantsFilenamePattern = constantsFilenamePattern;
	}

	/**
	 * Convert gnrc prod.
	 * 
	 * @param sheet
	 *            the sheet
	 * @param mappings
	 *            the mappings
	 * @return the gnrc prod
	 * @throws ParseException
	 *             the parse exception
	 */
	private GnrcProd convertGnrcProd(final Sheet sheet,
			final Map<String, String> mappings) throws ParseException {
		GnrcProd gnrcProd = new GnrcProd();

		gnrcProd.setProdAltNumSeg(parseProdAltNumSeg(sheet, mappings));
		gnrcProd.setProdKeySeg(parseProdKeySeg(sheet, mappings));
		gnrcProd.setRecDtTmSeg(parseRecDtTmSeg(sheet, mappings));
		gnrcProd.setProdInfoSeg(parseProdInfo(sheet, mappings));

		return gnrcProd;
	}

	protected RecDtTmSeg parseRecDtTmSeg(final Sheet sheet,
			final Map<String, String> mappings) {
		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String currentDateTime = DateHelper
				.formatDate2String(DateHelper.getCurrentDate(),
						DateHelper.DEFAULT_DATETIME_FORMAT);
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper
				.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),
						ConfigConstant.CTRY_REC_CDE), ConstantsPropertiesHelper
						.getValue(getJobCode(),
								ConfigConstant.GRP_MEMBR_REC_CDE)));
		recDtTmSeg.setRecCreatDtTm(currentDateTime);
		recDtTmSeg.setRecUpdtDtTm(currentDateTime);
		recDtTmSeg.setProdPrcUpdtDtTm(currentDateTime);
		recDtTmSeg.setRecOnlnUpdtDtTm(currentDateTime);
		recDtTmSeg.setProdStatUpdtDtTm(currentDateTime);
		recDtTmSeg.setTimeZone(GMTStr);
		return recDtTmSeg;
	}

	protected ProdKeySeg parseProdKeySeg(final Sheet sheet,
			final Map<String, String> mappings) throws ParseException {
		ProdKeySeg prodKeySeg = new ProdKeySeg();
		prodKeySeg.setCtryRecCde(mappings.get(Const.CTRY_REC_CDE));
		prodKeySeg.setGrpMembrRecCde(mappings.get(Const.GRP_MEMBR_REC_CDE));
		prodKeySeg.setProdCde(mappings.get(Const.PROD_ALT_PRIM_NUM));
		prodKeySeg.setProdTypeCde(mappings.get(Const.PROD_TYPE_CDE));
		return prodKeySeg;
	}

	protected ProdAltNumSeg[] parseProdAltNumSeg(final Sheet sheet,
			final Map<String, String> mappings) {
		List<ProdAltNumSeg> segs = new ArrayList<ProdAltNumSeg>();
		ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
		prodAltNumSeg.setProdAltNum(mappings.get(Const.PROD_ALT_PRIM_NUM));
		prodAltNumSeg.setProdCdeAltClassCde("P");
		segs.add(prodAltNumSeg);
		for (int index = 2; index <= 10; index++) {
			String prodAltNum = mappings.get(Const.PROD_ALT_NUM
					+ Const.UNDERLINE + index);
			String prodCdeAltClassCde = mappings
					.get(Const.PROD_CDE_ALT_CLASS_CDE + Const.UNDERLINE + index);
			if (StringUtils.isNotBlank(prodAltNum)
					&& StringUtils.isNotBlank(prodCdeAltClassCde)) {
				ProdAltNumSeg seg = new ProdAltNumSeg();
				seg.setProdAltNum(prodAltNum);
				seg.setProdCdeAltClassCde(prodCdeAltClassCde);
				segs.add(seg);
			}
		}
		return segs.toArray(new ProdAltNumSeg[segs.size()]);
	}

	protected ProdInfoSeg parseProdInfo(final Sheet sheet,
			final Map<String, String> mappings) throws ParseException {
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();

		prodInfoSeg.setProdSubtpCde(mappings.get(Const.PROD_SUBTP_CDE));
		prodInfoSeg.setProdName(mappings.get(Const.PROD_NAME));
		prodInfoSeg.setProdPllName(mappings.get(Const.PROD_PLL_NAME));
		prodInfoSeg.setProdSllName(mappings.get(Const.PROD_SLL_NAME));
		prodInfoSeg.setProdShrtName(mappings.get(Const.PROD_SHRT_NAME));
		prodInfoSeg.setProdShrtPllName(mappings.get(Const.PROD_SHRT_PLL_NAME));
		prodInfoSeg.setProdShrtSllName(mappings.get(Const.PROD_SHRT_SLL_NAME));
		prodInfoSeg.setProdDesc(mappings.get(Const.PROD_DESC));
		prodInfoSeg.setProdPllDesc(mappings.get(Const.PROD_PLL_DESC));
		prodInfoSeg.setProdSllDesc(mappings.get(Const.PROD_SLL_DESC));
		prodInfoSeg.setBchmkName(mappings.get(Const.BCHMK_NAME));
		prodInfoSeg.setBchmkPllName(mappings.get(Const.BCHMK_PLL_NAME));
		prodInfoSeg.setBchmkSllName(mappings.get(Const.BCHMK_SLL_NAME));
		prodInfoSeg.setAsetClassCde(mappings.get(Const.ASET_CLASS_CDE));
		prodInfoSeg.setProdStatCde(mappings.get(Const.PROD_STAT_CDE));
		prodInfoSeg.setCcyProdCde(mappings.get(Const.CCY_PROD_CDE));
		prodInfoSeg.setRiskLvlCde(mappings.get(Const.RISK_LVL_CDE));
		prodInfoSeg.setPrdProdCde(mappings.get(Const.PRD_PROD_CDE));
		prodInfoSeg.setPrdProdNum(mappings.get(Const.PRD_PROD_NUM));
		prodInfoSeg
				.setTermRemainDayCnt(mappings.get(Const.TERM_REMAIN_DAY_CNT));
		prodInfoSeg.setProdLnchDt(DateHelper
				.convertExcelDate2XmlFormat(mappings.get(Const.PROD_LNCH_DT)));
		prodInfoSeg.setProdMturDt(DateHelper
				.convertExcelDate2XmlFormat(mappings.get(Const.PROD_MTUR_DT)));
		prodInfoSeg.setMktInvstCde(mappings.get(Const.MKT_INVST_CDE));
		prodInfoSeg.setSectInvstCde(mappings.get(Const.SECT_INVST_CDE));
		prodInfoSeg.setAllowBuyProdInd(mappings.get(Const.ALLOW_BUY_PROD_IND));
		prodInfoSeg
				.setAllowSellProdInd(mappings.get(Const.ALLOW_SELL_PROD_IND));
		prodInfoSeg.setAllowBuyUtProdInd(mappings
				.get(Const.ALLOW_BUY_AMT_PROD_IND));
		prodInfoSeg.setAllowBuyAmtProdInd(mappings
				.get(Const.ALLOW_BUY_AMT_PROD_IND));
		prodInfoSeg.setAllowSellUtProdInd(mappings
				.get(Const.ALLOW_SELL_UT_PROD_IND));
		prodInfoSeg.setAllowSellAmtProdInd(mappings
				.get(Const.ALLOW_SELL_AMT_PROD_IND));
		prodInfoSeg.setAllowSellMipProdInd(mappings
				.get(Const.ALLOW_SELL_MIP_PROD_IND));
		prodInfoSeg.setAllowSellMipUtProdInd(mappings
				.get(Const.ALLOW_SELL_MIP_UT_PROD_IND));
		prodInfoSeg.setAllowSellMipAmtProdInd(mappings
				.get(Const.ALLOW_SELL_MIP_AMT_PROD_IND));
		prodInfoSeg.setAllowSwInProdInd(mappings
				.get(Const.ALLOW_SW_IN_PROD_IND));
		prodInfoSeg.setAllowSwInUtProdInd(mappings
				.get(Const.ALLOW_SW_IN_UT_PROD_IND));
		prodInfoSeg.setAllowSwInAmtProdInd(mappings
				.get(Const.ALLOW_SW_IN_AMT_PROD_IND));
		prodInfoSeg.setAllowSwOutProdInd(mappings
				.get(Const.ALLOW_SW_OUT_PROD_IND));
		prodInfoSeg.setAllowSwOutUtProdInd(mappings
				.get(Const.ALLOW_SW_OUT_UT_PROD_IND));
		prodInfoSeg.setAllowSwOutAmtProdInd(mappings
				.get(Const.ALLOW_SW_OUT_AMT_PROD_IND));
		prodInfoSeg.setIncmCharProdInd(mappings.get(Const.INCM_CHAR_PROD_IND));
		prodInfoSeg
				.setCptlGurntProdInd(mappings.get(Const.CPTL_GURNT_PROD_IND));
		prodInfoSeg
				.setYieldEnhnProdInd(mappings.get(Const.YIELD_ENHN_PROD_IND));
		prodInfoSeg
				.setGrwthCharProdInd(mappings.get(Const.GRWTH_CHAR_PROD_IND));
		prodInfoSeg.setPrtyProdSrchRsultNum(mappings
				.get(Const.PRTY_PROD_SRCH_RSULT_NUM));
		prodInfoSeg.setAvailMktInfoInd(mappings.get(Const.AVAIL_MKT_INFO_IND));
		prodInfoSeg.setPrdRtrnAvgNum(mappings.get(Const.PRD_RTRN_AVG_NUM));
		prodInfoSeg.setRtrnVoltlAvgPct(mappings.get(Const.RTRN_VOLTL_AVG_PCT));
		prodInfoSeg.setDmyProdSubtpRecInd(mappings
				.get(Const.DMY_PROD_SUBTP_REC_IND));
		prodInfoSeg.setDispComProdSrchInd(mappings
				.get(Const.DISP_COM_PROD_SRCH_IND));
		prodInfoSeg.setDivrNum(mappings.get(Const.DIVR_NUM));
		prodInfoSeg.setMrkToMktInd(mappings.get(Const.MRK_TO_MKT_IND));

		prodInfoSeg.setCtryProdTrade1Cde(mappings
				.get(Const.CTRY_PROD_TRADE_1_CDE));

		prodInfoSeg.setCtryProdTrade2Cde(mappings
				.get(Const.CTRY_PROD_TRADE_2_CDE));
		prodInfoSeg.setCtryProdTrade3Cde(mappings
				.get(Const.CTRY_PROD_TRADE_3_CDE));
		prodInfoSeg.setCtryProdTrade4Cde(mappings
				.get(Const.CTRY_PROD_TRADE_4_CDE));
		prodInfoSeg.setCtryProdTrade5Cde(mappings
				.get(Const.CTRY_PROD_TRADE_5_CDE));
		prodInfoSeg.setBusStartTm(mappings.get(Const.BUS_START_TM));
		prodInfoSeg.setBusEndTm(mappings.get(Const.BUS_END_TM));
		prodInfoSeg.setIntroProdCurrPrdInd(mappings
				.get(Const.INTRO_PROD_CURR_PRD_IND));
		prodInfoSeg.setProdTopSellRankNum(mappings
				.get(Const.PROD_TOP_SELL_RANK_NUM));
		prodInfoSeg.setProdTopPerfmRankNum(mappings
				.get(Const.PROD_TOP_PERFM_RANK_NUM));
		prodInfoSeg.setProdTopYieldRankNum(mappings
				.get(Const.PROD_TOP_YIELD_RANK_NUM));
		prodInfoSeg.setProdIssueCrosReferDt(DateHelper
				.convertExcelDate2XmlFormat(mappings
						.get(Const.PROD_ISSUE_CROS_REFER_DT)));
		prodInfoSeg.setCcyInvstCde(mappings.get(Const.CCY_INVST_CDE));
		prodInfoSeg.setQtyTypeCde(mappings.get(Const.QTY_UNIT_PROD_CDE));
		prodInfoSeg.setProdLocCde(mappings.get(Const.PROD_LOC_CDE));
		prodInfoSeg.setProdTaxFreeWrapActStaCde(mappings
				.get(Const.PROD_TAX_FREE_WRAP_ACT_STA_CDE));
		prodInfoSeg.setTrdFirstDt(DateHelper
				.convertExcelDate2XmlFormat(mappings.get(Const.TRD_FIRST_DT)));
		prodInfoSeg.setSuptRcblCashProdInd(mappings
				.get(Const.SUPT_RCBL_CASH_PROD_IND));
		prodInfoSeg.setSuptRcblScripProdInd(mappings
				.get(Const.SUPT_RCBL_SCRIP_PROD_IND));
		prodInfoSeg.setLoanProdOdMrgnPct(mappings
				.get(Const.LOAN_PROD_OD_MRGN_PCT));
		prodInfoSeg.setDcmlPlaceTradeUnitNum(mappings
				.get(Const.DCML_PLACE_TRADE_UNIT_NUM));
		prodInfoSeg.setPldgLimitAssocAcctInd(mappings
				.get(Const.PLDG_LIMIT_ASSOC_ACCT_IND));
		prodInfoSeg.setProdOwnCde(mappings.get(Const.PROD_OWN_CDE));
		prodInfoSeg.setForgnProdInd(mappings.get(Const.FORGN_PROD_IND));
		prodInfoSeg.setAsetCatExtnlCde(mappings.get(Const.ASET_CAT_EXTNL_CDE));
		prodInfoSeg.setChrgCatCde(mappings.get(Const.CHRG_CAT_CDE));
		prodInfoSeg.setAumChrgProdInd(mappings
				.get(Const.ASET_UNDER_MGMT_CHRG_PROD_IND));
		prodInfoSeg
				.setInvstImigProdInd(mappings.get(Const.INVST_IMIG_PROD_IND));
		prodInfoSeg.setRestrInvstrProdInd(mappings
				.get(Const.RESTR_INVSTR_PROD_IND));
		prodInfoSeg
				.setProdInvstObjText(mappings.get(Const.PROD_INVST_OBJ_TEXT));
		prodInfoSeg.setProdInvstObjPllText(mappings
				.get(Const.PROD_INVST_OBJ_PLL_TEXT));

		prodInfoSeg.setProdInvstObjSllText(mappings
				.get(Const.PROD_INVST_OBJ_SLL_TEXT));

		prodInfoSeg.setInvstInitMinAmt(mappings.get(Const.INVST_INIT_MIN_AMT));

		prodInfoSeg.setNoScribFeeProdInd(mappings
				.get(Const.NO_SCRIB_FEE_PROD_IND));

		prodInfoSeg.setTopSellProdInd(mappings.get(Const.TOP_SELL_PROD_IND));

		prodInfoSeg.setTopPerfmProdInd(mappings.get(Const.TOP_PERFM_PROD_IND));

		prodInfoSeg.setInvstInitMaxAmt(mappings.get(Const.INVST_INIT_MAX_AMT));

		prodInfoSeg.setRcmndProdDecsnCde(mappings
				.get(Const.RCMND_PROD_DECSN_CDE));

		prodInfoSeg.setAsetText(mappings.get(Const.ASET_TEXT));

		prodInfoSeg.setProdDervtCde(mappings.get(Const.PROD_DERVT_CDE));

		prodInfoSeg.setEqtyUndlInd(mappings.get(Const.EQTY_UNDL_IND));

		prodInfoSeg
				.setWlthAccumGoalInd(mappings.get(Const.WLTH_ACCUM_GOAL_IND));

		prodInfoSeg.setPrtyWlthAccumGoalCde(mappings
				.get(Const.PRTY_WLTH_ACCUM_GOAL_CDE));

		prodInfoSeg.setPlanForRtireGoalInd(mappings
				.get(Const.PLAN_FOR_RTIRE_GOAL_IND));

		prodInfoSeg.setPrtyPlnForRtireCde(mappings
				.get(Const.PRTY_PLN_FOR_RTIRE_CDE));

		prodInfoSeg.setEducGoalInd(mappings.get(Const.EDUC_GOAL_IND));

		prodInfoSeg.setPrtyEducCde(mappings.get(Const.PRTY_EDUC_CDE));

		prodInfoSeg.setLiveInRtireGoalInd(mappings
				.get(Const.LIVE_IN_RTIRE_GOAL_IND));

		prodInfoSeg.setPrtyLiveInRtireCde(mappings
				.get(Const.PRTY_LIVE_IN_RTIRE_CDE));

		prodInfoSeg.setProtcGoalInd(mappings.get(Const.PROTC_GOAL_IND));

		prodInfoSeg.setPrtyProtcCde(mappings.get(Const.PRTY_PROTC_CDE));

		prodInfoSeg.setMngSolnInd(mappings.get(Const.MNG_SOLN_IND));

		prodInfoSeg.setPrdInvstTnorNum(mappings.get(Const.PRD_INVST_TNOR_NUM));

		prodInfoSeg.setGeoRiskInd(mappings.get(Const.GEO_RISK_IND));

		prodInfoSeg.setProdLqdyInd(mappings.get(Const.PROD_LQDY_IND));

		prodInfoSeg.setRvrseEnqProdInd(mappings.get(Const.RVRSE_ENQ_PROD_IND));

		prodInfoSeg
				.setProdInvstTypeCde(mappings.get(Const.PROD_INVST_TYPE_CDE));

		prodInfoSeg.setIslmProdInd(mappings.get(Const.ISLM_PROD_IND));
		prodInfoSeg.setCntlAdvcInd(mappings.get(Const.CNTL_ADVC_IND));

		parseUserDefField(prodInfoSeg, mappings);
		parseAsetGeoLocAlloc(prodInfoSeg, mappings);
		parseAsetSicAlloc(prodInfoSeg, mappings);
		parseProdAsetUndl(prodInfoSeg, mappings);
		parseProdAsetVoltlClass(prodInfoSeg, mappings);
		parseProdList(prodInfoSeg, mappings);
		parseProdTradeCcy(prodInfoSeg, mappings);
		parseResChan(prodInfoSeg, mappings);

		return prodInfoSeg;
	}

	private void parseResChan(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		// no need to config, ignore this field
		String restrictChannelCode = ConstantsPropertiesHelper.getValue(
				getJobCode(), ConfigConstant.RES_CHANNEL_CDE);
		if (StringUtils.isNotBlank(restrictChannelCode)) {
			ResChanSeg resChan = new ResChanSeg();
			for (String resChnCde : restrictChannelCode.split("\\,")) {
				ResChannelDtl resChannelDtl = new ResChannelDtl();
				resChannelDtl.setResChannelCde(resChnCde);
				resChan.addResChannelDtl(resChannelDtl);
			}
			prodInfoSeg.setResChanSeg(resChan);
		}
	}

	/**
	 * Parses the aset geo loc alloc.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseAsetGeoLocAlloc(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 70; index++) {
			String geoLocCde = mappings.get(Const.GEO_LOC_CDE + Const.UNDERLINE
					+ index);
			String asetAllocWghtPct = mappings
					.get(Const.GEO_ASET_ALLOC_WGHT_PCT + Const.UNDERLINE
							+ index);
			if (StringUtils.isNotBlank(geoLocCde)
					&& StringUtils.isNotBlank(asetAllocWghtPct)) {
				AsetGeoLocAllocSeg seg = new AsetGeoLocAllocSeg();
				seg.setGeoLocCde(geoLocCde);
				seg.setAsetAllocWghtPct(asetAllocWghtPct);
				prodInfoSeg.addAsetGeoLocAllocSeg(seg);
			}
		}
	}

	/**
	 * Parses the aset sic alloc.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseAsetSicAlloc(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 20; index++) {
			String sicCde = mappings.get(Const.SIC_CDE + Const.UNDERLINE
					+ index);
			String asetAllocWghtPct = mappings
					.get(Const.SIC_ASET_ALLOC_WGHT_PCT + Const.UNDERLINE
							+ index);
			if (StringUtils.isNotBlank(sicCde)
					&& StringUtils.isNotBlank(asetAllocWghtPct)) {
				AsetSicAllocSeg seg = new AsetSicAllocSeg();
				seg.setSicCde(sicCde);
				seg.setAsetAllocWghtPct(asetAllocWghtPct);
				prodInfoSeg.addAsetSicAllocSeg(seg);
			}
		}
	}

	/**
	 * Parses the prod aset undl.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseProdAsetUndl(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 3; index++) {
			String asetUndlCde = mappings.get(Const.ASET_UNDL_CDE
					+ Const.UNDERLINE + index);
			if (StringUtils.isNotBlank(asetUndlCde)) {
				ProdAsetUndlSeg seg = new ProdAsetUndlSeg();
				// TODO: need confirm how to set the seq num
				seg.setAsetUndlCdeSeqNum(String.valueOf(index));
				seg.setAsetUndlCde(asetUndlCde);
				prodInfoSeg.addProdAsetUndlSeg(seg);
			}
		}
	}

	/**
	 * Parses the prod aset voltl class.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseProdAsetVoltlClass(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 70; index++) {
			String asetVoltlClassCde = mappings.get(Const.ASET_VOLTL_CLASS_CDE
					+ Const.UNDERLINE + index);
			String asetVoltlClassWghtPct = mappings
					.get(Const.ASET_VOLTL_CLASS_WGHT_PCT + Const.UNDERLINE
							+ index);
			if (StringUtils.isNotBlank(asetVoltlClassCde)
					&& StringUtils.isNotBlank(asetVoltlClassWghtPct)) {
				ProdAsetVoltlClassSeg seg = new ProdAsetVoltlClassSeg();
				seg.setAsetVoltlClassCde(asetVoltlClassCde);
				seg.setAsetVoltlClassWghtPct(asetVoltlClassWghtPct);
				prodInfoSeg.addProdAsetVoltlClassSeg(seg);
			}
		}
	}

	/**
	 * Parses the prod list.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseProdList(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 3; index++) {
			String prodListIndicator = mappings.get(Const.PROD_SLST_IND
					+ Const.UNDERLINE + index);
			String prodEffSlstDt = mappings.get(Const.PROD_EFF_SLST_DT
					+ Const.UNDERLINE + index);
			if (StringUtils.isNotBlank(prodListIndicator)
					&& StringUtils.isNotBlank(prodEffSlstDt)) {
				ProdListSeg seg = new ProdListSeg();
				seg.setProdListCde(ConstantsPropertiesHelper.getValue(
						this.jobCode, ConfigConstant.PROD_LIST_TYPE_CDE)
						+ Const.UNDERLINE + index);
				seg.setProdListTypeCde(ConstantsPropertiesHelper.getValue(
						this.jobCode, ConfigConstant.PROD_LIST_TYPE_CDE));
				seg.setProdEffSlstDt(DateHelper
						.convertExcelDate2XmlFormat(prodEffSlstDt));
				// ignore this RecCmntText
				// seg.setRecCmntText("");
				prodInfoSeg.addProdListSeg(seg);
			}
		}
	}

	/**
	 * Parses the prod trade ccy.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseProdTradeCcy(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 20; index++) {
			String ccyProdTradeCde = mappings.get(Const.CCY_PROD_TRADE_CDE
					+ Const.UNDERLINE + index);
			if (StringUtils.isNotBlank(ccyProdTradeCde)) {
				ProdTradeCcySeg seg = new ProdTradeCcySeg();
				seg.setCcyProdTradeCde(ccyProdTradeCde);
				prodInfoSeg.addProdTradeCcySeg(seg);
			}
		}
	}

	/**
	 * Parses the user def field.
	 * 
	 * @param prodInfoSeg
	 *            the prod info seg
	 * @param mappings
	 *            the mappings
	 * @throws ParseException
	 *             the parse exception
	 */
	private void parseUserDefField(final ProductInformation prodInfoSeg,
			final Map<String, String> mappings) throws ParseException {
		for (int index = 1; index <= 15; index++) {
			if (StringUtils.isNotBlank(mappings.get(Const.USER_DEFIN_FIELD
					+ Const.UNDERLINE + index))) {
				ProdUserDefSeg prodUserDefSeg = new ProdUserDefSeg();
				prodUserDefSeg.setFieldCde(Constants.FIELD_CDE_PREFIX_UDF
						+ StringUtils.leftPad(Integer.toString(index), 2, "0"));
				prodUserDefSeg.setFieldTypeCde(Constants.FIELD_TYPE_CDE_U);
				prodUserDefSeg.setFieldValueText(mappings
						.get(Const.USER_DEFIN_FIELD + Const.UNDERLINE + index));
				prodInfoSeg.addProdUserDefSeg(prodUserDefSeg);
			}
		}
	}
}
