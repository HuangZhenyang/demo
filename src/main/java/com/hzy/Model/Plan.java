package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by huangzhenyang on 2017/10/19.
 * 用户制定的习惯
 */
@Entity
public class Plan {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId; // 用户的id
    private String planName; // 习惯的名字
    private Integer finishedTimes; // 已经打卡的次数     打卡需要改变
    private Integer value; // 爱心值     打卡需要改变
    private String startDate; // 开始的那天的日期  2017-11-2
    private String deadline; // 结束的那天的日期  22017-11-23
    private String lastClock; // 最后一次打卡的日期，格式为2017-10-26       打卡需要改变
    private String stillKeeping; // 习惯是否还在坚持的布尔变量，判断该次签到的日期与最后一次打卡的日期是否差了一天，
                                    // 是的话为true并且finishedTimes+1,否则为false; 前端判断为false以后就设置打卡按钮不可点击
                                    // "over"则表示该习惯已经完成
                                    // 打卡可能需要改变


    public Plan() {
    }

    public Plan(Integer userId, String planName, Integer finishedTimes, Integer value, String startDate, String deadline, String lastClock, String stillKeeping) {
        this.userId = userId;
        this.planName = planName;
        this.finishedTimes = finishedTimes;
        this.value = value;
        this.startDate = startDate;
        this.deadline = deadline;
        this.lastClock = lastClock;
        this.stillKeeping = stillKeeping;
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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getFinishedTimes() {
        return finishedTimes;
    }

    public void setFinishedTimes(Integer finishedTimes) {
        this.finishedTimes = finishedTimes;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getLastClock() {
        return lastClock;
    }

    public void setLastClock(String lastClock) {
        this.lastClock = lastClock;
    }

    public String getStillKeeping() {
        return stillKeeping;
    }

    public void setStillKeeping(String stillKeeping) {
        this.stillKeeping = stillKeeping;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", userId=" + userId +
                ", planName='" + planName + '\'' +
                ", finishedTimes=" + finishedTimes +
                ", value=" + value +
                ", startDate='" + startDate + '\'' +
                ", deadline='" + deadline + '\'' +
                ", lastClock='" + lastClock + '\'' +
                ", stillKeeping='" + stillKeeping + '\'' +
                '}';
    }
}