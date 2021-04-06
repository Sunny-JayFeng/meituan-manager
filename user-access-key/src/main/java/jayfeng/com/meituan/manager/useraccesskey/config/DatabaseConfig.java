package jayfeng.com.meituan.manager.useraccesskey.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 多数据源配置
 * @author JayFeng
 * @date 2021/4/5
 */
@Configuration
public class DatabaseConfig {

    /**
     * 普通用户数据源
     * @return 返回数据源
     */
    @Bean
    @ConfigurationProperties("spring.datasource.user-account")
    public DataSource userAccountDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 密钥数据源
     * @return 返回数据源
     */
    @Bean
    @ConfigurationProperties("spring.datasource.manager")
    public DataSource managerDataSource() {
        return DataSourceBuilder.create().build();
    }

}
