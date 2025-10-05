package com.dummy.wpc.datadaptor.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.ibm.as400.access.AS400PackedDecimal;
import com.ibm.as400.access.AS400Text;

public class FixedLengthCommonUtility {

	private static final Logger logger = Logger.getLogger(FixedLengthCommonUtility.class);

	private static final String DEFAULT_ENCODING = "ISO-8859-1";

	public static String formateEBCDIC(String token, int length) {

		String text = null;
		try {
			String asciiString = formateEBCDIC_PLAIN(token, length);
			text = highOrLowEBCDICValueConvertToASCII(asciiString, "", "");
			text = replaceNonPrintableCharacterWithSpace(text);

		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("FixedLengthTokenizer error", e);
			}
		}

		return text;
	}

	public static String formateEBCDICSIMPCHN(String token, int length) {
		String text = null;
		try {
			AS400Text textConverter = new AS400Text(length);
			text = (String) textConverter.toObject(token.getBytes(DEFAULT_ENCODING));
			byte[] hostBytes = text.getBytes("Cp037");
			text = new String(hostBytes, "Cp1388");
		} catch (UnsupportedEncodingException e) {
			if (logger.isInfoEnabled()) {
				logger.error("FixedLengthTokenizer error", e);
			}
		}
		return text;

	}

	public static String formateEBCDICTRADCHN(String token, int length) {
		String text = null;
		try {
			AS400Text textConverter = new AS400Text(length);
			text = (String) textConverter.toObject(token.getBytes(DEFAULT_ENCODING));
			byte[] hostBytes = text.getBytes("Cp037");
			text = new String(hostBytes, "Cp937");
			text = highOrLowEBCDICValueConvertToASCII(text, "", "");
			// text = text.replaceAll("\uf817", "\u6052");
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("FixedLengthTokenizer error", e);
			}
		}
		return text;

	}

	public static BigDecimal formatePACKED(String token, int length, int precision) {
		BigDecimal value = null;
		// BigDecimal test=new BigDecimal("1234567890.123456789");
		// System.out.println(test.toPlainString());
		try {
			AS400PackedDecimal convertor = new AS400PackedDecimal(length, precision);
			value = (BigDecimal) convertor.toObject(token.getBytes(DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			if (logger.isInfoEnabled()) {
				logger.error("FixedLengthTokenizer error", e);
			}
		}
		return value;
	}

	public static String formateEBCDIC_PLAIN(String token, int length) {

		int leng = token.length();
		String text = null;
		try {
			AS400Text textConverter = new AS400Text(length);
			text = (String) textConverter.toObject(token.getBytes(DEFAULT_ENCODING));

		} catch (UnsupportedEncodingException e) {
			if (logger.isInfoEnabled()) {
				logger.error("FixedLengthTokenizer error", e);
			}
		}

		return text;
	}

	/**
	 * Converts String to String or the caller expected String, if it's a high value
	 * field, then should input param to expectHighValue, If it's a low value field,
	 * then should input param to expectLowValue, or will input the param null.
	 * 
	 * @param token
	 *            the token
	 * @param expect
	 *            high value result the expectHighValue
	 * @param expect
	 *            low value result the expectLowValue
	 * @return the String or expectHighValue or expectLowValue
	 * 
	 * @throws IllegalArgumentException
	 *             when 0 > token length
	 * @throws ArrayIndexOutOfBoundsException
	 *             when token length < 0
	 * @throws UnsupportedEncodingException
	 *             when the encoding is not supported
	 * 
	 * @see String
	 * @see IllegalArgumentException
	 * @see ArrayIndexOutOfBoundsException
	 * @see UnsupportedEncodingException
	 */
	public static String highOrLowEBCDICValueConvertToASCII(final String token, final String expectHighValue,
			final String expectLowValue) throws java.lang.Exception {

		String text = null;

		try {
			if (token != null && !StringUtils.isAsciiPrintable(token)) {
				byte[] highValueBytes = new byte[token.length()];
				Arrays.fill(highValueBytes, 0, token.length(), Byte.parseByte("-97"));

				byte[] lowValueBytes = new byte[token.length()];
				Arrays.fill(lowValueBytes, 0, token.length(), Byte.parseByte("0"));

				if (Arrays.equals(highValueBytes, token.getBytes(DEFAULT_ENCODING))) {
					text = expectHighValue;
				} else if (Arrays.equals(lowValueBytes, token.getBytes(DEFAULT_ENCODING))) {
					text = expectLowValue;
				} else {
					text = token;
				}
			} else {
				text = token;
			}
		} catch (IllegalArgumentException e) {
			throw new Exception(e.toString());
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Exception(e.toString());
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e.toString());
		}

		return text;
	}

	/**
	 * Replace the String non-printable character with Space 
	 * 
	 * @param token
	 *            the token
	 * @return the String which didn't contain non-printable character
	 * 
	 * @throws UnsupportedEncodingException
	 *             when the encoding is not supported
	 * 
	 * @see UnsupportedEncodingException
	 */
	public static String replaceNonPrintableCharacterWithSpace(final String token) throws java.lang.Exception {

		String text = null;

		try {
			if (token != null && !StringUtils.isAsciiPrintable(token)) {
				byte[] tokenByteArray = token.getBytes(DEFAULT_ENCODING);
				for (int i = 0; i < tokenByteArray.length; i++) {
					if (tokenByteArray[i] <= 0) {
						tokenByteArray[i] = 32;
					}
				}
				text = new String(tokenByteArray, DEFAULT_ENCODING);
			} else {
				text = token;
			}
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e.toString());
		}

		return text;
	}
}
