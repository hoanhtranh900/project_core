package com.osp.core.controller;

import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmDept;
import com.osp.core.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public interface BaseController<E> {

    //@DeleteMapping("/{id}")
    //ResponseEntity delete(@RequestParam("id") Long id);

    //@DeleteMapping("/all")
    //ResponseEntity deleteAll();

    //@PostMapping
    //ResponseEntity save(@RequestBody E entity);

    //@PutMapping
    //ResponseEntity update(@RequestBody E entity);

    @ApiOperation(notes = Constants.NOTE_API + "empty_note", value = "Chi tiết theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping("/{id}")
    ResponseEntity get(@PathVariable("id") Long id) throws UnauthorizedException;

    @ApiOperation(notes = Constants.NOTE_API + "empty_note", value = "Danh sách phân trang", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping("/getPaging")
    ResponseEntity getPaging(Pageable pageable) throws UnauthorizedException;

    @ApiOperation(notes = Constants.NOTE_API + "empty_note", value = "Danh sách ALL", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping("/all")
    ResponseEntity getAll() throws UnauthorizedException;

}
