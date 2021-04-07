package jayfeng.com.meituan.manager.useraccesskey.dao.manager;

import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员数据持久
 * @author JayFeng
 * @date 2021/4/6
 */
@Repository
public interface ManagerDao {

    /**
     * 根据 管理员 id 查出管理员对象
     * @param managerId 管理员 id
     * @return 返回管理员对象
     */
    @Select("SELECT `id`, `head_image`, `manager_name`, `rank` FROM `manager` WHERE `id` = #{managerId}")
    Manager selectManagerById(@Param("managerId") Integer managerId);

    /**
     * 根据账号查出一个管理员 -- 登录
     * @param managerName 账号
     * @return 返回对象
     */
    @Select("SELECT `id`, `head_image`, `manager_name`, `password`, `rank` FROM `manager` WHERE `manager_name` = #{managerName}")
    Manager selectManagerByName(@Param("managerName") String managerName);

    /**
     * 根据 职级 查出所有管理员id
     * @param rankList 职级列表
     * @return 管理员id 列表
     */
    @Select("SELECT `id` FROM `manager` WHERE `rank` in (${rankList})")
    List<Integer> selectManagerIdsByRank(@Param("rankList") String rankList);

}
