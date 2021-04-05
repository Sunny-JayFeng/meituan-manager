package jayfeng.com.meituan.manager.useraccesskey.service;

import jayfeng.com.meituan.manager.useraccesskey.bean.User;
import jayfeng.com.meituan.manager.useraccesskey.dao.user.UserDao;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import jayfeng.com.meituan.manager.useraccesskey.util.PatternMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 普通用户业务层
 * @author JayFeng
 * @date 2021/4/2
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PatternMatch patternMatch;

    /**
     * 根据手机号获取用户
     * @param phone 手机号
     * @return 返回数据
     */
    public ResponseData getUserByPhone(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) {
            log.info("getUserByPhone 根据手机号获取用户失败, 手机号格式不正确, phone: {}", phone);
            return ResponseData.createFailResponseData("getUserByPhoneInfo", null, "手机号格式不正确", "phone_error");
        }
        User user = userDao.selectUserByUserPhone(phone);
        if (user == null) {
            log.info("getUserByPhone 根据手机号获取用户失败, 不存在此用户");
            return ResponseData.createFailResponseData("getUserByPhoneInfo", null, "用户不存在", "user_not_exists");
        }
        log.info("getUserByPhone 根据手机号获取用户成功, user: {}", user);
        return ResponseData.createSuccessResponseData("getUserByPhoneInfo", user);
    }


    /**
     * 根据用户 id 列表删除用户
     * 由 rabbitMQ 发送消息来让删除 todo
     * @param userIdSet 用户 id 列表
     */
    public void removeUserByIdList(Set<Integer> userIdSet) {
        log.info("removeUserByIdList 根据 id 集合删除用户 userIdSet: {}", userIdSet);
        String userIds = handleAppendUserIds(userIdSet);
        List<User> userList = userDao.selectUsersByUserIds(userIds);
        // 日志打印
        for (User user : userList) log.info("removeUserByIdList 待删除用户对象 user: {}", user);
        userDao.deleteUsesByIds(userIds);
        log.info("removeUserByIdList 批量删除用户成功");
    }

    /**
     * 处理用户 id 列表，拼接成字符串
     * @param userIdSet 用户 id 列表
     * @return 字符串形式的用户 id 列表
     */
    private String handleAppendUserIds(Set<Integer> userIdSet) {
        // 这个逻辑不会有线程安全问题
        StringBuilder strb = new StringBuilder();
        for (Integer userId : userIdSet) {
            strb.append(userId);
            strb.append(", ");
        }
        String userIds = strb.substring(0, strb.length() - 1);
        log.info("handleAppendUserIds 用户 id 拼接结果 userIds: {}", userIdSet);
        return userIds;
    }

    /**
     * 更新用户是否有效
     * @param paramsMap 参数
     * 用户 id
     * 操作人 operator
     * 是否有效 isValid
     * @return 返回更新结果
     */
    public ResponseData updateUserIsValid(Map<String, String> paramsMap) {
        String id = paramsMap.get("userId");
        String operator = paramsMap.get("operator");
        String isValid = paramsMap.get("isValid");
        if (ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(operator) || ObjectUtils.isEmpty(isValid)) {
            log.info("updateUserIsValid 更新用户是否有效失败，参数异常 id: {}, operator: {}, isValid: {}", id, operator, isValid);
            return ResponseData.createFailResponseData("updateUserIsValidInfo", false, "参数异常", "params_exception");
        }
        int userId = -1;
        int isValidValue = -1;
        try {
            userId = Integer.parseInt(id);
            isValidValue = Integer.parseInt(isValid);
        } catch (NumberFormatException e) {
            log.info("updateUserIsValid 更新用户是否有效失败, 参数异常 userId: {}", id);
            return ResponseData.createFailResponseData("updateUserIsValidInfo", false, "参数异常", "params_exception");
        }
        User user = userDao.selectUserByUserId(userId);
        if (user == null) {
            log.info("updateUserIsValid 更新用户是否有效失败, 用户不存在");
            return ResponseData.createFailResponseData("updateUserIsValidInfo", false, "参数异常", "params_exception");
        }
        log.info("cancelUserAccount 更新用户是否有效 user: {}, isValid: {}, operator: {}", user, isValid, operator);
        userDao.updateUserIsValid(userId, isValidValue);
        log.info("cancelUserAccount 更新用户是否有效成功 operator: {}", operator);
        return ResponseData.createSuccessResponseData("cancelUserAccountInfo", true);
    }

}
