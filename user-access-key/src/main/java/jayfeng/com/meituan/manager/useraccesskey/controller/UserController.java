package jayfeng.com.meituan.manager.useraccesskey.controller;

import jayfeng.com.meituan.manager.useraccesskey.response.ResponseMessage;
import jayfeng.com.meituan.manager.useraccesskey.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 普通用户控制层
 * @author JayFeng
 * @date 2021/4/2
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/user_accessKey/manage_user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 更新用户是否有效
     * @param paramsMap 参数
     * 用户 id
     * 操作人 operator
     * 是否有效 isValid
     * @return 返回更新结果
     */
    @PutMapping("/updateUserIsValid")
    public ResponseMessage updateUserIsValid(@RequestBody Map<String, String> paramsMap) {
        log.info("updateUserIsValid 更新用户是否有效 paramsMap: {}", paramsMap);
        return requestSuccess(userService.updateUserIsValid(paramsMap));
    }

}