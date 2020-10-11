package com.vastsoft.yingtai.video.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.video.entity.FVideoCall;
import com.vastsoft.yingtai.video.excaption.VideoException;

public class VideoCallCacheService {
	public static final VideoCallCacheService instance=new VideoCallCacheService();
	private HashSet<FVideoCall> mapVideoCallCache=new HashSet<FVideoCall>();
	
	private FVideoCall findCall(long sendUserId,long recvUserId){
		for (Iterator<FVideoCall> iterator = mapVideoCallCache.iterator(); iterator.hasNext();) {
			FVideoCall fVideoCall = (FVideoCall) iterator.next();
			if (fVideoCall.getRecvUserId()!=recvUserId)
				continue;
			if (fVideoCall.getSendUserId()!=sendUserId)
				continue;
			return fVideoCall;
		}
		return null;
	}
	
	private List<FVideoCall> findCall(long recvUserId){
		List<FVideoCall> result=new ArrayList<>();
		for (Iterator<FVideoCall> iterator = mapVideoCallCache.iterator(); iterator.hasNext();) {
			FVideoCall fVideoCall = (FVideoCall) iterator.next();
			if (fVideoCall.getRecvUserId()==recvUserId)
				result.add(fVideoCall);
		}
		Collections.sort(result, new Comparator<FVideoCall>() {
			@Override
			public int compare(FVideoCall o1, FVideoCall o2) {
				return o1.getCreate_time().compareTo(o2.getCreate_time());
			}
		});
		return result;
	}
	
	public FVideoCall callVideoUser(Passport passport,FVideoCall videlCall) throws BaseException{
		try {
			if (videlCall==null)
				throw new VideoException("请指定要呼叫的人！");
			FVideoCall vc=findCall(videlCall.getSendUserId(), videlCall.getRecvUserId());
			if (vc==null) {
				mapVideoCallCache.add(videlCall);
				vc=videlCall;
			}else {
				vc.setChatRoom(videlCall.getChatRoom());
				vc.setCreate_time(new Date());
			}
			System.out.println(videlCall);
			return vc;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public FVideoCall queryVideoCall(Passport passport){
		List<FVideoCall> listCall=findCall(passport.getUserId());
		if (listCall==null||listCall.size()<=0)
			return null;
		while (true) {
			if (listCall.size()<=0)
				break;
			FVideoCall vc=listCall.get(0);
			if (vc==null)
				break;
			if (vc.getRecv_time()!=null) {
				if (System.currentTimeMillis()-vc.getRecv_time().getTime()>1000*30){
					mapVideoCallCache.remove(vc);
					listCall.remove(vc);
					continue;
				}
			}
			return vc;
		}
		return null;
	}
	
	public FVideoCall recvVideoCall(Passport passport){
		FVideoCall result=queryVideoCall(passport);
		if (result!=null){
			if (result.getRecv_time()==null){
				result.setRecv_time(new Date());
			}
		}
		return result;
	}
}
