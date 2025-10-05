package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"newsid", "newssource", "newscategory", "newstopic", "newsdate", "newstime", "timezone",
    "newsContent","translated"})
@XmlRootElement(name = "newslist")
public class NewsHeadlineList {
    @XmlElement(required = true)
    private String newsid;

    @XmlElement(required = true)
    private String newssource;

    @XmlElement(required = true)
    private String newscategory;

    @XmlElement(required = true)
    private String newstopic;

    @XmlElement(required = true)
    private String newsdate;

    @XmlElement(required = true)
    private String newstime;

    @XmlElement(required = true)
    private String timezone;

    private String newsContent;

    protected String translated;

//    protected String newslang;
//
//    public String getNewslang() {
//        return newslang;
//    }
//
//    public void setNewslang(String newslang) {
//        this.newslang = newslang;
//    }

    public String getNewsid() {
        return this.newsid;
    }

    public void setNewsid(final String newsid) {
        this.newsid = newsid;
    }

    public String getNewssource() {
        return this.newssource;
    }

    public void setNewssource(final String newssource) {
        this.newssource = newssource;
    }

    public String getNewscategory() {
        return this.newscategory;
    }

    public void setNewscategory(final String newscategory) {
        this.newscategory = newscategory;
    }

    public String getNewstopic() {
        return this.newstopic;
    }

    public void setNewstopic(final String newstopic) {
        this.newstopic = newstopic;
    }

    public String getNewsdate() {
        return this.newsdate;
    }

    public void setNewsdate(final String newsdate) {
        this.newsdate = newsdate;
    }

    public String getNewstime() {
        return this.newstime;
    }

    public void setNewstime(final String newstime) {
        this.newstime = newstime;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the newsContent
     */
    public String getNewsContent() {
        return this.newsContent;
    }

    /**
     * @param newsContent
     *            the newsContent to set
     */
    public void setNewsContent(final String newsContent) {
        this.newsContent = newsContent;
    }

}
