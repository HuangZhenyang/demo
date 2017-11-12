package com.hzy.Repository;

import com.hzy.Model.UserStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/11/12.
 *
 */
public interface UserStarRepository extends JpaRepository<UserStar, Integer>{
    UserStar findByUserIdAndProjectId(Integer userId, Integer projectId);

    List<UserStar> findByUserId(Integer userId);
}
