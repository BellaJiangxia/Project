package com.vastsoft.yingtai.module.msg.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class MsgStatus extends SingleClassConstant {

	protected MsgStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, MsgStatus> mapMsgStatus = new HashMap<Integer, MsgStatus>();

	public static final MsgStatus WAITING = new MsgStatus(1, "待发");
	public static final MsgStatus SUCCESS = new MsgStatus(2, "已发");
	public static final MsgStatus CANCELED = new MsgStatus(3, "已取消");
	/**用户拒收*/
	public static final MsgStatus USER_REJECT = new MsgStatus(40, "用户拒收");

	static {
		MsgStatus.mapMsgStatus.put(SUCCESS.getCode(), SUCCESS);
		MsgStatus.mapMsgStatus.put(WAITING.getCode(), WAITING);
		MsgStatus.mapMsgStatus.put(CANCELED.getCode(), CANCELED);
	}

	public static MsgStatus parseCode(int iCode) {
		return MsgStatus.mapMsgStatus.get(iCode);
	}

	public static List<MsgStatus> getAll() {
		return new ArrayList<MsgStatus>(MsgStatus.mapMsgStatus.values());
	}
}
