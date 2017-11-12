package com.hzy.Repository;

import com.hzy.Model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/8.
 * 继承PagingAndSortingRepository
 * 实现分页和排序查找
 */
public interface ProjectRepository extends PagingAndSortingRepository<Project,Integer>{
    @Query("select p from Project p where p.id=?1")
    Project findById(Integer id);

    List<Project> findByUserId(Integer userId);
}
