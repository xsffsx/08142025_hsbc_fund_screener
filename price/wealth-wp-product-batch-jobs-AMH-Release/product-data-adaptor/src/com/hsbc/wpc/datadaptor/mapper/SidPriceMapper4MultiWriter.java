package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.SidProdPrcSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

/**
 * 
 * 
 * 
 * @version $Revision: 1.1.2.3 $ $Date: 2013/07/08 11:43:42CST $
 */
public class SidPriceMapper4MultiWriter extends AbstractFieldSetMapper {

	private static final Logger logger = Logger
			.getLogger(SidPriceMapper4MultiWriter.class);

	@Override
	public Object mapLine(final FieldSet fieldSet) {
		MultiWriterObj arr = new MultiWriterObj();

		String replica_ctry_list = ConstantsPropertiesHelper.getValue(
				getJobCode(), ConfigConstant.REPLICATION_COUNTRY_LIST);

		if (StringUtils.isBlank(replica_ctry_list)) {
			String key = getJobCode() + "."
					+ ConfigConstant.REPLICATION_COUNTRY_LIST;

			SidPriceMapper4MultiWriter.logger.error("Can't found configure["
					+ key + "] for replication price files.");
			throw new IllegalArgumentException("Can't found configure[" + key
					+ "]!");
		}

		String[] ctryList = StringUtils.split(replica_ctry_list, ",");
		for (String ctry : ctryList) {
			arr.addObj(convertPrice(fieldSet, ctry));
		}

		return arr;
	}

	private ProdPrc convertPrice(final FieldSet fs, final String ctry_cde) {
		String grp_membr_cde = ConstantsPropertiesHelper.getValue(getJobCode(),
				ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + "." + ctry_cde);

		if (StringUtils.isBlank(grp_membr_cde)) {
			String key = getJobCode() + "."
					+ ConfigConstant.REPLICATION_GROUP_MEMBER_CDE + "."
					+ ctry_cde;

			SidPriceMapper4MultiWriter.logger.error("Can't found configure["
					+ key + "] for replication price files.");
			throw new IllegalArgumentException("Can't found configure[" + key
					+ "]!");
		}

		// SID Price
		// CTRYCDE,GRPMBRCDE,PRODTYPCDE,PRODCDE,
		// PRCEARLYRDMEFFDT,PRCEARLYRDMPCT,RECCRTDTTM,RECUPDTDTTM,TIMEZONE

		ProdPrc prodPrc = new ProdPrc();

		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ctry_cde);
		prodKey.setGrpMembrRecCde(grp_membr_cde);
		prodKey.setProdCde(StringUtils.trimToEmpty(fs.readString("PRODCDE")));
		prodKey.setProdTypeCde(Const.STRUCTURE_INVESTMENT_DEPOSIT);
		prodPrc.setProdKeySeg(prodKey);

		SidProdPrcSeg sidProdPrcSeg = new SidProdPrcSeg();

		sidProdPrcSeg.setPrcEarlyRdmEffDt(DateHelper.formatDate2String(
				DateHelper.parseToDate(fs.readString("PRCEARLYRDMEFFDT"),
						DateHelper.DEFAULT_DATE_FORMAT),
				DateHelper.DEFAULT_DATE_FORMAT));
		sidProdPrcSeg.setPrcEarlyRdmPct(DecimalHelper.trimZero(fs
				.readString("PRCEARLYRDMPCT")));

		RecDtTmSeg recDtTm = new RecDtTmSeg();
		recDtTm.setRecCreatDtTm(StringUtils.trimToEmpty(fs
				.readString("RECCRTDTTM")));
		recDtTm.setRecUpdtDtTm(StringUtils.trimToEmpty(fs
				.readString("RECUPDTDTTM")));
		recDtTm.setTimeZone(StringUtils.trimToEmpty(fs.readString("TIMEZONE")));
		sidProdPrcSeg.setRecDtTmSeg(recDtTm);

		prodPrc.setSidProdPrcSeg(sidProdPrcSeg);
		return prodPrc;
	}

}
