package jayfeng.com.meituan.manager.useraccesskey.handler.rabbitmq;

import jayfeng.com.meituan.manager.useraccesskey.constant.RabbitMQConstant;
import jayfeng.com.meituan.manager.useraccesskey.service.UserService;
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

    /**
     * 监听删除用户消息
     * @param userIdSet 待删除对象 id 列表
     */
    @RabbitListener(queues = RabbitMQConstant.DELETE_USER_MESSAGE_QUEUE)
    public void receiveDeleteUsersMessage(Set<Integer> userIdSet) {
        log.info("receiveDeleteUsersMessage 接收到删除用户的消息 userIdSet: {}", userIdSet);
        userService.removeUserByIdList(userIdSet);
    }

}
