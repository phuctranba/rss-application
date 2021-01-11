package com.e.rssapplication.DataBase;

public class RssLink {
    private EnumTypeNews enumTypeNews;
    private EnumWebSite webSite;
    private String link, id;

    public RssLink() {
    }

    public RssLink(String id, EnumTypeNews enumTypeNews, EnumWebSite webSite, String link) {
        this.enumTypeNews = enumTypeNews;
        this.webSite = webSite;
        this.link = link;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnumTypeNews getEnumTypeNews() {
        return enumTypeNews;
    }

    public void setEnumTypeNews(EnumTypeNews enumTypeNews) {
        this.enumTypeNews = enumTypeNews;
    }

    public EnumWebSite getWebSite() {
        return webSite;
    }

    public void setWebSite(EnumWebSite webSite) {
        this.webSite = webSite;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
