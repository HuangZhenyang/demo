package com.hzy;

/**
 * Created by huangzhenyang on 2017/11/2.
 */

import com.hzy.util.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptTest {




    @Test
    public void test() {
        String s = new String("tangfuqiang");
        System.out.println("原始：" + s);

        System.out.println("加密的：" + MD5Util.encrypt(s));
        System.out.println("解密的：" + MD5Util.decrypt(MD5Util.encrypt(s)));

    }


}
