package jayfeng.com.meituan.account.accesskey.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.account.accesskey.management.bean.Courier;
import jayfeng.com.meituan.account.accesskey.management.dao.courier.CourierDao;
import jayfeng.com.meituan.account.accesskey.management.exception.RequestForbiddenException;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import jayfeng.com.meituan.account.accesskey.management.service.CourierService;
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
 * 骑手业务层
 * @author JayFeng
 * @date 2021/5/8
 */
@Slf4j
@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierDao courierDao;
    @Autowired
    private RequestThreadLocalManagement requestThreadLocalManagement;

    /**
     * 动态条件分页查询骑手信息
     * @param paramsMap 参数
     * @param findPage 分页
     * phone -- 手机号
     * isValid -- 是否有效
     * @return 返回数据
     */
    @Override
    public ResponseData findCouriers(Map<String, String> paramsMap, Page<Courier> findPage) {
        String phone = paramsMap.get("phone");
        String isValid = paramsMap.get("isValid");
        QueryWrapper<Courier> queryWrapper = new QueryWrapper<>();
        // 手机号
        if (!ObjectUtils.isEmpty(phone)) queryWrapper.eq("phone", phone);
        // 是否有效
        if (!ObjectUtils.isEmpty(isValid)) {
            try {
                queryWrapper.eq("isValid", Integer.parseInt(isValid));
            } catch (NumberFormatException e) {
                log.info("findCouriers 查询失败, 参数异常, 请求非法");
                throw new RequestForbiddenException("您无权访问该服务");
            }
        }
        Page<Courier> dataPage = courierDao.selectPage(findPage, queryWrapper);
        log.info("findCouriers 骑手数据查询成功 total: {}", dataPage.getTotal());
        return ResponseData.createSuccessResponseData("findCouriersInfo", dataPage);
    }

    /**
     * 更新骑手是否有效
     * @param paramsMap 参数
     * @return
     */
    @Override
    @Transactional(transactionManager = "courierTransactionManager")
    public ResponseData updateCourierIsValid(Map<String, String> paramsMap) {
        String id = paramsMap.get("courierId");
        String isValid = paramsMap.get("isValid");
        if (ObjectUtils.isEmpty(id) || ObjectUtils.isEmpty(isValid)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        int courierId = -1;
        byte isValidValue = -1;
        try {
            courierId = Integer.parseInt(id);
            isValidValue = Byte.parseByte(isValid);
        } catch (NumberFormatException e) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Courier courier = courierDao.selectCourierById(courierId);
        if (courier == null) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        log.info("updateCourierIsValid 更新骑手是否有效 courier: {}, isValid: {}", courier, isValid);
        courierDao.updateCourierIsValid(courierId, isValidValue, System.currentTimeMillis());
        log.info("updateCourierIsValid 更新骑手是否有效 操作人: {}", requestThreadLocalManagement.getRequesterName());
        return ResponseData.createSuccessResponseData("updateCourierIsValidInfo",  true);
    }

    /**
     * 根据骑手 id 列表删除骑手
     * 由 rabbitMQ 发送消息来让删除
     * @param courierIdSet 骑手 id 列表
     */
    @Override
    @Transactional(transactionManager = "courierTransactionManager")
    public void removeCourierByIdList(Set<Integer> courierIdSet) {
        log.info("removeCourierByIdList 根据 id 集合删除骑手 courierIdSet: {}", courierIdSet);
        String courierIds = handleAppendCourierIds(courierIdSet);
        List<Courier> courierList = courierDao.selectCouriersByCourierIds(courierIds);
        if (courierList.isEmpty()) {
            log.info("removeCourierByIdList 对象集合为空");
            return;
        }
        for (Courier courier : courierList) log.info("removeCourierByIdList 待删除骑手对象 courier: {}", courier);
        courierDao.deleteCourierByIds(courierIds);
        log.info("removeCourierByIdList 批量删除骑手成功");
    }

    /**
     * 处理骑手 id 列表，拼接成字符串
     * @param courierIdSet 骑手 id 列表
     * @return 字符串形式的骑手 id 列表
     */
    private String handleAppendCourierIds(Set<Integer> courierIdSet) {
        // 这个逻辑不会有线程安全问题
        StringBuilder strb = new StringBuilder();
        for (Integer userId : courierIdSet) {
            strb.append(userId);
            strb.append(",");
        }
        String courierIds = strb.substring(0, strb.length() - 1);
        log.info("handleAppendUserIds 骑手 id 拼接结果 userIds: {}", courierIds);
        return courierIds;
    }
}
