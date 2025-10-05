/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.dal.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.lucene.search.BooleanQuery;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassCallbackWithoutResult;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassException;
import org.compass.core.CompassHit;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQuery.SortDirection;
import org.compass.core.CompassQuery.SortPropertyType;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassQueryBuilder.CompassQueryStringBuilder;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.engine.SearchEngineQueryParseException;
import org.compass.core.lucene.LuceneEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.dao.PredictiveSearchDao;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;

/**
 * <p>
 * <b> The implement for data access. </b>
 * </p>
 */
@Repository("predictiveSearchDao")
public class PredictiveSearchDaoImpl implements PredictiveSearchDao {

    private Map<String, CompassTemplate> compassMappings;

    @Value("#{systemConfig['app.supportSites']}")
    private String[] supportSites;

    @Value("#{systemConfig['predsrch.indexStorePath']}")
    private String indexStorePath;

    @Value("#{systemConfig['predsrch.maxClausecount']}")
    private int maxClausecount = 2048;

    public final static String ANALYZER_TYPE = "compass.engine.analyzer.default.type";

    @Value("#{systemConfig['predsrch.analyzer']}")
    private String analyzer;

    @Value("#{systemConfig['predsrch.delaySaveIndex']}")
    private long delaySaveIndex = 0;

    @Value("#{systemConfig['predsrch.concurrencyLevel']}")
    private String concurrencyLevel = "5";

    @Value("#{systemConfig['predsrch.addTimeout']}")
    private String addTimeout = "10000";

    @Value("#{systemConfig['predsrch.backlog']}")
    private String backlog = "100";

