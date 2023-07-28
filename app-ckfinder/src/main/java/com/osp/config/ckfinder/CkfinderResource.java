package com.osp.config.ckfinder;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Configuration
@EnableWebMvc
public class CkfinderResource implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/ckfinder/**").addResourceLocations("classpath:/static/ckfinder/");
        registry.addResourceHandler("/ckeditor/**").addResourceLocations("classpath:/static/ckeditor/");
    }

}
