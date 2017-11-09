package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/9/7.
 * 项目
 */
@Entity
public class Project {
    @Id
    @GeneratedValue
    private Integer id;

    private String projectName; // 项目名称
    private String initiatorName; // 发起方名称
    private Integer img; // 首页的图片链接
    private String description; // 简短的描述
    private double targetMoney; // 目标筹款金额
    private double currentMoney; // 当前已筹款的金额
    private String detail; // 项目详情
    private String imgListStr; // 项目所有图片的id组成的字符串  1_2_3
    private Integer userId; // 发起的用户id

    // 构造器
    public Project() {
    }

    public Project(String projectName, String initiatorName, Integer img, String description, double targetMoney, double currentMoney, String detail, String imgListStr, Integer userId) {
        this.projectName = projectName;
        this.initiatorName = initiatorName;
        this.img = img;
        this.description = description;
        this.targetMoney = targetMoney;
        this.currentMoney = currentMoney;
        this.detail = detail;
        this.imgListStr = imgListStr;
        this.userId = userId;
    }

    //getter and setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(double targetMoney) {
        this.targetMoney = targetMoney;
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        this.currentMoney = currentMoney;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImgListStr() {
        return imgListStr;
    }

    public void setImgListStr(String imgListStr) {
        this.imgListStr = imgListStr;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
