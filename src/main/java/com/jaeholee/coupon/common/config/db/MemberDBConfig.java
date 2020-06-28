package com.jaeholee.coupon.common.config.db;

import com.jaeholee.coupon.common.util.CommonDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Member db config.
 * Author : wdowon@gmail.com
 * basePackages 위치에 있는 entity 사용
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.jaeholee.coupon.member",
        entityManagerFactoryRef = "memberDBEntityManager",
        transactionManagerRef = "memberDBTransactionManager"
)
public class MemberDBConfig {

    private final Environment env;

    private String basePackage = "com.jaeholee.coupon.member";

    @Autowired
    public MemberDBConfig(Environment env) {
        this.env = env;
    }

    /**
     * Member db entity manager local container entity manager factory bean.
     *
     * @return the local container entity manager factory bean
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean memberDBEntityManager() {
        DataSource dataSource = memberDBDataSource();
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                CommonDBUtil.getDBEntityManager(env, basePackage, dataSource);

        return localContainerEntityManagerFactoryBean;
    }

    /**
     * Member db data source data source.
     *
     * @return the data source
     */
    @Bean
    @Primary
    public DataSource memberDBDataSource() {
        DriverManagerDataSource dataSource = CommonDBUtil.getDataSource(env, "member");

        return dataSource;
    }

    /**
     * Member db transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    @Primary
    public PlatformTransactionManager memberDBTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(memberDBEntityManager().getObject());

        return transactionManager;
    }
}
