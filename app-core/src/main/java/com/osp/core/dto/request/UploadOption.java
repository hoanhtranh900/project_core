package com.osp.core.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadOption {

    private Boolean needEncrypt;
    private Boolean needConvertPdf;
    private String contentType;
    private String fileBase64;

    private List<String> imageFileIds;
    private List<String> imageExtractIds;
    private String originalFileId;
    private Long pageNumber;

    private Object actor;
    private String attachMetadataCommnetId;
    private String attachMetadataOriginalId;
    private List<String> imageExtractCommentIds;
}
