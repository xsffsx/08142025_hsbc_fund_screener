package com.dummy.wpc.datadaptor.token;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import com.dummy.wpc.datadaptor.util.FixedLengthCommonUtility;

public class GSOPSFixedLengthTokenizer extends AbstractLineTokenizer {

	private static final Logger logger = Logger.getLogger(GSOPSFixedLengthTokenizer.class);
	
	private String[] types = null;

	private Range[] ranges;
	private String[] name;

	/**
	 * Set the column ranges. Used in conjunction with the
	 * {@link RangeArrayPropertyEditor} this property can be set in the form of a
	 * String describing the range boundaries, e.g. "1,4,7" or "1-3,4-6,7" or
	 * "1-2,4-5,7-10".
	 * 
	 * @param ranges
	 *            the column ranges expected in the input
	 */
	public void setColumns(Range[] ranges) {
		this.ranges = ranges;
	}

	public Range[] getColumns() {
		return this.ranges;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	/**
	 * Yields the tokens resulting from the splitting of the supplied
	 * <code>line</code>.
	 * 
	 * @param line
	 *            the line to be tokenised (can be <code>null</code>)
	 * 
	 * @return the resulting tokens (empty if the line is null)
	 */
	protected List doTokenize(String line) {
		List tokens = new ArrayList(ranges.length);
		int lineLength;
		String token;
		String tokenFormate = "ASCII";
		List<String> name0 = new ArrayList();
		for (int z = 0; z < this.names.length; z++) {
			name0.add(this.names[z].trim());
		}
		name = (String[]) name0.toArray(new String[name0.size()]);
		super.setNames(name);
		lineLength = line.length();

		int i = 0;
		try {
			for (i = 0; i < ranges.length; i++) {
				if (types != null) {
					tokenFormate = types[i];
				}
				int startPos = ranges[i].getMin() - 1;
				int endPos = ranges[i].getMax();
				int length = endPos - startPos;
				int precision;
				// StringTokenizer stringTokenizer=new StringTokenizer(tokenFormate,")");
				if (lineLength >= endPos) {
					token = line.substring(startPos, endPos);
					if (tokenFormate.trim().equalsIgnoreCase("EBCDIC")) {
						token = FixedLengthCommonUtility.formateEBCDIC(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("EBCDIC_PLAIN")) {
						token = FixedLengthCommonUtility.formateEBCDIC_PLAIN(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("TRADCHN")) {
						token = FixedLengthCommonUtility.formateEBCDICTRADCHN(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("SIMPCHN")) {
						token = FixedLengthCommonUtility.formateEBCDICSIMPCHN(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("ASCII")) {

					} else if (((String) tokenFormate.trim().subSequence(0, 6)).equalsIgnoreCase("PACKED")) {

						String value = tokenFormate.trim().substring(7, tokenFormate.trim().length() - 1);
						String len = value.substring(0, value.lastIndexOf("_"));
						String pre = value.substring(value.lastIndexOf("_") + 1, value.length());
						length = Integer.parseInt(len);
						precision = Integer.parseInt(pre);

						token = FixedLengthCommonUtility.formatePACKED(token, length, precision).toEngineeringString();

					} else {

					}
				} else if (lineLength >= startPos) {
					token = line.substring(startPos);
					if (tokenFormate.trim().equalsIgnoreCase("EBCDIC")) {
						token = FixedLengthCommonUtility.formateEBCDIC(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("EBCDIC_PLAIN")) {
						token = FixedLengthCommonUtility.formateEBCDIC_PLAIN(token, length);
					} else if (tokenFormate.trim().equalsIgnoreCase("ASCII")) {

					} else if (tokenFormate.substring(0, 6).trim().equalsIgnoreCase("PACKED")) {

						String value = tokenFormate.substring(8, tokenFormate.length() - 1);
						String len = value.substring(8, value.lastIndexOf("_") - 1);
						String pre = value.substring(value.lastIndexOf("_") + 1, value.length());
						length = Integer.parseInt(len);
						precision = Integer.parseInt(pre);
						// String a=stringTokenizer.toString();

						token = FixedLengthCommonUtility.formatePACKED(token, length, precision).toEngineeringString();
					}
				} else {
					token = "";
				}

				tokens.add(token);

			}
		} catch (Exception ex) {
			if (tokens != null) {
				if (name[i] != null) {
					if (logger.isInfoEnabled()) {
						logger.error("Error field name: " + name[i]);
						logger.error("Token format: " + tokenFormate);
					}
				}
				if (logger.isInfoEnabled()) {
					logger.error(tokens.toString(), ex);
				}
				if (logger.isInfoEnabled()) {
					logger.error(tokens.toString(), ex);
				}
			}
			throw new RuntimeException(ex);
		}

		return tokens;
	}

	

}
