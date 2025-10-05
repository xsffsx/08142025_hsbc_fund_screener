package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ConfigMapHelper {
	public static boolean isMultiWriter(Map<String, String> configMap) {
		String writerCountStr = configMap.get(Constants.WRITER_COUNT);
		int writerCount = 0;
		if (!StringUtils.isEmpty(writerCountStr)) {
			writerCount = Integer.parseInt(writerCountStr);
		}
		boolean multiWriter = writerCount > 0;
		return multiWriter;
	}
	
	
	public static boolean isMultiReader(Map<String, String> configMap) {
		String readerCountStr = configMap.get(Constants.READER_COUNT);
		int readerCount = 0;
		if (!StringUtils.isEmpty(readerCountStr)) {
			readerCount = Integer.parseInt(readerCountStr);
		}
		boolean multiReader = readerCount > 0;
		return multiReader;
	}
	
	public static void renameOutputFileName(Map<String, String> configMap) {
		boolean multiWriter = isMultiWriter(configMap);
		if (multiWriter) {
			int writerCount = Integer.valueOf(configMap.get(Constants.WRITER_COUNT));
			for (int i = 1; i <= writerCount; i++) {
				String itemPrefix = "writer." + i + ".";
				renameOutputFileName(configMap, itemPrefix);
			}
		}
		else{
			renameOutputFileName(configMap, "");
		}
	}

//	public static void renameOutputFileName(Map<String, String> configMap, String itemPrefix) {
////		String outputFilePath = configMap.get(itemPrefix + Constants.OUTPUT_FILE_PATH);
//		String realOutputFilePath = configMap.get(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
//		configMap.put(itemPrefix + Constants.OUTPUT_FILE_PATH,realOutputFilePath);
//		configMap.remove(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
//	}
	
	private static void renameOutputFileName(Map<String, String> configMap, String itemPrefix) {
		String outputFilePath = configMap.get(itemPrefix + Constants.OUTPUT_FILE_PATH);
		String realOutputFilePath = configMap.get(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
		File outputFile = new File(outputFilePath);
		File realOutputFile = new File(realOutputFilePath);
		if(realOutputFile.exists()){
			realOutputFile.delete();
		}
		outputFile.renameTo(realOutputFile);
		configMap.put(itemPrefix + Constants.OUTPUT_FILE_PATH,realOutputFilePath);
		configMap.remove(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
	}
	
	public static void replaceTempOutputFileName(Map<String, String> configMap) {
		boolean multiWriter = isMultiWriter(configMap);
		if (multiWriter) {
			int writerCount = Integer.valueOf(configMap.get(Constants.WRITER_COUNT));
			for (int i = 1; i <= writerCount; i++) {
				String itemPrefix = "writer." + i + ".";
				renameTempOutputFileName(configMap, itemPrefix);
			}
		}
		else{
			renameTempOutputFileName(configMap, "");
		}
	}
	
	private static void renameTempOutputFileName(Map<String, String> configMap, String itemPrefix) {
		String realOutputFilePath = configMap.get(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
		configMap.put(itemPrefix + Constants.OUTPUT_FILE_PATH,realOutputFilePath);
		configMap.remove(itemPrefix + Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
	}
}
