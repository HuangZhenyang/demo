package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/11/3.
 * 用户与习惯数量的表
 */
@Entity
public class UserPlan {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;
    private Integer planNumber; // 用户拥有的plan数量

    public UserPlan() {
    }

    public UserPlan(Integer userId, Integer planNumber) {
        this.userId = userId;
        this.planNumber = planNumber;
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

    public Integer getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(Integer planNumber) {
        this.planNumber = planNumber;
    }

    @Override
    public String toString() {
        return "UserPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", planNumber=" + planNumber +
                '}';
    }
}
