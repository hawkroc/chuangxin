package com.fh.controller.app.request;

import com.fh.controller.base.RequestData;

public class AddThoughtReq extends RequestData {
	public AddThoughtAction getAction() {
		return action;
	}

	public void setAction(AddThoughtAction action) {
		this.action = action;
	}

	private AddThoughtAction action;

}
