package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/11/12.
 * 用户与关注项目的关系表
 */
@Entity
public class UserStar {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId; // 用户id
    private Integer projectId; // 项目id
    private String timestamp; // 收藏的时间戳

    public UserStar() {
    }

    public UserStar(Integer userId, Integer projectId, String timestamp) {
        this.userId = userId;
        this.projectId = projectId;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
