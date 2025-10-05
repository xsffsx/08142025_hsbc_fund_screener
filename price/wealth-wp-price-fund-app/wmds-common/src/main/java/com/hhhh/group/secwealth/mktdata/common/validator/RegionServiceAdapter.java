package com.hhhh.group.secwealth.mktdata.common.validator;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.service.Service;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.CastorConverter;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.RegionEntity;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ServiceEntity;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;

import net.sf.json.JSONObject;

@Component("regionServiceAdapter")
public class RegionServiceAdapter {

    // key=countryCode+"_"+groupMember
    /** The region entities. */
    private Map<String, RegionEntity> regionEntities = new ConcurrentHashMap<String, RegionEntity>();
    // key=countryCode+"_"+groupMember+"_"+serviceId
    /** The services. */
    @Resource(name = "servicesHolder")
    private Map<String, Service> servicesHolder;

    @Value("#{systemConfig['app.validator.configPath']}")
    private String sitePath;

    /** The support sites. */
    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    /** The Constant REGION_MAPPING_FILE. */
    @Value("#{systemConfig['app.validator.regionEntityMapping']}")
    private String regionMappingFile;

    /** The Constant COMMON_VALIDATORS_CONFIG. */
    @Value("#{systemConfig['app.validator.commonValidators']}")
    private String commonValidatorsConfig;

    /** The custom validators. */
    @Value("#{systemConfig['app.validator.customValidators']}")
    private String customValidators;

    /** The Constant CONFIG_PREFIX. */
    @Value("#{systemConfig['app.validator.regionEntityConfig']}")
    private String configPrefix;

