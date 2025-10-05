package com.dummy.wpc.datadaptor.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.batch.item.ExecutionContext;

public class ExecutionContextHelper {
	public static Map copyProperties(ExecutionContext ec){
		Map map = new HashMap();
		for (Iterator iterator = ec.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry)iterator.next();
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	
	public static String getString(ExecutionContext ec, String key){
		Object value = ec.get(key);
		if(value == null){
			return null;
		}
		else{
			return value.toString();
		}
		
	}
}
