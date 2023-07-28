package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_FILE_ATTACHMENT", indexes = {
        @Index(name = "OBJECT_ID", columnList = "OBJECT_ID", unique = false),
        @Index(name = "OBJECT_TYPE", columnList = "OBJECT_TYPE", unique = false),
        @Index(name = "FILE_SERVICE_ID", columnList = "FILE_SERVICE_ID", unique = false),
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileAttachment extends Auditable implements Serializable {

    @Column(name = "OBJECT_ID")
    private Long objectId;
    
    @Column(name = "OBJECT_TYPE")
    private Long objectType;

    @Column(name = "FILE_NAME", length = 150)
    private String fileName;

    @Column(name = "FILE_EXTENTION", length = 50)
    private String fileExtention;

    @Column(name = "FILE_PATH", length = 1000)
    private String filePath;

    @Column(name = "NOTE", length = 500)
    private String note;

    @Column(name = "IS_SIGNED")
    private Long isSigned;

    @Column(name = "IS_ENCRYPT")
    private Long isEncrypt;

    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "UPDATOR_ID")
    private Long updatorId;

    @Column(name = "SIGNED_DOC_ID", length = 100)
    private String signedDocId;

    @Column(name = "FILE_SERVICE_ID")
    private Long fileServiceId;

    @Column(name = "STORAGE_TYPE", length = 100)
    private String storageType;

    @Column(name = "CONTENT_TYPE", length = 100)
    private String contentType;

    @Column(name = "PDF_ALREADY")
    private Long pdfAlready;

}
