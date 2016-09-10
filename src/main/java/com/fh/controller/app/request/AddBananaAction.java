package com.fh.controller.app.request;

import com.fh.entity.ThoughtEntity;
//hkhkhu
public class AddBananaAction {
/**
 * 
“action”: {
     “user_token”: string,
     “timestamp”: long，
     “action_type”: int,
     “thought”: {
            “topic”: string,
            “moment”: string,
            “vedio_url”: string,
            “image_url”: string,
            “selling_reason”: int,
            “product_info”: {
                   “name”: string,
                   “desc”: string,
                   “price”: double
             },
             “tags”: [
                    {
                         tag: int,
                         value1: string,
                         value2: string,
                         desc: string.
                    },
                    {
                         tag: int,
                         value1: string,
                         value2: string,
                         desc: string.
                    }
             ]
       }
}
   0 - share a banana
     1 - request a banana

 */
	
	
	private String user_token;
	public String getUser_token() {
	return user_token;
}
public void setUser_token(String user_token) {
	this.user_token = user_token;
}
public long getTimestamp() {
	return timestamp;
}
public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
}
public int getAction_type() {
	return action_type;
}
public void setAction_type(int action_type) {
	this.action_type = action_type;
}
public ThoughtEntity getThought() {
	return thought;
}
public void setThought(ThoughtEntity thought) {
	this.thought = thought;
}
	private long timestamp;
	private int action_type;
	private ThoughtEntity thought;
	
	
}
