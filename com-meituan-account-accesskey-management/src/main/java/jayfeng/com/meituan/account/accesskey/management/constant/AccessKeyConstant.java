package jayfeng.com.meituan.account.accesskey.management.constant;

/**
 * 短信/邮件密钥常量
 * @author JayFeng
 * @date 2021/4/2
 */
public enum AccessKeyConstant {

    // 短信验证码
    SHORT_MESSAGE_CODE(0);

    AccessKeyConstant(Integer accessKeyType) {
        this.accessKeyType = accessKeyType;
    }

    private Integer accessKeyType;

    public Integer getAccessKeyType() {
        return this.accessKeyType;
    }

}
