package com.fh.controller.app.request;

public class CheckThoughtAction {
//	  “user_token”: string,
//	     “thought_idthougth”: long

	private String user_token;
	public String getUser_token() {
		return user_token;
	}
	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}
//	public long getThought_idthougth() {
//		return thought_idthougth;
//	}
//	public void setThought_idthougth(long thought_idthougth) {
//		this.thought_idthougth = thought_idthougth;
//	}
	private String topic;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getKey_word() {
		return key_word;
	}
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}
	private String key_word;
}
