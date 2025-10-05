/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.Product;
import com.hhhh.group.secwealth.mktdata.elastic.component.ProductEntities;
import com.hhhh.group.secwealth.mktdata.elastic.dao.GRCompanyRepository;
import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import com.hhhh.group.secwealth.mktdata.elastic.dao.spec.CompanySpecification;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler.BMCComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;

@Service
@EnableScheduling
public class ScheduleDataLoadService {

    private static Logger logger = LoggerFactory.getLogger(ScheduleDataLoadService.class);

    @Autowired
    private AppProperties appProperty;

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private VerifyFilesService verifyFilesService;

    @Autowired
    private DataHandlerService dataHandler;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private Client elasticsearchClient;

    @Autowired
    private BMCComponent bmcComponent;

    @Autowired
    private ProductEntities productEntities;

    @Autowired
    private DataHandlerService dataHandlerService;

    @Autowired
    private GRCompanyRepository grCompanyRepository;

    @Autowired
    private CompanySpecification companySpecification;

    @Autowired
    private PredsrchCommonService predsrchCommonService;

    @Value("${predsrch.esSetting}")
    private String esSetting;

    @Value("${predsrch.esMapping}")
    private String esMapping;

    @Value("${predsrch.enableRetry:true}")
    private boolean enableRetry;

    @Value("${predsrch.retryTimes:3}")
    private int retryTimes;

    @Value("${predsrch.allXmlParseSuccess:true}")
    private boolean allXmlParseSuccess;

    public static final String UNDERSCORE = "_";

    private static final String BMC_ERROR_TEXT = "Write BMC encounter error";

    private static final String CURRENT_VERSION_TEXT = "|Current version=";

    private static final String OBJ_LIST_WPC_DATA_EXPIRED_TEXT = "getObjectListBySite|The WPC data is expired=";

    @Scheduled(cron = "${schedule.cron}")
    // cron expression:second minute hour day month week year(optional)
    public void loadData() throws Exception {
        // start volumeService
        Exception volumeServiceErr = null;
        Exception verifyFilesErr = null;
        try {
            ScheduleDataLoadService.logger.info("start copy wpc files to local");
            long startTime = System.currentTimeMillis();
            ScheduleDataLoadService.logger.info("-----start copy file  ------ {}", startTime);
            this.volumeService.copyToLocalData();
            ScheduleDataLoadService.logger.info("-----Copy file end.Used time ------ {}", (System.currentTimeMillis() - startTime));
        } catch (Exception err) {
            volumeServiceErr = err;
        }

        try {
            ScheduleDataLoadService.logger.warn("start verify wpc files with MD5");
            this.verifyFilesService.verifyFiles();
        } catch (Exception err) {
            verifyFilesErr = err;
        }

        // start loadData
        ScheduleDataLoadService.logger.info("load data start: {}", System.currentTimeMillis());
        LoadStatus status = this.loadDataIntoIndexBySiteKey();
        ScheduleDataLoadService.logger.info("load data end: {}", System.currentTimeMillis());
        // clear all old data files
        this.dataHandler.clearDataFiles();
        this.handleVolumeServiceErr(volumeServiceErr);
        this.handleVerifyFilesErr(volumeServiceErr, verifyFilesErr);
        this.handleIfDataUnavailable(status);
        this.handleIfDataParsingErr(status);
    }

