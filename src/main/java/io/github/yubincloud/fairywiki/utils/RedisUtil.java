package io.github.yubincloud.fairywiki.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 检测 Redis 中的 key 是否重复，若不重复则存入该 key
     * @param second 过期时间（秒）
     * @return 重复则返回 false，若新增 key 则返回 true
     */
    public boolean validateRepeatedKey(String key, long second) {
        if (redisTemplate.hasKey(key)) {
            LOG.info("key已存在：{}", key);
            return false;
        } else {
            LOG.info("key 不存在，放入 key：{}，过期时间：{}秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}
