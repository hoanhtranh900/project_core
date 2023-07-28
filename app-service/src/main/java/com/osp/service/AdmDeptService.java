package com.osp.service;

import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmDept;
import com.osp.core.entity.view.ViewAdmDept;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AdmDeptService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Optional<AdmDept> edit(AdmDept dto);
    HashMap initAddOrEdit(Long id) throws BadRequestException;
    Page<ViewAdmDept> getPage(SearchForm searchObject, Pageable pageable);
    List<AdmDept> deleteByIds(List<Long> ids);
    List<AdmDept> locks(List<Long> ids);
    List<AdmDept> unlocks(List<Long> ids);
    List<AdmDept> findByParentIdAndStatusAndIsDelete(Long parentId, Long status, Long isDelete);
    List<AdmDept> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete);
}
