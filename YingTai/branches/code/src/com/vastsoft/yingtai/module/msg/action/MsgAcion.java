package com.vastsoft.yingtai.module.msg.action;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.msg.constants.MsgStatus;
import com.vastsoft.yingtai.module.msg.entity.FMessage;
import com.vastsoft.yingtai.module.msg.service.MsgService;

public class MsgAcion extends BaseYingTaiAction {
	private SplitPageUtil spu;
	private MsgStatus msg_status;

	public String searchMessage(){
		try {
			List<FMessage> listMessage=MsgService.instance.searchMsg(takePassport(),msg_status, super.getStart(), super.getEnd(), spu);
			addElementToData("listMessage", listMessage);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setMsg_status(int msg_status) {
		this.msg_status = MsgStatus.parseCode(msg_status);
	}
	
	
}
