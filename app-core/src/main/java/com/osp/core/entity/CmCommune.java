
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.UtilsCommon;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/*
 * common table
 * */
@Entity
@Table(name = "T_CM_COMMUNE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CmCommune extends Auditable implements Serializable {
    @Comment("Tên phường xã")
    @Column(name = "COMMUNE_NAME", length = 300, nullable = false)
    private String communeName;

    @Comment("mã phường xã")
    @Column(name = "COMMUNE_CODE", nullable = false, unique = true, length = 50)
    private String communeCode;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @JoinColumn(name = "DISTRICT_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private CmDistrict district;

    public CmCommune formToBo(CmCommune dto, CmCommune bo) {
        bo.setCommuneCode(dto.getCommuneCode());
        bo.setCommuneName(dto.getCommuneName());
        bo.setIsDelete(dto.getIsDelete());
        bo.setStatus(dto.getStatus());
        bo.setModifiedBy(UtilsCommon.getUserNameLogin());
        bo.setModifiedDate(new Date());
        return bo;
    }
}
