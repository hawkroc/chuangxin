package com.fh.controller.base;

public class SignUpRequest  extends RequestData { 
	/**
	 * “phone”: string,
     “password”: string,
     “verification_code”: string

	 */
	
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVerification_code() {
		return verification_code;
	}
	public void setVerification_code(String verification_code) {
		this.verification_code = verification_code;
	}
	private String password;
	private String verification_code;
	
	

}
