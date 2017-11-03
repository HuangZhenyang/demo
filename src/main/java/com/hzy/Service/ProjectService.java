package com.hzy.Service;

import com.hzy.Model.Project;
import com.hzy.Repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhenyang on 2017/9/8.
 *
 */
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Page<Project> getProjectPage(String sortWay,String property,int page,int size){
        Sort.Order order = null;
        if("DESC".equals(sortWay)){
            order = new Sort.Order(Sort.Direction.DESC,property);
        }else if("ASC".equals(sortWay)){
            order = new Sort.Order(Sort.Direction.ASC,property);
        }

        Sort sort = new Sort(order);

        Pageable pageable = new PageRequest(page, size,sort); // 取第0页，每页5条数据； page从0开始
        Page<Project> projectPage = projectRepository.findAll(pageable);
        return projectPage;
    }
}
