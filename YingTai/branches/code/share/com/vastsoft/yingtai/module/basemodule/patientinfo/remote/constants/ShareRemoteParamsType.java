package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**远程检索参数类型*/
public final class ShareRemoteParamsType extends SingleClassConstant {
	private String param_name;
	
	/**病人身份证号*/
	public static final ShareRemoteParamsType IDENTITY_ID=new ShareRemoteParamsType(10, "身份证","identity_id");
	/**病人影像号  已过时，请使用 PASC_NUM*/
	@Deprecated
	public static final ShareRemoteParamsType EPS_NUM=new ShareRemoteParamsType(20, "影像号（原病历号）","eps_num");
	/**住院号*/
	public static final ShareRemoteParamsType ZHUYUAN_NUM=new ShareRemoteParamsType(30, "住院号","zhuyuan_num");
	/**就诊卡号*/
	public static final ShareRemoteParamsType DIAGNOSIS_CARD_NUM=new ShareRemoteParamsType(40, "就诊卡号","his_card_num");
	/**病案号*/
	public static final ShareRemoteParamsType BINGAN_NUM=new ShareRemoteParamsType(45, "病案号","bingan_num");
	/**pasc影像号*/
	public static final ShareRemoteParamsType PASC_NUM=new ShareRemoteParamsType(50, "PASC号（原病历号）","pasc_num");
	/**病人姓名*/
	public static final ShareRemoteParamsType PATIENT_NAME=new ShareRemoteParamsType(55, "病人姓名","patient_name");
	/**机构编号*/
	public static final ShareRemoteParamsType ORG_CODE = new ShareRemoteParamsType(60, "机构编号","org_code");
	/**机构AE号*/
	@Deprecated
	public static final ShareRemoteParamsType ORG_AE_NUM=new ShareRemoteParamsType(99, "机构AE号","org_ae_num");
//	/***/
//	public static final RPType IDENTITY_ID=new RPType(10, "身份证");

	
	private static Map<Integer, ShareRemoteParamsType> mapRPType=new LinkedHashMap<Integer,ShareRemoteParamsType>();
	
	static{
		mapRPType.put(IDENTITY_ID.getCode(), IDENTITY_ID);
		mapRPType.put(EPS_NUM.getCode(), EPS_NUM);
		mapRPType.put(PASC_NUM.getCode(), PASC_NUM);
		mapRPType.put(ZHUYUAN_NUM.getCode(), ZHUYUAN_NUM);
		mapRPType.put(DIAGNOSIS_CARD_NUM.getCode(), DIAGNOSIS_CARD_NUM);
		mapRPType.put(BINGAN_NUM.getCode(), BINGAN_NUM);
		mapRPType.put(PATIENT_NAME.getCode(), PATIENT_NAME);
		mapRPType.put(ORG_CODE.getCode(), ORG_CODE);
		mapRPType.put(ORG_AE_NUM.getCode(), ORG_AE_NUM);
	}
	
	protected ShareRemoteParamsType(int iCode, String strName,String param_name) {
		super(iCode, strName);
		this.param_name=param_name;
	}

	public static ShareRemoteParamsType parseCode(int code) {
		return mapRPType.get(code);
	}

	public static List<ShareRemoteParamsType> getAll() {
		List<ShareRemoteParamsType> result=new ArrayList<ShareRemoteParamsType>();
		result.add(PASC_NUM);
		result.add(EPS_NUM);
		result.add(IDENTITY_ID);
		result.add(ZHUYUAN_NUM);
		result.add(DIAGNOSIS_CARD_NUM);
		result.add(BINGAN_NUM);
		result.add(PATIENT_NAME);
		return result;
	}

	public String getParam_name() {
		return param_name;
	}
}
