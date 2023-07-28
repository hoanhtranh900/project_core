package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.Auditable;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Subselect("select * from T_ADM_USER_SESSION")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class V_AdmUserSession extends Auditable implements Serializable {

    @Comment("SESSION")
    @Column(name = "SESSION", length = 200)
    private String session;

    @Comment("IP ADDRESS")
    @Column(name = "IP_ADDRESS", length = 100)
    private String ipAddress;

    @Column(name = "USER_ID")
    private Long userId;

}
