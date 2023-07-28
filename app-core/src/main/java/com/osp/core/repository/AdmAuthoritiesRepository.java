package com.osp.core.repository;

import com.osp.core.entity.AdmAuthorities;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmAuthoritiesRepository extends BaseRepository<AdmAuthorities>, JpaSpecificationExecutor<AdmAuthorities> {

    @Query(value="select au from #{#entityName} au where au.parentId = 0 and au.id=:id")
    Optional<AdmAuthorities> checkParent(@Param("id") Long id);

    @Query(value="SELECT DISTINCT au.authKey " +
            "FROM AdmAuthorities au " +
            "INNER JOIN au.groups gr " +
            "INNER JOIN gr.groupUsers us " +
            "WHERE au.status=:status and gr.status=:status and us.status=:status " +
            "and au.isDelete=:isDelete and gr.isDelete=:isDelete and us.isDelete=:isDelete " +
            "and us.username=:username")
    List<String> loadListAuthorityOfUserByUsername(String username, Long status, Long isDelete);

    @Query(value="select au from #{#entityName} au where au.parentId = 0")
    List<AdmAuthorities> loadListParent();

    @Query(value="select ad from #{#entityName} ad where ad.parentId = :parentId and ad.status in (:status) ")
    List<AdmAuthorities> findByParentIdAndListStatus(Long parentId, List<Long> status);

    @Query(value="select au from #{#entityName} au where au.parentId=:id or au.id=:id")
    List<AdmAuthorities> getAllByParentId(@Param("id") Long id);

    @Query(value="select au from #{#entityName} au where au.parentId not in (0) ")
    List<AdmAuthorities> loadListChild();

    @Query(value="SELECT DISTINCT au " +
            "FROM AdmAuthorities au " +
            "INNER JOIN au.groups gr " +
            "WHERE gr.id=:groupId and au.parentId=0 ")
    List<AdmAuthorities> loadListParentByGroupId(@Param("groupId") Long groupId);

    @Query(value="SELECT DISTINCT au " +
            "FROM AdmAuthorities au " +
            "INNER JOIN au.groups gr " +
            "WHERE gr.id=:groupId and au.parentId not in (0) ")
    List<AdmAuthorities> loadListChildByGroupId(@Param("groupId") Long groupId);

    @Query(value="select au from #{#entityName} au where au.id in (:ids)")
    List<AdmAuthorities> loadByListIds(@Param("ids") List<Long> ids);
}
