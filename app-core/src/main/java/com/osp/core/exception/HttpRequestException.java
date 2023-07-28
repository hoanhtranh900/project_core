package com.osp.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * @author DELL
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class HttpRequestException extends Exception implements Serializable {


    private static final long serialVersionUID = 2141685067515932795L;

    public HttpRequestException(final String message) {
        super(message);
    }
}
