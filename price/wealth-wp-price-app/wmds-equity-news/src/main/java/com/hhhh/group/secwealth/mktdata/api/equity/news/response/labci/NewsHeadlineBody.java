package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"stockslist", "totalrecordno", "newslist", "newsid", "newstopic", "newscontent",
    "lastupdateddate", "lastupdatedtime", "timezone", "translated"})
@XmlRootElement(name = "body")
public class NewsHeadlineBody {

    @XmlElement(required = true)
    protected List<StocksList> stockslist;

    @XmlElement(required = true)
    protected String totalrecordno;

    @XmlElement(required = true)
    protected List<NewsHeadlineList> newslist;

    @XmlElement(required = true)
    protected String newsid;

    @XmlElement(required = true)
    protected String newstopic;

    @XmlElement(required = true)
    protected String newscontent;

    @XmlElement(required = true)
    protected String translated;

    @XmlElement(required = true)
    protected String lastupdateddate;

    @XmlElement(required = true)
    protected String lastupdatedtime;

    @XmlElement(required = true)
    protected String timezone;

    public void setStockslist(final List<StocksList> stockslist) {
        this.stockslist = stockslist;
    }

    public List<StocksList> getStockslist() {
        if (this.stockslist == null) {
            this.stockslist = new ArrayList<StocksList>();
        }
        return this.stockslist;
    }

    public String getTotalrecordno() {
        return this.totalrecordno;
    }

    public void setTotalrecordno(final String totalrecordno) {
        this.totalrecordno = totalrecordno;
    }

    public List<NewsHeadlineList> getNewslist() {
        return this.newslist;
    }

    public void setNewslist(final List<NewsHeadlineList> newslist) {
        this.newslist = newslist;
    }

    /**
     * @return the newsid
     */
    public String getNewsid() {
        return this.newsid;
    }

    /**
     * @param newsid
     *            the newsid to set
     */
    public void setNewsid(final String newsid) {
        this.newsid = newsid;
    }

    /**
     * @return the newstopic
     */
    public String getNewstopic() {
        return this.newstopic;
    }

    /**
     * @param newstopic
     *            the newstopic to set
     */
    public void setNewstopic(final String newstopic) {
        this.newstopic = newstopic;
    }

    /**
     * @return the newscontent
     */
    public String getNewscontent() {
        return this.newscontent;
    }

    /**
     * @param newscontent
     *            the newscontent to set
     */
    public void setNewscontent(final String newscontent) {
        this.newscontent = newscontent;
    }

    /**
     * @return the lastupdateddate
     */
    public String getLastupdateddate() {
        return this.lastupdateddate;
    }

    /**
     * @param lastupdateddate
     *            the lastupdateddate to set
     */
    public void setLastupdateddate(final String lastupdateddate) {
        this.lastupdateddate = lastupdateddate;
    }

    /**
     * @return the lastupdatedtime
     */
    public String getLastupdatedtime() {
        return this.lastupdatedtime;
    }

    /**
     * @param lastupdatedtime
     *            the lastupdatedtime to set
     */
    public void setLastupdatedtime(final String lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
        return this.timezone;
    }

    /**
     * @param timezone
     *            the timezone to set
     */
    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

}
