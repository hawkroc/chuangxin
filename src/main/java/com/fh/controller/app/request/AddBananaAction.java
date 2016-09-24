package com.fh.controller.app.request;

import com.fh.entity.BananaEntity;

//hkhkhu
public class AddBananaAction {
//	{
//	      “action_type”: int,
//	      “banana”: {
//	            “bubble”: {
//	                   “topic”: int,
//	                   “key_word”: string
//	             }  
//	            “product: {
//	                  “selling_reason”: int,
//	                  “item_info”: {
//	                         “name”: string,
//	                          “desc”: string,
//	                          “price”: double,
//	                          “currency”:string
//	                    },
//	                    “tags”: [{
//	                          “tag”: int,
//	                          “value”: string,
//	                          “desc”: string
//	                      },
//	                      {
//	                          “tag”: int,
//	                          “value”: string,
//	                          “desc”: string
//	                      }]
//	               }
//	        }
//	}
	

public int getAction_type() {
	return action_type;
}
public void setAction_type(int action_type) {
	this.action_type = action_type;
}

	private int action_type;
	public BananaEntity getBanana() {
		return banana;
	}
	public void setBanana(BananaEntity banana) {
		this.banana = banana;
	}

	private BananaEntity banana;
	
	
}
