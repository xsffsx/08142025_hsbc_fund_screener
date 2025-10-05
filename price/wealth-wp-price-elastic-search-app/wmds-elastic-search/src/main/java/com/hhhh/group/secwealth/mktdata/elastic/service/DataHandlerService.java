/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhhh.group.secwealth.mktdata.elastic.bean.*;
import com.hhhh.group.secwealth.mktdata.elastic.processor.ProductProcessor;
import com.hhhh.group.secwealth.mktdata.elastic.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity.DataFileInfo;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;

@Component
public class DataHandlerService {

    private static Logger logger = LoggerFactory.getLogger(DataHandlerService.class);

    @Value("${predsrch.filePath}")
    private String filePath;

    @Value("${predsrch.constantFilePath}")
    private String constantFilePath;

    @Value("${predsrch.expiredTime}")
    private long expiredTime;

    @Value("${predsrch.sourcePath}")
    private String sourceFilePath;

    @Value("${predsrch.rejectPath}")
    private String rejectFilePath;

    @Value("${predsrch.housekeep:true}")
    private boolean housekeepOn;

    private String housekeepDate = null;

    @Value("${predsrch.maxBackupDay}")
    private int maxBackupDay;

    @Autowired
    private AppProperties appProperty;

    @Autowired
    private ProductEntities productEntities;

    @Autowired
    private DataHandlerContext dataHandlerContext;

    @Autowired
    @Qualifier("dataTypeBySiteMap")
    private Map<String, String[]> dataTypeBySiteMap;

    @Autowired
    @Qualifier("xmlInputFactory")
    private XMLInputFactory xmlif;

    /**
     * The veriosn for WPC data file,key=site,version={timestamp,seq}.
     */
    private Map<String, Version> currentVerBySite = new ConcurrentHashMap<>();

    public static final long COUNT_IN_HOUR = 1000 * 60 * 60L;

    public static final long COUNT_IN_DAY = 1000 * 60 * 60 * 24L;

    public Map<String, Version> getCurrentVerBySite() {
        return this.currentVerBySite;
    }

    /**
     * <p>
     * <b> Check latest version if exist by out file. </b>
     * </p>
     * loop local file and get the latest file
     *
     * @param site
     *            the site
     * @return the latest version by site
     * @throws Exception
     *             the exception
     */
    public Version getLatestVersionBySite(final String site) {
    	DataHandlerService.logger.info("getLatestVersionBySite|getLatestVersionBySite start ----");
        DataHandlerService.logger.info("getLatestVersionBySite|filepath: {}", this.filePath);
        File[] files = getOutFilesBySite(site);
        Version latestVersion = this.currentVerBySite.get(site);
        boolean existNewVersion = false;
        if (ArrayUtils.isNotEmpty(files)) {
            for (File f : files) {
            	//Out files should be GB_HBEU_MKD_202104280603_1762.out
            	DataHandlerService.logger.info("getLatestVersionBySite|Original .out file name: {}", f.getName());
                // get file time stamp
                String fileName = f.getName().substring(0, f.getName().lastIndexOf(CommonConstants.SYMBOL_DOT));
                String[] fileNameArr = fileName.split(CommonConstants.SYMBOL_UNDERLINE);
                this.printFileNameArrLog(fileNameArr);
                // TW_hhhh_MKD_201301171006_1.out
                int length = fileNameArr.length;
                if (length >= PredictiveSearchConstant.OUT_FILE_LENGTH) {
                    Version compareVersion = new Version(fileNameArr[length - 2], fileNameArr[length - 1]);
                    DataHandlerService.logger.info("getLatestVersionBySite|compareVersion: {}", compareVersion);
                    DataHandlerService.logger.info("getLatestVersionBySite|latestVersion: {}", latestVersion);
                    if (compareVersion(compareVersion, latestVersion)) {
                        latestVersion = compareVersion;
                        existNewVersion = true;
                    }
                }
            }
        } else {
            DataHandlerService.logger.error("getLatestVersionBySite|No .out files in current folder");
        }
        DataHandlerService.logger.info("getLatestVersionBySite|existNewVersion: {}", existNewVersion);
        DataHandlerService.logger.info("getLatestVersionBySite|latestVersion after compare: {}", latestVersion);
        DataHandlerService.logger.info("getLatestVersionBySite|getLatestVersionBySite end ----");
        if (existNewVersion) {// newer file exit or not? xx:null
            return latestVersion;
        } else {
            return null;
        }
    }

