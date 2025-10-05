/*
 */
package com.dummy.wpc.datadaptor.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.dummy.wpc.common.exception.ParseException;
import com.dummy.wpc.common.exception.WPCBaseException;

/**
 * <p><b>
 * Insert description of the class's responsibility/role.
 * </b></p>
 */
public interface GenExcel {

    public HSSFWorkbook generate(List input,String specialTitleConfigFileName) throws ParseException, WPCBaseException;
}
