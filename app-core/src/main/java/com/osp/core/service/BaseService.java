package com.osp.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<E> {

    Optional<E> save(E entity);

    Optional<E> update(E entity);

    Optional<E> get(Long id);

    Page<E> getPaging(Pageable pageable);

    List<E> getAll();

    Boolean deleteById(Long id);

    Boolean deleteAll();
}
