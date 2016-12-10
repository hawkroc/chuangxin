package com.fh.service.system.appuser;

import org.springframework.stereotype.Service;

import com.fh.entity.BananaEntity;
import com.fh.entity.TransactionsBeans;
import com.fh.entity.UserEntity;
import com.fh.util.CacheUtil;

import net.sf.ehcache.Element;

@Service("cacheService")
public class CacheService {
	// userCache
	// userCacheEntity


	public void removeUserCache(String token,UserEntity u) {
		CacheUtil.removeCache(token, "userCache");
		CacheUtil.removeCache(token, "userCacheEntity");
		CacheUtil.removeCache(u.getId(), "userCacheEntityByid");
		
	}


	/**
	 * 
	 * @param token
	 * @return
	 */
	public UserEntity getUserByTokenFromCache(String token) {
		Element o = CacheUtil.getCacheObject(token, "userCacheEntity");
		UserEntity user = null;
		if (o != null) {
			user = (UserEntity) o.getObjectValue();
		}
		return user;
	}
	

	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public UserEntity getUserByTokenFromCache(int userid) {
		Element o = CacheUtil.getCacheObject(userid, "userCacheEntityByid");
		UserEntity user = null;
		if (o != null) {
			user = (UserEntity) o.getObjectValue();
		}
		return user;
	}
	
	
	
	// Transactions
	/**
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public TransactionsBeans saveAndupdateTransaction(TransactionsBeans t) throws Exception{
		
		//dao.save("WebappuserMapper.saveTransactions", t);
		CacheUtil.cacheSave(t.getId(), t, "Transactions");
		return t;
	}
	
	
	
private TransactionsBeans getTransactionFromCache(String id,String Cachename) throws Exception{
		
		//dao.save("WebappuserMapper.saveTransactions", t);
	Element o = CacheUtil.getCacheObject(id, Cachename);
	TransactionsBeans transactionsBeans=null;
	if(o!=null){
		transactionsBeans=(TransactionsBeans)o.getObjectValue();
	}
		return transactionsBeans;
	}
	
/**
 * 
 * @param id
 * @return
 * @throws Exception
 */
public TransactionsBeans getTransactionFromCacheById(String id) throws Exception{
	TransactionsBeans transactionsBeans=this.getTransactionFromCache(id, "Transactions");
	if(transactionsBeans==null){
		 transactionsBeans=this.getTransactionFromCache(id, "TransactionsLong");	
	}
     
	return transactionsBeans;
}

	
	// TransactionsLong
	// UserBanana


/**
 * 
 * @param phone
 * @return
 */
public BananaEntity getBananaFromCache(String phone) {
	Element o = CacheUtil.getCacheObject(phone, "UserBanana");
	BananaEntity rs = null;
	if (o != null) {
		rs = (BananaEntity) o.getObjectValue();
	}
	return rs;
}
	// UserBananaId
	public BananaEntity getBananaFromCacheById(long banana_id) {
		Element o = CacheUtil.getCacheObject(banana_id, "UserBananaId");
		String phone = null;

		BananaEntity rsult = null;
		if (o != null) {
			phone = (String) o.getObjectValue();
			Element rs = CacheUtil.getCacheObject(phone, "UserBanana");
			rsult = (BananaEntity) rs.getObjectValue();
		}

		return rsult;
	}

	// topickeywords_banana
}
