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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private UserPlanRepository userPlanRepository;

    private TokenUtil tokenUtil = new TokenUtil();

    private static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd");//设置日期格式


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
    public String clock(@RequestParam("token") String tokenStr, @RequestParam("planId") Integer planId) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr);


        // 前端判断为false以后就设置打卡按钮不可点击
        // 首先检查今天打卡的日期与最后一次打卡日期是不是差了一天，是的话进行下一步，
        // 否则返回打卡失败并把stillKeeping置为"false",更新数据库
        // finishedTimes += 1，lastClock = 今天，
        // 然后看今天是否已经是截止日期，是的话则该习惯已经完成，stillKeeping置为over, 更新user拥有的plan数量
        // 否则的话stillKeeping仍然为"true"
        Plan plan = planRepository.findById(planId); // 根据plan的id获取plan对象
        Date deadlineDate; // 截止的日期
        Date currDate; // 当前日期
        String currDateStr;
        Date lastClockDate; // 最后一次打卡日期
        int dueTime = 86400000;
        int heartValuePerClock = 5; // 每次打卡获得的爱心值

        try {
            deadlineDate = df.parse(plan.getDeadline());
            currDate= df.parse(df.format(new Date()));
            lastClockDate = df.parse(plan.getLastClock());
            currDateStr = df.format(currDate);

            if(plan.getLastClock() == null){ // // 如果为空，说明是第一次打卡
                plan.setLastClock(currDateStr);
                plan.setValue(plan.getValue() + heartValuePerClock);
                plan.setFinishedTimes(plan.getFinishedTimes() + 1);
                planRepository.save(plan);   // TODO:测试这里会不会覆盖原有的，还是新创建了   PASS:会覆盖原有的（相当于更新了）
                return "{\"ok\":\"true\", \"reason\":\"第一天打卡完成\"}";

            }else {
                if(currDate.getTime() - lastClockDate.getTime() == dueTime){ // 是否与上次打卡只差了一天
                    plan.setLastClock(currDateStr);
                    plan.setFinishedTimes(plan.getFinishedTimes() + 1);
                    plan.setValue(plan.getValue() + heartValuePerClock);
                    if(currDate.getTime() == deadlineDate.getTime()){ // 今天是否已经是截止日期
                        String stillKeepingPara = "over";
                        plan.setStillKeeping(stillKeepingPara);
                        //planRepository.save(plan);
                        UserPlan userPlan = userPlanRepository.findByUserId(userId);
                        Integer newPlanNumber = userPlan.getPlanNumber() + 1;
                        userPlan.setPlanNumber(newPlanNumber);
                        userPlanRepository.save(userPlan);
                    }else{
                        planRepository.save(plan);
                    }

                    return "{\"ok\":\"true\", \"reason\":\"目标达成\"}";
                } else if(currDate.getTime() - lastClockDate.getTime() > dueTime){
                    String stillKeepingPara = "false";
                    plan.setStillKeeping(stillKeepingPara);
                    planRepository.save(plan);
                    return "{\"ok\":\"false\", \"reason\":\"您距离上一次打卡已经超过一天\"}";
                }else if(currDate.getTime() - lastClockDate.getTime() == 0){ // 如果是最后一次打卡就是今天，则返回打卡失败
                    return "{\"ok\":\"false\", \"reason\":\"您今天已经打卡完毕，明天再来吧\"}";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "{\"ok\":\"true\"}";
    }


    /**
     * 添加习惯
     * @param tokenStr token
     * @return String 返回添加成功与否
     */
    @PostMapping("/user/add-plan")
    public String addPlan(@RequestParam("token") String tokenStr) {
        return  "";
    }


    //捐款
}
