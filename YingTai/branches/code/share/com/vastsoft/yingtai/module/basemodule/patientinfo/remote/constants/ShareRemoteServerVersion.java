package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.AbstractRemoteDataHandler;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.interfase.RemoteServerController;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver1server.Ver1RemoteDataHandler;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver1server.Ver1RemoteServerController;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver2server.Ver2RemoteDataHandler;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.ver2server.Ver2RemoteServerController;

/** 远程服务系统的版本 */
public class ShareRemoteServerVersion extends SingleClassConstant {
	private String description;
	private Class<?> remoteServerController;
	private Class<?> remoteDataHandler;
	private Set<RemoteInterface> supportInterface;
	
	public static final ShareRemoteServerVersion V_1 = new ShareRemoteServerVersion(1, "第一代版本", Ver1RemoteServerController.class,
			Ver1RemoteDataHandler.class, "此版本支持影像号条件检索！");
	public static final ShareRemoteServerVersion V_2 = new ShareRemoteServerVersion(2, "第二代版本", Ver2RemoteServerController.class,
			Ver2RemoteDataHandler.class, "此版本支持影像号、病人身份证、等条件检索！");
	private static Map<Integer, ShareRemoteServerVersion> mapRemoteSearchServerVersion = new HashMap<Integer, ShareRemoteServerVersion>();

	static {
		mapRemoteSearchServerVersion.put(V_1.getCode(), V_1);
		V_1.addSuportInterface(RemoteInterface.QUERY_LAST_PASC_NUMS);
		V_1.addSuportInterface(RemoteInterface.QUERY_THUMBNAIL);
		V_1.addSuportInterface(RemoteInterface.SEARCH_PATIENT_INFO);
		mapRemoteSearchServerVersion.put(V_2.getCode(), V_2);
		V_2.addSuportInterface(RemoteInterface.QUERY_LAST_PASC_NUMS);
		V_2.addSuportInterface(RemoteInterface.SEARCH_PATIENT_INFO);
		V_2.addSuportInterface(RemoteInterface.QUERY_THUMBNAIL);//向下兼容未找到缩略图的图像
		V_2.addSuportInterface(RemoteInterface.QUERY_PARAM_VALUES_BY_PATIENT_NAME);
	}

	protected ShareRemoteServerVersion(int iCode, String strName, Class<?> remoteServerController,
			Class<?> remoteDataHandler, String description) {
		super(iCode, strName);
		this.remoteServerController = (remoteServerController);
		this.description = description;
		this.remoteDataHandler = (remoteDataHandler);
	}

	private void addSuportInterface(RemoteInterface ri) {
		if (supportInterface==null) 
			supportInterface = new HashSet<RemoteInterface>();
		supportInterface.add(ri);
	}

	public String getDescription() {
		return description;
	}
	public static List<ShareRemoteServerVersion> getAll() {
		return new ArrayList<ShareRemoteServerVersion>(mapRemoteSearchServerVersion.values());
	}

	public static ShareRemoteServerVersion parseCode(int code) {
		ShareRemoteServerVersion cs = mapRemoteSearchServerVersion.get(code);
		return cs == null ? V_1 : cs;
	}
	@JSON(serialize=false)
	public RemoteServerController getRemoteServerController() throws InstantiationException, IllegalAccessException {
		return (RemoteServerController) remoteServerController.newInstance();
	}
	@JSON(serialize=false)
	public AbstractRemoteDataHandler getRemoteDataHandler() throws InstantiationException, IllegalAccessException {
		return (AbstractRemoteDataHandler) remoteDataHandler.newInstance();
	}
	@JSON(serialize=false)
	public boolean isSupportInterface(RemoteInterface ri){
		return this.supportInterface.contains(ri);
	}
	@JSON(serialize=false)
	public List<RemoteInterface> getSupportInterface(){
		return new ArrayList<RemoteInterface>(this.supportInterface);
	}
}
