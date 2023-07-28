/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.osp.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author DELL
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Slf4j
public class ResourceNotFoundException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Exception ex) {
        super(message);
        ResourceNotFoundException.log.error(message, ex);
    }
}
