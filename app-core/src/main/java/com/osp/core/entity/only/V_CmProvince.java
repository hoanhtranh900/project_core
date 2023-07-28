package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.entity.Auditable;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Subselect("select * from CM_PROVINCE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class V_CmProvince extends Auditable implements Serializable {
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "PROVINCE_NAME")
    private String provinceName;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "STATUS")
    private Long status;

}
