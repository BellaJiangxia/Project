package com.vastsoft.yingtai.module.org.realtion.entity;

import java.util.List;

import com.vastsoft.util.common.CollectionTools;

public class FOrgRelation{
	private long relation_org_id;
	private String relation_org_name;
	private long relation_org_code;
	private String relation_org_description;
	private String relation_org_permission;
	private long relation_org_type;
	private String relation_org_type_name;
	
	private TOrgRelation org_relation;
	private TOrgRelationConfig org_relation_config;
	private TOrgRelationConfig relation_org_relation_config;
	
	public long getRelation_org_id() {
		return relation_org_id;
	}
	public void setRelation_org_id(long relation_org_id) {
		this.relation_org_id = relation_org_id;
	}
	public String getRelation_org_name() {
		return relation_org_name;
	}
	public void setRelation_org_name(String relation_org_name) {
		this.relation_org_name = relation_org_name;
	}
	public long getRelation_org_code() {
		return relation_org_code;
	}
	public void setRelation_org_code(long relation_org_code) {
		this.relation_org_code = relation_org_code;
	}
	public String getRelation_org_description() {
		return relation_org_description;
	}
	public void setRelation_org_description(String relation_org_description) {
		this.relation_org_description = relation_org_description;
	}
	public String getRelation_org_permission() {
		return relation_org_permission;
	}
	public void setRelation_org_permission(String relation_org_permission) {
		this.relation_org_permission = relation_org_permission;
	}
	public long getRelation_org_type() {
		return relation_org_type;
	}
	public void setRelation_org_type(long relation_org_type) {
		this.relation_org_type = relation_org_type;
	}

	public TOrgRelation getOrg_relation() {
		return org_relation;
	}

	public void setOrg_relation(TOrgRelation org_relation) {
		this.org_relation = org_relation;
	}
	public TOrgRelationConfig getOrg_relation_config() {
		return org_relation_config;
	}
	public void setOrg_relation_config(TOrgRelationConfig org_relation_config) {
		this.org_relation_config = org_relation_config;
	}
//	public List<OrgPermission> getRelation_org_permissionList() {
//		return PermissionsSerializer.fromOrgPermissionString(this.relation_org_permission);
//	}
	public String getRelation_org_type_name() {
		return relation_org_type_name;
	}
	public void setRelation_org_type_name(String relation_org_type_name) {
		this.relation_org_type_name = relation_org_type_name;
	}
	public TOrgRelationConfig getRelation_org_relation_config() {
		return relation_org_relation_config;
	}
	public void setRelation_org_relation_config(TOrgRelationConfig relation_org_relation_config) {
		this.relation_org_relation_config = relation_org_relation_config;
	}
	public static FOrgRelation existInListByRelationOrgId(List<FOrgRelation> listOrgRelation,
			long relation_org_id) {
		if (CollectionTools.isEmpty(listOrgRelation))
			return null;
		for (FOrgRelation fOrgRelation : listOrgRelation) {
			if (fOrgRelation.getRelation_org_id()==relation_org_id)
				return fOrgRelation;
		}
		return null;
	}
}
