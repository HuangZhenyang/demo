package com.hzy.Repository;


import com.hzy.Model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by huangzhenyang on 2017/11/11.
 *
 */
public interface UserMessageRepository extends JpaRepository<UserMessage,Integer> {

}
