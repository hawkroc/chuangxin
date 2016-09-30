package com.fh.controller.app.appuser;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fh.controller.app.request.AddBananaAction;
import com.fh.controller.app.response.AddBananaRes;
import com.fh.controller.app.response.CheckThoughtRes;
import com.fh.controller.app.response.LoginResponse;
import com.fh.controller.app.response.ResBase;
import com.fh.controller.app.response.Resident;
import com.fh.controller.app.response.SignUpResponse;
import com.fh.controller.base.BaseController;
import com.fh.entity.LocationEntity;
import com.fh.entity.LoginEntity;
import com.fh.entity.SignUpEntity;
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

	/**
	 * 4.2 @critical Add a banana
	 * 
	 * @param p
	 * @return MultipartHttpServletRequest request
	 */
	@RequestMapping(value = "/bananas1", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody

	public Object addBanana(@RequestBody AddBananaAction p, HttpServletResponse response) {

		String token = request.getHeader("Bearer");
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		AddBananaRes t = null;

		try {
			t = appuserService.saveBanana(p.getBanana(), token, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;

	}

	private boolean checkToken() {
		String token = request.getHeader("Bearer");
		boolean rs = false;
		if (StringUtils.isEmpty(token) || appuserService.getPhoneByTokenFromCache(token) == null) {
			rs = true;
		}

		return rs;
	}

	//
	// private boolean checkFile(String filename){
	//
	//
	// }

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
		  System.out.println("UUUUU"+vidoname);
		long l = new Date().getTime();

		String videoname = l + vidoname;

		String imagename = l + imgname;

		 String Imagepath = Const.Imagepath+ imagename;
		 String Videopath = Const.Videopath + videoname;
//		String Imagepath = Const.testImagepath + imagename;
//		String Videopath = Const.testVideopath + videoname;

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
			// if (list != null) {
			// t.setResidents(list.size());
			// t.setList(list);
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/fileUpload2")
	public String fileUpload2(@RequestParam("video") CommonsMultipartFile file) throws IOException {
		long startTime = System.currentTimeMillis();
		System.out.println("fileName：" + file.getOriginalFilename());
		String path = "c:/" + new Date().getTime() + file.getOriginalFilename();

		File newFile = new File(path);
		// 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
		file.transferTo(newFile);
		long endTime = System.currentTimeMillis();
		System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");
		return "/success";
	}

}
