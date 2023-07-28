package com.osp.core.repository;

import com.osp.core.entity.AdmUserSession;
import com.osp.core.entity.SchedulerJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Repository
public interface SchedulerJobInfoRepository extends BaseRepository<SchedulerJobInfo>, JpaSpecificationExecutor<SchedulerJobInfo> {
    Optional<SchedulerJobInfo> findByJobName(String jobName);
}
