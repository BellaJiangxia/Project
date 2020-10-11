package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**远程系统接口*/
public final class RemoteInterface extends SingleClassConstant{
	/**查询病人数据*/
	public static final RemoteInterface SEARCH_PATIENT_INFO = new RemoteInterface(10, "查询病人数据");
	/**查询最近的新增影像号*/
	public static final RemoteInterface QUERY_LAST_PASC_NUMS = new RemoteInterface(20, "查询最近的新增影像号");
	/**查询指定影像的缩略图*/
	public static final RemoteInterface QUERY_THUMBNAIL = new RemoteInterface(30, "查询指定影像的缩略图");
	/**通过病人查询检索号*/
	public static final RemoteInterface QUERY_PARAM_VALUES_BY_PATIENT_NAME = new RemoteInterface(40, "通过病人查询检索号");
	
	private static Map<Integer, RemoteInterface> mapRemoteInterface=new LinkedHashMap<Integer, RemoteInterface>();
	
	static{
		mapRemoteInterface.put(SEARCH_PATIENT_INFO.getCode(), SEARCH_PATIENT_INFO);
		mapRemoteInterface.put(QUERY_LAST_PASC_NUMS.getCode(), QUERY_LAST_PASC_NUMS);
		mapRemoteInterface.put(QUERY_THUMBNAIL.getCode(), QUERY_THUMBNAIL);
		mapRemoteInterface.put(QUERY_PARAM_VALUES_BY_PATIENT_NAME.getCode(), QUERY_PARAM_VALUES_BY_PATIENT_NAME);
	}
	
	private RemoteInterface(int iCode, String strName) {
		super(iCode, strName);
	}
}
