package jayfeng.com.meituan.account.accesskey.management.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 普通用户数据源配置
 * @author JayFeng
 * @date 2021/4/5
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.account.accesskey.management.dao.user", sqlSessionFactoryRef = "userSqlSessionFactory")
public class UserDBConfig {

    @Autowired
    @Qualifier("userDataSource")
    private DataSource userDataSource;

    @Autowired
    @Qualifier("mybatisPlusPaginationInterceptor")
    private MybatisPlusInterceptor paginationInterceptor;

    @Bean(name = "userSqlSessionFactory")
    public MybatisSqlSessionFactoryBean userSqlSessionFactory() {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(userDataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        factoryBean.setPlugins(paginationInterceptor);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager userTransactionManager() {
        return new DataSourceTransactionManager(userDataSource);
    }

}
