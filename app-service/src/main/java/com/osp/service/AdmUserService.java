package com.osp.service;

import com.osp.core.entity.AdmUser;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.view.ViewAdmUser;
import com.osp.core.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdmUserService<E> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    Page<ViewAdmUser> getPage(SearchForm searchObject, Pageable pageable);
    List<AdmUser> deleteByIds(List<Long> ids);
    List<AdmUser> locks(List<Long> ids);
    List<AdmUser> unlocks(List<Long> ids);

    AdmUser profile(Long id);
    Optional<AdmUser> updateProfile(AdmUser form);
    Optional<AdmUser> changePass(AdmUser form) throws BadRequestException;
}
