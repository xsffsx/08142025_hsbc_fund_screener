package com.dummy.wpc.datadaptor.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.util.StringUtils;

public class DataAdaptorJobParametersConverter extends DefaultJobParametersConverter {
	private static final String DOUBLE_TYPE = "(double)";

	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	private NumberFormat numberFormat = new DecimalFormat("#");

	private Number parseNumber(String value) {
		try {
			return numberFormat.parse(value);
		} catch (ParseException ex) {
			String suffix = (numberFormat instanceof DecimalFormat) ? ", use "
					+ ((DecimalFormat) numberFormat).toPattern() : "";
			throw new IllegalArgumentException("Number format is invalid: [" + value + "], use " + suffix);
		}
	}

	@Override
	public JobParameters getJobParameters(Properties props) {
		if (props == null || props.isEmpty()) {
			return new JobParameters();
		}

		JobParametersBuilder propertiesBuilder = new JobParametersBuilder();

		for (Iterator it = props.entrySet().iterator(); it.hasNext();) {

			String key = (String) it.next();
			String value = (String) props.getProperty(key);
			if (key.endsWith(DATE_TYPE)) {
				Date date;
				try {
					date = dateFormat.parse(value);
				} catch (ParseException ex) {
					String suffix = (dateFormat instanceof SimpleDateFormat) ? ", use "
							+ ((SimpleDateFormat) dateFormat).toPattern() : "";
					throw new IllegalArgumentException("Date format is invalid: [" + value + "]" + suffix);
				}
				propertiesBuilder.addDate(StringUtils.replace(key, DATE_TYPE, ""), date);
			} else if (key.endsWith(LONG_TYPE)) {
				Long result;
				try {
					result = (Long) parseNumber(value);
				} catch (ClassCastException ex) {
					throw new IllegalArgumentException("Number format is invalid for long value: [" + value
							+ "], use a format with no decimal places");
				}
				propertiesBuilder.addLong(StringUtils.replace(key, LONG_TYPE, ""), result);
			} else if (key.endsWith(DOUBLE_TYPE)) {
				Double result;
				try {
					result = (Double) parseNumber(value);
				} catch (ClassCastException ex) {
					throw new IllegalArgumentException("Number format is invalid for double value: [" + value + "]");
				}
				propertiesBuilder.addDouble(StringUtils.replace(key, DOUBLE_TYPE, ""), result);
			} else if (StringUtils.endsWithIgnoreCase(key, STRING_TYPE)) {
				propertiesBuilder.addString(StringUtils.replace(key, STRING_TYPE, ""), value);
			} else {
				propertiesBuilder.addString(key, value.toString());
			}
		}

		return propertiesBuilder.toJobParameters();
	}
}
