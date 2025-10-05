package com.dummy.wpc.datadaptor.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.token.FieldInfo;

public class TokenizerHelper {
	private static Logger log = Logger.getLogger(TokenizerHelper.class);
	
	private Resource resource;
	private  List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
	private  String[] names = new String[0];
	
	public TokenizerHelper(Resource resource){
		this.resource = resource;
	}

	public  String[] getNames() {
		return names;
	}
	
	public  List<FieldInfo> initFieldInfoList() {
		List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
		List<String> nameList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new WPCBufferReader(new InputStreamReader(new FileInputStream(resource.getFile())),1048576);
			String lineValue = null;
			while ((lineValue = reader.readLine()) != null) {
				FieldInfo fieldInfo = new FieldInfo();
				fieldInfoList.add(fieldInfo);

				String[] lineSplit = lineValue.split(",");

				if (lineSplit.length > 0) {
					String name = lineSplit[0].trim();
					fieldInfo.setFieldName(name);
					nameList.add(name);
				}
				if (lineSplit.length > 1) {
					int start = Integer.parseInt(lineSplit[1].trim());
					fieldInfo.setStart(start);
				}
				if (lineSplit.length > 2) {
					int offset = Integer.parseInt(lineSplit[2].trim());
					fieldInfo.setOffset(offset);
				}
			}
			names = nameList.toArray(new String[] {});
		} catch (FileNotFoundException e) {
			fieldInfoList = null;
			log.error("can not find the resource.");
		} catch (IOException e) {
			fieldInfoList = null;
			log.error("error occured when read the resource.");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				log.error("error occured when close the resource.");
			}
		}
		return fieldInfoList;
	}
}
