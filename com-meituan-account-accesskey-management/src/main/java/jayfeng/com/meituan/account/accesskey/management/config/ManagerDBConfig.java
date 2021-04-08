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
 * @author JayFeng
 * @date 2021/4/6
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.manager.useraccesskey.dao.manager", sqlSessionFactoryRef = "managerSqlSessionFactory")
public class ManagerDBConfig {

    @Autowired
    @Qualifier("managerDataSource")
    private DataSource managerDataSource;

    @Autowired
    @Qualifier("mybatisPlusPaginationInterceptor")
    private MybatisPlusInterceptor paginationInterceptor;

    @Bean(name = "managerSqlSessionFactory")
    public MybatisSqlSessionFactoryBean managerSqlSessionFactory() {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(managerDataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        factoryBean.setPlugins(paginationInterceptor);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate managerSqlSessionTemplate(@Qualifier("managerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager managerTransactionManager() {
        return new DataSourceTransactionManager(managerDataSource);
    }

}
