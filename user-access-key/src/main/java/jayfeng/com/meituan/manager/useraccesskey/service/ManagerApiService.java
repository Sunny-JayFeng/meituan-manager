package jayfeng.com.meituan.manager.useraccesskey.service;

import jayfeng.com.meituan.manager.useraccesskey.bean.ManagerApi;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 管理员接口业务逻辑层 -- 接口
 * @author JayFeng
 * @date 2021/4/7
 */
@Service
public interface ManagerApiService {

    /**
     * 给某个管理员分配一个 api 访问接口权限
     * @param managerId 管理员 id
     * @param managerApi api
     * @return 返回数据
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData addApiToManagerByManagerId(Integer managerId,  ManagerApi managerApi);

    /**
     * 根据职级分配接口权限
     * @param paramsMap 参数
     * rankList -- 职级列表
     * apiName -- 接口名称
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData addManagerApiByRank(Map<String, String> paramsMap);

    /**
     * 根据接口路径和管理员id取消接口访问权限
     * @param paramsMap 参数
     * managerId -- 管理员id
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData removeManagerApiByPathAndManagerId(Map<String, String> paramsMap);

    /**
     * 根据接口id和职级取消接口访问权限
     * @param paramsMap 参数
     * rankList -- 职级列表
     * managerApiId -- 接口id
     * @return 返回数据
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData removeApiOfManagerByRankAndPath(Map<String, String> paramsMap);

}
