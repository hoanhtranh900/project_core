package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_FILE_ATTACHMENT_METADATA", indexes = {
        @Index(name = "OBJECT_TYPE", columnList = "OBJECT_TYPE", unique = false),
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileAttachmentMetadata extends Auditable implements Serializable {

    @Column(name = "VB_ATTACHMENT_ID", nullable = true)
    private Long vbAttachmentId;
    
    @Basic
    @Column(name = "OBJECT_TYPE", nullable = true, length = 100)
    private String objectType;
    
    @Column(name = "SIZE_TOTAL", nullable = true)
    private Long sizeTotal;
    
    @Column(name = "PAGE_SIZE", nullable = true)
    private Long pageSize;
    
    @Column(name = "STATUS", nullable = true)
    private String status;

    @Column(name = "IS_DELETE", nullable = true, columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;
    
}
