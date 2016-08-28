package com.fh.util;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fh.entity.ProductInfo;
public class JsonUtil {
	
	public static String beanToJson(Object bean)  {
		ObjectMapper m = new ObjectMapper();  
		String value=null;
		try {
			value = m.writeValueAsString(bean);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return value;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		ProductInfo p = new ProductInfo();
		p.setDesc("dsfdsfdsf");
		p.setName("name");
		p.setPrice(45.0);
		System.out.println(JsonUtil.beanToJson(p));
		

	}
}
