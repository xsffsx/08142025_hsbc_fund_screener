package com.dummy.wpc.datadaptor.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiWriterObj {// MultiWriterObj
	private Object[] array;

	private String identifier;

	public Object[] getArray() {
		return array;
	}

	public void setArray(Object[] array) {
		this.array = array;
	}

	public void addObj(Object obj) {
		// array = ArrayUtils.add(array, obj);
		List list = new ArrayList();
		if (array != null && array.length > 0) {
			Collections.addAll(list, array);
		}
		Collections.addAll(list, new Object[] { obj });
		array = list.toArray(new Object[list.size()]);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public boolean areAllObjectsNull(){
		boolean isNull = true;
		if(array != null && array.length > 0){
			for(Object obj : array){
				if(obj != null){
					return false;
				}
			}
		}
		
		return isNull;
	}
}
