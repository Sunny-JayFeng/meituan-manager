package jayfeng.com.meituan.account.accesskey.management.controller;

import jayfeng.com.meituan.account.accesskey.management.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家管理控制层
 * @author JayFeng
 * @date 2021/4/8
 */
@Slf4j
@RestController
@RequestMapping("/meituan/manager/sellerManagement")
public class SellerController {

    @Autowired
    private SellerService sellerService;

}
