package com.vastsoft.yingtai.module.org.realtion.service;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationStatus;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;

public class SearchOrgRelationConfigParam extends AbstractSearchParam {
	private long self_org_id;//用户当前所在的机构
	private Long relation_org_id;
	private long[] include_relation_org_ids;//包含的好友机构的ID
	private long[] exclude_relation_org_ids;//排除的好友机构的ID
	private String relation_org_name;
	private PublishReportType publish_report_type;
	private PublishReportType[] include_publish_report_types;//包含的发布报告类型
	private OrgRelationStatus status;
	private OrgRelationPatientInfoShareType share_patient_info;
	private OrgRelationPatientInfoShareType[] irspi;//include_relation_share_patient_info; // 包括的机构分享病例库类型

	public SearchOrgRelationConfigParam(long self_org_id) {
		super();
		this.self_org_id = self_org_id;
	}

	public long getSelf_org_id() {
		return self_org_id;
	}

	public void setSelf_org_id(long self_org_id) {
		this.self_org_id = self_org_id;
	}

	public Long getRelation_org_id() {
		return relation_org_id;
	}

	public void setRelation_org_id(Long relation_org_id) {
		this.relation_org_id = relation_org_id;
	}

	public PublishReportType getPublish_report_type() {
		return publish_report_type;
	}

	public void setPublish_report_type(PublishReportType publish_report_type) {
		this.publish_report_type = publish_report_type;
	}

	public OrgRelationStatus getStatus() {
		return status;
	}

	public void setStatus(OrgRelationStatus status) {
		this.status = status;
	}

	public String getRelation_org_name() {
		return relation_org_name;
	}

	public void setRelation_org_name(String relation_org_name) {
		this.relation_org_name = relation_org_name;
	}

	public OrgRelationPatientInfoShareType getShare_patient_info() {
		return share_patient_info;
	}

	public void setShare_patient_info(OrgRelationPatientInfoShareType share_patient_info) {
		this.share_patient_info = share_patient_info;
	}

	public long[] getInclude_relation_org_ids() {
		return include_relation_org_ids;
	}

	public void setInclude_relation_org_ids(long[] include_relation_org_ids) {
		this.include_relation_org_ids = include_relation_org_ids;
	}

	public long[] getExclude_relation_org_ids() {
		return exclude_relation_org_ids;
	}

	public void setExclude_relation_org_ids(long[] exclude_relation_org_ids) {
		this.exclude_relation_org_ids = exclude_relation_org_ids;
	}

	public PublishReportType[] getInclude_publish_report_types() {
		return include_publish_report_types;
	}

	public void setInclude_publish_report_types(PublishReportType[] include_publish_report_types) {
		this.include_publish_report_types = include_publish_report_types;
	}

//	public OrgRelationPatientInfoShareType[] getInclude_relation_share_patient_info() {
//		return include_relation_share_patient_info;
//	}
//
//	public void setInclude_relation_share_patient_info(OrgRelationPatientInfoShareType[] include_relation_share_patient_info) {
//		this.include_relation_share_patient_info = include_relation_share_patient_info;
//	}
	
	
	public OrgRelationPatientInfoShareType[] getIrspi() {
		return irspi;
	}

	/**设置机构病例分享的包含的类型
	 * @return
	 */
	public void setIrspi(OrgRelationPatientInfoShareType[] irspi) {
		this.irspi = irspi;
	}

}
