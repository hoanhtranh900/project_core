package com.osp.service;

import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmDistrict;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface CmDistrictService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    List<CmDistrict> districtByProvince(Long provinceId);
    Optional<CmDistrict> edit(CmDistrict dto);
    HashMap initAddOrEdit(Long id) throws BadRequestException;
    Page<CmDistrict> getPage(SearchForm searchObject, Pageable pageable);
    List<CmDistrict> deleteByIds(List<Long> ids);
    List<CmDistrict> locks(List<Long> ids);
    List<CmDistrict> unlocks(List<Long> ids);
}
