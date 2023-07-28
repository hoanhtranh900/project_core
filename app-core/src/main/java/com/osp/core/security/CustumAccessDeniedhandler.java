package com.osp.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.core.dto.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * TODO: write you class description here
 *
 * @author
 */

public class CustumAccessDeniedhandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String path = request.getRequestURI();
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String body = mapper.writeValueAsString(
                ResponseData.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .path(path)
                        .message(accessDeniedException.getMessage())
                        .time(new Date())
                        .build()
        );
        response.getWriter().write(body);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
