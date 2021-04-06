package jayfeng.com.meituan.manager.useraccesskey.service;

import com.google.gson.Gson;
import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import jayfeng.com.meituan.manager.useraccesskey.constant.CookieConstant;
import jayfeng.com.meituan.manager.useraccesskey.constant.RedisConstant;
import jayfeng.com.meituan.manager.useraccesskey.dao.manager.ManagerDao;
import jayfeng.com.meituan.manager.useraccesskey.exception.RequestForbiddenException;
import jayfeng.com.meituan.manager.useraccesskey.redis.RedisService;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import jayfeng.com.meituan.manager.useraccesskey.util.CookieManagement;
import jayfeng.com.meituan.manager.useraccesskey.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * 管理员业务逻辑
 * @author JayFeng
 * @date 2021/4/6
 */
@Slf4j
@Service
public class ManagerService {

    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private CookieManagement cookieManagement;
    @Autowired
    private EncryptUtil encryptUtil;
    @Autowired
    private RedisService redisService;

    private Gson gson = new Gson();

    /**
     * 获取已登录管理员对象
     * @param request request
     * @return 返回对象
     */
    public ResponseData findManager(HttpServletRequest request) {
        Object managerObj = cookieManagement.getLoginManager(request, CookieConstant.MANAGER_KEY.getCookieKey(), RedisConstant.MANAGER_UUID_MAP.getRedisMapKey());
        if (managerObj == null) return ResponseData.createSuccessResponseData("findUserInfo", null);
        Manager manager = gson.fromJson(managerObj.toString(), Manager.class);
        log.info("findManager 获取已登录管理员信息 manager: {}", manager);
        return ResponseData.createSuccessResponseData("findManagerInfo", manager);
    }

    /**
     * 管理员登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回数据
     */
    public ResponseData managerLogin(Map<String, String> paramsMap, HttpServletResponse response) {
        String managerName = paramsMap.get("managerName");
        String password = paramsMap.get("password");
        if (ObjectUtils.isEmpty(managerName) || ObjectUtils.isEmpty(password)) {
            log.info("managerLogin 管理员登录失败, 参数异常, 请求不是来自浏览器, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Manager manager = managerDao.selectManagerByName(managerName);
        if (encryptUtil.matches(password, manager.getPassword())) {
            log.info("managerLogin 管理员登录成功");
            managerSetCookie(response, manager);
            return ResponseData.createSuccessResponseData("managerLoginInfo", manager);
        } else {
            log.info("managerLogin 管理员登录失败, 账号或密码错误 managerName: {}, password: {}", managerName, password);
            return ResponseData.createFailResponseData("managerLoginInfo", false, "账号或密码错误", "manager_name_or_password_error");
        }
    }

    /**
     * 登录成功, 设置 cookie
     * @param response 响应
     * @param manager 管理员
     */
    private void managerSetCookie(HttpServletResponse response, Manager manager) {
        String cookieValue = UUID.randomUUID().toString();
        cookieManagement.setCookie(response, CookieConstant.MANAGER_KEY.getCookieKey(), cookieValue);
        redisService.addUUID(RedisConstant.MANAGER_UUID_MAP.getRedisMapKey(), cookieValue, gson.toJson(manager));
    }

    /**
     * 管理员退出登录
     * @param manager 管理员
     * @param request 请求
     * @param response 响应
     */
    public ResponseData managerLogout(Manager manager, HttpServletRequest request, HttpServletResponse response) {
        String managerUUID = cookieManagement.removeCookie(response, request.getCookies(), CookieConstant.MANAGER_KEY.getCookieKey());
        log.info("managerLogout 管理员登出成功, manager: {}, managerUUID: {}", manager, managerUUID);
        redisService.deleteUUID(RedisConstant.MANAGER_UUID_MAP.getRedisMapKey(), managerUUID);
        return ResponseData.createSuccessResponseData("managerLogoutInfo", true);
    }

}
