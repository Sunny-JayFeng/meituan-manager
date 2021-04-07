package jayfeng.com.meituan.manager.useraccesskey.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.manager.useraccesskey.bean.AccessKey;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 短信密钥业务逻辑 -- 接口
 * @author JayFeng
 * @date 2021/4/7
 */
@Service
public interface AccessKeyService {

    /**
     * 动态条件分页查询密钥
     * @param paramsMap 查询条件
     * @param findPage 分页
     * @return 返回数据
     */
    ResponseData findAccessKeys(Map<String, String> paramsMap, Page<AccessKey> findPage);

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     * @return 返回数据
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData addAccessKey(AccessKey accessKey);

    /**
     * 根据 id 修改短信服务密钥
     * @param newAccessKey 密钥
     * @return 返回是否成功
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData updateAccessKeyById(AccessKey newAccessKey);

    /**
     * 删除短信服务密钥
     * @param id id
     * @return 返回是否删除成功
     */
    @Transactional(transactionManager = "managerTransactionManager")
    ResponseData deleteAccessKeyById(Integer id);

}
