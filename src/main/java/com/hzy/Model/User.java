package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/8/4.
 *
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private String name; //用户名
    private String password; //密码
    private String email; //邮箱
    private String region; //地址
    private String gender; //性别
    private double balance; //余额
    private double value; // 爱心值
    private Integer head; // 头像的id


    public User() {
    }

    public User(String name, String password, String email, String region, String gender, double balance, double value, Integer head) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.region = region;
        this.gender = gender;
        this.balance = balance;
        this.value = value;
        this.head = head;
    }

    //getter and setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", region='" + region + '\'' +
                ", gender='" + gender + '\'' +
                ", balance=" + balance +
                '}';
    }
}
