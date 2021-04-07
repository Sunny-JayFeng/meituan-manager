package jayfeng.com.meituan.manager.useraccesskey.service;

import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 管理员业务逻辑 -- 接口
 * @author JayFeng
 * @date 2021/4/7
 */
@Service
public interface ManagerService {

    /**
     * 获取已登录管理员对象
     * @param request request
     * @return 返回对象
     */
    ResponseData findManager(HttpServletRequest request);

    /**
     * 获取当前登录的管理员
     * @param request 请求
     * @return 返回管理员对象
     */
    Manager getManager(HttpServletRequest request);

    /**
     * 管理员登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回数据
     */
    ResponseData managerLogin(Map<String, String> paramsMap, HttpServletResponse response);

    /**
     * 管理员退出登录
     * @param manager 管理员
     * @param request 请求
     * @param response 响应
     */
    ResponseData managerLogout(Manager manager, HttpServletRequest request, HttpServletResponse response);

}
