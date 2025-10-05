package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.hhhh.group.secwealth.mktdata.elastic.bean.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.dao.GRCompanyRepository;
import com.hhhh.group.secwealth.mktdata.elastic.dao.entiry.GRCompanyPo;
import com.hhhh.group.secwealth.mktdata.elastic.dao.spec.CompanySpecification;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StockInstmProductProcessor implements ProductProcessor {

    @Value("${predsrch.skipErrRecord}")
    private boolean skipErrRecord = false;

    @Autowired
    private GRCompanyRepository grCompanyRepository;

    @Autowired
    private CompanySpecification companySpecification;

    @Autowired
    private DataHandlerCommonService dataHandlerCommonService;

    private static final String PRODUCT_TYPE_SEC_HK = "SEC-HK";

    private static final String PRODUCT_TYPE_SEC_US = "SEC-US";

    @Override
    public void process(XMLStreamReader xmlr, DataSiteEntity entity, String siteKey, Version usedVersion, DataSiteEntity.DataFileInfo info) throws XMLStreamException {
        log.info("In processStockinstm: {}", info.getDataFile().getName());
        siteKey = siteKey.toLowerCase();
        //whether encounter exception when parse xml files
        boolean parseStockExceptionFlag = false;
        List<CustomizedEsIndexForProduct> objList = entity.getObjList();
        List<CustomizedEsIndexForProduct> stockList = new ArrayList<>();
        // house view
        List<GRCompanyPo> houseViewList = this.getHouseViewList(info);
        List<GRCompanyPo> recentUpdateList = this.getRecentlyUpdateList(info);

        int totalNum = 0;
        try {
            Unmarshaller unmarshaller;
            JAXBContext jc = JAXBContext.newInstance(StockInstm.class);
            unmarshaller = jc.createUnmarshaller();
            JAXBElement<StockInstm> je;
            int symbolErr = 0;
            int codeErr = 0;
            xmlr.nextTag();
            xmlr.require(XMLStreamConstants.START_ELEMENT, null, "stockInstmLst");
            xmlr.nextTag();
            while (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                totalNum++;
                boolean[] errors = {false, false};
                CustomizedEsIndexForProduct customizedEsIndexForUtb = new CustomizedEsIndexForProduct();
                je = unmarshaller.unmarshal(xmlr, StockInstm.class);
                StockInstm stockInstm = je.getValue();
                this.printLogForStockInstm(stockInstm);
                if (stockInstm != null) {
                    String symbol = this.getProductSymbol(stockInstm);
                    boolean eligible = this.checkValidProduct(stockInstm, symbol);
                    if(eligible) {
                        ProdKeySeg prodKeySeg = stockInstm.getProdKeySeg();
                        List<ProdAltNumSeg> prodAltNumSeg = stockInstm.getProdAltNumSeg();
                        ProdInfoSeg prodInfoSeg = stockInstm.getProdInfoSeg();
                        customizedEsIndexForUtb = this.dataHandlerCommonService.handleCommonData(prodKeySeg, prodAltNumSeg, prodInfoSeg, errors);
                        this.updateHouseViewInfo(info, houseViewList, recentUpdateList, customizedEsIndexForUtb, symbol);
                        customizedEsIndexForUtb.setSequence(usedVersion.genVersion());
                        if (errors[0]) {
                            symbolErr++;
                        } else if (errors[1]) {
                            codeErr++;
                        }
                        updateRequiredStockPropToIndexObj(stockList, errors, customizedEsIndexForUtb, stockInstm);
                    }
                }
                if (xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
                    xmlr.next();
                }
            }
            this.dataHandlerCommonService.handleCommonLog(totalNum, symbolErr, codeErr, entity, "processStockinstm " + siteKey + usedVersion);
        } catch (Exception e) {
            log.error("processStockinstm: {} encountered some error: {}", info.getDataFile().getName(), e.getMessage());
            parseStockExceptionFlag = true;
            stockList.clear();
        } finally {
            this.dataHandlerCommonService.updateObjListAndFileInfo(info, totalNum, parseStockExceptionFlag, objList, stockList);
            xmlr.close();
        }
    }

    protected List<GRCompanyPo> getRecentlyUpdateList(DataSiteEntity.DataFileInfo info) {
        List<GRCompanyPo> recentUpdateList = new ArrayList<>();
        if (PRODUCT_TYPE_SEC_HK.equalsIgnoreCase(info.getProductType())){
            recentUpdateList = this.grCompanyRepository.findAll(this.companySpecification.getRecentUpdateSpec("HK"));
            log.info("House view HK market Recently updated size: {}", recentUpdateList.size());
        } else if (PRODUCT_TYPE_SEC_US.equalsIgnoreCase(info.getProductType())) {
            recentUpdateList = this.grCompanyRepository.findAll(this.companySpecification.getRecentUpdateSpec("US"));
            log.info("House view US market Recently updated size: {}", recentUpdateList.size());
        }
        return recentUpdateList;
    }

    protected List<GRCompanyPo> getHouseViewList(DataSiteEntity.DataFileInfo info) {
        List<GRCompanyPo> houseViewList = new ArrayList<>();
        if (PRODUCT_TYPE_SEC_HK.equalsIgnoreCase(info.getProductType())){
            houseViewList = this.grCompanyRepository.findAllByMarketEqualsAndExpireIsNull("HK");
            log.info("House view of HK market size: {}", houseViewList.size());
        } else if (PRODUCT_TYPE_SEC_US.equalsIgnoreCase(info.getProductType())) {
            houseViewList = this.grCompanyRepository.findAllByMarketEqualsAndExpireIsNull("US");
            log.info("House view of US market size: {}", houseViewList.size());
        }
        return houseViewList;
    }

    private void printLogForStockInstm(StockInstm stockInstm) {
        if (log.isDebugEnabled()) {
            log.debug("stockInstm: {}", new ReflectionToStringBuilder(stockInstm, new RecursiveToStringStyle()));
        }
    }

    private void updateHouseViewInfo(DataSiteEntity.DataFileInfo info, List<GRCompanyPo> houseViewList, List<GRCompanyPo> recentUpdateList, CustomizedEsIndexForProduct customizedEsIndexForUtb, String symbol) {
        if (PRODUCT_TYPE_SEC_HK.equalsIgnoreCase(info.getProductType()) || PRODUCT_TYPE_SEC_US.equalsIgnoreCase(info.getProductType())){
            for (GRCompanyPo com : recentUpdateList){
                if (StringUtil.isValid(symbol) && symbol.equalsIgnoreCase(com.getSymbol())){
                    customizedEsIndexForUtb.setHouseViewRecentUpdate("Y");
                    break;
                }
            }

            for (GRCompanyPo com : houseViewList){
                if (StringUtil.isValid(symbol) && symbol.equalsIgnoreCase(com.getSymbol())){
                    customizedEsIndexForUtb.setHouseViewIndicator("Y");
                    customizedEsIndexForUtb.setHouseViewRating(com.getRating());
                    break;
                }
            }
        }
    }

    private String getProductSymbol(StockInstm stockInstm) {
        String symbol = null;
        if (stockInstm.getProdAltNumSeg() != null && ListUtil.isValid(stockInstm.getProdAltNumSeg())) {
            for (ProdAltNumSeg prodAltNumSeg : stockInstm.getProdAltNumSeg()) {
                if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equalsIgnoreCase(prodAltNumSeg.getProdCdeAltClassCde())) {
                    symbol = prodAltNumSeg.getProdAltNum();
                    break;
                }
            }
        }
        return symbol;
    }

    private boolean checkValidProduct(StockInstm stockInstm, String symbol) {
        String ctryProdTradeCde1 = null;
        if(null != stockInstm.getProdInfoSeg()){
            ctryProdTradeCde1 = stockInstm.getProdInfoSeg().getCtryProdTrade1Cde();
        }
        String prodTypeCde1 = null;
        if(null != stockInstm.getProdKeySeg()){
            prodTypeCde1 = stockInstm.getProdKeySeg().getProdTypeCde();
        }
        boolean hkStockCodeNoChar = true;
        boolean usStockCodeNoNumber = true;
        //when HK SEC/WRTS products, execulde the no-numeric stock code products in response
        if("HK".equals(ctryProdTradeCde1) && ("SEC".equals(prodTypeCde1) || "WRTS".equals(prodTypeCde1))
                && !StringUtil.isNumeric(symbol)){
            hkStockCodeNoChar = false;
        }
        //when US SEC products, execulde the numeric stock code products in response
        if("US".equals(ctryProdTradeCde1) && "SEC".equals(prodTypeCde1)
                && !StringUtil.isUSSymbol(symbol)){
            usStockCodeNoNumber = false;
        }
        return (hkStockCodeNoChar && usStockCodeNoNumber);
    }

    private void updateRequiredStockPropToIndexObj(List<CustomizedEsIndexForProduct> stockList, boolean[] errors, CustomizedEsIndexForProduct customizedEsIndexForUtb, StockInstm stockInstm) {
        if (errors[0] && this.skipErrRecord) {
            log.error("|Skip to save the record without symbol|prodName: {}", customizedEsIndexForUtb.getProductName());
        } else {
            this.updateValueFromStockInstmSeg(customizedEsIndexForUtb, stockInstm);
            this.updateInvstMipCutOffDayofMth(customizedEsIndexForUtb, stockInstm);
            stockList.add(customizedEsIndexForUtb);
        }
    }

    private void updateValueFromStockInstmSeg(CustomizedEsIndexForProduct customizedEsIndexForUtb, StockInstm stockInstm) {
        StockInstmSeg stockInstmSeg = stockInstm.getStockInstmSeg();
        if (null != stockInstmSeg) {
            if (null != stockInstmSeg.getMktProdTrdCde()) {
                customizedEsIndexForUtb.setExchange(stockInstmSeg.getMktProdTrdCde());
            }

            if (null != stockInstmSeg.getInvstMipMinAmt()) {
                customizedEsIndexForUtb.setInvstMipMinAmt(stockInstmSeg.getInvstMipMinAmt());
            }

            if (null != stockInstmSeg.getInvstMipIncrmMinAmt()) {
                customizedEsIndexForUtb.setInvstMipIncrmMinAmt(stockInstmSeg.getInvstMipIncrmMinAmt());
            }

            if (null != stockInstmSeg.getVaEtfIndicator()) {
                customizedEsIndexForUtb.setVaEtfIndicator(stockInstmSeg.getVaEtfIndicator());
            }
        }
    }

    private void updateInvstMipCutOffDayofMth(CustomizedEsIndexForProduct customizedEsIndexForUtb, StockInstm stockInstm) {
        ProdInfoSeg prodInfoSeg = stockInstm.getProdInfoSeg();
        if (prodInfoSeg != null) {
            List<ProdUserDefExtSeg> prodUserDefExtSegs = prodInfoSeg.getProdUserDefExtSeg();
            if (ListUtil.isValid(prodUserDefExtSegs)) {
                for (ProdUserDefExtSeg prodUserDefExtSeg : prodUserDefExtSegs) {
                    String fieldCde = prodUserDefExtSeg.getFieldCde();
                    String fieldValue = prodUserDefExtSeg.getFieldValue();
                    if("invstMipCutOffDayofMth".equals(fieldCde)){
                        customizedEsIndexForUtb.setInvstMipCutOffDayofMth(fieldValue);
                    }
                }
            }
        }
    }

}
