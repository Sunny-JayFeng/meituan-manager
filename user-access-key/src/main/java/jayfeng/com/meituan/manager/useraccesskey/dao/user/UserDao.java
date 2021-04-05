package jayfeng.com.meituan.manager.useraccesskey.dao.user;

import jayfeng.com.meituan.manager.useraccesskey.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author JayFeng
 * @date 2021/4/2
 */
@Repository
public interface UserDao {

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `user_name`, `head_image`, `phone`, `password`, `email`, `is_vip`, " +
            " `automatic_renewal`, `vip_create_time`, `,vip_end_time`, `vip_type`, `vip_grade`," +
            " `birthday`, `create_time`, `update_time` FROM `user` WHERE `phone` = #{phone}")
    User selectUserByUserPhone(@Param("phone") String phone);

    /**
     * 根据 id 查找用户对象 (用于删除用户的时候查询)
     * @param userId 用户 id
     * @return 返回用户对象
     */
    @Select("SELECT * FROM `user` WHERE `id` = #{userId}")
    User selectUserByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户 id 列表 in 查询出用户对象列表
     * @param userIds 用户 id 列表
     * @return 返回用户对象列表
     */
    @Select("SELECT * FROM `user` WHERE `id` in ( ${userIds} )")
    List<User> selectUsersByUserIds(@Param("userIds") String userIds);

    /**
     * 根据 id 删除用户对象
     * @param userId 用户 id
     */
    @Delete("DELETE FROM `user` WHERE `id` = #{userId}")
    void deleteUserById(@Param("userId") Integer userId);

    /**
     * 根据用户 id 列表批量删除用户
     * @param userIds 用户 id 列表
     */
    @Delete("DELETE FROM `user` WHERE `id` in ( ${userIds} )")
    void deleteUsesByIds(@Param("userIds") String userIds);

    /**
     * 根据用户 id 更新用户是否有效
     * @param userId 用户 id
     * @param isValid 是否有效
     */
    @Update("UPDATE `user` SET `is_valid` WHERE `id` = #{userId}")
    void updateUserIsValid(@Param("userId") Integer userId, @Param("isValid") Integer isValid);

}
