package com.vastsoft.yingtai.module.org.realtion.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;

public class TOrgRelation {
	private long id;
	private long org_id;
	private long friend_org_id;
	private int publish_report_type;
	private Date create_time;
	private int status;
	
	public TOrgRelation(long org_id, long friend_org_id) {
		super();
		this.org_id = org_id;
		this.friend_org_id = friend_org_id;
	}

	public TOrgRelation() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public long getFriend_org_id() {
		return friend_org_id;
	}

	public void setFriend_org_id(long friend_org_id) {
		this.friend_org_id = friend_org_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusStr() {
		OrgStatus statos=OrgStatus.parseCode(status);
		return statos==null?"":statos.getName();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPublish_report_type() {
		return publish_report_type;
	}
	
	public String getPublish_report_typeStr() {
		PublishReportType prt = PublishReportType.parseFrom(publish_report_type);
		return prt==null?"":prt.getName();
	}

	public void setPublish_report_type(int publish_report_type) {
		this.publish_report_type = publish_report_type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (friend_org_id ^ (friend_org_id >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (org_id ^ (org_id >>> 32));
		result = prime * result + publish_report_type;
		result = prime * result + status;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TOrgRelation other = (TOrgRelation) obj;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (friend_org_id != other.friend_org_id)
			return false;
		if (id != other.id)
			return false;
		if (org_id != other.org_id)
			return false;
		if (publish_report_type != other.publish_report_type)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
}
