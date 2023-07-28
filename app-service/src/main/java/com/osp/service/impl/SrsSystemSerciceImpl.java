package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.LoginRequest;
import com.osp.core.dto.response.UserInfo;
import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.dto.wso2is.ISUserInfo;
import com.osp.core.entity.AdmRight;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.AdmUserSession;
import com.osp.core.exception.UnauthorizedException;
import com.osp.core.repository.*;
import com.osp.core.security.TokenHelper;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.SrsSystemSercice;
import com.osp.service.Wso2ISService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SrsSystemSerciceImpl implements SrsSystemSercice {

    @Autowired private Wso2ISService wso2ISService;
    @Autowired private AdmUserRepository admUserRepository;
    @Autowired private AdmRightRepository admRightRepository;
    @Autowired private AdmGroupRepository groupRepository;
    @Autowired private TokenHelper tokenHelper;
    @Autowired private MessageSource messageSource;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private AdmUserSessionRepository userSessionRepository;
    @Autowired private AdmAuthoritiesRepository authoritiesRepository;
    @Autowired private TelegramBot telegramBot;

    @Override
    public UserInfo loginJWT(LoginRequest loginRequest) throws UnauthorizedException {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            String session = servletRequestAttributes.getSessionId();

            AdmUser user = (AdmUser) authentication.getPrincipal();

            List<String> listRole = authoritiesRepository.loadListAuthorityOfUserByUsername(user.getUsername(), ConstantString.STATUS.active, ConstantString.IS_DELETE.active);

            UserInfo userInfo = new UserInfo();
            // Trả về jwt cho người dùng.
            String jwt = tokenHelper.generateToken(loginRequest, user, session);
            ISTokenInfo accessToken = new ISTokenInfo();
            accessToken.setAccessToken(jwt);
            accessToken.setTokenType("Bearer");
            accessToken.setExpiresIn(tokenHelper.getClaimsFromToken(jwt).getExpiration().getTime());
            userInfo.setAccessTokenInfo(accessToken);
            userInfo.setUsername(user.getUsername());
            userInfo.setMobileAlias(user.getPhoneNumber());
            userInfo.setEmail(user.getEmail());
            userInfo.setAuthorities(listRole);

            List<AdmRight> rights = admRightRepository.findAll();
            userInfo.setRights(rights);

            Optional<AdmUserSession> userSession = userSessionRepository.findByIpAddress(loginRequest.getIp());
            if (!userSession.isPresent()) {
                userSessionRepository.save(new AdmUserSession(session, loginRequest.getIp(), admUserRepository.getById(user.getId())));
            }

            return userInfo;
        } catch (BadCredentialsException e) {
            log.error(loginRequest.getUsername() + "|" + e.getMessage());
            Locale locale = LocaleContextHolder.getLocale();
            LocaleContextHolder.setDefaultLocale(locale);
            Locale locale1 = LocaleContextHolder.getLocaleContext().getLocale();
            String msg = messageSource.getMessage("MSG_LOGIN_FAILED", null, locale);
            throw new UnauthorizedException(msg);
        } catch (AccessDeniedException ex1) {
            log.error(loginRequest.getUsername() + "|" + ex1.getMessage());
            throw new AccessDeniedException(messageSource.getMessage("error_401", null, UtilsCommon.getLocale()));
        } catch (LockedException ex) {
            log.error(loginRequest.getUsername() + "|" + ex.getMessage());
            throw new UnauthorizedException(messageSource.getMessage("MSG_LOCKED_ACCOUNT", null, UtilsCommon.getLocale()));
        } catch (DisabledException exx) {
            log.error(loginRequest.getUsername() + "|" + exx.getMessage());
            throw new UnauthorizedException(messageSource.getMessage("MSG_DISABLE_ACCOUNT", null, UtilsCommon.getLocale()));
        } catch (AuthenticationException atx) {
            log.error(loginRequest.getUsername() + "|" + atx.getMessage());
            throw new UnauthorizedException(messageSource.getMessage("NOTE_EVICT", null, UtilsCommon.getLocale()));
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
            log.error(loginRequest.getUsername() + "|" + ex.getMessage());
            throw new UnauthorizedException(messageSource.getMessage("error.login_fail", null, UtilsCommon.getLocale()));
        }
    }

    @Override
    public UserInfo getAccessTokenByRefreshTokenJwt(String refreshToken) throws UnauthorizedException {

        return null;
    }

    @Override
    public boolean logoutJwt() {
        try {
            Optional<AdmUser> us = UtilsCommon.getUserLogin();
            if (us.isPresent()) {
                Optional<AdmUserSession> userOpt = userSessionRepository.findByIpAddress(us.get().getIpAddress());
                if (userOpt.isPresent()) {
                    AdmUserSession user = userOpt.get();
                    userSessionRepository.delete(user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserInfo loginIS(String authorizationCode) throws UnauthorizedException {
        ISTokenInfo isTokenInfo = wso2ISService.getAccessTokenByCode(authorizationCode);
        if (isTokenInfo != null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAccessTokenInfo(isTokenInfo);
            ISUserInfo isUserInfo = wso2ISService.getUserInfo(isTokenInfo.getAccessToken());
            if(isUserInfo != null) {
                AdmUser admUser = admUserRepository.findByUsername(isUserInfo.getSub()).orElseThrow(() -> new UnauthorizedException(messageSource.getMessage("error.login_fail", null, UtilsCommon.getLocale())));

                List<String> list = authoritiesRepository.loadListAuthorityOfUserByUsername(admUser.getUsername(), ConstantString.STATUS.active, ConstantString.IS_DELETE.active);

                userInfo.setUsername(admUser.getUsername());
                userInfo.setMobileAlias(admUser.getPhoneNumber());
                userInfo.setEmail(admUser.getEmail());
                userInfo.setAuthorities(list);

                List<AdmRight> rights = admRightRepository.findAll();
                userInfo.setRights(rights);

                return userInfo;
            }
        }
        return null;
    }

    @Override
    public ISTokenInfo getAccessTokenByRefreshToken(String refreshToken) {
        return wso2ISService.getAccessTokenByRefreshToken(refreshToken);
    }

}
