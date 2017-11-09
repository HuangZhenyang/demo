package com.hzy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * Created by huangzhenyang on 2017/11/8.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetFilenameTest {

    @Test
    public void getFilenameTest(){
        String path = "C:\\Users\\huang zhen yang\\Pictures"; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }
        File fa[] = f.listFiles();
        if(fa != null){
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                if (fs.isDirectory()) {
                    System.out.println(fs.getName() + " [目录]");
                } else {
                    System.out.println(fs.getName());
                }
            }
        }

        String testStr = "5.jpg.66";
        String[] arr = testStr.split("\\.");
        for (String temp:arr) {
            System.out.println(temp);
        }
        System.out.println(arr.length);

    }





}
