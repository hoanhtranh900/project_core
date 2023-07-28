package com.osp.core.repository;

import com.osp.core.entity.AdmUserSession;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmUserSessionRepository extends BaseRepository<AdmUserSession>, JpaSpecificationExecutor<AdmUserSession> {

    Optional<AdmUserSession> findByIpAddress(String ipAddress);
    Optional<AdmUserSession> findAllByUserId(Long userId);
    void deleteByUserId(Long userId);
}
