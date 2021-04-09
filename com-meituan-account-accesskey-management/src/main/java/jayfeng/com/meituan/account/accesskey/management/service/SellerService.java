package jayfeng.com.meituan.account.accesskey.management.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.account.accesskey.management.bean.Seller;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * @author JayFeng
 * @date 2021/4/8
 */
@Service
public interface SellerService {


    /**
     * 动态条件分页查询商家信息
     * @param paramsMap 参数
     * @param findPage 分页
     * @return 返回数据
     */
    ResponseData findSellers(Map<String, String> paramsMap, Page<Seller> findPage);

    /**
     * 根据商家 id 列表删除商家
     * 由 rabbitMQ 发送消息来让删除
     * @param sellerIdSet 商家 id 列表
     */
    @Transactional(transactionManager = "sellerTransactionManager")
    void removeSellerByIdList(Set<Integer> sellerIdSet);

    /**
     * 更新商家是否有效
     * @param paramsMap 参数
     * @return 返回更新是否成功
     */
    @Transactional(transactionManager = "sellerTransactionManager")
    ResponseData updateSellerIsValid(Map<String, String> paramsMap);

}
