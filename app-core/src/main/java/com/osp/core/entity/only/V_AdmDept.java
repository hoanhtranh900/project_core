package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmGroup;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.Auditable;
import com.osp.core.utils.UtilsCommon;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Subselect;

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
@Subselect("select * from T_ADM_DEPT")
public class V_AdmDept extends Auditable implements Serializable {

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "DEPT_CODE")
    private String deptCode;

    @Column(name = "DEPT_DESC")
    private String deptDesc;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DELETE")
    private Long isDelete;

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
