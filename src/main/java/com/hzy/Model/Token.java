package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/11/2.
 * 存储用户Token
 * 如果用户的token过期，则删除数据库里的token
 */
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId; // 用户的id
    private String tokenStr; // 加密过的存储的tokenStr
    private String status; // token的状态，"alive"表示未过期
                            //  "dead" 表示已经过期

    // 构造器
    public Token() {
    }

    public Token(Integer userId, String tokenStr, String status) {
        this.userId = userId;
        this.tokenStr = tokenStr;
        this.status = status;
    }

    // getter and setter
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

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + tokenStr + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
