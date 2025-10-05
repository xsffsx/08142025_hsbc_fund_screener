package com.dummy.wpc.datadaptor.beanconverter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.DefaultFieldSet;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;

public class ProdTypeListBeanConverter extends AbstractBeanConverter{

	private void putValue(List valueList, String value){
		if(value == null){
			value = "";
		}
		//value = "\"" + value + "\"";
		valueList.add(value);
	}
	
	public Object convert(Object source) {
		
		if(source==null){
			return null;
		}
		if(!(source instanceof MultiWriterObj)){
			throw new IllegalArgumentException("The source must be a instance of com.dummy.wpc.datadaptor.mapper.MultiWriterObj.");
		}
		MultiWriterObj multiObj=(MultiWriterObj)source;
		List valueList = new ArrayList();
		
		if(multiObj.getArray() != null && multiObj.getArray().length == 1){
			GnrcProd grp = (GnrcProd) multiObj.getArray()[0];
			String prodAltNum = null;
			if(grp != null){
				putValue(valueList,grp.getProdKeySeg().getProdTypeCde());
				putValue(valueList,grp.getProdInfoSeg().getProdSubtpCde());
				ProdAltNumSeg[] prodAltNumSegArray = grp.getProdAltNumSeg();
				for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegArray) {
					if (StringUtils.equals(prodAltNumSeg.getProdCdeAltClassCde(), "P")) {
						prodAltNum = prodAltNumSeg.getProdAltNum();
						break;
					}
				}
				putValue(valueList,prodAltNum);
//				putValue(valueList,grp.getProdKeySeg().getProdCde());
				putValue(valueList,grp.getProdInfoSeg().getProdName());
			}
			
		}
		
		String[] values= new String[valueList.size()];
		valueList.toArray(values);
		FieldSet result = new DefaultFieldSet(values);
		return result;
	}

}
