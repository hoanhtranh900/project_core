package com.osp.config.ckfinder;

import com.ckfinder.connector.ConnectorServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CKFinderServletConfig {

    @Bean
    public ServletRegistrationBean connectCKFinder(){
        ServletRegistrationBean registrationBean=new ServletRegistrationBean(new ConnectorServlet(),"/ckfinder/core/connector/java/connector.java");
        registrationBean.addInitParameter("XMLConfig","classpath:/ckfinder.xml");
        registrationBean.addInitParameter("debug","true");
        registrationBean.addInitParameter("configuration","com.osp.config.ckfinder.CKFinderConfig");
        return registrationBean;
    }

}
