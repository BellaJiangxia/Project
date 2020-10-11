package com.vastsoft.yingtai.video.action;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.video.entity.FVideoCall;
import com.vastsoft.yingtai.video.service.VideoCallCacheService;

public class VideoAction extends BaseYingTaiAction {
	private FVideoCall videoCall;

	public String callVideoComm(){
		try {
			String chatRoom= DateTools.dateToString(new Date(), "ssSSS");
			System.out.println(chatRoom);
			videoCall.setChatRoom(chatRoom);
			videoCall=VideoCallCacheService.instance.callVideoUser(takePassport(), videoCall);
			addElementToData("videoCall", videoCall);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	public String queryVideoComm(){
		try {
			videoCall=VideoCallCacheService.instance.queryVideoCall(takePassport());
			addElementToData("videoCall", videoCall);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	public String recvVideoComm(){
		try {
			videoCall=VideoCallCacheService.instance.recvVideoCall(takePassport());
			addElementToData("videoCall", videoCall);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setVideoCall(FVideoCall videoCall) {
		this.videoCall = videoCall;
	}
	
	
}
