package com.fh.controller.app.appuser;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fh.controller.app.request.AddBananaReq;
import com.fh.controller.app.request.CheckThoughtReq;
import com.fh.controller.app.request.ResidentListRequest;
import com.fh.controller.app.response.AddBananaRes;
import com.fh.controller.app.response.CheckThoughtRes;
import com.fh.controller.app.response.LoginResponse;
import com.fh.controller.app.response.ResBase;
import com.fh.controller.app.response.Resident;
import com.fh.controller.app.response.ResidentsListResponse;
import com.fh.controller.app.response.SignUpResponse;
import com.fh.controller.base.BaseController;
import com.fh.controller.base.ResponseData;
import com.fh.entity.LocationEntity;
import com.fh.entity.LoginEntity;
import com.fh.entity.SignUpEntity;
import com.fh.entity.TestEntity;
import com.fh.service.system.appuser.AppuserService;
import com.fh.util.Const;
import com.fh.util.MD5;
import com.fh.util.PageData;

import com.fh.util.Tools;

/**
 * 会员-接口类
 * 
 * 
 */
@Controller
@RequestMapping(value = "/appuser")
@SessionAttributes("test")
public class IntAppuserController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Resource(name = "appuserService")
	private AppuserService appuserService;

	/**
	 * 
	 * @param p
	 * @return
	 */
	// @RequestMapping(value = "/version", method = RequestMethod.GET)
	@RequestMapping(value = { "/version", "/versiontest" }, method = RequestMethod.GET)
	@ResponseBody

	public Object getCurrentVersion() {

		return new ResBase() {
		};

	}

	@RequestMapping(value = { "/sign_up", "/verification_code", "/forgot" }, method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody

	public Object signUp(@RequestBody SignUpEntity p, HttpServletResponse response) {

		SignUpResponse rs = new SignUpResponse();
		HttpSession s = this.getRequest().getSession();
		String token = request.getHeader("Bearer");
		boolean istype = StringUtils.isEmpty(p.getType());
		try {

			if (appuserService.checkPhone(p.getPhone()) != null && istype) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);

			} else if (StringUtils.isEmpty((p.getVerification_code()))
					|| s.getAttribute("Verification_code_time") == null) {
				String Verification_code = String.valueOf(Tools.getRandomNum());
				rs.setVerification_code(Verification_code);
				s.setAttribute("Verification_code", Verification_code);
				s.setAttribute("Verification_code_time", System.currentTimeMillis());
			} else {

				long sec = ((System.currentTimeMillis()) - (long) s.getAttribute("Verification_code_time")) / 1000;

				if (p.getVerification_code().equalsIgnoreCase((String) s.getAttribute("Verification_code"))
						&& sec < Const.secEx) {

					if (!istype) {

						if (!StringUtils.isEmpty(token) && appuserService.getPhoneByTokenFromCache(token) != null) {
							if (appuserService.getPhoneByTokenFromCache(token).equalsIgnoreCase(p.getPhone())) {

								rs.setUser_token(appuserService.updateAppUserPassword(p));
								response.setStatus(HttpServletResponse.SC_CREATED);
								return rs;
							}
						}
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return rs;
					} else {
						rs.setUser_token(appuserService.saveAppUser(p));
						response.setStatus(HttpServletResponse.SC_CREATED);
					}
				} else {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object login(@RequestBody LoginEntity p, HttpServletResponse response) {

		LoginResponse t = null;

		try {
			if (appuserService.checkPhone(p.getPhone()) == null && StringUtils.isEmpty(p.getUser_token())) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return t;
			}
			t = appuserService.updateLoginAppUser(p);
			if (t != null) {
				HttpSession s = this.getRequest().getSession();
				s.setAttribute("LoginResponse", t);

			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return t;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;

	}
	
	
	
	

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/current_locations", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object updateLocation(@RequestBody LocationEntity p, HttpServletResponse response) {


		String token = request.getHeader("Bearer");
		if (StringUtils.isEmpty(token) || appuserService.getPhoneByTokenFromCache(token) == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}else{
			p.setPhone(appuserService.getPhoneByTokenFromCache(token));
		}

		try {
			
			appuserService.udateUserLocation(p);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object logout(HttpServletResponse response) {
		LoginResponse t = null;
		String token = request.getHeader("Bearer");
		if (StringUtils.isEmpty(token) || appuserService.getPhoneByTokenFromCache(token) == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return t;
		}
		try {
			appuserService.updateLogout(token);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return t;

	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/resident_list", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody

	public Object residentList(@RequestBody ResidentListRequest p,
			@RequestHeader(value = "User-Agent") String userAgent) {

		if (StringUtils.isNotBlank(p.getAction().getUser_id())) {
			return ResponseData.creatResponseWithFailMessage(1, 1, "please login first", null);
		}
		ResidentsListResponse t = new ResidentsListResponse();
		try {
			List<Resident> list = appuserService.getResidentList(p.getAction());
			if (list != null) {
				t.setResidents(list.size());
				t.setList(list);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseData.creatResponseWithSuccessMessage(null, t);

	}

	/////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/addBanana", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object addThought(@RequestBody AddBananaReq p, @RequestHeader(value = "User-Agent") String userAgent) {

		if (!StringUtils.isNotBlank(p.getAction().getUser_token())) {
			return ResponseData.creatResponseWithFailMessage(1, 1, "please login first", null);
		}
		System.out.println(p.getAction().toString());
		AddBananaRes t = null;
		try {
			t = appuserService.saveBanana(p.getAction());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseData.creatResponseWithSuccessMessage(null, t);

	}

	// Check thought

	/**
	 * http://api.sosxsos.com/checkThought
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/checkThought", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody

	public Object CheckThought(@RequestBody CheckThoughtReq p, @RequestHeader(value = "User-Agent") String userAgent) {

		if (!StringUtils.isNotBlank(p.getAction().getUser_token())) {
			return ResponseData.creatResponseWithFailMessage(1, 1, "please login first", null);
		}
		CheckThoughtRes t = null;
		try {
			t = appuserService.checkThought(p.getAction());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseData.creatResponseWithSuccessMessage(null, t);

	}

	/////////////////////////////////////////////////////////////////////////////////////////

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
	public String springUpload(HttpServletRequest request, @RequestHeader(value = "User-Agent") String userAgent)
			throws IllegalStateException, IOException {
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

}
