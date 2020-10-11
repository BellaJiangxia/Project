package com.vastsoft.yingtai.module.sys.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.sys.constants.LogOperatModule;
import com.vastsoft.yingtai.module.sys.constants.LogStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;

public class TOperationRecord{
	private long id;
	private long operator_id;
	private long org_id;
	private int operator_type;
	private String inf_name="";
	private String params="";
	private String returns="";
	private int status;
	private String description="";
	private String error_msg="";
	private Date create_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}

	public int getOperator_type() {
		return operator_type;
	}
	
	public String getOperator_typeStr() {
		UserType ut=UserType.parseCode(operator_type);
		return ut==null?"未知":ut.getName();
	}

	public void setOperator_type(int operator_type) {
		this.operator_type = operator_type;
	}

	@Override
	public String toString() {
		return "TOperationRecord [id=" + id + ", operator_id=" + operator_id + ", org_id=" + org_id + ", operator_type="
				+ operator_type + ", inf_name=" + inf_name + ", params=" + params + ", returns=" + returns + ", status="
				+ status + ", description=" + description + ", error_msg=" + error_msg + ", create_time=" + create_time
				+ "]";
	}

	public String getInf_name() {
		return inf_name;
	}
	
	public String getModuleName() {
		LogOperatModule module=LogOperatModule.parseInfName(inf_name);
		return module==null?"未知":module.getName();
	}

	public void setInf_name(String inf_name) {
		this.inf_name = inf_name;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String paramets) {
		this.params = paramets;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusStr() {
		return LogStatus.parseCode(status).getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public Date getCreate_time() {
		return create_time;
	}
	
	public String getCreate_timeStr() {
		return DateTools.dateToString(create_time);
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

}
