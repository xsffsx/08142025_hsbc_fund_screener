
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.fundlist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"indexId", "indexName", "weighting"})
@XmlRootElement(name = "PrimaryProspectusBenchmark")
public class PrimaryProspectusBenchmark {

    @XmlElement(name = "IndexId", required = true)
    protected String indexId;
    @XmlElement(name = "IndexName", required = true)
    protected String indexName;
    @XmlElement(name = "Weighting", required = true)
    protected String weighting;


    public String getIndexId() {
        return this.indexId;
    }


    public void setIndexId(final String indexId) {
        this.indexId = indexId;
    }


    public String getIndexName() {
        return this.indexName;
    }


    public void setIndexName(final String indexName) {
        this.indexName = indexName;
    }


    public String getWeighting() {
        return this.weighting;
    }


    public void setWeighting(final String weighting) {
        this.weighting = weighting;
    }
}
