package com.dummy.wpc.datadaptor.token;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.mapping.DefaultFieldSet;
import org.springframework.batch.item.file.mapping.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.util.TokenizerHelper;

public class TxtTokenizer extends FixedLengthTokenizer {
	private List<FieldInfo> fieldInfoList;
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	protected List<String> doTokenize(String line) {
		if (fieldInfoList == null) {
			TokenizerHelper tHelper = new TokenizerHelper(resource);
			fieldInfoList = tHelper.initFieldInfoList();
			names = tHelper.getNames();
		}

		List<String> valueList = new ArrayList<String>();
		int length = line.length();
		// String lineValue = null;
		if (fieldInfoList != null) {
			for (int i = 0; i < fieldInfoList.size(); i++) {
				FieldInfo fieldInfo = fieldInfoList.get(i);
				int start = fieldInfo.getStart();
				int offset = fieldInfo.getOffset();
				int end = start + offset;
				if (start >= length) {
					break;
				}
				if (end > length) {
					end = length;
				}
				String value = line.substring(start, end);
				valueList.add(value);
			}
		}
		return valueList;
	}

	public FieldSet tokenize(String line) {
		if (line == null) {
			line = "";
		}
		List<String> tokens = new ArrayList<String>(doTokenize(line));
		// some cases allow the real data length less than total field length.
		if (tokens.size() < names.length) {
			int variance = names.length - tokens.size();
			for (int i = 0; i < variance; i++) {
				tokens.add(null);
			}
		}
		String[] values = (String[]) tokens.toArray(new String[tokens.size()]);

		if (names.length == 0) {
			return new DefaultFieldSet(values);
		}
		return new DefaultFieldSet(values, names);
	}
}
