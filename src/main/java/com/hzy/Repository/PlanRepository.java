package com.hzy.Repository;

import com.hzy.Model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/11/1.
 * 用户习惯的Repository
 */
public interface PlanRepository extends JpaRepository<Plan,Integer> {
    //根据用户id返回
    @Query("select p from Plan p where p.userId=?1")
    List<Plan> findByUserId(Integer userId);

    //根据planid返回
    @Query("select p from Plan p where p.id=?1")
    Plan findById(Integer id);

    //根据用户id和stillKeeping为"true"返回
    @Query("select p from Plan p where p.userId=?1 and p.stillKeeping=?2")
    List<Plan> findByUserIdAndStillKeeping(Integer userId, String stillKeeping);
}
