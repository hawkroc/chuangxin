package com.fh.controller.app.response;

import java.util.List;

public class ResidentsListResponse {
private int n;
public int getN() {
	return n;
}
public void setN(int n) {
	this.n = n;
}
public List<Resident> getList() {
	return list;
}
public void setList(List<Resident> list) {
	this.list = list;
}
private List<Resident> list;
}
