package com.osp.core.repository;

import com.osp.core.entity.FileImageExtract;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImageExtractRepository extends BaseRepository<FileImageExtract>, JpaSpecificationExecutor<FileImageExtract> {
}
