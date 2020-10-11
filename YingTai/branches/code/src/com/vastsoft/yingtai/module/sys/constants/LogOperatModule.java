package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class LogOperatModule extends SingleClassConstant {
	private String inf_name;
	protected LogOperatModule(int iCode, String strName,String strInfName) {
		super(iCode, strName);
		this.inf_name=strInfName;
	}

	private static final Map<Integer, LogOperatModule> mapLogOperatModule = new HashMap<Integer, LogOperatModule>();
	/** 诊断模块 */
	public static final LogOperatModule DIAGNOSIS = new LogOperatModule(1, "诊断模块","com.vastsoft.yingtai.module.diagnosis.action.DiagnosisAction");
	/** 结算模块 */
	public static final LogOperatModule FINANCE = new LogOperatModule(2, "结算模块","com.vastsoft.yingtai.module.financel.action.FinanceAction");
	/** 病例模块 */
	public static final LogOperatModule MEDICAL_HIS = new LogOperatModule(3, "病例模块","com.vastsoft.yingtai.module.medical.action.MedicalHisAction");
	/** 公共模块 */
	public static final LogOperatModule MSG = new LogOperatModule(4, "公共模块","com.vastsoft.yingtai.action.CommonAction");
	/** 机构模块 */
	public static final LogOperatModule ORG = new LogOperatModule(5, "机构模块","com.vastsoft.yingtai.module.org.action.OrgAction");
	/** 统计模块 */
	public static final LogOperatModule STAT = new LogOperatModule(6, "统计模块","com.vastsoft.yingtai.module.stat.action.StatAction");
	/** 文件模块 */
	public static final LogOperatModule FILE = new LogOperatModule(7, "文件模块","com.vastsoft.yingtai.module.sys.action.FileAction");
	/** 日志模块 */
	public static final LogOperatModule LOG = new LogOperatModule(8, "日志模块","com.vastsoft.yingtai.module.sys.action.LogAction");
	/** 系统模块 */
	public static final LogOperatModule SYS = new LogOperatModule(9, "系统模块","com.vastsoft.yingtai.module.sys.action.SysAction");
	/** 用户模块 */
	public static final LogOperatModule USER = new LogOperatModule(10, "用户模块","com.vastsoft.yingtai.module.user.action.UserAction");
	
	static {
		LogOperatModule.mapLogOperatModule.put(DIAGNOSIS.getCode(), DIAGNOSIS);
		LogOperatModule.mapLogOperatModule.put(FINANCE.getCode(), FINANCE);
		LogOperatModule.mapLogOperatModule.put(MEDICAL_HIS.getCode(), MEDICAL_HIS);
		LogOperatModule.mapLogOperatModule.put(MSG.getCode(), MSG);
		LogOperatModule.mapLogOperatModule.put(ORG.getCode(), ORG);
		LogOperatModule.mapLogOperatModule.put(STAT.getCode(), STAT);
		LogOperatModule.mapLogOperatModule.put(FILE.getCode(), FILE);
		LogOperatModule.mapLogOperatModule.put(LOG.getCode(), LOG);
		LogOperatModule.mapLogOperatModule.put(SYS.getCode(), SYS);
		LogOperatModule.mapLogOperatModule.put(USER.getCode(), USER);
	}
	
	public static LogOperatModule parseCode(int iCode) {
		return LogOperatModule.mapLogOperatModule.get(iCode);
	}

	public static List<LogOperatModule> getAll() {
		return new ArrayList<LogOperatModule>(LogOperatModule.mapLogOperatModule.values());
	}

	public static LogOperatModule parseInfName(String strInfName){
		if (strInfName==null||strInfName.trim().isEmpty())return null;
		strInfName=strInfName.trim();
		List<LogOperatModule> listModule=getAll();
		for (LogOperatModule logOperatModule : listModule) {
			if (strInfName.contains(logOperatModule.getInf_name()))
				return logOperatModule;
		}
		return null;
	}
	
	public String getInf_name() {
		return inf_name;
	}
}
