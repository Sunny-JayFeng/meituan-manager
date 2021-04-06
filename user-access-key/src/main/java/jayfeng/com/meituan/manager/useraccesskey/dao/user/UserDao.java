package jayfeng.com.meituan.manager.useraccesskey.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jayfeng.com.meituan.manager.useraccesskey.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JayFeng
 * @date 2021/4/2
 */
@Repository
public interface UserDao extends BaseMapper<User> {

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
    @Select("SELECT * FROM `user` WHERE `id` in ( ${userIds} ) and `is_valid` = 0")
    List<User> selectUsersByUserIds(@Param("userIds") String userIds);

    /**
     * 查询已经注销 14 天的用户，删除
     * @param updateTime 更新时间
     * @return 返回用户列表
     */
    @Select("SELECT * FROM `user` WHERE `is_valid` = 0 AND `update_time` < #{updateTime}")
    List<User> selectInvalidUsersToDelete(@Param("updateTime") Long updateTime);

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
     * @param updateTime 更新时间
     */
    @Update("UPDATE `user` SET `is_valid` = #{isValid}, `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updateUserIsValid(@Param("userId") Integer userId, @Param("isValid") Integer isValid, @Param("updateTime") Long updateTime);

}
