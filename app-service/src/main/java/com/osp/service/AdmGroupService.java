package com.osp.service;

import com.osp.core.entity.AdmGroup;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.view.ViewAdmGroup;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AdmGroupService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Page<ViewAdmGroup> getPage(SearchForm searchObject, Pageable pageable);
    List<AdmGroup> deleteByIds(List<Long> ids);
    List<AdmGroup> locks(List<Long> ids);
    List<AdmGroup> unlocks(List<Long> ids);

    HashMap initAdd();
    AdmGroup addGroup(AdmGroup group);
    AdmGroup editGroup(AdmGroup dto) throws BadRequestException;
    HashMap initEdit(Long groupId);
}
