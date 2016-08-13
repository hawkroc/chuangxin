package com.fh.service.system.appuser;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.javassist.bytecode.annotation.IntegerMemberValue;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fh.controller.app.request.SignUpRequest;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.SignUpEntity;
import com.fh.util.PageData;

import sun.util.logging.resources.logging;


@Service("appuserService")
public class AppuserService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	//======================================================================================
	
	/*
	* 通过id获取数据
	*/
	public PageData findByUiId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUiId", pd);
	}
	/*
	* 通过loginname获取数据
	*/
	 @Cacheable(value="myCache", key="123456") 
	public PageData findByUId(PageData pd)throws Exception{
		
		return (PageData)dao.findForObject("AppuserMapper.findByUId", pd);
	}
	 
/**
 * 
 * @param p
 * @return
 * @throws Exception
 */
	 public boolean checkPhone(SignUpEntity p) throws Exception {
			
			boolean res=false;
			java.lang.Integer n=(Integer)dao.findForObject("WebappuserMapper.checkUser", p);
		 
			if(n.intValue()==0){
				res=true;
			}
			
			return res;
			
		}
	 

	 /*
		* 保存webapp用户
		*/
		public void saveAppUser(SignUpEntity p)throws Exception{
			dao.save("WebappuserMapper.saveU", p);
		}

	/*
	* 通过邮箱获取数据
	*/
	public PageData findByUE(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUE", pd);
	}
	
	/*
	* 通过编号获取数据
	*/
	public PageData findByUN(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUN", pd);
	}
	
	/*
	* 保存用户
	*/
	public void saveU(PageData pd)throws Exception{
		dao.save("AppuserMapper.saveU", pd);
	}
	/*
	* 修改用户
	*/
	public void editU(PageData pd)throws Exception{
		dao.update("AppuserMapper.editU", pd);
	}
	/*
	* 删除用户
	*/
	public void deleteU(PageData pd)throws Exception{
		dao.delete("AppuserMapper.deleteU", pd);
	}
	/*
	* 批量删除用户
	*/
	public void deleteAllU(String[] USER_IDS)throws Exception{
		dao.delete("AppuserMapper.deleteAllU", USER_IDS);
	}
	/*
	*用户列表(全部)
	*/
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.listAllUser", pd);
	}
	
	/*
	*用户列表(用户组)
	*/
	public List<PageData> listPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.userlistPage", page);
	}
	
	/*
	* 保存用户IP
	*/
	public void saveIP(PageData pd)throws Exception{
		dao.update("AppuserMapper.saveIP", pd);
	}
	
	/*
	* 登录判断
	*/
	public PageData getUserByNameAndPwd(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getUserInfo", pd);
	}
	/*
	* 跟新登录时间
	*/
	public void updateLastLogin(PageData pd)throws Exception{
		dao.update("AppuserMapper.updateLastLogin", pd);
	}
	//======================================================================================
	
	
}

