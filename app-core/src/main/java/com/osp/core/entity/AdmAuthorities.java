package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.UtilsCommon;
import com.osp.core.utils.UtilsDate;
import lombok.*;
import org.hibernate.annotations.Comment;

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
@Table(name = "T_ADM_AUTHORITIES", indexes = {
        @Index(name = "AUTH_KEY", columnList = "AUTH_KEY", unique = true),
        @Index(name = "STATUS", columnList = "STATUS", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmAuthorities extends Auditable implements Serializable {

    @JsonIgnore
    @ManyToMany(mappedBy = "groupAuthorities", fetch = FetchType.LAZY)
    private List<AdmGroup> groups;

    @Comment("Role hệ thống")
    @Column(name = "AUTH_KEY", length = 100, unique = true)
    private String authKey;

    @Comment("id cấp n-1")
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Comment("Tên chức năng")
    @Column(name = "AUTHORITIE_NAME", length = 100)
    private String authoritieName;

    @Comment("Mô tả")
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Comment("xắp xếp theo danh sách")
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    public AdmAuthorities formToBo(AdmAuthorities form, AdmAuthorities bo) {
        bo.setAuthKey(form.getAuthKey());
        bo.setParentId(form.getParentId());
        bo.setAuthoritieName(form.getAuthoritieName());
        bo.setDescription(form.getDescription());
        bo.setOrderId(form.getOrderId());
        bo.setModifiedBy(UtilsCommon.getUserNameLogin());
        bo.setModifiedDate(new Date());
        return bo;
    }

    @Transient
    private boolean expand = false;
    @Transient
    private int level = 0;
    @Transient
    private List<AdmAuthorities> children = new ArrayList<>();
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
