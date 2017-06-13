package com.pwrd.dao.redis;

import com.alibaba.fastjson.JSONObject;
import com.pwrd.support.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ylwys
 * Date: 2017/1/10
 * Time: 10:52
 */

/**
 * redis主从集群功能包括
 * 1.db保存数据
 */
@PropertySource(value = "classpath:/application.properties")
@Repository
public class RedisClusterDao {

    @Value("${spring.redis.cluster.master.node.ports}")
    private String clusterMasterPorts;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 清空redis集群数据库(只需要清空master端口，slave端口自动清空)
     */
    public void clear() {
        String[] masterPorts = clusterMasterPorts.split(",");
        jedisCluster.getClusterNodes().forEach((k, v) -> {
            for (String masterPort : masterPorts) {
                if (k.contains(":" + masterPort)) {
                    v.getResource().flushDB();
                    break;
                }
            }
        });
    }

    //--------------------------ObjectTemplate---------------------

    /**
     * 保存redis对象
     *
     * @param nodeKey
     */
    public void saveObj(String nodeKey, Object obj) {
        jedisCluster.set(nodeKey, Utils.jsonString(obj));
    }

    /**
     * 获得redis对象
     *
     * @param nodeKey
     * @return
     */
    public <T> T getObj(String nodeKey, Class<T> tClass) {
        return JSONObject.parseObject(jedisCluster.get(nodeKey), tClass);
    }

    /**
     * 删除redis对象
     *
     * @param nodeKey
     */
    public boolean delObj(String nodeKey) {
        if (jedisCluster.exists(nodeKey)) {
            jedisCluster.del(nodeKey);
            return true;
        }
        return false;
    }


    //--------------------------StringTemplate----------------------

    public boolean removeNodeKey(String nodeKey) {
        if (jedisCluster.exists(nodeKey)) {
            jedisCluster.del(nodeKey);
            return true;
        }
        return false;
    }

    public void forListLeftPush(String nodeKey, String value) {
        jedisCluster.lpush(nodeKey, value);
    }

    public String forListLeftPop(String nodeKey) {
        return jedisCluster.lpop(nodeKey);
    }

    public void forListRightPush(String nodeKey, String value) {
        jedisCluster.rpush(nodeKey, value);
    }

    public String forListRightPop(String nodeKey) {
        return jedisCluster.rpop(nodeKey);
    }

    public long forListSize(String nodeKey) {
        return jedisCluster.llen(nodeKey);
    }

    public List<String> forListGet(String nodeKey, int start, int end) {
        return jedisCluster.lrange(nodeKey, start, end);
    }

    public long forListRemove(String nodeKey, long count, String removeValue) {
        return jedisCluster.lrem(nodeKey, count, removeValue);
    }

    public void forZSetPutWithScore(String nodeKey, String value, double score) {
        jedisCluster.zadd(nodeKey, score, value);
    }

    public long forZSetRemove(String nodeKey, String value) {
        return jedisCluster.zrem(nodeKey, value);
    }

    public List<String> forZSetGetRankAsc(String nodeKey, int start, int end) {
        return new ArrayList(jedisCluster.zrange(nodeKey, start, end));
    }

    public List<String> forZSetGetRankDesc(String nodeKey, int start, int end) {
        return new ArrayList<>(jedisCluster.zrevrange(nodeKey, start, end));
    }

    public List<Tuple> forZSetGetRankDescWithScore(String nodeKey, int start, int end) {
        return new ArrayList<>(jedisCluster.zrevrangeWithScores(nodeKey, start, end));
    }
}
