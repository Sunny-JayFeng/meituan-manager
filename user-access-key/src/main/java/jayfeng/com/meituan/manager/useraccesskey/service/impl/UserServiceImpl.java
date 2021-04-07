package jayfeng.com.meituan.manager.useraccesskey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.manager.useraccesskey.bean.User;
import jayfeng.com.meituan.manager.useraccesskey.dao.user.UserDao;
import jayfeng.com.meituan.manager.useraccesskey.exception.RequestForbiddenException;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import jayfeng.com.meituan.manager.useraccesskey.service.UserService;
import jayfeng.com.meituan.manager.useraccesskey.util.PatternMatch;
import jayfeng.com.meituan.manager.useraccesskey.util.RequestThreadLocalManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 普通用户业务层 -- 实现类
 * @author JayFeng
 * @date 2021/4/7
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RequestThreadLocalManagement requestThreadLocalManagement;

    /**
     * 动态条件分页查询用户信息
     * @param paramsMap 参数
     * @param findPage 分页
     * @return 返回用户信息
     */
    @Override
    public ResponseData findUsers(Map<String, String> paramsMap, Page<User> findPage) {
        String userName = paramsMap.get("userName"); // 用户名
        String phone = paramsMap.get("phone"); // 电话
        String email = paramsMap.get("email"); // 邮箱
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 用户名查询
        if (!ObjectUtils.isEmpty(userName)) queryWrapper.eq("user_name", userName);
        // 手机号查询
        if (!ObjectUtils.isEmpty(phone)) queryWrapper.eq("phone", phone);
        // 邮箱查询
        if (!ObjectUtils.isEmpty(email)) queryWrapper.eq("email", email);
        Page<User> dataPage = userDao.selectPage(findPage, queryWrapper);
        log.info("findUsers 用户数据查询成功 total: {}", dataPage.getTotal());
        return ResponseData.createSuccessResponseData("findUsersInfo", dataPage);
    }

    /**
     * 根据用户 id 列表删除用户
     * 由 rabbitMQ 发送消息来让删除
     * @param userIdSet 用户 id 列表
     */
    @Override
    @Transactional(transactionManager = "userAccountTransactionManager")
    public void removeUserByIdList(Set<Integer> userIdSet) {
        log.info("removeUserByIdList 根据 id 集合删除用户 userIdSet: {}", userIdSet);
        String userIds = handleAppendUserIds(userIdSet);
        List<User> userList = userDao.selectUsersByUserIds(userIds);
        if (userList.isEmpty()) {
            log.info("removeUserByIdList 对象集合为空");
            return;
        }
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
            strb.append(",");
        }
        String userIds = strb.substring(0, strb.length() - 1);
        log.info("handleAppendUserIds 用户 id 拼接结果 userIds: {}", userIdSet);
        return userIds;
    }

    /**
     * 更新用户是否有效
     * @param paramsMap 参数
     * 用户 id
     * 是否有效 isValid
     * @return 返回更新结果
     */
    @Override
    @Transactional(transactionManager = "userAccountTransactionManager")
    public ResponseData updateUserIsValid(Map<String, String> paramsMap) {
        String id = paramsMap.get("userId");
        String isValid = paramsMap.get("isValid");
        if (ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(isValid)) {
            log.info("updateUserIsValid 更新用户是否有效失败，参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        int userId = -1;
        int isValidValue = -1;
        try {
            userId = Integer.parseInt(id);
            isValidValue = Integer.parseInt(isValid);
        } catch (NumberFormatException e) {
            log.info("updateUserIsValid 更新用户是否有效失败，参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        User user = userDao.selectUserByUserId(userId);
        if (user == null) {
            log.info("updateUserIsValid 更新用户是否有效失败，参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        log.info("cancelUserAccount 更新用户是否有效 user: {}, isValid: {}", user, isValid);
        userDao.updateUserIsValid(userId, isValidValue, System.currentTimeMillis());
        log.info("cancelUserAccount 更新用户是否有效成功 操作人: {}", requestThreadLocalManagement.getRequesterName());
        return ResponseData.createSuccessResponseData("cancelUserAccountInfo", true);
    }

}
