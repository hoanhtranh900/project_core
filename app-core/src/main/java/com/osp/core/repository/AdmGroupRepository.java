package com.osp.core.repository;

import com.osp.core.entity.AdmGroup;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmGroupRepository extends BaseRepository<AdmGroup>, JpaSpecificationExecutor<AdmGroup> {

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<AdmGroup> loadByListIds(@Param("ids") List<Long> ids);

}
