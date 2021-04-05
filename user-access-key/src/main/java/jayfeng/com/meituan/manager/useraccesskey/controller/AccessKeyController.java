package jayfeng.com.meituan.manager.useraccesskey.controller;

import jayfeng.com.meituan.manager.useraccesskey.bean.AccessKey;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseMessage;
import jayfeng.com.meituan.manager.useraccesskey.service.AccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 短信密钥控制层
 * @author JayFeng
 * @date 2021/4/1
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/user_accessKey/accessKey")
public class AccessKeyController extends BaseController {

    @Autowired
    private AccessKeyService accessKeyService;

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     * @return 返回
     */
    @PostMapping("/addAccessKey")
    public ResponseMessage addAccessKey(@RequestBody AccessKey accessKey) {
        log.info("addAccessKey 新增密钥 accessKey: {}", accessKey);
        return requestSuccess(accessKeyService.addAccessKey(accessKey));
    }

    /**
     * 根据 id 修改短信服务密钥
     * @param newAccessKey 密钥
     * @return 返回修改是否成功
     */
    @PutMapping("/updateAccessKeyById")
    public ResponseMessage updateAccessKeyById(@RequestBody AccessKey newAccessKey) {
        log.info("updateAccessKeyById 修改短信服务密钥 newAccessKey: {}", newAccessKey);
        return requestSuccess(accessKeyService.updateAccessKeyById(newAccessKey));
    }

    /**
     * 删除短信服务密钥
     * @param id id
     * @return 返回删除是否成功
     */
    @DeleteMapping("/deleteAccessKeyById/{id}")
    public ResponseMessage deleteAccessKeyById(@PathVariable("id") Integer id) {
        log.info("deleteAccessKeyById 删除短信服务密钥 id: {}", id);
        return requestSuccess(accessKeyService.deleteAccessKeyById(id));
    }

}
