package com.hzy.Repository;

import com.hzy.Model.News;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/7.
 */
public interface NewsRepository extends CrudRepository<News,Integer> {
    // 找出数据库中最新插入的三条数据    TEST PASS
    List<News> findTop3ByOrderByIdDesc();

}
