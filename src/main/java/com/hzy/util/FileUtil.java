package com.hzy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/11/8.
 * 文件处理相关工具类
 */

public class FileUtil {
    public static List<String> getFileNameList(String directoryPath) {
        List<String> fileNameList = new ArrayList<>();
        File f = new File(directoryPath);
        if (!f.exists()) {
            System.out.println(directoryPath + " not exists");
            return null;
        }
        File fa[] = f.listFiles();
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
        }

        return fileNameList;
    }
}
