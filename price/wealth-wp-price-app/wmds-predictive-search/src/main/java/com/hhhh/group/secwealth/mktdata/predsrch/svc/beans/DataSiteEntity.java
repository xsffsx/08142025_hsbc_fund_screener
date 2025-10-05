/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;

public class DataSiteEntity {

    private List<DataFileInfo> dataFiles = new ArrayList<DataFileInfo>();

    private File refDataFile = null;

    private boolean isParsingError = false;

    private List<SearchableObject> objList = new ArrayList<SearchableObject>();

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

    public List<SearchableObject> getObjList() {
        return this.objList;
    }

    public void setObjList(final List<SearchableObject> objList) {
        this.objList = objList;
    }

    public static class DataFileInfo {

        private String productType;

        private File dataFile;

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
    }

}