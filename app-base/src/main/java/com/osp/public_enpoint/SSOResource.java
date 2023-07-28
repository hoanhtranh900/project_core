package com.osp.public_enpoint;

import com.osp.core.contants.Constants;
import com.osp.core.dto.request.LoginRequest;
import com.osp.core.dto.response.UserInfo;
import com.osp.core.exception.UnauthorizedException;
import com.osp.core.utils.UtilsCommon;
import com.osp.service.SrsSystemSercice;
import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.exception.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController
@RequestMapping(value = "/public/v1/sso", produces = MediaType.APPLICATION_JSON_VALUE)
public class SSOResource {

    @Autowired private MessageSource messageSource;
    @Autowired private SrsSystemSercice srsSystemSercice;

    @ApiOperation(response = UserInfo.class, notes = Constants.NOTE_API + "empty_note", value = "Login đăng nhập", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(value = "/jwt-login")
    public ResponseEntity<ResponseData> loginJWT(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) throws UnauthorizedException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println(request.getHeader("Accept-Language"));
        log.info("language current: " + UtilsCommon.getLocale() + ", examle error_401: " + messageSource.getMessage("error_401", null, UtilsCommon.getLocale()));
        loginRequest.setIp(request.getRemoteAddr());
        loginRequest.setUserAgent(request.getRemoteUser());
        return new ResponseEntity<>(new ResponseData<>(srsSystemSercice.loginJWT(loginRequest), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = Boolean.class, notes = Constants.NOTE_API + "empty_note", value = "logout wso2is", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/jwt-logout")
    public ResponseEntity<ResponseData> logoutWso2IS() {
        return new ResponseEntity<>(new ResponseData<>(srsSystemSercice.logoutJwt(), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = ISTokenInfo.class, notes = Constants.NOTE_API + "empty_note", value = "refreshToken wso2is", authorizations = {@Authorization(value = Constants.API_KEY)})
    @RequestMapping(value = "/jwt-refresh-token", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> getAccessTokenByRefreshTokenJwt(@Valid @RequestBody String refreshToken) throws UnauthorizedException {
        return new ResponseEntity<>(new ResponseData<>(srsSystemSercice.getAccessTokenByRefreshTokenJwt(refreshToken), Result.SUCCESS), HttpStatus.NO_CONTENT);
    }

    @ApiOperation(response = UserInfo.class, notes = Constants.NOTE_API + "empty_note", value = "Login wso2is", authorizations = {@Authorization(value = Constants.API_KEY)})
    @RequestMapping(value = "/is-login", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> loginIS(@Valid @RequestBody String authorizationCode) throws UnauthorizedException {
        return new ResponseEntity<>(new ResponseData<>(srsSystemSercice.loginIS(authorizationCode), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = ISTokenInfo.class, notes = Constants.NOTE_API + "empty_note", value = "refreshToken wso2is", authorizations = {@Authorization(value = Constants.API_KEY)})
    @RequestMapping(value = "/is-refresh-token", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> getAccessTokenByRefreshToken(@Valid @RequestBody String refreshToken) {
        return new ResponseEntity<>(new ResponseData<>(srsSystemSercice.getAccessTokenByRefreshToken(refreshToken), Result.SUCCESS), HttpStatus.NO_CONTENT);
    }

}
