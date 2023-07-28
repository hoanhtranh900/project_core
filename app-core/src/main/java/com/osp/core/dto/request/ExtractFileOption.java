package com.osp.core.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractFileOption {

    private Boolean needEncrypt;
    private Long attachmentId;
    private Long vbAttachmentId;
    private String contentType;
    private Long objectId;
    private String objectType;
}
