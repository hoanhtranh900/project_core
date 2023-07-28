
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.UtilsCommon;
import com.osp.core.utils.UtilsDate;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_PARAM_SYSTEM", indexes = {
        @Index(name = "NAME", columnList = "NAME", unique = true),
        @Index(name = "STATUS", columnList = "STATUS", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmParaSystem extends Auditable implements Serializable {

    @Comment("Từ khóa")
    @Column(name = "NAME", length = 200, unique=true)
    private String name;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("Giá trị")
    @Column(name = "VALUE", length = 200)
    private String value;

    @Comment("Mô tả")
    @Column(name = "DESCRIPTION", length = 200)
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

    public AdmParaSystem formToBo(AdmParaSystem dto, AdmParaSystem paraSystem) {
        paraSystem.setName(dto.getName());
        paraSystem.setValue(dto.getValue());
        paraSystem.setDescription(dto.getDescription());
        paraSystem.setModifiedBy(UtilsCommon.getUserNameLogin());
        paraSystem.setModifiedDate(new Date());
        return paraSystem;
    }
}
