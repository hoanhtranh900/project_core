package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
@Builder
@Getter
@Setter
@Entity
@Table( name = "T_SCHEDULER_JOB_HISTORY", indexes = {
        @Index(name = "JOB_NAME", columnList = "JOB_NAME", unique = false),
})
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SchedulerJobHistory extends Auditable implements Serializable {

    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;

    @Column(name = "JOB_NAME", length = 50)
    private String jobName;

    @Column(name = "TIME_SUCCESS")
    private Date timeSuccess;

    @Column(name = "STATUS")
    private Long status;
}

