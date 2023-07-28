package com.osp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.wso2is.ISCreateUser;
import com.osp.core.dto.wso2is.ISRegistration;
import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.dto.wso2is.ISUserInfo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/*
* Service dành cho tích hợp xác thực với WSO2IS
* */
public interface Wso2ISService {

	// lấy thông tin user của wso2is theo token
	ISUserInfo getUserInfo(String accessToken);

	// lấy thông tin user của wso2is grant type code cơ chế oidc
	ISTokenInfo getAccessTokenByCode(String authorizationCode);

	// lấy lại token theo Refresh token của wso2is
	ISTokenInfo getAccessTokenByRefreshToken(String refreshToken);

	// lấy thông tin user của wso2 api manager theo token
    ISUserInfo getUserInfoAm(String accessToken);

	// Filter search user wso2is cơ chế scim2
	String getFilter(SearchForm searchObject);

	// tìm kiếm user trong is
	ResponseEntity<Object> searchUsers(SearchForm searchObject, String isUrl, String isUser, String password);

	// tìm kiếm user trong is
	ResponseEntity<Object> filterUsers(SearchForm searchForm, String isUrl, String isUser, String password);

	// tạo mới user trong is
	ResponseEntity<Object> createUser(ISCreateUser isCreateUser, String isUrl, String isUser, String password);

	// lấy user theo id
	ResponseEntity<Object> getUserById(String id, String isUrl, String isUser, String password);

	// cập nhật user theo id
	ResponseEntity<Object> updateUserById(String id, Object data, String isUrl, String isUser, String password);

	// update nhiều user
	ResponseEntity<Object> updateMuntiUser(String id, Object data, String isUrl, String isUser, String password);

	// xóa user
	ResponseEntity<Object> deleteUserById(String id, String isUrl, String isUser, String password);

	// https://docs.wso2.com/display/IS511/apidocs/self-registration/#!/operations#SelfRegister#litePost
	// Self register user
	ResponseEntity<Object> selfRegistration(ISRegistration isCreateUser, String isUrl, String isUser, String password);

	// khóa or mở khóa user
	ResponseEntity<Object> lockOrUnlockUser(List<String> ids, boolean status, String isUrl, String isUser, String password) throws JsonProcessingException;

	// Self resend code
	ResponseEntity<Object> selfResendCode(Object isCreateUser, String isUrl, String isUser, String password);

	// Self validate code
	ResponseEntity<Object> selfValidateCode(Object isCreateUser, String isUrl, String isUser, String password);

	// Validate username
	ResponseEntity<Object> validateUsername(Object isCreateUser, String isUrl, String isUser, String password);

}
