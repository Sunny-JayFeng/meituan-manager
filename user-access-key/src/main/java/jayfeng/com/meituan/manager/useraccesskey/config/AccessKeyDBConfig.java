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
 * 密钥数据源
 * @author JayFeng
 * @date 2021/4/5
 */
@Configuration
@MapperScan(basePackages = "jayfeng.com.meituan.manager.useraccesskey.dao.accesskey", sqlSessionFactoryRef = "accessKeySqlSessionFactory")
public class AccessKeyDBConfig {

    @Autowired
    @Qualifier("accessKeyDataSource")
    private DataSource accessKeyDataSource;

    @Bean
    public SqlSessionFactory accessKeySqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(accessKeyDataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate accessKeySqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(accessKeySqlSessionFactory());
    }

}
