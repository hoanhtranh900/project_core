package com.osp.service;

import com.osp.core.entity.AdmParaSystem;
import com.osp.core.dto.request.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdmParaSystemService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Page<AdmParaSystem> getPage(SearchForm searchObject, Pageable pageable);

    List<AdmParaSystem> deleteByIds(List<Long> ids);
    List<AdmParaSystem> locks(List<Long> ids);
    List<AdmParaSystem> unlocks(List<Long> ids);
}
