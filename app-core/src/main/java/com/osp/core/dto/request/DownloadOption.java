package com.osp.core.dto.request;

public class DownloadOption {
    private String type;
    private String waterMarkText;
    private Long vbAttachmentId;
    private Long attachmentMetadataId;
    private Long pageNumber;
    private String imageStatus;
    private Long includeAttachDocumentId;

    

    public String getWaterMarkText() {
        return waterMarkText;
    }

    public void setWaterMarkText(String waterMarkText) {
        this.waterMarkText = waterMarkText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Long getVbAttachmentId() {
        return vbAttachmentId;
    }

    public void setVbAttachmentId(Long vbAttachmentId) {
        this.vbAttachmentId = vbAttachmentId;
    }

    public Long getAttachmentMetadataId() {
        return attachmentMetadataId;
    }

    public void setAttachmentMetadataId(Long attachmentMetadataId) {
        this.attachmentMetadataId = attachmentMetadataId;
    }

    public Long getIncludeAttachDocumentId() {
        return includeAttachDocumentId;
    }

    public void setIncludeAttachDocumentId(Long includeAttachDocumentId) {
        this.includeAttachDocumentId = includeAttachDocumentId;
    }

    
}
