package com.hzy.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by huangzhenyang on 2017/9/7.
 * 用户与捐赠的项目之间的关系
 */
@Entity
@Table(name = "user_project")
@IdClass(UserProjectMultiKeysClass.class)
public class UserProject  implements Serializable {
    private double donateMoney; // 捐赠的钱
    private Integer userId; // 用户的id
    private Integer projectId;  // 捐赠的项目id
    private String timestamp; // 时间戳

    @Id
    public Integer getUserId(){
        return this.userId;
    }

    @Id
    public Integer getProjectId(){
        return this.projectId;
    }

    @Id
    public String getTimestamp(){
        return this.timestamp;
    }

    //getter and setter
    public double getDonateMoney() {
        return donateMoney;
    }

    public void setDonateMoney(double donateMoney) {
        this.donateMoney = donateMoney;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserProject{" +
                "donateMoney=" + donateMoney +
                ", userId=" + userId +
                ", projectId=" + projectId +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
