package com.osp.config;

import com.osp.core.dto.response.ResponseData;
import com.osp.core.exception.*;
import com.osp.core.utils.UtilsHttp;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(status.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(headers), status);
    }

    @Transactional
    public void loggingAdvice(Exception ex, WebRequest request) {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
        String index = new StringBuilder("error").append(username != null ? username.substring(0, 1).toUpperCase() + username.substring(1, username.length()) : "").toString();
        HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();
        String error = new StringBuilder(index + " - " + httpServletRequest.getMethod() + " " + httpServletRequest.getRemoteAddr() + ":" + httpServletRequest.getLocalPort() + httpServletRequest.getRequestURI()).toString();
        log.error(error + " - " + ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.BAD_REQUEST.value()).message(errors.get(0)).time(new Date()).path(path).build(), UtilsHttp.getHeaders(headers), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.NOT_FOUND.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(headers), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ResponseData> resourceNotFoundException(Exception ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.NOT_FOUND.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseData> globalExceptionHandler(Exception ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).time(new Date()).path(path).build(), UtilsHttp.getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({ProxyAuthenticationRequired.class})
    public ResponseEntity<ResponseData> tokenFailedException(ProxyAuthenticationRequired ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
    }

    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ResponseData> loginFailedException(UnauthorizedException ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.UNAUTHORIZED.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler({ExpiredResourcesDateException.class, PermissionException.class, IllegalArgumentException.class, SignatureException.class, MalformedJwtException.class, UnsupportedJwtException.class, ExpiredJwtException.class, AccessDeniedException.class})
    public ResponseEntity<ResponseData> permissionException(Exception ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.FORBIDDEN.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler({MissingRequestHeaderException.class, BadRequestException.class})
    public ResponseEntity<ResponseData> headMissException(Exception ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.BAD_REQUEST.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({NotAllowCallingException.class, FileIOException.class})
    public ResponseEntity<ResponseData> notImplementedException(Exception ex, WebRequest request) {
        loggingAdvice(ex, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.NOT_IMPLEMENTED.value()).message(ex.getMessage()).time(new Date()).path(path).build(), UtilsHttp.getHeaders(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseData> handleDefaultException(WebRequest request, Throwable ex) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpStatus.NOT_FOUND.value()).path(path).message(ex.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseData> appBaseException(WebRequest request, BaseException exception) throws IOException {
        loggingAdvice(exception, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).path(path).message(exception.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseData> validateRequestException(WebRequest request, ConstraintViolationException exception) throws IOException {
        loggingAdvice(exception, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpServletResponse.SC_PRECONDITION_FAILED).path(path).message(exception.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.PRECONDITION_FAILED);
    }

    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ResponseData> authenticationException(WebRequest request, AuthenticationException exception) throws IOException {
        loggingAdvice(exception, request);
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        return new ResponseEntity<>(ResponseData.builder().code(HttpServletResponse.SC_UNAUTHORIZED).path(path).message(exception.getMessage()).time(new Date()).build(), UtilsHttp.getHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
