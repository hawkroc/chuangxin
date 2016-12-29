package com.fh.controller.app.response;

import com.fh.entity.BubbleEntity;
import com.fh.entity.MediaEntity;
import com.fh.entity.StatisticsEntity;

public class ResProfile {
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isVerified_id() {
		return verified_id;
	}
	public void setVerified_id(boolean verified_id) {
		this.verified_id = verified_id;
	}
	public String getVerified_email() {
		return verified_email;
	}
	public void setVerified_email(String verified_email) {
		this.verified_email = verified_email;
	}
	public StatisticsEntity getStatistics() {
		return statistics;
	}
	public void setStatistics(StatisticsEntity statistics) {
		this.statistics = statistics;
	}
	public MediaEntity getMedia() {
		return media;
	}
	public void setMedia(MediaEntity media) {
		this.media = media;
	}
	public BubbleEntity getBubble() {
		return bubble;
	}
	public void setBubble(BubbleEntity bubble) {
		this.bubble = bubble;
	}
	private int status;
	private boolean verified_id;
	private String verified_email;
	private StatisticsEntity statistics;
	private MediaEntity media;
	private BubbleEntity bubble;

	
	
//
//{
//  "status": int,
//  "verified_id": boolean,
//  "verified_email": string,
//  "bubble":{
//    "topic":int,    
//    "key_word":string
//  },
//  "media":{
//    "video_url":string,
//    "image_url":string
//  },
//  "statistics": {
//    "zoned": int
//    "reported": int
//    "agree": int
//    "success": int
//    "recycle": int
//    "video": int
//  }
//}

	
	
	
	
	
	

}
