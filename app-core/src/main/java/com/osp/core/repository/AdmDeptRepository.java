package com.osp.core.repository;

import com.osp.core.entity.AdmDept;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmDeptRepository extends BaseRepository<AdmDept>, JpaSpecificationExecutor<AdmDept> {

    List<AdmDept> findByParentIdAndStatusAndIsDelete(Long parentId, Long status, Long isDelete);

    @Query(value="select ad from #{#entityName} ad where ad.parentId=:parentId and ad.status in (:status) and ad.isDelete=:isDelete ")
    List<AdmDept> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete);

    @Query(value="select ad from #{#entityName} ad where ad.id in (:ids)")
    List<AdmDept> loadByListIds(@Param("ids") List<Long> ids);

}
