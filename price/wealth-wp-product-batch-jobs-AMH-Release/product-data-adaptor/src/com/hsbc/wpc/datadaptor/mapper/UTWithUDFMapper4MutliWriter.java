package com.dummy.wpc.datadaptor.mapper;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;

public class UTWithUDFMapper4MutliWriter extends UtInstmCommonMapper4MutliWriter {

	public Object mapLine(FieldSet fieldSet) {
		MultiWriterObj arr = (MultiWriterObj) super.mapLine(fieldSet);
		
		UtTrstInstm utTrstInstm = (UtTrstInstm) arr.getArray()[0];
		ProdInfoSeg prodInfo = utTrstInstm.getProdInfoSeg();
		
		List<String> names_list = Arrays.asList(fieldSet.getNames());
		for (int i = 1; i <= 15; i++) {
			int index = names_list.indexOf(Constants.USR_DEF_FLD_PREFIX + i);
			if (index >= 0) {
				ProdUserDefSeg prodUserDef = new ProdUserDefSeg();
				prodUserDef.setFieldTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "field_type_cde"));
				String fieldCdePrefix = ConstantsPropertiesHelper.getValue(getJobCode(), "field_cde_prefix");
				String fieldCdeSuffix = String.valueOf(100 + i).substring(1);
				prodUserDef.setFieldCde(fieldCdePrefix + fieldCdeSuffix);
				prodUserDef.setFieldValueText(fieldSet.readString(Constants.USR_DEF_FLD_PREFIX + i));
				prodInfo.addProdUserDefSeg(prodUserDef);
			}
		}
		
		return arr;
	}
	
}
