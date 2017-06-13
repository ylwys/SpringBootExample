package com.pwrd.dao.cache;

import com.pwrd.dao.mysql.UserDao;

import com.pwrd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * User: ylwys
 * Date: 2016/12/20
 * Time: 14:01
 */
@Repository
public class UserCacheDao {

    @Autowired
    private UserDao userDao;


    /**
     * 更新对象：1.redis缓存更新，2.mysql更新
     *
     * @param user
     */
    @CachePut(value = "user", keyGenerator = "wiselyKeyGenerator")
    public void saveUser(User user) {
        //1.redis自动保存redis缓存(我们代码不需要些东西)
        //2. 存入mysql数据
        userDao.save(user);
    }

    /**
     * 获取对象：1.缓存查找，2.数据库查找，3进入缓存
     *
     * @param id
     * @return
     */
    @Cacheable(value = "user", keyGenerator = "wiselyKeyGenerator")
    public String getUser(Long id) {
        //如果没有缓存就会调到这里从mysql中读取,之后又自动进入redis缓存
//        User user = userDao.findOne(id);
//        redisObjectDao.saveObj("user_" + id, user);
//        return user;
        return "aaaaaaa";
    }

    /**
     * 删除对象 : 1.缓存删除，2.数据库删除
     *
     * @param id
     */
    @CacheEvict(value = "user", keyGenerator = "wiselyKeyGenerator")
    public void deleteUser(Long id) {
        //1.自动删除redis缓存中的对象
        //2.删除mysql数据库
        userDao.delete(id);
    }

}
