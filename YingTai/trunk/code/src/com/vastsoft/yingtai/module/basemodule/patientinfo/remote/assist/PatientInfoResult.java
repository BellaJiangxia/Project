package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.MapBuilder;
import com.vastsoft.util.common.ReflectTools;
import com.vastsoft.util.common.ReflectTools.AccessableLevel;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.inf.ReflectToMapFilter;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.ReflectException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;

public class PatientInfoResult {
	private TPatient patient;
	private ParamEntry paramEntry;

	public PatientInfoResult(TPatient patient, ShareRemoteParamsType remoteParamsType, String paramValue) {
		super();
		this.patient = patient;
		this.paramEntry = new ParamEntry(remoteParamsType, paramValue);
	}

	public PatientInfoResult() {
	}

	public TPatient getPatient() {
		return this.patient;
	}

	public ShareRemoteParamsType getRemoteParamsType() {
		return this.paramEntry.remoteParamsType;
	}

	public String getParamValue() {
		return this.paramEntry.paramValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paramEntry == null) ? 0 : paramEntry.hashCode());
		result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
		PatientInfoResult other = (PatientInfoResult) obj;
		if (paramEntry == null) {
			if (other.paramEntry != null)
				return false;
		} else if (!paramEntry.equals(other.paramEntry))
			return false;
		if (patient == null) {
			if (other.patient != null)
				return false;
		} else if (!patient.equals(other.patient))
			return false;
		return true;
	}

	private static class ParamEntry {
		private ShareRemoteParamsType remoteParamsType;
		private String paramValue;

		private ParamEntry(ShareRemoteParamsType remoteParamsType, String paramValue) {
			super();
			this.remoteParamsType = remoteParamsType;
			this.paramValue = paramValue;
		}

		private ParamEntry() {
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((paramValue == null) ? 0 : paramValue.hashCode());
			result = prime * result + ((remoteParamsType == null) ? 0 : remoteParamsType.hashCode());
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
			ParamEntry other = (ParamEntry) obj;
			if (paramValue == null) {
				if (other.paramValue != null)
					return false;
			} else if (!paramValue.equals(other.paramValue))
				return false;
			if (remoteParamsType == null) {
				if (other.remoteParamsType != null)
					return false;
			} else if (!remoteParamsType.equals(other.remoteParamsType))
				return false;
			return true;
		}

		private void jsonDeserialize(JSONObject jsonObject) {
			this.paramValue = jsonObject.optString("paramValue");
			this.remoteParamsType = ShareRemoteParamsType.parseCode(jsonObject.optInt("remoteParamsType", 0));
		}

	}

	public void jsonDeserialize(JSONObject jsonObject, long org_id) throws JSONException {
		try {
			JSONObject jsonObjTemp = jsonObject.optJSONObject("patient");
			if (jsonObjTemp == null)
				throw new JSONException("没有找到病人JSON对象！");
			this.patient = new TPatient();
			this.patient.deserialize_s(jsonObjTemp, org_id);
			jsonObjTemp = jsonObject.optJSONObject("paramEntry");
			if (jsonObjTemp == null)
				throw new JSONException("没有找到参数实体JSON对象！");
			this.paramEntry = new ParamEntry();
			this.paramEntry.jsonDeserialize(jsonObjTemp);
		} catch (BaseException | JSONException e) {
			e.printStackTrace();
			throw new JSONException(e);
		}

	}

	public Map<String, Object> toMap(final int type) throws ReflectException {
		return ReflectTools.toMap(this, AccessableLevel.PUBLIC, false, new MapBuilder<String, ReflectToMapFilter>("patient",new ReflectToMapFilter(){
					@Override
					public Object filter(Object value) {
						try {
							Map<String, ReflectToMapFilter> mapBuilder = new HashMap<String, ReflectToMapFilter>();
							switch (type) {
							case 1:
								mapBuilder.put("identity_id",new ReflectToMapFilter(){
									@Override
									public Object filter(Object value) {
										if (value == null || StringTools.isEmpty(value.toString()))
											return null;
										return StringTools.formatEndStringWithTrim(value.toString(), 4, '*');
									}
								});
								break;
							case 2:
								mapBuilder.put("name",new ReflectToMapFilter(){
									@Override
									public Object filter(Object value) {
										if (value == null || StringTools.isEmpty(value.toString()))
											return null;
										return StringTools.formatBeginStringWithTrim(value.toString(), 1, '*');
									}
								});
								break;
							default:
								break;
							}
							return ReflectTools.toMap(value, AccessableLevel.PUBLIC, false, 
									mapBuilder, null);
						} catch (ReflectException e) {
							e.printStackTrace();
							return null;
						}
					}
				}).put("paramEntry", new ReflectToMapFilter() {
					@Override
					public Object filter(Object value) {
						try {
							if (value == null)
								return null;
							Map<String, ReflectToMapFilter> mapBuilder = new HashMap<String, ReflectToMapFilter>();
							mapBuilder.put("paramValue",new ReflectToMapFilter(){
								@Override
								public Object filter(Object value) {
									if (value == null || StringTools.isEmpty(value.toString()))
										return null;
									return StringTools.formatBeginStringWithTrim(value.toString(), 1, '*');
								}
							});
							return ReflectTools.toMap(value, AccessableLevel.PUBLIC, false, mapBuilder, null);
						} catch (ReflectException e) {
							e.printStackTrace();
							return null;
						}
					}
				}).toMap(), null);
	}
}
