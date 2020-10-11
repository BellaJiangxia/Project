package com.vastsoft.yingtaidicom.search.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class PatientDataExtraType extends SingleClassConstant {
	/**病人病历卡号*/
	public static final PatientDataExtraType PATIENT_CARD_NUM = new PatientDataExtraType(10, "病人病历卡号", "patient_card_num");
	/**病人病历卡办卡时间*/
	public static final PatientDataExtraType PATIENT_CARD_GOT_CARD_TIME = new PatientDataExtraType(10, "病人病历卡办卡时间",
			"patient_card_got_card_time");
	
	private static Map<Integer, PatientDataExtraType> mapPatientDataExtraType = new HashMap<Integer, PatientDataExtraType>();
	
	static{
		mapPatientDataExtraType.put(PATIENT_CARD_NUM.getCode(), PATIENT_CARD_NUM);
		mapPatientDataExtraType.put(PATIENT_CARD_GOT_CARD_TIME.getCode(), PATIENT_CARD_GOT_CARD_TIME);
	}
	
	protected PatientDataExtraType(int iCode, String strName,String key_name) {
		super(iCode, strName);
		this.key_name = key_name;
	}
	public String getKey_name() {
		return key_name;
	}
	private String key_name;

	public static List<PatientDataExtraType> getAll() {
		return new ArrayList<PatientDataExtraType>(mapPatientDataExtraType.values());
	}
}
