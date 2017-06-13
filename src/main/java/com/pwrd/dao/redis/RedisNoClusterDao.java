package com.pwrd.dao.redis;

/**
 * User: ylwys
 * Date: 2017/1/10
 * Time: 11:06
 */


import com.pwrd.model.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis单一端口操作功能包括
 * 1.redis缓存
 * 2.id生成器
 * 3.同步锁控制
 */
@Repository
public class RedisNoClusterDao {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //--------------------------ObjectTemplate---------------------

    /**
     * 保存redis对象
     *
     * @param nodeKey
     */
    public void saveObj(String nodeKey, Object obj) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(nodeKey, obj);
    }

    /**
     * 获得redis对象
     *
     * @param nodeKey
     * @return
     */
    public <T> T getObj(String nodeKey, Class<T> tClass) {
        ValueOperations<String, BaseModel> valueOperations = redisTemplate.opsForValue();
        return (T) valueOperations.get(nodeKey);
    }

    /**
     * 删除redis对象
     *
     * @param nodeKey
     */
    public boolean delObj(String nodeKey) {
        if (redisTemplate.hasKey(nodeKey)) {
            redisTemplate.delete(nodeKey);
            return true;
        }
        return false;
    }


    //--------------------------StringTemplate---------------------

    public void forValuePut(String nodeKey, String value, long timeOut, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(nodeKey, value, timeOut, timeUnit);
    }

    public void forValuePut(String nodeKey, String value) {
        stringRedisTemplate.opsForValue().set(nodeKey, value);
    }

    public void forValuePutTimeOut(String nodeKey, String value, long timeOut, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(nodeKey, value, timeOut, timeUnit);
    }


    public String forValueGet(String nodeKey) {
        return stringRedisTemplate.opsForValue().get(nodeKey);
    }

    public boolean removeNodeKey(String nodeKey) {
        if (stringRedisTemplate.hasKey(nodeKey)) {
            stringRedisTemplate.delete(nodeKey);
            return true;
        } else {
            return false;
        }
    }

    public boolean haveNodeKey(String nodeKey) {
        return stringRedisTemplate.hasKey(nodeKey);
    }

    public List<String> forValueMultiGet(List<String> nodeKeys) {
        return stringRedisTemplate.opsForValue().multiGet(nodeKeys);
    }

    public void forListLeftPush(String nodeKey, String value) {
        stringRedisTemplate.opsForList().leftPush(nodeKey, value);
    }

    public String forListLeftPop(String nodeKey) {
        return stringRedisTemplate.opsForList().leftPop(nodeKey);
    }

    public void forListRightPush(String nodeKey, String value) {
        stringRedisTemplate.opsForList().rightPush(nodeKey, value);
    }

    public String forListRightPop(String nodeKey) {
        return stringRedisTemplate.opsForList().rightPop(nodeKey);
    }

    public long forListSize(String nodeKey) {
        return stringRedisTemplate.opsForList().size(nodeKey);
    }

    public List<String> forListGet(String nodeKey, int start, int end) {
        return stringRedisTemplate.opsForList().range(nodeKey, start, end);
    }

    public String forListGetByIndex(String nodeKey, int index) {
        return stringRedisTemplate.opsForList().index(nodeKey, index);
    }

    public void forListSetOnIndex(String nodeKey, int index, String newValue) {
        stringRedisTemplate.opsForList().set(nodeKey, index, newValue);
    }

    public void forListTrim(String nodeKey, int start, int end) {
        stringRedisTemplate.opsForList().trim(nodeKey, start, end);
    }

    public long forListRemove(String nodeKey, long count, String removeValue) {
        return stringRedisTemplate.opsForList().remove(nodeKey, count, removeValue);
    }

    public Map<String, String> forMapGet(String nodeKey) {
        return stringRedisTemplate.<String, String>opsForHash().entries(nodeKey);
    }

    public void forMapPutAll(String nodeKey, Map<String, String> dataMap) {
        stringRedisTemplate.<String, String>opsForHash().putAll(nodeKey, dataMap);
    }

    public void forMapPut(String nodeKey, String mapKey, String mapValue) {
        stringRedisTemplate.<String, String>opsForHash().put(nodeKey, mapKey, mapValue);
    }

    public boolean forMapRemoveKey(String nodeKey, String removeKey) {
        stringRedisTemplate.<String, String>opsForHash().delete(nodeKey, removeKey);
        return true;
    }

    public long forMapSize(String nodeKey) {
        return stringRedisTemplate.<String, String>opsForHash().size(nodeKey);
    }

    public void forSetPut(String nodeKey, String value) {
        stringRedisTemplate.opsForSet().add(nodeKey, value);
    }

    public long forSetRemove(String nodeKey, String removeValue) {
        return stringRedisTemplate.opsForSet().remove(nodeKey, removeValue);
    }

    public Set<String> forSetGet(String nodeKey) {
        return stringRedisTemplate.opsForSet().members(nodeKey);
    }

    public void forZSetPutWithScore(String nodeKey, String value, double score) {
        stringRedisTemplate.opsForZSet().add(nodeKey, value, score);
    }

    public long forZSetRemove(String nodeKey, String value) {
        return stringRedisTemplate.opsForZSet().remove(nodeKey, value);
    }

    public void forZSetIncrementScore(String nodeKey, String value, double score) {
        stringRedisTemplate.opsForZSet().incrementScore(nodeKey, value, score);
    }

    public List<String> forZSetGetRankAsc(String nodeKey, int start, int end) {
        return new ArrayList(stringRedisTemplate.opsForZSet().range(nodeKey, start, end));
    }

    public List<String> forZSetGetRankDesc(String nodeKey, int start, int end) {
        return new ArrayList(stringRedisTemplate.opsForZSet().reverseRange(nodeKey, start, end));
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
