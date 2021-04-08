package jayfeng.com.meituan.account.accesskey.management.controller;

import jayfeng.com.meituan.account.accesskey.management.bean.AccessKey;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseMessage;
import jayfeng.com.meituan.account.accesskey.management.service.AccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * 动态条件分页查询密钥
     * @param paramsMap 查询条件
     * @return 返回数据
     */
    @GetMapping("/findAccessKey")
    public ResponseMessage findAccessKeys(@RequestParam Map<String, String> paramsMap) {
        log.info("findAccessKeys 查询密钥 paramsMap: {}", paramsMap);
        return requestSuccess(accessKeyService.findAccessKeys(paramsMap, getPage(paramsMap)));
    }

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
