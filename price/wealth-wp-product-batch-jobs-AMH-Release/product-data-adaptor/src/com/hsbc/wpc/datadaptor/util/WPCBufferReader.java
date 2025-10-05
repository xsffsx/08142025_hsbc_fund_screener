package com.dummy.wpc.datadaptor.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class WPCBufferReader extends BufferedReader  {
	
	BufferedReader in = null;
	int limitIndex =0;
	
	public WPCBufferReader(Reader reader,int length) {
		super(reader);
		in = new BufferedReader(reader);
		limitIndex = length;
	}
	
	public WPCBufferReader(InputStreamReader reader,int length) {
		super(reader);
		in = new BufferedReader(reader);
		limitIndex = length;
	}
	
	public WPCBufferReader(FileReader reader,int length) {
		super(reader);
		in = new BufferedReader(reader);
		limitIndex = length;
	}
				
	public String readLine() throws IOException {
		
		StringBuilder sb = new StringBuilder();
		
		for (int ch; (ch = in.read()) != -1;) {
			int index = sb.length();
			if (index <= limitIndex) {
				if (ch == '\r')
					continue;
				if (ch == '\n')
					return sb.toString();

				char temp = (char) ch;
				sb.append(temp);
				
			} else {
				in.close();
				throw new IOException("input text should not exceed " + limitIndex + " KB!");
			}
		}

		if (sb.length() == 0 && in.read() == -1) {
			return null;
		} else if (sb.length() <= limitIndex) {
			return sb.toString();
		} else {
			in.close();
			throw new IOException("input text should not exceed " + limitIndex + " KB!");
		}
	}		
}
