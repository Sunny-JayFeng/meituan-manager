package jayfeng.com.meituan.account.accesskey.management.constant;

/**
 * Cookie 常量
 * @author JayFeng
 * @date 2020/4/06
 */
public enum CookieConstant {

    MANAGER_KEY("manageruuid"),

//    DO_MAIN("mymeituan.com"),
    DO_MAIN("127.0.0.1"),

    PATH("/"),

    MAX_AGE(3600 * 24);

    private String message;

    private Integer maxAge;

    CookieConstant(Integer maxAge) {
        this.maxAge = maxAge;
    }

    CookieConstant(String message) {
        this.message = message;
    }

    public String getCookieKey() {
        return this.message;
    }

    public String getCookieDoMain() {
        return this.message;
    }

    public String getCookiePath() {
        return this.message;
    }

    public Integer getMaxAge() {
        return this.maxAge;
    }
}
