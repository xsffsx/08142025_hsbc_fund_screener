/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.hhhh.group.secwealth.mktdata.common.exception.BaseException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.svc.response.Response;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.DurationExceptionUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

@Component("bmcAdvisorBySite")
public class BMCAdvisorBySite {

    /** The site entitys. */
    private ConcurrentHashMap<String, SiteExceptionEntity> siteEntitys;

    /** The last update of config. */
    private Long lastUpdateOfConfig;

    /** The last update of mapping. */
    private Long lastUpdateOfMapping;

    /** The bmc log file path. */
    private String bmcLogFilePath;

    /** The mapping file url. */
    private URL mappingFileURL;

    /** The config file url. */
    private URL configFileURL;

    @Value("#{systemConfig['app.bmc.configPath']}")
    private String configPath;

    @Value("#{systemConfig['app.bmc.bmcExceptionConfig']}")
    private String bmcExceptionConfig;

    @Value("#{systemConfig['app.bmc.bmcExceptionMapping']}")
    private String bmcExceptionMapping;

    @Value("#{systemConfig['app.bmc.printFiles']}")
    private Boolean isPrintFiles;

    /**
     * <p>
     * <b> Initialize the exception type which defined in external config </b>
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @PostConstruct
    public void init() throws Exception {
        this.mappingFileURL = this.getClass().getClassLoader().getResource(this.configPath + this.bmcExceptionMapping);
        this.configFileURL = this.getClass().getClassLoader().getResource(this.configPath + this.bmcExceptionConfig);
        InputStream configIn = null;
        InputStream mappingIn = null;
        try {
            configIn = this.configFileURL.openStream();
            mappingIn = this.mappingFileURL.openStream();
            String mappingStr = StringUtil.streamToStringConvert(mappingIn);
            String configStr = StringUtil.streamToStringConvert(configIn);
            LogUtil.debug(BMCAdvisorBySite.class, "mappingStr: {}, configStr: {}", mappingStr, configStr);
            StringReader reader = new StringReader(configStr);
            Mapping mapping = new Mapping();
            mapping.loadMapping(new InputSource(new StringReader(mappingStr)));
            Unmarshaller unMarshaller = new Unmarshaller(SiteExceptionEntitys.class);
            unMarshaller.setIgnoreExtraElements(true);
            unMarshaller.setMapping(mapping);
            SiteExceptionEntitys entitys = (SiteExceptionEntitys) unMarshaller.unmarshal(reader);
            entitys.initSiteEntityMap();
            this.bmcLogFilePath = entitys.getBmcFilePath(StringUtil.getServerName());
            LogUtil.debug(BMCAdvisorBySite.class, this.bmcLogFilePath);
            this.siteEntitys = entitys.getSiteEntityMap();
        } catch (Exception e) {
            LogUtil.error(BMCAdvisorBySite.class, "Can't init BMCAdvisorBySite|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        } finally {
            try {
                if (configIn != null) {
                    configIn.close();
                }
                if (mappingIn != null) {
                    mappingIn.close();
                }
            } catch (Exception e) {
                LogUtil.error(BMCAdvisorBySite.class,
                    "Init BMCAdvisorBySite, can not close java.io.InputStream|exception=" + e.getMessage(), e);
                // throw new SystemException(e);
            }
        }

    }

    /**
     *
     * After throwing
     *
     * @param joinPoint
     * @param e
     */
    public void afterThrowing(final JoinPoint joinPoint, final Throwable e) {
        LogUtil.error(BMCAdvisorBySite.class, "BMC LOGGER --> afterReturning --> className: {}, method name: {}, error message: {}",
            joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), e.getMessage(), e);
        BaseException ex = null;
        if (e instanceof BaseException) {
            ex = (BaseException) e;
        } else {
            ex = new SystemException(ErrTypeConstants.SYSTEM_ERROR, e);
        }

