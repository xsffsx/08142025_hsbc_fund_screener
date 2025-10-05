package com.dummy.wpc.datadaptor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PropertiesHelper {
	public static OrderedProperties loadProperties(String path) {
		OrderedProperties prop = new OrderedProperties();
		try {
			prop.load(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			prop = null;
			throw new RuntimeException("The config path \"" + path + "\" can't be found");
		} catch (IOException e) {
			prop = null;
			throw new RuntimeException("Error occured when read the config file", e);
		}
		return prop;
	}
}
