package com.pwrd.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwrd.model.User;
import com.pwrd.service.UserService;
import com.pwrd.support.IdCounter;
import com.pwrd.support.IdCounterNameEnum;
import com.pwrd.support.PageKey;
import com.pwrd.support.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 15:27
 */

@Controller
@EnableAutoConfiguration
@RequestMapping("/example") //***********idea运行调试把注解打开
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private IdCounter idCounter;

    //-------------------mysql测试----------------------

    @RequestMapping("/create")
    @ResponseBody
    public String create(HttpServletRequest request) {
        return userService.create(request);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(HttpServletRequest request) {
        return userService.update(request);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        return userService.delete(request);
    }

    @RequestMapping("/fineByAccount")
    @ResponseBody
    public String fineByAccount(HttpServletRequest request) {
        return userService.fineByAccount(request);
    }

    @RequestMapping("/defineSql")
    @ResponseBody
    public String defineSql(HttpServletRequest request) {
        return userService.defineSql(request);
    }

    @RequestMapping("/defineSql1")
    @ResponseBody
    public String defineSql1(HttpServletRequest request) {
        return userService.defineSql1(request);
    }

    @RequestMapping("/defineSql2")
    @ResponseBody
    public String defineSql2(HttpServletRequest request) {
        return userService.defineSql2(request);
    }

    //-------------------------redis测试-----------------------
    @RequestMapping("/redisTest")
    @ResponseBody
    public String redisTest(HttpServletRequest request) {
        return "aaa";
    }

    //------------------------redis + mysql 缓存测试---------------
    @RequestMapping("/redisCacheTest/test")
    @ResponseBody
    public String redisCacheTest(HttpServletRequest request, long id) {
        return userService.cacheGetUser(id);
    }

    //------------------------网页测试-------------------------

    @RequestMapping("/test")
//    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response, String a) {
//        String a1 = request.getParameter("a");//这样也行
//        System.out.println(idCounter.getIncreaseId(IdCounterNameEnum.USER));
//        System.out.println(idCounter.getIncreaseId(IdCounterNameEnum.USER));
//        System.out.println(idCounter.getIncreaseId(IdCounterNameEnum.USER));
//        System.out.println(idCounter.getIncreaseId(IdCounterNameEnum.USER));
//        System.out.println(idCounter.getIncreaseId(IdCounterNameEnum.USER));


        response.setHeader("content-type", "text/plain;charset=UTF-8");
        try (OutputStream outputStream = response.getOutputStream()) {
//            JSONObject resultObj = new JSONObject();
//            resultObj.put("resultCode", resultCode);
//            resultObj.put("resultId", resultId);
//            resultObj.put("resultMsg", resultMsg);
//            if (jsonArray != null && jsonArray.size() > 0) {
//                resultObj.put("resultData", jsonArray);
//            }

            String returnStr = "阿斯蒂芬";
            byte[] dataByteArr = returnStr.getBytes("UTF-8");
            outputStream.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(redisStringDao.forValueGet("aaaa"));


//        return "hello11111";
    }

    @RequestMapping("/findAllUser")
    public void findAllUser(HttpServletRequest request, HttpServletResponse response) {
        int page = Integer.parseInt(request.getParameter("page"));// 页码
        int pageSize = Integer.parseInt(request.getParameter("rows"));// 每行显示数

        List<User> userList = userService.findUserPage(page - 1, pageSize);
        int totalUserCount = userService.getAllUserCount();
        Map<String, Object> retMap = new HashMap();
        retMap.put("total", totalUserCount);
        retMap.put("rows", userList);
        Utils.commonOut(response, retMap);
    }

    @RequestMapping("/addUser")
    public void addUser(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        int level = Integer.parseInt(request.getParameter("level"));
        long userId = idCounter.getIncreaseId(IdCounterNameEnum.USER_ID_COUNTER);

        User user = new User(account, password, level);
        user.setId(userId);
        userService.addUser(user);

        Map<String, Object> retMap = new HashMap();
        retMap.put("msg", "成功");
        retMap.put("success", true);
        Utils.commonOut(response, retMap);
    }

    @RequestMapping("/updateUser")
    public void updateUser(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        int level = Integer.parseInt(request.getParameter("level"));

        boolean result = userService.updateUser(id, account, password, level);
        Map<String, Object> retMap = new HashMap();
        retMap.put("msg", "成功");
        retMap.put("success", result);
        Utils.commonOut(response, retMap);
    }

    @RequestMapping("/delUser")
    public void delUser(HttpServletRequest request, HttpServletResponse response) {
        long id = Long.parseLong(request.getParameter("id"));
        userService.delUser(id);

        Map<String, Object> retMap = new HashMap();
        retMap.put("msg", "成功");
        retMap.put("success", true);
        Utils.commonOut(response, retMap);
    }


    @RequestMapping("/")
    public String index(HttpServletRequest request, Map<String, Object> model) {
        model.put("test", "aaaaaaa");
        return PageKey.TEST.getPageName();
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request) {
        userService.login(request);
    }


}
