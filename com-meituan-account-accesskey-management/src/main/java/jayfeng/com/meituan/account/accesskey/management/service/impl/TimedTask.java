package jayfeng.com.meituan.account.accesskey.management.service.impl;

import jayfeng.com.meituan.account.accesskey.management.bean.Courier;
import jayfeng.com.meituan.account.accesskey.management.bean.Seller;
import jayfeng.com.meituan.account.accesskey.management.bean.User;
import jayfeng.com.meituan.account.accesskey.management.dao.courier.CourierDao;
import jayfeng.com.meituan.account.accesskey.management.dao.seller.SellerDao;
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
    private SellerDao sellerDao;
    @Autowired
    private CourierDao courierDao;
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
            log.info("timedDeleteUser 定时查出注销超过 14 天的用户 size: {}", invalidUserList.size());
            Set<Integer> userIdSet = new HashSet<>();
            for (User user : invalidUserList) {
                userIdSet.add(user.getId());
            }
            sendMessageHandler.sendDeleteUsersMessage(userIdSet);
        }
    }

    /**
     * 深夜 3：30执行
     * 查询已经注销 14 天的商家
     * 若有 rabbitMQ发布删除消息
     * 若无 不做处理
     */
    @Scheduled(cron = "0 30 3 * * ?")
    private void timedDeleteSeller() {
        List<Seller> invalidSellerList = sellerDao.selectInvalidSellersToDelete(System.currentTimeMillis() - FOURTEEN_DAY_MS);
        if (!invalidSellerList.isEmpty()) {
            log.info("timedDeleteSeller 定时查出注销超过 14 天的用户 size: {}", invalidSellerList.size());
            Set<Integer> sellerIdSet = new HashSet<>();
            for (Seller seller : invalidSellerList) {
                sellerIdSet.add(seller.getId());
            }
            sendMessageHandler.sendDeleteSellersMessage(sellerIdSet);
        }
    }

    /**
     * 深夜 3：40执行
     * 查询已经注销 14 天的骑手
     * 若有 rabbitMQ 发布删除消息
     * 若无 不做处理
     */
    @Scheduled(cron = "0 40 3 * * ?")
    private void timedDeleteCourier() {
        List<Courier> invalidCourierList = courierDao.selectInValidCourierToDelete(System.currentTimeMillis());
        if (!invalidCourierList.isEmpty()) {
            log.info("timedDeleteCourier 定时查出注销超过 14 天的骑手 size: {}", invalidCourierList.size());
            Set<Integer> courierIdSet = new HashSet<>();
            for (Courier courier : invalidCourierList) {
                courierIdSet.add(courier.getId());
            }
            sendMessageHandler.sendDeleteCourierMessage(courierIdSet);
        }
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
