package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmGroup;
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

/**
 *
 * @author sonnv1
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Subselect("select * from T_ADM_AUTHORITIES")
public class V_AdmAuthorities extends Auditable implements Serializable {

    @Column(name = "AUTH_KEY")
    private String authKey;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "AUTHORITIE_NAME")
    private String authoritieName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_ID")
    private Long orderId;

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
