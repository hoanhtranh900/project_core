package com.osp.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author DELL
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException(final String message) {
        super(message);
    }
}
