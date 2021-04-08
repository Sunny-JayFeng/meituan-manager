package jayfeng.com.meituan.account.accesskey.management.service.impl;

import com.google.gson.Gson;
import jayfeng.com.meituan.account.accesskey.management.bean.Manager;
import jayfeng.com.meituan.account.accesskey.management.constant.CookieConstant;
import jayfeng.com.meituan.account.accesskey.management.constant.RedisConstant;
import jayfeng.com.meituan.account.accesskey.management.dao.manager.ManagerApiDao;
import jayfeng.com.meituan.account.accesskey.management.dao.manager.ManagerDao;
import jayfeng.com.meituan.account.accesskey.management.exception.RequestForbiddenException;
import jayfeng.com.meituan.account.accesskey.management.redis.RedisService;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import jayfeng.com.meituan.account.accesskey.management.service.ManagerService;
import jayfeng.com.meituan.account.accesskey.management.util.CookieManagement;
import jayfeng.com.meituan.account.accesskey.management.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 管理员业务逻辑 -- 实现类
 * @author JayFeng
 * @date 2021/4/7
 */
@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private CookieManagement cookieManagement;
    @Autowired
    private EncryptUtil encryptUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ManagerApiDao managerApiDao;

    private Gson gson = new Gson();

    /**
     * 获取已登录管理员对象
     * @param request request
     * @return 返回对象
     */
    @Override
    public ResponseData findManager(HttpServletRequest request) {
        Manager manager = getManager(request);
        if (manager == null) return ResponseData.createSuccessResponseData("findUserInfo", null);
        log.info("findManager 获取已登录管理员信息 manager: {}", manager);
        return ResponseData.createSuccessResponseData("findManagerInfo", manager);
    }

    /**
     * 获取当前登录的管理员
     * @param request 请求
     * @return 返回管理员对象
     */
    @Override
    public Manager getManager(HttpServletRequest request) {
        Object managerObj = cookieManagement.getLoginManager(request, CookieConstant.MANAGER_KEY.getCookieKey(), RedisConstant.MANAGER_UUID_MAP.getRedisMapKey());
        if (managerObj == null) return null;
        return gson.fromJson(managerObj.toString(), Manager.class);
    }

    /**
     * 管理员登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回数据
     */
    @Override
    public ResponseData managerLogin(Map<String, String> paramsMap, HttpServletResponse response) {
        String managerName = paramsMap.get("managerName");
        String password = paramsMap.get("password");
        if (ObjectUtils.isEmpty(managerName) || ObjectUtils.isEmpty(password)) {
            log.info("managerLogin 管理员登录失败, 参数异常, 请求非法, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Manager manager = managerDao.selectManagerByName(managerName);
        if (encryptUtil.matches(password, manager.getPassword())) {
            log.info("managerLogin 管理员登录成功, 设置 cookie");
            managerSetCookie(response, manager);
            List<String> apiList = managerApiDao.selectApiListByManagerId(manager.getId());
            log.info("managerLogin 根据当前登录的管理员 managerId: {} 查询允许访问的接口 apiListSize: {}", manager.getId(), apiList.size());
            // 当前管理员允许访问的接口列表存进 redis
            redisService.addManagerApiList(RedisConstant.API_MAP.getRedisMapKey(), managerName, gson.toJson(apiList));
            manager.setPassword(null); // 密码不可见
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
        redisService.addManagerUUID(RedisConstant.MANAGER_UUID_MAP.getRedisMapKey(), cookieValue, gson.toJson(manager));
    }

    /**
     * 管理员退出登录
     * @param manager 管理员
     * @param request 请求
     * @param response 响应
     */
    @Override
    public ResponseData managerLogout(Manager manager, HttpServletRequest request, HttpServletResponse response) {
        String managerUUID = cookieManagement.removeCookie(response, request.getCookies(), CookieConstant.MANAGER_KEY.getCookieKey());
        log.info("managerLogout 管理员登出成功, manager: {}, managerUUID: {}", manager, managerUUID);
        redisService.deleteManagerUUID(RedisConstant.MANAGER_UUID_MAP.getRedisMapKey(), managerUUID);
        redisService.deleteApiList(RedisConstant.API_MAP.getRedisMapKey(), manager.getManagerName());
        return ResponseData.createSuccessResponseData("managerLogoutInfo", true);
    }

}
