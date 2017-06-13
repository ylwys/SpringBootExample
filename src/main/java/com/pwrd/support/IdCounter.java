package com.pwrd.support;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.EnumMap;
import java.util.Map;

/**
 * User: ylwys
 * Date: 2016/12/20
 * Time: 19:25
 */

public class IdCounter {

    private Map<IdCounterNameEnum, RedisAtomicLong> idCounterMap = new EnumMap<IdCounterNameEnum, RedisAtomicLong>(IdCounterNameEnum.class);

    public void init(RedisTemplate redisTemplate) {
        for (IdCounterNameEnum idCounterNameEnum : IdCounterNameEnum.values()) {
            idCounterMap.put(idCounterNameEnum, new RedisAtomicLong(idCounterNameEnum.name(), redisTemplate.getConnectionFactory()));
        }
    }

    public long getIncreaseId(IdCounterNameEnum idCounterName) {
        if (idCounterMap.containsKey(idCounterName)) {
            RedisAtomicLong redisAtomicLong = idCounterMap.get(idCounterName);
            return redisAtomicLong.incrementAndGet();
        } else {
            return -1;
        }
    }


}
