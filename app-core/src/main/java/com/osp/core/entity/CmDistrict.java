
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.UtilsCommon;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
* common table
* */
@Entity
@Table(name = "T_CM_DISTRICT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CmDistrict extends Auditable implements Serializable {
    @Comment("Tên huyện")
    @Column(name = "DISTRICT_NAME", length = 300, nullable = false)
    private String districtName;

    @Comment("Mã huyện")
    @Column(name = "DISTRICT_CODE", nullable = false, unique = true, length = 50)
    private String districtCode;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "district")
    private List<CmCommune> communues;

    @JoinColumn(name = "PROVINCE_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private CmProvince province;

    public CmDistrict formToBo(CmDistrict dto, CmDistrict bo) {
        bo.setDistrictCode(dto.getDistrictCode());
        bo.setDistrictName(dto.getDistrictName());
        bo.setIsDelete(dto.getIsDelete());
        bo.setStatus(dto.getStatus());
        bo.setModifiedBy(UtilsCommon.getUserNameLogin());
        bo.setModifiedDate(new Date());
        return bo;
    }
}
