package com.osp.core.repository;

import com.osp.core.entity.CmDistrict;
import com.osp.core.entity.CmProvince;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmDistrictRepository extends BaseRepository<CmDistrict>, JpaSpecificationExecutor<CmDistrict> {

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<CmDistrict> loadByListIds(@Param("ids") List<Long> ids);

    List<CmDistrict> findByProvince(CmProvince province);
}
