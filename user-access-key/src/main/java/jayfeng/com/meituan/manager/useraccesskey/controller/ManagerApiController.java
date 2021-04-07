package jayfeng.com.meituan.manager.useraccesskey.controller;

import jayfeng.com.meituan.manager.useraccesskey.bean.ManagerApi;
import jayfeng.com.meituan.manager.useraccesskey.response.ResponseMessage;
import jayfeng.com.meituan.manager.useraccesskey.service.ManagerApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员接口控制层
 * @author JayFeng
 * @date 2021/4/7
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/api")
public class ManagerApiController extends BaseController {

    @Autowired
    private ManagerApiService managerApiService;

    /**
     * 给某个管理员分配一个 api 访问接口权限
     * @param managerId 管理员 id
     * @param managerApi api
     * @return 返回数据
     */
    @PostMapping("/addApiToManager/{managerId}")
    public ResponseMessage addApiToManager(@PathVariable("managerId") Integer managerId,
                                           @RequestBody ManagerApi managerApi) {
        log.info("addApiToManager 接口权限分配 managerId: {}, managerApi: {}", managerId, managerApi);
        return requestSuccess(managerApiService.addApiToManagerByManagerId(managerId, managerApi));
    }

    /**
     * 根据职级分配接口权限
     * @param paramsMap 参数
     * @return 返回数据
     */
    @PostMapping("/addManagerApiByRank")
    public ResponseMessage addManagerApiByRank(@RequestBody Map<String, String> paramsMap) {
        log.info("addManagerApiByRank 根据职级分配接口权限 paramsMap: {}", paramsMap);
        return requestSuccess(managerApiService.addManagerApiByRank(paramsMap));
    }

    /**
     * 根据接口路径和管理员id取消接口访问权限
     * @param paramsMap 参数
     * managerId -- 管理员id
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @DeleteMapping("/removeManagerApiByPathAndManagerId")
    public ResponseMessage removeManagerApiByPathAndManagerId(@RequestBody Map<String, String> paramsMap) {
        log.info("removeManagerApiByPathAndManagerId 取消接口访问权限 paramsMap: {}", paramsMap);
        return requestSuccess(managerApiService.removeManagerApiByPathAndManagerId(paramsMap));
    }

    /**
     * 根据接口路径和职级取消接口访问权限
     * @param paramsMap 参数
     * rankList -- 职级列表
     * apiPath -- 接口路径
     * @return 返回数据
     */
    @DeleteMapping("/removeApiOfManagerByRankAndPath")
    public ResponseMessage removeApiOfManagerByRankAndPath(@RequestBody Map<String, String> paramsMap) {
        log.info("removeApiOfManagerByRankAndPath 取消接口访问权限 paramsMap: {}", paramsMap);
        return requestSuccess(managerApiService.removeApiOfManagerByRankAndPath(paramsMap));
    }

}
