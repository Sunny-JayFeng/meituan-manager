package jayfeng.com.meituan.account.accesskey.management.bean;

import lombok.Data;

/**
 * 管理员实体类
 * @author JayFeng
 * @date 2021/4/6
 */
@Data
public class Manager {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 账号
     */
    private String managerName;

    /**
     * 密码
     */
    private String password;

    /**
     * 职级
     */
    private Integer rank;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
