package jayfeng.com.meituan.account.accesskey.management.handler.rabbitmq;

import jayfeng.com.meituan.account.accesskey.management.constant.RabbitMQConstant;
import jayfeng.com.meituan.account.accesskey.management.service.SellerService;
import jayfeng.com.meituan.account.accesskey.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 消息接收处理
 * @author JayFeng
 * @date 2021/4/5
 */
@Slf4j
@Component
public class ReceiveMessageHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    /**
     * 监听删除用户消息
     * @param userIdSet 待删除对象 id 列表
     */
    @RabbitListener(queues = RabbitMQConstant.DELETE_USER_MESSAGE_QUEUE)
    public void receiveDeleteUsersMessage(Set<Integer> userIdSet) {
        log.info("receiveDeleteUsersMessage 接收到删除用户的消息 userIdSet: {}", userIdSet);
        userService.removeUserByIdList(userIdSet);
    }

    /**
     * 监听删除商家消息
     * @param sellerIdSet 待删除商家 id 列表
     */
    @RabbitListener(queues = RabbitMQConstant.DELETE_SELLER_MESSAGE_QUEUE)
    public void receiveDeleteSellersMessage(Set<Integer> sellerIdSet) {
        log.info("receiveDeleteSellersMessage 接收到删除商家的消息 sellerIdSet: {}", sellerIdSet);
        sellerService.removeSellerByIdList(sellerIdSet);
    }

}
