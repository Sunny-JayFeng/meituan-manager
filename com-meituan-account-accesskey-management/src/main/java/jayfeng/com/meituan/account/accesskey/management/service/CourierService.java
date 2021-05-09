package jayfeng.com.meituan.account.accesskey.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.account.accesskey.management.bean.Courier;
import jayfeng.com.meituan.account.accesskey.management.response.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * 骑手业务层
 * @author JayFeng
 * @date 2021/5/8
 */
@Service
public interface CourierService {

    /**
     * 动态条件分页查询骑手信息
     * @param paramsMap 参数
     * @param findPage 分页
     * phone -- 手机号
     * isValid -- 是否有效
     * @return 返回数据
     */
    ResponseData findCouriers(Map<String, String> paramsMap, Page<Courier> findPage);

    /**
     * 更新骑手是否有效
     * @param paramsMap 参数
     * @return 返回更新是否成功
     */
    @Transactional(transactionManager = "courierTransactionManager")
    ResponseData updateCourierIsValid(Map<String, String> paramsMap);

    /**
     * 根据骑手 id 列表删除骑手
     * 由 rabbitMQ 发送消息来让删除
     * @param courierIdSet 骑手 id 列表
     */
    @Transactional(transactionManager = "courierTransactionManager")
    void removeCourierByIdList(Set<Integer> courierIdSet);

}
