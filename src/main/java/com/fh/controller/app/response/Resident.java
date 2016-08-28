package com.fh.controller.app.response;

import com.fh.entity.ThoughtEntity;

public class Resident {
//	  “user_id”: string,
//      “phone”: string,
//      “thought”: string,
//      “geo_lat”: double,
//      “get_lng”: double,
//      “distance”: double,
//      “distance_unit”: string
	
	private String user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ThoughtEntity getThought() {
		return thought;
	}
	public void setThought(ThoughtEntity thought) {
		this.thought = thought;
	}
	public double getGeo_lat() {
		return geo_lat;
	}
	public void setGeo_lat(double geo_lat) {
		this.geo_lat = geo_lat;
	}
	public double getGet_lng() {
		return get_lng;
	}
	public void setGet_lng(double get_lng) {
		this.get_lng = get_lng;
	}
	private String phone;
	private ThoughtEntity thought;
	private double geo_lat;
	private double get_lng;
	//private double distance;
	//private String distance_unit;
	
}
