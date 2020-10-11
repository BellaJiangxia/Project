package com.vastsoft.yingtai.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.user.constants.UserType;

/**
 * @author HonF
 */
public class UserPermission extends SingleClassConstant implements Permission {

	protected UserPermission(int iCode, String strName) {
		super(iCode, strName);
	}

	private static Map<Integer, UserPermission> mapAdminPower = new HashMap<Integer, UserPermission>();
	private static Map<Integer, UserPermission> mapUserPower = new HashMap<Integer, UserPermission>();
	private static Map<Integer, UserPermission> mapAllPerms=new HashMap<Integer, UserPermission>();
	
	/** 超级管理员权限 */
	public final static UserPermission SUPER_ADMIN = new UserPermission(100, "超级管理员权限");
	/** 用户管理权限 */
	public final static UserPermission ADMIN_USER_MGR = new UserPermission(101, "用户管理权限");
	/** 机构管理权限 */
	public final static UserPermission ADMIN_ORG_MGR = new UserPermission(102, "机构管理权限");
	/** 病历查看权限 */
	public final static UserPermission ADMIN_MEDICAL_MGR = new UserPermission(103, "病历查看权限");
	/** 结算系统管理权限 */
	public final static UserPermission ADMIN_FINALCAL_MGR = new UserPermission(104, "结算系统管理权限");
	/** 消息管理权限 */
	public final static UserPermission ADMIN_MESSAGE_MGR = new UserPermission(105, "消息管理权限");
	/** 统计管理权限 */
	public final static UserPermission ADMIN_SATA_MGR = new UserPermission(106, "统计管理权限");
	/** 公共模版管理权限 */
	public final static UserPermission ADMIN_TEMPLATE_MGR = new UserPermission(107, "公共模版管理权限");
	/** 日志管理权限 */
	public final static UserPermission ADMIN_LOG_MGR = new UserPermission(108, "日志管理权限");
	/** 字典管理权限 */
	public final static UserPermission ADMIN_DIC = new UserPermission(109, "字典管理权限");
	/** 字典管理权限 */
	public final static UserPermission ADMIN_QUALITY_CONTROL = new UserPermission(110, "质量控制权限");

	/** 报告员权限 */
	public final static UserPermission ORG_REPORT = new UserPermission(201, "报告诊断权限");
	/** 审核员权限 */
	public final static UserPermission ORG_AUDIT = new UserPermission(202, "审核诊断权限");
	/** 病历管理权限 */
	public final static UserPermission ORG_MEDICAL_MGR = new UserPermission(203, "病历管理权限");
	/** 机构管理权限 */
	public final static UserPermission ORG_MGR = new UserPermission(204, "机构管理权限");

	static {
		// mapAdminPower.put(SUPER_ADMIN.getCode(), SUPER_ADMIN);
		mapAdminPower.put(ADMIN_USER_MGR.getCode(), ADMIN_USER_MGR);
		mapAdminPower.put(ADMIN_ORG_MGR.getCode(), ADMIN_ORG_MGR);
		mapAdminPower.put(ADMIN_MEDICAL_MGR.getCode(), ADMIN_MEDICAL_MGR);
		mapAdminPower.put(ADMIN_FINALCAL_MGR.getCode(), ADMIN_FINALCAL_MGR);
		mapAdminPower.put(ADMIN_MESSAGE_MGR.getCode(), ADMIN_MESSAGE_MGR);
		mapAdminPower.put(ADMIN_SATA_MGR.getCode(), ADMIN_SATA_MGR);
		mapAdminPower.put(ADMIN_TEMPLATE_MGR.getCode(), ADMIN_TEMPLATE_MGR);
		mapAdminPower.put(ADMIN_LOG_MGR.getCode(), ADMIN_LOG_MGR);
		mapAdminPower.put(ADMIN_DIC.getCode(), ADMIN_DIC);

		mapUserPower.put(ORG_REPORT.getCode(), ORG_REPORT);
		mapUserPower.put(ORG_AUDIT.getCode(), ORG_AUDIT);
		mapUserPower.put(ORG_MEDICAL_MGR.getCode(), ORG_MEDICAL_MGR);
		mapUserPower.put(ORG_MGR.getCode(), ORG_MGR);
		
		mapAllPerms.put(SUPER_ADMIN.getCode(), SUPER_ADMIN);
		mapAllPerms.put(ADMIN_USER_MGR.getCode(), ADMIN_USER_MGR);
		mapAllPerms.put(ADMIN_ORG_MGR.getCode(), ADMIN_ORG_MGR);
		mapAllPerms.put(ADMIN_MEDICAL_MGR.getCode(), ADMIN_MEDICAL_MGR);
		mapAllPerms.put(ADMIN_FINALCAL_MGR.getCode(), ADMIN_FINALCAL_MGR);
		mapAllPerms.put(ADMIN_MESSAGE_MGR.getCode(), ADMIN_MESSAGE_MGR);
		mapAllPerms.put(ADMIN_SATA_MGR.getCode(), ADMIN_SATA_MGR);
		mapAllPerms.put(ADMIN_TEMPLATE_MGR.getCode(), ADMIN_TEMPLATE_MGR);
		mapAllPerms.put(ADMIN_LOG_MGR.getCode(), ADMIN_LOG_MGR);
		mapAllPerms.put(ADMIN_DIC.getCode(), ADMIN_DIC);

		mapAllPerms.put(ORG_REPORT.getCode(), ORG_REPORT);
		mapAllPerms.put(ORG_AUDIT.getCode(), ORG_AUDIT);
		mapAllPerms.put(ORG_MEDICAL_MGR.getCode(), ORG_MEDICAL_MGR);
		mapAllPerms.put(ORG_MGR.getCode(), ORG_MGR);
	}