    /**
     *
     * <p>
     * <b> Init compass by site. </b>
     * </p>
     *
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        try {
            this.compassMappings = new ConcurrentHashMap<String, CompassTemplate>();
            for (int i = 0; i < this.supportSites.length; i++) {
                this.compassMappings.put(this.supportSites[i], createCompassTemplate(this.supportSites[i]));
            }
            BooleanQuery.setMaxClauseCount(this.maxClausecount);
        } catch (Exception e) {
            LogUtil.error(PredictiveSearchDaoImpl.class, "Can't init PredictiveSearchDaoImpl|exception=" + e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    private CompassTemplate createCompassTemplate(final String site) {
        CompassConfiguration conf = new CompassConfiguration().setSetting(CompassEnvironment.CONNECTION, this.indexStorePath + site)
            .addClass(SearchableObject.class).addClass(ProdAltNumSeg.class);
        conf.setSetting(PredictiveSearchDaoImpl.ANALYZER_TYPE, this.analyzer);
        // Add transaction processor settings
        conf.setSetting(LuceneEnvironment.Transaction.Processor.TYPE, LuceneEnvironment.Transaction.Processor.Lucene.NAME)
            .setSetting(LuceneEnvironment.Transaction.Processor.Lucene.CONCURRENCY_LEVEL, this.concurrencyLevel)
            .setSetting(LuceneEnvironment.Transaction.Processor.Lucene.ADD_TIMEOUT, this.addTimeout)
            .setSetting(LuceneEnvironment.Transaction.Processor.Lucene.BACKLOG, this.backlog);
        return new CompassTemplate(conf.buildCompass());
    }

    private void closeTemplate(CompassTemplate current) {
        Compass currCompass = current.getCompass();
        try {
            currCompass.getSearchEngineIndexManager().deleteIndex();
            currCompass.getSearchEngineIndexManager().releaseLocks();
            currCompass.getSearchEngineIndexManager().close();
            currCompass.close();
            currCompass = null;
            current = null;
        } catch (Exception e) {
            LogUtil.error(PredictiveSearchDaoImpl.class, "SystemException: PredictiveSearchDao, closeTemplate, error", e);
            throw new SystemException(e);
        } finally {
            if ((currCompass != null) && (!currCompass.isClosed())) {
                currCompass.close();
            }
        }
    }

    @Override
    public List<SearchableObject> findProductListByKey(final String searchString, final String[] sortingFields, final String site,
        final int topNum) throws Exception {
        LogUtil.debug(PredictiveSearchDaoImpl.class, "findProductListByKey start = {}", System.currentTimeMillis());
        return this.compassMappings.get(site).execute(new CompassCallback<List<SearchableObject>>() {
            public List<SearchableObject> doInCompass(final CompassSession session) throws CompassException {
                CompassQueryBuilder qb = session.queryBuilder();
                CompassQueryStringBuilder cqsb = qb.queryString(searchString);
                CompassQuery cq;
                try {
                    cq = cqsb.toQuery();
                } catch (Exception e) {
                    if (e instanceof SearchEngineQueryParseException) {
                        LogUtil.error(PredictiveSearchDaoImpl.class,
                            "SystemException: PredictiveSearchDao, findProductListByKey, Input with an invalid string error", e);
                        throw new CommonException(ErrTypeConstants.XSS_SECURITY_ERR, "Input with an invalid string error.");
                    } else {
                        throw e;
                    }
                }
                if (null != sortingFields && sortingFields.length > 0) {
                    for (String s : sortingFields) {
                        cq.addSort(s, SortPropertyType.AUTO, SortDirection.AUTO);
                    }
                }

                int max = topNum;
                CompassHits compassHits = cq.hits();
                LogUtil.debug(PredictiveSearchDaoImpl.class, "The compassHits size is: " + compassHits.getLength());
                CompassDetachedHits hits = compassHits.detach(0, max);
                LogUtil.debug(PredictiveSearchDaoImpl.class, "Search record number: {}", hits.length());

                List<SearchableObject> resultList = new ArrayList<SearchableObject>();
                Iterator<CompassHit> iterator = hits.iterator();
                while (iterator.hasNext()) {
                    CompassHit ch = iterator.next();
                    SearchableObject di = (SearchableObject) ch.data();
                    resultList.add(di);
                }
                LogUtil.debug(PredictiveSearchDaoImpl.class, "findProductListByKey end= {}", System.currentTimeMillis());
                return resultList;
            }
        });
    }

    @Override
    public void refreshIndex(final String siteKey, final List<SearchableObject> data) throws Exception {
        LogUtil.debug(PredictiveSearchDaoImpl.class, "refreshIndex start");
        CompassTemplate temp = createCompassTemplate(siteKey);
        save(temp, data);
        CompassTemplate current = this.compassMappings.get(siteKey);
        this.compassMappings.put(siteKey, temp);
        closeTemplate(current);
        LogUtil.debug(PredictiveSearchDaoImpl.class, "refreshIndex end");
    }

    @Override
    public void createIndex(final String siteKey, final List<SearchableObject> data) throws Exception {
        LogUtil.debug(PredictiveSearchDaoImpl.class, "createIndex start");
        this.compassMappings.get(siteKey).getCompass().getSearchEngineIndexManager().deleteIndex();
        this.compassMappings.get(siteKey).getCompass().getSearchEngineIndexManager().createIndex();
        save(this.compassMappings.get(siteKey), data);
        LogUtil.debug(PredictiveSearchDaoImpl.class, "createIndex end");
    }

    @Override
    public void save(final CompassTemplate template, final List<SearchableObject> data) throws Exception {
        template.execute(new CompassCallbackWithoutResult() {
            protected void doInCompassWithoutResult(final CompassSession session) throws CompassException {
                if (null != data) {
                    int size = data.size();
                    for (int i = 0; i < size; i++) {
                        try {
                            Thread.sleep(PredictiveSearchDaoImpl.this.delaySaveIndex);
                        } catch (InterruptedException e) {
                            LogUtil.error(PredictiveSearchDaoImpl.class,
                                "SystemException: PredictiveSearchDao, save, error, delaySaveIndex: "
                                    + PredictiveSearchDaoImpl.this.delaySaveIndex,
                                e);
                            throw new SystemException(e);
                        }
                        session.save(data.get(i));
                    }
                }
            }
        });
    }
}