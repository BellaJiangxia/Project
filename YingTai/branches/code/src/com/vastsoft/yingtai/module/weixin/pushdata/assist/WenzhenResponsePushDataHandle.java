package com.vastsoft.yingtai.module.weixin.pushdata.assist;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.SysConfig;
import com.vastsoft.yingtai.module.weixin.pushdata.exception.PushDataParamException;
import com.vastsoft.yingtai.module.weixin.pushdata.interfase.PushDataTask;

public class WenzhenResponsePushDataHandle extends PushDataTask {
	private String weixin_union_id;
	private long case_id;
	private String msg_content;
	
	public WenzhenResponsePushDataHandle(String weixin_union_id,long case_id,String msg_content) throws PushDataParamException{
		super();
		this.weixin_union_id=weixin_union_id;
		this.case_id=case_id;
		this.msg_content=msg_content;
		if (this.weixin_union_id==null||this.weixin_union_id.trim().isEmpty())
			throw new PushDataParamException("weixin_union_id","微信ID必须指定！");
		if (this.case_id<=0)
			throw new PushDataParamException("case_id","病例ID必须指定！");
		if (this.msg_content==null||this.msg_content.trim().isEmpty())
			throw new PushDataParamException("msg_content","回复内容文本必须指定！");
	}
	@Override
	protected NetInfo getNetInfo() throws BaseException {
		try {
			StringBuilder param=new StringBuilder();
			param.append("weixin_union_id=").append(weixin_union_id).append('&');
			param.append("case_id=").append(case_id).append('&').append("msg_content=").append(msg_content);
			return new NetInfo(SysConfig.instance.getTuisong_wenzhenresponse_url(), param.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	protected void onReponseEvent(String jsonStr) {
		
	}
	@Override
	protected void onPushDataFailed(NetInfo netInfo) {
		
	}

}
