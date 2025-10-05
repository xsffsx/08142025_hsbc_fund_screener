package com.dummy.wpc.datadaptor.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.MarkFailedException;
import org.springframework.batch.item.NoWorkFoundException;
import org.springframework.batch.item.ResetFailedException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.Resource;

import com.dummy.wpc.common.exception.WPCBaseException;
import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import com.dummy.wpc.datadaptor.bean.RecordBean;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.mapper.IMapper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExcelHelper;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;
import com.dummy.wpc.datadaptor.util.GenExcel;

/*
 * Only for HFI same layout multi excel to single xml case.
 */
public class ExcelMultiFileReader implements IReader {

    private static final Logger LOGGER = Logger.getLogger(ExcelMultiFileReader.class);

    private boolean throwErrWhenReadFailed = false;

    private HSSFWorkbook workbook = null;

    private Map titleMap = null;

    private State state = null;

    private IMapper mapper = null;

    private String firstColumnDataTitle = null;
    
    private RecordBean recordBean = null;
    
    private String ctryCde = null;

    private String orgnCde = null;

    private String jobCode = null;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        ctryCde = ExecutionContextHelper.getString(executionContext, Constants.CTRY_CODE);
        orgnCde = ExecutionContextHelper.getString(executionContext, Constants.ORGN_CODE);
        jobCode = ExecutionContextHelper.getString(executionContext, Constants.JOB_CODE);
        try {
            firstColumnDataTitle = ExecutionContextHelper.getString(executionContext, Constants.FIRST_COLUMN_DATA_TITLE);
            workbook = compositeFile(executionContext);
            String specialTitleConfigFileName = ExecutionContextHelper.getString(executionContext, Constants.TITLE_SPECIAL_CONFIG);
            titleMap = ExcelHelper.processTitleRow(getState().getTitleRow(), ExcelHelper
                .getSpecialTitleMap(specialTitleConfigFileName));
            mapper = getMapper(executionContext);
            recordBean = new RecordBean();
        } catch (WPCBaseException e) {
            LOGGER.error(e.getMessage());
            String msg = "Job [" + ctryCde + "," + orgnCde + "," + jobCode + "],Reader open failed. ";
            TNGMessage.logTNGMsgExInfo("009", "E", TNGMsgConstants.SERVICE_NAME, msg);
        }
    }

    @Override
    public synchronized Object read() throws Exception, UnexpectedInputException, NoWorkFoundException {
        HSSFRow row = null;
        Object bond = null;
        try {
            do {
              //If reading row throw exception,will return row=null,end reading.
                row = getState().readRow();
                if (row != null) {
                    recordBean.increaseTotalRecord();
                    try {
                        bond = mapper.maprow(row, titleMap);
                    } catch (Exception e) {
                        recordBean.increaseErrorRecord();
                        LOGGER.error("Unable to map row to object from sheet " + getState().getCurrentSheeNum() + " '"
                            + getState().getCurrentSheeName() + "' at row: " + getState().getCurrentRowNum(), e);
                    }
                }
            } while (bond == null && !getState().isEnd());
            
            return bond;
        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage());
            String msg = "Job [" + ctryCde + "," + orgnCde + "," + jobCode + "],Read file exception. ";
            TNGMessage.logTNGMsgExInfo("009", "E", TNGMsgConstants.SERVICE_NAME, msg);
            if (throwErrWhenReadFailed) {
                throw ex;
            }
        }
        return null;
    }

    @Override
    public void close(ExecutionContext executionContext) throws ItemStreamException {
        workbook = null;
        if (state == null) {
            return;
        }
        try {
            state.close();
        } finally {
            state = null;
        }
        if (recordBean != null) {
            LOGGER.info(recordBean.getLogSummary());
            if (recordBean.getErrorRecord() > 0) {
                String msg = "Job [" + ctryCde + "," + orgnCde + "," + jobCode + "],File has " + recordBean.getErrorRecord()
                    + " invalid records.";
                TNGMessage.logTNGMsgExInfo("009", "E", TNGMsgConstants.SERVICE_NAME, msg);
            }
        }
    }

    public void setThrowErrWhenReadFailed(boolean throwErrWhenReadFailed) {
        this.throwErrWhenReadFailed = throwErrWhenReadFailed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.dummy.wpc.datadaptor.reader.IReader#setMapper(com.dummy.wpc.datadaptor.mapper.IMapper)
     */
    public void setMapper(IMapper mapper) {
        this.mapper = mapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.ItemReader#mark()
     */
    public synchronized void mark() throws MarkFailedException {
        getState().mark();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.ItemReader#reset()
     */
    public synchronized void reset() throws ResetFailedException {
        getState().reset();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.batch.item.ItemStream#update(org.springframework.batch.item.ExecutionContext)
     */
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    public void afterPropertiesSet() throws Exception {}

    /*
     * (non-Javadoc)
     * 
     * @see com.dummy.wpc.datadaptor.reader.IReader#setResource(org.springframework.core.io.Resource)
     */
    public void setResource(Resource resource) {

    }

    private IMapper getMapper(ExecutionContext executionContext) {
        String mapperName = ExecutionContextHelper.getString(executionContext, Constants.MAPPER);
        String jobCode = ExecutionContextHelper.getString(executionContext, Constants.JOB_CODE);
        IMapper mapper = null;
        if (StringUtils.isNotEmpty(mapperName)) {
            try {
                Class clz = Class.forName(mapperName);
                mapper = (IMapper) clz.newInstance();
                mapper.setJobCode(jobCode);
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
            } catch (IllegalAccessException e) {
                LOGGER.error(e.getMessage());
            } catch (InstantiationException e) {
                LOGGER.error(e.getMessage());
            }
        }else{
            throw new RuntimeException("Mapper is null!Please config in properties file");
        }
        return mapper;
    }

    private HSSFWorkbook compositeFile(ExecutionContext executionContext) throws WPCBaseException {

        int fileCount = Integer.parseInt(ExecutionContextHelper.getString(executionContext, Constants.READER_COUNT));
        HSSFWorkbook workbook = null;
        List files = new ArrayList();
        File file = null;
        String keyPrefix = null;
        String filePathKey = null;
        String dataFilePath = null;
        for (int i = 0; i < fileCount; i++) {
            keyPrefix = "reader." + (i + 1) + ".";
            filePathKey = keyPrefix + Constants.DATA_FILE_PATH;
            dataFilePath = ExecutionContextHelper.getString(executionContext, filePathKey);
            if (StringUtils.isEmpty(dataFilePath)) {
                throw new RuntimeException("Data file path cannot be empty: " + filePathKey);
            }

            file = new File(dataFilePath);
            files.add(file);
        }

        if (files.size() != fileCount) {
            throw new IllegalArgumentException(
                "Specified reader count is NOT equal to defined reader amount.Please check properties file!");
        }

        try {
            String excelGeneratorName = ExecutionContextHelper.getString(executionContext, Constants.EXCEL_GENERATOR);
            String specialTitleConfigFileName = ExecutionContextHelper.getString(executionContext, Constants.TITLE_SPECIAL_CONFIG);
            if (StringUtils.isNotEmpty(excelGeneratorName)) {
                Class clz = Class.forName(excelGeneratorName);
                GenExcel excelGenerator = (GenExcel) clz.newInstance();
                workbook = excelGenerator.generate(files, specialTitleConfigFileName);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            TNGMessage.logTNGMsg(Const.ERROR_CDE, Const.ERROR, "SPOMS BOND Upload Service");
            throw new WPCBaseException(this.getClass(), "compositeFile", e.getMessage());
        }
        return workbook;

    }

    private int skip2DataTitleRowNum(HSSFWorkbook workbook, int sheetIndex) {

        int dataTitleRowIndex = 0;
        HSSFRow row = null;
        HSSFCell cell = null;
        String cellValue = "";
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        do {
            row = sheet.getRow(dataTitleRowIndex);
            if (row != null) {
                cell = row.getCell(0);
                if (cell != null) {
                    cellValue = cell.getStringCellValue().toLowerCase().trim();
                }
            } else {
                cell = null;
            }
            dataTitleRowIndex++;
        } while (!ExcelHelper.isCellEmpty(cell) && !cellValue.equalsIgnoreCase(firstColumnDataTitle));

        return --dataTitleRowIndex;

    }

    private State getState() {
        if (state == null) {
            state = new State();
            state.open(workbook);
        }
        return state;
    }

    private class State {

        HSSFWorkbook workbook = null;
        private int currentSheeIndex = 0;
        private int currentRowIndex = 0;
        private int markedSheeIndex = -1;
        private int markedRowIndex = -1;
        private int dataTitleRowIndex = -1;

        public HSSFRow readRow() {
            HSSFSheet sheet = null;
            HSSFRow row = null;

            try {
                if (workbook != null) {
                    sheet = workbook.getSheetAt(currentSheeIndex);
                    row = sheet.getRow(currentRowIndex);
                    currentRowIndex++;
                    if (row == null) {
                        if (currentSheeIndex == (workbook.getNumberOfSheets() - 1)) {
                            // last sheet,last row,return null to end.
                            return null;
                        } else {
                            // move to next sheet
                            currentSheeIndex++;
                            // skip to actual data.
                            dataTitleRowIndex = skip2DataTitleRowNum(workbook, currentSheeIndex);
                            currentRowIndex = dataTitleRowIndex + 1;
                            sheet = workbook.getSheetAt(currentSheeIndex);
                            row = sheet.getRow(currentRowIndex);
                            currentRowIndex++;
                        }
                    }
                }
            } catch (Exception e) {
                throw new UnexpectedInputException("Unable to read from sheet " + currentSheeIndex + " '" + sheet.getSheetName()
                    + "' at row: "
                    + currentRowIndex, e);
            }
            return row;
        }

        public HSSFRow getTitleRow() {
            HSSFRow titleRow = null;
            if (workbook != null) {
                HSSFSheet sheet = workbook.getSheetAt(currentSheeIndex);
                titleRow = sheet.getRow(dataTitleRowIndex);
            }
            return titleRow;
        }

        public void open(HSSFWorkbook wb) {
            try {
                workbook = wb;
                dataTitleRowIndex = skip2DataTitleRowNum(workbook, currentSheeIndex);
                currentRowIndex = dataTitleRowIndex + 1;//move to actual data row
                mark();
            } catch (Exception e) {
                throw new ItemStreamException("Could not open resource", e);
            }
        }

        public void close() {
            workbook = null;
            currentSheeIndex = 0;
            currentRowIndex = 0;
            markedSheeIndex = -1;
            markedRowIndex = -1;
            dataTitleRowIndex = -1;
        }

        public int getCurrentSheeNum() {
            return currentSheeIndex;
        }
        
        public String getCurrentSheeName() {
            String sheetName = null;
            if (workbook != null) {
                HSSFSheet sheet = workbook.getSheetAt(currentSheeIndex);
                if (sheet != null) {
                    sheetName = sheet.getSheetName();
                }
            }
            return sheetName;
        }

        public int getCurrentRowNum() {
            return currentRowIndex;
        }

        public int getDataTitleRowNum() {
            return dataTitleRowIndex;
        }

        public boolean isEnd() {
            boolean end = false;
            if (currentSheeIndex == (workbook.getNumberOfSheets() - 1)) {
                HSSFSheet sheet = workbook.getSheetAt(currentSheeIndex);
                HSSFRow row = sheet.getRow(currentRowIndex);
                if (row == null) {
                    end = true;
                }
            }
            return end;
        }
        
        public void mark() throws MarkFailedException {
            markedSheeIndex = currentSheeIndex;
            markedRowIndex = currentRowIndex;
        }

        public void reset() throws ResetFailedException {
            currentSheeIndex = markedSheeIndex;
            currentRowIndex = markedRowIndex;
        }

    }

}
