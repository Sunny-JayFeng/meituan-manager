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
 * 商家数据源配置
 * @author JayFeng
 * @date 2021/4/8
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.account.accesskey.management.dao.seller", sqlSessionFactoryRef = "sellerSqlSessionFactory")
public class SellerDBConfig {

    @Autowired
    @Qualifier("sellerDataSource")
    private DataSource sellerDataSource;

    @Autowired
    @Qualifier("mybatisPlusPaginationInterceptor")
    private MybatisPlusInterceptor paginationInterceptor;

    @Bean(name = "sellerSqlSessionFactory")
    public MybatisSqlSessionFactoryBean sellerSqlSessionFactory() {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(sellerDataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        factoryBean.setPlugins(paginationInterceptor);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate sellerSqlSessionTemplate(@Qualifier("sellerSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager sellerTransactionManager() {
        return new DataSourceTransactionManager(sellerDataSource);
    }

}
