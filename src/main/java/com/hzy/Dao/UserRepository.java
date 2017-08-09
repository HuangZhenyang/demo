package com.hzy.Dao;

import com.hzy.Model.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/8/4.
 */

public interface UserRepository extends JpaRepository<User,Integer>{
    //public List<User> findByUserName(String userName);
    //根据邮箱返回用户
    @Query("select u from User u where u.email=?1")
    public User findByEmail(String email);
    //根据用户名返回用户
    @Query("select u from User u where u.userName=?1")
    public User findByUserName(String userName);
    //修改密码
    @Modifying
    @Transactional
    @Query("update User u set u.password=?1")
    public Boolean setPassword(String password);

}
