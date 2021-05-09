package jayfeng.com.meituan.account.accesskey.management.controller;

import jayfeng.com.meituan.account.accesskey.management.response.ResponseMessage;
import jayfeng.com.meituan.account.accesskey.management.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 骑手控制层
 * @author JayFeng
 * @date 2021/5/8
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/courierManagement")
public class CourierController extends BaseController {

    @Autowired
    private CourierService courierService;

    /**
     * 动态条件分页查询骑手信息
     * @param paramsMap 参数
     * @return 返回数据
     */
    @GetMapping("/findCouriers")
    public ResponseMessage findCouriers(@RequestParam Map<String, String> paramsMap) {
        log.info("findCouriers 查询骑手信息 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.findCouriers(paramsMap, getPage(paramsMap)));
    }

    /**
     * 更新骑手是否有效
     * @param paramsMap 参数
     * @return 返回更新是否成功
     */
    @PutMapping("/updateCourierIsValid")
    public ResponseMessage updateCourierIsValid(@RequestBody Map<String, String> paramsMap) {
        log.info("updateCourierIsValid 更新骑手是否有效 paramsMap: {}", paramsMap);
        return requestSuccess(courierService.updateCourierIsValid(paramsMap));
    }

}
