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
	
	
	private String phone;
	private ThoughtEntity thought;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}


	private double lat;
	private double lon;
	//private double distance;
	//private String distance_unit;
	
}
