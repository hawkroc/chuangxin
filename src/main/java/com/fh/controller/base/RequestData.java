package com.fh.controller.base;

import java.io.Serializable;

public class RequestData implements Serializable {
/**
 * “api_version”: integer,
    “action_version”: integer
    “action”: object

 */
	
	private  String  api_version;
	public String getApi_version() {
	return api_version;
}
public void setApi_version(String api_version) {
	this.api_version = api_version;
}
public String getAction_version() {
	return action_version;
}
public void setAction_version(String action_version) {
	this.action_version = action_version;
}
	private String action_version;

}
