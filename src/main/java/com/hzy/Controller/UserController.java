package com.hzy.Controller;

import com.hzy.Model.News;
import com.hzy.Model.Project;
import com.hzy.Repository.NewsRepository;
import com.hzy.Repository.ProjectRepository;
import com.hzy.Repository.UserProjectRepository;
import com.hzy.Repository.UserRepository;
import com.hzy.Model.User;
import com.hzy.Service.ProjectService;
import com.hzy.Service.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/8/4.
 *
 * 用户相关操作Controller
 *
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserProjectService userProjectService;

    //用户注册
    @PostMapping("/user/register")
    public String register(HttpServletRequest request,@RequestParam("name") String name,
                         @RequestParam("password") String password,@RequestParam("email") String email,
                         @RequestParam("region") String region,@RequestParam("gender") String gender){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRegion(region);
        user.setGender(gender);
        user.setBalance(0.00);

        String result;

        //是否已被注册
        if(userRepository.findByEmail(email) != null){
            result = "{\"ok\":\"false\",\"reason\":\"已被注册\"}";
            return result;
        }
        try{
            userRepository.save(user);
        }catch (Exception e){
            result = "{\"ok\":\"false\",\"reason\":\"注册失败\"}";
            return result;
        }

        request.getSession().setAttribute("currentUser",user); // 将当前用户存进session
        return "{\"ok\":\"true\"}";
    }
    //用户登录
    @PostMapping("/user/login")
    public String login(HttpServletRequest request,@RequestParam("email") String email,
                        @RequestParam("password") String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return "{\"ok\":\"false\",\"reason\":\"用户不存在\"}";
        }else if(!(user.getPassword().equals(password))){
            return "{\"ok\":\"false\",\"reason\":\"密码错误\"}";
        }
        request.getSession().setAttribute("currentUser",user);
        return "{\"ok\":\"true\"}";
    }

    //获取主页
    @GetMapping("/user/main")
    public String main(HttpServletRequest request){
        if(request.getSession().getAttribute("currentUser") == null){
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        }else{
            JSONObject resultJsonObject = new JSONObject();
            JSONArray newsJsonArray = new JSONArray();

            JSONArray projectsJsonArray = new JSONArray();


            //取出news  TEST PASS
            List<News> newsList = newsRepository.findTop3ByOrderByIdDesc();
            for(News news:newsList){
                JSONObject newsJsonObject = new JSONObject();
                newsJsonObject.put("href","/news/"+news.getId());
                newsJsonObject.put("imgUrl",news.getImgUrl());
                newsJsonArray.put(newsJsonObject);
            }
            resultJsonObject.put("news",newsJsonArray);

            //取出projects  分页
            Page<Project> projectPage = projectService.getProjectPage("DESC","id",0,5);
            List<Project> projectList = projectPage.getContent();
            for(Project project:projectList){
                JSONObject projectsJsonObject = new JSONObject();
                projectsJsonObject.put("initiatorName",project.getInitiatorName());
                projectsJsonObject.put("imgUrl",project.getImgUrl());
                projectsJsonObject.put("projectName",project.getProjectName());
                projectsJsonObject.put("href","/project/"+project.getId());
                //获取已经捐赠的人数
                projectsJsonObject.put("peopleNumber",""+userProjectService.getNumberByProjectId(project.getId()));

                projectsJsonArray.put(projectsJsonObject);
            }
            resultJsonObject.put("projects",projectsJsonArray);

            return resultJsonObject.toString();
        }
    }


}
