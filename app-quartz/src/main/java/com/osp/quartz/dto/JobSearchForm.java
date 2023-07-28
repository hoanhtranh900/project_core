package com.osp.quartz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Getter
@Setter
@NoArgsConstructor
public class JobSearchForm {
    private String jobName;
    private String jobGroup;
    private String jobStatus;

    public JobSearchForm(String jobName, String jobGroup, String jobStatus) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobStatus = jobStatus;
    }
}
