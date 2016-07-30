package com.fh.controller.base;

/**
 * define the response
 */
public enum ResponseEnum {

	SUCCESS("00", " request  success ！"),
	FAILURE("01", "please try again！"),
	NUMFAILURE("02","parameter can not be null or ilegale"),
	ACCOUNTNOTEXIST("03","there is no this user"),
	OTP_VERIFY_FAIL("04","verify failed "),
	OPEN_FAIL("05"," failed "),
	ACCOUNT_HAS_REGISTERED("06","already be regisger"),
	CALL_FAILURE("07","error!"),
	RESULT_NULL("08","no data!");

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
