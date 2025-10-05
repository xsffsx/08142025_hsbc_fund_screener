package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ASharesStock;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.ASharesStockInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuote;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("aSharesStockService")
public class ASharesStockService {

    private static final Logger logger = LoggerFactory.getLogger(PredSrchService.class);

    @Autowired
    private EtnetProperties etnetProperties;

    @Autowired
    private HttpClientHelper httpClientHelper;

    protected static Map<String, ASharesStock> aSharesStockMap = new HashMap<>();

    protected static final ThreadLocal<Integer> REQUEST_ETNET_COUNT = new ThreadLocal<>();

    protected static final int REQUEST_ETNET_MAX_TIMES = 3;

    public void initASharesStock() {
        Map<String, ASharesStock> stockMap = getASharesStockFromETNet();
        log.info("A shares stock loaded from ETNet with {} records", stockMap.size());
        synchronized(this){
            aSharesStockMap = stockMap;
        }
    }

    public Map<String, ASharesStock> getASharesStockFromETNet() {
        Map<String, ASharesStock> newASharesStockMap = new HashMap<>();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));

            String res = this.httpClientHelper.doGet(this.etnetProperties.getProxyName(), this.etnetProperties.getAsharesStockUrl(), params, null);
            ASharesStockInfo stockInfo = JsonUtil.fromJson(res, ASharesStockInfo.class);

            List<ASharesStock> aSharesStockList = stockInfo.getASharesStock();
            for (ASharesStock stock : aSharesStockList) {
                newASharesStockMap.put(stock.getASymbol(), stock);
            }
        } catch (Exception e) {
            Integer count = REQUEST_ETNET_COUNT.get();
            if (count == null) {
                count = 0;
            }
            count++;
            if (count >= REQUEST_ETNET_MAX_TIMES) {
                logger.error("Load a shares stock from ETNet with {} times error", count, e);
                REQUEST_ETNET_COUNT.remove();
                return new HashMap<>();
            } else {
                logger.warn("Load a shares stock from ETNet with {} times error", count, e);
                REQUEST_ETNET_COUNT.set(count);
                return getASharesStockFromETNet();
            }
        }
        if (REQUEST_ETNET_COUNT.get() != null) {
            REQUEST_ETNET_COUNT.remove();
        }
        return newASharesStockMap;
    }

    public int reloadASharesStockFromETNet() {
        Map<String, ASharesStock> newASharesStockMap = getASharesStockFromETNet();
        if (!newASharesStockMap.isEmpty()) {
            synchronized(this) {
                aSharesStockMap = newASharesStockMap;
            }
        }
        return newASharesStockMap.size();
    }

    public synchronized void setASharesInfo(QuotesLabciQuote quote) {
        if (aSharesStockMap.isEmpty()) {
            logger.info("The a shares stock cache is empty, will be loaded from ETNet");
            reloadASharesStockFromETNet();
            if (aSharesStockMap.isEmpty()) {
                logger.error("The ETNet is unavailable, mapping info can not provided");
                return;
            }
        }
        String symbol = quote.getSymbol();
        if (aSharesStockMap.containsKey(symbol)) {
            ASharesStock stock = aSharesStockMap.get(symbol);
            quote.setEligibility(stock.getEligibility());
            quote.setStockConnectStatus(stock.getStockConnectStatus());
            if (StringUtils.isNotEmpty(stock.getHSymbol())) {
                quote.setHpgCode(stock.getHSymbol());
            }
        }
    }

}
