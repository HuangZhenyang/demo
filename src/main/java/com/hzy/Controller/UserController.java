package com.hzy.Controller;


import com.hzy.Model.*;
import com.hzy.Repository.*;
import com.hzy.Service.ProjectService;
import com.hzy.Service.UserProjectService;
import com.hzy.util.FileUtil;
import com.hzy.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserStarRepository userStarRepository;

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
        user.setValue(0.00);

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
        String tokenStr;
        if (token == null) {
            tokenStr = tokenUtil.createToken(user.getId());
            //tokenRepository.save(new Token(user.getId(), tokenStr, "alive"));
            tokenUtil.saveToken(new Token(user.getId(), tokenStr, "alive"));
        } else if (!tokenUtil.checkToken(token.getTokenStr())) { // false表示已过期，则更新token
            tokenStr = tokenUtil.updateToken(token.getId(), user.getId());
        } else {
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
        userJsonObject.put("head", "/img/head/" + user.getHead() + ".jpg");
        userJsonObject.put("value", user.getValue());

        resultJsonObject.put("user", userJsonObject);

        //取出news  TEST PASS
        List<News> newsList = newsRepository.findTop3ByOrderByIdDesc();
        for (News news : newsList) {
            JSONObject newsJsonObject = new JSONObject();
            newsJsonObject.put("id", news.getId());
            newsJsonObject.put("href", "/img/news/" + news.getId() + ".jpg");
            newsJsonObject.put("imgUrl", "/img/news/" + news.getImg() + ".jpg");
            newsJsonObject.put("newsDetail", news.getNewsDetail());
            newsJsonArray.put(newsJsonObject);
        }
        resultJsonObject.put("news", newsJsonArray);

        //取出projects  分页
        //Page<Project> projectPage = projectService.getProjectPage("DESC", "id", 0, 10);
        //List<Project> projectList = projectPage.getContent();
        List<Project> projectList = projectRepository.findAll();
        for (Project project : projectList) {
            if( project.getUserId().equals( tokenUtil.getUserId( tokenStr))){
                continue;
            }
            JSONObject projectsJsonObject = new JSONObject();  // 在for循环里初始化，才不会出bug

            projectsJsonObject.put("id", project.getId());
            projectsJsonObject.put("href", "/project/" + project.getId());
            projectsJsonObject.put("projectName", project.getProjectName());
            projectsJsonObject.put("initiatorName", project.getInitiatorName());
            projectsJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
            projectsJsonObject.put("description", project.getDescription());
            projectsJsonObject.put("targetMoney", project.getTargetMoney());
            projectsJsonObject.put("currentMoney", project.getCurrentMoney());
            projectsJsonObject.put("detail", project.getDetail());
            projectsJsonObject.put("imgListStr", project.getImgListStr());
            projectsJsonObject.put("userId", project.getUserId());
            projectsJsonObject.put("over", project.getOver());
            projectsJsonObject.put("startTime", project.getStartDate());
            //获取已经捐赠的人数
            projectsJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

            projectsJsonArray.put(projectsJsonObject);
        }
        resultJsonObject.put("projects", projectsJsonArray);
        resultJsonObject.put("ok", "true");

        return resultJsonObject.toString();
//        }
    }


    /**
     * 客户端先上传项目的信息，服务端返回该新项目的id, 客户端传图片的时候带着项目id
     * 爱心值达到200才能发布项目
     *
     * @param tokenStr          token
     * @param projectNamePara   项目名称
     * @param initiatorNamePara 发起方的名字（如果是用户就是用户名）
     * @param descriptionPara   项目的简短的一句话描述
     * @param targetMoneyPara   目标筹款金额
     * @param detailPara        项目的具体详情描述（仅包含文字）
     */
    @PostMapping("/user/publish-project")
    public String publishProject(@RequestParam("token") String tokenStr,
                                 @RequestParam("projectName") String projectNamePara,
                                 @RequestParam("initiatorName") String initiatorNamePara,
                                 @RequestParam("description") String descriptionPara,
                                 @RequestParam("targetMoney") double targetMoneyPara,
                                 @RequestParam("detail") String detailPara) {

        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }
        // 创建对象
        User user = userRepository.findById(tokenUtil.getUserId(tokenStr));
        // 判断用户爱心值是否超过200
        if (user.getValue() < 200) {
            return "{\"ok\":\"false\", \"reason\":\"您的爱心值不够哦\"}";
        }

        Project tempProject = new Project();
        // 插入数据
        tempProject.setProjectName(projectNamePara);
        tempProject.setInitiatorName(initiatorNamePara);
        tempProject.setDescription(descriptionPara);
        tempProject.setTargetMoney(targetMoneyPara);
        tempProject.setDetail(detailPara);
        tempProject.setUserId(user.getId());
        String overStr = "false";
        tempProject.setOver(overStr);
        String startDateStr = df.format(new Date());
        tempProject.setStartDate(startDateStr);
        //保存
        Project newProject = projectRepository.save(tempProject);
        Integer newProjectId = newProject.getId();

        return "{\"ok\":\"true\", \"newProjectId\": \"" + newProjectId + "\"}";
    }


    /**
     * project detail的图片
     *
     * @param tokenStr          token
     * @param newProjectIdPara  新建项目的id
     * @param multipartFilePara 图片
     */
    @PostMapping("/user/project-img-upload")
    public String projectImgUpload(@RequestParam("token") String tokenStr,
                                   @RequestParam("newProjectId") Integer newProjectIdPara,
                                   @RequestParam("pic") MultipartFile multipartFilePara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer newProjectId = newProjectIdPara;

        MultipartFile multipartFile = multipartFilePara;
        int fileNumber = 1; // 图片的序号， 5.jpg
        if (!multipartFile.isEmpty()) {
            // 获取project目录下最新的id是多少,得到新图片的id
            //C:\project\demo\src\main\resources\static\img\project
            String directoryPath = "C:\\Users\\Administrator\\Desktop\\demo-master\\src\\main\\resources\\static\\img\\project\\";
            List<Integer> fileIndexList = FileUtil.getFileNameList(directoryPath);  // 取出的文件是按顺序排列的, 数字小到大，然后是字母a-z,当然，这里只有数字
            Integer lastFileIndex;
            if (fileIndexList == null) {
                fileNumber = 1;
            } else {
                lastFileIndex = fileIndexList.get(fileIndexList.size() - 1); // 获取最后一个
                fileNumber = lastFileIndex + 1;
            }

            // 存储图片
            try {
                String filePath = "C:\\Users\\Administrator\\Desktop\\demo-master\\src\\main\\resources\\static\\img\\project\\";
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath + fileNumber + ".jpg")));//保存图片到目录下
                out.write(multipartFile.getBytes());
                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "{\"ok\":\"false\"}";
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"ok\":\"false\"}";
            }
        }

        // 存储图片与project的关系字段
        Project project = projectRepository.findById(newProjectId);
        String imgListStrPara = project.getImgListStr();
        if (imgListStrPara == null) {
            project.setImg(fileNumber);  // 将该项目首页的图片设置为上传的第一张图片
            imgListStrPara = fileNumber + "_";
        } else {
            imgListStrPara = imgListStrPara + fileNumber + "_";
        }
        project.setImgListStr(imgListStrPara);

        projectRepository.save(project);

        return "{\"ok\":\"true\", \"imgListStrPara\":\"" + imgListStrPara + "\"}";
    }


    /**
     * 捐款
     * <p>
     * 前端如果判断currentMoney和targetMoney相等，就显示已经完成该项目
     * 判断加上去后新的money的值，并判断加上去以后会不会超过targetMoney
     * 如果会，则退一部分款项； 新建一个userProject对象，存进数据库
     *
     * @param tokenStr       token
     * @param projectIdPara  要捐款的项目的id
     * @param numOfMoneyPara 捐的数量
     */
    @PostMapping("/user/donate")
    public String donateProject(@RequestParam("token") String tokenStr,
                                @RequestParam("projectId") Integer projectIdPara,
                                @RequestParam("numOfMoney") double numOfMoneyPara,
                                @RequestParam("message") String messagePara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        User user = userRepository.findById(tokenUtil.getUserId(tokenStr));
        Integer projectId = projectIdPara;
        double numOfMoney = numOfMoneyPara;
        String message = messagePara;
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            return "{\"ok\":\"false\",\"reason\":\"project id: " + projectId + "找不到了 ！\"}";
        }

        UserProject userProject = new UserProject();

        //计算加上去后的新的当前money
        double targetMoney = project.getTargetMoney();
        double currentMoney = project.getCurrentMoney();
        double newMoney = currentMoney + numOfMoney;
        double userCurrBalance = user.getBalance();
        double userCurrValue = user.getValue();


        if (newMoney > targetMoney) {  // 如果当前的捐款 加上 用户该捐款大于目标金额，则只扣除需要的那部分
            // 只减去用户余额中 targetMoney-currentMoney的那部分
            if (userCurrBalance - (targetMoney - currentMoney) < 0) {
                return "{\"ok\":\"false\",\"reason\":\"您的余额不足\"}";
            }
            user.setBalance(userCurrBalance - (targetMoney - currentMoney));
            user.setValue(userCurrValue + 0.5 * (targetMoney - currentMoney));
            userRepository.save(user);

            project.setCurrentMoney(targetMoney);
            String overStr = "true";
            project.setOver(overStr);
            projectRepository.save(project);

            userProject.setDonateMoney(targetMoney - currentMoney);

        } else {
            if (userCurrBalance < numOfMoney) {
                return "{\"ok\":\"false\",\"reason\":\"您的余额不足\"}";
            }
            user.setBalance(userCurrBalance - numOfMoney);
            user.setValue(userCurrValue + 0.5 * numOfMoney);
            userRepository.save(user);
            project.setCurrentMoney(newMoney);
            projectRepository.save(project);

            userProject.setDonateMoney(numOfMoney);

        }
        //userProject
        userProject.setUserId(user.getId());
        userProject.setProjectId(projectId);
        SimpleDateFormat userProjectDateFormate = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//设置日期格式
        String timestampStr = userProjectDateFormate.format(new Date());
        userProject.setTimestamp(timestampStr);

        userProjectRepository.save(userProject);

        //user message
        UserMessage userMessage = new UserMessage();
        userMessage.setMessage(message);
        userMessage.setUserId(user.getId());
        userMessage.setProjectId(projectId);
        userMessage.setTimestamp(timestampStr);

        userMessageRepository.save(userMessage);

        return "{\"ok\":\"true\"}";
    }


    /**
     * 获取习惯
     *
     * @param tokenStr token
     * @return String 返回用户的所有习惯
     */
    @PostMapping("/user/get-plans")
    public String getPlan(@RequestParam("token") String tokenStr) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

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
     *
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
        int heartValuePerClock = 1; // 每次打卡获得的爱心值

        try {
            deadlineDate = df.parse(plan.getDeadline());
            currDate = df.parse(df.format(new Date()));
            currDateStr = df.format(currDate);

            if (plan.getLastClock() == null) { // // 如果为空，说明是第一次打卡
                plan.setLastClock(currDateStr);
                plan.setValue(plan.getValue() + heartValuePerClock); // 新建计划时爱心值为0
                plan.setFinishedTimes(plan.getFinishedTimes() + 1);
                planRepository.save(plan);   // TODO:测试这里会不会覆盖原有的，还是新创建了   PASS:会覆盖原有的（相当于更新了）
                return "{\"ok\":\"true\", \"reason\":\"第一天打卡完成\"}";

            } else {
                lastClockDate = df.parse(plan.getLastClock());
                if (currDate.getTime() - lastClockDate.getTime() == dueTime) { // 是否与上次打卡只差了一天
                    plan.setLastClock(currDateStr);
                    plan.setFinishedTimes(plan.getFinishedTimes() + 1);
                    plan.setValue(plan.getValue() + heartValuePerClock);
                    if (currDate.getTime() == deadlineDate.getTime()) { // 今天是否已经是截止日期
                        String stillKeepingPara = "over";
                        plan.setStillKeeping(stillKeepingPara);
                        //planRepository.save(plan);
                        // 设置用户的plan数量+1
                        UserPlan userPlan = userPlanRepository.findByUserId(userId);
                        Integer newPlanNumber = userPlan.getPlanNumber() + 1;
                        userPlan.setPlanNumber(newPlanNumber);
                        userPlanRepository.save(userPlan);
                        // 将该计划的爱心值加到用户的爱心值中
                        User user = userRepository.findById(userId);
                        user.setValue(plan.getValue());
                        userRepository.save(user);
                    } else {
                        planRepository.save(plan);
                    }

                    return "{\"ok\":\"true\", \"reason\":\"目标达成\"}";
                } else if (currDate.getTime() - lastClockDate.getTime() > dueTime) {
                    String stillKeepingPara = "false";
                    plan.setStillKeeping(stillKeepingPara);
                    planRepository.save(plan);
                    return "{\"ok\":\"false\", \"reason\":\"您距离上一次打卡已经超过一天\"}";
                } else if (currDate.getTime() - lastClockDate.getTime() == 0) { // 如果是最后一次打卡就是今天，则返回打卡失败
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
     *
     * @param tokenStr token
     * @return String 返回添加成功与否
     */
    @PostMapping("/user/add-plan")
    public String addPlan(@RequestParam("token") String tokenStr,
                          @RequestParam("planName") String planNamePara) {
        // 找出UserPlan中当前用户的plan number有多少个
        // 找出当前该用户一共有多少个 状态为"true"的plan, 并与上面的对比看是否小于最大的习惯数量
        // 小的话则添加该习惯，否则返回添加失败
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr); // 用户ID
        UserPlan userPlan = userPlanRepository.findByUserId(userId); // 获取用户与plan的数量关系
        Integer userPlanNumber = userPlan.getPlanNumber();// 获取用户能
        String stillKeepingStr = "true";
        List<Plan> planList = planRepository.findByUserIdAndStillKeeping(userId, stillKeepingStr); // 获取当前用户仍在坚持的习惯列表

        String newPlanName = planNamePara;

        if (planList.size() < userPlanNumber) { // 如果当前用户的习惯数量小于他习惯数量最大值，则添加习惯
            Plan newPlan = new Plan();

            // 开始日期
            String startDateStr = df.format(new Date());
            // 截止日期
            Calendar calendar = Calendar.getInstance();
            int addDays = 21; // 需要增加的天数
            calendar.add(Calendar.DATE, addDays);// num为增加的天数
            Date deadlineDate = calendar.getTime();
            String deadlineDateStr = df.format(deadlineDate);
            // 是否仍在坚持
            String stillKeepingPara = "true";

            // 设置字段的值
            newPlan.setUserId(userId);
            newPlan.setPlanName(newPlanName);
            newPlan.setFinishedTimes(0);
            newPlan.setValue(0);
            newPlan.setStartDate(startDateStr);
            newPlan.setDeadline(deadlineDateStr);
            //newPlan.setLastClock(startDateStr);
            newPlan.setStillKeeping(stillKeepingPara);

            planRepository.save(newPlan);

            return "{\"ok\":\"true\"}";

        } else { // 否则，返回false
            return "{\"ok\":\"false\", \"reason\":\"您当前的习惯数量已达上限\"}";
        }
    }


    /**
     * 删除习惯
     *
     * @param tokenStr   token
     * @param planIdPara 需要删除的plan 的id
     * @return String 返回是否成功删除
     */
    @PostMapping("/user/del-plan")
    public String delPlan(@RequestParam("token") String tokenStr,
                          @RequestParam("planId") Integer planIdPara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer planId = planIdPara;
        planRepository.delete(planId);

        return "{\"ok\":\"true\"}";
    }


    /**
     * 获取所有的捐款message
     *
     * @param tokenStr token
     * @return String 返回用户信息，项目名字，message, timestamp
     */
    @PostMapping("/user/get-user-message")
    public String getDonationInfo(@RequestParam("token") String tokenStr) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        List<UserMessage> userMessageList = userMessageRepository.findAll();
        if (userMessageList == null || userMessageList.size() == 0) {
            return "{\"ok\":\"false\", \"reason\":\"userMessageList为空\"}";
        }
        JSONObject resultJsonObject = new JSONObject();
        JSONArray userMessageJsonArray = new JSONArray();
        for (UserMessage userMessage : userMessageList) {
            JSONObject userMessageJsonObject = new JSONObject();
            User user = userRepository.findById(userMessage.getUserId());
            Project project = projectRepository.findById(userMessage.getProjectId());
            String message = userMessage.getMessage();
            String timestamp = userMessage.getTimestamp();
            // user
            userMessageJsonObject.put("name", user.getId());
            userMessageJsonObject.put("email", user.getEmail());
            userMessageJsonObject.put("region", user.getRegion());
            userMessageJsonObject.put("gender", user.getGender());
            userMessageJsonObject.put("balance", user.getBalance());
            userMessageJsonObject.put("head", "/img/head/" + user.getHead() + ".jpg");
            userMessageJsonObject.put("value", user.getValue());
            // project
            userMessageJsonObject.put("projectName", project.getProjectName());
            // message
            userMessageJsonObject.put("message", message);
            // timestamp
            userMessageJsonObject.put("timestamp", timestamp);

            userMessageJsonArray.put(userMessageJsonObject);
        }

        resultJsonObject.put("ok", "true");
        resultJsonObject.put("userMessage", userMessageJsonArray);

        return resultJsonObject.toString();
    }


    /**
     * 获得某个项目所有捐助信息
     *
     * @param tokenStr      token
     * @param projectIdPara 项目的id
     * @return 返回捐赠该项目的用户名字，头像，捐了多少钱，时间戳
     */
    @PostMapping("/user/get-all-donation")
    public String getAllDonation(@RequestParam("token") String tokenStr,
                                 @RequestParam("projectId") Integer projectIdPara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer projectId = projectIdPara;
        List<UserProject> userProjectList = userProjectRepository.findByProjectId(projectId);
        if (userProjectList == null || userProjectList.size() == 0) {
            return "{\"ok\":\"false\", \"reason\":\"userProjectList为空\"}";
        }

        JSONObject resultJsonObject = new JSONObject();
        JSONArray tempJsonArray = new JSONArray();

        for (UserProject userProject : userProjectList) {
            JSONObject tempJsonObject = new JSONObject();
            User user = userRepository.findById(userProject.getUserId());

            tempJsonObject.put("userName", user.getName());
            tempJsonObject.put("head", "/img/head/" + user.getHead() + ".jpg");
            tempJsonObject.put("donateMoney", userProject.getDonateMoney());
            tempJsonObject.put("timestamp", userProject.getTimestamp());

            tempJsonArray.put(tempJsonObject);
        }

        resultJsonObject.put("ok", "true");
        resultJsonObject.put("result", tempJsonArray);

        return resultJsonObject.toString();
    }


    /**
     * 用户关注项目
     *
     * @param tokenStr token
     * @param projectIdPara  项目id
     * @return String 返回关注的结果
     */
    @PostMapping("/user/star-project")
    public String starProject(@RequestParam("token") String tokenStr,
                              @RequestParam("projectId") Integer projectIdPara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr); // 用户id
        Integer projectId = projectIdPara;// project id
        SimpleDateFormat userStarDateFormate = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");//设置日期格式
        String timestampStr = userStarDateFormate.format(new Date()); // 时间戳timestamp
        UserStar userStar = new UserStar();

        userStar.setUserId(userId);
        userStar.setProjectId(projectId);
        userStar.setTimestamp(timestampStr);

        userStarRepository.save(userStar);

        return "{\"ok\":\"true\"}";
    }


    /**
     * 取消关注项目
     *
     * @param tokenStr  token
     * @param projectIdPara 项目id
     * @return  String 返回取消关注的结果
     */
    @PostMapping("/user/cancel-start-project")
    public String cancelStarProject(@RequestParam("token") String tokenStr,
                                    @RequestParam("projectId") Integer projectIdPara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr);
        Integer projectId = projectIdPara;
        UserStar userStar = userStarRepository.findByUserIdAndProjectId(userId, projectId);
        if (userStar != null) {
            userStarRepository.delete(userStar);
        } else {
            return "{\"ok\":\"false\",\"reason\":\"您没有关注该项目\"}";
        }
        return "{\"ok\":\"true\"}";
    }


    /**
     * 进入项目详情时返回用户是否关注该项目
     *
     * @param tokenStr token
     * @param projectIdPara 项目id
     * @return String 返回用户是否关注了该项目的结果
     */
    @PostMapping("/user/if-has-stared")
    public String ifHasStared(@RequestParam("token") String tokenStr,
                              @RequestParam("projectId") Integer projectIdPara) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr);
        Integer projectId = projectIdPara;
        UserStar userStar = userStarRepository.findByUserIdAndProjectId(userId, projectId);
        if (userStar != null) {
            return "{\"ok\":\"true\"}";
        } else {
            return "{\"ok\":\"false\"}";
        }
    }


    /**
     * 返回用户关注项目(userstar)，捐助过的项目(userproject)，发布的项目(project)
     *
     * @param tokenStr token
     * @return String  返回用户关注项目(userstar)，捐助过的项目(userproject)，发布的项目(project)
     */
    @PostMapping("/user/get-related-projects")
    public String getRelatedProjects(@RequestParam("token") String tokenStr) {
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr);
        // user star
        List<UserStar> userStarList = userStarRepository.findByUserId(userId);
        // user project
        List<UserProject> userProjectList = userProjectRepository.findByUserId(userId);
        // 发布的项目 project
        List<Project> projectList = projectRepository.findByUserId(userId);

        JSONObject resultJsonObject = new JSONObject();
        JSONArray userStarJsonArray = new JSONArray();
        JSONArray userProjectJsonArray = new JSONArray();
        JSONArray projectJsonArray = new JSONArray();

        // user star
        if (userStarList == null || userStarList.size() == 0) {
            resultJsonObject.put("userStar", "null");
        } else {
            for (UserStar userStar : userStarList) {
                JSONObject userStarJsonObject = new JSONObject();
                Integer projectId = userStar.getProjectId();
                Project project = projectRepository.findById(projectId);

                userStarJsonObject.put("id", project.getId());
                userStarJsonObject.put("href", "/project/" + project.getId());
                userStarJsonObject.put("projectName", project.getProjectName());
                userStarJsonObject.put("initiatorName", project.getInitiatorName());
                userStarJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
                userStarJsonObject.put("description", project.getDescription());
                userStarJsonObject.put("targetMoney", project.getTargetMoney());
                userStarJsonObject.put("currentMoney", project.getCurrentMoney());
                userStarJsonObject.put("detail", project.getDetail());
                userStarJsonObject.put("imgListStr", project.getImgListStr());
                userStarJsonObject.put("userId", project.getUserId());
                userStarJsonObject.put("over", project.getOver());
                userStarJsonObject.put("startTime", project.getStartDate());
                //获取已经捐赠的人数
                userStarJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

                userStarJsonArray.put(userStarJsonObject);
            }

            resultJsonObject.put("userStar", userStarJsonArray);
        }

        // user project
        if(userProjectList == null || userProjectList.size() == 0){
            resultJsonObject.put("userProject", "null");
        }else{
            List<Integer> projectIdList = new ArrayList<>();
            for(UserProject userProject:userProjectList){
                JSONObject userProjectJsonObject = new JSONObject();
                Integer projectId = userProject.getProjectId();
                if(projectIdList.contains(projectId)){
                    continue;
                }else {
                    projectIdList.add(projectId);
                }

                Project project = projectRepository.findById(projectId);

                userProjectJsonObject.put("id", project.getId());
                userProjectJsonObject.put("href", "/project/" + project.getId());
                userProjectJsonObject.put("projectName", project.getProjectName());
                userProjectJsonObject.put("initiatorName", project.getInitiatorName());
                userProjectJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
                userProjectJsonObject.put("description", project.getDescription());
                userProjectJsonObject.put("targetMoney", project.getTargetMoney());
                userProjectJsonObject.put("currentMoney", project.getCurrentMoney());
                userProjectJsonObject.put("detail", project.getDetail());
                userProjectJsonObject.put("imgListStr", project.getImgListStr());
                userProjectJsonObject.put("userId", project.getUserId());
                userProjectJsonObject.put("over", project.getOver());
                userProjectJsonObject.put("startTime", project.getStartDate());
                //获取已经捐赠的人数
                userProjectJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

                userProjectJsonArray.put(userProjectJsonObject);
            }

            resultJsonObject.put("userProject", userProjectJsonArray);
        }

        // project
        if(projectList == null || projectList.size() == 0){
            resultJsonObject.put("projectList", "null");
        }else{
            for(Project project:projectList){
                JSONObject projectJsonObject = new JSONObject();

                projectJsonObject.put("id", project.getId());
                projectJsonObject.put("href", "/project/" + project.getId());
                projectJsonObject.put("projectName", project.getProjectName());
                projectJsonObject.put("initiatorName", project.getInitiatorName());
                projectJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
                projectJsonObject.put("description", project.getDescription());
                projectJsonObject.put("targetMoney", project.getTargetMoney());
                projectJsonObject.put("currentMoney", project.getCurrentMoney());
                projectJsonObject.put("detail", project.getDetail());
                projectJsonObject.put("imgListStr", project.getImgListStr());
                projectJsonObject.put("userId", project.getUserId());
                projectJsonObject.put("over", project.getOver());
                projectJsonObject.put("startTime", project.getStartDate());
                //获取已经捐赠的人数
                projectJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

                projectJsonArray.put(projectJsonObject);
            }

            resultJsonObject.put("project", projectJsonArray);
        }

        resultJsonObject.put("ok","true");

        return resultJsonObject.toString();
    }


    /**
     * 获取单个项目信息
     *
     * */
    @PostMapping("/user/get-project")
    public String getProject(@RequestParam("token") String tokenStr,
                             @RequestParam("projectId") Integer projectIdPara){
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer projectId = projectIdPara;
        Project project = projectRepository.findById(projectId);
        if(project == null){
            return "{\"ok\":\"false\",\"reason\":\"该项目不存在\"}";
        }else{
            JSONObject resultJsonObject = new JSONObject();
            JSONObject projectJsonObject = new JSONObject();

            projectJsonObject.put("id", project.getId());
            projectJsonObject.put("href", "/project/" + project.getId());
            projectJsonObject.put("projectName", project.getProjectName());
            projectJsonObject.put("initiatorName", project.getInitiatorName());
            projectJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
            projectJsonObject.put("description", project.getDescription());
            projectJsonObject.put("targetMoney", project.getTargetMoney());
            projectJsonObject.put("currentMoney", project.getCurrentMoney());
            projectJsonObject.put("detail", project.getDetail());
            projectJsonObject.put("imgListStr", project.getImgListStr());
            projectJsonObject.put("userId", project.getUserId());
            projectJsonObject.put("over", project.getOver());
            projectJsonObject.put("startTime", project.getStartDate());
            //获取已经捐赠的人数
            projectJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

            resultJsonObject.put("ok","true");
            resultJsonObject.put("project", projectJsonObject);
            return resultJsonObject.toString();
        }
    }


    /**
     * 充值
     *
     * */
    @PostMapping("/user/deposit")
    public String deposit(@RequestParam("token") String tokenStr,
                          @RequestParam("money") double moneyPara){
        double money = moneyPara;
        if(money <= 0){
            return "{\"ok\":\"false\", \"reason\":\"充值的金额应该大于0\"}";
        }

        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }

        Integer userId = tokenUtil.getUserId(tokenStr);
        User user = userRepository.findById(userId);
        double currBalance = user.getBalance();
        double newBalance = currBalance + money;

        user.setBalance(newBalance);
        userRepository.save(user);

        return "{\"ok\":\"true\"}";

    }


    /**
     * 返回发布的项目
     *
     * */
    @PostMapping("/get-published-projects")
    public String getPublishedProjects(@RequestParam("token") String tokenStr){
        if (tokenStr == null) {
            return "{\"ok\":\"false\",\"reason\":\"您还未登录\"}";
        } else if (!tokenUtil.checkToken(tokenStr)) {  // 返回false表示已经过期
            return "{\"ok\":\"false\", \"reason\":\"您的Token已过期,请重新登录\"}";
        }
        Integer userId = tokenUtil.getUserId(tokenStr);
        List<Project> projectList= projectRepository.findByUserId(userId);
        if(projectList == null){
            return "{\"ok\":\"false\", \"reason\":\"您还没有发布过任何项目\"}";
        }
        JSONObject resultJsonObject = new JSONObject();
        JSONArray projectsJsonArray = new JSONArray();
        for(Project project: projectList){
            JSONObject projectsJsonObject = new JSONObject();

            projectsJsonObject.put("id", project.getId());
            projectsJsonObject.put("href", "/project/" + project.getId());
            projectsJsonObject.put("projectName", project.getProjectName());
            projectsJsonObject.put("initiatorName", project.getInitiatorName());
            projectsJsonObject.put("img", "/img/project/" + project.getImg() + ".jpg");
            projectsJsonObject.put("description", project.getDescription());
            projectsJsonObject.put("targetMoney", project.getTargetMoney());
            projectsJsonObject.put("currentMoney", project.getCurrentMoney());
            projectsJsonObject.put("detail", project.getDetail());
            projectsJsonObject.put("imgListStr", project.getImgListStr());
            projectsJsonObject.put("userId", project.getUserId());
            projectsJsonObject.put("over", project.getOver());
            projectsJsonObject.put("startTime", project.getStartDate());
            //获取已经捐赠的人数
            projectsJsonObject.put("peopleNumber", "" + userProjectService.getNumberByProjectId(project.getId()));

            projectsJsonArray.put(projectsJsonObject);
        }
        resultJsonObject.put("projects", projectsJsonArray);
        resultJsonObject.put("ok","true");

        return resultJsonObject.toString();
    }


    /**
     * 测试图片上传
     *
     * */
    /*@PostMapping("/user/pic-upload-test")
    public String picUpload(@RequestParam("name") String picName,
                            @RequestParam("pic") MultipartFile multipartFilePara){
        MultipartFile multipartFile = multipartFilePara;
        if(!multipartFile.isEmpty()){
            try{
                // D:\\p\\demo\\src\\main\\resources\\static\\img\\test\\
                // C:\\project\\insurance\\img\\test\\
                String filePath = "\\img\\test\\";
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath + picName + ".jpg")));//保存图片到目录下
                out.write(multipartFile.getBytes());
                out.flush();
                out.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "{\"ok\":\"false\"}";
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"ok\":\"false\"}";
            }
        }


        return "{\"ok\":\"true\"}";
    }

*/


}