    private LoadStatus loadDataIntoIndexBySiteKey() {
        LoadStatus status = new LoadStatus(false, false);
        Map<String, SiteDataInfo> map = new WeakHashMap<>();
        for (String siteKey : this.appProperty.getSupportSites()) {
            // ignore default site
            if (CommonConstants.DEFAULT.equalsIgnoreCase(siteKey)) {
                continue;
            }
            List<SiteDataInfo> siteDataList = new ArrayList<>();
            try {
                List<CustomizedEsIndexForProduct> objList = new ArrayList<>();

                SiteDataInfo info = map.get(siteKey);
                if (null != info) {
                    siteDataList.add(info);
                } else {
                    info = getVersionInfoBySite(siteKey, status);
                    siteDataList.add(info);
                    map.put(siteKey, info);
                }

                if (ListUtil.isValid(siteDataList)) {
                    boolean needUpdateData = this.isNeedUpdateData(siteDataList);
                    // may need update for gr?
                    this.findLatestUpdateForGr(needUpdateData);
                    this.updateObjListByNeedUpdateFlag(status, siteDataList, objList, needUpdateData);
                } else {
                	ScheduleDataLoadService.logger.info("siteDataList is null or the list is empty.");
                }

                if (ListUtil.isValid(objList)) {
                    String sequence = objList.get(0).getSequence();
                    objList.add(createHealthCheckObj(sequence));
                    long currentTimeMillis = System.currentTimeMillis();
                    ScheduleDataLoadService.logger.info("save index start {}", currentTimeMillis);
                    List<IndexQuery> indexList = new ArrayList<>();

                    String indexName = siteKey.toLowerCase();

                    String currentIndexName = indexName + ScheduleDataLoadService.UNDERSCORE + sequence
                        + ScheduleDataLoadService.UNDERSCORE + currentTimeMillis;
                    Iterator<String> keysIt = this.elasticsearchClient.admin().cluster().prepareState().get()
                        .getState().getMetaData().getIndices().keysIt();
                    List<String> deleteList = getDeleteIndexList(indexName, keysIt);
                    this.elasticsearchRestTemplate.indexOps(IndexCoordinates.of(currentIndexName)).create(Document.parse(loadFromFile(this.esSetting)));
                    this.elasticsearchRestTemplate.indexOps(IndexCoordinates.of(currentIndexName)).putMapping(Document.parse(loadFromFile(this.esMapping)));
                    this.saveData(objList, indexList, currentIndexName);
                    ScheduleDataLoadService.logger
                        .info("save index start, total last seconds {}", (System.currentTimeMillis() - currentTimeMillis) / 1000);
                    IndexCoordinates coordinates=IndexCoordinates.of(currentIndexName);
                    this.elasticsearchRestTemplate.indexOps(coordinates).refresh();
                    indexList.clear();
                    ScheduleDataLoadService.logger.info("save index completed, total number is {}", objList.size());
                    currentTimeMillis = System.currentTimeMillis();
                    ScheduleDataLoadService.logger.info("start delete old data {}", currentTimeMillis);
                    this.deleteOldIndex(deleteList);
                } else {
                	ScheduleDataLoadService.logger.info("[CustomizedEsIndexForProduct] index list is null (variable objList is null)");
                }
                objList.clear();
            } catch (Exception e) {
                ScheduleDataLoadService.logger.error("loadData|Data parsing error for the support site: {}", siteKey);
                ScheduleDataLoadService.logger.error("Data parsing error", e);
                status.setDataParsingErr(true,
                    "loadData|Data parsing error for the support site=" + siteKey + ", error message: " + e.getMessage());
            } finally {
                siteDataList.clear();
            }
        }
        map.clear();
        return status;
    }

