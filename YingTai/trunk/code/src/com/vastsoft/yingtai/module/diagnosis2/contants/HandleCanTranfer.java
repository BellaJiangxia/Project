package com.vastsoft.yingtai.module.diagnosis2.contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class HandleCanTranfer extends SingleClassConstant {

	protected HandleCanTranfer(int iCode, String strName) {
		super(iCode, strName);
	}
	private static final Map<Integer, HandleCanTranfer> mapHandleCanTranfer = new HashMap<Integer, HandleCanTranfer>();
	/** 可移交 */
	public static final HandleCanTranfer CAN_TRANFER = new HandleCanTranfer(1, "可移交");
	/** 不可移交 */
	public static final HandleCanTranfer NONE = new HandleCanTranfer(2, "不可移交");
	static {
		mapHandleCanTranfer.put(CAN_TRANFER.getCode(), CAN_TRANFER);
		mapHandleCanTranfer.put(NONE.getCode(), NONE);
	}

	public static HandleCanTranfer parseFrom(int iCode) {
		return HandleCanTranfer.mapHandleCanTranfer.get(iCode);
	}

	public static List<HandleCanTranfer> getAll() {
		return new ArrayList<HandleCanTranfer>(HandleCanTranfer.mapHandleCanTranfer.values());
	}
}
