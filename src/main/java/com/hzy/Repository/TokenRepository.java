package com.hzy.Repository;

import com.hzy.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by huangzhenyang on 2017/11/2.
 * Token的Repository
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{

    // 删除token
    @Modifying
    @Query("delete from Token t where t.userId = ?1 and t.tokenStr= ?2")
    void deleteByUserIdAndTokenStr(Integer userId, String tokenStr);

    // 找到token
    Token findFirstById(Integer userId);


    //修改密码
    @Modifying
    @Query("update Token t set t.tokenStr=?2 where t.id=?1")
    void updateTokenStr(Integer tokenId, String tokenStr);
}
