package jayfeng.com.meituan.account.accesskey.management.dao.courier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jayfeng.com.meituan.account.accesskey.management.bean.Courier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 骑手持久层
 * @author JayFeng
 * @date 2021/5/8
 */
@Repository
public interface CourierDao extends BaseMapper<Courier> {

    /**
     * 根据 id 查找骑手
     * @param courierId 骑手 id
     * @return 返回对象
     */
    @Select("SELECT `id`, `name`, `birthday`, `phone`, `received_orders`, `timeout_times`, " +
            "`praise_times`, `negative_comment_times`, `is_valid` FROM `courier` WHERE `id` = #{courierId}")
    Courier selectCourierById(@Param("courierId") Integer courierId);

    /**
     * 根据 id 列表查出骑手列表
     * @param courierIds id 列表
     * @return 返回骑手列表
     */
    @Select("SELECT `id`, `name`, `birthday`, `phone`, `received_orders`, `timeout_times`, " +
            "`praise_times`, `negative_comment_times`, `is_valid` FROM `courier` WHERE `id` in (${courierIds})")
    List<Courier> selectCouriersByCourierIds(@Param("courierIds") String courierIds);

    /**
     * 查询已经注销 14 天的骑手，删除
     * @param updateTime 当前时间
     * @return 返回骑手列表
     */
    @Select("SELECT `id`, `name`, `birthday`, `phone`, `received_orders`, `timeout_times`, " +
            "`praise_times`, `negative_comment_times`, `is_valid` FROM `courier` WHERE `update_time` < #{updateTime}")
    List<Courier> selectInValidCourierToDelete(@Param("updateTime") Long updateTime);

    /**
     * 更新骑手是否有效
     * @param courierId 骑手 id
     * @param isValid 是否有效
     * @param updateTime 更新时间
     */
    @Update("UPDATE `courier` SET `is_valid` = #{isValid}, `update_time` = #{updateTime} WHERE `id` = #{courierId} ")
    void updateCourierIsValid(@Param("courierId") Integer courierId, @Param("isValid") Byte isValid, @Param("updateTime") Long updateTime);

    /**
     * 根据骑手 id 列表批量删除骑手
     * @param courierIds 骑手 id 列表
     */
    @Delete("DELETE FROM `courier` WHERE `id` in ( ${courierIds} )")
    void deleteCourierByIds(@Param("courierIds") String courierIds);

}
