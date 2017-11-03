package com.hzy;

/**
 * Created by huangzhenyang on 2017/11/2.
 * 日期测试
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DateTest {
    private static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    @Test
    public void testDate() throws ParseException {
        Date tokenDate = df.parse("2017-11-01");
        Date currDate = df.parse(df.format(new Date()));

        System.out.println("curr:" + currDate.getTime());
        System.out.println("token: "+ tokenDate.getTime());
        System.out.println("???:   "+(currDate.getTime() - tokenDate.getTime()));
    }
}
