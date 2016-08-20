package com.fh.controller.app.appuser;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fh.controller.app.request.LoginRequest;
import com.fh.controller.app.request.SignUpRequest;
import com.fh.controller.app.response.LoginResponse;
import com.fh.controller.app.response.SignUpResponse;
import com.fh.controller.base.BaseController;
import com.fh.controller.base.ResponseData;
import com.fh.entity.TestEntity;
import com.fh.service.system.appuser.AppuserService;

import com.fh.util.MD5;
import com.fh.util.PageData;

import com.fh.util.Tools;

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
		logBefore(logger, "根据用户名获取会员信息");
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";

		try {

			pd = appuserService.findByUId(pd);

			map.put("pd", pd);
			result = (null == pd) ? "02" : "01";

		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			map.put("result", result);
			logAfter(logger);
		}

		return map;
	}
	
	

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object logout(@RequestBody LoginRequest p) {
		try {
			appuserService.Logout(p.getAction());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseData.creatResponseWithSuccessMessage(null, "logout success");

	}

	
	

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object login(@RequestBody LoginRequest p) {
	  
		LoginResponse t=null;
		
		try {
			if (appuserService.checkPhone(p.getAction().getPhone()) == null&& StringUtils.isEmpty(p.getAction().getUser_token())) {
				return ResponseData.creatResponseWithFailMessage(1,1,"there are no this user","Rf");
			} 
			t= appuserService.loginAppUser(p.getAction());
			if(t!=null){
				HttpSession s = this.getRequest().getSession();
				s.setAttribute("LoginResponse", t);
				t.setStatus("1");
			}else{
				//t.setStattus("faild");
				return ResponseData.creatResponseWithFailMessage(1,1,"password is error","Rf");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseData.creatResponseWithSuccessMessage(null, t);

	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object signUp(@RequestBody SignUpRequest p) {

		
		SignUpResponse rs = new SignUpResponse();
		HttpSession s = this.getRequest().getSession();

		try {

			if (appuserService.checkPhone(p.getAction().getPhone()) != null) {
				rs.setStatus("you already sigup please login");
			} else if (StringUtils.isEmpty((p.getAction().getVerification_code()))||s.getAttribute("Verification_code_time")==null) {
				rs.setStatus("pending");
				String Verification_code = String.valueOf(Tools.getRandomNum());
				rs.setVerification_code(Verification_code);
				s.setAttribute("Verification_code", Verification_code);
				logBefore(logger, "RandomNum is " + Verification_code);
				s.setAttribute("Verification_code_time", System.currentTimeMillis());
			} else {
		
				long sec = ((System.currentTimeMillis()) - (long) s.getAttribute("Verification_code_time")) / 1000;
				// long temp =()->
				if (p.getAction().getVerification_code().equalsIgnoreCase((String) s.getAttribute("Verification_code"))
						&& sec < 90) {
					appuserService.saveAppUser(p.getAction());
					rs.setStatus("success");

					rs.setUser_id(String.valueOf(appuserService.checkPhone(p.getAction().getPhone()).intValue()));
				} else {
					rs.setStatus("verify_failed");
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	

		return ResponseData.creatResponseWithSuccessMessage(null, rs);

	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/getTest", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object getAppuserAll(@RequestBody TestEntity p) {
		logBefore(logger, "TEST @RequestBody");

		return ResponseData.buildSuccessResponseWithMeg("" + p.getName() + p.getRole());

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getTest5/{id}", method = RequestMethod.POST)
	@ResponseBody

	public Object getAppuserAllByCache(@PathVariable String id) {
		logBefore(logger, "TEST cache");

		String s = MD5.md5(id);
		System.out.println(s);
		return ResponseData.buildSuccessResponseWithMeg(s);

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

	/**
	 * 
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("springUpload")
	public String springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String path = "E:/springUpload" + file.getOriginalFilename();
					// 上传
					file.transferTo(new File(path));
				}

			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("方法三的运行时间：" + String.valueOf(endTime - startTime) + "ms");
		return "/success";
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("fileUpload2")
	public String fileUpload2(@RequestParam("file") CommonsMultipartFile file) throws IOException {
		long startTime = System.currentTimeMillis();
		System.out.println("fileName：" + file.getOriginalFilename());
		String path = "E:/" + new Date().getTime() + file.getOriginalFilename();

		File newFile = new File(path);
		// 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
		file.transferTo(newFile);
		long endTime = System.currentTimeMillis();
		System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");
		return "/success";
	}

}
