package jayfeng.com.meituan.manager.useraccesskey.constant;

/**
 * Cookie 常量
 * @author JayFeng
 * @date 2020/4/06
 */
public enum CookieConstant {

    MANAGER_KEY("manageruuid"),

//    DO_MAIN("mymeituan.com"),
    DO_MAIN("127.0.0.1"),
    PATH("/");

    private String message;

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

}
