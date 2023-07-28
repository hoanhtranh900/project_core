package com.osp.core.utils;

import com.osp.core.contants.Constants;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class UtilsHttp {

    public static String getHostName(InetAddress inaHost) throws UnknownHostException, InvocationTargetException {
        try {
            Class clazz = Class.forName("java.net.InetAddress");
            Constructor[] constructors = clazz.getDeclaredConstructors();
            constructors[0].setAccessible(true);
            InetAddress ina = (InetAddress) constructors[0].newInstance();

            Field[] fields = ina.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("nameService")) {
                    field.setAccessible(true);
                    Method[] methods = field.get(null).getClass().getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.getName().equals("getHostByAddr")) {
                            method.setAccessible(true);
                            return (String) method.invoke(field.get(null), inaHost.getAddress());
                        }
                    }
                }
            }
        } catch (ClassNotFoundException cnfe) {
        } catch (IllegalAccessException iae) {
        } catch (InstantiationException ie) {
        } catch (InvocationTargetException ite) {
            throw (UnknownHostException) ite.getCause();
        }
        return null;
    }

    public static void getAllParamsReq(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName);
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                System.out.println(paramValue);
            }
        }
    }

    public static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static HttpHeaders getHeaders(HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static HttpHeaders getHeadersWithClientCredentials(String client_id, String secret_id) {
        String plainClientCredentials = client_id + ":" + secret_id;
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
        HttpHeaders headers = getHeaders();
        headers.add("Authorization", "Basic " + base64ClientCredentials);
        return headers;
    }

    public static String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.HEADER_FIELD.AUTHORIZATION);
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7);
        }
        return null;
    }

    public static String getSecToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.HEADER_FIELD.AUTHORIZATION);
        if (accessToken != null && accessToken.startsWith("Basic ")) {
            return accessToken.substring(6);
        }
        return null;
    }

    public static String getIpClient(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

}
