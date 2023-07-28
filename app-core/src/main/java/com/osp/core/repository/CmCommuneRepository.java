package com.osp.core.repository;

import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmDistrict;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmCommuneRepository extends BaseRepository<CmCommune>, JpaSpecificationExecutor<CmCommune> {

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<CmCommune> loadByListIds(@Param("ids") List<Long> ids);

    List<CmCommune> findByDistrict(CmDistrict district);
}
