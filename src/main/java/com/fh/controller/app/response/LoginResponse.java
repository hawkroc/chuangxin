package com.fh.controller.app.response;

import com.fh.entity.BubbleEntity;

public class LoginResponse  {

//	//private int id;
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	//private String status;
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}

//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	
	
	public BubbleEntity getThought() {
		return thought;
	}
	public void setThought(BubbleEntity thought) {
		this.thought = thought;
	}
//	private String type;
	private BubbleEntity thought;
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}
	private String user_token;
//	public String getPhone() {
//		return phone;
//	}
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//	private String phone;
	
	
	

}
