package com.dummy.wpc.datadaptor.mapper;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.dummy.wpc.batch.xml.Sid;
import com.dummy.wpc.batch.xml.SidRtrnSeg;
import com.dummy.wpc.batch.xml.SidSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.reader.Sheet;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;

public class SidProdRowMapper extends GnrcProdRowMapper {

	@Override
	public MultiWriterObj mapRow(final Sheet sheet,
			final Map<String, String> mappings) throws Exception {

		MultiWriterObj multiWriterObj = new MultiWriterObj();

		Sid sid = new Sid();

		sid.setProdAltNumSeg(parseProdAltNumSeg(sheet, mappings));
		sid.setProdKeySeg(parseProdKeySeg(sheet, mappings));
		sid.setRecDtTmSeg(parseRecDtTmSeg(sheet, mappings));
		sid.setProdInfoSeg(parseProdInfo(sheet, mappings));

		sid.setSidSeg(convertSidProd(sheet, mappings));

		String[] ctryList = loadReplicaCtryList(getJobCode());
		for (String ctry : ctryList) {
			String grbMebCde = ConstantsPropertiesHelper.getValue(getJobCode(),
					ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + Const.PERIOD
							+ ctry);
			Sid instance = new Sid();
			BeanUtils.copyProperties(sid, instance);
			instance.setProdKeySeg(replicaProdKeySeg(instance.getProdKeySeg(),
					ctry, grbMebCde));
			instance.setProdInfoSeg(assignConfigurableValue(
					instance.getProdInfoSeg(), ctry, grbMebCde));
			multiWriterObj.addObj(instance);
		}
		return multiWriterObj;
	}

	private SidSeg convertSidProd(final Sheet sheet,
			final Map<String, String> mappings) throws ParseException {

		SidSeg seg = new SidSeg();

		seg.setProdExtnlCde(mappings.get(Const.PROD_EXTNL_CDE));
		seg.setProdExtnlTypeCde(mappings.get(Const.PROD_EXTNL_TYPE_CDE));
		seg.setProdConvCde(mappings.get(Const.PROD_CONV_CDE));
		seg.setRtrnIntrmPrevPct(mappings.get(Const.RTRN_INTRM_PREV_PCT));
		seg.setRtrnIntrmPaidPrevDt(DateHelper
				.convertExcelDate2XmlFormat(mappings
						.get(Const.RTRN_INTRM_PAID_PREV_DT)));
		seg.setRtrnIntrmPaidNextDt(DateHelper
				.convertExcelDate2XmlFormat(mappings
						.get(Const.RTRN_INTRM_PAID_NEXT_DT)));
		seg.setCcyLinkDepstCde(mappings.get(Const.CCY_LINK_DEPST_CDE));
		seg.setMktStartDt(DateHelper.convertExcelDate2XmlFormat(mappings
				.get(Const.MKT_START_DT)));
		seg.setMktEndDt(DateHelper.convertExcelDate2XmlFormat(mappings
				.get(Const.MKT_END_DT)));
		seg.setYieldAnnlMinPct(mappings.get(Const.YIELD_ANNL_MIN_PCT));
		seg.setYieldAnnlPotenPct(mappings.get(Const.YIELD_ANNL_POTEN_PCT));
		seg.setAllowEarlyRdmInd(mappings.get(Const.ALLOW_EARLY_RDM_IND));
		seg.setRdmEarlyDalwText(mappings.get(Const.RDM_EARLY_DALW_TEXT));
		seg.setRdmEarlyIndAmt(mappings.get(Const.RDM_EARLY_IND_AMT));
		seg.setOfferTypeCde(mappings.get(Const.OFFER_TYPE_CDE));
		seg.setCustSellQtaNum(mappings.get(Const.CUST_SELL_QTA_NUM));
		seg.setRuleQtaAltmtCde(mappings.get(Const.RULE_QTA_ALTMT_CDE));
		seg.setBonusIntCalcTypeCde(mappings.get(Const.BONUS_INT_CALC_TYPE_CDE));
		seg.setBonusIntDtTypeCde(mappings.get(Const.BONUS_INT_DT_TYPE_CDE));
		seg.setCptlProtcPct(mappings.get(Const.CPTL_PROTC_PCT));
		seg.setLnchProdInd(mappings.get(Const.LNCH_PROD_IND));
		seg.setRtrvProdExtnlInd(mappings.get(Const.RTRV_PROD_EXTNL_IND));

		// SID sub-table
		for (int index = 1; index <= 24; index++) {
			String rtrnIntrmPaidDt = mappings.get(Const.RTRN_INTRM_PAID_DT
					+ Const.UNDERLINE + index);
			String rtrnIntrmPct = mappings.get(Const.RTRN_INTRM_PCT
					+ Const.UNDERLINE + index);

			if (StringUtils.isNotBlank(rtrnIntrmPaidDt)
					&& StringUtils.isNotBlank(rtrnIntrmPct)) {

				SidRtrnSeg sidRtrnSeg = new SidRtrnSeg();
				sidRtrnSeg.setRtrnIntrmPaidDt(DateHelper
						.convertExcelDate2XmlFormat(rtrnIntrmPaidDt));
				sidRtrnSeg.setRtrnIntrmPct(rtrnIntrmPct);

				seg.addSidRtrnSeg(sidRtrnSeg);
			}
		}

		return seg;
	}
}
