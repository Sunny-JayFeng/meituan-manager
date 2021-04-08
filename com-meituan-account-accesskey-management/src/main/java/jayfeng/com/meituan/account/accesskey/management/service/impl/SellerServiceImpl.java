package jayfeng.com.meituan.account.accesskey.management.service.impl;

import jayfeng.com.meituan.account.accesskey.management.dao.seller.SellerDao;
import jayfeng.com.meituan.account.accesskey.management.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JayFeng
 * @date 2021/4/8
 */
@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerDao sellerDao;

}
