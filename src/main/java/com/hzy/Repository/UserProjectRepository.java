package com.hzy.Repository;

import com.hzy.Model.UserProject;
import com.hzy.Model.UserProjectMultiKeysClass;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/9/7.
 * UserProject的数据访问层
 */
public interface UserProjectRepository extends JpaRepository<UserProject,UserProjectMultiKeysClass>{
    // 根据用户Id,找出用户参与的所有UserProject
    // TEST PASS
    List<UserProject> findByUserId(Integer userId);

    // 根据项目id,找出参与项目的所有UserProject
    // TEST PASS
    List<UserProject> findByProjectId(Integer projectId);

    // 根据用户id和项目id 找出所有的UserProject
    // TEST PASS
    List<UserProject> findByUserIdAndProjectId(Integer userId,Integer projectId);
}
