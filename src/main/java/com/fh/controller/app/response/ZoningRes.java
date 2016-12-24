package com.fh.controller.app.response;

import java.util.List;

import com.fh.util.PageData;

public class ZoningRes {
	public List<PageData> getZoning_requests() {
		return zoning_requests;
	}

	public void setZoning_requests(List<PageData> zoning_requests) {
		this.zoning_requests = zoning_requests;
	}

	private List<PageData> zoning_requests;

}
