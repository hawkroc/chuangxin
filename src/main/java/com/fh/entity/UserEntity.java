package com.fh.entity;


public class UserEntity {
//`id`, `phone`, `type`, `lon`, `lat`, `status`
	
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	private String phone;
	private int type;
	private double lon;
	private double 	lat;
	private int status;
	private int push_type;
	public int getPush_type() {
		return push_type;
	}
	public void setPush_type(int push_type) {
		this.push_type = push_type;
	}
	public String getPush_token() {
		return push_token;
	}
	public void setPush_token(String push_token) {
		this.push_token = push_token;
	}
	private String push_token;
	
	
	
	
}
