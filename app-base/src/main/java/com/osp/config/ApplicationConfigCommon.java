package com.osp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
public class ApplicationConfigCommon {
    /*
    * dev
    * */
    private static final Resource[] DEV_PROPERTIES = new ClassPathResource[]{new ClassPathResource("application-dev.properties"),};
    /*
    * thử nghiệm dev
    * */
    private static final Resource[] SIT_PROPERTIES = new FileSystemResource[]{new FileSystemResource("./config/application-sit.properties"),};
    /*
    * prod
    * */
    private static final Resource[] PROD_PROPERTIES = new FileSystemResource[]{new FileSystemResource("./config/application-prod.properties"),};
    /*
    * thử nghiệm prod
    * */
    private static final Resource[] UAT_PROPERTIES = new FileSystemResource[]{new FileSystemResource("./config/application-uat.properties"),};

    @Component
    @Profile("dev")
    public static class DevConfig {

        private DevConfig() {}

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(DEV_PROPERTIES);
            return pspc;
        }
    }

    @Component
    @Profile("sit")
    public static class TestConfig {

        private TestConfig() {}

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(SIT_PROPERTIES);
            return pspc;
        }
    }

    @Component
    @Profile("prod")
    public static class ProdConfig {

        private ProdConfig() {}

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(PROD_PROPERTIES);
            return pspc;
        }
    }

    @Component
    @Profile("uat")
    public static class UatConfig {

        private UatConfig() {}

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(UAT_PROPERTIES);
            return pspc;
        }
    }
}
