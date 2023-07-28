package com.osp.core.repository;

import com.osp.core.entity.AdmRight;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmRightRepository extends BaseRepository<AdmRight>, JpaSpecificationExecutor<AdmRight> {

    @Query(value="select ad from #{#entityName} ad where ad.parentId=:parentId and ad.status in (:status) and ad.isDelete=:isDelete ")
    List<AdmRight> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete);

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<AdmRight> loadByListIds(@Param("ids") List<Long> ids);

    @Query(value="select au from #{#entityName} au where au.parentId = 0")
    List<AdmRight> loadListParent();
}
