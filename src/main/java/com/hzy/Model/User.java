package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/8/4.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private String userName; //用户名
    private String password; //密码
    private String email; //邮箱
    private String address; //地址
    private String gender; //性别
    private double balance; //余额

    public User() {
    }

    public User(String userName, String password, String email,String address,String gender,double balance) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.balance = balance;
    }

    // Getter
    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public double getBalance(){
        return balance;
    }

    // Setter
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }
}
