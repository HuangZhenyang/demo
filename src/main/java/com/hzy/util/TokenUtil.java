package com.hzy.util;

import com.hzy.Model.Token;
import com.hzy.Repository.TokenRepository;
import com.hzy.Service.TokenService;
import com.hzy.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by huangzhenyang on 2017/11/2.
 * Token 工具类
 */
@Component
public class TokenUtil {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    private static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    private int dueTime = 86400000;

    public static TokenUtil tokenUtil;

    public TokenUtil() {
    }

    @PostConstruct
    public void init() {
        tokenUtil = this;
        tokenUtil.tokenRepository = this.tokenRepository;
        tokenUtil.tokenService = this.tokenService;
    }



    /**
     * 创建Token
     *
     * @return String  返回加密的Token:时间戳+用户Id
     */
    public String createToken(Integer userId) {
        String tempStr = df.format(new Date()) + "_" + userId;
        return MD5Util.encrypt(tempStr);
    }

    /**
     * 保存Token
     * @param newTokenPara   需要保存的token
     */
    public void saveToken(Token newTokenPara){
        Token newToken = newTokenPara;
        tokenUtil.tokenRepository.save(newToken);
    }


    /**
     * 判断Token是否过期
     * 有效期为一天，相差 86400000
     *
     * @return Boolean  true表示未过期，false表示已过期
     */
    public Boolean checkToken(String tokenStr) {
        tokenStr = MD5Util.encrypt(tokenStr);
        String[] tokenArr = tokenStr.split("_");
        try {
            Date tokenDate = df.parse(tokenArr[0]);
            Date currDate = df.parse(df.format(new Date()));
            System.out.println(df.format(new Date()));
            System.out.println(currDate.getTime() - tokenDate.getTime());
            if (currDate.getTime() - tokenDate.getTime() >= dueTime) { // 过期，删除
                tokenUtil.tokenService.deleteByUserIdAndTokenStr(Integer.parseInt(tokenArr[1]), tokenArr[0]);
                return false;
            }

        } catch (ParseException e) {
            System.out.println("Date parse Error in TokenUtil.java: line 50");
        }

        return true;
    }

    /**
     * 获取当前Token中的User ID
     *
     * @return Integer 返回用户id
     */
    public Integer getUserId(String tokenStr) {
        tokenStr = MD5Util.encrypt(tokenStr);
        String[] tokenArr = tokenStr.split("_");
        return (Integer.parseInt(tokenArr[1]));
    }

    /**
     * 根据用户id从数据库里获取token
     *
     * @param userId 用户id
     * @return Token  返回token对象
     */
    public Token getToken(Integer userId) {
        return tokenUtil.tokenRepository.findFirstById(userId);
    }

    /**
     * 根据token id 更新token
     * @param tokenId token的id
     */
    public String updateToken(Integer tokenId, Integer userId) {
        String newTokenStr = df.format(new Date()) + "_" + userId;
        newTokenStr = MD5Util.encrypt(newTokenStr);
        tokenUtil.tokenService.updateTokenStr(tokenId, newTokenStr);
        return newTokenStr;
    }

}
