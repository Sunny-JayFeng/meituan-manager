package jayfeng.com.meituan.manager.useraccesskey.dao.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jayfeng.com.meituan.manager.useraccesskey.bean.AccessKey;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * 短信密钥持久层
 * @author JayFeng
 * @date 2021/4/1
 */
@Repository
public interface AccessKeyDao extends BaseMapper<AccessKey> {

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     */
    @Insert("INSERT INTO `access_key`(`region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time`) " +
            "VALUES(#{accessKey.regionId}, #{accessKey.accessKeyId}, #{accessKey.secret}, #{accessKey.type}, #{accessKey.createTime}, #{accessKey.updateTime})")
    void addAccessKey(@Param("accessKey") AccessKey accessKey);

    /**
     * 根据 id 查询密钥
     * @param id id
     * @return 返回密钥
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time` FROM `access_key` WHERE `id` = #{id}")
    AccessKey selectAccessKey(@Param("id") Integer id);

    /**
     * 检查当前 accessKeyId 是否已经存在
     * @param accessKeyId accessKeyId
     * @return 返回 id
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time` FROM `access_key` WHERE `access_key_id` = #{accessKeyId}")
    AccessKey selectOneByAccessKeyId(@Param("accessKeyId") String accessKeyId);

    /**
     * 根据 id 删除密钥
     * @param id id
     */
    @Delete("DELETE FROM `access_key` WHERE `id` = #{id}")
    void deleteAccessKey(@Param("id") Integer id);

    /**
     * 根据 id 更新密钥信息
     * @param accessKey 密钥
     */
    @Update("UPDATE `access_key` SET `region_id` = #{accessKey.regionId}, `access_key_id` = #{accessKey.accessKeyId}, " +
            "`secret` = #{accessKey.secret}, `update_time` = #{accessKey.updateTime} WHERE `id` = #{accessKey.id}")
    void updateAccessKey(@Param("accessKey") AccessKey accessKey);

}
