package xin.spring.bless.javafx.db.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import xin.spring.bless.javafx.db.YamlDataBase;
import xin.spring.bless.javafx.db.config.database.DataBase;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * jpa配置类
 */
@Configuration
@ComponentScan(basePackages = {"xin.spring.bless.javafx"})
@EnableJpaRepositories( basePackages = {"xin.spring.bless.javafx.db.repositories"})
public class JpaConfig {

    // 名字必须是entityManagerFactory,或者把@bean中name属性设置为entityManagerFactory
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        // 设置数据库(如果在hibernate中配置了连接池,则不需要设置)
        // em.setDataSource(dataSource());
        // 指定Entity所在的包
        em.setPackagesToScan("xin.spring.bless.javafx.common.pojo");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        // 配置属性
        Properties properties = new Properties();
        // 会话中获取连接数据源
        DataBase dataBase = YamlDataBase.newInstance().decode();

        properties.setProperty("hibernate.connection.driver_class", dataBase.getHibernate().getConnection().getDriverClass());
        properties.setProperty("hibernate.connection.url", dataBase.getHibernate().getConnection().getUrl());
        properties.setProperty("hibernate.connection.username", dataBase.getHibernate().getConnection().getUsername());
        properties.setProperty("hibernate.connection.password", dataBase.getHibernate().getConnection().getPassword());
        properties.setProperty("hibernate.dialect", dataBase.getHibernate().getDialect());
        properties.setProperty("hibernate.connection.provider_class", dataBase.getHibernate().getConnection().getProviderClass());
        properties.setProperty("hibernate.c3p0.min_size", dataBase.getHibernate().getC3p0().getMinSize());
        properties.setProperty("hibernate.c3p0.max_size", dataBase.getHibernate().getC3p0().getMaxSize());
        properties.setProperty("hibernate.hbm2ddl.auto", dataBase.getHibernate().getHbm2ddl().getAuto());
        properties.setProperty("hibernate.show_sql", dataBase.getHibernate().getShowSql());
        properties.setProperty("format_sql", dataBase.getHibernate().getFormatSql());
        em.setJpaProperties(properties);
        return em;
    }

    // 名字必须是transactionManager,或者把@bean中name属性设置为transactionManager
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

}
