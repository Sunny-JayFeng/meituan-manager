package jayfeng.com.meituan.account.accesskey.management.controller;

import jayfeng.com.meituan.account.accesskey.management.response.ResponseMessage;
import jayfeng.com.meituan.account.accesskey.management.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商家管理控制层
 * @author JayFeng
 * @date 2021/4/8
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/sellerManagement")
public class SellerController extends BaseController {

    @Autowired
    private SellerService sellerService;

    /**
     * 动态条件分页查询商家信息
     * @param paramsMap 参数
     * @return 返回数据
     */
    @GetMapping("/findSellers")
    public ResponseMessage findSellers(@RequestParam Map<String, String> paramsMap) {
        log.info("findSellers 查询商家信息 paramsMap: {}", paramsMap);
        return requestSuccess(sellerService.findSellers(paramsMap, getPage(paramsMap)));
    }

    /**
     * 更新商家是否有效
     * @param paramsMap 参数
     * @return 返回更新是否成功
     */
    @PutMapping("/updateSellerIsValid")
    public ResponseMessage updateSellerIsValid(@RequestBody Map<String, String> paramsMap) {
        log.info("updateSellerIsValid 更新商家是否有效 paramsMap: {}", paramsMap);
        return requestSuccess(sellerService.updateSellerIsValid(paramsMap));
    }

}
