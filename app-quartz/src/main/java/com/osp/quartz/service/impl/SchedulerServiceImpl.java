package com.osp.quartz.service.impl;

import com.osp.core.repository.SchedulerJobInfoRepository;
import com.osp.quartz.component.JobScheduleCreator;
import com.osp.quartz.dto.JobSearchForm;
import com.osp.core.entity.SchedulerJobInfo;
import com.osp.quartz.jobs.SynchronizeCronJob;
import com.osp.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Slf4j
@Transactional
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired private ApplicationContext context;
    @Autowired private JobScheduleCreator scheduleCreator;
    @Autowired private SchedulerJobInfoRepository schedulerJobInfoRepository;

    @Override
    public void restoreJob(String jobName, String jobGroup, String cronExpression, Class<? extends QuartzJobBean> jobClass) {
        deleteJob(jobName, jobGroup);
        if (!isJobWithNamePresent(jobName, jobGroup)) {
            Optional<SchedulerJobInfo> jobInfoOpt = schedulerJobInfoRepository.findByJobName(jobName);
            if (jobInfoOpt.isPresent()) {
                cronExpression = jobInfoOpt.get().getCronExpression();
            }
            scheduleCronJob(jobName, jobGroup, jobClass, new Date(), cronExpression);
            startJobNow(jobName, jobGroup);
        }
    }

    @Override
    public void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass())).withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();

            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = this.scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, this.context, jobInfo.getJobName(), jobInfo.getJobGroup());

                Trigger trigger;
                if (jobInfo.getCronJob()) {
                    trigger = this.scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {
                    trigger = this.scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }

                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean scheduleOneTimeJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass, Date date) {

        String jobKey = jobName;
        String triggerKey = jobName;

        JobDetail jobDetail = this.scheduleCreator.createJob(jobClass, false, this.context, jobKey, jobGroup);

        Trigger cronTriggerBean = JobScheduleCreator.createSingleTrigger(triggerKey, date, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        try {
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean scheduleCronJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass, Date date, String cronExpression) {

        String jobKey = jobName;
        String triggerKey = jobName;

        JobDetail jobDetail = this.scheduleCreator.createJob(jobClass, false, this.context, jobKey, jobGroup);

        Trigger cronTriggerBean = this.scheduleCreator.createCronTrigger(triggerKey, date, cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        try {
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
            return true;
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while scheduling job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void updateScheduleJob(SchedulerJobInfo jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {
            newTrigger = this.scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(), jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            newTrigger = this.scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        try {
            this.schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean updateOneTimeJob(String jobName, Date date) {

        String jobKey = jobName;

        try {
            Trigger newTrigger = JobScheduleCreator.createSingleTrigger(jobKey, date, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Date dt = this.schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobKey), newTrigger);
            return true;
        } catch (Exception e) {
            System.out.println("SchedulerException while updating one time job with key :" + jobKey + " message :" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCronJob(String jobName, Date date, String cronExpression) {

        String jobKey = jobName;

        try {
            Trigger newTrigger = this.scheduleCreator.createCronTrigger(jobKey, date, cronExpression, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Date dt = this.schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobKey), newTrigger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            this.schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean startJobNow(String jobName, String jobGroup) {

        String jobKey = jobName;

        JobKey jKey = new JobKey(jobKey, jobGroup);
        try {
            this.schedulerFactoryBean.getScheduler().triggerJob(jKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unScheduleJob(String jobName) {
        try {
            return this.schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
        } catch (SchedulerException e) {
            log.error("Failed to un-schedule job - {}", jobName, e);
            return false;
        }
    }

    @Override
    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            return this.schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroup) {

        String jobKey = jobName;

        JobKey jkey = new JobKey(jobKey, jobGroup);

        try {
            boolean status = this.schedulerFactoryBean.getScheduler().deleteJob(jkey);
            return status;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            this.schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean pauseJob(String jobName, String jobGroup) {

        String jobKey = jobName;
        JobKey jkey = new JobKey(jobKey, jobGroup);

        try {
            this.schedulerFactoryBean.getScheduler().pauseJob(jkey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            this.schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @Override
    public boolean resumeJob(String jobName, String jobGroup) {

        String jobKey = jobName;

        JobKey jKey = new JobKey(jobKey, jobGroup);
        try {
            this.schedulerFactoryBean.getScheduler().resumeJob(jKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean stopJob(String jobName, String jobGroup) {
        try {
            String jobKey = jobName;

            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            JobKey jkey = new JobKey(jobKey, jobGroup);

            return scheduler.interrupt(jkey);

        } catch (SchedulerException e) {
            System.out.println("SchedulerException while stopping job. error message :" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isJobRunning(String jobName, String jobGroup) {

        String jobKey = jobName;

        try {
            List<JobExecutionContext> currentJobs = this.schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    String jobNameDB = jobCtx.getJobDetail().getKey().getName();
                    String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
                    if (jobKey.equalsIgnoreCase(jobNameDB) && jobGroup.equalsIgnoreCase(groupNameDB)) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getAllJobs(JobSearchForm jobSearch) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();

            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    if (StringUtils.isNotBlank(jobSearch.getJobName()) && jobName.toLowerCase().indexOf(jobSearch.getJobName().toLowerCase()) == -1) {
                        continue;
                    }

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("jobName", jobName);
                    map.put("jobFullName", "jobFullName");
                    map.put("groupName", jobGroup);

                    String jobState = null;
                    if (isJobRunning(jobName, jobGroup)) {
                        //map.put("jobStatus", "RUNNING");
                        map.put("jobStatus", "Đang chạy");
                        jobState = "RUNNING";
                    } else {
                        jobState = getJobState(jobName, jobGroup);
                        if (jobState.equals("PAUSED")) {
                            jobState = "Tạm dừng";
                        } else if (jobState.equals("BLOCKED")) {
                            jobState = "Bị chặn";
                        } else if (jobState.equals("COMPLETE")) {
                            jobState = "Hoàn thành";
                        } else if (jobState.equals("ERROR")) {
                            jobState = "Lỗi";
                        } else if (jobState.equals("NONE")) {
                            jobState = "Không xác định";
                        } else if (jobState.equals("SCHEDULED")) {
                            jobState = "Đang đặt lịch";
                        }
                        map.put("jobStatus", jobState);
                    }

                    if (StringUtils.isNotBlank(jobSearch.getJobStatus()) && !jobState.toLowerCase().equals(jobSearch.getJobStatus().toLowerCase())) {
                        continue;
                    }

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);

                    Date scheduleTime = triggers.size() == 0 ? null : triggers.get(0).getStartTime();
                    Date nextFireTime = triggers.size() == 0 ? null : triggers.get(0).getNextFireTime();
                    Date lastFiredTime = triggers.size() == 0 ? null : triggers.get(0).getPreviousFireTime();

                    map.put("cronExpression", triggers.size() == 0 ? null : ((CronTriggerImpl) triggers.get(0)).getCronExpression());
                    map.put("scheduleTime", scheduleTime);
                    map.put("lastFiredTime", lastFiredTime);
                    map.put("nextFireTime", nextFireTime);

                    list.add(map);
                }

            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean isJobWithNamePresent(String jobName, String jobGroup) {
        try {

            JobKey jobKey = new JobKey(jobName, jobGroup);
            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while checking job with name and group exist:" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getJobState(String jobName, String jobGroup) {

        try {

            JobKey jobKey = new JobKey(jobName, jobGroup);

            Scheduler scheduler = this.schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
            if (triggers != null && triggers.size() > 0) {
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                    if (Trigger.TriggerState.PAUSED.equals(triggerState)) {
                        return "PAUSED";
                    } else if (Trigger.TriggerState.BLOCKED.equals(triggerState)) {
                        return "BLOCKED";
                    } else if (Trigger.TriggerState.COMPLETE.equals(triggerState)) {
                        return "COMPLETE";
                    } else if (Trigger.TriggerState.ERROR.equals(triggerState)) {
                        return "ERROR";
                    } else if (Trigger.TriggerState.NONE.equals(triggerState)) {
                        return "NONE";
                    } else if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
                        return "SCHEDULED";
                    }
                }
            }
        } catch (SchedulerException e) {
            System.out.println("SchedulerException while checking job with name and group exist:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> jobDetail(String jobName, String jobGroup, String jobStatus) {
        try {
            JobSearchForm jobSearch = new JobSearchForm(jobName, jobGroup, jobStatus);
            return getAllJobs(jobSearch);

        } catch (Exception e) {
            System.out.println("SchedulerException while stopping job. error message :" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
