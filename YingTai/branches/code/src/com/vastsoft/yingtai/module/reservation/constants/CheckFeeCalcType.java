package com.vastsoft.yingtai.module.reservation.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class CheckFeeCalcType extends SingleClassConstant {

	protected CheckFeeCalcType(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, CheckFeeCalcType> mapReservationStatus = new HashMap<Integer, CheckFeeCalcType>();

	public static final CheckFeeCalcType ONCE = new CheckFeeCalcType(1, "按人次收费");
	public static final CheckFeeCalcType EVERY_PART = new CheckFeeCalcType(2, "按部位计费");
	public static final CheckFeeCalcType EVER_EXPOSURE = new CheckFeeCalcType(3, "按曝光次数收费");

	static {
		CheckFeeCalcType.mapReservationStatus.put(ONCE.getCode(), ONCE);
		CheckFeeCalcType.mapReservationStatus.put(EVERY_PART.getCode(), EVERY_PART);
		CheckFeeCalcType.mapReservationStatus.put(EVER_EXPOSURE.getCode(), EVER_EXPOSURE);
	}

	public static CheckFeeCalcType parseCode(int iCode) {
		return CheckFeeCalcType.mapReservationStatus.get(iCode);
	}

	public static List<CheckFeeCalcType> getAll() {
		return new ArrayList<CheckFeeCalcType>(CheckFeeCalcType.mapReservationStatus.values());
	}
}
