package com.dummy.wpc.datadaptor.listener;

import com.dummy.wpc.datadaptor.constant.IngressFileStatus;
import com.dummy.wpc.datadaptor.reader.PoiSheet;
import com.dummy.wpc.datadaptor.util.Constants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.ExitStatus;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class is used to read the excel file which upload by UI page and handle it
 * ExecutionContext in StepExecution.
 */
public class MsutExcelFileIngressLogListner extends StepListenerSupport {
    private final static String CRT_DT_TM = "crtDtTm";
    private final static String STAT = "stat";
    private final static String ID = "_id";
    private final static String IDENTIFIER = "identifier";
    private final static String RAWDATA = "rawData";
    private final static String NO_OF_RECORDS_TO_BE_PROCESS = "noOfRecordsToBeProcess";
    private final static String IS_ALL_RECORD_LOADED_TO_DATABASE = "isAllRecordLoadedToDatabase";
    private int linesToSkip = 0;
    private int sheetIndex = 0;
    private String[] headerColumns;
    private String[] rowContent;

    private Resource dataPath;

    MongoCollection<Document> fileIngressStatColl;
    MongoCollection<Document> datProcStatColl;
    MongoCollection<Document> sequenceColl;

    private static final Logger log = Logger.getLogger(MsutExcelFileIngressLogListner.class);

    public ExitStatus afterStep(StepExecution stepExecution) {
        try {
            //excelMSUtMultiWriterJob encounter NullPointException, clean the previous content of the generated file
            if(checkExcelMSUTError(stepExecution)){
                return null;
            }

            //save fileIngressStat and datProcStat
            saveFileIngressStatAndProceLog();
        } catch (IOException e) {
            log.error("Failed to generated file ingress status, cause:" + e.getMessage(), e);
            return null;
        }
        log.info("Successfully generated file ingress status for " + dataPath.getFilename());
        return null;
    }

