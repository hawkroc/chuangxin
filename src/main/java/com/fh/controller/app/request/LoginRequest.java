package com.fh.controller.app.request;

import com.fh.controller.base.RequestData;
import com.fh.entity.LoginEntity;

public class LoginRequest extends RequestData {
private LoginEntity action;

public LoginEntity getAction() {
	return action;
}

public void setAction(LoginEntity action) {
	
	this.action = action;
}
}
