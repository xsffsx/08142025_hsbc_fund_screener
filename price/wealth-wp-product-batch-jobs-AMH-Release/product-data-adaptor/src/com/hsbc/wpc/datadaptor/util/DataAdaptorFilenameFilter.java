package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

/**
 * 
 * @author WMDHKG0007
 * 
 */
public class DataAdaptorFilenameFilter implements FilenameFilter {
	private Map<String, String> configMap;
	private int readerIndex = 0;
	private Map<String, Map<String, String>> valueMapView;
	public DataAdaptorFilenameFilter() {

	}

	public DataAdaptorFilenameFilter(Map<String, String> configMap,int readerIndex,Map<String, Map<String, String>> valueMapView) {
		super();
		this.configMap = configMap;
		this.readerIndex = readerIndex;
		if(valueMapView == null){
			throw new NullPointerException("The value of valueMapView cannot be null.");
		}
		this.valueMapView = valueMapView;
	}

	public boolean accept(File dir, String name) {
		return FilePatternHandler.isFilePattern(name, configMap,readerIndex,valueMapView);
	}
}