    private Boolean checkExcelMSUTError(StepExecution stepExecution) throws IOException {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        Object isExcelMSUTError = executionContext.get("isExcelMSUTError");
        if(Boolean.TRUE.equals(isExcelMSUTError)){
            String writerKeyPrefix = "writer.1.";
            String outputFilePath = (String)executionContext.get(writerKeyPrefix + Constants.OUTPUT_FILE_PATH);
            File file = new File(outputFilePath);
            //if the excel file contain null row, the variable 'isExcelMSUTError' will be set true,this case need to clean the previous content of the excel file
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><utTrstInstmLst>");
                fileWriter.close();
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void saveFileIngressStatAndProceLog() throws IOException {
        if(!dataPath.getFile().exists()){
            return;
        }
        Date now = new Date();
        Document fileIngressStatPo = new Document()
                .append(ID, getId("SQ_FILE_INGRESS_STAT"))
                .append("md5", DigestUtils.md5Hex(dataPath.getInputStream()))
                .append("fileName", dataPath.getFilename())
                .append(CRT_DT_TM, now)
                .append("updtDtTm", now);

        insertDataProceLog(fileIngressStatPo);
        fileIngressStatColl.insertOne(fileIngressStatPo);
    }


    private long getId(String sequenceName) {
        return sequenceColl.findOneAndUpdate(
                        Filters.eq(ID, sequenceName),
                        Updates.inc("max", 1),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER))
                .getInteger("max")
                .longValue();
    }

    private void insertDataProceLog(Document fileIngressStatusPo) {
        XSSFWorkbook workbook = null;
        Document document = null;
        Date now = null;
        Long rowCount = 0L;
        Long finishCount = 0L;
        Long errorCount = 0L;
        String rawData = null;
        String stat = null;
        String identifier = null;
        Long fisid = 0L;
        String errorMessage = null;
        String prodCde = null;
        String prodName = null;
        String rowIndex = null;
        boolean result = false;
        ArrayList<Document> docList = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(dataPath.getInputStream());
            com.dummy.wpc.datadaptor.reader.Sheet sheet = new PoiSheet(workbook.getSheetAt(sheetIndex), linesToSkip);
            for (int currentRow = 1; currentRow <= sheet.getNumberRows(); currentRow++) {
                if (currentRow == 1) {
                    headerColumns = readRow(sheet, 0);
                    continue;
                }
                rowContent = readRow(sheet, currentRow-1);
                if (rowContent == null || rowContent.length == 0) {
                    continue;
                }

                document = new Document(buildColumnContentMap(rowContent, currentRow));
                prodCde = document.getString("dummy Fund_Code");
                prodName = document.getString("dummy_FundName_EN");
                rowIndex = document.getString("RowIndex");

                rowCount++;
                now = new Date();
                if (StringUtils.isBlank(prodCde)||StringUtils.isBlank(prodName)) {
                    errorCount++;
                    rawData = document.toJson();
                    stat = IngressFileStatus.ERROR.toString();
                    identifier = "";
                    fisid = fileIngressStatusPo.getLong(ID);
                    errorMessage = "RowIndex "+rowIndex+"  Record item [productCode : "+prodCde+" prodName : "+prodName+"] Missing required fields,please check further.";
                } else {
                    finishCount++;

                    // Save the document to MongoDB
                    rawData = document.toJson();
                    stat = IngressFileStatus.FINISHED.toString();
                    identifier = document.getString("dummy Fund_Code");
                    fisid = fileIngressStatusPo.getLong(ID);
                    errorMessage = "";
                }
                docList.add(new Document().append(ID, getId("SQ_DAT_PROC_STAT"))
                        .append("rawData", rawData)
                        .append(STAT, stat)
                        .append("identifier", identifier)
                        .append("fisid", fisid)
                        .append("sourceSystem", "MS")
                        .append("errorMessage", errorMessage)
                        .append(CRT_DT_TM, now)
                        .append("updtDtTm", now));
                if (docList.size() >= 1000) {
                    datProcStatColl.insertMany(docList);
                    docList.clear();
                }
            }
            if (!docList.isEmpty()) {
                datProcStatColl.insertMany(docList);
            }

            fileIngressStatusPo
                    .append(STAT, IngressFileStatus.FINISHED.toString())
                    .append(NO_OF_RECORDS_TO_BE_PROCESS, rowCount)
                    .append("finishedCount", finishCount)
                    .append("pendingCount", 0L)
                    .append("errorCount", errorCount)
                    .append("skippedCount", 0L)
                    .append(IS_ALL_RECORD_LOADED_TO_DATABASE, true);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fileIngressStatusPo
                    .append(STAT, IngressFileStatus.ERROR.toString())
                    .append(NO_OF_RECORDS_TO_BE_PROCESS, rowCount)
                    .append("finishedCount", finishCount)
                    .append("pendingCount", 0L)
                    .append("errorCount", errorCount)
                    .append("skippedCount", 0L)
                    .append(IS_ALL_RECORD_LOADED_TO_DATABASE, false);
        }
        log.info("fileIngressStatusPo:" + fileIngressStatusPo);
    }


    private String[] readRow(com.dummy.wpc.datadaptor.reader.Sheet sheet, int currentRow) throws Exception {
        if (currentRow < sheet.getNumberRows()) {
            return sheet.getRow(currentRow);
        }
        return null;
    }

    private Map<String, Object> buildColumnContentMap(String[] content, int sheetIndex) {
        Map<String, Object> columnContentMap = new HashMap<String, Object>();

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


    public void setLinesToSkip(int linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    public void setDataPath(Resource dataPath) {
        this.dataPath = dataPath;
    }

    public void setMongoDatabase(MongoDatabase mongoDatabase) {
        fileIngressStatColl = mongoDatabase.getCollection("tt_file_ingress_stat");
        datProcStatColl = mongoDatabase.getCollection("tt_dat_proc_stat");
        sequenceColl = mongoDatabase.getCollection("sequence");
    }
}
