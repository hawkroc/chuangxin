package com.fh.entity;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class ThoughtEntity extends BaseEntity{
/**
 *  “thought”: {
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

 */
	
	private String topic;
	public String getTopic() {
	return topic;
}
public void setTopic(String topic) {
	this.topic = topic;
}
public String getMoment() {
	return moment;
}
public void setMoment(String moment) {
	this.moment = moment;
}
public String getVedio_url() {
	return vedio_url;
}
public void setVedio_url(String vedio_url) {
	this.vedio_url = vedio_url;
}
public String getImage_url() {
	return image_url;
}
public void setImage_url(String image_url) {
	this.image_url = image_url;
}
public int getSelling_reason() {
	return selling_reason;
}
public void setSelling_reason(int selling_reason) {
	this.selling_reason = selling_reason;
}
public ProductInfo getProduct_info() {
	return product_info;
}
public void setProduct_info(ProductInfo product_info) {
	this.product_info = product_info;
}
public java.util.List<Tag> getTags() {
	return tags;
}
public void setTags(java.util.List<Tag> tags) {
	this.tags = tags;
}
	private String moment;
	private String vedio_url;
	private String image_url;
	private int selling_reason;
	private ProductInfo product_info;
	private java.util.List<Tag> tags;
	
	
	
	
	
	
}
