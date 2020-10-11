package com.vastsoft.yingtai.module.org.realtion.entity;

import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;

public class TOrgRelationConfig {
	private long id;
	private long relation_id;
	private long org_id;
	private long publish_report_org_id;
	private int share_patient_info;//分享病例库//只有当此值为 1 时才表示分享
	
	public TOrgRelationConfig(long relation_id, long org_id) {
		super();
		this.relation_id = relation_id;
		this.org_id = org_id;
	}

	public TOrgRelationConfig() {
		super();
	}

	//仅显示
	private String v_report_name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(long relation_id) {
		this.relation_id = relation_id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public long getPublish_report_org_id() {
		return publish_report_org_id;
	}

	public void setPublish_report_org_id(long publish_report_org_id) {
		this.publish_report_org_id = publish_report_org_id;
	}

	public String getV_report_name() {
		return v_report_name;
	}

	public void setV_report_name(String v_report_name) {
		this.v_report_name = v_report_name;
	}

	public int getShare_patient_info() {
		return share_patient_info;
	}
	
	public String getShare_patient_infoStr() {
		OrgRelationPatientInfoShareType or = OrgRelationPatientInfoShareType.parseCode(share_patient_info);
		return or==null?"":or.getName();
	}

	public void setShare_patient_info(int share_patient_info) {
		this.share_patient_info = share_patient_info;
	}

}
