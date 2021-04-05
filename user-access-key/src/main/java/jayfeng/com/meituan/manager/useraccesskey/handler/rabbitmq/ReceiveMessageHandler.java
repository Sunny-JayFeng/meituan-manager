package jayfeng.com.meituan.manager.useraccesskey.handler.rabbitmq;

import jayfeng.com.meituan.manager.useraccesskey.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收处理
 * @author JayFeng
 * @date 2021/4/5
 */
@Component
public class ReceiveMessageHandler {

//    /**
//     * 监听短信密钥更新消息
//     * @param message 消息
//     */
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue, // 创建临时队列
//                    exchange = @Exchange(value = RabbitMQConstant.UPDATE_ACCESS_KEY_MSG_EXCHANGE, type = "fanout")
//            )
//    })
//    public void receiveUpdateAccessKeyMsg(String message) {
//        System.out.println("消息接收1");
//        System.out.println(message);
//        System.out.println();
//    }
//
//    /**
//     * 监听短信密钥更新消息
//     * @param message 消息
//     */
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue, // 创建临时队列
//                    exchange = @Exchange(value = RabbitMQConstant.UPDATE_ACCESS_KEY_MSG_EXCHANGE, type = "fanout")
//            )
//    })
//    public void receiveUpdateAccessKeyMsg2(String message) {
//        System.out.println("消息接收2");
//        System.out.println(message);
//        System.out.println();
//    }

}
