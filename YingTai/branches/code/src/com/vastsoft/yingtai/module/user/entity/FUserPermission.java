package com.vastsoft.yingtai.module.user.entity;

import java.util.HashSet;
import java.util.Set;

import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class FUserPermission {
	private boolean selected;
	private UserPermission userPermission;

	public FUserPermission(UserPermission up) {
		this.userPermission = up;
	}

	public FUserPermission(UserPermission up, boolean selected) {
		this.userPermission = up;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public UserPermission getUserPermission() {
		return userPermission;
	}

	public static Set<FUserPermission> getAllPermissionByUserTypeAndOrgPerm(UserType userType,Set<UserPermission> listUserPerms){
		Set<FUserPermission> listfups=new HashSet<FUserPermission>();
		if (userType==null)
			return listfups;
//		if (listOrgPerms.contains(OrgPermission.DIAGNOSISER)) {
			if(userType.equals(UserType.ORG_DOCTOR)){
				FUserPermission fup=new FUserPermission(UserPermission.ORG_REPORT);
				if (listUserPerms.contains(UserPermission.ORG_REPORT))
					fup.selected=true;
				listfups.add(fup);
				fup=new FUserPermission(UserPermission.ORG_AUDIT);
				if (listUserPerms.contains(UserPermission.ORG_AUDIT))
					fup.selected=true;
				listfups.add(fup);
			}
//		}
//		if (listOrgPerms.contains(OrgPermission.REQUESTOR)) {
			FUserPermission fup=new FUserPermission(UserPermission.ORG_MEDICAL_MGR);
			if (listUserPerms.contains(UserPermission.ORG_MEDICAL_MGR))
				fup.selected=true;
			listfups.add(fup);
//		}
		fup=new FUserPermission(UserPermission.ORG_MGR);
		if (listUserPerms.contains(UserPermission.ORG_MGR))
			fup.selected=true;
		listfups.add(fup);
		return listfups;
	}
}