    @PostConstruct
    public void init() throws Exception {
        try {
            LogUtil.debug(RegionServiceAdapter.class, "RegionServiceAdapter start to init");

            String instanceName = System.getProperty(LogUtil.INSTANCE_NAME);
            String app = System.getProperty(LogUtil.LOG_APP);
            String cfOrg = System.getProperty(LogUtil.LOG_CF_ORG);
            String cfSpace = System.getProperty(LogUtil.LOG_CF_SPACE);
            String version = System.getProperty(LogUtil.LOG_VERSION);
            LogUtil.info(RegionServiceAdapter.class, "System Property - instanceName: " + instanceName + " ,app: " + app
                + " ,cfOrg: " + cfOrg + " ,cfSpace: " + cfSpace + " ,version: " + version);

            URL commonValidates = this.getClass().getClassLoader().getResource(this.sitePath + this.commonValidatorsConfig);
            if (StringUtil.isValid(this.customValidators)) {
                URL customValidates = this.getClass().getClassLoader().getResource(this.sitePath + this.customValidators);
                ValidatorMapper.init(commonValidates.openStream(), customValidates.openStream());
            } else {
                ValidatorMapper.init(commonValidates.openStream());
            }
            loadRegionConfig();
            LogUtil.debug(RegionServiceAdapter.class, "RegionServiceAdapterImpl initialization have been done");
        } catch (Exception e) {
            LogUtil.error(RegionServiceAdapter.class,
                "SystemException: RegionServiceAdapter, init, Can't init RegionServiceAdapter|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    /**
     * Load region config.
     * 
     * @throws Exception
     *             the exception
     */
    public void loadRegionConfig() throws Exception {
        LogUtil.debug(RegionServiceAdapter.class, "updateRegionConfig() start");
        for (int i = 0; i < this.supportSites.length; i++) {
            String siteKey = this.supportSites[i];
            URL url = this
                .getClass()
                .getClassLoader()
                .getResource(
                    new StringBuffer(this.sitePath).append(this.configPrefix).append(CommonConstants.SYMBOL_LINE_CONNECTIVE)
                        .append(siteKey).append(CommonConstants.XML_FILE_EXTENSION).toString());
            if (null == url) {
                url = this
                    .getClass()
                    .getClassLoader()
                    .getResource(
                        new StringBuffer(this.sitePath).append(this.configPrefix).append(CommonConstants.SYMBOL_LINE_CONNECTIVE)
                            .append(CommonConstants.DEFAULT).append(CommonConstants.XML_FILE_EXTENSION).toString());
            }
            InputStream configIn = null;
            InputStream mappingIn = null;
            try {
                configIn = url.openStream();
                URL mappingFile = this.getClass().getClassLoader().getResource(this.sitePath + this.regionMappingFile);
                mappingIn = mappingFile.openStream();
                String mappingContent = StringUtil.streamToStringConvert(mappingIn);
                String configContent = StringUtil.streamToStringConvert(configIn);
                RegionEntity entity = (RegionEntity) CastorConverter.castorXMLToBeanConvert(configContent, mappingContent,
                    RegionEntity.class, false);

                this.regionEntities.put(siteKey, entity);
            } catch (Exception e) {
                LogUtil.error(RegionServiceAdapter.class,
                    "SystemException: RegionServiceAdapter, loadRegionConfig error, supportSites: " + this.supportSites[i]
                        + " ,sitePath: " + this.sitePath + " ,regionMappingFile: " + this.regionMappingFile, e);
                throw new SystemException(e);
            } finally {
                if (configIn != null) {
                    configIn.close();
                }
                if (mappingIn != null) {
                    mappingIn.close();
                }
            }
        }
    }

    public boolean isSiteSupport(final String key) throws Exception {
        for (String s : this.supportSites) {
            if (key.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public RegionEntity getRegionEntity(final String siteKey) throws Exception {
        return this.regionEntities.get(siteKey);
    }

    public String[] getReqParameters(final String siteKey, final String serviceId) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        if (null == serviceEntity) {
            throw new CommonException(ErrTypeConstants.SERVICE_NO_AVAILABLE, "Site is not support, No such Site: " + siteKey
                + ", Founction is not support, No such founction: " + serviceId);
        }
        return serviceEntity.getReqParameters();
    }

    public String[] getReqListParameters(final String siteKey, final String serviceId) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        return serviceEntity.getReqListParameters();
    }

    public List<Validator> getValidators(final String siteKey, final String serviceId) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        return serviceEntity.getValidators();
    }

    public String getServiceName(final String siteKey, final String serviceId) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        return serviceEntity.getServiceName();
    }

    public String getRequestClassName(final String siteKey, final String serviceId) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        return serviceEntity.getRequestClassName();
    }

    public Object adaptService(final String siteKey, final String serviceId, final JSONObject json) throws Exception {
        ServiceEntity serviceEntity = getServiceEntity(siteKey, serviceId);
        Service srv = this.servicesHolder.get(serviceEntity.getServiceName());

        return srv.doService(json);
    }

    /**
     * Gets the service entity.
     * 
     * @param siteKey
     *            the site key
     * @param serviceId
     *            the service id
     * @return the service entity
     * @throws Exception
     *             the exception
     */
    protected ServiceEntity getServiceEntity(final String siteKey, final String serviceId) throws Exception {
        RegionEntity regionEntity = this.regionEntities.get(siteKey);
        if (regionEntity == null) {
            throw new CommonException(ErrTypeConstants.SITE_NOT_SUPPORT, "Site is not support, No such Site: " + siteKey);
        }
        ServiceEntity serviceEntity = regionEntity.getServiceEntities().get(serviceId);
        if (serviceEntity == null) {
            throw new CommonException(ErrTypeConstants.SERVICE_NO_AVAILABLE, "Service is not support, No such Service: "
                + serviceId);
        }
        return serviceEntity;
    }

    /**
     * Gets the services.
     * 
     * @return the services
     */
    public Map<String, Service> getServices() {
        return this.servicesHolder;
    }

    /**
     * Sets the services.
     * 
     * @param services
     *            the services
     */
    public void setServices(final Map<String, Service> servicesHolder) {
        this.servicesHolder = servicesHolder;
    }

    /**
     * Gets the region entities.
     * 
     * @return the region entities
     */
    public Map<String, RegionEntity> getRegionEntities() {
        return this.regionEntities;
    }

    /**
     * Sets the region entities.
     * 
     * @param regionEntities
     *            the region entities
     */
    public void setRegionEntities(final Map<String, RegionEntity> regionEntities) {
        this.regionEntities = regionEntities;
    }

    /**
     * Gets the support sites.
     * 
     * @return the support sites
     */
    public String[] getSupportSites() {
        return this.supportSites;
    }

    /**
     * Sets the support sites.
     * 
     * @param supportSites
     *            the new support sites
     */
    public void setSupportSites(final String[] supportSites) {
        this.supportSites = supportSites;
    }

    /**
     * Gets the custom validators.
     * 
     * @return the custom validators
     */
    public String getCustomValidators() {
        return this.customValidators;
    }

    /**
     * Sets the custom validators.
     * 
     * @param customValidators
     *            the new custom validators
     */
    public void setCustomValidators(final String customValidators) {
        this.customValidators = customValidators;
    }

}
