package com.osp.core.dto.request;

import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {
    private boolean init = false;

    private String userId;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String description;
    private String status;
    private String isDelete;
    private String username;
    private String phoneNumber;
    private String typeRegistration;

    // AdmParaSystem
    private String name;
    private String value;

    // AdmRight
    private String parentId;
    private String rightCode;
    private String rightName;
    private String rightOrder;
    private String urlRewrite;
    private String iconUrl;
    private String badgeSql;

    // AdmAuthorities
    private String keyword;
    private String authKey;
    private String authoritieName;
    private String groupName;
    private Date fromDate;
    private Date toDate;

    // wso2is
    private String attributes;
    private String filter;
    private String startIndex;
    private String count;
    private String domain;

    // SchedulerJobInfo
    private String type;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private String jobFullName;
    private String cronExpression;
    private String repeatTime;
    private Boolean cronJob;
    private String typeSyn;
    private String month;
    private String year;
    private String day;
    private String hour;
    private String minute;
    private String weekDay;
    private String weekTime;

    // AdmDept
    private String deptName;
    private String deptDesc;

    // CmCommune
    private String communeName;
    private String communeCode;

    //CmDistrict
    private String districtName;
    private String districtCode;

    // CmProvince
    private String provinceCode;
    private String provinceName;
}
