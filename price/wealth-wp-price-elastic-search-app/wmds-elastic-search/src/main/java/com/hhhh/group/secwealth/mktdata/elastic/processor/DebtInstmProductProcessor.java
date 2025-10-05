package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.DebtInstm;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.service.DataHandlerCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

@Component
@Slf4j
public class DebtInstmProductProcessor implements ProductProcessor {

    @Autowired
    private DataHandlerCommonService dataHandlerCommonService;

    @Override
    public void process(XMLStreamReader xmlr, DataSiteEntity entity, String siteKey, Version usedVersion, DataSiteEntity.DataFileInfo info) throws XMLStreamException {
        log.info("In processDebtinstm: {}", info.getDataFile().getName());
        siteKey = siteKey.toLowerCase();
        try {
            Unmarshaller unmarshaller;
            JAXBContext jc = JAXBContext.newInstance(DebtInstm.class);
            unmarshaller = jc.createUnmarshaller();
            JAXBElement<DebtInstm> je;
            List<CustomizedEsIndexForProduct> objList = entity.getObjList();
            int symbolErr = 0;
            int codeErr = 0;
            xmlr.nextTag();
            xmlr.require(XMLStreamConstants.START_ELEMENT, null, "debtInstmLst");
            xmlr.nextTag();
            int totalNUm = 0;
            while (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                totalNUm++;
                boolean[] errors = {false, false};
                CustomizedEsIndexForProduct customizedEsIndexForUtb;
                je = unmarshaller.unmarshal(xmlr, DebtInstm.class);
                DebtInstm debtInstm = je.getValue();
                if (log.isDebugEnabled()) {
                    log.debug("debtInstm: {}", new ReflectionToStringBuilder(debtInstm, new RecursiveToStringStyle()));
                }
                if (debtInstm != null) {
                    customizedEsIndexForUtb = this.dataHandlerCommonService.handleCommonData(debtInstm.getProdKeySeg(), debtInstm.getProdAltNumSeg(), debtInstm.getProdInfoSeg(), errors);
                    // add version sequence
                    customizedEsIndexForUtb.setSequence(usedVersion.genVersion());
                    if (errors[0]) {
                        symbolErr++;
                    } else if (errors[1]) {
                        codeErr++;
                    }
                    this.dataHandlerCommonService.addToListIfValid(objList, errors, customizedEsIndexForUtb);
                }
                if (xmlr.getEventType() == XMLStreamConstants.CHARACTERS) {
                    xmlr.next();
                }
            }
            this.dataHandlerCommonService.handleCommonLog(totalNUm, symbolErr, codeErr, entity, "processGnrcprod " + siteKey + usedVersion);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            xmlr.close();
        }
    }
}
