/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.PostConstruct;

import org.compass.core.engine.SearchEngineQueryParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.dao.PredictiveSearchDao;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Version;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.constants.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.handler.DataHandler;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VerifyFilesService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VolumeService;

/**
 * <p>
 * <b> Predictive search manager implement. </b>
 * </p>
 */
@Component("predictiveSearchManager")
public class PredictiveSearchManagerImpl implements PredictiveSearchManager {

    @Autowired
    @Qualifier("volumeService")
    private VolumeService volumeService;

    @Autowired
    @Qualifier("verifyFilesService")
    private VerifyFilesService verifyFilesService;

    /** The predsrch dao. */
    @Autowired
    @Qualifier("predictiveSearchDao")
    private PredictiveSearchDao predictiveSearchDao;

    /** The support sites. */
    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    /** The site converter rule. */
    @Value("#{systemConfig['predsrch.siteLoadInterfaceRule']}")
    private String[] siteConverterRule;

    /** The data handler. */
    @Autowired
    @Qualifier("dataHandler")
    private DataHandler dataHandler;

    /** The side need laod INDEX from Constant folder */
    @Value("#{systemConfig['predsrch.indexLoadFromConstantsSideList'].split(',')}")
    private List<String> indexLoadFromConstantsSideList;

    /** The Constant MAXCLAUSECOUNT. */
    private final static String MAXCLAUSECOUNT = "maxClauseCount";

    /** The have init data. */
    private boolean haveInitData = false;

    /** The delay time. */
    @Value("#{systemConfig['predsrch.delayTime']}")
    private long delayTime;

    /** The Constant CONSTANT_DATA. */
    public static final int CONSTANT_DATA = 1;

    /** The Constant VARIABLE_DATA. */
    public static final int VARIABLE_DATA = 2;

    /**
     * Inits the.
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
        try {
            LogUtil.debug(PredictiveSearchManagerImpl.class, "Start to init predictive search data...");
            Timer timer = new Timer();
            InitSearchData task = new InitSearchData(this);
            timer.schedule(task, this.delayTime);
        } catch (Exception e) {
            LogUtil.error(PredictiveSearchManagerImpl.class, "init data error" + e.getMessage(), e);
        }
    }

    /**
     * Search product by key.
     *
     * @param searchClauses
     *            the search clauses
     * @param sortingFields
     *            the sorting fields
     * @param site
     *            the site
     * @param topNum
     *            the top num
     * @param serviceLog
     *            the service log
     * @param errorLog
     *            the error log
     * @return the list
     * @throws Exception
     *             the exception
     */
    @Override
    public List<SearchableObject> findProductListByKey(final String searchClauses, final String[] sortingFields, final String site,
        final int topNum) throws Exception {
        try {
            LogUtil.debug(PredictiveSearchManagerImpl.class, searchClauses);
            List<SearchableObject> searchableObjectList =
                this.predictiveSearchDao.findProductListByKey(searchClauses, sortingFields, site, topNum);
            addWCode(searchableObjectList);
            return searchableObjectList;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (null != cause) {
                LogUtil.error(PredictiveSearchManagerImpl.class, "findProductListByKey|detail error=" + cause.getMessage(), e);
            }
            if (StringUtil.isValid(e.getMessage())) {
                if (e.getMessage().contains(PredictiveSearchManagerImpl.MAXCLAUSECOUNT)) {
                    LogUtil.warn(PredictiveSearchManagerImpl.class, "Exceed the max record|search clauses=" + searchClauses);
                    LogUtil.warn(PredictiveSearchManagerImpl.class, "Detailed error=" + e.getMessage());
                    return null;
                }
            }
            if (e instanceof SearchEngineQueryParseException) {
                LogUtil.error(PredictiveSearchManagerImpl.class,
                    "Encountered invalid string when quering, detail error=" + e.getMessage());
                throw new CommonException(PredictiveSearchConstant.ERRORMSG_QUERYSTRINGINVALID);
            }
            throw e;
        }
    }

