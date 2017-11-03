package com.hzy.Repository;

import com.hzy.Model.UserPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huangzhenyang on 2017/11/3.
 *
 */
@Repository
public interface UserPlanRepository extends CrudRepository<UserPlan, Integer> {

    UserPlan findByUserId(Integer userId);
}
