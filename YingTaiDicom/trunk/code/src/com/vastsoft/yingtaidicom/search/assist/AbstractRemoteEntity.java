package com.vastsoft.yingtaidicom.search.assist;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public abstract class AbstractRemoteEntity implements Cloneable {
	private ExternalSystemType systemType;
	private String brand;
	private String version;
	private boolean isCopy;

	public AbstractRemoteEntity(SystemIdentity systemIdentity) {
		this(systemIdentity.getSystemType(), systemIdentity.getSystemBrand().getName(),
				systemIdentity.getSystemVersion().getName());
	}

	public AbstractRemoteEntity(ExternalSystemType systemType, String brand, String version) {
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

	/** JSON反序列化 */
	public void deserialize_s(JSONObject jsonObject) throws JSONException {
		this.systemType = ExternalSystemType.parseCode(jsonObject.optInt("systemType"));
		this.brand = jsonObject.optString("brand");
		this.version = jsonObject.optString("version");
		this.deserialize(jsonObject);
	}

	/** JSON反序列化 */
	protected abstract void deserialize(JSONObject jsonObject) throws JSONException;

	/** 替换，通过指定对象 */
	public void replaceWith(AbstractRemoteEntity entity) {
		if (entity.getSystemType() != null)
			this.setSystemType(entity.getSystemType());
		if (!StringTools.isEmpty(entity.getBrand()))
			this.setBrand(entity.getBrand().trim());
		if (!StringTools.isEmpty(entity.getVersion()))
			this.setVersion(entity.getVersion().trim());
		this.replaceFrom(entity);
	}

	protected abstract void replaceFrom(AbstractRemoteEntity entity);

	public abstract PatientDataType getPdType();

	public void merge(AbstractRemoteEntity entity) {
		if (this.getSystemType() == null)
			this.setSystemType(entity.getSystemType());
		if (StringTools.isEmpty(this.getBrand()))
			this.setBrand(entity.getBrand());
		if (StringTools.isEmpty(this.getVersion()))
			this.setVersion(entity.getVersion());
		this.mergeFrom(entity);
	}

	protected abstract void mergeFrom(AbstractRemoteEntity re);

	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractRemoteEntity result = (AbstractRemoteEntity) super.clone();
		result.isCopy = true;
		return result;
	}

	public ExternalSystemType getSystemType() {
		return systemType;
	}

	public void setSystemType(ExternalSystemType systemType) {
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
}
