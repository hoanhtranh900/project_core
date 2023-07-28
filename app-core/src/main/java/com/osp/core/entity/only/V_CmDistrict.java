package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.entity.Auditable;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Subselect("select * from CM_DISTRICT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class V_CmDistrict extends Auditable implements Serializable {
    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Column(name = "DISTRICT_CODE")
    private String districtCode;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "PROVINCE_ID")
    private Long provinceId;

}
