package com.dummy.wpc.datadaptor.token;

import java.util.List;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.util.TokenizerHelper;

public class CSVTokenizer extends DelimitedLineTokenizer {
	private Resource resource;
	private List<FieldInfo> fieldInfoList;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	protected List doTokenize(String line) {
		if (fieldInfoList == null) {
			TokenizerHelper tHelper = new TokenizerHelper(resource);
			fieldInfoList = tHelper.initFieldInfoList();
			names = tHelper.getNames();
		}
		return super.doTokenize(line);
	}
}
