package com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;
/**诊断阴阳性*/
public final class ReportFomType extends SingleClassConstant {
	/**阴性*/
	public static final ReportFomType NEGATIVE=new ReportFomType(1, "阴性");
	/**阳性*/
	public static final ReportFomType POSITIVE=new ReportFomType(2, "阳性");
	private static Map<Integer, ReportFomType> mapDiagnosisFomType=new HashMap<Integer,ReportFomType>();
	static{
		mapDiagnosisFomType.put(NEGATIVE.getCode(), NEGATIVE);
		mapDiagnosisFomType.put(POSITIVE.getCode(), POSITIVE);
	}
	protected ReportFomType(int iCode, String strName) {
		super(iCode, strName);
	}
	public static ReportFomType parseCode(int iCode) {
		ReportFomType dft=ReportFomType.mapDiagnosisFomType.get(iCode);
		return dft==null?ReportFomType.NEGATIVE:dft;
	}

	public static List<ReportFomType> getAll() {
		return new ArrayList<ReportFomType>(ReportFomType.mapDiagnosisFomType.values());
	}

}
