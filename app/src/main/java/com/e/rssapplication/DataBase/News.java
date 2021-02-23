package com.e.rssapplication.DataBase;

import java.io.Serializable;
import java.util.Date;

//Đối tượng lưu tin
public class News implements Serializable {

//    Bao gồm id, tiêu đề, mô tả, đường link đọc, ảnh thumbnail, đường dẫn lưu offline
    private String id, title, description, link, image, path;
//    Ngày đăng
    private Date pubdate;
//    Nguồn web site
    private EnumWebSite webSite;
//    Loại tin
    private EnumTypeNews typeNews;
//    Có được lưu offline chưa
    private boolean saved;

    public EnumTypeNews getTypeNews() {
        return typeNews;
    }

    public void setTypeNews(EnumTypeNews typeNews) {
        this.typeNews = typeNews;
    }

    public EnumWebSite getWebSite() {
        return webSite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
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
