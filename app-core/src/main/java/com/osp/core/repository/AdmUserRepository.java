package com.osp.core.repository;

import com.osp.core.entity.AdmUser;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmUserRepository extends BaseRepository<AdmUser>, JpaSpecificationExecutor<AdmUser> {

    Optional<AdmUser> findByUsername(String userName);

    @Query(value = "select distinct au from AdmUser au INNER JOIN au.typeUsers tus where au.username=:username and tus.typeCode in :typeCodes ")
    Optional<AdmUser> findByUsernameAndTypeUserIn(String username, List<String> typeCodes);

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<AdmUser> loadByListIds(@Param("ids") List<Long> ids);
}
