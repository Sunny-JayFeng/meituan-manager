package jayfeng.com.meituan.manager.useraccesskey.redis;

import jayfeng.com.meituan.manager.useraccesskey.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

}
