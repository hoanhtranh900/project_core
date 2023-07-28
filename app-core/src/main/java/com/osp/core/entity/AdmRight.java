
package com.osp.core.entity;

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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_RIGHT", indexes = {
        @Index(name = "PARENT_ID", columnList = "PARENT_ID", unique = false),
        @Index(name = "RIGHT_CODE", columnList = "RIGHT_CODE", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmRight extends Auditable implements Serializable {

    @Comment("Id chức năng")
    @Column(name = "AUTHORITIE_ID")
    private Long authoritieId;

    @Comment("id cấp n-1")
    @Column(name = "PARENT_ID")
    private Long parentId;

    @Comment("Mã menu")
    @Column(name = "RIGHT_CODE", length = 100)
    private String rightCode;

    @Comment("Tên menu")
    @Column(name = "RIGHT_NAME", length = 100)
    private String rightName;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("Xắp xếp")
    @Column(name = "RIGHT_ORDER")
    private Long rightOrder;

    @Comment("url FE")
    @Column(name = "URL_REWRITE", length = 500)
    private String urlRewrite;

    @Comment("icon hiển thị")
    @Column(name = "ICON_URL", length = 100)
    private String iconUrl;

    @Comment("Mô tả")
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Column(name = "BADGE_SQL", length = 100)
    private String badgeSql;

    public AdmRight formToBo(AdmRight form, AdmRight bo) {

        bo.setParentId(form.getParentId());
        bo.setRightCode(form.getRightCode());
        bo.setRightName(form.getRightName());
        bo.setRightOrder(form.getRightOrder());
        bo.setUrlRewrite(form.getUrlRewrite());
        bo.setIconUrl(form.getIconUrl());
        bo.setDescription(form.getDescription());
        bo.setBadgeSql(form.getBadgeSql());

        return bo;
    }

    @Transient
    private String statusStr;
    @Transient
    private String isDeleteStr;
    @Transient
    private boolean expand = false;
    @Transient
    private int level = 0;
    @Transient
    private List<AdmAuthorities> children = new ArrayList<>();

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
