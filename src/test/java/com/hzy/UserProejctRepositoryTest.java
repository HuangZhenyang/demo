package com.hzy;

import com.hzy.Model.UserProject;
import com.hzy.Model.UserProjectMultiKeysClass;
import com.hzy.Repository.UserProjectRepository;
import com.hzy.Service.UserProjectService;
import com.hzy.Service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProejctRepositoryTest {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private UserProjectService userProjectService;

    @Test
    public void testUserProjectRepository(){
        UserProjectMultiKeysClass userProjectMultiKeysClass =
                new UserProjectMultiKeysClass(1, 1, "2017-09-08");
        UserProject userProject = entityManager.find(UserProject.class,userProjectMultiKeysClass);
        System.out.println(userProject.toString());
    }


    @Test
    public void testFindByUserId(){
        List<UserProject> userProjects = userProjectRepository.findByUserId(1);
        for(UserProject userProject:userProjects){
            System.out.println(userProject.toString());
        }
    }

    @Test
    public void testFindByProjectId(){
        List<UserProject> userProjects = userProjectRepository.findByProjectId(1);
        for(UserProject userProject:userProjects){
            System.out.println(userProject.toString());
        }
    }

    @Test
    public void testFindByUserIdAndProjectId(){
        List<UserProject> userProjects = userProjectRepository.findByUserIdAndProjectId(1,1);
        for(UserProject userProject:userProjects){
            System.out.println(userProject.toString());
        }
    }

    @Test
    public void testFindByUserIdAndProjectIdAndTimestamp(){
        UserProject userProject = userProjectService.findByUserIdAndProjectIdAndTimestamp(1,1,"2017-09-08");
        System.out.println(userProject.toString());
    }
}
