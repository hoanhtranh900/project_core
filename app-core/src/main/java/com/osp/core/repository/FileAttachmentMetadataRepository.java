package com.osp.core.repository;

import com.osp.core.entity.FileAttachmentMetadata;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachmentMetadataRepository extends BaseRepository<FileAttachmentMetadata>, JpaSpecificationExecutor<FileAttachmentMetadata> {

}
