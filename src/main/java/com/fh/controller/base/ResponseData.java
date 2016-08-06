package com.fh.controller.base;

import java.util.HashMap;
import java.util.Map;




/**
 * 返回值统一对象
 */
public class ResponseData {

	public static final String CODE_KEY = "CODE";
	public static final String MSG_KEY = "error_messages";

	
	/**
	 *  “result_code”: integer
     “error_count”: integer
     “error_messages”: []
     “record_for_request”: string
     “record”: object

	 */
	
	
	public static final String Record_For_Request="record_for_request";//string
	public static final String Error_Messages="";//string
	public static final String DATA_KEY = "record";//obj
	public static final String Error_Count="error_count";//int
	public static final String Result_Code="result_code";//int
	
	public static Map<String, Object> creatResponseWithFullMessage(int RCount, int ECount,String EMessages,String RFRequest,Object rec) {
		final Map<String, Object> result = new HashMap<String, Object>();
		//result.put(CODE_KEY, code);
		result.put(Result_Code, RCount);
		result.put(Error_Count, ECount);
		result.put(Error_Messages, EMessages);
		result.put(Record_For_Request, RFRequest);
		result.put(DATA_KEY, rec);
		return result;
	}
	
	
	public static Map<String, Object> creatResponseWithSuccessMessage(String RFRequest,Object obj) {
		final Map<String, Object> result = new HashMap<String, Object>();
		//result.put(CODE_KEY, code);
		result.put(Result_Code, 0);
		result.put(Error_Count, 0);
		result.put(Error_Messages, null);
		result.put(Record_For_Request, RFRequest);
		result.put(DATA_KEY, obj);
		return result;
	}
	
	

	public static Map<String, Object> creatResponseWithFailMessage(int RCount, int ECount,String EMessages,String RFRequest) {
		final Map<String, Object> result = new HashMap<String, Object>();
		//result.put(CODE_KEY, code);
		result.put(Result_Code, RCount);
		result.put(Error_Count, ECount);
		result.put(Error_Messages, EMessages);
		result.put(Record_For_Request, RFRequest);
		return result;
	}
	
	
	
	
	
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
	public static Map<String, Object> buildSuccessResponseWithMeg(String msg) {
		return buildResponse(ResponseEnum.SUCCESS,msg);
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
		//result.put(CODE_KEY, code);
		result.put(MSG_KEY, msg);
		result.put(MSG_KEY, msg);
		result.put(MSG_KEY, msg);
		return result;
	}
}