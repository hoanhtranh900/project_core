package com.osp.core.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_EXT_OTP_SESSION", indexes = {
        @Index(name = "RECEIVER", columnList = "RECEIVER", unique = false),
        @Index(name = "TIME_EXPIRED", columnList = "TIME_EXPIRED", unique = false),
})
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtOtpSession extends Auditable implements Serializable {

    @Comment("Người nhận (email or số điện thoại)")
    @Column(name = "RECEIVER", nullable = false)
    private String receiver;

    @Comment("Mã")
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Comment("Thời gian hết hạn")
    @Column(name = "TIME_EXPIRED")
    private Date timeExpired;

    @Comment("Loại OTP")
    @Column(name = "TYPE_OTP")
    private Long typeOtp;
}
