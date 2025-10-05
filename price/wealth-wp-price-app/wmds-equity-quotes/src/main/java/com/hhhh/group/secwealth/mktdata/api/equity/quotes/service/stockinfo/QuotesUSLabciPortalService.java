package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo;

import com.google.gson.Gson;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo.StockInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.labciportal.stockinfo.StockInfoRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo.StockInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo.entity.Data;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.LabciPortalTokenService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service("quotesUSLabciPortalService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class QuotesUSLabciPortalService {

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSLabciPortalService.class);

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private LabciPortalTokenService labciPortalTokenService;

    @Autowired
    @Getter
    @Setter
    private LabciPortalProperties labciPortalProperties;

    public StockInfo getStockInfo(String channelId, String marketCode, String tCode, String language) {
        String tokenStr = null;
        try {
            tokenStr = this.labciPortalTokenService.encryptLabciPortalToken(channelId, marketCode);
        } catch (Exception e) {
            logger.error("Failed to generate LabCI Portal token string.", e);
        }
        String proxyName = this.labciPortalProperties.getProxyName();
        String stockInfoUrl = this.labciPortalProperties.getStockInfoUrl();

        StockInfoRequest stockInfoRequest = new StockInfoRequest();
        stockInfoRequest.setToken(tokenStr);
        stockInfoRequest.setNbRic(tCode);
        stockInfoRequest.setLang(language);

        String responseStr = null;
        try {
            responseStr = this.httpClientHelper.doPost(
                    proxyName,
                    stockInfoUrl,
                    new Gson().toJson(stockInfoRequest),
                    null);
        } catch (Exception e) {
            logger.error("Failed to get stock info from LabCi Portal. T code: [" + tCode + "] ", e);
        }

        StockInfoResponse stockInfoResponse = new Gson().fromJson(responseStr, StockInfoResponse.class);
        StockInfo stockInfo = new StockInfo();
        if(stockInfoResponse.getStatusCode().equalsIgnoreCase("000") && stockInfoResponse.getStatusText().equalsIgnoreCase("OK")) {
            Data data = stockInfoResponse.getResult().getData();

            stockInfo.setNbRic(data.getNbRic());
            stockInfo.setSymbol(data.getSymbol());
            stockInfo.setName(data.getName());

            Boolean isADR = null;
            if(null!=data.getIsADR()) {
                isADR = data.getIsADR().equalsIgnoreCase("Y")?Boolean.TRUE:Boolean.FALSE;
            } else {
                logger.warn("The field isADR is NULL, symbol: " + data.getSymbol());
            }
            stockInfo.setIsADR(isADR);

            Boolean isETF = null;
            if(null!= data.getIsETF()) {
                isETF = data.getIsETF().equalsIgnoreCase("Y")?Boolean.TRUE:Boolean.FALSE;
            } else {
                logger.warn("The field isETF is NULL, symbol: " + data.getSymbol());
            }
            stockInfo.setIsETF(isETF);

            stockInfo.setPERatio(BigDecimalUtil.fromStringAndCheckNull(data.getPER()));
            stockInfo.setExpectedPERatio(BigDecimalUtil.fromStringAndCheckNull(data.getEPER()));
            stockInfo.setMarketCap(BigDecimalUtil.fromStringAndCheckNull(data.getMktcap()));
            stockInfo.setDivYield(BigDecimalUtil.fromStringAndCheckNull(data.getDivYield()));
            stockInfo.setExpectedDividendYield(BigDecimalUtil.fromStringAndCheckNull(data.getEDivYield()));
            stockInfo.setCcy(data.getCcy());
            stockInfo.setIndustry(data.getIndustry());
            stockInfo.setExDivDate(data.getExDivDate());
            stockInfo.setLs(BigDecimalUtil.fromStringAndCheckNull(data.getLs()));
            stockInfo.setNc(BigDecimalUtil.fromStringAndCheckNull(data.getNc()));
            stockInfo.setPc(BigDecimalUtil.fromStringAndCheckNull(data.getPc()));
            stockInfo.setPrevClose(BigDecimalUtil.fromStringAndCheckNull(data.getPrevClose()));
            stockInfo.setVolume(BigDecimalUtil.fromStringAndCheckNull(data.getVolume()));
            stockInfo.setTurnover(BigDecimalUtil.fromStringAndCheckNull(data.getTurnover()));
            stockInfo.setAdrRatio(BigDecimalUtil.fromStringAndCheckNull(data.getAdrRatio()));
            stockInfo.setAdrPrice(BigDecimalUtil.fromStringAndCheckNull(data.getAdrPrice()));
            stockInfo.setAdrCcy(data.getAdrCcy());
            stockInfo.setSymbolHK(data.getSymbolHK());
            stockInfo.setNameHK(data.getNameHK());
            stockInfo.setTurnover(BigDecimalUtil.fromStringAndCheckNull(data.getTurnover()));

            //EFT info
            stockInfo.setFundType(data.getFdType());
            stockInfo.setFundTypeSubClass(data.getFdSubType());
            stockInfo.setFundRegion(data.getFdRegion());
            stockInfo.setBeta6M(BigDecimalUtil.fromStringAndCheckNull(data.getBeta6m()));
            stockInfo.setBeta1y(BigDecimalUtil.fromStringAndCheckNull(data.getBeta1y()));
            stockInfo.setBeta3y(BigDecimalUtil.fromStringAndCheckNull(data.getBeta3y()));
            stockInfo.setBeta5y(BigDecimalUtil.fromStringAndCheckNull(data.getBeta5y()));
            stockInfo.setBeta10y(BigDecimalUtil.fromStringAndCheckNull(data.getBeta10y()));
            stockInfo.setAmount(BigDecimalUtil.fromStringAndCheckNull(data.getAum()));
            stockInfo.setAmountCcy(data.getAumCcy());
            stockInfo.setAmountDate(data.getAumDate());
            stockInfo.setExpenseRatio(BigDecimalUtil.fromStringAndCheckNull(data.getExpenseRatio()));
        } else {
            logger.error("Failed to get stock info from LabCI portal: " + responseStr);
        }

        return stockInfo;
    }
}
