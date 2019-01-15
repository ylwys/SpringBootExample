package com.pwrd.service;


import com.pwrd.dao.cache.UserCacheDao;
import com.pwrd.dao.mysql.ItemDao;
import com.pwrd.dao.mysql.UserDao;
import com.pwrd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * User: ylwys
 * Date: 2016/12/17
 * Time: 15:29
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCacheDao userCacheDao;

    @Autowired
    private ItemDao itemDao;


    //-----------------------mysql操作--------------------------------

    public String create(HttpServletRequest request) {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        int level = Integer.parseInt(request.getParameter("level"));

        User user = new User(account, password, level, 1);
        userDao.save(user);

        return "save success" + "new Id:" + user.getId();
    }

    public void addUser(User user) {
        userDao.save(user);
    }

    public boolean updateUser(long id, String account, String password, int level) {
        User user = userDao.findOne(id);
        if (user == null) {
            return false;
        }
        user.setAccount(account);
        user.setPassword(password);
        user.setLevel(level);
        userDao.save(user);
        return true;
    }

    public void delUser(long id) {
        User user = new User(id);
        userDao.delete(user);
    }

    public String update(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        int level = Integer.parseInt(request.getParameter("level"));

        User user = userDao.findOne(id);
        if (user != null) {
            user.setAccount(account);
            user.setPassword(password);
            user.setLevel(level);

            userDao.save(user);
            return "update success";
        }
        return "update fail";
    }

    public String delete(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));

        User user = new User(id);
        userDao.delete(user);

        return "delete success";
    }

    public List<User> findAllUser() {
        return (List<User>) userDao.findAll();
    }

    public int getAllUserCount() {
        return userDao.getAllUserCount();
    }


    public List<User> findUserPage(int page, int pageSize) {
        Pageable pageable = new PageRequest(page, pageSize);
        return userDao.queryUserPage(pageable);
    }


    public String fineByAccount(HttpServletRequest request) {
        String account = request.getParameter("account");
        List<User> userList = userDao.getByAccount(account);
        return userList.size() + "";
    }

    public String defineSql(HttpServletRequest request) {
        String account = request.getParameter("account");
        int count = userDao.defineSql(account);
        return count + "";
    }

    public String defineSql1(HttpServletRequest request) {
        String account = request.getParameter("account");

        int page = 0;//从0开始
        int perPageNum = 10;
        Pageable pageable = new PageRequest(page, perPageNum);

        List<User> userList = userDao.defineSql1(account, pageable);
        return userList.size() + "";
    }

    public String defineSql2(HttpServletRequest request) {
        String account = request.getParameter("account");
        userDao.defineSql2(account);
        return "";
    }

    //-----------------------mysql 配合 redis缓存----------------------------
    public String cacheGetUser(Long id) {
        return userCacheDao.getUser(id);
    }


    public void login(HttpServletRequest request) {
    }
}
