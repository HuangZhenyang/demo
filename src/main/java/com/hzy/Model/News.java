package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/9/7.
 *
 */
@Entity
public class News {
    @Id
    @GeneratedValue
    private Integer id;

    private String title; // 新闻标题
    private Integer img; // 首页显示的图片的链接
    private String newsDetail; // 新闻内容

    //构造器
    public News() {
    }

    public News(String title, Integer img, String newsDetail) {
        this.title = title;
        this.img = img;
        this.newsDetail = newsDetail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img=" + img +
                ", newsDetail='" + newsDetail + '\'' +
                '}';
    }
}
