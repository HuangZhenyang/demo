package com.hzy.Service;

import com.hzy.Model.UserProject;
import com.hzy.Model.UserProjectMultiKeysClass;
import com.hzy.Repository.UserProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/8.
 * UserProject 的Service层
 */
@Service
public class UserProjectService {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public UserProject findByUserIdAndProjectIdAndTimestamp(Integer userId,Integer projectId,String timestamp){
        UserProjectMultiKeysClass userProjectMultiKeysClass =
                new UserProjectMultiKeysClass(1,1,"2017-09-08");
        UserProject userProject = entityManager.find(UserProject.class,userProjectMultiKeysClass);
        return userProject;
    }

    public int getNumberByProjectId(Integer projectId){
        List<UserProject> userProjectList = userProjectRepository.findByProjectId(projectId);
        return userProjectList.size();
    }
}
