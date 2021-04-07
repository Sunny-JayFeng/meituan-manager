package jayfeng.com.meituan.manager.useraccesskey.bean;

import lombok.Data;

/**
 * 管理员允许访问的 api
 * @author JayFeng
 * @date 2021/4/7
 */
@Data
public class ManagerApi {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 管理员 id
     */
    private Integer managerId;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口路径
     */
    private String apiPath;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
