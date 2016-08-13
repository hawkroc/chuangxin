package com.fh.controller.app.response;

public class SignUpResponse {
	/**
	 * “status”: string,
    “verification_code”: string,
    “user_id”: string,
    “user_token”: string

	 */
	
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVerification_code() {
		return verification_code;
	}
	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}
	private String verification_code;
	private String user_id;
	private String user_token;
	

}
