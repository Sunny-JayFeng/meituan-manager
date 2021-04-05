package jayfeng.com.meituan.manager.useraccesskey.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 普通用户数据源配置
 * @author JayFeng
 * @date 2021/4/5
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.manager.useraccesskey.dao.user", sqlSessionFactoryRef = "userAccountSqlSessionFactory")
public class UserAccountDBConfig {

    @Autowired
    @Qualifier("userAccountDataSource")
    private DataSource userAccountDataSource;

    @Bean
    public SqlSessionFactory userAccountSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(userAccountDataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate userAccountSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(userAccountSqlSessionFactory());
    }

}
