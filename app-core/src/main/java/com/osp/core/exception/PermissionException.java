package com.osp.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author DELL
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionException extends Exception implements Serializable {

    private static final long serialVersionUID = 2395420452644916136L;

    public PermissionException(final String message) {
        super(message);
    }
}
