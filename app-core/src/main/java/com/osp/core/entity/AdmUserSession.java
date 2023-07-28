package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "T_ADM_USER_SESSION",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "SESSION"})},
        indexes = {
                @Index(name = "SESSION", columnList = "SESSION", unique = false),
        }
)
@Setter
@Getter
@Builder
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmUserSession extends Auditable implements Serializable {

    @Comment("SESSION")
    @Column(name = "SESSION", length = 200)
    private String session;

    @Comment("IP ADDRESS")
    @Column(name = "IP_ADDRESS", length = 100)
    private String ipAddress;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private AdmUser user;

    public AdmUserSession(String session, String ipAddress, AdmUser user) {
        this.session = session;
        this.user = user;
        this.ipAddress = ipAddress;
    }
}
