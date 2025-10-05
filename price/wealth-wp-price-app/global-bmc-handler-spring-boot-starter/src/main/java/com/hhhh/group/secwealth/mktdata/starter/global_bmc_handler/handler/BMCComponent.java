/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.handler;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.constant.Constant;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity.BMCEntity;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity.BMCEntitys;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity.DurationExCounter;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity.ExceptionCounter;
import com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.util.converter.CastorConverter;

import lombok.Setter;

public class BMCComponent {

    private static final Logger logger = LoggerFactory.getLogger(BMCComponent.class);

    private Map<String, BMCEntity> bmcEntityMap;

    @Setter
    private String path;

    @Setter
    private String mapping;

    @Setter
    private String config;

    @Setter
    private String entityKey;

    @Setter
    private String defaultEntityName;

    @Setter
    private String prefixMessage;

    @Setter
    private String defaultExKey;

    @Setter
    private BMCHelper helper;

    private boolean isPrintFiles;

    private String bmcFilePath;

    @PostConstruct
    public void init() throws Exception {
        final ResourceLoader loader = new DefaultResourceLoader();
        final URL mappingURL = loader.getResource(this.path + this.mapping).getURL();
        final URL configURL = loader.getResource(this.path + this.config).getURL();
        final BMCEntitys bmcEntitys = (BMCEntitys) CastorConverter.convertXMLToBean(mappingURL, configURL, BMCEntitys.class, true);
        bmcEntitys.initBMCEntityMap();
        this.bmcEntityMap = bmcEntitys.getBmcEntityMap();
        this.isPrintFiles = Boolean.valueOf(bmcEntitys.getPrintFiles());
        if (this.isPrintFiles) {
            this.bmcFilePath = this.helper.getBmcFilePath(bmcEntitys.getBmcFilePath());
        }
    }

    public void doBMC(final Throwable e, final String traceCode) throws IOException {
        final String entityName = String.valueOf(ArgsHolder.getArgs(this.entityKey));
        BMCEntity entity;
        if (this.bmcEntityMap.containsKey(entityName)) {
            entity = this.bmcEntityMap.get(entityName);
        } else {
            entity = this.bmcEntityMap.get(this.defaultEntityName);
        }
        String exKey = e.getClass().getName() + e.getMessage();
        ExceptionCounter exCounter = entity.getExceptionCounterMap().get(exKey);
        if (exCounter == null) {
            exKey = e.getMessage();
            exCounter = entity.getExceptionCounterMap().get(exKey);
            if (exCounter == null) {
                exCounter = entity.getExceptionCounterMap().get(this.defaultExKey);
            }
        }
        if (!exCounter.ignoreException()) {
            synchronized (exCounter) {
                final List<DurationExCounter> counterDurationExList = exCounter.getDurationExceptionList();
                this.helper.removeExpiredExceptions(counterDurationExList, exCounter.getTimeDuration());
                this.helper.addCurrentException(counterDurationExList, e);
                if (counterDurationExList.size() >= exCounter.getMaxNum()) {
                    genBMC(exCounter.getBmcExCde(), exCounter.getBmcExMsg(), traceCode);
                    this.helper.clearExceptions(counterDurationExList);
                }
            }

            synchronized (entity) {
                final List<DurationExCounter> entityDurationExList = entity.getDurationExceptionList();
                this.helper.removeExpiredExceptions(entityDurationExList, entity.getExTimeDurationThrownPastSec());
                this.helper.addCurrentException(entityDurationExList, e);
                if (entityDurationExList.size() >= entity.getExNumThrownPastSec()) {
                    genBMC(entity.getExCdeThrownPastSec(), entity.getExMgsThrownPastSec(), traceCode);
                    this.helper.clearExceptions(entityDurationExList);
                }
            }

            if (entity.getTotalExceptionNum().get() >= entity.getExNumExceedLimit()) {
                genBMC(entity.getExCdeExceedLimit(), entity.getExMgsExceedLimit(), traceCode);
                entity.getTotalExceptionNum().getAndSet(0);
            } else {
                entity.getTotalExceptionNum().getAndIncrement();
            }
        }
    }

    private void genBMC(final String bmcExCde, final String bmcExMsg, final String traceCode) throws IOException {
        final String errMsg = this.prefixMessage + Constant.BMC_ERR_MSG_PATTERN.replace(Constant.PALCEHOLDER_BMC_EX_CODE, bmcExCde)
            .replace(Constant.PALCEHOLDER_BMC_EX_MSG, bmcExMsg).replace(Constant.PALCEHOLDER_TRACE_CODE, traceCode);
        BMCComponent.logger.error(errMsg);
        if (this.isPrintFiles) {
            this.helper.genBMCFile(this.bmcFilePath, errMsg);
        }
    }

}
