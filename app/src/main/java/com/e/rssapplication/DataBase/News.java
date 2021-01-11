package com.e.rssapplication.DataBase;

import java.util.Date;

public class News {
    private String id, title, description, link, image;
    private Date pubdate;
    private EnumWebSite webSite;
    private EnumTypeNews typeNews;

    public EnumTypeNews getTypeNews() {
        return typeNews;
    }

    public void setTypeNews(EnumTypeNews typeNews) {
        this.typeNews = typeNews;
    }

    public EnumWebSite getWebSite() {
        return webSite;
    }

    public void setWebSite(EnumWebSite webSite) {
        this.webSite = webSite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }
}
