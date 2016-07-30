package com.fh.controller.base;

import java.util.HashMap;
import java.util.Map;


/**
 * 返回值统一对象
 */
public class ResponseData {

	public static final String CODE_KEY = "CODE";
	public static final String MSG_KEY = "MSG";
	public static final String DATA_KEY = "DATA";
	
	public static Map<String, Object> buildResponse(ResponseEnum responseEnum) {
		return buildResponse(responseEnum, null);
	}

	public static Map<String, Object> buildResponse(ResponseEnum responseEnum, Object data) {
		Map<String, Object> result = buildResponse(responseEnum.getCode(), responseEnum.getMessage());
		if (data != null) {
			result.put(DATA_KEY, data);
		}
		return result;
	}

	public static Map<String, Object> buildSuccessResponse(Object data) {
		return buildResponse(ResponseEnum.SUCCESS, data);
	}

	public static Map<String, Object> buildSuccessResponse() {
		return buildSuccessResponse(null);
	}

	public static Map<String, Object> buildFailResponseWithMsg(String msg) {
		return buildResponse(ResponseEnum.FAILURE.getCode(), msg);
	}

	public static Map<String, Object> buildFailResponse() {
		return buildResponse(ResponseEnum.FAILURE);
	}

	public static Map<String, Object> buildResponse(String code, String msg) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE_KEY, code);
		result.put(MSG_KEY, msg);
		return result;
	}
}