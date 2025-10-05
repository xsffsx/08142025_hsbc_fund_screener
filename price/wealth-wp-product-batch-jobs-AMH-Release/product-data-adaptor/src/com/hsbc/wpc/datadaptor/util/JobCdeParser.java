package com.dummy.wpc.datadaptor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JobCdeParser {
	private static Logger log = Logger.getLogger(JobCdeParser.class);
	private static final char BLOCK_SEPARATER_START = '[';
	private static final char BLOCK_SEPARATER_END = ']';
	private static final char ITEM_DELIMITER = ',';

	public static List<String> parse(String jobCdeStr) {
		if (log.isDebugEnabled()) {
			log.debug("Parse jobCde string begin.");
		}
		// check null
		if (null == jobCdeStr) {
			log.error("The jobCde string is " + jobCdeStr);
			return null;
		}
		// validate the string to be parsed
		if (!validate(jobCdeStr)) {
			log.error("The jobCde string is invalid, Number of '[' and ']' don't match: " + jobCdeStr);
			return null;
		}

		List<String> resultList;
		Stack<Object> stack = new Stack<Object>();
		StringBuffer strBuff = new StringBuffer();

		char[] chars = jobCdeStr.toCharArray();
		for (char c : chars) {
			if (c == BLOCK_SEPARATER_START) {
				// push the segment that before '[' into stack
				pushStackCleanStringBuffer(stack, strBuff);
				// push '[' into stack
				strBuff.append(c);
				pushStackCleanStringBuffer(stack, strBuff);
			} else if (c == BLOCK_SEPARATER_END) {
				// push the segment that before ']' into stack
				pushStackCleanStringBuffer(stack, strBuff);

				handleBlockSepEnd(stack);
			} else if (c == ITEM_DELIMITER) {
				// push the segment that before ',' into stack
				pushStackCleanStringBuffer(stack, strBuff);
				// push ',' into stack
				strBuff.append(c);
				pushStackCleanStringBuffer(stack, strBuff);
			} else {
				strBuff.append(c);
			}
		}
		// push the rest segment into stack
		pushStackCleanStringBuffer(stack, strBuff);

		// retrieve the parsed list from stack
		resultList = handleBlockSepEnd(stack);
		Collections.reverse(resultList);

		if (log.isDebugEnabled()) {
			log.debug("Total number of jobCde: " + resultList.size());
			log.debug(resultList);
			log.debug("Parse jobCde string end.");
		}
		return resultList;
	}

	private static void pushStackCleanStringBuffer(Stack<Object> stack, StringBuffer strBuff) {
		if (strBuff.length() > 0) {
			stack.push(strBuff.toString());
			// clear StringBuffer obj
			strBuff.setLength(0);
		}
	}

	private static List<String> handleBlockSepEnd(Stack<Object> stack) {
		List<String> resultList = new ArrayList<String>();

		do {
			if (stack.isEmpty()) {
				break;
			}
			Object obj = stack.pop();
			if (String.valueOf(obj).equals(String.valueOf(ITEM_DELIMITER))) {
				List<String> tempResultList = handleItemSep(stack);
				resultList.addAll(tempResultList);
				continue;
			} else if (String.valueOf(obj).equals(String.valueOf(BLOCK_SEPARATER_START))) {
				stack.push(resultList);
				break;
			} else {
				if (obj instanceof List) {
					resultList = cartesianProduct(((List<String>) obj), resultList);
				} else {
					List<String> tempList = new ArrayList<String>();
					tempList.add(String.valueOf(obj));
					resultList = cartesianProduct(tempList, resultList);
				}
			}
		} while (true);

		return resultList;
	}

	private static List<String> handleItemSep(Stack<Object> stack) {
		List<String> tempResultList = new ArrayList<String>();

		do {
			if (stack.isEmpty()) {
				break;
			}
			Object obj = stack.pop();
			if (String.valueOf(obj).equals(String.valueOf(ITEM_DELIMITER))) {
				stack.push(obj);
				break;
			} else if (String.valueOf(obj).equals(String.valueOf(BLOCK_SEPARATER_START))) {
				// push back the '[' into stack for handleBlockSepEnd() use
				stack.push(obj);
				break;
			} else {
				if (obj instanceof List) {
					tempResultList = cartesianProduct(((List<String>) obj), tempResultList);
				} else {
					List<String> tempList = new ArrayList<String>();
					tempList.add(String.valueOf(obj));
					tempResultList = cartesianProduct(tempList, tempResultList);
				}
			}
		} while (true);

		return tempResultList;
	}

	private static List<String> cartesianProduct(List<String> list1, List<String> list2) {
		List<String> resultList = new ArrayList<String>();

		if (list1 == null || list1.size() == 0) {
			resultList = list2;
			return resultList;
		}
		if (list2 == null || list2.size() == 0) {
			resultList = list1;
			return resultList;
		}
		for (String str1 : list1) {
			for (String str2 : list2) {
				resultList.add(str1 + str2);
			}
		}

		return resultList;
	}

	private static boolean validate(String str) {
		return StringUtils.countMatches(str, String.valueOf(BLOCK_SEPARATER_START)) == StringUtils.countMatches(str, String.valueOf(BLOCK_SEPARATER_END));
	}
}
