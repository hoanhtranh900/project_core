package com.osp.core.repository;

import com.osp.core.entity.ActionHistory;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionHistoryRepository extends BaseRepository<ActionHistory>, JpaSpecificationExecutor<ActionHistory> {
}
