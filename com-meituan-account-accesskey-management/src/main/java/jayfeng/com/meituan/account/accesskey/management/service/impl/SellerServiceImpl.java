package jayfeng.com.meituan.account.accesskey.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.account.accesskey.management.bean.Seller;
import jayfeng.com.meituan.account.accesskey.management.dao.seller.SellerDao;
import jayfeng.com.meituan.account.accesskey.management.exception.RequestForbiddenException;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import jayfeng.com.meituan.account.accesskey.management.service.SellerService;
import jayfeng.com.meituan.account.accesskey.management.util.RequestThreadLocalManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JayFeng
 * @date 2021/4/8
 */
@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private RequestThreadLocalManagement requestThreadLocalManagement;

    /**
     * 动态条件分页查询商家信息
     * @param paramsMap 参数
     * phone -- 手机号
     * isValid -- 是否有效
     * @param findPage 分页
     * @return 返回数据
     */
    @Override
    public ResponseData findSellers(Map<String, String> paramsMap, Page<Seller> findPage) {
        String phone = paramsMap.get("phone");
        String isValid = paramsMap.get("isValid");
        QueryWrapper<Seller> queryWrapper = new QueryWrapper<>();
        // 手机号
        if (!ObjectUtils.isEmpty(phone)) queryWrapper.eq("phone", phone);
        // 是否有效
        if (!ObjectUtils.isEmpty(isValid)) {
            try {
                queryWrapper.eq("isValid", Integer.parseInt(isValid));
            } catch (NumberFormatException e) {
                log.info("findSellers 查询失败, 参数异常, 请求非法");
                throw new RequestForbiddenException("您无权访问该服务");
            }
        }
        Page<Seller> dataPage = sellerDao.selectPage(findPage, queryWrapper);
        log.info("findSellers 商家数据查询成功 total: {}", dataPage.getTotal());
        return ResponseData.createSuccessResponseData("findSellersInfo", dataPage);
    }

    /**
     * 根据商家 id 列表删除商家
     * 由 rabbitMQ 发送消息来让删除
     * @param sellerIdSet 商家 id 列表
     */
    @Override
    @Transactional(transactionManager = "sellerTransactionManager")
    public void removeSellerByIdList(Set<Integer> sellerIdSet) {
        log.info("removeSellerByIdList 根据 id 集合删除商家 sellerIdSet: {}", sellerIdSet);
        String sellerIds = handleAppendSellerIds(sellerIdSet);
        List<Seller> sellerList = sellerDao.selectSellersBySellerIds(sellerIds);
        if (sellerList.isEmpty()) {
            log.info("removeSellerByIdList 对象集合为空");
            return;
        }
        for (Seller seller : sellerList) log.info("removeSellerByIdList 待删除商家对象 seller: {}", seller);
        sellerDao.deleteSellerByIds(sellerIds);
        log.info("removeSellerByIdList 批量删除商家成功");
    }

    /**
     * 处理用户 id 列表，拼接成字符串
     * @param sellerIdSet 用户 id 列表
     * @return 字符串形式的用户 id 列表
     */
    private String handleAppendSellerIds(Set<Integer> sellerIdSet) {
        // 这个逻辑不会有线程安全问题
        StringBuilder strb = new StringBuilder();
        for (Integer userId : sellerIdSet) {
            strb.append(userId);
            strb.append(",");
        }
        String sellerIds = strb.substring(0, strb.length() - 1);
        log.info("handleAppendUserIds 用户 id 拼接结果 userIds: {}", sellerIds);
        return sellerIds;
    }

    /**
     * 更新商家是否有效
     * @param paramsMap 参数
     * @return 返回更新是否成功
     */
    @Override
    @Transactional(transactionManager = "sellerTransactionManager")
    public ResponseData updateSellerIsValid(Map<String, String> paramsMap) {
        String id = paramsMap.get("sellerId");
        String isValid = paramsMap.get("isValid");
        if (ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(isValid)) {
            log.info("updateSellerIsValid 更新商家是否有效失败, 参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        int sellerId = -1;
        int isValidValue = -1;
        try {
            sellerId = Integer.parseInt(id);
            isValidValue = Integer.parseInt(isValid);
        } catch (NumberFormatException e) {
            log.info("updateSellerIsValid 更新商家是否有效失败, 参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Seller seller = sellerDao.selectSellerById(sellerId);
        if (seller == null) {
            log.info("updateSellerIsValid 更新商家是否有效失败, 参数异常, 请求非法");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        log.info("updateSellerIsValid 更新商家是否有效 seller: {}, isValid: {}", seller, isValid);
        sellerDao.updateSellerIsValid(sellerId, isValidValue, System.currentTimeMillis());
        log.info("updateSellerIsValid 更新商家是否有效 操作人: {}", requestThreadLocalManagement.getRequesterName());
        return ResponseData.createSuccessResponseData("updateSellerIsValidInfo", true);
    }
}
