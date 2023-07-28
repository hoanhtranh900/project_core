package com.osp.core.service;

import com.google.common.collect.Lists;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<E, R extends BaseRepository<E>> implements BaseService<E> {

	private final R repository;

	public BaseServiceImpl(R repository) {
		this.repository = repository;
	}

	@Override
	public Optional<E> save(E entity) {
		return Optional.of(repository.save(entity));
	}

	@Override
	public Optional<E> update(E entity) {
		return Optional.of(repository.save(entity));
	}

	@Override
	public Optional<E> get(Long id) {
		return repository.findById(id);
	}

	@Override
	public Page<E> getPaging(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public List<E> getAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Boolean deleteById(Long id) {
		E entity = get(id).orElseThrow(() -> new BaseException("Entity not found by id: " + id));
		repository.delete(entity);
		return !Optional.of(repository.findById(id)).isPresent();
	}

	@Override
	public Boolean deleteAll() {
		repository.deleteAll();
		return Lists.newArrayList(repository.findAll()).isEmpty();
	}

}
