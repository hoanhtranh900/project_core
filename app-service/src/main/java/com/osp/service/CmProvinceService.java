package com.osp.service;

import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmProvince;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface CmProvinceService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Optional<CmProvince> edit(CmProvince dto);
    HashMap initAddOrEdit(Long id) throws BadRequestException;
    Page<CmProvince> getPage(SearchForm searchObject, Pageable pageable);
    List<CmProvince> deleteByIds(List<Long> ids);
    List<CmProvince> locks(List<Long> ids);
    List<CmProvince> unlocks(List<Long> ids);
}
