package com.vastsoft.yingtaidicom.search.entity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.assist.AbstractRemoteEntity;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class TPatientOrgMapper extends AbstractRemoteEntity {
	private long id;
	private long patient_id;
	private long org_id;
	private String card_num;// 就诊卡号
	private Date got_card_time;// 办卡时间
	private Date create_time;

	public TPatientOrgMapper(ExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public TPatientOrgMapper(SystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	public long getId() {
		return id;
	}

	public long getPatient_id() {
		return patient_id;
	}

	public long getOrg_id() {
		return org_id;
	}

	public String getCard_num() {
		return card_num;
	}

	public Date getGot_card_time() {
		return got_card_time;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPatient_id(long patient_id) {
		this.patient_id = patient_id;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public void setGot_card_time(Date got_card_time) {
		this.got_card_time = got_card_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@Override
	public PatientDataType getPdType() {
		return PatientDataType.PATIENT_ORG_MAPPER_DA;
	}

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject result = new JSONObject();
		result.put("card_num", this.card_num);
		result.put("got_card_time", DateTools.dateToString(this.getGot_card_time()));
		return result;
	}

	@Override
	protected void replaceFrom(AbstractRemoteEntity entity) {
		TPatientOrgMapper patientOrgMapper = (TPatientOrgMapper) entity;
		if (!StringTools.isEmpty(patientOrgMapper.getCard_num()))
			this.card_num = patientOrgMapper.card_num;
		if (patientOrgMapper.getGot_card_time() != null)
			this.got_card_time = patientOrgMapper.getGot_card_time();
	}

	@Override
	protected void mergeFrom(AbstractRemoteEntity re) {
		TPatientOrgMapper patientOrgMapper = (TPatientOrgMapper) re;
		if (StringTools.isEmpty(this.card_num))
			this.card_num = patientOrgMapper.getCard_num();
		if (this.getGot_card_time() == null)
			this.got_card_time = patientOrgMapper.getGot_card_time();
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		this.card_num = jsonObject.optString("card_num", "");
		this.got_card_time = DateTools.strToDate(jsonObject.optString("got_card_time", ""));
	}
}
