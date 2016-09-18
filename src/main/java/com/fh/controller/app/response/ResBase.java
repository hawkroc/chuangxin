package com.fh.controller.app.response;
import com.fh.util.Const;

public interface ResBase {
public static	final String version_code =Const.VERSION;

default String getVersion_code() {
	return version_code;
}



}
