package com.hzy;

import com.hzy.Model.Project;
import com.hzy.Repository.ProjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by huangzhenyang on 2017/9/8.
 * ProjectRepositoryTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testProjectRepository() {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort sort = new Sort(order);

        Pageable pageable = new PageRequest(0, 5,sort); // 取第0页，每页5条数据； page从0开始
        Page<Project> projectPage = projectRepository.findAll(pageable);

        System.out.println("总个数"+projectPage.getTotalElements());
        System.out.println("总页数"+projectPage.getTotalPages());
        System.out.println("当前第几页:"+projectPage.getNumber());
        System.out.println("返回的所有对象："+projectPage.getContent());
        System.out.println("当前页面的记录数"+projectPage.getNumberOfElements());

        System.out.println(projectPage.getContent().get(0).getImgUrl());
    }
}
