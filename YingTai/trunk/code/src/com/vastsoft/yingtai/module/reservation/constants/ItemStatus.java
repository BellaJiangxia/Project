package com.vastsoft.yingtai.module.reservation.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class ItemStatus extends SingleClassConstant {

	protected ItemStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, ItemStatus> mapReservationStatus = new HashMap<Integer, ItemStatus>();

	public static final ItemStatus VALIDATE = new ItemStatus(1, "有效");
	public static final ItemStatus INVALIDATE = new ItemStatus(0, "无效");

	static {
		ItemStatus.mapReservationStatus.put(VALIDATE.getCode(), VALIDATE);
		ItemStatus.mapReservationStatus.put(INVALIDATE.getCode(), INVALIDATE);
	}

	public static ItemStatus parseCode(int iCode) {
		return ItemStatus.mapReservationStatus.get(iCode);
	}

	public static List<ItemStatus> getAll() {
		return new ArrayList<ItemStatus>(ItemStatus.mapReservationStatus.values());
	}
}
