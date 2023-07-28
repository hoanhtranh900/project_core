package com.osp.core.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_EXT_NOTIFICATION", indexes = {
        @Index(name = "USER_ID", columnList = "USER_ID", unique = false),
        @Index(name = "IS_READ", columnList = "IS_READ", unique = false),
})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtNotification extends Auditable implements Serializable {

    @Comment("ID user")
    @Column(name = "USER_ID")
    private Long userId;

    @Comment("Mô tả")
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Comment("Nội dung")
    @Lob
    @Column(name = "CONTENT")
    private Long content;

    @Comment("Trạng thái đã đọc")
    @Column(name = "IS_READ")
    private boolean read;

    @Comment("Loại thông báo")
    @Column(name = "TYPE")
    private Long type;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE")
    private Long isDelete;

}
