package com.fh.controller.app.response;

public class CheckThoughtRes {
//	“status”: int,
//    “image_url”: string,
//   “video_url”: string
	
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	private String image_url;
	private String video_url;

}
