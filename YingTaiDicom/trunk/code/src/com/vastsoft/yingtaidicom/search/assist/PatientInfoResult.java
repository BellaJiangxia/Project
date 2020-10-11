package com.vastsoft.yingtaidicom.search.assist;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.entity.TPatient;

public class PatientInfoResult {
	private TPatient patient;
	private ParamEntry paramEntry;

	public PatientInfoResult(TPatient patient, RemoteParamsType remoteParamsType, String paramValue) {
		super();
		this.patient = patient;
		this.paramEntry = new ParamEntry(remoteParamsType, paramValue);
	}

	public PatientInfoResult() {
	}

	public TPatient getPatient() {
		return this.patient;
	}

	public RemoteParamsType getRemoteParamsType() {
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
		private RemoteParamsType remoteParamsType;
		private String paramValue;

		private ParamEntry(RemoteParamsType remoteParamsType, String paramValue) {
			super();
			this.remoteParamsType = remoteParamsType;
			this.paramValue = paramValue;
			if (this.remoteParamsType == null)
				throw new RuntimeException();
			if (this.paramValue == null)
				throw new RuntimeException();
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
			this.remoteParamsType = RemoteParamsType.parseCode(jsonObject.optInt("remoteParamsType", 0));
		}

		public JSONObject jsonSerialize() throws JSONException {
			JSONObject result = new JSONObject();
			result.put("paramValue", this.paramValue);
			result.put("remoteParamsType", this.remoteParamsType.getCode());
			return result;
		}

	}

	public void jsonDeserialize(JSONObject jsonObject, long org_id) throws JSONException {
		try {
			JSONObject jsonObjTemp = jsonObject.optJSONObject("patient");
			if (jsonObjTemp == null)
				throw new JSONException("没有找到病人JSON对象！");
			this.patient = new TPatient();
			this.patient.deserialize_s(jsonObject);
			jsonObjTemp = jsonObject.optJSONObject("paramEntry");
			if (jsonObjTemp == null)
				throw new JSONException("没有找到参数实体JSON对象！");
			this.paramEntry = new ParamEntry();
			this.paramEntry.jsonDeserialize(jsonObjTemp);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JSONException(e);
		}

	}

	public JSONObject jsonSerialize() throws JSONException {
		try {
			JSONObject result = new JSONObject();
			result.put("patient", this.patient.serialize_s());
			result.put("paramEntry", this.paramEntry.jsonSerialize());
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new JSONException(e);
		}
	}
}
