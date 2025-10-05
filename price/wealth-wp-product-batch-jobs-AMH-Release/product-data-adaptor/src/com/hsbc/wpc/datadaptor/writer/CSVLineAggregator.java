package com.dummy.wpc.datadaptor.writer;

import org.springframework.batch.item.file.mapping.FieldSet;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CSVLineAggregator implements LineAggregator {
	private String delimiter = ",";
	private String textGualifier = "\"";
	private boolean handleTextGualifier = true;
	/**
	 * Method used to create string representing object.
	 * 
	 * @param fieldSet arrays of strings representing data to be stored
	 */
	public String aggregate(FieldSet fieldSet) {
		StringBuffer buffer = new StringBuffer();
		String[] args = fieldSet.getValues();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if(arg == null){
				arg = "";
			}
			if(handleTextGualifier){
//				if(!arg.startsWith(textGualifier) && !arg.endsWith(textGualifier)){
				arg = arg.replaceAll(textGualifier, textGualifier + textGualifier);
				arg = textGualifier + arg + textGualifier;
//				}
			}
			buffer.append(arg);

			if (i != (args.length - 1)) {
				buffer.append(delimiter);
			}
		}

		return buffer.toString();
	}

	public void setHandleTextGualifier(boolean handleTextGualifier) {
		this.handleTextGualifier = handleTextGualifier;
	}

	/**
	 * Sets the character to be used as a delimiter.
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setTextGualifier(String textGualifier) {
		this.textGualifier = textGualifier;
	}
	
	public static void main(String[] args){
		String str = "a\"b\"";
		System.out.println(str);
		System.out.println(str.replaceAll("\"", "\"\""));
	}
	
	
}
