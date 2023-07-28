
package com.osp.core.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.Auditable;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Subselect("select * from T_ADM_USER_TYPE")
public class ViewAdmUserType extends Auditable implements Serializable {

    @JsonIgnore
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private ViewAdmUser users;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "TYPE_CODE")
    private String typeCode;

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
