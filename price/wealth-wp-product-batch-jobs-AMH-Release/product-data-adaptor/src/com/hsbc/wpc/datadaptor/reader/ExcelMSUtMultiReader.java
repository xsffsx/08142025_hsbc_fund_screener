package com.dummy.wpc.datadaptor.reader;

import java.util.HashMap;
import java.util.Map;

import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractBufferedItemReaderItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.dummy.wpc.datadaptor.bean.RecordBean;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.mapper.RowMapper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class ExcelMSUtMultiReader<T> extends AbstractBufferedItemReaderItemStream implements ResourceAwareItemReaderItemStream,
        InitializingBean {


    private static Log log = LogFactory.getLog(ExcelMSUtMultiReader.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private String encoding = DEFAULT_CHARSET;

    private Resource resource;

    private XSSFWorkbook workbook;

    private Sheet sheet;

    private int linesToSkip = 0;

    private int currentRow = 0;

    private int sheetIndex = 0;

    private RowMapper<T> rowMapper;

    private String jobCode;

    private String[] headerColumns;

    private RecordBean recordBean;

    private String[] rowContent;
    private ExecutionContext executionContext;

    public ExcelMSUtMultiReader() {
        this.setName(ClassUtils.getShortClassName(this.getClass()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    protected void doClose() throws Exception {
        if (resource != null) {
            IOUtils.closeQuietly(resource.getInputStream());
        }
    }

    public void open(ExecutionContext executionContext) throws ItemStreamException {
        jobCode = executionContext.getString(Constants.JOB_BEAN_ID);
//        String fieldListConf = executionContext.getString(Constants.FIELD_LIST_CONFIG);
//        
//        try {
//            headerColumns = FileUtils.readLines(new File(fieldListConf));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        constructFieldSetMapperClass(executionContext);
        if (rowMapper == null) {
            throw new IllegalArgumentException("Cannot find rowMapper for jobCode: " + jobCode);
        }
        super.open(executionContext);
        this.executionContext = executionContext;
    }

    @Override
    protected void doOpen() throws Exception {
        workbook = new XSSFWorkbook(resource.getInputStream());
        workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        recordBean = new RecordBean();
        RecordBean.setCurRecordBean("ExcelMSUtRecordBean", this.recordBean);
        Sheet sheet = new PoiSheet(workbook.getSheetAt(sheetIndex), linesToSkip);
        int rows = sheet.getNumberRows() - 1;
        recordBean.setTotalRecord(rows);
    }

    @Override
    protected Object doRead() throws Exception {
        Sheet sheet = new PoiSheet(workbook.getSheetAt(sheetIndex), linesToSkip);
        if(currentRow == 1){
        	headerColumns = readRow(sheet, --currentRow);
        	currentRow++;
        }
        try {
            rowContent = readRow(sheet, currentRow++);
        } catch (Exception e) {
            executionContext.put("isExcelMSUTError", true);
            String msg = "RowIndex " + currentRow + " record is null, missing required fields,please check further.";
            log.error(msg);
            TNGMessage.logTNGMsgExInfo("001", "E", TNGMsgConstants.SERVICE_NAME, msg);
            return null;
        }
        if (rowContent == null || rowContent.length == 0) {
            return null;
        } else {
        	MultiWriterObj multiWriterObj= (MultiWriterObj) rowMapper.mapRow(sheet, buildColumnContentMap(rowContent,currentRow));
        	  if(multiWriterObj.getArray()==null){
        		  recordBean.increaseErrorRecord();
        	  }
        	  return multiWriterObj;
        }
    }

    private Map<String, String> buildColumnContentMap(String[] content, int sheetIndex) {
        Map<String, String> columnContentMap = new HashMap<String, String>();

        if (content == null || content.length == 0 || headerColumns == null || headerColumns.length == 0) {
            throw new IllegalArgumentException("Row content is null or header columns is null.");
        }
        for (int index = 0; index < headerColumns.length; index++) {
            if (index < content.length) {
                columnContentMap.put(headerColumns[index], content[index]);
            } else {
                columnContentMap.put(headerColumns[index], StringUtils.EMPTY);
            }
        }
        columnContentMap.put("RowIndex", String.valueOf(sheetIndex));
        return columnContentMap;
    }

    private String[] readRow(Sheet sheet, int currentRow) throws Exception {
        if (currentRow < sheet.getNumberRows()) {
            return sheet.getRow(currentRow);
        }
        return null;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setLinesToSkip(int linesToSkip) {
        this.linesToSkip = linesToSkip;
        this.currentRow = linesToSkip;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    private void constructFieldSetMapperClass(ExecutionContext executionContext) {
        String rowMapperClassName = ExecutionContextHelper.getString(executionContext, Constants.MAPPER);
        if (StringUtils.isEmpty(rowMapperClassName)) {
            return;
        }
        try {
            Class clz = Class.forName(rowMapperClassName);
            Object instance = clz.newInstance();
            if (instance instanceof RowMapper) {
                rowMapper = (RowMapper) instance;
                rowMapper.setJobCode(ExecutionContextHelper.getString(executionContext, Constants.JOB_CODE));
            } else {
                throw new IllegalArgumentException("rowMapper is not instance of RowMapper.");
            }

        } catch (Exception e) {
            log.error("Cannot instantiate the field set mapper \"" + rowMapperClassName + "\", Please check carefully!", e);
        }
    }
}
