package com.hzy;

import com.hzy.Model.News;
import com.hzy.Repository.NewsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void testFindTop3OrderByIdDesc(){
        List<News> newsList = newsRepository.findTop3ByOrderByIdDesc();
        System.out.println(">>> news:");
        System.out.println(newsList.size());
        for(News news:newsList){
            System.out.println(news.toString());
        }
    }
}
