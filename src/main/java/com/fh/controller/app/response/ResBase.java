package com.fh.controller.app.response;
import com.fh.util.Const;

public interface ResBase {
public static	final double version_code =Const.VERSION;

default double getVersion_code() {
	return version_code;
}



}
