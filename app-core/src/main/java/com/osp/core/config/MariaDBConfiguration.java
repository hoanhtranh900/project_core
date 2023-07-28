package com.osp.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {
            "com.osp.core.repository",
            "com.osp.core.entity"
    },
    entityManagerFactoryRef = "mariadbEntityManagerFactory",
    transactionManagerRef = "mariadbTransactionManager"
)
public class MariaDBConfiguration {
    @Autowired private Environment env;
    private String dbPrifix = "db.primary.datasource";

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mariadbEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mariaDataSource());
        em.setPackagesToScan(new String[] {
                "com.osp.core.entity"
        });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName(dbPrifix);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty(dbPrifix + ".dll-auto"));
        properties.put("hibernate.dialect", env.getProperty(dbPrifix + ".dialect"));
        properties.put("hibernate.show_sql", env.getProperty(dbPrifix + ".show_sql"));
        properties.put("hibernate.default_schema", env.getProperty(dbPrifix + ".default_schema"));
        properties.put("hibernate.enable_lazy_load_no_trans", true);
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public DataSource mariaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty(dbPrifix + ".driverClassName"));
        dataSource.setUrl(env.getProperty(dbPrifix + ".url"));
        dataSource.setUsername(env.getProperty(dbPrifix + ".username"));
        dataSource.setPassword(env.getProperty(dbPrifix + ".password"));
        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager mariadbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mariadbEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public DataSourceInitializer mariadbInitializer(@Qualifier("mariaDataSource") final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        if (env.getProperty(dbPrifix + ".dll-auto").equals("create")) {
            resourceDatabasePopulator.addScript(new ClassPathResource("/data.sql"));
        }
        resourceDatabasePopulator.setContinueOnError(true);
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

}
