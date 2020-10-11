package com.vastsoft.yingtai.module.user.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

/**
 * @author jyb
 */
public class UserTypeDetail extends SingleClassConstant {
	public static final UserTypeDetail DOCTOR = new UserTypeDetail(101, "医师",null,UserType.ORG_DOCTOR);
	public static final UserTypeDetail TECHNICIAN = new UserTypeDetail(102, "技师",null,UserType.ORG_GENERAL);
	public static final UserTypeDetail NURSE = new UserTypeDetail(103, "护士",null,UserType.ORG_GENERAL);
	public static final UserTypeDetail OTHER = new UserTypeDetail(104, "其他",null,UserType.ORG_GENERAL);
	public static final UserTypeDetail NON_RADIOLOGIST = new UserTypeDetail(105, "非放射科医生",null,UserType.ORG_DOCTOR);
	
	public static final UserTypeDetail ZRYS = new UserTypeDetail(201, "主任医师",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);
	public static final UserTypeDetail FZRYS = new UserTypeDetail(202, "副主任医师",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);
	public static final UserTypeDetail ZZYS = new UserTypeDetail(203, "主治医师",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);
	public static final UserTypeDetail YS = new UserTypeDetail(204, "医师",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);
	public static final UserTypeDetail YSS = new UserTypeDetail(205, "医士",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);
	public static final UserTypeDetail JXSXYS = new UserTypeDetail(206, "进修实习医师",UserTypeDetail.DOCTOR,UserType.ORG_DOCTOR);

	public static final UserTypeDetail ZRJS = new UserTypeDetail(207, "主任技师",UserTypeDetail.TECHNICIAN,UserType.ORG_GENERAL);
	public static final UserTypeDetail FZRJS = new UserTypeDetail(208, "副主任技师",UserTypeDetail.TECHNICIAN,UserType.ORG_GENERAL);
	public static final UserTypeDetail JS = new UserTypeDetail(209, "技师",UserTypeDetail.TECHNICIAN,UserType.ORG_GENERAL);
	public static final UserTypeDetail JSS = new UserTypeDetail(210, "技士",UserTypeDetail.TECHNICIAN,UserType.ORG_GENERAL);
	
	public static final UserTypeDetail ZRHS = new UserTypeDetail(211, "主任护士",UserTypeDetail.NURSE,UserType.ORG_GENERAL);
	public static final UserTypeDetail FZRHS = new UserTypeDetail(212, "副主任护士",UserTypeDetail.NURSE,UserType.ORG_GENERAL);
	public static final UserTypeDetail HS = new UserTypeDetail(213, "护师",UserTypeDetail.NURSE,UserType.ORG_GENERAL);
	public static final UserTypeDetail HSS = new UserTypeDetail(214, "护士",UserTypeDetail.NURSE,UserType.ORG_GENERAL);

	public static final Map<Integer, UserTypeDetail> mapUserType = new HashMap<Integer, UserTypeDetail>();

	static {
		UserTypeDetail.mapUserType.put(DOCTOR.getCode(), DOCTOR);
		UserTypeDetail.mapUserType.put(TECHNICIAN.getCode(), TECHNICIAN);
		UserTypeDetail.mapUserType.put(NURSE.getCode(), NURSE);
		UserTypeDetail.mapUserType.put(OTHER.getCode(), OTHER);
		UserTypeDetail.mapUserType.put(NON_RADIOLOGIST.getCode(), NON_RADIOLOGIST);
		
		UserTypeDetail.mapUserType.put(ZRYS.getCode(), ZRYS);
		UserTypeDetail.mapUserType.put(FZRYS.getCode(), FZRYS);
		UserTypeDetail.mapUserType.put(ZZYS.getCode(), ZZYS);
		UserTypeDetail.mapUserType.put(YS.getCode(), YS);
		UserTypeDetail.mapUserType.put(YSS.getCode(), YSS);
		UserTypeDetail.mapUserType.put(JXSXYS.getCode(), JXSXYS);
		
		UserTypeDetail.mapUserType.put(ZRJS.getCode(), ZRJS);
		UserTypeDetail.mapUserType.put(FZRJS.getCode(), FZRJS);
		UserTypeDetail.mapUserType.put(JS.getCode(), JS);
		UserTypeDetail.mapUserType.put(JSS.getCode(), JSS);
		
		UserTypeDetail.mapUserType.put(ZRHS.getCode(), ZRHS);
		UserTypeDetail.mapUserType.put(FZRHS.getCode(), FZRHS);
		UserTypeDetail.mapUserType.put(HS.getCode(), HS);
		UserTypeDetail.mapUserType.put(HSS.getCode(), HSS);
	}

	private UserType ut;
	private UserTypeDetail parent;
	
	public UserTypeDetail(int iCode, String strName,UserTypeDetail parent,UserType type) {
		super(iCode, strName);
		this.ut=type;
		this.parent=parent;
	}

	public UserType getUserType() {
		return ut;
	}

	public UserTypeDetail getParent() {
		return parent;
	}

	public static UserTypeDetail parseCode(int iCode) {
		return UserTypeDetail.mapUserType.get(iCode);
	}

	public static List<UserTypeDetail> getAllByType(UserType type) {
		List<UserTypeDetail> list=new ArrayList<UserTypeDetail>();
		for(UserTypeDetail utd:mapUserType.values()){
			if(type.equals(utd.getUserType()))
				list.add(utd);
		}
		return list;
	}
	
	public static List<UserTypeDetail> getAllByParent(UserTypeDetail utd) {
		List<UserTypeDetail> list=new ArrayList<UserTypeDetail>();
		for(UserTypeDetail uu:mapUserType.values()){
			if(uu.getParent().equals(utd))
				list.add(uu);
		}
		return list;
	}
	
	public static List<UserTypeDetail> getAll(){
		return new ArrayList<UserTypeDetail>(mapUserType.values());
	}
}
