package com.osp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.core.contants.Constants;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.dto.wso2is.ISCreateUser;
import com.osp.core.dto.wso2is.ISRegistration;
import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.dto.wso2is.ISUserInfo;
import com.osp.core.exception.BaseException;
import com.osp.core.exception.Result;
import com.osp.core.utils.UtilsHttp;
import com.osp.service.Wso2ISService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class Wso2ISServiceImpl implements Wso2ISService {
	@Autowired private RestTemplate restTemplate;
	@Autowired private ObjectMapper objectMapper;
	@Value("${apim.apim_gateway_url}")
	private String APIM_GATEWAY_URL;

    @Override
    public ISUserInfo getUserInfo(String accessToken) {
		try {
			String url = Constants.sso_url + "/oauth2/userinfo?schema=openid";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			ResponseEntity<ISUserInfo> user = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, new HttpEntity<String>(headers), ISUserInfo.class);
			return user.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public ISTokenInfo getAccessTokenByCode(String authorizationCode) {
		try {
			StringBuilder QPM_AUTHCODE_GRANT = new StringBuilder()
					.append("?client_id")
					.append("=")
					.append(Constants.sso_client_id)
					.append("&client_secret")
					.append("=")
					.append(Constants.sso_secret_id)
					.append("&grant_type")
					.append("=")
					.append("authorization_code")
					.append("&code")
					.append("=")
					.append(authorizationCode)
					.append("&redirect_uri")
					.append("=")
					.append(Constants.sso_redirect_uri);
			String url = Constants.sso_url + "/oauth2/token" + QPM_AUTHCODE_GRANT;

			HttpEntity<String> request = new HttpEntity<>(UtilsHttp.getHeadersWithClientCredentials(Constants.sso_client_id, Constants.sso_secret_id));
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, null, Object.class);

			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();

			ISTokenInfo isTokenInfo = new ISTokenInfo();
			isTokenInfo.setAccessToken((String) map.get("access_token"));
			isTokenInfo.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
			isTokenInfo.setTokenType((String) map.get("token_type"));
			isTokenInfo.setRefreshToken((String) map.get("refresh_token"));
			isTokenInfo.setIdToken((String) map.get("id_token"));

			return isTokenInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	@Override
	public ISTokenInfo getAccessTokenByRefreshToken(String refreshToken) {
        try {
            String url = Constants.sso_url + "/oauth2/token";
            HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(Constants.sso_client_id, Constants.sso_secret_id);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestbody = new LinkedMultiValueMap<String, String>();
            requestbody.add("grant_type", "refresh_token");
            requestbody.add("redirect_uri", Constants.sso_redirect_uri);
            requestbody.add("refresh_token", refreshToken);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestbody, headers);
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);

            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();

            ISTokenInfo isTokenInfo = new ISTokenInfo();
            isTokenInfo.setAccessToken((String) map.get("access_token"));
			isTokenInfo.setExpiresIn(Long.parseLong(map.get("expires_in").toString()));
            isTokenInfo.setTokenType((String) map.get("token_type"));
            isTokenInfo.setRefreshToken((String) map.get("refresh_token"));
            isTokenInfo.setIdToken((String) map.get("id_token"));

            return isTokenInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public ISUserInfo getUserInfoAm(String accessToken) {
		try {
			String url = APIM_GATEWAY_URL + "/userinfo";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			ResponseEntity<ISUserInfo> user = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), ISUserInfo.class);
			return user.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getFilter(SearchForm searchObject) {
		String filter = "";
		if (StringUtils.isNotBlank(searchObject.getUsername())) {
			filter += "userName co " + searchObject.getUsername();
		}
		if (StringUtils.isNotBlank(searchObject.getStatus())) {
			if (StringUtils.isNotBlank(filter)) {
				filter += " and ";
			}
			filter += "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.accountLocked eq ";
			if (searchObject.getStatus().equals("0")) {
				filter += "false";
			} else {
				filter += "true";
			}
		}
		if (StringUtils.isNotBlank(searchObject.getTypeRegistration())) {
			if (StringUtils.isNotBlank(filter)) {
				filter += " and ";
			}
			filter += "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.c_typeRegistration eq " + searchObject.getTypeRegistration();
		}
		if (StringUtils.isNotBlank(searchObject.getEmail())) {
			if (StringUtils.isNotBlank(filter)) {
				filter += " and ";
			}
			filter += "emails co " + searchObject.getEmail();
		}
		if (StringUtils.isNotBlank(searchObject.getPhone())) {
			if (StringUtils.isNotBlank(filter)) {
				filter += " and ";
			}
			filter += "phoneNumbers co " + searchObject.getPhone();
		}
		return filter;
	}

	@Override
	public ResponseEntity<Object> searchUsers(SearchForm searchObject, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users/.search";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);

			JSONObject json = new JSONObject();
			if (StringUtils.isNotBlank(searchObject.getStartIndex())) {
				json.put("startIndex", Integer.parseInt(searchObject.getStartIndex()));
			}
			if (StringUtils.isNotBlank(searchObject.getDomain())) {
				json.put("domain", searchObject.getDomain());
			}
			json.put("schemas", Arrays.asList(Constants.SearchRequest.split(",")));
			if (StringUtils.isNotBlank(searchObject.getCount())) {
				json.put("count", Integer.parseInt(searchObject.getCount()));
			}
			json.put("attributes", Arrays.asList(Constants.sso_attributes.split(",")));

			if (StringUtils.isNotBlank(searchObject.getFilter())) {
				json.put("filter", searchObject.getFilter());
			}

			HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody() == null ? new ResponseData<>(Result.SUCCESS) : response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.searchUsers)!");
		}
	}

	@Override
	public ResponseEntity<Object> filterUsers(SearchForm searchForm, String isUrl, String isUser, String password) {
		try {
			searchForm.setAttributes(Constants.sso_attributes);
			String url = isUrl + "/scim2/Users";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			StringBuilder query = new StringBuilder()
					.append("?attributes")
					.append("=")
					.append(searchForm.getAttributes())
					.append("&startIndex")
					.append("=")
					.append(searchForm.getStartIndex())
					.append("&count")
					.append("=")
					.append(searchForm.getCount())
					.append("&domain")
					.append("=")
					.append(searchForm.getDomain());

			if (StringUtils.isNotBlank(searchForm.getFilter())) {
				query.append("&filter").append("=").append(searchForm.getFilter());
			}

			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<Object> response = restTemplate.exchange(url + query, HttpMethod.GET, request, Object.class);
			return new ResponseEntity<>(response.getBody() == null ? new ResponseData<>(Result.SUCCESS) : response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.filterUsers)!");
		}
	}

	@Override
	public ResponseEntity<Object> createUser(ISCreateUser isCreateUser, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(isCreateUser), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.createUser)!");
		}
	}

	@Override
	public ResponseEntity<Object> getUserById(String id, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users/" + id;
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.getUserById)!");
		}
	}

	@Override
	public ResponseEntity<Object> updateUserById(String id, Object data, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users/" + id;
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(data), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.PUT, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.updateUserById)!");
		}
	}

	@Override
	public ResponseEntity<Object> updateMuntiUser(String id, Object data, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users/" + id;
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(data), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.PATCH, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.updateMuntiUser)!");
		}
	}

	@Override
	public ResponseEntity<Object> deleteUserById(String id, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/scim2/Users/" + id;
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.deleteUserById)!");
		}
	}

	@Override
	public ResponseEntity<Object> selfRegistration(ISRegistration isCreateUser, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/api/identity/user/v1.0/me";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(isCreateUser), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody() == null ? new ResponseData<>(Result.SUCCESS) : response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.selfRegistration)!");
		}
	}

	@Override
	public ResponseEntity<Object> lockOrUnlockUser(List<String> ids, boolean status, String isUrl, String isUser, String password) throws JsonProcessingException {
		try {
			for (String id: ids) {
				Object object = getUserById(id, isUrl, isUser, password).getBody();
				JSONObject json = new JSONObject(objectMapper.writeValueAsString(object));
				json.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User", json.getJSONObject("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User").put("accountLocked", status));
				String jsonStr = json.toString();
				updateUserById(id, objectMapper.readValue(jsonStr, Object.class), isUrl, isUser, password);
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.selfRegistration)!");
		}
		return new ResponseEntity<>(new ResponseData<>(Result.SUCCESS), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Object> selfResendCode(Object isCreateUser, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/api/identity/user/v1.0/me/resend-code";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(isCreateUser), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.selfResendCode)!");
		}
	}

	@Override
	public ResponseEntity<Object> selfValidateCode(Object isCreateUser, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/api/identity/user/v1.0/me/validate-code";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(isCreateUser), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.selfValidateCode)!");
		}
	}

	@Override
	public ResponseEntity<Object> validateUsername(Object isCreateUser, String isUrl, String isUser, String password) {
		try {
			String url = isUrl + "/api/identity/user/v1.0/validate-username";
			HttpHeaders headers = UtilsHttp.getHeadersWithClientCredentials(isUser, password);
			HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(isCreateUser), headers);
			ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			return new ResponseEntity<>(response.getBody(), response.getStatusCode());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("Lỗi xử lý data (AuthorizationServiceImpl.validateUsername)!");
		}
	}
}
