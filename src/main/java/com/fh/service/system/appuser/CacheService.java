package com.fh.service.system.appuser;

import org.springframework.stereotype.Service;

import com.fh.entity.BananaEntity;
import com.fh.entity.TransactionsBeans;
import com.fh.util.CacheUtil;

import net.sf.ehcache.Element;

@Service("cacheService")
public class CacheService {
	// userCache
	// userCacheEntity
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
	// TransactionsLong
	// UserBanana
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
