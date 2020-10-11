package com.vastsoft.yingtai.module.org.product.assist;

import com.vastsoft.util.db.AbstractSearchParam;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductStatus;

public class SearchOrgProductParam extends AbstractSearchParam {
	private Long id;
	private Long org_id;
	private String name;
	private String like_name;
	private Long device_type_id;
	private Long part_type_id;
	private OrgProductStatus status;
	private OrgProductStatus[] exclude_statuses;
	private OrgProductChargeType charge_type;
	public Long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLike_name() {
		return like_name;
	}
	public void setLike_name(String like_name) {
		this.like_name = like_name;
	}
	public Long getDevice_type_id() {
		return device_type_id;
	}
	public void setDevice_type_id(Long device_type_id) {
		this.device_type_id = device_type_id;
	}
	public Long getPart_type_id() {
		return part_type_id;
	}
	public void setPart_type_id(Long part_type_id) {
		this.part_type_id = part_type_id;
	}
	public OrgProductStatus getStatus() {
		return status;
	}
	public void setStatus(OrgProductStatus status) {
		this.status = status;
	}
	public OrgProductChargeType getCharge_type() {
		return charge_type;
	}
	public void setCharge_type(OrgProductChargeType charge_type) {
		this.charge_type = charge_type;
	}
	public OrgProductStatus[] getExclude_statuses() {
		return exclude_statuses;
	}
	public void setExclude_statuses(OrgProductStatus[] exclude_statuses) {
		this.exclude_statuses = exclude_statuses;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
