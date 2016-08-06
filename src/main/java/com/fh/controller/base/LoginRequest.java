package com.fh.controller.base;

import com.fh.entity.TestEntity;

public class LoginRequest extends RequestData {
private TestEntity action;

public TestEntity getAction() {
	return action;
}

public void setAction(TestEntity action) {
	this.action = action;
}
}
