package jayfeng.com.meituan.manager.useraccesskey.util;

import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求线程管理
 * @author JayFeng
 * @date 2021/4/7
 */
@Slf4j
@Component
public class RequestThreadLocalManagement {

    // 管理端为内部使用, 并发不会很高
    private Map<Long, ThreadLocal<Long>> requestUseTimeThreadLocalMap = new HashMap<>(512);

    private Map<Long, ThreadLocal<Manager>> requesterThreadLocalMap = new HashMap<>(512);

    /**
     * 存入请求到达时间
     */
    public void requestStart() {
        ThreadLocal<Long> requestStartThreadLocal = new ThreadLocal<>();
        requestStartThreadLocal.set(System.currentTimeMillis());
        requestUseTimeThreadLocalMap.put(Thread.currentThread().getId(), requestStartThreadLocal);
    }

    /**
     * 请求耗时
     * @return 返回请求耗时
     */
    public Long getRequestUseTime() {
        ThreadLocal<Long> requestStartThreadLocal = requestUseTimeThreadLocalMap.get(Thread.currentThread().getId());
        return System.currentTimeMillis() - requestStartThreadLocal.get();
    }

    /**
     * 存入发起请求的管理员
     * @param manager 管理员
     */
    public void setRequester(Manager manager) {
        ThreadLocal<Manager> requesterThreadLocal = new ThreadLocal<>();
        requesterThreadLocal.set(manager);
        requesterThreadLocalMap.put(Thread.currentThread().getId(), requesterThreadLocal);
    }

    /**
     * 获取发起请求的管理员
     * @return 返回发起请求的管理员
     */
    public Manager getRequester() {
        return requesterThreadLocalMap.get(Thread.currentThread().getId()).get();
    }

    /**
     * 获取发起请求的管理的账号名
     * @return 返回
     */
    public String getRequesterName() {
        return requesterThreadLocalMap.get(Thread.currentThread().getId()).get().getManagerName();
    }

}
