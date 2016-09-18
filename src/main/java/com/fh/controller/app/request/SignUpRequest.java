package com.fh.controller.app.request;


import com.fh.entity.SignUpEntity;


public class SignUpRequest  { 
	/**
	 * “phone”: string,
     “password”: string,
     “verification_code”: string

	 */
	
	private SignUpEntity action;

	public SignUpEntity getAction() {
		return action;
	}

	public void setAction(SignUpEntity action) {
		this.action = action;
	}
	
	
	
	

}
