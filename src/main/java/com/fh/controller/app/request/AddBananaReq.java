package com.fh.controller.app.request;

import com.fh.controller.base.RequestData;

public class AddBananaReq extends RequestData {
	public AddBananaAction getAction() {
		return action;
	}

	public void setAction(AddBananaAction action) {
		this.action = action;
	}

	private AddBananaAction action;

}
