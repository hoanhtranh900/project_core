package com.osp.core.repository;

import com.osp.core.entity.SchedulerJobHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Repository
public interface SchedulerJobHistoryRepository extends BaseRepository<SchedulerJobHistory>, JpaSpecificationExecutor<SchedulerJobHistory> {

}
