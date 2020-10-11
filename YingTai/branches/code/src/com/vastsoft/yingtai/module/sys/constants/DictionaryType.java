package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.common.SingleClassConstant;

public final class DictionaryType extends SingleClassConstant {
	/** 支持的最大条数，-1为无限 */
	private int maxNum;
	/** 父类型 */
	private DictionaryType parent_type;
	/** 字典说明 */
	private String desc;
	/** 可操作类型 */
	private DictionaryFlag flag;
	/** 单位 */
	private String unit;
	
	private static final Map<Integer, DictionaryType> mapDictionaryType = new HashMap<Integer, DictionaryType>();
	
	private static final DictionaryType ROOT = new DictionaryType(999999, "字典主类型", null, 0, "字典主类型", DictionaryFlag.NULL_U_D, "");
	
	/** 设备类型 */
	public static final DictionaryType DEVICE_TYPE = new DictionaryType(1, "设备类型", ROOT, -1, "设备类型", DictionaryFlag.CAN_U_D,
			"");
	/** 部位类型 */
	public static final DictionaryType BODY_PART_TYPE = new DictionaryType(2, "部位类型", DEVICE_TYPE, -1, "部位类型", DictionaryFlag.CAN_U_NOT_D,
			"");
	/** 机构级别类型 */
	public static final DictionaryType ORG_LEVELS = new DictionaryType(3, "机构级别类型", ROOT, -1, "机构级别类型", DictionaryFlag.CAN_U_D,
			"");
	/** 医生级别类型 */
	public static final DictionaryType DOCTOR_LEVELS = new DictionaryType(4, "医生级别类型", ROOT, -1, "医生级别类型",
			DictionaryFlag.CAN_U_D, "");
	// /** 文件保存路径 */
	// public static final DictionaryType FILE_PATH = new DictionaryType(5,
	// "文件保存路径", 1);
	/** 系统诊断提成定额 */
	public static final DictionaryType SYS_DEDUCT_QUOTA = new DictionaryType(6, "系统诊断提成定额", ROOT, 1, "系统诊断提成定额",
			DictionaryFlag.CAN_U_NOT_D, "元");
	/** 找回密码验证码有效分钟数 */
	public static final DictionaryType CHECK_CODE_MINUTES = new DictionaryType(7, "找回密码验证码有效分钟数", ROOT, 1, "找回密码验证码有效分钟数",
			DictionaryFlag.CAN_U_NOT_D, "分钟");
	/** 允许诊断信息的最大条数 */
	public static final DictionaryType MAX_REPORT_MSG_COUNT = new DictionaryType(8, "允许诊断交谈的最大条数", ROOT, 1,
			"允许诊断交谈的最大条数，0为不限", DictionaryFlag.CAN_U_NOT_D, "条");
	/** 发送诊断提醒的扫描间隔分钟数 */
	public static final DictionaryType SEND_MSG_MINUTES = new DictionaryType(9, "发送诊断提醒的扫描间隔分钟数", ROOT, 1, "发送诊断提醒的扫描间隔分钟数",
			DictionaryFlag.CAN_U_NOT_D, "分钟");
	// /** 系统短信后缀 */
	// public static final DictionaryType MSG_PREFIX = new DictionaryType(10,
	// "系统短信后缀", 1);
	/** 机构类型（医院，医生集团/诊所等） */
	public static final DictionaryType ORG_TYPE = new DictionaryType(11, "机构类型", ROOT, -1, "机构类型", DictionaryFlag.CAN_U_D, "");
	/** 机构编码 */
	public static final DictionaryType ORG_CODE = new DictionaryType(12, "机构编码", ROOT, 1, "机构编码", DictionaryFlag.NULL_U_D,
			"");
	// /** 图像服务器应用URL*/
	// public static final DictionaryType DICOM_SERVER_URL=new
	// DictionaryType(13, "图像服务器应用URL", 1);

