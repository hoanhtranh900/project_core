package com.osp.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author DELL
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception implements Serializable {

    private static final long serialVersionUID = 1668054874585244657L;

    public UnauthorizedException(final String message) {
        super(message);
    }
}
