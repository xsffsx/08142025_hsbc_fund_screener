package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.exception.productBatchException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WatchThread extends Thread {

	Process process;
	boolean over;
	ArrayList<String> stream;

	public WatchThread(Process process) {
		this.process = process;
		over = false;
		stream = new ArrayList<>();
	}

	@Override
	public void run() {
		String data = null;
		try {

			while (true) {
				if (process == null || over)
					break;
				// limit each line length to 1M
				BufferedReader reader = new WPCBufferReader(new InputStreamReader(process.getInputStream()),1048576);
				while ((data = reader.readLine()) != null) {
					stream.add(data);
				}
			}
		} catch (Exception e) {
			throw new productBatchException(e);
		}
	}

	public void setOver(boolean over) {
		this.over = over;
	}

}
