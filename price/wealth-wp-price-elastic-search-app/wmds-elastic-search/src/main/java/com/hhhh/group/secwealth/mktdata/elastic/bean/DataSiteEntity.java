/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;


public class DataSiteEntity {

    private List<DataFileInfo> dataFiles = new ArrayList<>();

    private File refDataFile = null;

    private boolean isParsingError = false;

    private List<CustomizedEsIndexForProduct> objList = new ArrayList<>();

    public void addDataFile(final File file, final String productType) {
        DataFileInfo info = new DataFileInfo();
        info.setDataFile(file);
        info.setProductType(productType);
        this.dataFiles.add(info);
    }

    public void setDataFiles(final List<DataFileInfo> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public List<DataFileInfo> getDataFiles() {
        return this.dataFiles;
    }

    public File getRefDataFile() {
        return this.refDataFile;
    }

    public void setRefDataFile(final File refDataFile) {
        this.refDataFile = refDataFile;
    }

    public boolean isParsingError() {
        return this.isParsingError;
    }

    public void setParsingError(final boolean isParsingError) {
        this.isParsingError = isParsingError;
    }

    public List<CustomizedEsIndexForProduct> getObjList() {
        return this.objList;
    }

    public void setObjList(final List<CustomizedEsIndexForProduct> objList) {
        this.objList = objList;
    }

    public class DataFileInfo {

        private String productType;

        private File dataFile;

        private boolean parseSuccess;

        private int totalProductNum;

        public boolean isParseSuccess() {
            return parseSuccess;
        }

        public void setParseSuccess(boolean parseSuccess) {
            this.parseSuccess = parseSuccess;
        }

        public String getProductType() {
            return this.productType;
        }

        public void setProductType(final String productType) {
            this.productType = productType;
        }

        public File getDataFile() {
            return this.dataFile;
        }

        public void setDataFile(final File dataFile) {
            this.dataFile = dataFile;
        }

        public int getTotalProductNum() { return this.totalProductNum; }

        public void setTotalProductNum(int totalProductNum) { this.totalProductNum = totalProductNum; }
    }

}