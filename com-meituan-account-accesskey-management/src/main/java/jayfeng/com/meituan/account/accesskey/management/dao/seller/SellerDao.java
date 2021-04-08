package jayfeng.com.meituan.account.accesskey.management.dao.seller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jayfeng.com.meituan.account.accesskey.management.bean.Seller;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JayFeng
 * @date 2021/4/8
 */
@Repository
public interface SellerDao extends BaseMapper<Seller> {

    /**
     * 根据 id 查找商家对象
     * @param sellerId 商家 id
     * @return 返回对象
     */
    @Select("SELECT `id`, `name`, `age`, `phone`, `id_card`, `is_valid`, `create_time`, `update_time` " +
            "FROM `seller` WHERE `id` = #{sellerId}")
    Seller selectSellerById(@Param("sellerId") Integer sellerId);

    /**
     * 根据 id 列表查出商家列表
     * @param sellerIds id列表
     * @return 返回商家列表
     */
    @Select("SELECT `id`, `name`, `age`, `phone`, `id_card`, `is_valid`, `create_time`, `update_time` " +
            "FROM `seller` WHERE `id` in ( ${sellerIds} )")
    List<Seller> selectSellersBySellerIds(@Param("sellerIds") String sellerIds);

    /**
     * 查询已经注销 14 天的商家，删除
     * @param updateTime 当前时间
     * @return 返回商家列表
     */
    @Select("SELECT `id`, `name`, `age`, `phone`, `id_card`, `is_valid`, `create_time`, `update_time` " +
            "FROM `seller` WHERE `is_valid` = 0 AND `update_time` = #{updateTime}")
    List<Seller> selectInvalidSellersToDelete(@Param("updateTime") Long updateTime);

    /**
     * 根据商家 id 列表批量删除商家
     * @param sellerIds 商家id列表
     */
    @Delete("DELETE FROM `seller` WHERE `id` in ( ${sellerIds} )")
    void deleteSellerByIds(@Param("sellerIds") String sellerIds);

    /**
     * 根据商家 id 更新用户是否有效
     * @param sellerId 商家id
     * @param isValid 是否有效
     * @param updateTime 更新时间
     */
    @Update("UPDATE `seller` SET `is_valid` = #{isValid}, `update_time` = #{updateTime} WHERE `id` = #{sellerId}")
    void updateSellerIsValid(@Param("sellerId") Integer sellerId, @Param("isValid") Integer isValid, @Param("updateTime") Long updateTime);

}
