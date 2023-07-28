package com.osp.core.utils;

import com.osp.core.entity.AdmUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Optional;

/**
 * TODO: write you class description here
 *
 * @author
 */

public class UtilsCommon {

    public static Locale getLocale() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getLocale();
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }

    public static String getUserNameLogin() {
        try {
            AdmUser authentication = (AdmUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return authentication.getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    public static Optional<AdmUser> getUserLogin() {
        try {
            AdmUser user = (AdmUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Authentication getAuthentication() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
    }

    public static void addSession(String key, Object value) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute(key, value);
    }

    public static HttpSession sessionContext() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public static Object getSessionKey(String key) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true).getAttribute(key);
    }
}
