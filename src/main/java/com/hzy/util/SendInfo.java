package com.hzy.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by huangzhenyang on 2017/8/6.
 */
public class SendInfo {
    public static void render(String content,String type,HttpServletResponse response) throws Exception{
        response.setContentType(type + ";charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
        out.close();
    }
}
