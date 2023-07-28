package com.osp.quartz.service;

import com.osp.core.entity.SchedulerJobInfo;
import com.osp.quartz.dto.JobSearchForm;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Service
public interface SchedulerService {

    /*
    * restore JOB
    * cronExpression khởi tạo mặc định
    * */
    void restoreJob(String jobName, String jobGroup, String cronExpression, Class<? extends QuartzJobBean> jobClass);

    /*
    * Tạo mới JOB
    * */
    void scheduleNewJob(SchedulerJobInfo jobInfo);

    /*
     * tạo mới JOB theo time nhất định
     * */
    boolean scheduleOneTimeJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass, Date date);

    /*
     * tạo mới JOB theo cronExpression
     * */
    boolean scheduleCronJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass, Date date, String cronExpression);

    /*
     * cập nhật JOB
     * */
    void updateScheduleJob(SchedulerJobInfo jobInfo);

    /*
     * cập nhật JOB theo time nhất định
     * */
    boolean updateOneTimeJob(String jobName, Date date);

    /*
     * cập nhật JOB theo cronExpression
     * */
    boolean updateCronJob(String jobName, Date date, String cronExpression);

    /*
     * start JOB ngay lập tức
     * */
    boolean startJobNow(SchedulerJobInfo jobInfo);

    /*
     * start JOB ngay lập tức
     * */
    boolean startJobNow(String jobName, String jobGroup);

    /*
     * Hủy đặt lịch vào lần tiếp theo
     * */
    boolean unScheduleJob(String jobName);

    /*
     * Xóa JOB
     * */
    boolean deleteJob(SchedulerJobInfo jobInfo);

    /*
     * Xóa JOB
     * */
    boolean deleteJob(String jobName, String jobGroup);

    /*
     * tạm dừng JOB
     * */
    boolean pauseJob(SchedulerJobInfo jobInfo);

    /*
     * tạm dừng JOB
     * */
    boolean pauseJob(String jobName, String jobGroup);

    /*
     * tiếp tục chạy JOB
     * */
    boolean resumeJob(SchedulerJobInfo jobInfo);

    /*
     * tiếp tục chạy JOB
     * */
    boolean resumeJob(String jobName, String jobGroup);

    /*
     * dừng JOB
     * */
    boolean stopJob(String jobName, String jobGroup);

    /*
     * check JOB đang chạy
     * */
    boolean isJobRunning(String jobName, String jobGroup);

    /*
     * lấy list JOB
     * */
    List<Map<String, Object>> getAllJobs(JobSearchForm jobSearch);

    /*
    * check tồn tại JOB
    * */
    boolean isJobWithNamePresent(String jobName, String jobGroup);

    /*
    * Lấy trạng thái JOB
    * */
    String getJobState(String jobName, String jobGroup);

    /*
    * Lấy chi tiết JOB
    * */
    List<Map<String, Object>> jobDetail(String jobName, String jobGroup, String jobStatus);
}
