package com.osp.core.dto.request;

import com.osp.core.entity.FileAttachmentMetadata;
import com.osp.core.entity.FileImageExtract;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class ExtractFileResult {
    private List<FileImageExtract> imageExtractList;
    private FileAttachmentMetadata attachmentMetadata;

}
