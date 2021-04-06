package jayfeng.com.meituan.manager.useraccesskey.dao.manager;

import jayfeng.com.meituan.manager.useraccesskey.bean.Manager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 管理员数据持久
 * @author JayFeng
 * @date 2021/4/6
 */
@Repository
public interface ManagerDao {

    /**
     * 根据账号查出一个管理员 -- 登录
     * @param managerName 账号
     * @return 返回对象
     */
    @Select("SELECT `id`, `head_image`, `manager_name`, `password`, `rank` FROM `manager` WHERE `manager_name` = #{managerName}")
    Manager selectManagerByName(@Param("managerName") String managerName);

}
