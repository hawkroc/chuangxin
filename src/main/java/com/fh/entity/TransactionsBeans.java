package com.fh.entity;

class TransactionsBeans {
//	{
//		  "status": 0,
//		  "prev_status": 0,
//		  "time_remaining": 0,
//		  "zoned_time": "string",
//		  "threading": {
//			"time": "string",
//			"place": "string"
//		  },
//		  "ended_time": "string"
//		}
//	
	
	private String Getsby ;
	public String getGetsby() {
		return Getsby;
	}
	public void setGetsby(String getsby) {
		Getsby = getsby;
	}
	public String getSharesbby() {
		return Sharesbby;
	}
	public void setSharesbby(String sharesbby) {
		Sharesbby = sharesbby;
	}
	public long getBanana_id() {
		return banana_id;
	}
	public void setBanana_id(long banana_id) {
		this.banana_id = banana_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPrev_status() {
		return prev_status;
	}
	public void setPrev_status(int prev_status) {
		this.prev_status = prev_status;
	}
	public String getTime_remaining() {
		return time_remaining;
	}
	public void setTime_remaining(String time_remaining) {
		this.time_remaining = time_remaining;
	}
	public String getZoned_time() {
		return zoned_time;
	}
	public void setZoned_time(String zoned_time) {
		this.zoned_time = zoned_time;
	}
	public String getEnded_time() {
		return ended_time;
	}
	public void setEnded_time(String ended_time) {
		this.ended_time = ended_time;
	}
	public Threading getThreading() {
		return threading;
	}
	public void setThreading(Threading threading) {
		this.threading = threading;
	}
	private String Sharesbby;
	private long banana_id;
	private	int status;
	private	int prev_status;
	private String  time_remaining;
	private String  zoned_time;
	private String  ended_time;
	private Threading threading;
	
}
