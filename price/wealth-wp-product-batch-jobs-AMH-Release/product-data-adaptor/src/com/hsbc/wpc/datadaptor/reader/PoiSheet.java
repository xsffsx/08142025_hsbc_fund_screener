package com.dummy.wpc.datadaptor.reader;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.ErrorConstants;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;


public class PoiSheet implements Sheet {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    private org.apache.poi.xssf.usermodel.XSSFSheet delegate;
    
    private int linesToSkip;
    
    public PoiSheet(org.apache.poi.xssf.usermodel.XSSFSheet delegate, int linesToSkip) {
        this.delegate = delegate;
        this.linesToSkip = linesToSkip;
    }
    
    @Override
    public int getNumberRows() {
        return delegate.getLastRowNum() + 1;
    }
    
    @Override
    public String getName() {
        return delegate.getSheetName();
    }
    
    @Override
    public String[] getRow(int rowNumber) throws Exception {

        if (rowNumber > delegate.getLastRowNum()) {
            return null;
        }
        
        XSSFRow row = delegate.getRow(rowNumber);
        row.getPhysicalNumberOfCells();
        row.getLastCellNum();
        List<String> cells = new LinkedList<String>();
        DataFormatter formatter = new DataFormatter();
        
        try {
            
            for (int index = 0; index < row.getLastCellNum(); index++) {
                XSSFCell current = (XSSFCell) row.getCell(index, Row.CREATE_NULL_AS_BLANK);
                switch (current.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(current)) {
                        cells.add(DATE_FORMAT.format(current.getDateCellValue()));
                    } else {
                        cells.add(formatter.formatCellValue(current));
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cells.add(String.valueOf(current.getBooleanCellValue()));
                    break;
                case Cell.CELL_TYPE_STRING:
                    cells.add(current.getRichStringCellValue().getString());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    cells.add("");
                    break;
                case Cell.CELL_TYPE_ERROR:
                    if (current.getErrorCellValue() == ErrorConstants.ERROR_DIV_0) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #DIV/0! ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_NA) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #N/A! ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_NAME) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #NAME? ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_NULL) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #NULL! ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_NUM) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #NUM! ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_REF) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #REF! ");
                    } else if (current.getErrorCellValue() == ErrorConstants.ERROR_VALUE) {
                        cells.add("Invalid column " + current.getColumnIndex() + " found: #VALUE! ");
                    } else {
                        cells.add("Unknown error found ");
                    }
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cells.add(current.getCellFormula());
                    break;
                default:
                    throw new IllegalArgumentException("Cannot handle cell of type: " + current.getCellType());
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cells.toArray(new String[cells.size()]);
    }
    
    @Override
    public String[] getHeader() throws Exception {
        return this.getRow(linesToSkip - 1);
    }
    
    @Override
    public int getNumberOfColumns() throws Exception {
        String[] columns = getHeader();
        if (columns != null) {
            return columns.length;
        }
        return 0;
    }
    
}
