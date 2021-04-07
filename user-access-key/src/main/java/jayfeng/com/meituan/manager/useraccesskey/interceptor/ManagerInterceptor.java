package jayfeng.com.meituan.manager.useraccesskey.interceptor;

import com.google.gson.Gson;
import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import jayfeng.com.meituan.manager.useraccesskey.constant.CookieConstant;
import jayfeng.com.meituan.manager.useraccesskey.constant.RedisConstant;
import jayfeng.com.meituan.manager.useraccesskey.exception.RequestForbiddenException;
import jayfeng.com.meituan.manager.useraccesskey.redis.RedisService;
import jayfeng.com.meituan.manager.useraccesskey.service.ManagerService;
import jayfeng.com.meituan.manager.useraccesskey.util.CookieManagement;
import jayfeng.com.meituan.manager.useraccesskey.util.RequestThreadLocalManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 管理员拦截器
 * @author JayFeng
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ManagerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private RequestThreadLocalManagement requestThreadLocalManagement;

    private Gson gson = new Gson();

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
        Manager manager = managerService.getManager(request);
        if (manager == null) {
            log.info("用户未登陆, 重定向到登录页面");
            response.sendRedirect("/meituan/manager/login.html");
            return false;
        }
        String requestURI = request.getRequestURI();
        String managerName = manager.getManagerName();
        Object apiListObj = redisService.getManagerApiList(RedisConstant.API_MAP.getRedisMapKey(), managerName);
        if (apiListObj == null || !haveAuthority(requestURI, gson.fromJson(apiListObj.toString(), List.class))) {
            log.info("当前登录的管理员无权访问该接口, api: {}, managerName: {}", requestURI, managerName);
            log.info("当前登录的管理员允许访问的接口, apiList: {}", apiListObj);
            throw new RequestForbiddenException("您无权访问");
        }
        requestThreadLocalManagement.setRequester(manager);
        return true;
    }

    /**
     * 判断允许访问的 api 接口中是否包含当前请求的接口
     * @param requestURI 请求接口
     * @param apiList 允许访问的接口列表(父级，标注在 controller 上的路径)
     * @return 返回 true / false
     */
    private boolean haveAuthority(String requestURI, List<String> apiList) {
        for (String api : apiList) {
            if (requestURI.contains(api)) return true;
        }
        return false;
    }

}
