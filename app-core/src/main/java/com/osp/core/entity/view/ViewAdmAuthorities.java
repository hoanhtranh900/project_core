package com.osp.core.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.AdmGroup;
import com.osp.core.entity.Auditable;
import com.osp.core.entity.only.V_AdmGroup;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ViewAdmAuthorities extends Auditable implements Serializable {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "T_ADM_GROUP_AUTHORITIES",
            joinColumns = @JoinColumn(name = "AUTHORITY"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private List<V_AdmGroup> groups;

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
    private boolean expand = false;
    @Transient
    private int level = 0;
    @Transient
    private List<ViewAdmAuthorities> children = new ArrayList<>();
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
