package com.jaeholee.coupon.common.config.db;

import com.jaeholee.coupon.common.util.CommonDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Coupon db config.
 * Author : wdowon@gmail.com
 * basePackages 위치에 있는 entity 사용
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.jaeholee.coupon.coupon",
        entityManagerFactoryRef = "couponDBEntityManager",
        transactionManagerRef = "couponDBTransactionManager"
)
public class CouponDBConfig {

    private final Environment env;

    private String basePackage = "com.jaeholee.coupon.coupon";

    @Autowired
    public CouponDBConfig(Environment env) {
        this.env = env;
    }

    /**
     * Coupon db entity manager local container entity manager factory bean.
     *
     * @return the local container entity manager factory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean couponDBEntityManager() {
        DataSource dataSource = couponDBDataSource();
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                CommonDBUtil.getDBEntityManager(env, basePackage, dataSource);

        return localContainerEntityManagerFactoryBean;
    }

    /**
     * Coupon db data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource couponDBDataSource() {
        DriverManagerDataSource dataSource = CommonDBUtil.getDataSource(env, "coupon");

        return dataSource;
    }

    /**
     * Coupon db transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager couponDBTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(couponDBEntityManager().getObject());

        return transactionManager;
    }
}