    /**
     * add W Code to searchableObjectList
     *
     * @param searchableObjectList
     */
    private void addWCode(final List<SearchableObject> searchableObjectList) throws Exception {
        for (SearchableObject searchableObject : searchableObjectList) {
            List<ProdAltNumSeg> prodAltNumSegList = searchableObject.getProdAltNumSeg();
            if (ListUtil.isValid(prodAltNumSegList)) {
                ProdAltNumSeg prodAltNumSeg = new ProdAltNumSeg();
                prodAltNumSeg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_W);
                prodAltNumSeg.setProdAltNum(searchableObject.getProductCode());
                prodAltNumSegList.add(prodAltNumSeg);
                searchableObject.setProdAltNumSeg(prodAltNumSegList);
            }
        }
    }

    /**
     * <p>
     * <b> Load data by site. </b>
     * </p>
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void loadData() throws Exception {
        // start volumeService
        Exception volumeServiceErr = null;
        Exception verifyFilesErr = null;
        try {
            LogUtil.warn(PredictiveSearchManagerImpl.class, "start copy wpc files to local");
            this.volumeService.copyToLocalData();
        } catch (Exception err) {
            volumeServiceErr = err;
        }
        try {
            LogUtil.warn(PredictiveSearchManagerImpl.class, "start verify wpc files with MD5");
            this.verifyFilesService.verifyFiles();
        } catch (Exception err) {
            verifyFilesErr = err;
        }

        // start loadData
        LogUtil.debug(PredictiveSearchManagerImpl.class, "load data start=" + System.currentTimeMillis());
        LoadStatus status = new LoadStatus(false, false);
        Map<String, SiteDataInfo> map = new HashMap<String, SiteDataInfo>();
        for (String siteKey : this.supportSites) {
            // ignore default site
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                continue;
            }
            List<SiteDataInfo> siteDataList = new ArrayList<SiteDataInfo>();
            try {
                List<SearchableObject> objList = new ArrayList<SearchableObject>();
                boolean haveConverSite = false;
                boolean needUpdateData = false;
                for (int i = 0; null != this.siteConverterRule && i < this.siteConverterRule.length; i++) {
                    if (this.siteConverterRule[i].contains(siteKey)) {
                        // Have conversite
                        haveConverSite = true;
                        String[] converSites = this.siteConverterRule[i].split(CommonConstants.SYMBOL_SEPARATOR);
                        for (String site : converSites) {
                            // Loop in all site
                            SiteDataInfo info = map.get(site);
                            if (null != info) {
                                siteDataList.add(info);
                            } else {
                                info = getVersionInfoBySite(site, status);
                                siteDataList.add(info);
                                map.put(site, info);
                            }
                        }
                    }
                }
                // Don't have conver site
                if (!haveConverSite) {
                    SiteDataInfo info = map.get(siteKey);
                    if (null != info) {
                        siteDataList.add(info);
                    } else {
                        info = getVersionInfoBySite(siteKey, status);
                        siteDataList.add(info);
                        map.put(siteKey, info);
                    }
                }
                if (null != siteDataList && siteDataList.size() > 0) {
                    for (SiteDataInfo tempInfo : siteDataList) {
                        List<SearchableObject> list = tempInfo.getObjectList();
                        if (null != list && list.size() > 0) {
                            needUpdateData = true;
                            break;
                        }
                    }
                    if (needUpdateData) {
                        for (SiteDataInfo tempInfo : siteDataList) {
                            List<SearchableObject> list = tempInfo.getObjectList();
                            if (null != list && list.size() > 0) {
                                objList.addAll(list);
                            } else {
                                List<SearchableObject> tempList = getCurrentVersionData(tempInfo.getSite(), status);
                                if (null != tempList && tempList.size() > 0) {
                                    objList.addAll(tempList);
                                }
                            }
                        }
                    }
                }
                if (null != objList && objList.size() > 0) {
                    objList.add(this.dataHandler.createHealthCheckObj());
                    if (this.haveInitData) {
                        this.predictiveSearchDao.refreshIndex(siteKey, objList);
                    } else {
                        this.predictiveSearchDao.createIndex(siteKey, objList);
                        this.haveInitData = true;
                    }
                }
            } catch (Exception e) {
                LogUtil.error(PredictiveSearchManagerImpl.class, "loadData|Data parsing error for the support site=" + siteKey);
                LogUtil.error(PredictiveSearchManagerImpl.class, "Data parsing error", e);
                status.setDataParsingErr(true,
                    "loadData|Data parsing error for the support site=" + siteKey + ", error message: " + e.getMessage());
            } finally {
                siteDataList.clear();
            }
        }
        map.clear();
        LogUtil.info(PredictiveSearchManagerImpl.class, "load data end=" + System.currentTimeMillis());
        // clear all old data files
        this.dataHandler.clearDataFiles();

        if (volumeServiceErr != null) {
            LogUtil.error(PredictiveSearchManagerImpl.class, "volumeService download files error:", volumeServiceErr);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_VOLUMESERVICEERR);
        }

        if (verifyFilesErr != null) {
            LogUtil.error(PredictiveSearchManagerImpl.class, "MD5 verify files error:", volumeServiceErr);
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_DATAVERIFYERR);
        }
        if (status.isDataUnavailable) {
            LogUtil.debug(PredictiveSearchManagerImpl.class, "PredictiveSearch is Data Unavailable: Message: {}",
                status.getDataParsingErrMessage());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_DATAUNAVAILABLE, status.getDataUnavailableMessage());
        }
        if (status.isDataParsingErr) {
            LogUtil.debug(PredictiveSearchManagerImpl.class, "PredictiveSearch is Data Parsing Err: Message: {}",
                status.getDataParsingErrMessage());
            throw new CommonException(PredictiveSearchConstant.ERRORMSG_DATAPARSINGERR, status.getDataParsingErrMessage());
        }
    }

    /**
     * Gets the version info by site.
     *
     * @param site
     *            the site
     * @param status
     *            the status
     * @param type
     *            the type
     * @return the version info by site
     */
    private SiteDataInfo getVersionInfoBySite(final String site, final LoadStatus status) throws Exception {
        // get latest object by wpc data file
        Version currentVer = this.dataHandler.getCurrentVerBySite().get(site);
        Version latestVer = this.dataHandler.getLatestVersionBySite(site);
        // get latest object by constant data file
        Version currentConstantVer = this.dataHandler.getConstantCurrentVerBySite().get(site);
        Version latestConstantVer = this.dataHandler.getLatestConstantVerBySite(site);
        SiteDataInfo info = new SiteDataInfo();
        info.setSite(site);
        List<SearchableObject> list = null;
        List<SearchableObject> constantList = null;
        // INDEX load on constants floader
        if (ListUtil.isValid(this.indexLoadFromConstantsSideList) && this.indexLoadFromConstantsSideList.contains(site)) {
            if (null != latestVer && null != latestConstantVer) {
                list = getObjectListBySite(site, latestVer, currentVer, status);
                constantList = getConstantObjListBySite(site, latestConstantVer, status);
                if (null != list && null != constantList) {
                    // Add new version wpc data and new version constant data
                    list.addAll(constantList);
                } else {
                    LogUtil.error(PredictiveSearchManagerImpl.class,
                        "No data list for the new version|wpc version=" + latestVer + "|costant data version=" + latestConstantVer);
                }
            } else if (null != latestVer && null == latestConstantVer) {
                list = getObjectListBySite(site, latestVer, currentVer, status);
                if (null != currentConstantVer) {
                    // Add new version wpc data with current constant data
                    constantList = getConstantObjListBySite(site, currentConstantVer, status);
                    if (null != list && null != constantList) {
                        list.addAll(constantList);
                    }
                }
            } else if (null == latestVer && null != latestConstantVer) {
                if (null != currentVer && !currentVer.isCheckedExpired() && this.dataHandler.ifDataExpired(site)) {
                    currentVer.setCheckedExpired(true);
                }
                // Add new version constant data with current wpc data
                constantList = getConstantObjListBySite(site, latestConstantVer, status);
                if (null != currentVer) {
                    list = getObjectListBySite(site, null, currentVer, status);
                    if (null != list && null != constantList) {
                        list.addAll(constantList);
                    }
                } else {
                    list = constantList;
                }
            } else {
                // Null list if no new version for wpc data and constant data
                if (null != currentVer && !currentVer.isCheckedExpired() && this.dataHandler.ifDataExpired(site)) {
                    currentVer.setCheckedExpired(true);
                }
            }
        } else {// INDEX load on predictive_search floader
            if (null != latestVer) {
                list = getObjectListBySite(site, latestVer, currentVer, status);
            } else {
                if (null != currentVer && !currentVer.isCheckedExpired() && this.dataHandler.ifDataExpired(site)) {
                    currentVer.setCheckedExpired(true);
                }
                if (null != currentVer) {
                    list = getObjectListBySite(site, null, currentVer, status);
                }
            }
        }

        info.setObjectList(list);

        return info;
    }

    private List<SearchableObject> getCurrentVersionData(final String site, final LoadStatus status) throws Exception {
        // get latest object by wpc data file
        Version currentVer = this.dataHandler.getCurrentVerBySite().get(site);
        List<SearchableObject> list = null;
        list = getObjectListBySite(site, null, currentVer, status);
        // get latest object by constant data file
        Version currentConstantVer = this.dataHandler.getConstantCurrentVerBySite().get(site);
        List<SearchableObject> constantList = getConstantObjListBySite(site, currentConstantVer, status);
        if (null != list && null != constantList) {
            list.addAll(constantList);
        } else if (null != constantList) {
            list = constantList;
        }
        return list;
    }

    /**
     * Gets the object list by site.
     *
     * @param siteKey
     *            the site key
     * @param status
     *            the status
     * @return the object list by site
     * @throws Exception
     *             the exception
     */
    private List<SearchableObject> getObjectListBySite(final String siteKey, final Version latestVer, final Version currentVer,
        final LoadStatus status) throws Exception {
        // get the latest fiels by site and version
        File[] files = null;
        if (null != latestVer) {
            files = this.dataHandler.getLatestFilesBySite(siteKey, latestVer, false);
        } else if (null != currentVer) {
            files = this.dataHandler.getLatestFilesBySite(siteKey, currentVer, false);
        }
        if (files != null && files.length > 0) {
            DataSiteEntity entity = this.dataHandler.buildFileEntityBySite(siteKey, files);
            List<SearchableObject> objList = entity.getObjList();
            if (null != objList && objList.size() > 0) {
                if (entity.isParsingError()) {
                    LogUtil.warn(PredictiveSearchManagerImpl.class,
                        "getObjectListBySite|Data parsing error for the site=" + siteKey);
                    status.setDataParsingErr(true, "getObjectListBySite|Data parsing error for the site=" + siteKey);
                }
                if (null != latestVer) {
                    latestVer.setCheckedExpired(false);
                    this.dataHandler.getCurrentVerBySite().put(siteKey, latestVer);
                    LogUtil.warn(PredictiveSearchManagerImpl.class, new StringBuilder("Success to load data for the site=")
                        .append(siteKey).append("|Current version=").append(latestVer.genVersion()).toString());
                } else if (null != currentVer) {
                    LogUtil.warn(PredictiveSearchManagerImpl.class, new StringBuilder("Success to load data for the site=")
                        .append(siteKey).append("|Current version=").append(currentVer.genVersion()).toString());
                }
                return objList;
            } else {
                LogUtil.warn(PredictiveSearchManagerImpl.class,
                    "getObjectListBySite|No SearchableObject to be loaded for the site=" + siteKey);
                if (null != latestVer && null != currentVer && !currentVer.isCheckedExpired()) {
                    currentVer.setCheckedExpired(true);
                    LogUtil.warn(PredictiveSearchManagerImpl.class, "getObjectListBySite|The WPC data is expired=" + siteKey);
                    status.setDataUnavailable(true, "getObjectListBySite|The WPC data is expired=" + siteKey);
                }
            }
        } else if (null != latestVer) {
            LogUtil.warn(PredictiveSearchManagerImpl.class, "No latest version data files for the site=" + siteKey);
            if (null != currentVer && !currentVer.isCheckedExpired()) {
                currentVer.setCheckedExpired(true);
                LogUtil.warn(PredictiveSearchManagerImpl.class, "getObjectListBySite|The WPC data is expired=" + siteKey);
                status.setDataUnavailable(true, "getObjectListBySite|The WPC data is expired=" + siteKey);
            }
        }
        return null;
    }

    /**
     * Gets the constant obj list by site.
     *
     * @param siteKey
     *            the site key
     * @param status
     *            the status
     * @return the constant obj list by site
     * @throws Exception
     *             the exception
     */
    private List<SearchableObject> getConstantObjListBySite(final String siteKey, final Version ver, final LoadStatus status)
        throws Exception {
        if (null != ver) {
            // get the latest fiels by site and version
            File[] files = this.dataHandler.getLatestFilesBySite(siteKey, ver, true);
            if (files != null && files.length > 0) {
                DataSiteEntity entity = this.dataHandler.buildFileEntityBySite(siteKey, files);
                List<SearchableObject> objList = entity.getObjList();
                if (null != objList && objList.size() > 0) {
                    if (entity.isParsingError()) {
                        LogUtil.error(PredictiveSearchManagerImpl.class,
                            "getConstantObjListBySite|Parsing data error for the site=" + siteKey);
                        status.setDataParsingErr(true, "getConstantObjListBySite|Parsing data error for the site=" + siteKey);
                    }
                    this.dataHandler.getConstantCurrentVerBySite().put(siteKey, ver);
                    LogUtil.error(PredictiveSearchManagerImpl.class,
                        new StringBuilder("Success to load constant data for the site=").append(siteKey).append("|Current version=")
                            .append(ver.genVersion()).toString());
                    return objList;
                }
            }
        }
        return null;
    }

    /**
     * The Class SiteDataInfo.
     */
    public static class SiteDataInfo {

        private String site;

        /** The object list. */
        private List<SearchableObject> objectList;

        /**
         * Gets the object list.
         *
         * @return the objectList
         */
        public List<SearchableObject> getObjectList() {
            return this.objectList;
        }

        /**
         * Sets the object list.
         *
         * @param objectList
         *            the objectList to set
         */
        public void setObjectList(final List<SearchableObject> objectList) {
            this.objectList = objectList;
        }

        /**
         * @return the site
         */
        public String getSite() {
            return this.site;
        }

        /**
         * @param site
         *            the site to set
         */
        public void setSite(final String site) {
            this.site = site;
        }
    }

    /**
     * The Class LoadStatus.
     */
    public static class LoadStatus {
        // check if no data from WPC exceeds one day
        /** The is data unavailable. */
        private boolean isDataUnavailable = false;
        // check if any data parsing error
        /** The is data parsing err. */
        private boolean isDataParsingErr = false;

        private String dataUnavailableMessage;

        private String dataParsingErrMessage;

        /**
         * Instantiates a new load status.
         *
         * @param isDataUnavailableParameter
         *            the is data unavailable parameter
         * @param isDataParsingErrParameter
         *            the is data parsing err parameter
         */
        public LoadStatus(final boolean isDataUnavailableParameter, final boolean isDataParsingErrParameter) {
            this.isDataUnavailable = isDataUnavailableParameter;
            this.isDataParsingErr = isDataParsingErrParameter;
        }

        /**
         * Checks if is data unavailable.
         *
         * @return the isDataUnavailable
         */
        public boolean isDataUnavailable() {
            return this.isDataUnavailable;
        }

        /**
         * Sets the data unavailable.
         *
         * @param isDataUnavailable
         *            the isDataUnavailable to set
         */
        public void setDataUnavailable(final boolean isDataUnavailable, final String dataUnavailableMessage) {
            this.isDataUnavailable = isDataUnavailable;
            if (isDataUnavailable) {
                this.dataUnavailableMessage = dataUnavailableMessage;
            }
        }

        /**
         * Checks if is data parsing err.
         *
         * @return the isDataParsingErr
         */
        public boolean isDataParsingErr() {
            return this.isDataParsingErr;
        }

        /**
         * Sets the data parsing err.
         *
         * @param isDataParsingErr
         *            the isDataParsingErr to set
         */
        public void setDataParsingErr(final boolean isDataParsingErr, final String dataParsingErrMessage) {
            this.isDataParsingErr = isDataParsingErr;
            if (isDataParsingErr) {
                this.dataParsingErrMessage = dataParsingErrMessage;
            }
        }

        /**
         * @return the dataUnavailableMessage
         */
        public String getDataUnavailableMessage() {
            return this.dataUnavailableMessage;
        }

        /**
         * @param dataUnavailableMessage
         *            the dataUnavailableMessage to set
         */
        public void setDataUnavailableMessage(final String dataUnavailableMessage) {
            this.dataUnavailableMessage = dataUnavailableMessage;
        }

        /**
         * @return the dataParsingErrMessage
         */
        public String getDataParsingErrMessage() {
            return this.dataParsingErrMessage;
        }

        /**
         * @param dataParsingErrMessage
         *            the dataParsingErrMessage to set
         */
        public void setDataParsingErrMessage(final String dataParsingErrMessage) {
            this.dataParsingErrMessage = dataParsingErrMessage;
        }
    }

    /**
     * The Class InitSearchData.
     */
    public static class InitSearchData extends java.util.TimerTask {

        /** The task manager. */
        private PredictiveSearchManager taskManager;

        /**
         * Instantiates a new inits the search data.
         *
         * @param tempManager
         *            the temp manager
         */
        public InitSearchData(final PredictiveSearchManager tempManager) {
            this.taskManager = tempManager;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.util.TimerTask#run()
         */
        public void run() {
            try {
                LogUtil.warn(PredictiveSearchManagerImpl.class, "start to load index");
                this.taskManager.loadData();
            } catch (Exception e) {
                if (e instanceof CommonException) {
                    CommonException commonException = (CommonException) e;
                    LogUtil.error(PredictiveSearchManagerImpl.class, "Failed to init search data:" + commonException.getMessage()
                        + ", error message: " + commonException.getErrMessage(), e);
                } else {
                    LogUtil.error(PredictiveSearchManagerImpl.class, "Failed to init search data:" + e.getMessage(), e);
                }
            }
        }

        /**
         * Gets the task manager.
         *
         * @return the taskManager
         */
        public PredictiveSearchManager getTaskManager() {
            return this.taskManager;
        }

        /**
         * Sets the task manager.
         *
         * @param taskManager
         *            the taskManager to set
         */
        public void setTaskManager(final PredictiveSearchManager taskManager) {
            this.taskManager = taskManager;
        }
    }
}