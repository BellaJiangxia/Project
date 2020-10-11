package com.vastsoft.yingtaidicom.search.assist;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;

public class RemoteEntityAgent {
	private AbstractRemoteEntity entity;
//	private Map<PatientDataExtraType, Object> extraAttributes = new HashMap<PatientDataExtraType, Object>();
	private String parentUid;
	private String uid;
//	/** 是由什么系统保存的 */
//	private ExternalSystemType esType;

	RemoteEntityAgent(String parentUid, AbstractRemoteEntity entity) {
		this();
		this.parentUid = parentUid;
//		if (this.esType == null)
//			throw new RuntimeException("保存系统类型必须指定！");
		this.entity = entity;
		if (this.entity == null)
			throw new RuntimeException("保存实体数据对象必须指定！");
	}

//	RemoteEntityAgent(ExternalSystemType esType, String parentUid, AbstractRemoteEntity entity) {
//		this();
//		this.parentUid = parentUid;
//		this.esType = esType;
//		if (this.esType == null)
//			throw new RuntimeException("保存系统类型必须指定！");
//		this.entity = entity;
//		if (this.entity == null)
//			throw new RuntimeException("保存实体数据对象必须指定！");
////		this.extraAttributes = extraAttributes;
//	}

	private RemoteEntityAgent() {
		super();
		this.uid = CommonTools.getUUID();
	}

	public void replace(AbstractRemoteEntity entity) {
		this.entity.replaceWith(entity);
//		if (extraAttributes == null || extraAttributes.size() <= 0)
//			return;
//		this.extraAttributes.putAll(extraAttributes);
	}

	public PatientDataType getPdType() {
		return this.entity.getPdType();
	}

	/**
	 * 此方法返回的数据的一个备份，并非源数据，当修改此返回对象源数据不会改变<br>
	 * 要改变源数据，请调用RemoteResult的updateData()方法
	 *
	 * @throws CloneNotSupportedException
	 */
	public AbstractRemoteEntity getEntity() throws CloneNotSupportedException {
		return (AbstractRemoteEntity) this.entity.clone();
	}

	public String getUid() {
		return this.uid;
	}
	
//	public Object getExtraAttr(PatientDataExtraType pdeType){
//		return this.extraAttributes.get(pdeType);
//	}
	
	JSONObject serialize() throws JSONException {
		JSONObject result = new JSONObject();
		result.put("entity", this.entity.serialize_s());
		result.put("uid", this.uid);
		result.put("parentUid", this.parentUid);
		result.put("pdType", this.getPdType().getCode());
//		if (this.extraAttributes != null && this.extraAttributes.size() > 0) {
//			JSONObject extraAttributesJsonObj = new JSONObject();
//			for (Iterator<PatientDataExtraType> iterator = this.extraAttributes.keySet().iterator(); iterator
//					.hasNext();) {
//				PatientDataExtraType type = (PatientDataExtraType) iterator.next();
//				extraAttributesJsonObj.putOpt(type.getKey_name(), this.extraAttributes.get(type));
//			}
//			result.put("extraAttributes", extraAttributesJsonObj);
//		}
		return result;
	}
	
	public static RemoteEntityAgent jsonDeserialize(JSONObject jsonObject) throws InstantiationException, IllegalAccessException, JSONException {
		RemoteEntityAgent result = new RemoteEntityAgent();
		PatientDataType pdType = PatientDataType.parseCode(jsonObject.optInt("pdType", 0));
		result.entity = pdType.getEntity();
		result.entity.deserialize_s(jsonObject.optJSONObject("entity"));
		result.uid = jsonObject.optString("uid");
		result.parentUid= jsonObject.optString("parentUid");
		return result;
	}

	public String getParentUid() {
		return this.parentUid;
	}

	ExternalSystemType getSystemType() {
		return this.entity.getSystemType();
	}

	void merge(AbstractRemoteEntity entity) {
		this.entity.merge(entity);
//		if (extraAttributes2 == null || extraAttributes2.size() <= 0)
//			return;
//		for (Iterator<PatientDataExtraType> iterator = extraAttributes2.keySet().iterator(); iterator.hasNext();) {
//			PatientDataExtraType type = (PatientDataExtraType) iterator.next();
//			Object obj = this.extraAttributes.get(type);
//			if (obj==null)
//				this.extraAttributes.put(type, extraAttributes2.get(type));
//		}
	}
}