package com.pwrd.support;


import com.pwrd.dao.redis.RedisNoClusterDao;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.concurrent.TimeUnit;

/**
 * User: ylwys
 * Date: 2017/1/16
 * Time: 11:40
 */

/**
 * redis同步锁(使用单一端口redis做)
 */
public class RedisLock {

    private RedisNoClusterDao redisNoClusterDao;

    private String lockKey;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int timeOut = 5 * 1000;


    public RedisLock(String lockKey) {
        this.redisNoClusterDao = (RedisNoClusterDao) SpringUtil.getBean("redisNoClusterDao");
        this.lockKey = lockKey + "_lock";
    }

    public void lock() {
        RedisTemplate redisTemplate = redisNoClusterDao.getRedisTemplate();
        while (true) {
            boolean temp = (boolean) redisTemplate.execute(new SessionCallback<Boolean>() {
                @Override
                public Boolean execute(RedisOperations redisOperations) throws DataAccessException {
                    do {
                        redisOperations.watch(lockKey);
                        //锁被占用了
                        if (redisNoClusterDao.haveNodeKey(lockKey)) {
                            redisOperations.unwatch();
                            return false;
                        }
                        //开启事物
                        redisOperations.multi();
                        redisNoClusterDao.forValuePutTimeOut(lockKey, "1", timeOut, TimeUnit.MILLISECONDS);
                    } while (redisOperations.exec() == null);
                    return true;
                }
            });

            //得到锁啦
            if (temp == true) {
                break;
            }
        }
    }

    public void unlock() {
        redisNoClusterDao.delObj(lockKey);
    }

}
