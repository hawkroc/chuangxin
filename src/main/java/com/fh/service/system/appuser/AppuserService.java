package com.fh.service.system.appuser;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
//import org.apache.ibatis.javassist.bytecode.annotation.IntegerMemberValue;
import org.springframework.stereotype.Service;

import com.fh.controller.app.response.AddBananaRes;
import com.fh.controller.app.response.CheckThoughtRes;
import com.fh.controller.app.response.LoginResponse;
import com.fh.controller.app.response.Resident;
import com.fh.dao.DaoSupport;
import com.fh.entity.BananaEntity;
import com.fh.entity.BubbleEntity;
import com.fh.entity.LocationEntity;
import com.fh.entity.LocationRangeEntity;
import com.fh.entity.LoginEntity;
import com.fh.entity.MediaEntity;
import com.fh.entity.Page;
import com.fh.entity.ProductEntity;
import com.fh.entity.SignUpEntity;
import com.fh.util.CacheUtil;
import com.fh.util.LatLonUtil;
import com.fh.util.MD5;
import com.fh.util.PageData;

import net.sf.ehcache.Element;

@Service("appuserService")
public class AppuserService {
	// private static Cache cache =
	// CacheManager.getInstance().getCache("myCache");

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	// ======================================================================================

	/*
	 * 通过id获取数据
	 */
	public PageData findByUiId(PageData pd) throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.findByUiId", pd);
	}

	/*
	 * 通过loginname获取数据
	 */

	public PageData findByUId(PageData pd) throws Exception {

		return (PageData) dao.findForObject("AppuserMapper.findByUId", pd);
	}

	/**
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public Integer checkPhone(String phone) throws Exception {

		java.lang.Integer res = (Integer) dao.findForObject("WebappuserMapper.checkUser", phone);
		return res;

	}

	/**
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public LoginResponse updateLoginAppUser(LoginEntity e) throws Exception {
		LoginResponse loginResponse = null;

		if (StringUtils.isNotEmpty(e.getPassword()) && StringUtils.isNotEmpty(e.getPhone())) {
             Object temp= dao.findForObject("WebappuserMapper.login", e);
             if(temp==null){
            	 return loginResponse;
             }
			Integer id = (int) temp;

			if (id != null) {
				loginResponse = new LoginResponse();
				// dao.update("WebappuserMapper.saveLocation", e);
				loginResponse.setUser_token(getUserTokenAndPutCache(e.getPhone()));
			}

		} else if (StringUtils.isNotEmpty(e.getUser_token())) {
			String phone = getPhoneByTokenFromCache(e.getUser_token());
			if (phone != null) {
				e.setPhone(phone);
				loginResponse = new LoginResponse();
				loginResponse = (LoginResponse) dao.findForObject("WebappuserMapper.loginByToken", phone);
				// dao.update("WebappuserMapper.saveLocation", e);
			}

		}

		return loginResponse;

	}

	public void udateUserLocation(LocationEntity e) throws Exception {

		dao.update("WebappuserMapper.saveLocation", e);
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public String getPhoneByTokenFromCache(String token) {
		Element o = CacheUtil.getCacheObject(token, "userCache");
		String phone = null;
		if (o != null) {
			phone = (String) o.getObjectValue();
		}
		return phone;
	}

	/**
	 * 
	 * @param phone
	 * @return
	 */
	private String getUserTokenAndPutCache(String phone) {
		String token = MD5.md5((phone + System.currentTimeMillis()));
		CacheUtil.cacheSave(token, phone, "userCache");
		return token;

	}

	private void removeUserCache(String token) {
		CacheUtil.removeCache(token, "userCache");
	}

	@SuppressWarnings("unchecked")
	public List<Resident> getResidentList(double latitude,double longitude,double accuracy) throws Exception {

		List<Resident> residents = null;
		
			LocationRangeEntity l = LatLonUtil.getInstance().getDefaultAround(latitude, longitude);
			residents = (List<Resident>) dao.findForList("WebappuserMapper.searchResident", l);
			if (residents != null) {
				for (Resident resident : residents) {
					 resident.setBanana(getBananaFromCache(resident.getPhone()));
				}
			}

		return residents;

	}

	// need to be done
	public CheckThoughtRes checkBubbles(String topic ,String keyword) throws Exception {

		CheckThoughtRes rs = null;
		
			// r.getThought_idthougth();
			rs = new CheckThoughtRes();
			// r.getThought_idthougth();
			Element o = CacheUtil.getCacheObject(topic + keyword, "topickeywords_banana");
			if (o != null) {
				BananaEntity b = getBananaFromCache((String) o.getObjectValue());
				// rs.setImage_url(thoughtEntity.getVedio_url());
				// rs.setVideo_url(thoughtEntity.getImage_url());
				
				rs.setVideo_url(b.getBubble().getVideo_url());
		}

		return rs;

	}

	/**
	 * 
	 * @param phone
	 * @return
	 */
	private BananaEntity getBananaFromCache(String phone) {
		Element o = CacheUtil.getCacheObject(phone, "myThought");
		BananaEntity rs = null;
		if (o != null) {
			rs = (BananaEntity) o.getObjectValue();
		}
		return rs;
	}

	// add throuts

	public AddBananaRes saveBanana(BananaEntity banana, String token,String Imagepath,String Videopath) throws Exception {

		// List<Resident> residents = null;
		// if (getPhoneByTokenFromCache(r.getUser_token()) != null) {
		// LocationRangeEntity l =
		// LatLonUtil.getInstance().getDefaultAround(r.getGeo_lat(),
		// r.getGet_lng());
		// residents = (List<Resident>)
		// dao.findForObject("WebappuserMapper.searchResident", l);
		// }
		AddBananaRes residents = new AddBananaRes();
		String phone = getPhoneByTokenFromCache(token);
		//for test
	//	phone="334455";
		//phone="123456";
		
		if (phone == null) {
			return residents;
		}
		int userid = checkPhone(phone);

		banana.setUserid(userid);
		System.out.println(banana.toString());
		BubbleEntity t = banana.getBubble();
		
		t.setUserid(userid);
		t.setImage_url(Imagepath);
		t.setVideo_url(Videopath);
		System.out.println(t.toString());
		ProductEntity product = banana.getProduct();
		System.out.println(product.toString());
		product.setName(product.getItem_info().getName());
		product.setPrice(product.getItem_info().getPrice());
		dao.save("WebappuserMapper.saveThought", t);
		dao.save("WebappuserMapper.saveProduct", product);
		banana.setProductId(product.getId());
		banana.setThoughtId(t.getId());
		
		dao.save("WebappuserMapper.saveBanana", banana);
		MediaEntity mediaEntity=new MediaEntity();
		mediaEntity.setImage_url(Imagepath);
		mediaEntity.setVideo_url(Videopath);
		banana.setMedia((mediaEntity));
		CacheUtil.cacheSave(phone, banana, "myThought");
		CacheUtil.cacheSave(t.getTopic() + t.getKey_word(), phone, "topickeywords_banana");
		// residents.setStatus(0);
		// residents.setThought_id(t.getId());
		residents.setBanana_id(banana.getId());
		residents.setVideo_url(Videopath);
		residents.setImage_url(Imagepath);
		return residents;

	}

	/**
	 * 
	 * @param e
	 */
	public void updateLogout(String token) throws Exception {

		dao.update("WebappuserMapper.logout", getPhoneByTokenFromCache(token));
		CacheUtil.removeCache(token, "userCache");
	}

	/*
	 * 保存webapp用户
	 */
	public String saveAppUser(SignUpEntity p) throws Exception {
		dao.save("WebappuserMapper.saveU", p);
		return this.getUserTokenAndPutCache(p.getPhone());
	}

	public String updateAppUserPassword(SignUpEntity p) throws Exception {
		dao.save("WebappuserMapper.updatePassword", p);
		return this.getUserTokenAndPutCache(p.getPhone());
	}

	/*
	 * 通过邮箱获取数据
	 */
	public PageData findByUE(PageData pd) throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.findByUE", pd);
	}

	/*
	 * 通过编号获取数据
	 */
	public PageData findByUN(PageData pd) throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.findByUN", pd);
	}

	/*
	 * 保存用户
	 */
	public void saveU(PageData pd) throws Exception {
		dao.save("AppuserMapper.saveU", pd);
	}

	/*
	 * 修改用户
	 */
	public void editU(PageData pd) throws Exception {
		dao.update("AppuserMapper.editU", pd);
	}

	/*
	 * 删除用户
	 */
	public void deleteU(PageData pd) throws Exception {
		dao.delete("AppuserMapper.deleteU", pd);
	}

	/*
	 * 批量删除用户
	 */
	public void deleteAllU(String[] USER_IDS) throws Exception {
		dao.delete("AppuserMapper.deleteAllU", USER_IDS);
	}

	/*
	 * 用户列表(全部)
	 */
	public List<PageData> listAllUser(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("AppuserMapper.listAllUser", pd);
	}

	/*
	 * 用户列表(用户组)
	 */
	public List<PageData> listPdPageUser(Page page) throws Exception {
		return (List<PageData>) dao.findForList("AppuserMapper.userlistPage", page);
	}

	/*
	 * 保存用户IP
	 */
	public void saveIP(PageData pd) throws Exception {
		dao.update("AppuserMapper.saveIP", pd);
	}

	/*
	 * 登录判断
	 */
	public PageData getUserByNameAndPwd(PageData pd) throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.getUserInfo", pd);
	}

	/*
	 * 跟新登录时间
	 */
	public void updateLastLogin(PageData pd) throws Exception {
		dao.update("AppuserMapper.updateLastLogin", pd);
	}
	// ======================================================================================

}
