package com.jaeholee.coupon.common.util;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

/**
 * Common db util.
 * 공통 DB 설정 유틸
 * author : wdowon@gmail.com
 */
public class CommonDBUtil {

    /**
     * Gets db entity manager.
     *
     * @param env        the env (classpath:application.properties)
     * @param dataSource the data source
     * @return the db entity manager
     */
    public static LocalContainerEntityManagerFactoryBean getDBEntityManager(Environment env,
                                                                            String basePackages,
                                                                            DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan(basePackages);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * Gets data source.
     *
     * @param env    the env (classpath:application.properties)
     * @param dbName the db name
     * @return the data source
     */
    public static DriverManagerDataSource getDataSource(Environment env, String dbName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty(dbName + ".datasource.driver-class-name")));
        dataSource.setUrl(Objects.requireNonNull(env.getProperty(dbName + ".datasource.url")));
        dataSource.setUsername(Objects.requireNonNull(env.getProperty(dbName + ".datasource.username")));
        dataSource.setPassword(Objects.requireNonNull(env.getProperty(dbName + ".datasource.password")));

        return dataSource;
    }

}
