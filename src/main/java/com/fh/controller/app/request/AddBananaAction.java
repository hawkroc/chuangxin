package com.fh.controller.app.request;

import com.fh.entity.BananaEntity;

//hkhkhu
public class AddBananaAction {
//	“action”: {
//    “user_token”: string,
//    “action_type”: int,
//    “banana”: {
//          “thought”: {
//                 “topic”: string,
//                 “key_word”: string
//           }
//          “video_url”: string,
//          “product: {
//                “image_url”: string,
//                “selling_reason”: int,
//                “item_info”: {
//                       “name”: string,
//                        “desc”: string,
//                        “price”: double
//                  },
//                  “tags”: [{
//                        “tag”: int,
//                        “value”: string,
//                        “desc”: string
//                    },
//                    {
//                        “tag”: int,
//                        “value”: string,
//                        “desc”: string
//                    }]
//             }
//      }
//}
	
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

	private long timestamp;
	private int action_type;
	public BananaEntity getBanana() {
		return banana;
	}
	public void setBanana(BananaEntity banana) {
		this.banana = banana;
	}

	private BananaEntity banana;
	
	
}
