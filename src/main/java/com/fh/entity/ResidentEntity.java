package com.fh.entity;

public class ResidentEntity {
  //  “current_lng”: double
//	  “user_id”: string,
//    “user_token”: string,
//    “current_lat”: double,
	private String user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	private String user_token;
	
	

	public double getCurrent_lat() {
		return current_lat;
	}
	public void setCurrent_lat(double current_lat) {
		this.current_lat = current_lat;
	}
	public double getCurrent_lng() {
		return current_lng;
	}
	public void setCurrent_lng(double current_lng) {
		this.current_lng = current_lng;
	}

	private double current_lat;
	private double current_lng;
}
