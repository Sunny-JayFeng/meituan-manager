package jayfeng.com.meituan.manager.useraccesskey.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * redis 操作
 * @author JayFeng
 * @date 2020/4/2
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisOperate redisOperate;

    /**
     * 获取某个 map 中的某个键值对的值
     * @param redisKey map key
     * @param sessionId 键
     * @return 值
     */
    public Object getUserJSON(String redisKey, String sessionId) {
        Object userObj = redisOperate.getValueForHash(redisKey, sessionId);
        log.info("getUserJSON 从 redis 缓存中获取用户 userObj: {}", userObj);
        return userObj;
    }

    /**
     * 存入 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 存入的 key
     */
    public void addUUID(String redisKey, String sessionId, String objectStr) {
        log.info("addUUID 向redis缓存中添加一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.setForHash(redisKey, sessionId, objectStr);
    }

    /**
     * 退出登录
     * 删除 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 删除的 key
     */
    public void deleteUUID(String redisKey, String sessionId) {
        log.info("deleteUUID 从redis缓存中删除一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.removeForHash(redisKey, sessionId);
    }

}
