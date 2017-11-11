
package com.hzy;


/**
 * Created by huangzhenyang on 2017/10/8.
 * 测试布萌接口
 */


import bubi4j.Account;
import bubi4j.Asset;
import bubi4j.common.BubiServiceFactory;
import com.alibaba.fastjson.JSON;

import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BumengTest {

    @Test
    public void testBumeng(){
        String url = "https://api.bubidev.cn/";
        String appid = "7dfd883aaf191c575283c31266de6ad3";
        String appkey = "e8aa8f04e31d1321699266e45b89f5fc";
        BubiServiceFactory factory = BubiServiceFactory.getInstance(url, appid, appkey);
        String accessToken = "";
        //获取token
        try {
            accessToken = factory.getOAuthService().getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("accessToken:   " + accessToken);



        //待注册布比账户名
        String userName = "b121042";
        //待注册布比账户密码
        String password = "qaz12345678";
        //接口调用凭据号,必须在商户系统里保证唯一
        String tradeNo = "1000009820141203515651588";

        Map sParaTemp = new HashMap();
        sParaTemp.put("user_name", userName);
        sParaTemp.put("password", password);
        sParaTemp.put("trade_no", tradeNo);

        //请求布比区块链注册接口
        Account account = factory.getAccountService(accessToken);
        String result = null;
        try {
            result = account.registerBubiAccount(JSON.toJSONString(sParaTemp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("user register result: "+result);


        //[必填] 资产发行商布比地址 即bubi_id
        String assetIssuer = "bubiV8i68LTsZS2aFn57G16KpwkrdXRNijnfWFam";
//[必填] 待发行资产名称
        String assetName = "花牛贝";
//[必填] 待发行资产单位
        String assetUnit = "贝";
//[必填] 待发行资产数量
        String assetAmount = "10000";
//[必填] 资产发行商账号密码
        String accPwd = "qaz12345678";
//[可选] 资产说明，如，资产描述或摘要
        String metadata = "";
//[必填]接口调用凭据号,必须在商户系统里保证唯一
        tradeNo = "100000982014120544348888";
//请求资产发行接口
        Asset asset = factory.getAssetService(accessToken);
        try {
            result = asset.issue(assetIssuer,accPwd,tradeNo,assetName,assetUnit,assetAmount,metadata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("发行资产result：" + result);
    }

    @Test
    public void testBumengAPI(){
        String url = "https://api.bubidev.cn/oauth2/token";
        String appid = "7dfd883aaf191c575283c31266de6ad3";
        String appkey = "e8aa8f04e31d1321699266e45b89f5fc";
        String grantType = "client_credentials";

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("client_id", appid)
                .add("client_secret", appkey)
                .add("grant_type", grantType)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseData = response.body().string();
            System.out.println("token:   "+responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

