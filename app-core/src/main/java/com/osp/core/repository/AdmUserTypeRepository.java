package com.osp.core.repository;

import com.osp.core.entity.AdmUserType;
import com.osp.core.entity.AdmUserType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmUserTypeRepository extends BaseRepository<AdmUserType>, JpaSpecificationExecutor<AdmUserType> {
}
