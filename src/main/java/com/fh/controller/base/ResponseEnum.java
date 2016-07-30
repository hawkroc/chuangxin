package com.fh.controller.base;

/**
 * define the response
 */
/**
 * 会员-接口类 
 *    
 * 相关参数协议：
 * 00	请求失败
 * 01	请求成功
 * 02	返回空值
 * 03	请求协议参数不完整    
 * 04  用户名或密码错误
 * 05  FKEY验证失败
*/
public enum ResponseEnum {

	SUCCESS("01", " request  success ！"),
	FAILURE("00", "please try again！"),
	PARAMETERS_INCOMPLETE("03","parameter can not be null or ilegale"),
	RESULT_NULL("02","no data!"),
    NO_PRIMISSION("04","no primission");
//	OTP_VERIFY_FAIL("04","verify failed "),
//	OPEN_FAIL("05"," failed "),
//	ACCOUNT_HAS_REGISTERED("06","already be regisger"),
//	CALL_FAILURE("07","error!"),
//	RESULT_NULL("08","no data!");

	private String code;

	private String message;

	// 构造方法
	private ResponseEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ResponseEnum getResponseEnumByCode(int code) {
		String codeStr = String.valueOf(code);
		for (ResponseEnum responseEnum : ResponseEnum.values()) {
			if (responseEnum.getCode().equals(codeStr)) {
				return responseEnum;
			}
		}
		return ResponseEnum.SUCCESS;
	}

}
