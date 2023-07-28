package com.osp.core.controller;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.exception.BaseException;
import com.osp.core.exception.Result;
import com.osp.core.exception.UnauthorizedException;
import com.osp.core.service.BaseService;
import com.osp.core.utils.UtilsCommon;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


public abstract class BaseControllerImpl<E, S extends BaseService<E>> implements BaseController<E> {

	private final S service;
    private final String role_view;
    private final MessageSource messageSource;

    protected BaseControllerImpl(S service, String role_view, MessageSource messageSource) {
        this.service = service;
        this.role_view = role_view;
        this.messageSource = messageSource;
    }

    /*@Override
    public ResponseEntity delete(@PathVariable Long id) {
        return new ResponseEntity(new ResponseData(service.deleteById(id), Result.SUCCESS), HttpStatus.OK);
    }*/

    /*@Override
    public ResponseEntity deleteAll() {
        return new ResponseEntity(new ResponseData(service.deleteAll(), Result.SUCCESS), HttpStatus.OK);
    }*/

    /*@Override
    public ResponseEntity<ResponseData> save(@RequestBody E entity) {
        E e = service.save(entity).orElseThrow(() -> new BaseException(
                        String.format(ErrorType.ENTITY_NOT_SAVED.getDescription(), entity.toString())
                ));
        return new ResponseEntity(new ResponseData(e, Result.SUCCESS), HttpStatus.OK);
    }*/

    /*@Override
    public ResponseEntity update(@RequestBody E entity) {
        E e = service.update(entity)
                .orElseThrow(() -> new BaseException(
                        String.format(ErrorType.ENTITY_NOT_UPDATED.getDescription(), entity)
                ));
        return new ResponseEntity(new ResponseData(e, Result.SUCCESS), HttpStatus.OK);
    }*/

    @Override
    public ResponseEntity get(Long id) throws UnauthorizedException {
        if (!ConstantAuthor.contain(role_view, UtilsCommon.getAuthentication())) {
            throw new UnauthorizedException(messageSource.getMessage("error_401", null, UtilsCommon.getLocale()));
        }
        E e = service.get(id).orElseThrow(() -> new BaseException( messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Entity"}, UtilsCommon.getLocale())));
        return new ResponseEntity(new ResponseData(e, Result.SUCCESS), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getPaging(Pageable pageable) throws UnauthorizedException {
        if (!ConstantAuthor.contain(role_view, UtilsCommon.getAuthentication())) {
            throw new UnauthorizedException(messageSource.getMessage("error_401", null, UtilsCommon.getLocale()));
        }
    	return new ResponseEntity(new ResponseData(service.getPaging(pageable), Result.SUCCESS), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAll() throws UnauthorizedException {
        if (!ConstantAuthor.contain(role_view, UtilsCommon.getAuthentication())) {
            throw new UnauthorizedException(messageSource.getMessage("error_401", null, UtilsCommon.getLocale()));
        }
        return new ResponseEntity(new ResponseData(service.getAll(), Result.SUCCESS), HttpStatus.OK);
    }

}
