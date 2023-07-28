
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_USER_TYPE", indexes = {
        @Index(name = "TYPE_CODE", columnList = "TYPE_CODE", unique = false),
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmUserType extends Auditable implements Serializable {

    @JsonIgnore
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private AdmUser users;

    @Comment("Tên loại người dùng")
    @Column(name = "TYPE_NAME", length = 100, unique = true)
    private String typeName;

    @Comment("Mã loại người dùng")
    @Column(name = "TYPE_CODE", length = 100)
    private String typeCode;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

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

}
