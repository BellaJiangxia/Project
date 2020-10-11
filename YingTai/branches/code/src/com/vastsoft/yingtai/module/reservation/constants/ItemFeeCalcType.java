package com.vastsoft.yingtai.module.reservation.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class ItemFeeCalcType extends SingleClassConstant {

	protected ItemFeeCalcType(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, ItemFeeCalcType> mapReservationStatus = new HashMap<Integer, ItemFeeCalcType>();

	public static final ItemFeeCalcType TOTAL = new ItemFeeCalcType(11, "总价累加");
	public static final ItemFeeCalcType EVEERY_ITEM = new ItemFeeCalcType(12, "各项累加");

	static {
		ItemFeeCalcType.mapReservationStatus.put(TOTAL.getCode(), TOTAL);
		ItemFeeCalcType.mapReservationStatus.put(EVEERY_ITEM.getCode(), EVEERY_ITEM);
	}

	public static ItemFeeCalcType parseCode(int iCode) {
		return ItemFeeCalcType.mapReservationStatus.get(iCode);
	}

	public static List<ItemFeeCalcType> getAll() {
		return new ArrayList<ItemFeeCalcType>(ItemFeeCalcType.mapReservationStatus.values());
	}
}
