package com.fh.controller.app.response;

public class LoginResponse {
//	status”: string,
//    “type”: string,
//    “thought”: string
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String stattus;
	public String getStattus() {
		return stattus;
	}
	public void setStattus(String stattus) {
		this.stattus = stattus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThought() {
		return thought;
	}
	public void setThought(String thought) {
		this.thought = thought;
	}
	private String type;
	private String thought;
	
	
	

}
