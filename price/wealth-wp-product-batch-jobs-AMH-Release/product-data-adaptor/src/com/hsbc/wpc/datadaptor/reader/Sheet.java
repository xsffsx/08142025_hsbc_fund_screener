package com.dummy.wpc.datadaptor.reader;

public interface Sheet {

	public int getNumberRows();

	public String getName();

	public String[] getRow(int rowNumber) throws Exception;

	public String[] getHeader() throws Exception;

	public int getNumberOfColumns() throws Exception;
}
