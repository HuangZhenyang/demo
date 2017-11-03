package com.hzy.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by huangzhenyang on 2017/8/11.
 * 测试返回html页面
 */
@Controller
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "page";
    }

    /*@GetMapping("/getimg")  不能直接返回静态资源
    public String getImg(){
        return "img/news/1.jpg";
    }*/
}
