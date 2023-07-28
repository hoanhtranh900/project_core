package com.osp.core.exception;

public enum Result {

	SUCCESS(200, "Success"),
	NO_CONTENT(204, "No content"),
	BAD_REQUEST(400, "Bad request"),
	UNAUTHORIZED(401, "Unauthorized"),
	TOKEN_EXPIRE_TIME(401, "Token expire time"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Api not found"),
	METHOD_NOT_ALLOW(405, "Method not allow"),
    SERVER_ERROR(500, "500 Internal server error");

	private int code;
	private String message;

	Result(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return (this.code==1);
	}

}
