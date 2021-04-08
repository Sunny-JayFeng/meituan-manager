package jayfeng.com.meituan.account.accesskey.management.redis;

import jayfeng.com.meituan.account.accesskey.management.constant.RedisConstant;
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
     * 存入当前登录的管理员允许访问的接口
     * @param redisKey 权限列表 map
     * @param managerName 管理员账号
     * @param apiListStr 允许访问的接口(父级，标注在 controller 上的路径)
     */
    public void addManagerApiList(String redisKey, String managerName, String apiListStr) {
        log.info("addManagerApiList 向 redis 缓存中存入当前管理员允许访问的接口 redisKey: {}, managerName: {}, apiListStr: {}", redisKey, managerName, apiListStr);
        redisOperate.setForHash(redisKey, managerName, apiListStr);
    }
    /**
     * 获取管理员 api 权限列表
     * @param redisKey 权限列表 map
     * @param managerName 管理员账号
     * @return 返回数据
     */
    public Object getManagerApiList(String redisKey, String managerName) {
        Object apiListObj = redisOperate.getValueForHash(redisKey, managerName);
        log.info("getManagerApiList 从 redis 缓存中获取管理员 api 权限列表");
        return apiListObj;
    }

    /**
     * 管理员退出登录, 删除允许访问的接口的列表
     * @param redisKey 权限列表 map
     * @param managerName 管理员账号
     */
    public void deleteApiList(String redisKey, String managerName) {
        log.info("deleteApiList 从 redis 缓存中删除 managerName: {} 允许访问的接口列表", managerName);
        redisOperate.removeForHash(redisKey, managerName);
    }

    /**
     * 存入 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 存入的 key
     */
    public void addManagerUUID(String redisKey, String sessionId, String objectStr) {
        log.info("addManagerUUID 向redis缓存中添加一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.setForHash(redisKey, sessionId, objectStr);
    }

    /**
     * 获取某个 map 中的某个键值对的值
     * @param redisKey map key
     * @param sessionId 键
     * @return 值
     */
    public Object getManagerJSON(String redisKey, String sessionId) {
        Object managerObj = redisOperate.getValueForHash(redisKey, sessionId);
        log.info("getManagerJSON 从 redis 缓存中获取管理员 managerObj: {}", managerObj);
        return managerObj;
    }

    /**
     * 退出登录
     * 删除 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 删除的 key
     */
    public void deleteManagerUUID(String redisKey, String sessionId) {
        log.info("deleteManagerUUID 从redis缓存中删除一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.removeForHash(redisKey, sessionId);
    }

    /**
     * 夜间删除 redis 缓存中所有的管理员 UUID
     */
    public void deleteAllManagerUUID() {
        log.info("deleteAllManagerUUID 删除 redis 缓存中所有的管理员 UUID");
        redisOperate.remove(RedisConstant.MANAGER_UUID_MAP.getRedisMapKey());
    }

}
