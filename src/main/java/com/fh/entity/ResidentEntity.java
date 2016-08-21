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

	private double geo_lat;
	private double get_lng;
}
