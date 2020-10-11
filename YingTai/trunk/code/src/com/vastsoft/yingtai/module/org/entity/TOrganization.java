package com.vastsoft.yingtai.module.org.entity;

import java.util.Date;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.org.assist.AbstractOrgCertificationEntity;
import com.vastsoft.yingtai.module.org.constants.OrgCertificationLevel;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.constants.OrgVisible;

public class TOrganization extends AbstractOrgCertificationEntity{
	private long id;
	private long org_code;
	private String permission;
	private long creator_id;
//	private long logo_file_id;
	private String description;
	private long verify_user_id;
	private Date varify_time;
	private int status;
	private Date create_time;
//	private String scan_file_ids;//机构其他上传的照片
	private String note;
	private int visible;
	private int is_public;
//	private int org_property;
	private int certification_level;//认证级别
	private long type;
	
	//仅作显示
	private String creator_name;
	private String type_name;
	private String level_name;
	private String verifier_name;
	private String v_creator_mobile;
	private int v_creator_type;
	
	public TOrganization() {
		super();
	}

	public TOrganization(long id) {
		super();
		this.id = id;
	}


	public long getOrg_code() {
		return org_code;
	}

	public void setOrg_code(long org_code) {
		this.org_code = org_code;
	}

	public long getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(long creator_id) {
		this.creator_id = creator_id;
	}

	public String getDescription() {
		return description;
	}
	
	public String getDesc() {
		return StringTools.cutStr(description,15);
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}
	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

//	public String getScan_file_ids() {
//		return scan_file_ids;
//	}
//
//	public void setScan_file_ids(String scan_file_ids) {
//		this.scan_file_ids = scan_file_ids;
//	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getVerifier_name() {
		return verifier_name;
	}

	public void setVerifier_name(String verifier_name) {
		this.verifier_name = verifier_name;
	}
	
	public Date getVarify_time() {
		return varify_time;
	}

	public void setVarify_time(Date varify_time) {
		this.varify_time = varify_time;
	}
//	public boolean isRequester(){
//		List<OrgPermission> listOP=this.getPermissionList();
//		return listOP.contains(OrgPermission.REQUESTOR);
//	}
	
//	public boolean isDiagnosiser(){
//		List<OrgPermission> listOP=this.getPermissionList();
//		return listOP.contains(OrgPermission.DIAGNOSISER);
//	}
	
//	public List<OrgPermission> getPermissionList() {
//		return PermissionsSerializer.fromOrgPermissionString(this.permission);
//	}

	public int getVisible() {
		return visible;
	}
	
	public String getVisibleStr() {
		OrgVisible ov= OrgVisible.parseCode(this.visible);
		return ov==null?"":ov.getName();
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getV_creator_mobile() {
		return v_creator_mobile;
	}

	public void setV_creator_mobile(String v_creator_mobile) {
		this.v_creator_mobile = v_creator_mobile;
	}

	public int getV_creator_type() {
		return v_creator_type;
	}

	public void setV_creator_type(int v_creator_type) {
		this.v_creator_type = v_creator_type;
	}
	
	public OrgStatus getOrgStatus(){
		return OrgStatus.parseCode(this.status);
	}

	public int getIs_public() {
		return is_public;
	}

	public void setIs_public(int is_public) {
		this.is_public = is_public;
	}

	public int getCertification_level() {
		return certification_level;
	}
	
	public String getCertification_level_name() {
		OrgCertificationLevel ocl = OrgCertificationLevel.parseCode(certification_level);
		return ocl==null?"":ocl.getName();
	}
	
	public void setCertification_level(int certification_level) {
		this.certification_level = certification_level;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OrgCertificationLevel getCertificationLevel(){
		return OrgCertificationLevel.parseCode(this.certification_level);
	}

	public boolean getIsVIP(){
		return OrgCertificationLevel.CERTIFICATIONED.getCode()==this.certification_level;
	}
}
