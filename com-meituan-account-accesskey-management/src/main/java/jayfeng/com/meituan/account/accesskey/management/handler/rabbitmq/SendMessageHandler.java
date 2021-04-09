package jayfeng.com.meituan.account.accesskey.management.handler.rabbitmq;

import jayfeng.com.meituan.account.accesskey.management.constant.RabbitMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 发送消息
 * @author JayFeng
 * @date 2021/4/5
 */
@Slf4j
@Component
@EnableScheduling
public class SendMessageHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 密钥数据更新, 发布消息
     */
    public void sendUpdateAccessKeyMessage() {
        log.info("sendUpdateAccessKeyMessage 密钥数据有更新，发布更新消息");
        rabbitTemplate.convertAndSend(RabbitMQConstant.UPDATE_ACCESS_KEY_MSG_EXCHANGE, "", "更新密钥");
    }

    /**
     * 发布消息, 删除已经注销14天的用户
     * @param userIdSet 用户 id 列表
     */
    public void sendDeleteUsersMessage(Set<Integer> userIdSet) {
        log.info("sendDeleteUsersMessage 发布消息, 删除注销超过 14 天的用户 userIdSet: {}", userIdSet);
        rabbitTemplate.convertAndSend(RabbitMQConstant.DELETE_USER_MESSAGE_QUEUE, userIdSet);
    }

    /**
     * 发布消息, 删除已经注销 14 天的商家
     * @param sellerIdSet 商家 id 列表
     */
    public void sendDeleteSellersMessage(Set<Integer> sellerIdSet) {
        log.info("sendDeleteSellersMessage 发布消息, 删除注销超过 14 天的商家 sellerIdSet: {}", sellerIdSet);
        rabbitTemplate.convertAndSend(RabbitMQConstant.DELETE_SELLER_MESSAGE_QUEUE, sellerIdSet);
    }

}
