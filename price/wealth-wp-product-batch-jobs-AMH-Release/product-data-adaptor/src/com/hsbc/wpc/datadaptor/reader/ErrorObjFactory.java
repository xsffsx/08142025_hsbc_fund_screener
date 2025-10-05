package com.dummy.wpc.datadaptor.reader;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;

public class ErrorObjFactory {
	public static String toString(Object badObj){
		String str = "";
		if(badObj == null){
			str = "";
		}
		return str;
	}
}
