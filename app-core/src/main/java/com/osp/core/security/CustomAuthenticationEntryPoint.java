package com.osp.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.utils.UtilsCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * TODO: write you class description here
 *
 * @author
 */

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageSource messageSource;

    @Override
    @ExceptionHandler(value = {AuthenticationException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String path = request.getRequestURI();
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String body = mapper.writeValueAsString(
                ResponseData.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .path(path)
                        .message(messageSource.getMessage("NOTE_EVICT", null, UtilsCommon.getLocale()))
                        .time(new Date()).build()
        );
        response.getWriter().write(body);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
