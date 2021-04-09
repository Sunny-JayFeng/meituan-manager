package jayfeng.com.meituan.account.accesskey.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.account.accesskey.management.bean.User;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * 普通用户业务层 -- 接口
 * @author JayFeng
 * @date 2021/4/7
 */
@Service
public interface UserService {

    /**
     * 动态条件分页查询用户信息
     * @param paramsMap 参数
     * @param findPage 分页
     * @return 返回用户信息
     */
    ResponseData findUsers(Map<String, String> paramsMap, Page<User> findPage);

    /**
     * 根据用户 id 列表删除用户
     * 由 rabbitMQ 发送消息来让删除
     * @param userIdSet 用户 id 列表
     */
    @Transactional(transactionManager = "userTransactionManager")
    void removeUserByIdList(Set<Integer> userIdSet);

    /**
     * 更新用户是否有效
     * @param paramsMap 参数
     * 用户 id
     * 是否有效 isValid
     * @return 返回更新结果
     */
    @Transactional(transactionManager = "userTransactionManager")
    ResponseData updateUserIsValid(Map<String, String> paramsMap);

}
