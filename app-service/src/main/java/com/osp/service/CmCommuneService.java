package com.osp.service;

import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmCommune;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface CmCommuneService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    List<CmCommune> communeByDistrict(Long districtId);
    Optional<CmCommune> edit(CmCommune dto);
    HashMap initAddOrEdit(Long id) throws BadRequestException;
    Page<CmCommune> getPage(SearchForm searchObject, Pageable pageable);
    List<CmCommune> deleteByIds(List<Long> ids);
    List<CmCommune> locks(List<Long> ids);
    List<CmCommune> unlocks(List<Long> ids);
}
