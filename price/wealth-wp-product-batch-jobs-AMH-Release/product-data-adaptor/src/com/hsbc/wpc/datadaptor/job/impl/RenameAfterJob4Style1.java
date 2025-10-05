package com.dummy.wpc.datadaptor.job.impl;

import java.io.File;
import java.sql.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.datadaptor.job.AfterJob;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.DateHelper;

public class RenameAfterJob4Style1 implements AfterJob {
    private static Logger log = Logger.getLogger(RenameAfterJob4Style1.class);

    public void afterJob(Map<String, String> configMap) throws Exception {

        renameAckFile(configMap);
        renameInputFile(configMap);
    }

    /**
     * <p>
     * <b> Insert description of the method's responsibility/role. </b>
     * </p>
     * 
     * @param configMap
     * @throws Exception
     */
    private void renameAckFile(Map<String, String> configMap) throws Exception {
        String checkACK = configMap.get(Constants.CHECK_ACK);
        if (StringUtils.isNotEmpty(checkACK) && Constants.Y.equals(checkACK)) {
            String readerCountStr = configMap.get(Constants.READER_COUNT);
            int readerCount = 0;
            if (readerCountStr != null) {
                readerCount = Integer.parseInt(configMap.get(Constants.READER_COUNT));
                String inputFilePath = null;
                String inputFilePathKey = null;
                File inputFile = null;
                for (int i = 1; i <= readerCount; i++) {
                    inputFilePathKey = "reader." + i + "." + Constants.DATA_FILE_PATH;
                    inputFilePath = configMap.get(inputFilePathKey);
                    if (StringUtils.isNotBlank(inputFilePath)) {
                        inputFile = new File(inputFilePath);
                    }else{
                        throw new Exception("The property value cannot be bank:"+inputFilePathKey+" , rename fialed.");
                    }
                    renameAckFile(inputFile);
                }
            } else {
                String inputFilePath = configMap.get(Constants.DATA_FILE_PATH);
                File inputFile = new File(inputFilePath);
                renameAckFile(inputFile);
            }
        }
    }

    /**
     * <p>
     * <b> Insert description of the method's responsibility/role. </b>
     * </p>
     * 
     * @param configMap
     * @throws Exception
     */
    private void renameInputFile(Map<String, String> configMap) throws Exception {
       
        String readerCountStr = configMap.get(Constants.READER_COUNT);
        int readerCount = 0;
        if (StringUtils.isNotBlank(readerCountStr)) {
            readerCount = Integer.parseInt(configMap.get(Constants.READER_COUNT));
            String inputFilePath = null;
            String inputFilePathKey = null;
            File inputFile = null;
            for (int i = 1; i <= readerCount; i++) {
                inputFilePathKey = "reader." + i + "." + Constants.DATA_FILE_PATH;
                inputFilePath = configMap.get(inputFilePathKey);
                if (!StringUtils.isBlank(inputFilePath)) {
                    inputFile = new File(inputFilePath);
                }else{
                    throw new Exception("The property value cannot be bank:"+inputFilePathKey+" , rename fialed.");
                }
                renameInputFile(inputFile);
            }
        } else {
            String inputFilePath = configMap.get(Constants.DATA_FILE_PATH);
            File inputFile = new File(inputFilePath);
            renameInputFile(inputFile);
        }
    }

    /**
     * <p>
     * <b> Insert description of the method's responsibility/role. </b>
     * </p>
     * 
     * @param inputFile
     * @throws Exception
     */
    private void renameInputFile(File inputFile) throws Exception {

        // SGdummyUTUT.TXT to SGdummyUTUT.YYYYMMDDHHMMSS.BAK.
        String currentDateTimeStr = DateHelper.formatDate2String(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");
        if (inputFile == null || inputFile.isDirectory() || !inputFile.exists()) {
            throw new Exception("Cannot rename input file,it cannot be null or not existing or folder: " + inputFile);
        }
        String inputFileName = inputFile.getName();
        String newFileName = null;
//        int lastIndexOf = inputFileName.lastIndexOf(".");
//        if (lastIndexOf >= 0) {
//            newFileName = inputFileName.substring(0, lastIndexOf);
//        } else {
            newFileName = inputFileName;
//        }
        newFileName += "." + currentDateTimeStr + ".BAK";
        File newDataFile = new File(inputFile.getParent() + File.separator + newFileName);
        boolean result = inputFile.renameTo(newDataFile);
        if (result == false) {
            throw new Exception("Cannot rename the file \"" + inputFile + "\" to \"" + newDataFile
                + "\", the rename action failed.");
        }
    }

    /**
     * <p>
     * <b> Rename ack file. e.g. SGdummyUTUT.ACK TO SGdummyUTUT.201207261051.ACK. </b>
     * </p>
     * 
     * @param inputFile
     * @throws Exception
     */
    private void renameAckFile(File inputFile) throws Exception {

        String currentDateTimeStr = DateHelper.formatDate2String(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");
        String inputFileName = inputFile.getName();
        String ackFileName = inputFileName;
        String newAckFileName = ackFileName;
        boolean result = false;
        int lastIndexOf = inputFileName.lastIndexOf(".");
        if (lastIndexOf >= 0) {
            inputFileName = inputFileName.substring(0, lastIndexOf);
        }
        
        String ackFileName1 = inputFileName + ".ack";
        String ackFileName2 = inputFileName + ".ACK";
        File ackFile1 = new File(inputFile.getParent() + File.separator + ackFileName1);
        File ackFile2 = new File(inputFile.getParent() + File.separator + ackFileName2);
        if (ackFile1.exists()) {
            ackFileName = ackFileName1;
            newAckFileName = ackFileName1 + "." + currentDateTimeStr + ".BAK";
        } else if (ackFile2.exists()) {
            ackFileName = ackFileName2;
            newAckFileName = ackFileName2 + "." + currentDateTimeStr + ".BAK";
        } else {
            throw new Exception("Cannot find the ack file of \"" + inputFile + "\", the rename action failed.");
        }

        File ackFile = new File(inputFile.getParent() + File.separator + ackFileName);
        File newAckFile = new File(inputFile.getParent() + File.separator + newAckFileName);
        result = ackFile.renameTo(newAckFile);
        if (result == false) {
            throw new Exception("Cannot rename the file \"" + ackFile + "\" to \"" + newAckFile + "\", the rename action failed.");
        }
    }

}
