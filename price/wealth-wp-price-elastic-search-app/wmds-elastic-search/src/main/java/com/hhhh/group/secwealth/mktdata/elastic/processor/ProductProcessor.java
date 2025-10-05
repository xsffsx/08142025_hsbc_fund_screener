package com.hhhh.group.secwealth.mktdata.elastic.processor;

import com.hhhh.group.secwealth.mktdata.elastic.bean.DataSiteEntity;
import com.hhhh.group.secwealth.mktdata.elastic.bean.Version;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public interface ProductProcessor {
    void process(XMLStreamReader xmlr, DataSiteEntity entity, String siteKey,
                 Version usedVersion, DataSiteEntity.DataFileInfo info) throws XMLStreamException;
}
