package com.vastsoft.yingtaidicom.search.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**远程检索参数类型*/
public final class RemoteParamsType extends SingleClassConstant {
	private String param_name;
	
	/**病人身份证号*/
	public static final RemoteParamsType IDENTITY_ID=new RemoteParamsType(10, "身份证","identity_id");
	/**病人影像号,已过时，远诊服务已经不支持*/
	@Deprecated
	public static final RemoteParamsType EPS_NUM=new RemoteParamsType(20, "影像号（原病历号）","eps_num");
	/**住院号*/
	public static final RemoteParamsType ZHUYUAN_NUM=new RemoteParamsType(30, "住院号","zhuyuan_num");
	/**就诊卡号*/
	public static final RemoteParamsType DIAGNOSIS_CARD_NUM=new RemoteParamsType(40, "就诊卡号","his_card_num");
	/**病案号*/
	public static final RemoteParamsType BINGAN_NUM=new RemoteParamsType(45, "病案号","bingan_num");
	/**pasc影像号*/
	public static final RemoteParamsType PASC_NUM=new RemoteParamsType(50, "PASC号","pasc_num");
	/**机构编号*/
	public static final RemoteParamsType ORG_CODE = new RemoteParamsType(60, "机构编号","org_code");
	
	private static Map<Integer, RemoteParamsType> mapRPType=new LinkedHashMap<Integer,RemoteParamsType>();
	
	static{
		mapRPType.put(IDENTITY_ID.getCode(), IDENTITY_ID);
		mapRPType.put(EPS_NUM.getCode(), EPS_NUM);
		mapRPType.put(ZHUYUAN_NUM.getCode(), ZHUYUAN_NUM);
		mapRPType.put(DIAGNOSIS_CARD_NUM.getCode(), DIAGNOSIS_CARD_NUM);
		mapRPType.put(BINGAN_NUM.getCode(), BINGAN_NUM);
		mapRPType.put(PASC_NUM.getCode(), PASC_NUM);
		mapRPType.put(ORG_CODE.getCode(), ORG_CODE);
	}
	
	private RemoteParamsType(int iCode, String strName,String param_name) {
		super(iCode, strName);
		this.param_name=param_name;
	}

	public static RemoteParamsType parseCode(int code) {
		if (code == 20)
			return PASC_NUM;
		return mapRPType.get(code);
	}
	
	public static List<RemoteParamsType> getAll() {
		return new ArrayList<RemoteParamsType>(mapRPType.values());
	}

	public String getParam_name() {
		return param_name;
	}

	@Override
	public String toString() {
		return "RemoteParamsType [param_name=" + param_name + "]";
	}
}
