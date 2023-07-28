package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_FILE_IMAGE_EXTRACT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FileImageExtract extends Auditable implements Serializable {

    @Column(name = "VB_ATTACHMENT_ID", nullable = true)
    private Long vbAttachmentId;

    @Column(name = "ATTACHMENT_METADATA_ID", nullable = true)
    private Long attachmentMetadataId;

    @Column(name = "FILE_SIZE", nullable = true)
    private Long fileSize;

    @Column(name = "PAGE_NUMBER", nullable = true)
    private Long pageNumber;

    @Column(name = "FILE_NAME", nullable = true)
    private String fileName;
    
    @Column(name = "FILE_EXTENTION", nullable = true, length = 50)
    private String fileExtention;
    
    @Column(name = "FILE_PATH",length = 1000)
    private String filePath;
    
    @Column(name = "NOTE", nullable = true)
    private String note;
    
    @Column(name = "IS_SIGNED", nullable = true, precision = 0)
    private Long isSigned;
    
    @Column(name = "IS_ENCRYPT", nullable = true, precision = 0)
    private Long isEncrypt;
    
    @Column(name = "STORAGE_TYPE", nullable = true, length = 100)
    private String storageType;
    
    @Column(name = "STATUS", nullable = true, length = 50)
    private Long status;
    
    @Column(name = "IS_DELETE", nullable = true, precision = 0)
    private Long isDelete;
    
    @Column(name = "IS_EDITED", nullable = true, precision = 0)
    private Long isEdited;

    public FileImageExtract(FileImageExtract imageExtract) {
        this.vbAttachmentId = imageExtract.vbAttachmentId;
        this.attachmentMetadataId = imageExtract.attachmentMetadataId;
        this.fileSize = imageExtract.fileSize;
        this.pageNumber = imageExtract.pageNumber;
        this.fileName = imageExtract.fileName;
        this.fileExtention = imageExtract.fileExtention;
        this.filePath = imageExtract.filePath;
        this.note = imageExtract.note;
        this.isSigned = imageExtract.isSigned;
        this.isEncrypt = imageExtract.isEncrypt;
        this.storageType = imageExtract.storageType;
        this.status = imageExtract.status;
        this.isDelete = imageExtract.isDelete;
        this.isEdited = imageExtract.isEdited;
    }
}
