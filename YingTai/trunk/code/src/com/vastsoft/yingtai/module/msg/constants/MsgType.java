package com.vastsoft.yingtai.module.msg.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class MsgType extends SingleClassConstant {
	private String desc;
	private boolean edit;
	private boolean show;
	/** 新诊断提醒短信 */
	public static MsgType DIAGNOSIS_REMIND = new MsgType(1, "新诊断提醒","当有新的诊断申请，且机构中一段时间之后无人诊断时发送短信提醒！",true,true);
	/** 诊断完成提醒短信 */
	public static MsgType DIAGNOSISED_REMIND = new MsgType(2, "新报告提醒","当有诊断机构诊断完成的信息报告时短信提醒！",true,true);
	/** 新移交提醒*/
	public static MsgType NEW_TRANFAR_REMIND = new MsgType(3, "新移交提醒","当机构中有人将诊断移交给您时短信提醒！",true,true);
	/** 重置登录密码*/
	public static MsgType RESET_PASSPORT_REMIND = new MsgType(4, "重置密码提醒","当您重置密码时发送验证码给您，此短信必须接收！",false,true);
	/** 注册验证码*/
	public static MsgType REGESTER_CHECK_CODE_REMIND = new MsgType(5, "注册验证码提醒","发送注册验证码短信",false,false);
	
	private static Map<Integer, MsgType> mapMsgType = new HashMap<Integer, MsgType>();
	static {
		mapMsgType.put(DIAGNOSIS_REMIND.getCode(), DIAGNOSIS_REMIND);
		mapMsgType.put(DIAGNOSISED_REMIND.getCode(), DIAGNOSISED_REMIND);
		mapMsgType.put(NEW_TRANFAR_REMIND.getCode(), NEW_TRANFAR_REMIND);
		mapMsgType.put(RESET_PASSPORT_REMIND.getCode(), RESET_PASSPORT_REMIND);
		mapMsgType.put(REGESTER_CHECK_CODE_REMIND.getCode(), REGESTER_CHECK_CODE_REMIND);
	}
	
	protected MsgType(int iCode, String strName,String desc,boolean edit,boolean show) {
		super(iCode, strName);
		this.desc=desc;
		this.edit=edit;
		this.show=show;
	}

	public static MsgType parseCode(int iCode) {
		return MsgType.mapMsgType.get(iCode);
	}

	public static List<MsgType> getAll() {
		return new ArrayList<MsgType>(MsgType.mapMsgType.values());
	}

	public String getDesc() {
		return desc;
	}

	public boolean isEdit() {
		return edit;
	}

	public boolean isShow() {
		return show;
	}
}
