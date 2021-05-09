package jayfeng.com.meituan.account.accesskey.management.constant;

/**
 * rabbitMQ 常量
 * @author JayFeng
 * @date 2021/4/3
 */
public class RabbitMQConstant {

    private RabbitMQConstant(){}

    // 密钥更新消息通知交换机
    public static final String UPDATE_ACCESS_KEY_MSG_EXCHANGE = "update_access_key_msg_exchange";

    // 删除用户消息队列
    public static final String DELETE_USER_MESSAGE_QUEUE = "delete_users";

    // 删除商家消息队列
    public static final String DELETE_SELLER_MESSAGE_QUEUE = "delete_sellers";

    // 删除骑手消息队列
    public static final String DELETE_COURIER_MESSAGE_QUEUE = "delete_courier";
}
