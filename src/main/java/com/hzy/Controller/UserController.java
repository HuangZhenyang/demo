package com.hzy.Controller;

import com.hzy.Dao.UserRepository;
import com.hzy.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by huangzhenyang on 2017/8/4.
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /*@RequestMapping("/user")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/adduser")
    public String addUser(HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("email") String email){
        User user = new User(userName,password,email);
        request.getSession().setAttribute("loginUser",user);
        userRepository.save(user);
        return "{\"ok\":\"true\"}";
    }

    @PostMapping("/finduserbyname")
    public User findUserOne(@RequestParam("userName") String userName){
        return userRepository.findByUserName(userName);
    }

    @GetMapping("/getloginuser")
    public User getLoginUser(HttpServletRequest request){
        return (User)request.getSession().getAttribute("loginUser");
    }*/
    //用户注册
    @PostMapping("/user/register")
    public String register(HttpServletRequest request,@RequestParam("userName") String userName,
                         @RequestParam("password") String password,@RequestParam("email") String email,
                         @RequestParam("address") String address,@RequestParam("gender") String gender){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        user.setGender(gender);
        user.setBalance(0.00);

        String result;

        //是否已被注册
        if(userRepository.findByEmail(email) != null){
            result = "{'ok':false,'reason':'已被注册'}";
            return result;
        }
        try{
            userRepository.save(user);
        }catch (Exception e){
            result = "{'ok':false,'reason':'注册失败'}";
            return result;
        }

        request.getSession().setAttribute("currentUser",user);
        return "{\"ok\":\"true\"}";
    }
    //用户登录
    @PostMapping("/user/login")
    public String login(HttpServletRequest request,@RequestParam("userName") String userName,
                        @RequestParam("password") String password){
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return "{'ok':false,'reason':'用户名不存在'}";
        }else if(!(user.getPassword().equals(password))){
            return "{'ok':false,'reason':'密码错误'}";
        }
        request.getSession().setAttribute("currentUser",user);
        return "{'ok':true}";
    }

    //获取主页
    
}
