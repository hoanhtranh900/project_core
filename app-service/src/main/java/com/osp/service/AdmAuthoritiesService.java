package com.osp.service;

import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.view.ViewAdmAuthorities;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AdmAuthoritiesService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Optional<AdmAuthorities> edit(AdmAuthorities dto);
    HashMap initAddOrEdit(Long id) throws BadRequestException;
    Page<ViewAdmAuthorities> getPage(SearchForm searchObject, Pageable pageable);
    List<AdmAuthorities> deleteByIds(List<Long> ids);
    List<AdmAuthorities> locks(List<Long> ids);
    List<AdmAuthorities> unlocks(List<Long> ids);
    List<AdmAuthorities> loadListParent();
    List<AdmAuthorities> findByParentIdAndListStatus(Long parentId, List<Long> status);
}
