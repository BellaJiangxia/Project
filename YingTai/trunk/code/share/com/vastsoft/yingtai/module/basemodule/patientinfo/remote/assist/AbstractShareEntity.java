package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;

public abstract class AbstractShareEntity implements Cloneable {
	private int source_type;
	private long org_id;
	
	private ShareExternalSystemType systemType;
	private String brand;
	private String version;
	private boolean isCopy;

	public AbstractShareEntity() {
		super();
	}

	public AbstractShareEntity(ShareSystemIdentity systemIdentity) {
		this(systemIdentity.getSystemType(), systemIdentity.getSystemBrand().getName(),
				systemIdentity.getSystemVersion().getName());
	}

	public AbstractShareEntity(ShareExternalSystemType systemType, String brand, String version) {
		super();
		this.systemType = systemType;
		this.brand = brand;
		this.version = version;
	}

	public JSONObject serialize_s() throws JSONException {
		JSONObject result = this.serialize();
		result.put("systemType", systemType.getCode());
		result.put("brand", brand);
		result.put("version", version);
		return result;
	}

	/** JSON序列化 */
	protected abstract JSONObject serialize() throws JSONException;

	/** JSON反序列化 
	 * @throws BaseException 
	 * @throws JSONException */
	public void deserialize_s(JSONObject jsonObject,long org_id) throws BaseException, JSONException {
		this.systemType = ShareExternalSystemType.parseCode(jsonObject.optInt("systemType"));
		if (this.systemType == null)
			throw new JSONException("必须指定数据来源系统！");
		this.source_type = systemType.getSource_type().getCode();
		this.brand = jsonObject.optString("brand");
		this.version = jsonObject.optString("version");
		this.deserialize(jsonObject);
	}

	/** JSON反序列化 
	 * @throws BaseException */
	protected abstract void deserialize(JSONObject jsonObject) throws JSONException, BaseException;

	/** 替换，通过指定对象 */
	public void replaceWith(AbstractShareEntity entity) {
		if (entity.getSystemType() != null)
			this.setSystemType(entity.getSystemType());
		if (!StringTools.isEmpty(entity.getBrand()))
			this.setBrand(entity.getBrand().trim());
		if (!StringTools.isEmpty(entity.getVersion()))
			this.setVersion(entity.getVersion().trim());
		this.replaceFrom(entity);
	}

	protected abstract void replaceFrom(AbstractShareEntity entity);

	public abstract SharePatientDataType getPdType();

	public void merge(AbstractShareEntity entity) {
		if (this.getSystemType() == null)
			this.setSystemType(entity.getSystemType());
		if (StringTools.isEmpty(this.getBrand()))
			this.setBrand(entity.getBrand());
		if (StringTools.isEmpty(this.getVersion()))
			this.setVersion(entity.getVersion());
		this.mergeFrom(entity);
	}

	protected abstract void mergeFrom(AbstractShareEntity re);

	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractShareEntity result = (AbstractShareEntity) super.clone();
		result.isCopy = true;
		return result;
	}

	public int getSource_type() {
		return this.source_type;
	}

	public PatientDataSourceType getSourceType() {
		return PatientDataSourceType.parseCode(source_type);
	}

	public ShareExternalSystemType getSystemType() {
		return systemType;
	}
	@JSON(deserialize = false)
	public void setSystemType(ShareExternalSystemType systemType) {
		this.systemType = systemType;
		if (systemType == null)
			throw new RuntimeException("病人数据的来源类型必须指定！");
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isCopy() {
		return isCopy;
	}

	public void setSource_type(int source_type) {
		this.source_type = source_type;
	}

	public long getOrg_id() {
		return org_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
}
