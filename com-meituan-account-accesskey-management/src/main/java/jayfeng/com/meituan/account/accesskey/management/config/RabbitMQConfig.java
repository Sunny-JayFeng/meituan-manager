package jayfeng.com.meituan.account.accesskey.management.config;

import jayfeng.com.meituan.account.accesskey.management.constant.RabbitMQConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 消息中间件 rabbitMQ 配置
 * @author JayFeng
 * @date 2021/4/5
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue deleteUsersQueue() {
        return new Queue(RabbitMQConstant.DELETE_USER_MESSAGE_QUEUE);
    }

    @Bean
    public Queue deleteSellersQueue() {
        return new Queue(RabbitMQConstant.DELETE_SELLER_MESSAGE_QUEUE);
    }
}