    /**
     *
     * @param needUpdateData whether need to replace all cache
     * if no need update, then just update the cache by gr recently update product
     */
    public void findLatestUpdateForGr(boolean needUpdateData) {
        if (needUpdateData) {
            return ;
        }
        try {
            // sleep one minutes before search data from DB
            TimeUnit.MINUTES.sleep(1);
            List<GRCompanyPo> latestUpdateCompanies = this.grCompanyRepository.findAll(this.companySpecification.getRecentUpdateSpec(""));
            if (ListUtil.isValid(latestUpdateCompanies)) {
                Map<String, List<GRCompanyPo>> latestUpdateMap = latestUpdateCompanies.stream().collect(Collectors.groupingBy(GRCompanyPo::getMarket));
                List<CustomizedEsIndexForProduct> latestUpdateSearchResults = new ArrayList<>();
                for (Map.Entry<String, List<GRCompanyPo>> entry : latestUpdateMap.entrySet()) {
                    String market = entry.getKey();
                    List<GRCompanyPo> companies = entry.getValue();
                    List<String> productSymbols = companies.stream().map(GRCompanyPo::getSymbol).collect(Collectors.toList());
                    logger.info("GR {} market latest recently update list: {}.", market, productSymbols);
                    this.checkDataIntegrity(market, latestUpdateSearchResults, productSymbols);
                }

                if (ListUtil.isValid(latestUpdateSearchResults)) {
                    for (CustomizedEsIndexForProduct product : latestUpdateSearchResults) {
                        if (!"Y".equalsIgnoreCase(product.getHouseViewRecentUpdate())) {
                            Document document = Document.create();
                            document.append("houseViewRating", product.getHouseViewRating());
                            document.append("houseViewRecentUpdate", "Y");
                            document.append("houseViewIndicator", "Y");
                            UpdateQuery updateQuery = UpdateQuery.builder(product.getId()).withDocument(document)
                                    .withDocAsUpsert(true).build();
                            this.elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of(this.predsrchCommonService.getCurrentIndexName("hk_hbap")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to update the cache for GR data!", e);
            Thread.currentThread().interrupt();
        }

    }

    /**
     *
     * @param market product market code
     * @param latestUpdateSearchResults product list in cache
     * @param productSymbols product list in db
     * check the data integrity between db and cache, and log the not found in cache
     */
    private void checkDataIntegrity(String market, List<CustomizedEsIndexForProduct> latestUpdateSearchResults, List<String> productSymbols) {
        List<CustomizedEsIndexForProduct> searchResults = this.predsrchCommonService.customizedEsIndexForProductSearch(market, productSymbols);
        if (ListUtil.isValid(searchResults)) {
            List<String> searchResultSymbols = searchResults.stream().map(CustomizedEsIndexForProduct::getSymbol).collect(Collectors.toList());
            Collection<String> subtract = CollectionUtils.subtract(productSymbols, searchResultSymbols);
            if (CollectionUtils.isNotEmpty(subtract)) {
                logger.error("Can not found these GR products: {} from WPC files!", subtract);
            }
            latestUpdateSearchResults.addAll(searchResults);
        } else {
            logger.error("Can not found these GR products: {} from WPC files!", productSymbols);
        }
    }

    /**
     *
     * @param objList the product list which parse from wpc xmls files
     * @param indexList index product list
     * @param currentIndexName current index name need to be create
     */
    private void saveData(List<CustomizedEsIndexForProduct> objList, List<IndexQuery> indexList, String currentIndexName) {
        for (CustomizedEsIndexForProduct ind : objList) {
            IndexQuery fooIdxQuery = new IndexQueryBuilder().withObject(ind).build();
            indexList.add(fooIdxQuery);
            if (indexList.size() == 3000) {
                this.elasticsearchRestTemplate.bulkIndex(indexList, IndexCoordinates.of(currentIndexName));
                ScheduleDataLoadService.logger.info("3000 records saved");
                indexList.clear();
            }
        }
        if (!indexList.isEmpty()) {
            this.elasticsearchRestTemplate.bulkIndex(indexList,IndexCoordinates.of(currentIndexName));
        }
    }

    private List<String> getDeleteIndexList(String indexName, Iterator<String> keysIt) {
        List<String> deleteList = new ArrayList<>();
        while (keysIt.hasNext()) {
            String oldIndexName = keysIt.next();
            if (oldIndexName.contains(indexName)) {
                deleteList.add(oldIndexName);
            }
        }
        return deleteList;
    }

    private void deleteOldIndex(List<String> deleteList) {
        if (ListUtil.isValid(deleteList)) {
            for (String deleteName : deleteList) {
                this.elasticsearchRestTemplate.indexOps(IndexCoordinates.of(deleteName)).delete();
                ScheduleDataLoadService.logger.info("delete index with name {}", deleteName);
            }
        }
    }

    /**
     * if all xml files parse success, then check whether need create new index by siteDataList
     * @param siteDataList
     * @return
     */
    private boolean isNeedUpdateData(List<SiteDataInfo> siteDataList) {
        boolean needUpdateData = false;
        for (SiteDataInfo tempInfo : siteDataList) {
            List<CustomizedEsIndexForProduct> list = tempInfo.getObjectList();
            if (this.allXmlParseSuccess && ListUtil.isValid(list)) {
                needUpdateData = true;
                break;
            }
        }
        ScheduleDataLoadService.logger.info("needUpdateData: {}", needUpdateData);
        return needUpdateData;
    }

    private void handleIfDataParsingErr(LoadStatus status) {
        if (status.isDataParsingErr) {
            // note: seems in wpc test file, will always have some parsing errors.
            ScheduleDataLoadService.logger.debug("PredictiveSearch is Data Parsing Err: Message: {}",
                status.getDataParsingErrMessage());
            String traceCode = ExTraceCodeGenerator.generate();
            try {
                this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_DATAPARSINGERR), traceCode);
            } catch (IOException e1) {
                ScheduleDataLoadService.logger.error(BMC_ERROR_TEXT, e1);
            }
        }
    }

    protected void handleIfDataUnavailable(LoadStatus status) {
        if (status.isDataUnavailable) {
            ScheduleDataLoadService.logger.debug("PredictiveSearch is Data Unavailable: Message: {}",
                status.getDataParsingErrMessage());
            String traceCode = ExTraceCodeGenerator.generate();
            try {
                this.bmcComponent.doBMC(new VendorException(ExCodeConstant.EX_CODE_DATAUNAVAILABLE), traceCode);
            } catch (IOException e1) {
                ScheduleDataLoadService.logger.error(BMC_ERROR_TEXT, e1);
            }
        }
    }

    private void handleVerifyFilesErr(Exception volumeServiceErr, Exception verifyFilesErr) {
        if (verifyFilesErr != null) {
            ScheduleDataLoadService.logger.error("MD5 verify files error:", volumeServiceErr);
            String traceCode = ExTraceCodeGenerator.generate();
            try {
                this.bmcComponent.doBMC(new VendorException(verifyFilesErr.getMessage()), traceCode);
            } catch (IOException e1) {
                ScheduleDataLoadService.logger.error(BMC_ERROR_TEXT, e1);
            }
        }
    }

    private void handleVolumeServiceErr(Exception volumeServiceErr) {
        if (volumeServiceErr != null) {
            ScheduleDataLoadService.logger.error("volumeService download files error: {}", volumeServiceErr.getMessage());
            String traceCode = ExTraceCodeGenerator.generate();
            try {
                this.bmcComponent.doBMC(new VendorException(volumeServiceErr.getMessage()), traceCode);
            } catch (IOException e1) {
                ScheduleDataLoadService.logger.error(BMC_ERROR_TEXT, e1);
            }
        }
    }

    private void updateObjListByNeedUpdateFlag(LoadStatus status, List<SiteDataInfo> siteDataList, List<CustomizedEsIndexForProduct> objList, boolean needUpdateData) throws Exception {
        if (needUpdateData) {
            for (SiteDataInfo tempInfo : siteDataList) {
                List<CustomizedEsIndexForProduct> list = tempInfo.getObjectList();
                if (ListUtil.isValid(list)) {
                    objList.addAll(list);
                } else {
                    // when this condition happen?necessary?
                    List<CustomizedEsIndexForProduct> tempList = getCurrentVersionData(tempInfo.getSite(), status);
                    if (ListUtil.isValid(tempList)) {
                        objList.addAll(tempList);
                    }
                }
            }
        }
    }

    private CustomizedEsIndexForProduct createHealthCheckObj(final String sequence) {
        CustomizedEsIndexForProduct utb = new CustomizedEsIndexForProduct();
        utb.setId(PredictiveSearchConstant.SYMBOL_HEALTHCHECK);
        utb.setProductName(CommonConstants.EMPTY_STRING);
        utb.setSymbol(CommonConstants.EMPTY_STRING);
        utb.setPopularity(CommonConstants.POPULARITY);
        utb.setSequence(sequence);
        return utb;
    }

    private List<CustomizedEsIndexForProduct> getCurrentVersionData(final String site, final LoadStatus status) throws Exception {
        // get latest object by wpc data file
        Version currentVer = this.dataHandler.getCurrentVerBySite().get(site);
        return getObjectListBySite(site, null, currentVer, status);
    }

    /**
     *
     * @param site site key
     * @param status
     * @return
     * @throws Exception
     */
    private SiteDataInfo getVersionInfoBySite(final String site, final LoadStatus status) throws Exception {
        // get latest object by wpc data file
        Version currentVer = this.dataHandler.getCurrentVerBySite().get(site);// will be null at first load time，
        Version latestVer = this.dataHandler.getLatestVersionBySite(site);// find latest file from local,or will be null
        SiteDataInfo info = new SiteDataInfo();
        info.setSite(site);
        List<CustomizedEsIndexForProduct> list = new ArrayList<>();
        // INDEX load on predictive_search folder
        ScheduleDataLoadService.logger.info("Start load INDEX from predictive_search folder..");
        ScheduleDataLoadService.logger.info("currentVersion is {}", currentVer);
        ScheduleDataLoadService.logger.info("latestVersion is {}", latestVer);
        if (null == currentVer && null == latestVer) {
            ScheduleDataLoadService.logger.error("Please check the wpc .out files under wpc outgoing folder. Usually due to wpc .out files missing.");
        }
        if (null != latestVer) {
            list = getObjectListBySite(site, latestVer, currentVer, status);
        } else {
            if (null != currentVer && !currentVer.isCheckedExpired() && this.dataHandler.ifDataExpired(site)) {
                currentVer.setCheckedExpired(true);
            }
        }
        int listSize = 0;
        if (ListUtil.isValid(list)) {
            listSize = list.size();
        }
        ScheduleDataLoadService.logger.info("INDEX load result: {}", listSize);
        ScheduleDataLoadService.logger.info("End load INDEX from predictive_search folder..");
        info.setObjectList(list);
        return info;
    }

    private Product getProductType(DataSiteEntity.DataFileInfo info, final String siteKey){
        String productType = info.getProductType();
        Product product = this.productEntities.getProductEntities().get(// product-description.xml
                StringUtil.combineWithUnderline(siteKey, productType));
        // if the site product don't exist,then use default product.
        // Actually,it will not include site info.-Fred
        if (product == null) {
            product = this.productEntities.getProductEntities().get(productType);// like
        }

        if (product == null) {
            String typePrefix = productType.replaceAll("[^a-zA-Z].+$", "");
            product = this.productEntities.getProductEntities().get(typePrefix);
        }
        return product;
    }

    /**
     *
     * @param siteKey site key
     * @param latestVer the latest version
     * @param currentVer the current version
     * @param status
     * @return
     * @throws Exception
     */
    protected List<CustomizedEsIndexForProduct> getObjectListBySite(final String siteKey, final Version latestVer,
        final Version currentVer, final LoadStatus status) throws Exception {
        // get the latest files by site and version
        File[] files = null;
        Version usedVersion = null;
        if (null != latestVer) {
            files = this.dataHandler.getLatestFilesBySite(siteKey, latestVer, false);
            usedVersion = latestVer;
        } else if (null != currentVer) {// get file that match the version
            files = this.dataHandler.getLatestFilesBySite(siteKey, currentVer, false);
            usedVersion = currentVer;
        }
        if (ArrayUtils.isNotEmpty(files)) {
            DataSiteEntity entity = this.dataHandler.buildFileEntityBySite(siteKey, files, usedVersion);
            int parseXmlFilesSuccessNum = 0;
            int retryCount = 0;
            retryIfNecessary(siteKey, usedVersion, entity, parseXmlFilesSuccessNum, retryCount);
            List<CustomizedEsIndexForProduct> objList = entity.getObjList();
            if (ListUtil.isValid(objList)) {
                if (entity.isParsingError()) {
                    ScheduleDataLoadService.logger.warn("getObjectListBySite|Data parsing error for the site: {}", siteKey);
                    status.setDataParsingErr(true, "getObjectListBySite|Data parsing error for the site=" + siteKey);
                }
                // first load time will not be null
                if (null != latestVer) {
                    // not expired
                    latestVer.setCheckedExpired(false);
                    // set version as currentVersion
                    this.dataHandler.getCurrentVerBySite().put(siteKey, latestVer);
                    this.logByVersion(siteKey, latestVer);
                } else if (null != currentVer) {
                    this.logByVersion(siteKey, currentVer);
                }
                return objList;
            } else {
                updateStatusIfProductListInvalid(siteKey, latestVer, currentVer, status);
            }
        } else if (null != latestVer) {
            // no file, latest version is not empty,check current version is not null&not expired,if no,set current version expired.
            this.updateStatusIfFilesInvalid(siteKey, latestVer, currentVer, status, files);
        }
        return Lists.newArrayList();
    }

    /**
     * if no new version, the mark the current version status as expire
     * @param siteKey
     * @param latestVer
     * @param currentVer
     * @param status
     * @param files
     */
    private void updateStatusIfFilesInvalid(String siteKey, Version latestVer, Version currentVer, LoadStatus status, File[] files) {
        if (null == files){
            ScheduleDataLoadService.logger.error("Copy all the config files failed, stop replace the current index, the lastest file version is: {}", latestVer);
        }
        ScheduleDataLoadService.logger.warn("No latest version data files for the site: {}", siteKey);
        if (null != currentVer && !currentVer.isCheckedExpired()) {
            currentVer.setCheckedExpired(true);
            ScheduleDataLoadService.logger.warn("GetObjectListBySite|The WPC data is expired, siteKey is {}", siteKey);
            status.setDataUnavailable(true, OBJ_LIST_WPC_DATA_EXPIRED_TEXT + siteKey);
        }
    }

    private void updateStatusIfProductListInvalid(String siteKey, Version latestVer, Version currentVer, LoadStatus status) {
        ScheduleDataLoadService.logger.warn("getObjectListBySite|No SearchableObject to be loaded for the site {}", siteKey);
        if (null != latestVer && null != currentVer && !currentVer.isCheckedExpired()) {
            currentVer.setCheckedExpired(true);
            ScheduleDataLoadService.logger.warn("{}, getObjectListBySite|The WPC data is expired", siteKey);
            status.setDataUnavailable(true, OBJ_LIST_WPC_DATA_EXPIRED_TEXT + siteKey);
        }
    }

    private void logByVersion(String siteKey, Version latestVer) {
        if (logger.isWarnEnabled()) {
            ScheduleDataLoadService.logger.warn(new StringBuilder("Success to load data for the site=").append(siteKey)
                    .append(CURRENT_VERSION_TEXT).append(latestVer.genVersion()).toString());
        }
    }

    /**
     * re-load files to ensure all the files in config <b>dataTypeBySite<b/> parse success
     * @param siteKey
     * @param usedVersion
     * @param entity
     * @param parseXmlFilesSuccessNum
     * @param retryCount
     * @throws InterruptedException
     */
    private void retryIfNecessary(String siteKey, Version usedVersion, DataSiteEntity entity, int parseXmlFilesSuccessNum, int retryCount) throws InterruptedException {
        List<DataSiteEntity.DataFileInfo> dataFiles = entity.getDataFiles();
        if (this.enableRetry && ListUtil.isValid(dataFiles)){
            while (parseXmlFilesSuccessNum != dataFiles.size() && retryCount <= this.retryTimes){
                parseXmlFilesSuccessNum = 0;
                List<DataSiteEntity.DataFileInfo> needRetryFilesList = new ArrayList<>();
                for (DataSiteEntity.DataFileInfo dataFileInfo:dataFiles){
                    if (dataFileInfo.isParseSuccess()){
                        parseXmlFilesSuccessNum++;
                    } else {
                        needRetryFilesList.add(dataFileInfo);
                    }
                }
                retryCount = retryLoadFileData(siteKey, usedVersion, entity, retryCount, needRetryFilesList);
            }

            this.updateParseFlag(parseXmlFilesSuccessNum, retryCount, dataFiles);
        }
    }

    private void updateParseFlag(int parseXmlFilesSuccessNum, int retryCount, List<DataSiteEntity.DataFileInfo> dataFiles) {
        if (parseXmlFilesSuccessNum != dataFiles.size() && retryCount > this.retryTimes){
            ScheduleDataLoadService.logger.error("Retry three times, but still have xml parse failed");
            this.allXmlParseSuccess = false;
        } else {
            ScheduleDataLoadService.logger.info("All xml files parse success");
            this.allXmlParseSuccess = true;
        }
    }

    private int retryLoadFileData(String siteKey, Version usedVersion, DataSiteEntity entity, int retryCount, List<DataSiteEntity.DataFileInfo> needRetryFilesList) throws InterruptedException {
        if (ListUtil.isValid(needRetryFilesList)){
            retryCount++;
            for (DataSiteEntity.DataFileInfo dataFileInfo:needRetryFilesList){
                Product product = this.getProductType(dataFileInfo, siteKey);
                if (null != product && retryCount <= this.retryTimes){
                    ScheduleDataLoadService.logger.warn("Retry to parse the xml file, the file name is:{}, retryTimes is:{}", dataFileInfo.getDataFile().getName(), retryCount);
                    this.sleepByRetryCount(retryCount);
                    dataHandlerService.handleData(siteKey, product, dataFileInfo, entity, usedVersion);
                }
            }
        }
        return retryCount;
    }

    private void sleepByRetryCount(int retryCount) throws InterruptedException {
        if (retryCount > 0) {
            if (retryCount < this.retryTimes) {
                Thread.sleep(3000);
            } else {
                Thread.sleep(5000);
            }
        }
    }

    protected String loadFromFile(final String fileName) throws IllegalStateException {
        StringBuilder buffer = new StringBuilder(2048);
        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            while (reader.ready()) {
                buffer.append(reader.readLine());
                buffer.append(' ');
            }
        } catch (Exception e) {
            throw new IllegalStateException("couldn't load file " + fileName, e);
        }
        return buffer.toString();
    }

    /**
     * The Class SiteDataInfo.
     */
    public class SiteDataInfo {

        private String site;

        /** The object list. */
        private List<CustomizedEsIndexForProduct> objectList;

        public String getSite() {
            return this.site;
        }

        public void setSite(final String site) {
            this.site = site;
        }

        public List<CustomizedEsIndexForProduct> getObjectList() {
            return this.objectList;
        }

        public void setObjectList(final List<CustomizedEsIndexForProduct> objectList) {
            this.objectList = objectList;
        }

    }

    /**
     * The Class LoadStatus.
     */
    public class LoadStatus {
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

        public void setDataParsingErr(final boolean isDataParsingErr, final String dataParsingErrMessage) {
            this.isDataParsingErr = isDataParsingErr;
            if (isDataParsingErr) {
                this.dataParsingErrMessage = dataParsingErrMessage;
            }
        }

        public String getDataUnavailableMessage() {
            return this.dataUnavailableMessage;
        }

        public void setDataUnavailableMessage(final String dataUnavailableMessage) {
            this.dataUnavailableMessage = dataUnavailableMessage;
        }

        public String getDataParsingErrMessage() {
            return this.dataParsingErrMessage;
        }

        public void setDataParsingErrMessage(final String dataParsingErrMessage) {
            this.dataParsingErrMessage = dataParsingErrMessage;
        }
    }
}
