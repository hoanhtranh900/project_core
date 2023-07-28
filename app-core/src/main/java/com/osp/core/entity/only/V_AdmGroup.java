package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.AdmDept;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.Auditable;
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
public class V_AdmGroup extends Auditable implements Serializable {

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "DESCRIPTION")
    private String description;

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
