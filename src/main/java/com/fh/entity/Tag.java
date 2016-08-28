package com.fh.entity;

public class Tag extends BaseEntity{
//	  tag: int,
//      value1: string,
//      value2: string,
//      desc: string.
	
	private int tag;
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	private String value1 ;
	private String value2 ;
	
	

}