        this.doBMCRecord(e, "", "", ex.getTraceCode());
    }

    /**
     * After returning
     *
     * @param joinPoint
     * @param event
     */
    public void afterReturning(final JoinPoint joinPoint, final Object event) {
        LogUtil.error(BMCAdvisorBySite.class, "BMC LOGGER --> afterReturning --> className: {}, method name: {}",
            joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            Throwable e = null;
            String countryCode = null;
            String groupMember = null;
            String traceCode = null;
            ResponseEntity<?> responseEntity = null;
            if ((args[0] != null && args[0] instanceof Throwable)) {
                e = (Throwable) args[0];
            }
            if ((args[1] != null && args[1] instanceof HttpServletRequest)) {
                HttpServletRequest request = (HttpServletRequest) args[1];
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String key = (String) headerNames.nextElement();
                    if (CommonConstants.REQUEST_HEADER_COUNTRYCODE.equals(key.toLowerCase())) {
                        countryCode = request.getHeader(key);
                    }
                    if (CommonConstants.REQUEST_HEADER_GROUPMEMBER.equals(key.toLowerCase())) {
                        groupMember = request.getHeader(key);
                    }
                    if (countryCode != null && groupMember != null) {
                        break;
                    }
                }
            }
            if (event != null && event instanceof ResponseEntity) {
                responseEntity = (ResponseEntity<?>) event;
                Response response = (Response) responseEntity.getBody();
                traceCode = response.getTraceCode();
            }
            if (e != null && traceCode != null) {
                this.doBMCRecord(e, countryCode, groupMember, traceCode);
            }
        }
    }

    /**
     * @param e
     * @param countryCode
     * @param groupMember
     * @param errTrackerCode
     */
    public void doBMCRecord(final Throwable e, final String countryCode, final String groupMember, String errTrackerCode) {
        try {
            // update config file or mapping file if changed
            if (this.isPrintFiles != null && true == this.isPrintFiles.booleanValue()) {
                updateConfig();
            }
            if (StringUtil.isInvalid(errTrackerCode) && e instanceof BaseException) {
                errTrackerCode = ((BaseException) e).getTraceCode();
            }
            LogUtil.error(BMCAdvisorBySite.class, "BMC LOGGER --> [ErrTrackerCode=" + errTrackerCode + "], error message: {}",
                e.getMessage());

            // ExceptionCounter as defined calculation
            SiteExceptionEntity entity = getEntity(countryCode, groupMember);
            if (null != entity.getExceptionCounters()) {
                ExceptionCounter count;
                String key = e.getClass().getName() + e.getMessage();
                LogUtil.debug(BMCAdvisorBySite.class, "BMCAdvisorBySite key=" + key);
                count = entity.getExceptionCounters().get(key);

                if (null != count) {
                    if (!count.ignoreException()) {
                        List<DurationException> exceptionList = count.getExceptionList();
                        DurationExceptionUtil.updateException(exceptionList, e, count.getTimeDuration());
                        int errNum = count.getMaxCount();
                        if (exceptionList.size() >= errNum) {
                            genBmcLog(count.getBmcErrCde(), count.getBmcErrMsg(), errTrackerCode);
                            exceptionList.clear();
                        }
                    }
                    return;
                }
            }

            // Exception exceed max number during specified durtion calculation
            List<DurationException> recentExceptionList = entity.getRecentExceptionList();
            DurationExceptionUtil.updateException(recentExceptionList, e, entity.getTimeDuration());
            int errNum = entity.getErrNum();
            int errNumStart = entity.getErrNumStart();
            if (recentExceptionList.size() >= errNum) {
                genBmcLog(entity.getErrCdeThrownPastSec(), entity.getErrmgsThrownPastSec(), errTrackerCode);
                recentExceptionList.clear();
                entity.totalCountDecrease(errNum - 1);
                return;
            }

            // Exception since start up exceed max number calculation
            entity.totalCountIncrement();
            if (entity.getTotalExceptionCount() >= errNumStart) {
                genBmcLog(entity.getErrCdeExceedLimit(), entity.getErrmgsExceedLimit(), errTrackerCode);
                entity.clearTotalCount();
            }
        } catch (Exception ex) {
            LogUtil.error(BMCAdvisorBySite.class, "The method afterThrowing occur error", ex);
        }
    }

    /**
     * <p>
     * <b> get SiteExceptionEntity by countryCode and groupMember. </b>
     * </p>
     *
     * @param countryCode
     *            the country code
     * @param groupMember
     *            the group member
     * @return the entity
     */
    private SiteExceptionEntity getEntity(final String countryCode, final String groupMember) {
        SiteExceptionEntity entity = this.siteEntitys.get(countryCode + groupMember);
        if (null != entity) {
            return entity;
        } else {
            return this.siteEntitys.get(CommonConstants.DEFAULT);
        }
    }

    /**
     * Update config.
     *
     * @throws Exception
     *             the exception
     */
    private void updateConfig() throws Exception {
        File configFile = new File(this.configFileURL.getFile());
        File mappingFile = new File(this.mappingFileURL.getFile());
        if (this.lastUpdateOfConfig == null) {
            this.lastUpdateOfConfig = 0L;
        }
        if (this.lastUpdateOfMapping == null) {
            this.lastUpdateOfMapping = 0L;
        }
        if (this.lastUpdateOfConfig.longValue() != configFile.lastModified()
            || this.lastUpdateOfMapping.longValue() != mappingFile.lastModified()) {
            init();
            this.lastUpdateOfConfig = configFile.lastModified();
            this.lastUpdateOfMapping = mappingFile.lastModified();
        }
    }

    /**
     * <p>
     * <b> genBmcLog</b>
     * </p>
     * .
     *
     * @param errorCode
     *            the error code
     * @param errorMsg
     *            the error msg
     * @param errTrackerCode
     *            the err tracker code
     */
    private void genBmcLog(final String errorCode, final String errorMsg, final String errTrackerCode) {
        String errMsg = "logger -t root " + errorCode + " " + errorMsg + " [ErrTrackerCode=" + errTrackerCode + "]";
        LogUtil.debug(BMCAdvisorBySite.class, "BMC file generation, is Print BMC Files=" + this.isPrintFiles);
        try {
            if (this.isPrintFiles != null && true == this.isPrintFiles.booleanValue()) {
                LogUtil.genLogFile(this.bmcLogFilePath, errMsg);
                LogUtil.debug(BMCAdvisorBySite.class,
                    "BMC file generation, path is " + this.bmcLogFilePath + ", errMsg is " + errMsg);
            } else {
                LogUtil.error(BMCAdvisorBySite.class,
                    "BMC_LOG=" + errorCode + " " + errorMsg + " [ErrTrackerCode=" + errTrackerCode + "]");
            }
        } catch (Exception e) {
            LogUtil.error(BMCAdvisorBySite.class,
                "BMC file generate fail, path is " + this.bmcLogFilePath + ", exception is " + e.getMessage(), e);
        }
    }
}
