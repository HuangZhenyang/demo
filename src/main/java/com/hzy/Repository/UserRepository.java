package com.hzy.Repository;

import com.hzy.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by huangzhenyang on 2017/8/4.
 */

public interface UserRepository extends JpaRepository<User,Integer>{
    //public List<User> findByUserName(String userName);
    //根据邮箱返回用户
    @Query("select u from User u where u.email=?1")
    User findByEmail(String email);


    //修改密码
    @Modifying
    @Query("update User u set u.password=:password")
    Boolean updatePassword(@Param("password") String password);

    //
}
