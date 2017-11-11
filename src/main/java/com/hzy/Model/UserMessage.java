package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/11/11.
 *
 */
@Entity
public class UserMessage {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;
    private String message;
    private Integer projectId;
    private String timestamp;

    public UserMessage() {
    }

    public UserMessage(Integer userId, String message, Integer projectId, String timestamp) {
        this.userId = userId;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
