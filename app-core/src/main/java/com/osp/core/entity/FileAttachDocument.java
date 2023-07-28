package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_FILE_ATTACH_DOCUMENT", indexes = {
        @Index(name = "FILE_NAME", columnList = "FILE_NAME", unique = false),
        @Index(name = "FILE_CODE", columnList = "FILE_CODE", unique = false),
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileAttachDocument extends Auditable implements Serializable {

    @Comment("Tên file")
    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_CODE", length = 150)
    private String fileCode;

    @Column(name = "FILE_TYPE", length = 50)
    private String fileType;

    @Column(name = "FILE_SIZE", length = 1000)
    private Long fileSize;

    @Column(name = "IS_ENCRYPT")
    private Long isEncrypt;

    @Column(name = "IS_PDF")
    private Long isPdf;

    @Column(name = "PDF_ATTACH_DOCUMENT_ID")
    private String pdfAttachmentDocumentId;

    @Column(name = "IS_ORIGINAL")
    private Long isOriginal;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FILE_PATH", length = 500)
    private String filePath;

    @Column(name = "CONTENT_TYPE", length = 100)
    private String contentType;

    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Transient
    private String isDeleteStr;

    public String getIsDeleteStr() {
        return ConstantString.IS_DELETE.getStatusStr(this.isDelete);
    }

}
