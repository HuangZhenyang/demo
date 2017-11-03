package com.hzy.Service;

import com.hzy.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by huangzhenyang on 2017/11/2.
 * Token Service
 */

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public void deleteByUserIdAndTokenStr(Integer userId, String tokenStr){
        tokenRepository.deleteByUserIdAndTokenStr(userId, tokenStr);
    }

    @Transactional
    public void updateTokenStr(Integer tokenId, String tokenStr){
        tokenRepository.updateTokenStr(tokenId, tokenStr);
    }
}
