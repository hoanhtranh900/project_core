package com.osp.quartz.config;

import com.osp.core.contants.Constants;
import com.osp.quartz.component.SchedulerJobFactory;
import com.osp.quartz.jobs.CategoryBufferJob;
import com.osp.quartz.jobs.SynchronizeCronJob;
import com.osp.quartz.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Configuration
public class SchedulerConfig {

    // @Autowired DataSource dataSource;
    @Autowired private ApplicationContext applicationContext;
    @Autowired private SchedulerService schedulerService;

    @Bean
    public void startQuartzJob() {
        String job = Constants.job_login_domain;
        String cronExpression = Constants.cron_login_domain;
        this.schedulerService.restoreJob(job, Constants.jobGroup, cronExpression, SynchronizeCronJob.class);

        job = Constants.job_CategoryBuffer;
        cronExpression = Constants.cron_CategoryBuffer;
        this.schedulerService.restoreJob(job, Constants.jobGroup, cronExpression, CategoryBufferJob.class);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {

        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(this.applicationContext);

        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        // factory.setDataSource(this.dataSource);
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(jobFactory);

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
