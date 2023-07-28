package com.osp.core.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.AdmDept;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.Auditable;
import com.osp.core.entity.only.V_AdmAuthorities;
import com.osp.core.entity.only.V_AdmDept;
import com.osp.core.entity.only.V_AdmUser;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Subselect("select * from T_ADM_GROUP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ViewAdmGroup extends Auditable implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_AUTHORITIES",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY"))
    private List<V_AdmAuthorities> groupAuthorities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_DEPT",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEPT_ID"))
    private List<V_AdmDept> groupDepts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_USER",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<V_AdmUser> groupUsers;

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<Long> keys;

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
