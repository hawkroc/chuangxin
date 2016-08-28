package com.fh.util;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
public class JsonUtil {
	
	public static String beanToJson(Object bean) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper m = new ObjectMapper();  
		String value = m.writeValueAsString(bean);  
		return value;
	}

}
