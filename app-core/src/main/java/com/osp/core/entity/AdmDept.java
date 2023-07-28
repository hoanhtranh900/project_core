package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.UtilsCommon;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_DEPT", indexes = {
        @Index(name = "DEPT_NAME", columnList = "DEPT_NAME", unique = false),
        @Index(name = "DEPT_CODE", columnList = "DEPT_CODE", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmDept extends Auditable implements Serializable {

    @JsonIgnore
    @ManyToMany(mappedBy = "groupDepts", fetch = FetchType.LAZY)
    private List<AdmGroup> groupDepts;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_DEPT_USER",
            joinColumns = @JoinColumn(name = "DEPT_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<AdmUser> deptUsers;

    @Comment("Tên phòng ban")
    @Column(name = "DEPT_NAME", length = 100)
    private String deptName;

    @Comment("Mã phòng ban")
    @Column(name = "DEPT_CODE", length = 100, unique = true)
    private String deptCode;

    @Comment("Mô tả")
    @Column(name = "DEPT_DESC", length = 200)
    private String deptDesc;

    @Comment("id cấp n-1")
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    public AdmDept formToBo(AdmDept form, AdmDept bo) {
        bo.setModifiedBy(UtilsCommon.getUserNameLogin());
        bo.setModifiedDate(new Date());
        bo.setDeptName(form.getDeptName());
        bo.setDeptCode(form.getDeptCode());
        bo.setDeptDesc(form.getDeptDesc());
        bo.setParentId(form.getParentId());
        return bo;
    }

    @Transient
    private boolean expand = false;
    @Transient
    private int level = 0;
    @Transient
    private List<AdmDept> children = new ArrayList<>();
    @Transient
    private String statusStr;
    @Transient
    private String isDeleteStr;

    public String getStatusStr() {
        return ConstantString.STATUS.getStatusStr(this.status);
    }

    public String getIsDeleteStr() {
        return ConstantString.IS_DELETE.getStatusStr(this.isDelete);
    }

    public int getLevel() {
        if (parentId.compareTo(0L) != 0) {
            return 1;
        }
        return level;
    }

}
