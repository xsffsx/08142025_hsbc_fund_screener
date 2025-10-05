package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.hhhh.group.secwealth.mktdata.elastic.bean.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class UtTrstInstmProductProcessor implements ProductProcessor {

    @Value("${predsrch.skipErrRecord}")
    private boolean skipErrRecord = false;

    @Autowired
    private DataHandlerCommonService dataHandlerCommonService;

    public void process(final XMLStreamReader xmlr, final DataSiteEntity entity, String siteKey,
                                    final Version usedVersion, final DataSiteEntity.DataFileInfo info) throws XMLStreamException {
        log.info("In processUtTrstInstm: {}", info.getDataFile().getName());
        siteKey = siteKey.toLowerCase();
        int totalNUm = 0;
        boolean parseUtExceptionFlag = false;
        List<CustomizedEsIndexForProduct> objList = entity.getObjList();
        List<CustomizedEsIndexForProduct> utList = new ArrayList<>();
        try {
            Unmarshaller unmarshaller;
            JAXBContext jc = JAXBContext.newInstance(UtTrstInstm.class);
            unmarshaller = jc.createUnmarshaller();
            JAXBElement<UtTrstInstm> je;
            int symbolErr = 0;
            int codeErr = 0;
            xmlr.nextTag();
            xmlr.require(XMLStreamConstants.START_ELEMENT, null, "utTrstInstmLst");
            xmlr.nextTag();
            while (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                totalNUm++;
                // errors[0] check for symbol
                // errors[1] check for T/R code
                boolean[] errors = {false, false};
                CustomizedEsIndexForProduct customizedEsIndexForUtb = new CustomizedEsIndexForProduct();
                je = unmarshaller.unmarshal(xmlr, UtTrstInstm.class);
                UtTrstInstm utTrstInstm = je.getValue();
                if (log.isDebugEnabled()) {
                    log.debug("utTrstInstm: {}", new ReflectionToStringBuilder(utTrstInstm, new RecursiveToStringStyle()));
                }
                // 3. set required properties to es index object
                if (utTrstInstm != null) {
                    customizedEsIndexForUtb = dataHandlerCommonService.handleCommonData(utTrstInstm.getProdKeySeg(), utTrstInstm.getProdAltNumSeg(),
                            utTrstInstm.getProdInfoSeg(), errors);
                    // add version sequence
                    customizedEsIndexForUtb.setSequence(usedVersion.genVersion());
                    if (errors[0]) {
                        symbolErr++;
                    } else if (errors[1]) {
                        codeErr++;
                    }
                    this.setRequiredUtPropToIndexObj(utList, errors, customizedEsIndexForUtb, utTrstInstm);
                }
                if (xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
                    xmlr.next();
                }
            }
            this.dataHandlerCommonService.handleCommonLog(totalNUm, symbolErr, codeErr, entity, "processUtTrstInstm " + siteKey + usedVersion);
        } catch (Exception e) {
            log.error("processUtTrstInstm: {} encountered some error: {}", info.getDataFile().getName(), e.getMessage());
            parseUtExceptionFlag = true;
            utList.clear();
        } finally {
            this.dataHandlerCommonService.updateObjListAndFileInfo(info, totalNUm, parseUtExceptionFlag, objList, utList);
            xmlr.close();
        }
    }

    private void setRequiredUtPropToIndexObj(List<CustomizedEsIndexForProduct> utList, boolean[] errors, CustomizedEsIndexForProduct customizedEsIndexForUtb, UtTrstInstm utTrstInstm) {
        if (errors[0] && !this.skipErrRecord) {
            log.error("|Skip to save the record without symbol|prodName: {}", customizedEsIndexForUtb.getProductName());
        } else {
            ProdInfoSeg prodInfoSeg = utTrstInstm.getProdInfoSeg();
            if (prodInfoSeg != null) {
                // start custom
                this.updateValueByProdUserDefExtSegFieldCode(customizedEsIndexForUtb, prodInfoSeg);
                // set channel Restrict Group
                this.updateResChannelCde(customizedEsIndexForUtb, prodInfoSeg);
                // set chanl Code
                this.updateChanlCode(customizedEsIndexForUtb, prodInfoSeg);
                // set chanl Code allowBuy/allowSell indicate
                this.updateChanlAllowBuyAndSell(customizedEsIndexForUtb, prodInfoSeg);
                // end custom
                // start custom
                this.updateFundUnswithSeg(customizedEsIndexForUtb, utTrstInstm);
                // end custom
            }

            // start custom
            UtTrstInstmSeg utTrstInstmSeg = utTrstInstm.getUtTrstInstmSeg();
            if (utTrstInstmSeg != null) {// UtDataConverter 79
                customizedEsIndexForUtb.setProdShoreLocCde(utTrstInstmSeg.getProdShoreLocCde());
                customizedEsIndexForUtb.setFundHouseCde(utTrstInstmSeg.getFundHouseCde());// setFundHouseCde_analyzed
            }
            /**
             * skipped recDtTmSeg node as not required at this moment;
             */
            utList.add(customizedEsIndexForUtb);
        }
    }

    private void updateValueByProdUserDefExtSegFieldCode(CustomizedEsIndexForProduct customizedEsIndexForUtb, ProdInfoSeg prodInfoSeg) {
        List<String> swithableGroupsAnalyzed = new ArrayList<>();
        String allowTradeProdInd = null;
        String restrOnlScribInd = null;
        String piFundInd = null;
        String deAuthFundInd = null;
        String premRecomInd = null;
        String gbaAcctTrdb = null;
        String gnrAcctTrdb = null;
        String esgInd = null;
        String cmbProductInd = null;
        String wpbProductInd = null;
        String restrCmbOnlSrchInd = null;

        List<ProdUserDefExtSeg> prodUserDefExtSegs = prodInfoSeg.getProdUserDefExtSeg();
        if (ListUtil.isValid(prodUserDefExtSegs)) {
            for (ProdUserDefExtSeg prodUserDefExtSeg : prodUserDefExtSegs) {
                String fieldCde = prodUserDefExtSeg.getFieldCde();
                String fieldValue = prodUserDefExtSeg.getFieldValue();
                if ("switchFundGrpList".equals(fieldCde)) {
                    swithableGroupsAnalyzed.add(fieldValue);// special
                }
                allowTradeProdInd = getFieldValueByCode(allowTradeProdInd, fieldCde, fieldValue, "allowTradeProdInd");
                restrOnlScribInd = getFieldValueByCode(restrOnlScribInd, fieldCde, fieldValue, "restrOnlScribInd");
                piFundInd = getFieldValueByCode(piFundInd, fieldCde, fieldValue, "piFundInd");
                deAuthFundInd = getFieldValueByCode(deAuthFundInd, fieldCde, fieldValue, "deAuthFundInd");
                premRecomInd = getFieldValueByCode(premRecomInd, fieldCde, fieldValue, "premRecomInd");
                gbaAcctTrdb = getFieldValueByCode(gbaAcctTrdb, fieldCde, fieldValue, "gbaAcctTrdb");
                gnrAcctTrdb = getFieldValueByCode(gnrAcctTrdb, fieldCde, fieldValue, "gnrAcctTrdb");
                esgInd = getFieldValueByCode(esgInd, fieldCde, fieldValue, "esgInd");
                cmbProductInd = getFieldValueByCode(cmbProductInd, fieldCde, fieldValue, "cmbProductInd");
                wpbProductInd = getFieldValueByCode(wpbProductInd, fieldCde, fieldValue, "wpbProductInd");
                restrCmbOnlSrchInd = getFieldValueByCode(restrCmbOnlSrchInd, fieldCde, fieldValue, "restrCmbOnlSrchInd");
            }
            if (ListUtil.isValid(swithableGroupsAnalyzed)) {
                customizedEsIndexForUtb.setSwitchableGroups(swithableGroupsAnalyzed);
            }
            if (allowTradeProdInd != null) {
                customizedEsIndexForUtb.setAllowTradeProdInd(allowTradeProdInd);
            }
            customizedEsIndexForUtb.setRestrOnlScribInd(restrOnlScribInd);
            customizedEsIndexForUtb.setPiFundInd(piFundInd);
            customizedEsIndexForUtb.setDeAuthFundInd(deAuthFundInd);
            customizedEsIndexForUtb.setPremRecomInd(premRecomInd);
            customizedEsIndexForUtb.setGbaAcctTrdb(gbaAcctTrdb);
            customizedEsIndexForUtb.setGnrAcctTrdb(gnrAcctTrdb);
            customizedEsIndexForUtb.setEsgInd(esgInd);
            customizedEsIndexForUtb.setCmbProductInd(cmbProductInd);
            customizedEsIndexForUtb.setWpbProductInd(wpbProductInd);
            customizedEsIndexForUtb.setRestrCmbOnlSrchInd(restrCmbOnlSrchInd);
            // to add
        }
    }

    private String getFieldValueByCode(String allowTradeProdInd, String fieldCde, String fieldValue, String field) {
        if (field.equals(fieldCde)) {
            allowTradeProdInd = fieldValue;
        }
        return allowTradeProdInd;
    }

    private void updateResChannelCde(CustomizedEsIndexForProduct customizedEsIndexForUtb, ProdInfoSeg prodInfoSeg) {
        ResChanSeg resChanSeg = prodInfoSeg.getResChanSeg();
        Set<String> resChanSegSet = new HashSet<>();
        if (resChanSeg != null && resChanSeg.getResChannelDtl() != null) {
            for (ResChannelDtl seg : resChanSeg.getResChannelDtl()) {
                if (null != seg) {
                    resChanSegSet.add(seg.getResChannelCde());
                }
            }
        }
        if (!resChanSegSet.isEmpty()) {
            List<String> resChanSegList = new ArrayList<>(resChanSegSet);
            customizedEsIndexForUtb.setResChannelCde(resChanSegList);
        }
    }

    private void updateChanlCode(CustomizedEsIndexForProduct customizedEsIndexForUtb, ProdInfoSeg prodInfoSeg) {
        List<ChanlAttrSeg> chanlAttrSeg = prodInfoSeg.getChanlAttrSeg();
        Set<String> chanlAttrSegSet = new HashSet<>();
        if (chanlAttrSeg != null) {
            for (ChanlAttrSeg seg : prodInfoSeg.getChanlAttrSeg()) {
                if (null != seg) {
                    chanlAttrSegSet.add(seg.getChanlCde());
                }
            }
        }
        if (!chanlAttrSegSet.isEmpty()) {
            List<String> chanlAttrSegList = new ArrayList<>(chanlAttrSegSet);
            customizedEsIndexForUtb.setChanlCde(chanlAttrSegList);
        }
    }

    private void updateChanlAllowBuyAndSell(CustomizedEsIndexForProduct customizedEsIndexForUtb, ProdInfoSeg prodInfoSeg) {
        List<String> chanlBuyList = new ArrayList<>();
        List<String> chanlSellList = new ArrayList<>();
        List<String> chanlSwInList = new ArrayList<>();
        List<String> chanlSwOutList = new ArrayList<>();
        if (prodInfoSeg.getChanlAttrSeg() != null) {
            for (ChanlAttrSeg seg : prodInfoSeg.getChanlAttrSeg()) {
                if (null != seg && StringUtil.isValid(seg.getChanlCde()) && StringUtil.isValid(seg.getFieldValue())) {
                    if (StringUtils.equalsAny(seg.getFieldCde(), "allowBuyProdInd", "ALLOW_BUY_PROD_IND")) {
                        chanlBuyList.add(seg.getChanlCde());
                        chanlBuyList.add(seg.getFieldValue());
                    } else if (StringUtils.equalsAny(seg.getFieldCde(), "allowSellProdInd", "ALLOW_SELL_PROD_IND")) {
                        chanlSellList.add(seg.getChanlCde());
                        chanlSellList.add(seg.getFieldValue());
                    } else if (StringUtils.equalsAny(seg.getFieldCde(), "allowSwInProdInd", "ALLOW_SW_IN_PROD_IND")) {
                        chanlSwInList.add(seg.getChanlCde());
                        chanlSwInList.add(seg.getFieldValue());
                    } else if (StringUtils.equalsAny(seg.getFieldCde(), "allowSwOutProdInd", "ALLOW_SW_OUT_PROD_IND")) {
                        chanlSwOutList.add(seg.getChanlCde());
                        chanlSwOutList.add(seg.getFieldValue());
                    }
                }
            }
        }
        customizedEsIndexForUtb.setChanlAllowBuy(chanlBuyList);
        customizedEsIndexForUtb.setChanlAllowSell(chanlSellList);
        customizedEsIndexForUtb.setChanlAllowSwitchIn(chanlSwInList);
        customizedEsIndexForUtb.setChanlAllowSwitchOut(chanlSwOutList);
    }

    private void updateFundUnswithSeg(CustomizedEsIndexForProduct customizedEsIndexForUtb, UtTrstInstm utTrstInstm) {
        List<String> fundUnswithSegList = new ArrayList<>();
        if (utTrstInstm.getUtTrstInstmSeg() != null
                && utTrstInstm.getUtTrstInstmSeg().getFundUnswithSeg() != null) {
            for (FundUnswithSeg seg : utTrstInstm.getUtTrstInstmSeg().getFundUnswithSeg()) {
                fundUnswithSegList.add(seg.getFundUnswCde());
            }
            customizedEsIndexForUtb.setFundUnswithSeg(fundUnswithSegList);
        }
    }
}