	public static final DictionaryType USER_LOGO_PATH = new DictionaryType(14, "用户LOGO文件路径", ROOT, 1, "用户LOGO文件路径",
			DictionaryFlag.CAN_U_NOT_D, "");

	public static final DictionaryType USER_SCAN_PATH = new DictionaryType(15, "用户证件文件路径", ROOT, 1, "用户证件文件路径",
			DictionaryFlag.CAN_U_NOT_D, "");

	public static final DictionaryType ORG_LOGO_PATH = new DictionaryType(16, "机构LOGO文件路径", ROOT, 1, "机构LOGO文件路径",
			DictionaryFlag.CAN_U_NOT_D, "");

	public static final DictionaryType ORG_SCAN_PATH = new DictionaryType(17, "机构证件文件路径", ROOT, 1, "机构证件文件路径",
			DictionaryFlag.CAN_U_NOT_D, "");
	/** 电子申请单保存位置 */
	public static final DictionaryType CASE_HIS_REQUEST_FORM = new DictionaryType(18, "病例电子申请单保存位置", ROOT, 1,
			"病例电子申请单保存位置", DictionaryFlag.CAN_U_NOT_D, "");
	/** 系统用户默认密码 */
	public static final DictionaryType DEFAULT_PASSWORD = new DictionaryType(19, "默认密码", ROOT, 1, "系统所有用户的默认密码",
			DictionaryFlag.CAN_U_NOT_D, "");
	/** 病例的检查项目 */
	public static final DictionaryType DICOM_IMG_CHECK_PRO = new DictionaryType(20, "检查项目", BODY_PART_TYPE, 0, "病例检查项目",
			DictionaryFlag.CAN_U_D, "");

	public static final DictionaryType DOC_SIGN_PATH = new DictionaryType(21, "医生签名图片路径", ROOT, 1, "医生签名图片路径",
			DictionaryFlag.CAN_U_NOT_D, "");

	public static final DictionaryType BBS_PATH = new DictionaryType(22, "bbs路径", ROOT, 1, "bbs路径",
			DictionaryFlag.CAN_U_NOT_D, "");

	public static final DictionaryType TEAM_LEVELS = new DictionaryType(23, "工作团队级别", ROOT, -1, "工作团队级别",
			DictionaryFlag.CAN_U_D, "");

	static {
		DictionaryType.mapDictionaryType.put(DEVICE_TYPE.getCode(), DEVICE_TYPE);
		DictionaryType.mapDictionaryType.put(BODY_PART_TYPE.getCode(), BODY_PART_TYPE);
		DictionaryType.mapDictionaryType.put(ORG_LEVELS.getCode(), ORG_LEVELS);
		DictionaryType.mapDictionaryType.put(DOCTOR_LEVELS.getCode(), DOCTOR_LEVELS);
		// DictionaryType.mapDictionaryType.put(FILE_PATH.getCode(), FILE_PATH);
		DictionaryType.mapDictionaryType.put(SYS_DEDUCT_QUOTA.getCode(), SYS_DEDUCT_QUOTA);
		DictionaryType.mapDictionaryType.put(CHECK_CODE_MINUTES.getCode(), CHECK_CODE_MINUTES);
		DictionaryType.mapDictionaryType.put(MAX_REPORT_MSG_COUNT.getCode(), MAX_REPORT_MSG_COUNT);
		DictionaryType.mapDictionaryType.put(SEND_MSG_MINUTES.getCode(), SEND_MSG_MINUTES);
		// DictionaryType.mapDictionaryType.put(MSG_PREFIX.getCode(),
		// MSG_PREFIX);
		DictionaryType.mapDictionaryType.put(ORG_TYPE.getCode(), ORG_TYPE);
		DictionaryType.mapDictionaryType.put(ORG_CODE.getCode(), ORG_CODE);
		// DictionaryType.mapDictionaryType.put(DICOM_SERVER_URL.getCode(),
		// DICOM_SERVER_URL);
		DictionaryType.mapDictionaryType.put(USER_LOGO_PATH.getCode(), USER_LOGO_PATH);
		DictionaryType.mapDictionaryType.put(USER_SCAN_PATH.getCode(), USER_SCAN_PATH);
		DictionaryType.mapDictionaryType.put(ORG_LOGO_PATH.getCode(), ORG_LOGO_PATH);
		DictionaryType.mapDictionaryType.put(ORG_SCAN_PATH.getCode(), ORG_SCAN_PATH);
		DictionaryType.mapDictionaryType.put(CASE_HIS_REQUEST_FORM.getCode(), CASE_HIS_REQUEST_FORM);
		DictionaryType.mapDictionaryType.put(DEFAULT_PASSWORD.getCode(), DEFAULT_PASSWORD);
		DictionaryType.mapDictionaryType.put(DICOM_IMG_CHECK_PRO.getCode(), DICOM_IMG_CHECK_PRO);
		DictionaryType.mapDictionaryType.put(DOC_SIGN_PATH.getCode(), DOC_SIGN_PATH);
		DictionaryType.mapDictionaryType.put(BBS_PATH.getCode(), BBS_PATH);
		DictionaryType.mapDictionaryType.put(TEAM_LEVELS.getCode(), TEAM_LEVELS);
	}

