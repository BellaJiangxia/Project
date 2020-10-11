package com.vastsoft.yingtai.module.reservation.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class ReservationStatus extends SingleClassConstant {

	protected ReservationStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	private static final Map<Integer, ReservationStatus> mapReservationStatus = new HashMap<Integer, ReservationStatus>();

	public static final ReservationStatus UNRESERVE = new ReservationStatus(1, "未预约");
	public static final ReservationStatus RESERVED = new ReservationStatus(2, "已预约");
	public static final ReservationStatus AUDITED = new ReservationStatus(3, "已审核");
	public static final ReservationStatus ACCEPTED = new ReservationStatus(4, "已受理");
	public static final ReservationStatus UNCHECK = new ReservationStatus(5, "未检查");
	public static final ReservationStatus CHECKED = new ReservationStatus(6, "已检查");
	public static final ReservationStatus INVALIDATE = new ReservationStatus(0, "无效");

	public static final ReservationStatus REJECTED = new ReservationStatus(99, "审核未通过");

	static {
		ReservationStatus.mapReservationStatus.put(RESERVED.getCode(), RESERVED);
		ReservationStatus.mapReservationStatus.put(UNRESERVE.getCode(), UNRESERVE);
		ReservationStatus.mapReservationStatus.put(AUDITED.getCode(), AUDITED);
		ReservationStatus.mapReservationStatus.put(ACCEPTED.getCode(), ACCEPTED);
		ReservationStatus.mapReservationStatus.put(UNCHECK.getCode(), UNCHECK);
		ReservationStatus.mapReservationStatus.put(CHECKED.getCode(), CHECKED);
		ReservationStatus.mapReservationStatus.put(INVALIDATE.getCode(), INVALIDATE);
		ReservationStatus.mapReservationStatus.put(REJECTED.getCode(), REJECTED);
	}

	public static ReservationStatus parseCode(int iCode) {
		return ReservationStatus.mapReservationStatus.get(iCode);
	}

	public static List<ReservationStatus> getAll() {
		return new ArrayList<ReservationStatus>(ReservationStatus.mapReservationStatus.values());
	}
}
