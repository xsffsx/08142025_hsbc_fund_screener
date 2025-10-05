/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.configuration;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.analysis.common.CommonAnalysisPlugin;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.logging.LogConfigurator;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugin.analysis.pinyin.AnalysisPinyinPlugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import com.hhhh.group.secwealth.mktdata.elastic.properties.ElasticDirProperties;

/**
 * <p>
 * <b> ElasticSearchConfig
 * </p>
 */
@Configuration
public class ElasticSearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class.getName());

    @Value("${spring.data.elasticsearch.cluster-name:WMD-ELASTICSEARCH}")
    private String clusterName;

    private String dataDir;

    private String esHomeDir;

    private String logDir;

    private int port;

    @Value("${elastic.address}")
    private String publishAddr;

    private String httpEnabled;

    @Autowired
    private ElasticDirProperties elasticDirProperties;

    private static final String WINDOWS_PLATFORM = "windows";

    private static final String LINUX_PLATFORM = "linux";

    @PostConstruct
    public void init() {
        if (SystemUtils.IS_OS_WINDOWS) {
            this.dataDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.WINDOWS_PLATFORM).get("dataDir");
            this.esHomeDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.WINDOWS_PLATFORM).get("esHomeDir");
            this.logDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.WINDOWS_PLATFORM).get("logDir");
            this.port =
                Integer.parseInt(this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.WINDOWS_PLATFORM).get("port"));
            this.httpEnabled =
                this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.WINDOWS_PLATFORM).get("httpEnabled");
        } else {
            this.dataDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.LINUX_PLATFORM).get("dataDir");
            this.esHomeDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.LINUX_PLATFORM).get("esHomeDir");
            this.logDir = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.LINUX_PLATFORM).get("logDir");
            this.port =
                Integer.parseInt(this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.LINUX_PLATFORM).get("port"));
            this.httpEnabled = this.elasticDirProperties.getDirConfig().get(ElasticSearchConfig.LINUX_PLATFORM).get("httpEnabled");
        }

        try {
            (new File(this.dataDir)).mkdirs();
            (new File(this.esHomeDir)).mkdirs();
            (new File(this.logDir)).mkdirs();
        } catch (Exception e) {
            ElasticSearchConfig.logger.error("Exception occurred during making directories");
        }
    }

    @SuppressWarnings("resource")
    @Bean
    public Client elasticsearchClient() throws NodeValidationException {
        Settings.Builder settings = Settings.builder();
        try {
            settings.loadFromStream("elasticsearch.yml",
                getClass().getClassLoader().getResourceAsStream("config/elasticsearch.yml"), false);
        } catch (IOException e1) {
            ElasticSearchConfig.logger.error(e1.getMessage());
        }

        // settings.put("node.local", true); // run as local node
        settings.put("cluster.name", this.clusterName);
        if (this.httpEnabled.equals("true")) {
            settings.put("http.port", this.port);
        } else {
            settings.put("http.enabled", false);// disable HTTP API
        }
        settings.put("node.name", "nodeTest");
        settings.put("path.data", this.dataDir);
        settings.put("path.home", this.esHomeDir);
        settings.put("path.logs", this.logDir);
        if (ElasticSearchConfig.logger.isDebugEnabled()) {
            ElasticSearchConfig.logger.debug(ReflectionToStringBuilder.toString(settings));
        }
        Node node = new LocalNode(settings.build()).start();

        try {
            ElasticSearchConfig.logger.info("Node info: {}", node.client().admin().cluster().nodesInfo(new NodesInfoRequest()).get());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            ElasticSearchConfig.logger.error("Error in getting node status", e);
        }

        return node.client();
    }

    private static class LocalNode extends Node {
        LocalNode(final Settings settings) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, Collections.emptyMap(), null,null),
                asList(Netty4Plugin.class, AnalysisPinyinPlugin.class, CommonAnalysisPlugin.class), true);
        }
        protected void registerDerivedNodeNameWithLogger(String nodeName) {
            try {
                LogConfigurator.setNodeName(nodeName);
            } catch (Exception t) {
                ElasticSearchConfig.logger.warn("Error in LocalNode.registerDerivedNodeNameWithLogger()", t);
            }
        }
    }

    /**
     *
     * <p>
     * <b> Springdata elasticsearch sample way of creating the node client, but it
     * cannot be setup with plugin </b>
     * </p>
     *
     * @return
     */
    // @Deprecated
    // @Bean
    public NodeClientFactoryBean client() {
        NodeClientFactoryBean bean = new NodeClientFactoryBean(true);
        bean.setClusterName("WMD-ELASTICSEARCH");
        bean.setEnableHttp(false);
        bean.setPathData(this.dataDir);
        bean.setPathHome(this.esHomeDir);
        bean.setLocal(true);
        bean.setPathConfiguration("config/elasticsearch.yml");
        return bean;
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate(final Client client) {
        return new ElasticsearchRestTemplate(new RestHighLevelClient(RestClient.builder(HttpHost.create(publishAddr))));
    }

}
