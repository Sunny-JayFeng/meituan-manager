package jayfeng.com.meituan.manager.useraccesskey.config;

import jayfeng.com.meituan.manager.useraccesskey.interceptor.ManagerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 * @author JayFeng
 * @date 2021/4/6
 */
@Configuration
public class ManagerInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private ManagerInterceptor managerInterceptor;

    /**
     * 添加拦截器
     * @param registry 注册拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerInterceptor)
                .addPathPatterns("/meituan/manager/**")
                .excludePathPatterns("/meituan/manager/login",
                        "/meituan/manager/*.html",
                        "/meituan/manager/*.css",
                        "/meituan/manager/*.js");
    }

}
