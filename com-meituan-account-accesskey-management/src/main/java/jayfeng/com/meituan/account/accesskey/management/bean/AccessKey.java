package jayfeng.com.meituan.account.accesskey.management.bean;

import lombok.Data;

/**
 * 手机短信密钥
 * @author JayFeng
 * @date 2021/4/1
 */
@Data
public class AccessKey {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 发送源 id
     */
    private String regionId;

    /**
     * 密钥 id
     */
    private String accessKeyId;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 类型(用途)
     * 0 -- 用户短信验证码
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
