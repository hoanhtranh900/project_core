
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "T_ACTION_HISTORY", indexes = {
        @Index(name = "USER_NAME", columnList = "USER_NAME", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ActionHistory extends Auditable implements Serializable {

    @Comment("Tên tài khoản")
    @Column(name = "USER_NAME", length = 100)
    private String userName;

    @Comment("Hành động")
    @Column(name = "ACTION", length = 100)
    private String action;

    @Comment("khối chức năng")
    @Column(name = "MODULE", length = 200)
    private String module;

    @Comment("Chi tiết")
    @Column(name = "DETAIL_ACTION", length = 500)
    private String detailAction;

    @Comment("Ip thực hiện")
    @Column(name = "IP_ADDRESS", length = 100)
    private String ipAddress;

    @Comment("Thiết bị")
    @Column(name = "DEVICE", length = 50)
    private String device;

}
