package jayfeng.com.meituan.account.accesskey.management.config;

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
    @ConfigurationProperties("spring.datasource.user")
    public DataSource userDataSource() {
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

    /**
     * 商家数据源
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.seller")
    public DataSource sellerDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 骑手数据源
     * @return
     */
    @Bean
    @ConfigurationProperties("spring.datasource.courier")
    public DataSource courierDataSource() {
        return DataSourceBuilder.create().build();
    }

}
