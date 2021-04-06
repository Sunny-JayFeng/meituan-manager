package jayfeng.com.meituan.manager.useraccesskey.interceptor;

import jayfeng.com.meituan.manager.useraccesskey.constant.CookieConstant;
import jayfeng.com.meituan.manager.useraccesskey.constant.RedisConstant;
import jayfeng.com.meituan.manager.useraccesskey.util.CookieManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员拦截器
 * @author JayFeng
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ManagerInterceptor implements HandlerInterceptor {

    @Autowired
    private CookieManagement cookieManagement;

    /**
     * 拦截请求，判断是否已登录
     * @param request 请求
     * @param response 响应
     * @param handler 拦截
     * @return 返回 true / false
     * @throws IOException
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        Object managerObj = cookieManagement.getLoginManager(request, CookieConstant.MANAGER_KEY.getCookieKey(), RedisConstant.MANAGER_UUID_MAP.getRedisMapKey());
        if (managerObj == null) {
            log.info("用户未登陆, 重定向到登录页面");
            response.sendRedirect("/meituan/manager/login.html");
        }
        return managerObj != null;
    }

}
