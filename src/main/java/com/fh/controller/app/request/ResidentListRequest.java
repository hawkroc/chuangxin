package com.fh.controller.app.request;

import com.fh.controller.base.RequestData;
import com.fh.entity.ResidentEntity;

public class ResidentListRequest  extends RequestData {
//	  “user_id”: string,
//	     “user_token”: string,
//	     “current_lat”: double,
//	
	
	
	private ResidentEntity action;

	public ResidentEntity getAction() {
		return action;
	}

	public void setAction(ResidentEntity action) {
		
		this.action = action;
	}
}
