package com.osp.service;

import com.osp.core.entity.AdmRight;
import com.osp.core.dto.request.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdmRightService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Page<AdmRight> getPage(SearchForm searchObject, Pageable pageable);
    List<AdmRight> deleteByIds(List<Long> ids);
    List<AdmRight> locks(List<Long> ids);
    List<AdmRight> unlocks(List<Long> ids);
    List<AdmRight> loadListParent();
    List<AdmRight> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete);
}
