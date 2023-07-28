package com.osp.core.repository;

import com.osp.core.entity.AdmParaSystem;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmParaSystemRepository extends BaseRepository<AdmParaSystem>, JpaSpecificationExecutor<AdmParaSystem> {

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<AdmParaSystem> loadByListIds(@Param("ids") List<Long> ids);
}
