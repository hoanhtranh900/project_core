package com.osp.config;

import com.osp.core.contants.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Configuration
@EnableWebMvc
public class RestApiConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(Constants.webAddress.split("\\s*,\\s*"))
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("Origin", "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Headers", "Content-Type", "Authorization", "X-Requested-With",
                        "Accept", "X-Requested-With", "remember-me", "authorization", "x-auth-token",
                        "reportProgress", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .allowCredentials(false)
                .maxAge(3600L)
        ;

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootPath = Constants.ckfinder_baseDir;
        File dir = new File(rootPath);
        String dirTrue = dir.getAbsolutePath();
        registry
                .addResourceHandler("/f/**")
                .addResourceLocations("file:///" + dirTrue);
    }

}
