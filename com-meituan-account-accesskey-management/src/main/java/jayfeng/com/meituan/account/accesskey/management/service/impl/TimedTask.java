package jayfeng.com.meituan.account.accesskey.management.service.impl;

import jayfeng.com.meituan.account.accesskey.management.bean.User;
import jayfeng.com.meituan.account.accesskey.management.dao.user.UserDao;
import jayfeng.com.meituan.account.accesskey.management.handler.rabbitmq.SendMessageHandler;
import jayfeng.com.meituan.account.accesskey.management.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时任务
 * @author JayFeng
 * @date 2021/4/6
 */
@Slf4j
@Component
@EnableScheduling
public class TimedTask {

    private static final Long FOURTEEN_DAY_MS = 3600 * 24 * 1000L;

    @Autowired
    private SendMessageHandler sendMessageHandler;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    /**
     * 深夜 3 点执行
     * 查询已经注销 14 天的用户
     * 若有 rabbitMQ发布删除消息
     * 若无 不做处理
     */
    @Scheduled(cron = "0 0 3 * * ?")
    private void timedDeleteUser() {
        List<User> invalidUserList = userDao.selectInvalidUsersToDelete(System.currentTimeMillis() - FOURTEEN_DAY_MS);
        if (invalidUserList.isEmpty()) {
            log.info("timedDeleteUser 没有注销超过 14 天的用户, 无需发布删除用户的消息");
            return;
        }
        log.info("timedDeleteUser 定时查出注销超过 14 天的用户 size: {}", invalidUserList.size());
        Set<Integer> userIdSet = new HashSet<>();
        for (User user : invalidUserList) {
            userIdSet.add(user.getId());
        }
        sendMessageHandler.sendDeleteUsersMessage(userIdSet);
    }

    /**
     * 深夜 2 点执行
     * 删除 redis 缓存中所有的管理员 UUID
     */
    @Scheduled(cron = "0 0 2 * * ?")
    private void timedDeleteAllManagerUUID() {
        log.info("timedDeleteAllManagerUUID 夜间定时任务执行, 删除 redis 缓存中所有的管理员UUID");
        redisService.deleteAllManagerUUID();
    }

}
