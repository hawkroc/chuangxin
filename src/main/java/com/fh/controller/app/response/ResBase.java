package com.fh.controller.app.response;
import com.fh.util.Const;

public interface ResBase {
public static	final Integer version_code =Const.VERSION;

default Integer getVersion_code() {
	return version_code;
}



}
