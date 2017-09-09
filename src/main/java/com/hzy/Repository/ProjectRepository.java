package com.hzy.Repository;

import com.hzy.Model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by huangzhenyang on 2017/9/8.
 * 继承PagingAndSortingRepository
 * 实现分页和排序查找
 */
public interface ProjectRepository extends PagingAndSortingRepository<Project,Integer>{
}
