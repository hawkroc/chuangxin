package com.fh.entity;

public class BananaEntity extends BaseEntity{
/**
	 * clientsService
	 */
	private static final long serialVersionUID = 1L;
private int id;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getVideo_url() {
	return video_url;
}
public void setVideo_url(String video_url) {
	this.video_url = video_url;
}
public ProductEntity getProduct() {
	return product;
}
public void setProduct(ProductEntity product) {
	this.product = product;
}
public ThoughtEntity getThought() {
	return thought;
}
public void setThought(ThoughtEntity thought) {
	this.thought = thought;
}
private String video_url;
private ProductEntity product;
private ThoughtEntity thought;
private int thoughtId;
public int getThoughtId() {
	return thoughtId;
}
public void setThoughtId(int thoughtId) {
	this.thoughtId = thoughtId;
}
public int getProductId() {
	return productId;
}
public void setProductId(int productId) {
	this.productId = productId;
}
private int productId;


public int getUserid() {
	return userid;
}
public void setUserid(int userid) {
	this.userid = userid;
}
private int 	userid;

}
