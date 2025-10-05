package com.dummy.wpc.datadaptor.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author WMDHKG0007
 * 
 */
public class FilePatternHandler {
	private static Logger log = Logger.getLogger(FilePatternHandler.class);

	public static final char LEFT_ANGLE_BRACKET = '<';
	public static final char RIGHT_ANGLE_BRACKET = '>';
	public static final char STAR_CHAR = '*';
	public static final char DOT_CHAR = '.';

	public static final String LEFT_ANGLE_BRACKET_STR = "<";
	public static final String RIGHT_ANGLE_BRACKET_STR = ">";
	public static final String STAR_STR = "*";
	public static final String DOT_STR = ".";
	public static final String REGEX_DOT_STR = "\\.";


	// TW_dummy_BOND_20111010-101010_43188103.txt
	public static boolean isFilePattern(String fileName, Map<String, String> configMap,int readIndex, Map<String, Map<String, String>> valueMapView) {
		// whether the input file pattern is valid
		String keyPrefix = "";
		if(readIndex > 0){
			keyPrefix = "reader." + readIndex + ".";
		}
		String inputFilePattern = configMap.get(keyPrefix + Constants.INPUT_FILE_PATTERN);

		// split the input file pattern
		List<String> patternStrs = splitPattern(inputFilePattern);
		if (patternStrs == null) {
			return false;
		}
		String fileNamePattern = configMap.get(Constants.FILE_NAME_PATTERN);
		SimpleDateFormat format = new SimpleDateFormat(fileNamePattern);

		Map<String, String> valueMap = new HashMap<String, String>();
		String curArg = null;
		int preIndex = 0;
		int curIndex = 0;
		boolean speFlg = false;

		for (int i = 0; i < patternStrs.size(); i++) {
			
			String pattern = patternStrs.get(i);
						
			if(fileName.matches(pattern)){				
				return true;
			}
			
			if ((pattern.startsWith(LEFT_ANGLE_BRACKET_STR) && pattern.endsWith(RIGHT_ANGLE_BRACKET_STR)) || pattern.equals(STAR_STR)) {
				curArg = pattern;
				// end with <p1>
				// RSSCPIP.S<p1>
				if (i == (patternStrs.size() - 1)) {
					speFlg = true;
				}
				continue;
			} else {
				curIndex = fileName.indexOf(pattern, preIndex);
				
				if (curIndex != -1 ) {
					if (curArg != null) {
						// [<p1>, _, <p2>, _, <p3>, _, <p4>, _, <p5>, .csv]
						// TW_dummy_BOND_20110627-142500_43188103.csv
						String value = fileName.substring(preIndex, curIndex);
						if (curArg.equals(STAR_STR)) {
							if (!DateHelper.pattern(value, format)) {
								log.error("the date part \"" + value + "\" do not match the date format \"" + format + "\"");
								return false;
							}
						} else {
							valueMap.put(curArg, value);
						}
					}
					else{
						if(!fileName.startsWith(pattern, preIndex)){
							log.error("the file name \"" + fileName + "\" can not match the pattern \"" + pattern + "\"");
							return false;
						}
					}
					preIndex = curIndex + pattern.length();
				} else {
					log.error("the file name \"" + fileName + "\" can not match the pattern \"" + pattern + "\"");
					return false;
				}
			}
		}

		String last = patternStrs.get(patternStrs.size() - 1);
		if (last.startsWith(LEFT_ANGLE_BRACKET_STR) && last.endsWith(RIGHT_ANGLE_BRACKET_STR)) {
			String value = fileName.substring(preIndex, fileName.length());
			valueMap.put(curArg, value);
		} else if (last.equals(STAR_STR)) {
			String dateTimePart = fileName.substring(preIndex);
			if (!DateHelper.pattern(dateTimePart, format)) {
				return false;
			}
		}

		// case: the fileName large than input_file_pattern, that mean the
		// fileName ends with some contents which don't define in
		// input_file_pattern
		if (!speFlg && fileName.length() - preIndex > 0) {
			return false;
		}
		
		
		boolean match = patternAccordingRules(valueMap, configMap,keyPrefix);
//		if(match == false){
//			return false;
//		}
		
//		match = checkACK(fileName,configMap,keyPrefix);
		if(match){
			valueMapView.put(keyPrefix + fileName, valueMap);
		}
		return match;
	}

	

	private static boolean patternAccordingRules(Map<String, String> valueMap, Map<String, String> regexMap,String keyPrefix) {
		Pattern pattern = null;
		Matcher m = null;
		boolean match = true;

		Set<String> keys = valueMap.keySet();

		for (String key : keys) {
			String value = valueMap.get(key);
			String regex = regexMap.get(keyPrefix + key);
			if (StringUtils.isEmpty(regex)) {
				continue;
			}
			if (value == null) {
				value = "";
			}

			pattern = Pattern.compile(regex);
			m = pattern.matcher(value);
			match = m.matches();
			if (match) {
				continue;
			} else {
				log.error("the value " + value + " do not match the regex " + regex);
				return false;
			}
		}
		return match;
	}

	/*
	 * split the pattern String to a String list
	 * <p1>_<p2>KKK<p3>.txt<p4>_*_<p5>_*.txt --------------------------------->
	 * <p1> _ <p2> KKK <p3> .txt <p4> _ * _ <p5> _ * .txt
	 */
	public static List<String> splitPattern(String pattern) {
		List<String> list = new ArrayList<String>();
		List<Character> argumentsList = new ArrayList<Character>();
		List<Character> constantsList = new ArrayList<Character>();
		char[] charArray = pattern.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			switch (charArray[i]) {
			case LEFT_ANGLE_BRACKET:
				argumentsList.add(charArray[i]);
				break;
			case STAR_CHAR:
				if (constantsList.size() > 0) {
					list.add(charList2String(constantsList).trim());
					constantsList.clear();
				}
				list.add(String.valueOf(charArray[i]).trim());
				break;
			case RIGHT_ANGLE_BRACKET:
				if (argumentsList.contains(LEFT_ANGLE_BRACKET)) {
					argumentsList.add(charArray[i]);
				} else {
					constantsList.add(charArray[i]);
					break;
				}

			default:
				if (argumentsList.contains(LEFT_ANGLE_BRACKET) && !argumentsList.contains(RIGHT_ANGLE_BRACKET)) {
					argumentsList.add(charArray[i]);
					if (constantsList.size() > 0) {
						list.add(charList2String(constantsList).trim());
						constantsList.clear();
					}
				} else if (argumentsList.contains(LEFT_ANGLE_BRACKET) && argumentsList.contains(RIGHT_ANGLE_BRACKET)) {
					list.add(charList2String(argumentsList).trim());
					argumentsList.clear();
				} else {
					constantsList.add(charArray[i]);
				}
				break;
			}
		}

		if (constantsList.size() > 0) {
			list.add(charList2String(constantsList).trim());
		}
		return list;
	}

	/*
	 * change char list to String ['a','b','c']--> abc
	 */
	private static String charList2String(List<Character> charList) {
		Object[] object = charList.toArray();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < object.length; i++) {
			buffer.append(object[i]);
		}
		return buffer.toString();
	}

	public static String list2String(List<String> list) {
		StringBuffer buffer = new StringBuffer();
		for (String s : list) {
			buffer.append(s);
		}
		return buffer.toString().trim();
	}
}
