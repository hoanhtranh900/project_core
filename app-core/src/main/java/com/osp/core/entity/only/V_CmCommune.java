package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.entity.Auditable;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Subselect("select * from CM_COMMUNE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class V_CmCommune extends Auditable implements Serializable {
    @Column(name = "COMMUNE_NAME")
    private String communeName;

    @Column(name = "COMMUNE_CODE")
    private String communeCode;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "DISTRICT_ID")
    private Long districtId;

}
