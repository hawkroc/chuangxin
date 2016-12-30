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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
import com.fh.controller.app.response.ResProfile;
import com.fh.controller.app.response.Resident;
import com.fh.controller.app.response.SignUpResponse;
import com.fh.controller.app.response.TheardingRes;
import com.fh.controller.base.BaseController;
import com.fh.entity.BananaEntity;
import com.fh.entity.LocationEntity;
import com.fh.entity.LoginEntity;
import com.fh.entity.PushBean;
import com.fh.entity.SignUpEntity;
import com.fh.entity.Threading;
import com.fh.entity.TransactionsBeans;
import com.fh.entity.UserEntity;
import com.fh.service.system.appuser.AppuserService;
import com.fh.service.system.appuser.CacheService;
import com.fh.service.system.appuser.EmailService;
import com.fh.service.system.appuser.SmsService;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.FileUtil;
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

	@Resource(name = "cacheService")
	private CacheService cacheService;

	@Resource(name = "emailService")
	private EmailService emailService;

	@Resource(name = "threadsPool")
	private ThreadPoolTaskExecutor threadsPool;


	@Resource(name="smsService")
	private SmsService smsService;

	/**
	 * 1.Current version
	 * 
	 * @param p
	 * @return
	 */
	// @RequestMapping(value = "/version", method = RequestMethod.GET)
	@RequestMapping(value = { "/version", "/versiontest" }, method = RequestMethod.GET)
	@ResponseBody

	public Object getCurrentVersion(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
		// System.out.println("tst");
		// String token = request.getHeader("Bearer");
		// boolean istype = StringUtils.isEmpty(p.getType());
		try {

			if (appuserService.checkPhone(p.getPhone()) != null) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);

			} else {
				rs = new SignUpResponse();
				String Verification_code = String.valueOf(Tools.getRandomNum());
		
				rs.setVerification_code(Verification_code);
				smsService.sendMessage(p.getPhone(),Verification_code);
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
		} else {
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
	@RequestMapping(value = "/verification/codes", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public void get_verification_code(CommonRequst email, HttpServletResponse response) {
		// String token = request.getHeader("Bearer");
		String token = request.getHeader("Bearer");
		UserEntity userEntity = getUserFromCache(token);
		if (userEntity == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		}
		int code = (int) (Math.random() * 9000 + 1000);
		// String token = request.getHeader("Bearer");
		userEntity.setCode(code);
		String verified_email = email.getEmail();
		userEntity.setVerified_email(verified_email);

		emailService.setCode(code);
		emailService.setMaill(verified_email);
		threadsPool.execute(emailService);
		cacheService.updateCacheUse(userEntity, token);

	}

	/**
	 * 2.6.1 Verify email address https://api.sosxsos.com/v1/verification/codes
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/verification/emailes", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public Object verify_email(CommonRequst code, HttpServletResponse response) {
		String token = request.getHeader("Bearer");

		UserEntity userEntity = getUserFromCache(token);

		if (userEntity == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		if (StringUtils.isNotEmpty(code.getCode())) {
			int n = Integer.valueOf((code.getCode()));
			if (n != 0 || n == userEntity.getCode()) {
				// update status in db
				try {
					appuserService.updateUserMail(userEntity);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.setStatus(HttpServletResponse.SC_OK);

			} else {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				// userEntity.setVerified_email(null);
			}
			// userEntity.setCode(0);
			cacheService.updateCacheUse(userEntity, token);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return null;
	}

	// /**
	// * 2.6.1 Verify email address
	// https://api.sosxsos.com/v1/verification/codes
	// *
	// * @param p
	// * @return
	// */
	// @RequestMapping(value = "verification/emails", method =
	// RequestMethod.POST, produces = {
	// "application/json;charset=UTF-8" })
	// public void verify_email(CommonRequst code, HttpServletResponse response)
	// {
	// // String token = request.getHeader("Bearer");
	// if (checkToken()) {
	// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	//
	// } else {
	// // Verify mail
	//
	// }
	//
	// }

	/**
	 * 2.7 Upload ID photos
	 * https://api.sosxsos.com/v1/verification/identifications
	 * 
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "verification/identifications", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public Object identifications(@RequestParam("image") CommonsMultipartFile image, HttpServletResponse response) {
		// String token = request.getHeader("Bearer");
		UserEntity userEntity = getUserFromCache();
		if (userEntity == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		}

		String imgname = image.getOriginalFilename();
		// System.out.println(vidoname);
		if (!FileUtil.checkFileType(imgname, Const.imageType)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		// long l = new Date().getTime();

		String imagename = userEntity.getPhone();

		String Imagepath = Const.profile + imagename;

		// String i = "user_profile/" + imagename;

		ObjectMapper mapper = new ObjectMapper();

		File newImage = new File(Imagepath);
		// 通过CommonsMultipartFile的方法直接写文件（注意这个时候）
		try {

			image.transferTo(newImage);
			System.out.println("this is the image path " + Imagepath);
			appuserService.updateUserProfile(userEntity);

			// AddBananaAction addBananaAction = mapper.readValue(json,
			// AddBananaAction.class);
			// System.out.println("test key word: " +
			// addBananaAction.getBanana().getBubble().getKey_word());
			// t = appuserService.saveBanana(addBananaAction.getBanana(), token,
			// i, v);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;

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

	private UserEntity getUserFromCache(String token) {

		return cacheService.getUserByTokenFromCache(token);
	}

	private UserEntity getUserFromCache() {

		String token = request.getHeader("Bearer");

		return this.getUserFromCache(token);
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
		// System.out.println("dsafsdaf" + json);
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
			// System.out.println("test key word: " +
			// addBananaAction.getBanana().getBubble().getKey_word());
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
		// System.out.println("topic: " + topic + " key_word: " + key_word);
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
	@RequestMapping(value = "/transactions", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResCommon startTransaction(@RequestBody CommonRequst common, HttpServletResponse response) {

		// 5.1.1 Start Transaction & Zoning
		//// 5.2 Threading https://api.sosxsos.com/v1/transactions/#/threading
		// 5.3 Finish the transaction
		// * //5.4 Cancel the transaction
		// *https://api.sosxsos.com/v1/transactions/#/cancellation
		ResCommon result = new ResCommon();
		String token = request.getHeader("Bearer");
		UserEntity getby = getUserFromCache(token);

		UserEntity shareby = null;
		TransactionsBeans t = null;

		if (getby == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return result;

		}

		if (getby.getStatus() < 2) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseResult(HttpServletResponse.SC_FORBIDDEN);
			return result;
		}

		if (common != null && common.getBanana_id() != 0) {
			BananaEntity banana = cacheService.getBananaFromCacheById(common.getBanana_id());
			if (banana == null) {
				response.setStatus(HttpServletResponse.SC_GONE);
				return result;
			}
			System.out.println(getby.getId() + 1);
			if (banana.getStatus() == 1) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return result;
			}
			shareby = cacheService.getUserByTokenFromCache(banana.getUserid());

			if (shareby.getStatus() < 2) {
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				return result;
			}

			try {
				t = appuserService.generateTransactionsBeans(getby, banana.getId(), shareby);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (t != null) {

			result.setTransaction_id(t.getId());
			result.setStatus(t.getStatus());

			// Zoning push notification
		}
		return result;

	}

	/**
	 * 
	 * @param id
	 * @param common
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/transactions/{id}/zoning" }, method = RequestMethod.POST)
	@ResponseBody

	public ResCommon zoningActive(@PathVariable String id, @RequestBody CommonRequst common,
			HttpServletResponse response) {
		System.out.println("this zoningActive is ");
		ResCommon rs = new ResCommon();
		int status = 0;
		// System.out.println(test);
		if (checkToken()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}
		if (common == null || !StringUtils.isNotEmpty(id)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return rs;
		}

		TransactionsBeans transactionsBeans = null;
		try {
			transactionsBeans = cacheService.getTransactionFromCache(id, "Transactions");
			if (transactionsBeans == null) {
				status = Const.zoningtimeout;
				// push notifiction timeout
				response.setStatus(HttpServletResponse.SC_GONE);
				return rs;

			}

			rs.setTransaction_id(transactionsBeans.getId());
			BananaEntity banana = cacheService.getBananaFromCacheById(transactionsBeans.getBanana_id());
			if (banana == null) {
				// push notifiction timeout
				status = Const.bananaExpired;
				response.setStatus(HttpServletResponse.SC_GONE);
				return rs;
			}

			status = transactionsBeans.getStatus();

			if (common.isZone()) {
				status = Const.zoned;
				transactionsBeans.setZoned_time(DateUtil.getTimeBySecondChange(7200));
				appuserService.updateZoningTransaction(transactionsBeans, Const.zoned);
				cacheService.updateBananaFromCacheByid(transactionsBeans.getBanana_id(), 1);
				// push zooed message success

			} else {
				// push zooing message faild zoning ignored by Sharesby
				status = Const.Ignored;
				cacheService.removeTransactionsFromCache(id);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rs.setStatus(status);

		return rs;

	}

	/**
	 * 
	 * @param id
	 * @param common
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/transactions/{id}/threading" }, method = RequestMethod.POST)
	@ResponseBody

	public ResCommon threadingActive(@PathVariable String id, @RequestBody CommonRequst common,
			HttpServletResponse response) {
		// System.out.println("this threadingActive is ");
		ResCommon rs = null;
		UserEntity user = getUserFromCache();
		boolean isGetBy = true;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}
		if (common == null || !StringUtils.isNotEmpty(id)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return rs;
		}

		TransactionsBeans transactionsBeans = null;
		try {
			transactionsBeans = cacheService.getTransactionFromCache(id, "TransactionsLong");

			if (transactionsBeans == null) {
				response.setStatus(HttpServletResponse.SC_GONE);
				return rs;

			}

			int status = transactionsBeans.getStatus();

			if (transactionsBeans.getSharesby() == user.getId()) {
				isGetBy = false;
			}

			Threading threading = transactionsBeans.getThreading();

			if (threading == null) {
				threading = new Threading();

			}

			if (common.isAgree()) {
				// threading
				status = Const.threaded;
				appuserService.updateCommonTransaction(transactionsBeans, status, null);

				// push message threading success

			} else {
				int n = 0;
				if (isGetBy) {
					n = threading.getGetByTimes() + 1;
					if (n > 2) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						status = Const.Threading_limit;
						// push message
						return null;
					} else {
						threading.setGetByTimes(n);
					}

				} else {
					n = threading.getShareByTimes() + 1;

					if (n > 1) {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						status = Const.Threading_limit;
						// push message
						return null;
					} else {
						threading.setShareByTimes(n);
					}

				}
				threading.setPlace(common.getPlace());
				threading.setTime(common.getTime());
				status = Const.Threading_received;
				appuserService.updateCommonTransaction(transactionsBeans, status, threading);
				// push message thread request be received

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	@RequestMapping(value = { "/transactions/{id}/cancellation" }, method = RequestMethod.POST)
	@ResponseBody

	public ResCommon cancellActive(@PathVariable String id, @RequestBody CommonRequst common,
			HttpServletResponse response) {
		System.out.println("this cancellActive is ");
		ResCommon rs = null;
		UserEntity user = getUserFromCache();
		boolean isGetBy = true;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}
		if (common == null || !StringUtils.isNotEmpty(id)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return rs;
		}

		TransactionsBeans transactionsBeans = null;
		try {
			transactionsBeans = cacheService.getTransactionFromCache(id, "TransactionsLong");

			if (transactionsBeans == null) {
				response.setStatus(HttpServletResponse.SC_GONE);
				return rs;

			}

			if (transactionsBeans.getSharesby() == user.getId()) {
				isGetBy = false;
			}
			int cancle_reason = common.getCancel_reason();
			int status = transactionsBeans.getStatus();
			if (cancle_reason != 0) {
				if (transactionsBeans.getCancle_reason() != 0) {

					response.setStatus(HttpServletResponse.SC_CONFLICT);
					return rs;
				}
				transactionsBeans.setCancle_reason(cancle_reason);
				// transactionsBeans.setStatus(Const.Cancel_received);
				status = Const.Cancel_received;
				// appuserService.updateCommonTransaction(transactionsBeans,
				// Const.Cancel_received, null);

			} else {

				if (common.isAgree() && transactionsBeans.getCancle_reason() != 0) {
					status = Const.Closed;
					// appuserService.updateCommonTransaction(transactionsBeans,
					// Const.Closed, null);
				} else {
					// appuserService.updateCommonTransaction(transactionsBeans,
					// , null);
					status = Const.Dealbreaker;
				}

				cacheService.updateBananaFromCacheByid(transactionsBeans.getBanana_id(), 0);

			}
			appuserService.updateCommonTransaction(transactionsBeans, status, null);

			// common.getCancel_reason()

			///

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	/**
	 * 
	 * @param id
	 * @param common
	 * @param response
	 * @return
	 */

	@RequestMapping(value = { "/transactions/{id}/finish" }, method = RequestMethod.POST)
	@ResponseBody

	public ResCommon finishedActive(@PathVariable String id, @RequestBody CommonRequst common,
			HttpServletResponse response) {
		// System.out.println("this finishedActive is ");
		ResCommon rs = null;
		UserEntity user = getUserFromCache();
		boolean isGetBy = true;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}
		if (common == null || !StringUtils.isNotEmpty(id)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return rs;
		}

		TransactionsBeans transactionsBeans = null;
		try {
			transactionsBeans = cacheService.getTransactionFromCache(id, "TransactionsLong");

			if (transactionsBeans == null) {
				response.setStatus(HttpServletResponse.SC_GONE);
				return rs;

			}
			int status = transactionsBeans.getStatus();
			if (status == Const.Cancel_received) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return rs;
			}

			if (status == Const.Finshed_receive) {
				status = Const.Finshed;
				cacheService.removeBananaFromCacheByid((transactionsBeans.getBanana_id()));
			} else {
				status = Const.Finshed_receive;
			}

			if (transactionsBeans.getSharesby() == user.getId()) {
				isGetBy = false;
			}

			appuserService.updateCommonTransaction(transactionsBeans, status, null);

			// push message thread request be received

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	/**
	 * 7.1 Get transaction
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = { "/transactions" }, method = RequestMethod.GET)
	@ResponseBody

	public List<PageData> queryTransaction(HttpServletResponse response) {

		List<PageData> rs = null;
		// System.out.println(test);
		UserEntity user = getUserFromCache();
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}

		try {
			rs = appuserService.queryTransactionsListShareBy(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	/**
	 * 7.2 Get transaction details
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = { "/transactions/{id}" }, method = RequestMethod.GET)
	@ResponseBody

	public TransactionsBeans queryTransactionDetail(@PathVariable String id, HttpServletResponse response) {
		// System.out.println("this dsfsdf is " + id);
		TransactionsBeans rs = null;
		// System.out.println(test);
		UserEntity user = getUserFromCache();
		TransactionsBeans res = null;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return res;
		}

		try {
			res = appuserService.queryTransactionsDetail(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// Get active threadings

	// https://api.sosxsos.com/v1/threadings
	// 8.1 Get active threadings
	@RequestMapping(value = { "/threadings" }, method = RequestMethod.GET)
	@ResponseBody

	public TheardingRes getThreadings(HttpServletResponse response) {
		// System.out.println(test);
		UserEntity user = getUserFromCache();
		TheardingRes rs = null;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return rs;
		}

		try {
			rs = appuserService.queryTheardingsByUserID(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;

	}

	// Get active zoning requests

	// https://api.sosxsos.com/v1/zoning_requests
	// 8.2 Get active zoning requests
	@RequestMapping(value = { "/zoning_requests" }, method = RequestMethod.GET)
	@ResponseBody

	public Object getZoning_requests(HttpServletResponse response) {

		UserEntity user = getUserFromCache();
		// TheardingRes rs=null;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		return new ResBase() {
		};

	}

	// 9.1 Get user profile

	// https://api.sosxsos.com/v1/residents/#
	@RequestMapping(value = { "/residents" }, method = RequestMethod.GET)
	@ResponseBody
	public ResProfile getResidents(HttpServletResponse response) {

		UserEntity user = getUserFromCache();
		// TheardingRes rs=null;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		BananaEntity bananaEntity = cacheService.getBananaFromCache(user.getPhone());
		ResProfile rs = new ResProfile();
		rs.setStatus(user.getStatus());
		rs.setVerified_email(user.getVerified_email());
		rs.setVerified_id(user.isVerified_id());
		rs.setStatistics(user.getStatisticsEntity());
		if (bananaEntity != null) {
			rs.setBubble(bananaEntity.getBubble());

			rs.setMedia(bananaEntity.getMedia());

		}

		return rs;

	}

	// 9.2 Report abuse https://api.sosxsos.com/v1/reports
	// {
	// "type": int,
	// }
	@RequestMapping(value = { "/reports" }, method = RequestMethod.POST)
	@ResponseBody

	public Object makeReportsAbuse(HttpServletResponse response) {

		UserEntity user = getUserFromCache();
		// TheardingRes rs=null;
		// System.out.println(test);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		// String token = request.getHeader("Bearer");
		// user.setReported(user.getReported()+1);
		// cacheService.updateCacheUse(user, token);

		return new ResBase() {
		};

	}

}
