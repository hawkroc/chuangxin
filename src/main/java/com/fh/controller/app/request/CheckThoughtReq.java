package com.fh.controller.app.request;

import com.fh.controller.base.RequestData;

public class CheckThoughtReq   extends RequestData{
	


	public CheckThoughtAction getAction() {
		return action;
	}

	public void setAction(CheckThoughtAction action) {
		this.action = action;
	}

	private CheckThoughtAction action;

}
