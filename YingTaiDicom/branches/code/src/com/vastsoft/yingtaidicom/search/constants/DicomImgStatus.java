package com.vastsoft.yingtaidicom.search.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public final class DicomImgStatus extends SingleClassConstant {
	private static final Map<Integer, DicomImgStatus> mapMedicalStatus = new HashMap<Integer, DicomImgStatus>();
	/**正常*/
	public static final DicomImgStatus NORMAL = new DicomImgStatus(1, "正常");// 可以修改
	/**已提交诊断*/
	public static final DicomImgStatus COMMITED = new DicomImgStatus(2, "已提交诊断");//
	/**已诊断*/
	public static final DicomImgStatus DIAGNOSISED = new DicomImgStatus(3, "已诊断");

	static {
		DicomImgStatus.mapMedicalStatus.put(NORMAL.getCode(), NORMAL);
		DicomImgStatus.mapMedicalStatus.put(COMMITED.getCode(), COMMITED);
		DicomImgStatus.mapMedicalStatus.put(DIAGNOSISED.getCode(), DIAGNOSISED);
	}

	protected DicomImgStatus(int iCode, String strName) {
		super(iCode, strName);
	}

	public static DicomImgStatus parseCode(int iCode) {
		return DicomImgStatus.mapMedicalStatus.get(iCode);
	}

	public static List<DicomImgStatus> getAll() {
		return new ArrayList<DicomImgStatus>(mapMedicalStatus.values());
	}

}
