package com.hzy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/11/8.
 * 文件处理相关工具类
 */

public class FileUtil {
    public static List<Integer> getFileNameList(String directoryPath) {
        List<String> fileNameList = new ArrayList<>();
        List<Integer> fileIndexList = new ArrayList<>();
        File f = new File(directoryPath);
        if (!f.exists()) {
            System.out.println(directoryPath + " not exists");
            return null;
        }
        File fa[] = f.listFiles();
        System.out.println(fa.length);
        if (fa != null) {
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                if (fs.isDirectory()) {
                    System.out.println(fs.getName() + " [目录]");
                } else {
                    System.out.println(fs.getName());
                    fileNameList.add(fs.getName());
                }
            }
            // 对文件名（序号）进行排序
            for(int i=0;i<fa.length;i++){
                fileIndexList.add(Integer.parseInt(
                        fileNameList.get(i).split("\\.")[0]));

            }
        }
        Collections.sort(fileIndexList);


        return fileIndexList;
    }
}