	public static UserPermission parseAdminCode(int iCode) {
		return mapAdminPower.get(iCode);
	}

	public static List<UserPermission> getAllAdminPower() {
		return new ArrayList<UserPermission>(mapAdminPower.values());
	}

	// 判断权限列表是否全部是管理员权限
	public static boolean isAdminPermission(Set<UserPermission> listPerms) {
		List<UserPermission> listAdminPerms = getAllAdminPower();
		for (UserPermission userPermission : listPerms) {
			if (userPermission.equals(UserPermission.SUPER_ADMIN))
				return false;
			if (!listAdminPerms.contains(userPermission))
				return false;
		}
		return true;
	}

//	// 判断权限列表是否全部是机构用户权限
//	public static boolean isOrgPermission(List<UserPermission> listPerms,OrgPermission op) {
//		List<UserPermission> listOrgPerms = getAllUserPower();
//		for (UserPermission userPermission : listPerms) {
//			if (userPermission.equals(UserPermission.SUPER_ADMIN))
//				return false;
//			if(OrgPermission.DIAGNOSISER.equals(op)){
//				if (UserPermission.ORG_MEDICAL_MGR.equals(userPermission))
//					return false;
//			}else if(OrgPermission.REQUESTOR.equals(op)){
//				if (UserPermission.ORG_AUDIT.equals(userPermission) || UserPermission.ORG_REPORT.equals(userPermission))
//					return false;
//			}else{
//				if (!listOrgPerms.contains(userPermission))
//					return false;
//			}
//		}
//		
//		return true;
//	}
	
	public static boolean isUserPermission(List<UserPermission> listPerms,UserType ut){
		List<UserPermission> ups=UserPermission.getAllUserPower();
		
		for(UserPermission up:listPerms){
			if(UserType.ORG_GENERAL.equals(ut)&&(UserPermission.ORG_AUDIT.equals(up) || UserPermission.ORG_REPORT.equals(up))){
				return false;
			//医生也可管理病历
//			}else if(UserType.ORG_DOCTOR.equals(ut)&&UserPermission.ORG_MEDICAL_MGR.equals(up)){
//				return false;
			}else{
				if (!ups.contains(up))
					return false;
			}
		}
		
		return true;
	}

	public static UserPermission parseUserCode(int iCode) {
		return mapUserPower.get(iCode);
	}

	public static List<UserPermission> getAllUserPower() {
		return new ArrayList<UserPermission>(mapUserPower.values());
	}
	
	public static UserPermission parseCode(int iCode){
		return mapAllPerms.get(iCode);
	}
	
	public static List<UserPermission> getAllPermission(){
		return new ArrayList<UserPermission>();
	}

	@Override
	public PermissionType getType() {
		return PermissionType.UserPermissionType;
	}

	public static List<UserPermission> parsePermissionStr(String user_permission) {
		List<UserPermission> listp=new ArrayList<UserPermission>();
		if (user_permission==null||user_permission.trim().isEmpty())
			return listp;
		String [] pps=StringTools.splitString(user_permission, ',');
		for (String pp : pps) {
			try {
				UserPermission op= UserPermission.parseCode(Integer.valueOf(pp));
				if(op!=null)
					listp.add(op);
			} catch (Exception e) {
				continue;
			}
		}
		return listp;
	}
}
