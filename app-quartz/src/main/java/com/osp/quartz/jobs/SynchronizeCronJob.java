package com.osp.quartz.jobs;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import java.io.File;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Service
public class SynchronizeCronJob extends QuartzJobBean {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {

        File f = new File("C:\\");
        System.out.println("Printing the total space");
        System.out.println(f.getTotalSpace() +" bytes");
        System.out.println(f.getTotalSpace()/1000.00 +" Kilobytes");
        System.out.println(f.getTotalSpace()/1000000.00 +" Megabytes");
        System.out.println(f.getTotalSpace()/1000000000.00 +" Gigabytes");
        System.out.println("----------------------------");
        System.out.println("Printing the free space");
        System.out.println(f.getFreeSpace() +" bytes");
        System.out.println(f.getFreeSpace()/1000.00 +" Kilobytes");
        System.out.println(f.getFreeSpace()/1000000.00 +" Megabytes");
        System.out.println(f.getFreeSpace()/1000000000.00 +" Gigabytes");

    }

}
