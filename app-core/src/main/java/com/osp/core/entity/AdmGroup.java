package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_GROUP", indexes = {
        @Index(name = "GROUP_NAME", columnList = "GROUP_NAME", unique = false),
        @Index(name = "STATUS", columnList = "STATUS", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmGroup extends Auditable implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_AUTHORITIES",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY"))
    private List<AdmAuthorities> groupAuthorities;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_DEPT",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEPT_ID"))
    private List<AdmDept> groupDepts;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_USER",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<AdmUser> groupUsers;

    @Comment("Tên nhóm quyền")
    @Column(name = "GROUP_NAME", length = 100)
    private String groupName;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("Mô tả")
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Transient
    private List<Long> keys;

    //update
    public AdmGroup formToBo(AdmGroup form, AdmGroup bo) {
        bo.setGroupName(form.getGroupName());
        bo.setDescription(form.getDescription());
        return bo;
    }

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

}
