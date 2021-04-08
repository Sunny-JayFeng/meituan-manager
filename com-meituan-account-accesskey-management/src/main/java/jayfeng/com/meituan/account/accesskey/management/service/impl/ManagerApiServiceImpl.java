package jayfeng.com.meituan.account.accesskey.management.service.impl;

import jayfeng.com.meituan.account.accesskey.management.bean.ManagerApi;
import jayfeng.com.meituan.account.accesskey.management.dao.manager.ManagerApiDao;
import jayfeng.com.meituan.account.accesskey.management.dao.manager.ManagerDao;
import jayfeng.com.meituan.account.accesskey.management.exception.RequestForbiddenException;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import jayfeng.com.meituan.account.accesskey.management.service.ManagerApiService;
import jayfeng.com.meituan.account.accesskey.management.util.RequestThreadLocalManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理员接口业务逻辑层 -- 实现类
 * @author JayFeng
 * @date 2021/4/7
 */
@Slf4j
@Service
public class ManagerApiServiceImpl implements ManagerApiService {

    @Autowired
    private ManagerApiDao managerApiDao;
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private RequestThreadLocalManagement requestThreadLocalManagement;

    /**
     * 给某个管理员分配一个 api 访问接口权限
     * @param managerId 管理员 id
     * @param managerApi api
     * @return 返回数据
     */
    @Override
    @Transactional(transactionManager = "managerTransactionManager")
    public ResponseData addApiToManagerByManagerId(Integer managerId, ManagerApi managerApi) {
        if (ObjectUtils.isEmpty(managerId) || managerDao.selectManagerById(managerId) == null
                || managerApiDao.selectManagerApiIdById(managerApi.getId()) == null) {
            log.info("addApiToManager 根据管理员id分配接口权限失败, 请求非法, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        if (managerApiDao.selectManagerApiIdByManagerIdAndPath(managerId, managerApi.getApiPath()) != null) {
            log.info("addApiToManager 根据管理员id分配接口权限失败 权限已分配 managerId: {}, apiPath: {}", managerId, managerApi.getApiPath());
            return ResponseData.createFailResponseData("addApiToManagerInfo", false, "权限已分配", "authority_is_assigned");
        }
        managerApi.setManagerId(managerId);
        managerApi.setCreateTime(System.currentTimeMillis());
        managerApi.setUpdateTime(managerApi.getCreateTime());
        managerApiDao.insertManagerApi(managerApi);
        log.info("addApiToManager 根据管理员id分配接口权限成功, 操作人: {}, managerApi: {}", requestThreadLocalManagement.getRequesterName(), managerApi);
        return ResponseData.createSuccessResponseData("addApiToManagerInfo", true);
    }

    /**
     * 根据职级分配接口权限
     * @param paramsMap 参数
     * rankList -- 职级列表
     * apiName -- 接口名称
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @Override
    @Transactional(transactionManager = "managerTransactionManager")
    public ResponseData addManagerApiByRank(Map<String, String> paramsMap) {
        String rankList = paramsMap.get("rankList");
        String apiName = paramsMap.get("apiName");
        String apiPath = paramsMap.get("apiPath");
        if (ObjectUtils.isEmpty(rankList) || ObjectUtils.isEmpty(apiName) || ObjectUtils.isEmpty(apiPath)) {
            log.info("addManagerApiByRank 根据职级分配接口权限失败, 参数异常, 请求非法, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        List<Integer> managerIdList = managerDao.selectManagerIdsByRank(rankList);
        List<ManagerApi> managerApiList = new ArrayList<>(managerIdList.size());
        for (Integer managerId : managerIdList) {
            ManagerApi managerApi = new ManagerApi();
            managerApi.setManagerId(managerId);
            managerApi.setApiName(apiName);
            managerApi.setApiPath(apiPath);
            managerApi.setCreateTime(System.currentTimeMillis());
            managerApi.setUpdateTime(managerApi.getCreateTime());
            managerApiList.add(managerApi);
        }
        // 插入数据
        for (ManagerApi managerApi : managerApiList) {
            if (managerApiDao.selectManagerApiIdByManagerIdAndPath(managerApi.getManagerId(), managerApi.getApiPath()) != null) {
                log.info("addManagerApiByRank 该管理员已分配该权限，无需重复分配。managerId: {}, apiPath: {}", managerApi.getManagerId(), managerApi.getApiPath());
                continue;
            }
            managerApiDao.insertManagerApi(managerApi);
        }
        log.info("addManagerApiByRank 根据职级分配接口权限成功, 操作人: {}, managerListSize: {}, apiName: {}, apiPath: {}", requestThreadLocalManagement.getRequesterName(), managerApiList.size(), apiName, apiPath);
        return ResponseData.createSuccessResponseData("addApiToManagerByRankInfo", true);
    }

    /**
     * 根据接口路径和管理员id取消接口访问权限
     * @param paramsMap 参数
     * managerId -- 管理员id
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @Override
    @Transactional(transactionManager = "managerTransactionManager")
    public ResponseData removeManagerApiByPathAndManagerId(Map<String, String> paramsMap) {
        String managerId = paramsMap.get("managerId");
        String apiPath = paramsMap.get("apiPath");
        if (ObjectUtils.isEmpty(apiPath) || ObjectUtils.isEmpty(managerId)) {
            log.info("removeApiOfManagerById 取消接口访问权限失败, 参数异常, 请求非法, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        int managerIdValue;
        try {
            managerIdValue = Integer.parseInt(managerId);
        } catch (NumberFormatException e) {
            log.info("removeApiOfManagerById 取消接口访问权限失败, 参数异常, 请求非法, 拒绝处理");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        ManagerApi managerApi = managerApiDao.selectByIdAndManagerId(apiPath, managerIdValue);
        if (managerApi == null) {
            log.info("removeApiOfManagerById 取消接口访问权限失败, 不存在此权限分配 apiPath: {}, managerId: {}", apiPath, managerId);
            return ResponseData.createFailResponseData("removeApiOfManagerByIdInfo", false, "不存在此权限分配", "authority_assign_not_exists");
        }
        managerApiDao.deleteManagerApiById(managerApi.getId());
        log.info("removeApiOfManagerById 取消接口访问权限成功, 操作人: {}, managerId: {}, managerApi: {}", requestThreadLocalManagement.getRequesterName(), managerId, managerApi);
        return ResponseData.createSuccessResponseData("removeApiOfManagerByIdInfo", true);
    }

    /**
     * 根据接口路径和职级取消接口访问权限
     * @param paramsMap 参数
     * rankList -- 职级列表
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @Override
    @Transactional(transactionManager = "managerTransactionManager")
    public ResponseData removeApiOfManagerByRankAndPath(Map<String, String> paramsMap) {
        String rankList = paramsMap.get("rankList");
        String apiPath = paramsMap.get("apiPath");
        List<Integer> managerIdList = managerDao.selectManagerIdsByRank(rankList);
        String managerIds = handleAppendManagerIds(managerIdList);
        List<ManagerApi> managerApiList = managerApiDao.selectManagerApiByPathAndManagerIds(apiPath, managerIds);
        for (ManagerApi managerApi : managerApiList) {
            log.info("removeApiOfManagerByRankAndPath 根据职级和接口路径删除, 待删除对象 managerApi: {}", managerApi);
        }
        managerApiDao.deleteManagerApiByPathAndManagerIds(apiPath, managerIds);
        log.info("removeApiOfManagerByRankAndPath 根据职级和接口路径删除成功, 操作人: {}, rankList: {}, apiPath: {}", requestThreadLocalManagement.getRequesterName(), rankList, apiPath);
        return ResponseData.createSuccessResponseData("removeApiOfManagerByRankAndPathInfo", true);
    }

    /**
     * 处理管理员 id 列表，拼接成字符串
     * @param managerIdList 管理员 id 列表
     * @return 字符串形式的管理员 id 列表
     */
    private String handleAppendManagerIds(List<Integer> managerIdList) {
        // 这个逻辑不会有线程安全问题
        StringBuilder strb = new StringBuilder();
        for (Integer managerId : managerIdList) {
            strb.append(managerId);
            strb.append(",");
        }
        String managerIds = strb.substring(0, strb.length() - 1);
        log.info("handleAppendManagerIds 管理员 id 拼接结果 managerIds: {}", managerIds);
        return managerIds;
    }
}
