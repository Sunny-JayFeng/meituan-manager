package jayfeng.com.meituan.manager.useraccesskey.handler.rabbitmq;

import jayfeng.com.meituan.manager.useraccesskey.constant.RabbitMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendUpdateAccessKeyMessage() {
        log.info("sendUpdateAccessKeyMessage 密钥数据有更新，发布更新消息");
        rabbitTemplate.convertAndSend(RabbitMQConstant.UPDATE_ACCESS_KEY_MSG_EXCHANGE, "", "更新密钥");
    }

}
