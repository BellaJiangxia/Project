package com.vastsoft.yingtai.module.org.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.org.assist.AbstractOrgCertificationEntity;
import com.vastsoft.yingtai.module.org.constants.OrgChangeStatus;
import com.vastsoft.yingtai.module.org.constants.OrgProperty;

public class TOrgChange extends AbstractOrgCertificationEntity{
	private long id;
	private long type;
	private String org_name;
	private long logo_file_id;
	private String description;
	private long levels;
	private long verify_user_id;
	private Date varify_time;
	private String note;
	private String scan_file_ids;
	private Date change_time;
	private int change_status;
	private long org_id;
	private long change_user_id;
	
	private String v_c_user_name;
	private String v_v_user_name;
	
	private String type_name;
	private String level_name;
	private String c_type_name;
	private String c_level_name;
	private String c_description;
	private String creator_name;
	private String org_code;
	private String c_org_name;
	private int c_org_property;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public long getLogo_file_id() {
		return logo_file_id;
	}

	public void setLogo_file_id(long logo_file_id) {
		this.logo_file_id = logo_file_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getLevels() {
		return levels;
	}

	public void setLevels(long levels) {
		this.levels = levels;
	}

	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public Date getVarify_time() {
		return varify_time;
	}

	public void setVarify_time(Date varify_time) {
		this.varify_time = varify_time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getScan_file_ids() {
		return scan_file_ids;
	}

	public void setScan_file_ids(String scan_file_ids) {
		this.scan_file_ids = scan_file_ids;
	}

	public Date getChange_time() {
		return change_time;
	}

	public void setChange_time(Date change_time) {
		this.change_time = change_time;
	}

	public int getChange_status() {
		return change_status;
	}

	public void setChange_status(int change_status) {
		this.change_status = change_status;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public long getChange_user_id() {
		return change_user_id;
	}

	public void setChange_user_id(long change_user_id) {
		this.change_user_id = change_user_id;
	}

	public String getV_c_user_name() {
		return v_c_user_name;
	}

	public void setV_c_user_name(String v_c_user_name) {
		this.v_c_user_name = v_c_user_name;
	}

	public String getV_v_user_name() {
		return v_v_user_name;
	}

	public void setV_v_user_name(String v_v_user_name) {
		this.v_v_user_name = v_v_user_name;
	}
	
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getC_type_name() {
		return c_type_name;
	}

	public void setC_type_name(String c_type_name) {
		this.c_type_name = c_type_name;
	}

	public String getC_description() {
		return c_description;
	}

	public void setC_description(String c_description) {
		this.c_description = c_description;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getC_level_name() {
		return c_level_name;
	}

	public void setC_level_name(String c_level_name) {
		this.c_level_name = c_level_name;
	}

	public String getC_org_name() {
		return c_org_name;
	}

	public void setC_org_name(String c_org_name) {
		this.c_org_name = c_org_name;
	}

	public OrgChangeStatus getChangeStatus(){
		return OrgChangeStatus.parseCode(this.change_status);
	}

	public int getC_org_property() {
		return c_org_property;
	}

	public void setC_org_property(int c_org_property) {
		this.c_org_property = c_org_property;
	}

	public OrgProperty getChangeOrgProperty(){
		return OrgProperty.parseCode(this.c_org_property);
	}
}