	DictionaryType(int iCode, String strName,DictionaryType parent_type, int iMaxNum, String strDesc, DictionaryFlag flag, String unit) {
		super(iCode, strName);
		this.parent_type = parent_type;
		this.maxNum = iMaxNum;
		this.desc = strDesc;
		this.flag = flag;
		this.unit = unit;
	}

	public static DictionaryType parseFrom(int iCode) {
		return mapDictionaryType.get(iCode);
	}

	public static List<DictionaryType> getAll() {
		return new ArrayList<DictionaryType>(mapDictionaryType.values());
	}

	// public static String takeSysConfigListOfString(){
	// List<DictionaryType> listDt=new ArrayList<DictionaryType>();
	// listDt.add(CHECK_CODE_MINUTES);
	// listDt.add(DEFAULT_PASSWORD);
	// listDt.add(DOCTOR_LEVELS);
	// listDt.add(FILE_PATH)
	// listDt.add(e)
	// return parseList(listDt);
	// }

	public static String parseList(List<DictionaryType> listDicType) {
		if (listDicType == null || listDicType.size() <= 0)
			return "";
		StringBuilder sbb = new StringBuilder();
		sbb.append(listDicType.get(0).getCode());
		if (listDicType.size() > 1) {
			for (int i = 1; i < listDicType.size(); i++) {
				DictionaryType dt = listDicType.get(i);
				sbb.append(',').append(dt.getCode());
			}
		}
		return sbb.toString();
	}

	/** 最大条数，0为无限 */
	public int getMaxNum() {
		return maxNum;
	}

	public String getDesc() {
		return desc;
	}

	public DictionaryFlag getFlag() {
		return flag;
	}

	public boolean canModify() {
		return canRemove() || this.flag.equals(DictionaryFlag.CAN_U_NOT_D);
	}

	public boolean canRemove() {
		return this.flag.equals(DictionaryFlag.CAN_U_D);
	}

	public String getUnit() {
		return unit;
	}
	@JSON(serialize=false)
	public DictionaryType getParent_type() {
		return parent_type;
	}
	
	public List<DictionaryType> getSubDicType(){
		List<DictionaryType> result = new ArrayList<DictionaryType>();
		for (Iterator<DictionaryType> iterator = mapDictionaryType.values().iterator(); iterator.hasNext();) {
			DictionaryType type = (DictionaryType) iterator.next();
			if (type.getParent_type().equals(this)) {
				result.add(type);
			}
		}
		return result;
	}
}