    private void printFileNameArrLog(String[] fileNameArr) {
        for (String fn : fileNameArr) {
            DataHandlerService.logger.info("getLatestVersionBySite|file name array: {}", fn);
        }
    }

    /**
     * <p>
     * <b> Return all *.out files by site. </b>
     * </p>
     *
     * @param site
     *            the site
     * @return the out files by site
     */
    public File[] getOutFilesBySite(final String site) {
        File folder = new File(this.filePath);
        return folder.listFiles((arg0, arg1) -> (arg1.startsWith(site) && arg1.endsWith(PredictiveSearchConstant.OUT_FILE)));
    }

    /**
     *
     * @param v1 compare version
     * @param v2 latest version
     * @return
     */
    public boolean compareVersion(final Version v1, final Version v2) {
        if (v1 == null) {
            return false;
        }
        if (v2 == null) {
            return true;
        }
        long time1 = Long.parseLong(v1.getTimeStamp());
        long time2 = Long.parseLong(v2.getTimeStamp());
        if (time1 > time2) {
            return true;
        } else if (time1 == time2) {
            int seq1 = Integer.parseInt(v1.getSequence());
            int seq2 = Integer.parseInt(v2.getSequence());
            return (seq1 > seq2);
        } else {
            return false;
        }
    }

    public boolean ifDataExpired(final String site) throws ParseException {
        Version currentVer = this.currentVerBySite.get(site);
        if (null == currentVer) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateConstants.DATEFORMAT_YYYY_MM_DD_HH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT));
        long currentTime = dateFormat.parse(dateFormat.format(new Date())).getTime();
        long discrepancy =
            (currentTime - dateFormat.parse(currentVer.getTimeStamp().substring(0, 10)).getTime()) / DataHandlerService.COUNT_IN_HOUR;
        if (discrepancy >= this.expiredTime) {
            DataHandlerService.logger.error("site: {}|Expired hours: {}", site, discrepancy);
            return true;
        } else {
            return false;
        }
    }

    public File[] getLatestFilesBySite(final String siteKey, final Version ver, final boolean isConstantFile) throws IOException {
        File folder;
        final ResourceLoader loader = new DefaultResourceLoader();
        if (isConstantFile) {
            String actualConstantFilePath = loader.getResource(this.constantFilePath).getURL().getPath();
            folder = new File(actualConstantFilePath);
        } else {
            folder = new File(this.filePath);
        }

        File[] files = folder.listFiles((arg0, arg1) -> {
            String seq = new StringBuffer(CommonConstants.SYMBOL_UNDERLINE).append(ver.getSequence())
                .append(CommonConstants.SYMBOL_DOT).toString();
            return (arg1.contains(siteKey) && arg1.contains(ver.getTimeStamp()) && arg1.contains(seq)
                && arg1.endsWith(CommonConstants.XML_FILE_EXTENSION) && checkDataTypeFile(siteKey, arg1));
        });
        List<String> copySuccessFileType = getSuccessfulFileTypeList(files);
        final String[] dataTypes = this.dataTypeBySiteMap.get(siteKey);
        if (ArrayUtils.isNotEmpty(dataTypes)){
            // remove from dataTypeList if copy successful
            List<String> dataTypeList = updateDataTypeList(copySuccessFileType, dataTypes);
            if (files.length >= dataTypeList.size() && ListUtil.isInvalid(dataTypeList)){
                logger.info("Copy all the files config in dataTypeBySite success");
                return files;
            } else {
                dataTypeList.forEach(dataType -> logger.error("Copy data failed, the dataType is: {}", dataType));
                return new File[0];
            }
        }
        return files;
    }

    private List<String> updateDataTypeList(List<String> copySuccessFileType, String[] dataTypes) {
        List<String> dataTypeArrayList = Arrays.asList(dataTypes);
        List<String> dataTypeList = new ArrayList<>(dataTypeArrayList);
        if (ListUtil.isValid(copySuccessFileType) && ListUtil.isValid(dataTypeList)){
            for (String successFileType : copySuccessFileType){
                dataTypeList.remove(successFileType);
            }
        }
        return dataTypeList;
    }

    private List<String> getSuccessfulFileTypeList(File[] files) {
        List<String> copySuccessFileType = new ArrayList<>();
        if (null != files && files.length > 0){
            for (File file : files){
                String fileName = file.getName();
                String[] tempStrs = fileName.split(CommonConstants.SYMBOL_UNDERLINE);
                if (tempStrs.length == 6) {
                    copySuccessFileType.add(tempStrs[3]);
                }
            }
        }
        return copySuccessFileType;
    }

    // check if need to load this kind of data for this site
    private boolean checkDataTypeFile(final String siteKey, final String arg1) {
        final String[] dataTypes = this.dataTypeBySiteMap.get(siteKey);
        if (null != dataTypes && dataTypes.length > 0) {
            for (String dataType : dataTypes) {
                if (arg1.contains(dataType) && !arg1.contains("-" + dataType) && !arg1.contains(dataType + "-")) {
                    // UT and QDII-UT, if we just want load UT file, should
                    // ignore QDII-UT
                    return true;
                }
            }
            // if config dataTypes has no item can mapped, then return false
            return false;
        }
        // if not config dataTypes, then return true
        return true;
    }

    /**
     *
     * @param siteKey site key
     * @param files files need to be parse
     * @param usedVersion used version
     * @return
     * @throws Exception
     */
    public DataSiteEntity buildFileEntityBySite(final String siteKey, final File[] files, final Version usedVersion) {
        DataHandlerService.logger.debug("buildFileEntityBySite start");
        DataSiteEntity entity = new DataSiteEntity();
        for (File f : files) {
            // such as SG_hhhh_MKD_201212241404_8.out
            String filename = f.getName();
            DataHandlerService.logger.info("the file name is: {}", filename);
            if (filename.contains(PredictiveSearchConstant.REFERENCEDATA)) {
                // which site have this?
                entity.setRefDataFile(f);
            } else {
                String[] tempStrs = filename.split(CommonConstants.SYMBOL_UNDERLINE);
                // sample file format:SG_hhhh_MKD_BOND_20130117100611_1.xml,it will split to 6 by '_'
                if (tempStrs.length == 6) {
                    // (file,product type[UT,BOND...])
                    entity.addDataFile(f, tempStrs[3]);
                }
            }
        }
        int totalProductNumInXmlFile = 0;
        if (null != entity.getDataFiles() && ListUtil.isValid(entity.getDataFiles())) {
            for (DataFileInfo info : entity.getDataFiles()) {
                // Get the product handler by site
                String productType = info.getProductType();
                Product product = this.getProductByType(siteKey, productType);
                if (null != product) {
                    // objList in entity updated, load all data in xml to entity
                    handleData(siteKey, product, info, entity, usedVersion);
                } else {
                    DataHandlerService.logger.error("Undefined product type by site: {}, productType: {}",  siteKey, productType);
                }
                totalProductNumInXmlFile += info.getTotalProductNum();
            }
        }
        this.checkProductTotalNumByOutFile(siteKey, usedVersion, entity, totalProductNumInXmlFile);
        DataHandlerService.logger.debug("buildFileEntityBySite end|obj size {}", entity.getObjList().size());
        return entity;
    }

    /**
     * to ensure the product integrity in xml files after parse, check the product total number
     * @param siteKey site key
     * @param usedVersion used version
     * @param entity if checked failed, then clear up the product in entity
     * @param totalProductNumInXmlFile total product number in xml files after parse
     * @throws IOException
     */
    private void checkProductTotalNumByOutFile(String siteKey, Version usedVersion, DataSiteEntity entity, int totalProductNumInXmlFile) {
        Map<String, Integer> outFileTotalProductMap;
        try {
            File latestOutFile = this.getOutFilesBySiteAndUsedVersion(siteKey, usedVersion);
            /**
             * outFileTotalProductMap: SEC-HK -> 7081
             */
            outFileTotalProductMap = this.getOutFileTotalProductMap(latestOutFile);
        } catch (Exception e) {
            logger.error("Failed to get product total number from WPC out file, the usedVersion is: {}", usedVersion);
            return ;
        }

        int totalProductNumInOutFile = 0;
        String[] dataTypeBySiteArr = this.dataTypeBySiteMap.get(siteKey);
        for (int i = 0; i < dataTypeBySiteArr.length; i++) {
            totalProductNumInOutFile += outFileTotalProductMap.get(dataTypeBySiteArr[i]);
        }
        if (totalProductNumInXmlFile != totalProductNumInOutFile) {
            logger.error("WPC invalid version: {}, total product Number in out file is {}, but in xml files is: {}", usedVersion, totalProductNumInOutFile, totalProductNumInXmlFile);
            entity.setObjList(Lists.newArrayList());
        }
    }

    /**
     * parse out file and to get the product total number in each xml files
     * @param latestOutFile latest out file
     * @return
     * @throws IOException
     */
    private Map<String, Integer> getOutFileTotalProductMap(File latestOutFile) throws IOException {
        Map<String, Integer> outFileTotalProductMap = Maps.newHashMap();
        try(BufferedReader br = new BufferedReader(new FileReader(latestOutFile))) {
            String lineContent = br.readLine();
            while (null != lineContent) {
                List<String> list = Splitter.on(":").trimResults().splitToList(lineContent);
                String[] tempStrs = list.get(0).split(CommonConstants.SYMBOL_UNDERLINE);
                if (tempStrs.length == 6) {
                    logger.info("Total product number in out file for {} is {}", tempStrs[3], list.get(1));
                    outFileTotalProductMap.put(tempStrs[3], Integer.valueOf(list.get(1)));
                }
                lineContent = br.readLine();
            }
        }
        return outFileTotalProductMap;
    }

    /**
     * get current use out files
     * @param siteKey
     * @param usedVersion
     * @return
     */
    private File getOutFilesBySiteAndUsedVersion(String siteKey, Version usedVersion) {
        File[] outFiles = this.getOutFilesBySite(siteKey);
        if (ArrayUtils.isNotEmpty(outFiles)) {
            String currentVersion = usedVersion.genVersion();
            String seq = usedVersion.getSequence();
            for (File f : outFiles) {
                String outFileName = f.getName();
                if (outFileName.contains(currentVersion) && outFileName.contains(seq)) {
                    return f;
                }
            }
        }
        return null;
    }

    private Product getProductByType(String siteKey, String productType) {
        Product product = this.productEntities.getProductEntities().get(StringUtil.combineWithUnderline(siteKey, productType));
        // if the site product don't exist,then use default product.
        // Actually,it will not include site info.-Fred
        if (product == null) {
            product = this.productEntities.getProductEntities().get(productType);
        }

        if (product == null) {
            String typePrefix = productType.replaceAll("[^a-zA-Z].+$", "");
            product = this.productEntities.getProductEntities().get(typePrefix);
        }
        return product;
    }

    // actually, the parameter "entity" should be initialed during process.
    public void handleData(final String siteKey, final Product product, final DataFileInfo info, final DataSiteEntity entity,
        final Version usedVersion) {
        File dataFile = info.getDataFile();
        DataHandlerService.logger.info("xml input file: {}", dataFile.getAbsolutePath());
        processXMLFile(product.getNodeName(), entity, siteKey, usedVersion, info);
    }

    public void processXMLFile(final String nodeName, final DataSiteEntity entity, final String siteKey,
        final Version usedVersion, final DataFileInfo info) {
        DataHandlerService.logger.info("In processXMLFile: {}", info.getDataFile().getName());
        File dataFile = info.getDataFile();
        try {
            if (dataFile.length() > 0) {
                DataHandlerService.logger.info("NodeName {}", nodeName);
                XMLStreamReader xmlr = this.xmlif.createXMLStreamReader(new FileReader(dataFile));
                ProductProcessor productProcessor = this.dataHandlerContext.getProcessorByName(nodeName.toLowerCase());
                productProcessor.process(xmlr, entity, siteKey, usedVersion, info);
            } else {
                DataHandlerService.logger.info("{} not exists or length is 0, skip it", dataFile.getName());
            }
        } catch (Exception e) {
            DataHandlerService.logger.error("processXMLFile encountered error, the message is {}", e.getMessage());
        }
    }

    public void clearDataFiles() throws ParseException {
        if (this.housekeepOn) {
            if (this.maxBackupDay < 0) {// -1 in amh_ut,delete immediately.
                File sourceFile = new File(this.sourceFilePath);
                File rejectFile = new File(this.rejectFilePath);
                File file = new File(this.filePath);
                this.deleteFolderFiles(sourceFile);
                this.deleteFolderFiles(rejectFile);
                this.deleteFolderFiles(file);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DATEFORMAT_YYYY_MM_DD);
                sdf.setTimeZone(TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT));
                long currentTime = System.currentTimeMillis();
                String currentDate = sdf.format(currentTime);
                // housekeepDate is not null
                deleteFolderFilesIfHousekeepDateIsValid(sdf, currentTime);
                // housekeepDate is null
                deleteFilesAndUpdateHousekeepDate(currentTime, currentDate);
            }
        }
    }

    private void deleteFilesAndUpdateHousekeepDate(long currentTime, String currentDate) throws ParseException {
        if (null == this.housekeepDate || !currentDate.equals(this.housekeepDate)) {
            DataHandlerService.logger.info("Delete old data files for the day {}", currentDate);
            File folder = new File(this.filePath);
            // only delete all directory
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFiles(file);
                    }
                }
            }
            // delete Expired files by Sites
            this.deleteExpireFilesBySite(currentTime);
            this.housekeepDate = currentDate;
        }
    }

    private void deleteExpireFilesBySite(long currentTime) throws ParseException {
        for (String siteKey : this.appProperty.getSupportSites()) {
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                continue;
            }
            File folder = new File(this.filePath);
            this.deleteFilesBySite(siteKey, currentTime, folder);
        }
    }

    private void deleteFolderFilesIfHousekeepDateIsValid(SimpleDateFormat sdf, long currentTime) throws ParseException {
        if (this.housekeepDate != null) {
            long housekeepDateTime = sdf.parse(this.housekeepDate).getTime();
            long discrepancy = (currentTime - housekeepDateTime) / DataHandlerService.COUNT_IN_DAY;
            if (discrepancy > this.maxBackupDay) {
                File sourceFile = new File(this.sourceFilePath);
                File rejectFile = new File(this.rejectFilePath);
                this.deleteFolderFiles(sourceFile);
                this.deleteFolderFiles(rejectFile);
            }
        }
    }

    protected void deleteFilesBySite(final String siteKey, final long currentTime, File folder) throws ParseException {
        DataHandlerService.logger.info("housekeep: delete Files By Site: {}, currentTime: {}", siteKey, currentTime);
        File[] outFiles = folder.listFiles((dir, name) -> (name.endsWith(CommonConstants.OUT_FILE_EXTENSION) && name.startsWith(siteKey)));
        if (outFiles != null && outFiles.length > 0) {
            if (outFiles.length == 1) {
                final String outFileTime = this.getOutFileTime(outFiles[0].getName());
                if (StringUtil.isValid(outFileTime)) {
                    this.delXmlFiles(siteKey, folder, outFileTime);
                }
            } else {
                this.sortOutFiles(outFiles);
                boolean isExpired = false;
                SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DATEFORMAT_YYYY_MM_DD_HH_MM);
                for (int i = 1; i < outFiles.length; i++) {
                    File outFile = outFiles[i];
                    final String outFileTime = this.getOutFileTime(outFile.getName());
                    long fileTime = sdf.parse(outFileTime).getTime();
                    isExpired = checkExpired(currentTime, isExpired, fileTime);
                    deleteFilesIfExpire(siteKey, folder, isExpired, outFile, outFileTime);
                }
            }
        }
    }

    private void sortOutFiles(File[] outFiles) {
        Arrays.sort(outFiles, (o1, o2) -> {
            if (WpcFileUtil.converOutFileNameToTimestamp(o1.getName()) > WpcFileUtil.converOutFileNameToTimestamp(o2.getName())) {
                return -1;
            } else if (WpcFileUtil.converOutFileNameToTimestamp(o1.getName()) < WpcFileUtil.converOutFileNameToTimestamp(o2.getName())) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    private boolean checkExpired(long currentTime, boolean isExpired, long fileTime) {
        if (!isExpired) {
            long discrepancy = (currentTime - fileTime) / DataHandlerService.COUNT_IN_DAY;
            if (discrepancy > this.maxBackupDay) {
                isExpired = true;
            }
        }
        return isExpired;
    }

    private void deleteFilesIfExpire(String siteKey, File folder, boolean isExpired, File outFile, String outFileTime) {
        if (isExpired) {
            deleteFiles(outFile);
            File[] delXmlFiles = folder.listFiles((dir, name) -> (name.contains(CommonConstants.XML_FILE_EXTENSION) && name.startsWith(siteKey)
                && name.indexOf(outFileTime) > -1));
            if (delXmlFiles != null && delXmlFiles.length > 0) {
                for (File delXml : delXmlFiles) {
                    deleteFiles(delXml);
                }
            }
        }
    }

    private void delXmlFiles(String siteKey, File folder, String outFileTime) {
        //(name.endsWith(CommonConstants.XML_FILE_EXTENSION) || name.endsWith(CommonConstants.MD5_FILE_EXTENSION)
        File[] delXmlFiles = folder.listFiles((dir, name) -> (name.contains(CommonConstants.XML_FILE_EXTENSION) && name.startsWith(siteKey)
            && name.indexOf(outFileTime) < 0));
        if (delXmlFiles != null && delXmlFiles.length > 0) {
            for (File delXml : delXmlFiles) {
                deleteFiles(delXml);
            }
        }
    }

    private String getOutFileTime(final String fileName) {
        String[] parts = fileName.split(CommonConstants.SYMBOL_UNDERLINE);
        int length = parts.length;
        if (length >= PredictiveSearchConstant.OUT_FILE_LENGTH) {
            return parts[length - 2];
        }
        return null;
    }

    protected void deleteFiles(final File file) {
        DataHandlerService.logger.warn("housekeep: delete the file: {}", file.getName());
        if (file.isFile()) {
            try {
                Files.delete(file.toPath());
            } catch (Exception e) {
                logger.error("Unable to delete the file: {}", file.getName());
            }
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFiles(f);
            }
            try {
                Files.delete(file.toPath());
            } catch (Exception e) {
                logger.error("Unable to delete the file: {}", file.getName());
            }
        }
    }

    private void deleteFolderFiles(final File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            DataHandlerService.logger.warn("housekeep: delete the Files in Folder: {}", file.getName());
            File[] files = file.listFiles();
            for (File f : files) {
                try{
                    Files.delete(f.toPath());
                } catch (Exception e) {
                    logger.error("Failed to delete file: {}", f.getName());
                }
            }
        }
    }
}
