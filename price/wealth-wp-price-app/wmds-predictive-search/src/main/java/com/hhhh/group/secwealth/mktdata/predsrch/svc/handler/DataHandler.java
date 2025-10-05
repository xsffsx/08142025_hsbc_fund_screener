/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefDataLst;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity.DataFileInfo;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Product;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.ProductEntities;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Version;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.OutFileFilter;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.WpcFileUtil.WpcOutFileComprator;

/**
 * The Class DataHandler.
 */
@Component("dataHandler")
public class DataHandler {

    /** The Constant refDataUnmarshaller. */
    private final static Unmarshaller refDataUnmarshaller = getRefDataUnmarshaller();

    /** The Constant COUNT_IN_DAY. */
    public static final long COUNT_IN_DAY = 1000 * 60 * 60 * 24;

    /** The Constant COUNT_IN_HOUR. */
    public static final long COUNT_IN_HOUR = 1000 * 60 * 60;

    private SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHHmm);
    /**
     * The veriosn for WPC data file,key=site,version={timestamp,seq}.
     */
    private Map<String, Version> currentVerBySite = new ConcurrentHashMap<String, Version>();

    /**
     * The veriosn for constant data file,key=site,version={timestamp,seq}.
     */
    private Map<String, Version> constantCurrentVerBySite = new ConcurrentHashMap<String, Version>();

    /** The product entities. */
    @Autowired
    @Qualifier("productEntities")
    private ProductEntities productEntities;

    /** The file path. */
    @Value("#{systemConfig['predsrch.filePath']}")
    private String filePath;

    /** The constant file path. */
    @Value("#{systemConfig['predsrch.constantFilePath']}")
    private String constantFilePath;

    @Value("#{systemConfig['predsrch.sourcePath']}")
    private String sourceFilePath;

    @Value("#{systemConfig['predsrch.rejectPath']}")
    private String rejectFilePath;

    /** The housekeep_on. */
    private boolean housekeep_on = false;

    /** The housekeep_server. */
    @Value("#{systemConfig['predsrch.housekeep_server']}")
    private String[] housekeep_server;

    /** The max backup day. */
    @Value("#{systemConfig['predsrch.maxBackupDay']}")
    private int maxBackupDay;

    /** The time zone. */
    @Value("#{systemConfig['predsrch.timeZone']}")
    private String timeZone;

    /** The housekeep date. */
    private String housekeepDate = null;

    /** The skip err record. */
    @Value("#{systemConfig['predsrch.skipErrRecord']}")
    private boolean skipErrRecord = false;

    /** The expired time. */
    @Value("#{systemConfig['predsrch.expiredTime']}")
    private long expiredTime;

    /** The support sites. */
    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    @Value("#{systemConfig['predsrch.dataTypeBySite']}")
    private String dataTypeBySiteStr;

    private Map<String, String[]> dataTypeBySiteMap = new HashMap<String, String[]>();

    /**
     * <p>
     * <b> Init unmarshaller for reference data. </b>
     * </p>
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
//        String hostName = StringUtil.getServerName();
//        LogUtil.info(DataHandler.class, "hostName=" + hostName);
//        for (int i = 0; this.housekeep_server != null && i < this.housekeep_server.length; i++) {
//            if (StringUtil.isValid(hostName) && hostName.equalsIgnoreCase(this.housekeep_server[i])) {
//                this.housekeep_on = true;
//                LogUtil.info(DataHandler.class, "House keep server=" + hostName);
//                break;
//            }
//        }
    	// every instance of PCF requires house keep  
    	this.housekeep_on = true;
        // Config load data type by site
        if (StringUtil.isValid(this.dataTypeBySiteStr)) {
            String[] dataTypeBySites = this.dataTypeBySiteStr.split(CommonConstants.SYMBOL_SEPARATOR);
            if (null != dataTypeBySites && dataTypeBySites.length > 0) {
                for (String siteDataTypeStr : dataTypeBySites) {
                    if (StringUtil.isValid(siteDataTypeStr)) {
                        String[] siteDataTypes = siteDataTypeStr.split(CommonConstants.SYMBOL_COLON);
                        if (null != siteDataTypes && siteDataTypes.length > 0) {
                            if (StringUtil.isValid(siteDataTypes[1])) {
                                String[] dataTypes = siteDataTypes[1].split(CommonConstants.SYMBOL_COMMA);
                                if (null != dataTypes && dataTypes.length > 0) {
                                    this.dataTypeBySiteMap.put(siteDataTypes[0], dataTypes);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Builds the file entity by site.
     *
     * @param siteKey
     *            the site key
     * @param files
     *            the files
     * @return the data site entity
     * @throws Exception
     *             the exception
     */
    public DataSiteEntity buildFileEntityBySite(final String siteKey, final File[] files) throws Exception {
        LogUtil.debug(DataHandler.class, "buildFileEntityBySite start");
        DataSiteEntity entity = new DataSiteEntity();
        for (File f : files) {
            // such as SG_hhhh_MKD_201212241404_8.out
            String filename = f.getName();
            if (filename.contains(PredictiveSearchConstant.REFERENCEDATA)) {
                entity.setRefDataFile(f);
            } else {
                String[] tempStrs = filename.split(CommonConstants.SYMBOL_UNDERLINE);
                // sample file format:SG_hhhh_MKD_BOND_20130117100611_1.xml,it
                // will split to 6 by '_'
                if (tempStrs.length == 6) {
                    entity.addDataFile(f, tempStrs[3]);
                }
            }
        }
        RefDataLst refDataList = null;
        if (null != entity.getRefDataFile()) {
            refDataList = (RefDataLst) DataHandler.refDataUnmarshaller
                .unmarshal(new InputStreamReader(new FileInputStream(entity.getRefDataFile()), CommonConstants.CODING_UTF8));
        }
        if (null != entity.getDataFiles() && entity.getDataFiles().size() > 0) {
            for (DataFileInfo info : entity.getDataFiles()) {
                // Get the product handler by site
                String productType = info.getProductType();
                Product product =
                    this.productEntities.getProductEntities().get(StringUtil.combineWithUnderline(siteKey, productType));
                // if the site product don't exist,then use default product
                if (product == null) {
                    product = this.productEntities.getProductEntities().get(productType);
                }

                if (product == null) {
                    String typePrefix = productType.replaceAll("[^a-zA-Z].+$", "");
                    product = this.productEntities.getProductEntities().get(typePrefix);
                }

                if (null != product) {
                    product.getConverter().handleData(info.getDataFile(), refDataList, entity, this.skipErrRecord);
                } else {
                    LogUtil.error(DataHandler.class, "Undefined product type by site=" + siteKey + "_" + productType);
                }
            }
        }
        LogUtil.debug(DataHandler.class, "buildFileEntityBySite end|obj size=" + entity.getObjList().size());
        return entity;
    }

    /**
     * <p>
     * <b> Get the data files in latest version by site. </b>
     * </p>
     *
     * @param siteKey
     *            the site key
     * @param ver
     *            the ver
     * @return the latest files by site
     * @throws Exception
     *             the exception
     */
    public File[] getLatestFilesBySite(final String siteKey, final Version ver, final boolean isConstantFile) throws Exception {
        File folder = null;
        final ResourceLoader loader = new DefaultResourceLoader();

        if (isConstantFile) {
            String actualConstantFilePath = loader.getResource(this.constantFilePath).getURL().getPath();
            // folder = new File(this.constantFilePath);
            folder = new File(actualConstantFilePath);
        } else {
            folder = new File(this.filePath);
        }

        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File arg0, final String arg1) {
                String seq = new StringBuffer(CommonConstants.SYMBOL_UNDERLINE).append(ver.getSequence())
                    .append(CommonConstants.SYMBOL_DOT).toString();
                if (arg1.contains(siteKey) && arg1.contains(ver.getTimeStamp()) && arg1.contains(seq)
                    && arg1.endsWith(CommonConstants.XML_FILE_EXTENSION) && checkDataTypeFile(siteKey, arg1)) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    // check if need to load this kind of data for this site
    private boolean checkDataTypeFile(final String siteKey, final String arg1) {
        final String[] dataTypes = this.dataTypeBySiteMap.get(siteKey);
        if (null != dataTypes && dataTypes.length > 0) {
            for (String dataType : dataTypes) {
                if (arg1.contains(dataType)) {
                    // UT and QDII-UT, if we just want load UT file, should
                    // ignore QDII-UT
                    if (!arg1.contains("-" + dataType) && !arg1.contains(dataType + "-")) {
                        return true;
                    }
                }
            }
            // if config dataTypes has no item can mapped, then return false
            return false;
        }
        // if not config dataTypes, then return true
        return true;
    }

    /**
     * <p>
     * <b> Check latest version if exist by out file. </b>
     * </p>
     *
     * @param site
     *            the site
     * @return the latest version by site
     * @throws Exception
     *             the exception
     */
    public Version getLatestVersionBySite(final String site) throws Exception {
        LogUtil.debug(DataHandler.class, "genLatestVersion|filepath=" + this.filePath);
        File[] files = getOutFilesBySite(site);
        Version latestVersion = this.currentVerBySite.get(site);
        boolean eixstNewVersion = false;
        if (null != files && files.length > 0) {
            for (File f : files) {
                // get file time stamp
                String fileName = f.getName().substring(0, f.getName().lastIndexOf(CommonConstants.SYMBOL_DOT));
                String[] s = fileName.split(CommonConstants.SYMBOL_UNDERLINE);
                // TW_hhhh_MKD_201301171006_1.out
                int length = s.length;
                if (null != s && length >= PredictiveSearchConstant.OUT_FILE_LENGTH) {
                    Version compareVersion = new Version(s[length - 2], s[length - 1]);
                    if (compareVersion(compareVersion, latestVersion)) {
                        latestVersion = compareVersion;
                        compareVersion = null;
                        eixstNewVersion = true;
                    }
                }
            }
        } else {
            LogUtil.debug(DataHandler.class, "No out files in current folder");
        }
        LogUtil.debug(DataHandler.class, "getLatestVersionBySite end");
        if (eixstNewVersion) {
            return latestVersion;
        } else {
            return null;
        }

    }

    public Version getLatestConstantVerBySite(final String site) throws Exception {
        LogUtil.debug(DataHandler.class, "genLatestVersion|filepath=" + this.filePath);
        File[] files = getConstantFilesBySite(site);
        Version latestVersion = this.constantCurrentVerBySite.get(site);
        boolean eixstNewVersion = false;
        if (null != files && files.length > 0) {
            for (File f : files) {
                // get file time stamp
                String fileName = f.getName().substring(0, f.getName().lastIndexOf(CommonConstants.SYMBOL_DOT));
                String[] s = fileName.split(CommonConstants.SYMBOL_UNDERLINE);
                // HK_HBAP_MKD_INDEX_20140826111111_2.xml
                int length = s.length;
                if (null != s && length >= PredictiveSearchConstant.CONSTANT_FILE_LENGTH) {
                    Version compareVersion = new Version(s[length - 2], s[length - 1]);
                    if (compareVersion(compareVersion, latestVersion)) {
                        latestVersion = compareVersion;
                        compareVersion = null;
                        eixstNewVersion = true;
                    }
                }
            }
        } else {
            LogUtil.debug(DataHandler.class, "No out files in current folder");
        }
        LogUtil.debug(DataHandler.class, "getLatestVersionBySite end");
        if (eixstNewVersion) {
            return latestVersion;
        } else {
            return null;
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
     * @throws Exception
     *             the exception
     */
    public File[] getOutFilesBySite(final String site) throws Exception {
        File folder = new File(this.filePath);
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File arg0, final String arg1) {
                if (arg1.startsWith(site) && arg1.endsWith(PredictiveSearchConstant.OUT_FILE)) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    public File[] getConstantFilesBySite(final String site) throws Exception {
        final ResourceLoader loader = new DefaultResourceLoader();
        String actualConstantFilePath = loader.getResource(this.constantFilePath).getURL().getPath();
        // File folder = new File(this.constantFilePath);
        File folder = new File(actualConstantFilePath);
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(final File arg0, final String arg1) {
                if (arg1.startsWith(site) && arg1.endsWith(CommonConstants.XML_FILE_EXTENSION)) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    /**
     * <p>
     * <b> Create a health check object for predictive search,it will add to
     * index after load data files successfully. </b>
     * </p>
     *
     * @return the searchable object
     */
    public SearchableObject createHealthCheckObj() {
        SearchableObject obj = new SearchableObject();
        obj.setId(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setProductName(CommonConstants.EMPTY_STRING);
        obj.setProductName_analyzed(
            new String[] {CommonConstants.EMPTY_STRING, CommonConstants.EMPTY_STRING, CommonConstants.EMPTY_STRING});
        obj.setProductShortName(CommonConstants.EMPTY_STRING);
        obj.setProductShortName_analyzed(
            new String[] {CommonConstants.EMPTY_STRING, CommonConstants.EMPTY_STRING, CommonConstants.EMPTY_STRING});
        obj.setKey(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setKey_analyzed(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setProductType_analyzed(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setProductType(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setSymbol(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setSymbol_analyzed(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setCountryTradableCode(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        obj.setCountryTradableCode_analyzed(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        return obj;
    }

    /**
     * <p>
     * <b> Delete the data files which the date before the max backup day. </b>
     * </p>
     *
     * @throws Exception
     *             the exception
     */
    public void clearDataFiles() throws Exception {
        if (this.housekeep_on) {
            if (this.maxBackupDay < 0) {
                File sourceFile = new File(this.sourceFilePath);
                File rejectFile = new File(this.rejectFilePath);
                File file = new File(this.filePath);

                this.deleteFolderFiles(sourceFile);
                this.deleteFolderFiles(rejectFile);
                this.deleteFolderFiles(file);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMdd);
                sdf.setTimeZone(TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT));
                long currentTime = System.currentTimeMillis();
                String currentDate = sdf.format(currentTime);
                if (this.housekeepDate != null) {
                    long housekeepDateTime = sdf.parse(this.housekeepDate).getTime();
                    long discrepancy = (currentTime - housekeepDateTime) / DataHandler.COUNT_IN_DAY;
                    if (discrepancy > this.maxBackupDay) {
                        File sourceFile = new File(this.sourceFilePath);
                        File rejectFile = new File(this.rejectFilePath);
                        this.deleteFolderFiles(sourceFile);
                        this.deleteFolderFiles(rejectFile);
                    }
                }
                if (null == this.housekeepDate || !currentDate.equals(this.housekeepDate)) {
                    LogUtil.info(DataHandler.class, "Delete old data files for the day=" + currentDate);
                    File folder = new File(this.filePath);
                    // delete all directory
                    File[] files = folder.listFiles();
                    if (files != null && files.length > 0) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                deleteFiles(file);
                            }
                        }
                    }

                    // delete Expired files by Sites
                    for (String siteKey : this.supportSites) {
                        if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                            continue;
                        }
                        this.deleteFilesBySite(siteKey, currentTime);
                    }
                    this.housekeepDate = currentDate;
                }
            }
        }
    }

    // check date by hour
    /**
     * If data expired.
     *
     * @param site
     *            the site
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    public boolean ifDataExpired(final String site) throws Exception {
        Version currentVer = this.currentVerBySite.get(site);
        if (null == currentVer) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DateFormat_yyyyMMddHH);
        sdf.setTimeZone(TimeZone.getTimeZone(DateConstants.TIMEZONE_GMT));
        long currentTime = sdf.parse(sdf.format(new Date())).getTime();
        long discrepancy =
            (currentTime - sdf.parse(currentVer.getTimeStamp().substring(0, 10)).getTime()) / DataHandler.COUNT_IN_HOUR;
        if (discrepancy >= this.expiredTime) {
            LogUtil.error(DataHandler.class, site + "|Expired hours=" + discrepancy);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete files.
     *
     * @param file
     *            the file
     * @throws Exception
     *             the exception
     */
    private void deleteFiles(final File file) throws Exception {
        LogUtil.warn(DataHandler.class, "housekeep: delete the file: {}", file.getName());
        if (file.isFile()) {
            if (!file.delete()) {
                LogUtil.error(DataHandler.class, "Unable to delete the file=" + file.getName());
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
            if (!file.delete()) {
                LogUtil.error(DataHandler.class, "Unable to delete the folder=" + file.getName());
            }
        }
    }

    private void deleteFolderFiles(final File file) throws Exception {
        if (file != null && file.exists() && file.isDirectory()) {
            LogUtil.warn(DataHandler.class, "housekeep: delete the Files in Folder: {}", file.getName());
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                	LogUtil.warn(DataHandler.class, "housekeep: delete the file: {}", f.getName());
                    f.delete();
                }
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

    /**
     *
     * <p>
     * <b> delete old file by Site. </b>
     * </p>
     *
     * @param siteKey
     * @param currentTime
     * @throws Exception
     */
    private void deleteFilesBySite(final String siteKey, final long currentTime) throws Exception {
        LogUtil.info(DataHandler.class, "housekeep: delete Files By Site: {}, currentTime: {}", siteKey, currentTime);
        File folder = new File(this.filePath);
        File[] outFiles = folder.listFiles(new OutFileFilter(siteKey));
        if (outFiles != null && outFiles.length > 0) {
            if (outFiles.length == 1) {
                final String outFileTime = this.getOutFileTime(outFiles[0].getName());
                if (StringUtil.isValid(outFileTime)) {
                    File[] delXmlFiles = folder.listFiles(new FilenameFilter() {
                        public boolean accept(final File dir, final String name) {
                            if (name.endsWith(CommonConstants.XML_FILE_EXTENSION) && name.startsWith(siteKey)
                                && name.indexOf(outFileTime) < 0) {
                                return true;
                            }
                            return false;
                        }
                    });
                    if (delXmlFiles != null && delXmlFiles.length > 0) {
                        for (File delXml : delXmlFiles) {
                            deleteFiles(delXml);
                        }
                    }
                }
            } else {
                Arrays.sort(outFiles, new WpcOutFileComprator());
                boolean isExpired = false;
                for (int i = 1; i < outFiles.length; i++) {
                    File outFile = outFiles[i];
                    final String outFileTime = this.getOutFileTime(outFile.getName());
                    long fileTime = this.sdf.parse(outFileTime).getTime();
                    if (!isExpired == true) {
                        long discrepancy = (currentTime - fileTime) / DataHandler.COUNT_IN_DAY;
                        if (discrepancy > this.maxBackupDay) {
                            isExpired = true;
                        }
                    }
                    if (isExpired) {
                        deleteFiles(outFile);
                        File[] delXmlFiles = folder.listFiles(new FilenameFilter() {
                            public boolean accept(final File dir, final String name) {
                                if (name.endsWith(CommonConstants.XML_FILE_EXTENSION) && name.startsWith(siteKey)
                                    && name.indexOf(outFileTime) > -1) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        if (delXmlFiles != null && delXmlFiles.length > 0) {
                            for (File delXml : delXmlFiles) {
                                deleteFiles(delXml);
                            }
                        }
                    }
                }
            }
        }
    }

    // if Version1>Version2,return true
    /**
     * Compare version.
     *
     * @param v1
     *            the v1
     * @param v2
     *            the v2
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    public boolean compareVersion(final Version v1, final Version v2) throws Exception {
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
            if (seq1 > seq2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets the ref data unmarshaller.
     *
     * @return the ref data unmarshaller
     */
    public static Unmarshaller getRefDataUnmarshaller() {
        if (null != DataHandler.refDataUnmarshaller) {
            return DataHandler.refDataUnmarshaller;
        } else {
            try {
                return JAXBContext.newInstance(PredictiveSearchConstant.REFDATAPACKAGE).createUnmarshaller();
            } catch (JAXBException e) {
                LogUtil.error(DataHandler.class, e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * Gets the current ver by site.
     *
     * @return the current ver by site
     */
    public Map<String, Version> getCurrentVerBySite() {
        return this.currentVerBySite;
    }

    /**
     * Sets the current ver by site.
     *
     * @param currentVerBySite
     *            the current ver by site
     */
    public void setCurrentVerBySite(final Map<String, Version> currentVerBySite) {
        this.currentVerBySite = currentVerBySite;
    }

    /**
     * Gets the constant current ver by site.
     *
     * @return the constantCurrentVerBySite
     */
    public Map<String, Version> getConstantCurrentVerBySite() {
        return this.constantCurrentVerBySite;
    }

    /**
     * Sets the constant current ver by site.
     *
     * @param constantCurrentVerBySite
     *            the constantCurrentVerBySite to set
     */
    public void setConstantCurrentVerBySite(final Map<String, Version> constantCurrentVerBySite) {
        this.constantCurrentVerBySite = constantCurrentVerBySite;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(this.timeZone);
    }
}