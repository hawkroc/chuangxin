package com.fh.controller.app.appuser;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fh.controller.base.BaseController;
import com.fh.controller.base.ResponseData;
import com.fh.entity.TestEntity;
import com.fh.service.system.appuser.AppuserService;

import com.fh.util.PageData;

/**
 * 会员-接口类
 * 
 * 相关参数协议： 00 请求失败 01 请求成功 02 返回空值 03 请求协议参数不完整 04 用户名或密码错误 05 FKEY验证失败
 */
@Controller
@RequestMapping(value = "/appuser")
@SessionAttributes("test")
public class IntAppuserController extends BaseController {

	@Resource(name = "appuserService")
	private AppuserService appuserService;

	/**
	 * 根据用户名获取会员信息
	 */
	@RequestMapping(value = "/getAppuserByUm")
	@ResponseBody
	public Object getAppuserByUsernmae() {
		logBefore(logger, "test");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";

		// try{
		// if(Tools.checkKey("USERNAME", pd.getString("FKEY"))){ //检验请求key值是否合法
		// if(AppUtil.checkParam("getAppuserByUsernmae", pd)){ //检查参数
		// pd = appuserService.findByUId(pd);
		//
		// map.put("pd", pd);
		// result = (null == pd) ? "02" : "01";
		//
		// }else {
		// result = "03";
		// }
		// }else{
		// result = "05";
		// }
		// }catch (Exception e){
		// logger.error(e.toString(), e);
		// }finally{
		// map.put("result", result);
		// logAfter(logger);
		// }

		return ResponseData.buildSuccessResponseWithMeg("dsfdsfdsf");
		/// return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/getTest", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	@Cacheable
	public Object getAppuserAll(@RequestBody TestEntity p) {
		logBefore(logger, "TEST @RequestBody");
		return ResponseData.buildSuccessResponseWithMeg("" + p.getName() + p.getRole());

	}

	/**
	 * Restful API
	 * 
	 * @param name
	 * @param role
	 * @param phone
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/getTest2/{name}/{role}", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object getAppuserAll2(@PathVariable String name, @PathVariable String role) {
		logBefore(logger, "testAPI");
		return ResponseData.buildSuccessResponseWithMeg("" + name + role);
		/// return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 
	 * @param phone
	 * @param p
	 * @return
	 */
	
	@RequestMapping(value = "/getTest3", method = RequestMethod.GET)
	@ResponseBody
   
	public Object getAppuserAll3(@CookieValue(value = "userPhone", required = false) String phone,
			@ModelAttribute("test") TestEntity p) {
		logBefore(logger, "TEST2");
		return ResponseData.buildSuccessResponseWithMeg("" + phone);
		/// return AppUtil.returnObject(new PageData(), map);
	}

}
