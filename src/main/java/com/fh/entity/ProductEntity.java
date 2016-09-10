package com.fh.entity;

import java.util.List;


import com.fh.util.JsonUtil;

public class ProductEntity {
private ProductInfo product_info;

private String productInfoByJson;

public String getProductInfoByJson() {
	String json=null;
	if(this.product_info!=null){
	json= JsonUtil.beanToJson(this.product_info);
	}
return json;
	
}
public String getTagsByJson() {
	String json=null;
	if(this.tags!=null){
	json= JsonUtil.beanToJson(this.tags);
	}
return json;
}
private String tagsByJson;

//public String getProductInfoByJson(){
//	String json=null;
//	if(this.product_info!=null){
//	json= JsonUtil.beanToJson(this.product_info);
//	}
//return json;
//	
//}
//
//public String getTagsByJson(){
//	String json=null;
//	if(this.tags!=null){
//	json= JsonUtil.beanToJson(this.tags);
//	}
//return json;
//	
//}




private int id ;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public ProductInfo getProduct_info() {
	return product_info;
}
public void setProduct_info(ProductInfo product_info) {
	this.product_info = product_info;
}
public List<Tag> getTags() {
	return tags;
}
public void setTags(List<Tag> tags) {
	this.tags = tags;
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
private List<Tag> tags;
private String image_url;
private int selling_reason;


}
