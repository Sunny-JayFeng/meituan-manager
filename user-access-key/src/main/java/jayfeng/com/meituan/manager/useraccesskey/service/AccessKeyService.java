package jayfeng.com.meituan.manager.useraccesskey.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.manager.useraccesskey.bean.AccessKey;
import jayfeng.com.meituan.manager.useraccesskey.dao.manager.AccessKeyDao;
import jayfeng.com.meituan.manager.useraccesskey.exception.RequestForbiddenException;
import jayfeng.com.meituan.manager.useraccesskey.handler.rabbitmq.SendMessageHandler;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseData;
import jayfeng.com.meituan.rpc.accesskey.service.RPCAccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信密钥业务逻辑
 * @author JayFeng
 * @date 2021/4/1
 */
@Slf4j
@Service
public class AccessKeyService {

    @Autowired
    private AccessKeyDao accessKeyDao;
    @Autowired
    private SendMessageHandler sendMessageHandler;

    @DubboReference(version = "1.0.1")
    private RPCAccessKeyService rpcAccessKeyService;

    /**
     * 动态条件分页查询密钥
     * @param paramsMap 查询条件
     * @param findPage 分页
     * @return 返回数据
     */
    public ResponseData findAccessKeys(Map<String, String> paramsMap, Page<AccessKey> findPage) {
        String regionId = paramsMap.get("regionId");
        String type = paramsMap.get("type");
        QueryWrapper<AccessKey> queryWrapper = new QueryWrapper<>();
        // 地区id
        if (!ObjectUtils.isEmpty(regionId)) queryWrapper.eq("region_id", regionId);
        // 密钥类型（0 代表作为短信验证码）
        if (!ObjectUtils.isEmpty(type)) {
            try {
                queryWrapper.eq("type", Integer.parseInt(type));
            } catch (NumberFormatException e) {
                log.info("findAccessKeys 查询密钥数据失败, 参数异常, 请求不是来自流浪器, 拒绝处理");
                throw new RequestForbiddenException("您无权访问该服务");
            }
        }
        Page<AccessKey> dataPage = accessKeyDao.selectPage(findPage, queryWrapper);
        log.info("findAccessKeys 密钥数据查询成功 total: {}", dataPage.getTotal());
        return ResponseData.createSuccessResponseData("findAccessKeysInfo", dataPage);
    }

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     * @return 返回数据
     */
    public ResponseData addAccessKey(AccessKey accessKey) {
        AccessKey oldKey = accessKeyDao.selectOneByAccessKeyId(accessKey.getAccessKeyId());
        if (oldKey != null){
            log.info("addAccessKey 密钥新增失败, 密钥已存在 oldKey: {}", oldKey);
            return ResponseData.createFailResponseData("addAccessKeyInfo", false, "密钥已存在", "access_key_id_exists");
        }
        accessKey.setCreateTime(System.currentTimeMillis());
        accessKey.setUpdateTime(accessKey.getCreateTime());
        accessKeyDao.addAccessKey(accessKey);
        log.info("addAccessKey 密钥新增成功 accessKey: {}", accessKey);
        log.info("addAccessKey 密钥有更新, 远程调用更新");
        rpcAccessKeyService.initAllAccessKeyList();
        log.info("addAccessKey 密钥有更新, 发布更新消息");
        sendMessageHandler.sendUpdateAccessKeyMessage();
        return ResponseData.createSuccessResponseData("addAccessKeyInfo", true);
    }

    /**
     * 根据 id 修改短信服务密钥
     * @param newAccessKey 密钥
     * @return 返回是否成功
     */
    public ResponseData updateAccessKeyById(AccessKey newAccessKey) {
        log.info("updateAccessKeyById 根据 id 修改短信服务密钥 newAccessKey: {}", newAccessKey);
        AccessKey oldAccessKey = accessKeyDao.selectAccessKey(newAccessKey.getId());
        if (oldAccessKey == null) {
            log.info("updateAccessKeyById 短信服务密钥修改失败, 查无此密钥: newAccessKey: {}", newAccessKey);
            return ResponseData.createFailResponseData("updateAccessKeyByIdInfo", false, "不存在此密钥", "key_is_not_exists");
        }
        newAccessKey.setUpdateTime(System.currentTimeMillis());
        accessKeyDao.updateAccessKey(newAccessKey);
        log.info("updateAccessKeyById 更新密钥成功 oldAccessKey: {}", oldAccessKey);
        log.info("updateAccessKeyById 更新密钥成功 newAccessKey: {}", newAccessKey);
        log.info("updateAccessKeyById 密钥有更新, 远程调用更新");
        rpcAccessKeyService.initAllAccessKeyList();
        log.info("updateAccessKeyById 密钥有更新, 发布更新消息");
        sendMessageHandler.sendUpdateAccessKeyMessage();
        return ResponseData.createSuccessResponseData("updateAccessKeyByIdInfo", true);
    }

    /**
     * 删除短信服务密钥
     * @param id id
     * @return 返回是否删除成功
     */
    public ResponseData deleteAccessKeyById(Integer id) {
        log.info("deleteAccessKeyById 删除短信服务密钥 id: {}", id);
        AccessKey accessKey = accessKeyDao.selectAccessKey(id);
        if (accessKey == null) {
            log.info("deleteAccessKeyById 删除短信服务密钥失败, 密钥不存在");
            return ResponseData.createFailResponseData("deleteAccessKeyByIdInfo", false, "不存在此密钥", "key_is_not_exists");
        }
        accessKeyDao.deleteAccessKey(id);
        log.info("deleteAccessKeyById 删除短信服务密钥成功 accessKey: {}", accessKey);
        log.info("deleteAccessKeyById 密钥有更新, 远程调用更新");
        rpcAccessKeyService.initAllAccessKeyList();
        log.info("deleteAccessKeyById 密钥有更新, 发布更新消息");
        sendMessageHandler.sendUpdateAccessKeyMessage();
        return ResponseData.createSuccessResponseData("deleteAccessKeyInfo", true);
    }

}
