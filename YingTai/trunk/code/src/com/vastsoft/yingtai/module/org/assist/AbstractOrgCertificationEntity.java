package com.vastsoft.yingtai.module.org.assist;

import com.vastsoft.yingtai.module.org.constants.OrgProperty;

public abstract class AbstractOrgCertificationEntity {
	private String org_name;
	private int org_property;
	private long levels;
	private long logo_file_id;
	private String scan_file_ids;

	public AbstractOrgCertificationEntity() {
		super();
	}

	public int getOrg_property() {
		return org_property;
	}

	public void setOrg_property(int org_property) {
		this.org_property = org_property;
	}

	public long getLogo_file_id() {
		return logo_file_id;
	}

	public void setLogo_file_id(long logo_file_id) {
		this.logo_file_id = logo_file_id;
	}

	public String getScan_file_ids() {
		return scan_file_ids;
	}

	public void setScan_file_ids(String scan_file_ids) {
		this.scan_file_ids = scan_file_ids;
	}

	public long getLevels() {
		return levels;
	}

	public void setLevels(long levels) {
		this.levels = levels;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public OrgProperty getProperty(){
		return OrgProperty.parseCode(this.org_property);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (levels ^ (levels >>> 32));
		result = prime * result + (int) (logo_file_id ^ (logo_file_id >>> 32));
		result = prime * result + ((org_name == null) ? 0 : org_name.hashCode());
		result = prime * result + org_property;
		result = prime * result + ((scan_file_ids == null) ? 0 : scan_file_ids.hashCode());
		return result;
	}

	/**判断认证属性是否发生变化*/
	public boolean aoceEquals(AbstractOrgCertificationEntity obj) {
		if (this == obj)
			throw new RuntimeException("您不能检查同一个对象！");
		if (obj == null)
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		if (levels != obj.levels)
			return false;
		if (logo_file_id != obj.logo_file_id)
			return false;
		if (org_name == null) {
			if (obj.org_name != null)
				return false;
		} else if (!org_name.equals(obj.org_name))
			return false;
		if (org_property != obj.org_property)
			return false;
		if (scan_file_ids == null) {
			if (obj.scan_file_ids != null)
				return false;
		} else if (!scan_file_ids.equals(obj.scan_file_ids))
			return false;
		return true;
	}

	public OrgProperty getOrgProperty(){
		return OrgProperty.parseCode(this.org_property);
	}
}
