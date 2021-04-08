package jayfeng.com.meituan.account.accesskey.management.dao.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jayfeng.com.meituan.account.accesskey.management.bean.ManagerApi;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员接口数据持久层
 * @author JayFeng
 * @date 2021/4/7
 */
@Repository
public interface ManagerApiDao extends BaseMapper<ManagerApi> {

    /**
     * 给某个管理员分配一个 api 访问接口权限
     * @param managerApi 访问接口
     */
    @Insert("INSERT INTO  `manager_api`(`manager_id`, `api_name`, `api_path`, `create_time`, `update_time`)" +
            "VALUES(#{managerApi.managerId}, #{managerApi.apiName}, #{managerApi.apiPath}, #{managerApi.createTime}, #{managerApi.updateTime})")
    void insertManagerApi(@Param("managerApi") ManagerApi managerApi);

    /**
     * 根据接口路径和管理员id查出接口对象
     * @param apiPath 接口 id
     * @param managerId 管理员 id
     * @return 返回接口对象
     */
    @Select("SELECT * FROM `manager_api` WHERE `api_path` = #{apiPath} AND `manager_id` = #{managerId}")
    ManagerApi selectByIdAndManagerId(@Param("apiPath") String apiPath, @Param("managerId") Integer managerId);

    /**
     * 根据 管理员 id 查询 允许访问的接口列表
     * @param managerId 管理员 id
     * @return 允许访问的接口列表
     */
    @Select("SELECT `api_path` FROM `manager_api` WHERE `manager_id` = #{managerId}")
    List<String> selectApiListByManagerId(@Param("managerId") Integer managerId);

    /**
     * 根据接口路径和管理员id列表查询数据
     * @param apiPath 接口路径
     * @param managerIdList 管理员id列表
     * @return 返回数据
     */
    @Select("SELECT `id`, `manager_id`, `api_name`, `api_path` FROM `manager_api` WHERE `api_path` = #{apiPath} AND `manager_id` in ( ${managerIdList} )")
    List<ManagerApi> selectManagerApiByPathAndManagerIds(@Param("apiPath") String apiPath, @Param("managerIdList") String managerIdList);

    /**
     * 根据 id 验证接口是否存在
     * @param id id
     * @return 返回结果
     */
    @Select("SELECT `id` FROM `manager_api` WHERE `id` = #{id}")
    Integer selectManagerApiIdById(@Param("id") Integer id);

    /**
     * 根据 管理员id 和接口路径，判断权限是否已分配
     * @param managerId 管理员 id
     * @param apiPath 接口路径
     * @return 返回结果
     */
    @Select("SELECT `id` FROM `manager_api` WHERE `manager_id` = #{managerId} AND `api_path` = #{apiPath}")
    Integer selectManagerApiIdByManagerIdAndPath(@Param("managerId") Integer managerId, @Param("apiPath") String apiPath);

    /**
     * 根据 id 删除权限分配
     * @param managerApiId id
     */
    @Delete("DELETE FROM `manager_api` WHERE `id` = #{managerApiId}")
    void deleteManagerApiById(@Param("managerApiId") Integer managerApiId);

    /**
     * 根据接口路径和管理员id取消访问权限
     * @param apiPath 接口路径
     * @param managerIds 管理员id列表
     */
    @Delete("DELETE FROM `manager_api` WHERE `api_path` = #{apiPath} AND `manager_id` in ( ${managerIds} )")
    void deleteManagerApiByPathAndManagerIds(@Param("apiPath") String apiPath, @Param("managerIds") String managerIds);
}
