
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO: write you class description here
 *
 * @author
 */
@Builder
@Entity
@Table(name = "T_SCHEDULER_JOB_INFO", indexes = {
        @Index(name = "JOB_NAME", columnList = "JOB_NAME", unique = false),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SchedulerJobInfo extends Auditable implements Serializable {

    @Column(name = "JOB_NAME", length = 50)
    private String jobName;

    @Column(name = "JOB_FULL_NAME", length = 100)
    private String jobFullName;

    @Column(name = "JOB_GROUP", length = 50)
    private String jobGroup;

    @Column(name = "JOB_CLASS")
    private String jobClass;

    @Column(name = "CRON_EXPRESSION", length = 50)
    private String cronExpression;

    @Column(name = "REPEAT_TIME")
    private Long repeatTime;

    @Column(name = "CRON_JOB")
    private Boolean cronJob;

    @Column(name = "TYPE_SYN", length = 50)
    private String typeSyn;

    @Column(name = "MONTH")
    private Long month;

    @Column(name = "YEAR", length = 10)
    private Long year;

    @Column(name = "DAY", length = 1)
    private String day;

    @Column(name = "HOUR")
    private Long hour;

    @Column(name = "MINUTE")
    private Long minute;

    @Column(name = "WEEK_DAY", length = 50)
    private String weekDay;

    @Column(name = "WEEK_TIME", length = 50)
    private String weekTime;

    @Column(name = "SCHEDULE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleTime;
}
