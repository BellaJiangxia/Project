package com.vastsoft.yingtai.module.legitimate.service;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class LegitimateService {
	public static final LegitimateService instance = new LegitimateService();

	private Map<String, CacheStudyUIDs> mapCacheLegitimateStudyUIDs = new Hashtable<String, CacheStudyUIDs>();
	private static final int CacheTimeOut = 45;// 缓存超时的时间，单位秒钟

	private LegitimateService() {
	}

	public void verifyLegitimateForViewImage(String[] study_uids, String token) throws IllegalAccessException {
		CacheStudyUIDs cacheStudyUIDs = this.mapCacheLegitimateStudyUIDs.get(token);
		if (cacheStudyUIDs == null)
			throw new IllegalAccessException("指定的令牌无效或已过期，令牌只有" + CacheTimeOut + "秒有效时间！token：" + token);
		if (DateTools.timesBetweenAsSecond(new Date(), cacheStudyUIDs.create_time) >= CacheTimeOut){
			this.mapCacheLegitimateStudyUIDs.remove(token);
			throw new IllegalAccessException("指定的令牌已经过期，令牌只有" + CacheTimeOut + "秒有效时间！");
		}
		if (!ArrayTools.equals(study_uids, cacheStudyUIDs.study_uids))
			throw new IllegalAccessException("令牌包含的studyUIDs与查看的不符！");
		this.mapCacheLegitimateStudyUIDs.remove(token);
	}

	public String requestViewImage(Passport passport, String[] study_uids) throws IllegalAccessException {
		if (ArrayTools.isEmpty(study_uids))
			throw new IllegalAccessException("请至少指定一个study_uid！");
		// 检查是否可合法查看
		if (!this.checkLegitimateForRequestViewImage(passport, study_uids))
			throw new IllegalAccessException("您目前没有权利查看此影像！");
		String token = StringTools.getUUID();
		this.mapCacheLegitimateStudyUIDs.put(token, new CacheStudyUIDs(study_uids));
		this.checkAndRemoveTimeOutItem();
		return token;
	}

	private void checkAndRemoveTimeOutItem() {
		if (CommonTools.isEmpty(this.mapCacheLegitimateStudyUIDs))
			return;
		for (Iterator<String> ites =this.mapCacheLegitimateStudyUIDs.keySet().iterator();ites.hasNext();) {
			String token = ites.next();
			CacheStudyUIDs cacheStudyUIDs = this.mapCacheLegitimateStudyUIDs.get(token);
			if (cacheStudyUIDs != null
					&& DateTools.timesBetweenAsSecond(new Date(), cacheStudyUIDs.create_time) < CacheTimeOut)
				continue;
			this.mapCacheLegitimateStudyUIDs.remove(token);
		}
	}

	/** 检查用户是否可合法查看指定的study */
	private boolean checkLegitimateForRequestViewImage(Passport passport, String[] study_uids) {
		return true;
	}

	private class CacheStudyUIDs {
		private String[] study_uids;
		private Date create_time;

		private CacheStudyUIDs(String[] study_uids) {
			this.create_time = new Date();
			this.study_uids = study_uids;
		}
	}
}
