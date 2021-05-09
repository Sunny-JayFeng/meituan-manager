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
 * 骑手数据源配置
 * @author JayFeng
 * @date 2021/5/8
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.account.accesskey.management.dao.courier", sqlSessionFactoryRef = "courierSqlSessionFactory")
public class CourierDBConfig {

    @Autowired
    @Qualifier("courierDataSource")
    private DataSource courierDataSource;

    @Autowired
    @Qualifier("mybatisPlusPaginationInterceptor")
    private MybatisPlusInterceptor paginationInterceptor;

    @Bean(name = "courierSqlSessionFactory")
    public MybatisSqlSessionFactoryBean courierSqlSessionFactory() {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(courierDataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        factoryBean.setPlugins(paginationInterceptor);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate courierSqlSessionTemplate(@Qualifier("courierSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager courierTransactionManager() {
        return new DataSourceTransactionManager(courierDataSource);
    }

}
