package jayfeng.com.meituan.account.accesskey.management.controller;

import jayfeng.com.meituan.account.accesskey.management.bean.Manager;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseMessage;
import jayfeng.com.meituan.account.accesskey.management.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 管理员控制层
 * @author JayFeng
 * @date 2021/4/6
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/login_logout")
public class ManagerController extends BaseController {

    @Autowired
    private ManagerService managerService;

    /**
     * 页面初始化，获取管理员信息
     * @param request 拿到 cookie，获取管理员信息
     * @return 返回数据
     */
    @GetMapping("/findManager")
    public ResponseMessage findManager(HttpServletRequest request) {
        log.info("findManager 页面初始化, 获取用户信息");
        return requestSuccess(managerService.findManager(request));
    }

    /**
     * 管理员登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回数据
     */
    @PostMapping("/managerLogin")
    public ResponseMessage managerLogin(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        log.info("managerLogin 管理员登录 paramsMap: {}", paramsMap);
        return requestSuccess(managerService.managerLogin(paramsMap, response));
    }

    /**
     * 管理员退出登录
     * @param manager 管理员
     * @param request 请求
     * @param response 响应
     * @return 返回数据
     */
    @PostMapping("/managerLogout")
    public ResponseMessage managerLogout(@RequestBody Manager manager, HttpServletRequest request, HttpServletResponse response) {
        log.info("managerLogout 管理员退出登录 manager: {}", manager);
        return requestSuccess(managerService.managerLogout(manager, request, response));
    }

}
