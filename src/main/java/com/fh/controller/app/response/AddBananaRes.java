package com.fh.controller.app.response;

public class AddBananaRes {
	
//	
//	 “status”: int,
//	    “error_msg”: string,
//	    “thought_id”: long,
//	    “num_zones”: int,
//	    “num_reason”: int
	
	
	
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getNum_zones() {
		return num_zones;
	}
	public void setNum_zones(int num_zones) {
		this.num_zones = num_zones;
	}
	public int getNum_reason() {
		return num_reason;
	}
	public void setNum_reason(int num_reason) {
		this.num_reason = num_reason;
	}
	public long getThought_id() {
		return thought_id;
	}
	public void setThought_id(long thought_id) {
		this.thought_id = thought_id;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	private int num_zones;
	private int num_reason;
	private long thought_id;
	private String error_msg;


}
