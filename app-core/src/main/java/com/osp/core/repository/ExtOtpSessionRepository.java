package com.osp.core.repository;

import com.osp.core.entity.ExtOtpSession;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtOtpSessionRepository extends BaseRepository<ExtOtpSession>, JpaSpecificationExecutor<ExtOtpSession> {
    List<ExtOtpSession> findByReceiverAndTypeOtp(String receiver, Long typeOtp);
}
