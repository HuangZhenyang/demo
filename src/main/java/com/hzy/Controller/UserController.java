package com.hzy.Controller;

import com.hzy.Model.*;
import com.hzy.Repository.*;
import com.hzy.Service.ProjectService;
import com.hzy.Service.UserProjectService;
import com.hzy.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by huangzhenyang on 2017/8/4.
 * <p>
 * 用户相关操作Controller
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

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private TokenUtil tokenUtil = new TokenUtil();

    //用户注册
    @PostMapping("/user/register")
    public String register(HttpServletRequest request, @RequestParam("name") String name,
                           @RequestParam("password") String password, @RequestParam("email") String email,
                           @RequestParam("region") String region, @RequestParam("gender") String gender) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRegion(region);
        user.setGender(gender);
        user.setBalance(0.00);

        String result;

        //是否已被注册
        if (userRepository.findByEmail(email) != null) {
            result = "{\"ok\":\"false\",\"reason\":\"已被注册\"}";
            return result;
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            result = "{\"ok\":\"false\",\"reason\":\"注册失败\"}";
            return result;
        }

        request.getSession().setAttribute("currentUser", user); // 将当前用户存进session
        return "{\"ok\":\"true\"}";
    }

    //用户登录
    @PostMapping("/user/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "{\"ok\":\"false\",\"reason\":\"用户不存在\"}";
        } else if (!(user.getPassword().equals(password))) {
            return "{\"ok\":\"false\",\"reason\":\"密码错误\"}";
        }

        Token token = tokenUtil.getToken(user.getId());
        String tokenStr ;
        if(token == null){
            tokenStr = tokenUtil.createToken(user.getId());
            //tokenRepository.save(new Token(user.getId(), tokenStr, "alive"));
            tokenUtil.saveToken(new Token(user.getId(), tokenStr, "alive"));
        }else if(!tokenUtil.checkToken(token.getTokenStr())){ // false表示已过期，则更新token
            tokenStr = tokenUtil.updateToken(token.getId(), user.getId());
        }else{
            tokenStr = token.getTokenStr();
        }

        return "{\"ok\":\"true\", \"token\":" + tokenStr + "}";
    }

    /**
     * 获取主页
     *
     * @param tokenStr 加密过的token
     * @return 主页数据
     */
    @PostMapping("/user/main")
    public String main(HttpServletRequest request, @RequestParam("token") String tokenStr) {
//        Object object = request.getSession().getAttribute("currentUser");
//
//        if (object == null || !(object instanceof User)) {
//            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
//        } else {

        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        JSONObject resultJsonObject = new JSONObject();  // 返回的jsonObject
        JSONArray newsJsonArray = new JSONArray(); // news  jsonArray
        JSONArray projectsJsonArray = new JSONArray(); // project jsonArray
        JSONObject userJsonObject = new JSONObject(); // user  jsonObject

        // 取出user信息
        User user = userRepository.findById(tokenUtil.getUserId(tokenStr));
        userJsonObject.put("name", user.getId());
        userJsonObject.put("email", user.getEmail());
        userJsonObject.put("region", user.getRegion());
        userJsonObject.put("gender", user.getGender());
        userJsonObject.put("balance", user.getBalance());
        userJsonObject.put("head", "/img/head/"+user.getHead()+".jpg");

        resultJsonObject.put("user",userJsonObject);

        //取出news  TEST PASS
        List<News> newsList = newsRepository.findTop3ByOrderByIdDesc();
        for (News news : newsList) {
            JSONObject newsJsonObject = new JSONObject();
            newsJsonObject.put("href", "/img/news/" + news.getId() + ".jpg");
            newsJsonObject.put("imgUrl", "/img/news/" + news.getImg() + ".jpg");
            newsJsonObject.put("newsDetail", news.getNewsDetail());
            newsJsonArray.put(newsJsonObject);
        }
        resultJsonObject.put("news", newsJsonArray);

        //取出projects  分页
        Page<Project> projectPage = projectService.getProjectPage("DESC", "id", 0, 5);
        List<Project> projectList = projectPage.getContent();
        for (Project project : projectList) {
            JSONObject projectsJsonObject = new JSONObject();  // 在for循环里初始化，才不会出bug

            projectsJsonObject.put("href", "/project/" + project.getId());
            projectsJsonObject.put("projectName", project.getProjectName());
            projectsJsonObject.put("initiatorName", project.getInitiatorName());
            projectsJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
            projectsJsonObject.put("description", project.getDescription());
            projectsJsonObject.put("targetMoney", project.getTargetMoney());
            projectsJsonObject.put("currentMoney", project.getCurrentMoney());
            projectsJsonObject.put("detail", project.getDetail());
            //获取已经捐赠的人数
            projectsJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

            projectsJsonArray.put(projectsJsonObject);
        }
        resultJsonObject.put("projects", projectsJsonArray);

        return resultJsonObject.toString();
//        }
    }


    /**
     * 发布项目
     *
     * */
    @PostMapping("/user/publish-project")
    public String publishProject(){
        return "";
    }


    /**
     * 获取习惯
     *
     * @return String 返回用户的所有习惯
     */
    @PostMapping("/user/get-plans")
    public String getPlan(@RequestParam("token") String tokenStr) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }
        System.out.println("curren User:" + tokenUtil.getUserId(tokenStr));
        List<Plan> planList = planRepository.findByUserId(tokenUtil.getUserId(tokenStr));

        JSONObject resultJsonObject = new JSONObject();
        JSONArray plansJsonArray = new JSONArray();

        for (Plan plan : planList) {
            JSONObject planJsonObject = new JSONObject();

            planJsonObject.put("id", plan.getId());
            planJsonObject.put("userId", plan.getUserId());
            planJsonObject.put("planName", plan.getPlanName());
            planJsonObject.put("finishedTimes", plan.getFinishedTimes());
            planJsonObject.put("value", plan.getValue());
            planJsonObject.put("startDate", plan.getStartDate());
            planJsonObject.put("deadline", plan.getDeadline());
            planJsonObject.put("lastClock", plan.getLastClock());
            planJsonObject.put("stillKeeping", plan.getStillKeeping());

            plansJsonArray.put(planJsonObject);
        }
        resultJsonObject.put("plans", plansJsonArray);

        return resultJsonObject.toString();
    }


    /**
     * 打卡
     * 修改数据库里的数据
     * @param tokenStr token 串（加密过的）
     * @param planId   要打卡的习惯的id
     * @return String 返回打卡结果
     */
    @PostMapping("/user/clock")
    public String clock(@RequestParam("token") String tokenStr, @RequestParam("token") Integer planId) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Plan plan = planRepository.findById(planId);


        return "";
    }


    /**
     * 添加习惯
     */
    @PostMapping("/user/add-plan")
    public String addPlan(@RequestParam("token") String tokenStr) {
        return  "";
    }


    //捐款
}
