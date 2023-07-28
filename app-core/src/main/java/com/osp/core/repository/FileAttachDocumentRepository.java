package com.osp.core.repository;

import com.osp.core.entity.FileAttachDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachDocumentRepository extends BaseRepository<FileAttachDocument>, JpaSpecificationExecutor<FileAttachDocument> {

}
