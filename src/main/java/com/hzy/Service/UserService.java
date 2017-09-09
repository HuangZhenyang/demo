package com.hzy.Service;

import com.hzy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by huangzhenyang on 2017/8/5.
 * 更新删除操作写在service层
 * 增、查操作放在Repository
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updatePassword(String password){
        userRepository.updatePassword(password);
    }
}
