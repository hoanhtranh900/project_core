package com.osp.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true, proxyTargetClass = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
}
