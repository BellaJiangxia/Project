package com.vastsoft.yingtaidicom.search.entity;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.assist.AbstractRemoteEntity;
import com.vastsoft.yingtaidicom.search.constants.CaseHistoryType;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

/** 病例 */
public class TCaseHistory extends AbstractRemoteEntity {
	private long id;
	private long patient_id;
	private long org_id;
	private String case_his_num;// 病历号
	private int type;// 门诊；住院；远诊；其他
	// private String eps_num;
	// private String bingan_num;// 病案号
	// private long bingan_id;// 病案ID
	// private String pacs_case_num;// pasc系统病人病历号
	// private String jiuzhen_num;// 就诊序号

	private String symptom;// 症状/查体
	private String diagnosis;// 诊断
	private String patient_say;// 病人主诉
	 private String check_body;//查体
	private String doctor_advice;// 医嘱
	private String guahao_type;// 挂号类别

	private Date enter_time; // 入院时间
	private Date leave_time; // 出院时间
	private int status;
	private Date create_time;
	private long create_user_id;// 0标识系统检索到的
	private Date modify_time;
	private long modify_user_id;
	private String note;
	private String hospitalized_num;// 住院号
	private String reception_section;// 接诊科室
	private String reception_doctor;// 接诊医生
	private String eaf_list;// 电子申请单
	private int state;// 是否有效 99标识无效，其他标识有效

	public TCaseHistory(ExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public TCaseHistory(SystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	public TCaseHistory(long org_id, String case_his_num, SystemIdentity systemIdentity) {
		super(systemIdentity);
		this.org_id = org_id;
		this.case_his_num = case_his_num;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSymptom() {
		return symptom;
	}

	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}

	@JSON(serialize = false)
	public String getEaf_list() {
		return eaf_list;
	}

	@JSON(deserialize = false)
	public void setEaf_list(String eaf_list) {
		this.eaf_list = eaf_list;
	}

	public long getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(long patient_id) {
		this.patient_id = patient_id;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	// public String getPacs_case_num() {
	// return pacs_case_num;
	// }
	//
	// public void setPacs_case_num(String pacs_case_num) {
	// this.pacs_case_num = pacs_case_num;
	// }

	public TCaseHistory formatPropertys() {
		return null;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@JSON(serialize = false)
	public String getDiagnoses() {
		return this.symptom;
	}

	@Override
	@JSON(serialize = false)
	public PatientDataType getPdType() {
		return PatientDataType.CASE_HIS;
	}

	public Date getEnter_time() {
		return enter_time;
	}

	public void setEnter_time(Date enter_time) {
		this.enter_time = enter_time;
	}

	public Date getLeave_time() {
		return leave_time;
	}

	public void setLeave_time(Date leave_time) {
		this.leave_time = leave_time;
	}

	public String getReception_section() {
		return reception_section;
	}

	public void setReception_section(String reception_section) {
		this.reception_section = reception_section;
	}

	public String getReception_doctor() {
		return reception_doctor;
	}

	public void setReception_doctor(String reception_doctor) {
		this.reception_doctor = reception_doctor;
	}

	public String getHospitalized_num() {
		return hospitalized_num;
	}

	public void setHospitalized_num(String hospitalized_num) {
		this.hospitalized_num = hospitalized_num;
	}

	public int getType() {
		return type;
	}

	public String getTypeStr() {
		CaseHistoryType cht = CaseHistoryType.parseCode(type);
		return cht == null ? "" : cht.getName();
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public long getModify_user_id() {
		return modify_user_id;
	}

	public void setModify_user_id(long modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}
	
	@Override
	public String toString() {
		return "TCaseHistory [id=" + id + ", patient_id=" + patient_id + ", org_id=" + org_id + ", case_his_num="
				+ case_his_num + ", type=" + type + ", symptom=" + symptom + ", diagnosis=" + diagnosis
				+ ", patient_say=" + patient_say + ", check_body=" + check_body + ", doctor_advice=" + doctor_advice
				+ ", guahao_type=" + guahao_type + ", enter_time=" + enter_time + ", leave_time=" + leave_time
				+ ", status=" + status + ", create_time=" + create_time + ", create_user_id=" + create_user_id
				+ ", modify_time=" + modify_time + ", modify_user_id=" + modify_user_id + ", note=" + note
				+ ", hospitalized_num=" + hospitalized_num + ", reception_section=" + reception_section
				+ ", reception_doctor=" + reception_doctor + ", eaf_list=" + eaf_list + ", state=" + state + "]";
	}

	public String getPatient_say() {
		return patient_say;
	}

	public void setPatient_say(String patient_say) {
		this.patient_say = patient_say;
	}

	public String getDoctor_advice() {
		return doctor_advice;
	}

	public void setDoctor_advice(String doctor_advice) {
		this.doctor_advice = doctor_advice;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getGuahao_type() {
		return guahao_type;
	}

	public void setGuahao_type(String guahao_type) {
		this.guahao_type = guahao_type;
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		this.case_his_num = jsonObject.optString("case_his_num", "");
		this.create_time = DateTools.strToDate(jsonObject.optString("create_time"));
		this.diagnosis = jsonObject.optString("diagnosis", "");
		this.doctor_advice = jsonObject.optString("doctor_advice", "");
		this.enter_time = DateTools.strToDate(jsonObject.optString("enter_time"), new Date());
		this.guahao_type = jsonObject.optString("guahao_type", "");
		this.hospitalized_num = jsonObject.optString("hospitalized_num", "");
		this.leave_time = DateTools.strToDate(jsonObject.optString("leave_time"), new Date());
		this.note = jsonObject.optString("note", "");
		this.patient_say = jsonObject.optString("patient_say", "");
		this.reception_doctor = jsonObject.optString("reception_doctor", "");
		this.reception_section = jsonObject.optString("reception_section", "");
		this.symptom = jsonObject.optString("symptom", "");
		this.type = jsonObject.optInt("type", CaseHistoryType.OTHER.getCode());
	}

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();
		r.put("case_his_num", this.case_his_num);
		r.put("check_body", this.check_body);
		r.put("create_time", DateTools.dateToString(this.create_time));
		r.put("diagnosis", this.diagnosis);
		r.put("doctor_advice", this.doctor_advice);
		r.put("enter_time", DateTools.dateToString(this.enter_time));
		r.put("guahao_type", this.guahao_type);
		r.put("hospitalized_num", hospitalized_num);
		r.put("leave_time", DateTools.dateToString(this.leave_time));
		r.put("note", note);
		r.put("patient_say", this.patient_say);
		r.put("reception_doctor", this.reception_doctor);
		r.put("reception_section", reception_section);
		r.put("symptom", this.symptom);
		r.put("type", this.type);
		return r;
	}

	@Override
	protected void replaceFrom(AbstractRemoteEntity entity) {
		TCaseHistory caseHistory = (TCaseHistory) entity;
		if (!StringTools.isEmpty(caseHistory.case_his_num))
			this.case_his_num = caseHistory.case_his_num;
		if (!StringTools.isEmpty(caseHistory.check_body))
			this.check_body = caseHistory.check_body;
		if (caseHistory.create_time != null)
			this.create_time = caseHistory.create_time;
		if (!StringTools.isEmpty(caseHistory.diagnosis))
			this.diagnosis = caseHistory.diagnosis;
		if (!StringTools.isEmpty(caseHistory.doctor_advice))
			this.doctor_advice = caseHistory.doctor_advice;
		if (caseHistory.enter_time != null)
			this.enter_time = caseHistory.enter_time;
		if (!StringTools.isEmpty(caseHistory.guahao_type))
			this.guahao_type = caseHistory.guahao_type;
		if (!StringTools.isEmpty(caseHistory.hospitalized_num))
			this.hospitalized_num = caseHistory.hospitalized_num;
		if (caseHistory.leave_time != null)
			this.leave_time = caseHistory.leave_time;
		if (!StringTools.isEmpty(caseHistory.note))
			this.note = caseHistory.note;
		if (!StringTools.isEmpty(caseHistory.patient_say))
			this.patient_say = caseHistory.patient_say;
		if (!StringTools.isEmpty(caseHistory.reception_doctor))
			this.reception_doctor = caseHistory.reception_doctor;
		if (!StringTools.isEmpty(caseHistory.reception_section))
			this.reception_section = caseHistory.reception_section;
		if (!StringTools.isEmpty(caseHistory.symptom))
			this.symptom = caseHistory.symptom;
		if (CaseHistoryType.parseCode(caseHistory.type) != null)
			this.type = caseHistory.type;
	}

	@Override
	protected void mergeFrom(AbstractRemoteEntity re) {
		TCaseHistory caseHistory = (TCaseHistory) re;
		if (StringTools.isEmpty(this.case_his_num))
			this.case_his_num = caseHistory.case_his_num;
		if (StringTools.isEmpty(this.check_body))
			this.check_body = caseHistory.check_body;
		if (this.enter_time == null)
			this.enter_time = caseHistory.enter_time;
		if (StringTools.isEmpty(this.hospitalized_num))
			this.hospitalized_num = caseHistory.hospitalized_num;
		if (this.leave_time == null)
			this.leave_time = caseHistory.leave_time;
		if (this.create_time == null)
			this.create_time = caseHistory.create_time;
		if (StringTools.isEmpty(this.reception_doctor))
			this.reception_doctor = caseHistory.reception_doctor;
		if (StringTools.isEmpty(this.reception_section))
			this.reception_section = caseHistory.reception_section;
		if (CaseHistoryType.parseCode(type) == null)
			this.type = caseHistory.type;
		if (StringTools.isEmpty(this.symptom))
			this.symptom = caseHistory.symptom;
		else
			this.symptom = StringTools.concat(this.symptom, "\r\n", caseHistory.symptom);
		if (StringTools.isEmpty(this.note))
			this.note = caseHistory.note;
		if (!StringTools.isEmpty(this.diagnosis))
			this.diagnosis = caseHistory.diagnosis;
		if (!StringTools.isEmpty(this.patient_say))
			this.patient_say = caseHistory.patient_say;
		if (!StringTools.isEmpty(this.doctor_advice))
			this.doctor_advice = caseHistory.doctor_advice;
		if (!StringTools.isEmpty(this.guahao_type))
			this.guahao_type = caseHistory.guahao_type;
	}

	public static TCaseHistory findByCaseHisNumAndType(List<TCaseHistory> listCaseHostory, String case_his_num,
			CaseHistoryType type) {
		if (CollectionTools.isEmpty(listCaseHostory))
			return null;
		if (StringTools.isEmpty(case_his_num))
			return null;
		if (type == null)
			return null;
		for (TCaseHistory tCaseHistory : listCaseHostory) {
			if (case_his_num.equalsIgnoreCase(tCaseHistory.getCase_his_num()))
				if (type.getCode() == tCaseHistory.type)
					return tCaseHistory;
		}
		return null;
	}

	public String getCheck_body() {
		return check_body;
	}

	public void setCheck_body(String check_body) {
		this.check_body = check_body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((case_his_num == null) ? 0 : case_his_num.hashCode());
		result = prime * result + ((check_body == null) ? 0 : check_body.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (create_user_id ^ (create_user_id >>> 32));
		result = prime * result + ((diagnosis == null) ? 0 : diagnosis.hashCode());
		result = prime * result + ((doctor_advice == null) ? 0 : doctor_advice.hashCode());
		result = prime * result + ((eaf_list == null) ? 0 : eaf_list.hashCode());
		result = prime * result + ((enter_time == null) ? 0 : enter_time.hashCode());
		result = prime * result + ((guahao_type == null) ? 0 : guahao_type.hashCode());
		result = prime * result + ((hospitalized_num == null) ? 0 : hospitalized_num.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((leave_time == null) ? 0 : leave_time.hashCode());
		result = prime * result + ((modify_time == null) ? 0 : modify_time.hashCode());
		result = prime * result + (int) (modify_user_id ^ (modify_user_id >>> 32));
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + (int) (org_id ^ (org_id >>> 32));
		result = prime * result + (int) (patient_id ^ (patient_id >>> 32));
		result = prime * result + ((patient_say == null) ? 0 : patient_say.hashCode());
		result = prime * result + ((reception_doctor == null) ? 0 : reception_doctor.hashCode());
		result = prime * result + ((reception_section == null) ? 0 : reception_section.hashCode());
		result = prime * result + state;
		result = prime * result + status;
		result = prime * result + ((symptom == null) ? 0 : symptom.hashCode());
		result = prime * result + type;
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
		TCaseHistory other = (TCaseHistory) obj;
		if (case_his_num == null) {
			if (other.case_his_num != null)
				return false;
		} else if (!case_his_num.equals(other.case_his_num))
			return false;
		if (check_body == null) {
			if (other.check_body != null)
				return false;
		} else if (!check_body.equals(other.check_body))
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (create_user_id != other.create_user_id)
			return false;
		if (diagnosis == null) {
			if (other.diagnosis != null)
				return false;
		} else if (!diagnosis.equals(other.diagnosis))
			return false;
		if (doctor_advice == null) {
			if (other.doctor_advice != null)
				return false;
		} else if (!doctor_advice.equals(other.doctor_advice))
			return false;
		if (eaf_list == null) {
			if (other.eaf_list != null)
				return false;
		} else if (!eaf_list.equals(other.eaf_list))
			return false;
		if (enter_time == null) {
			if (other.enter_time != null)
				return false;
		} else if (!enter_time.equals(other.enter_time))
			return false;
		if (guahao_type == null) {
			if (other.guahao_type != null)
				return false;
		} else if (!guahao_type.equals(other.guahao_type))
			return false;
		if (hospitalized_num == null) {
			if (other.hospitalized_num != null)
				return false;
		} else if (!hospitalized_num.equals(other.hospitalized_num))
			return false;
		if (id != other.id)
			return false;
		if (leave_time == null) {
			if (other.leave_time != null)
				return false;
		} else if (!leave_time.equals(other.leave_time))
			return false;
		if (modify_time == null) {
			if (other.modify_time != null)
				return false;
		} else if (!modify_time.equals(other.modify_time))
			return false;
		if (modify_user_id != other.modify_user_id)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (org_id != other.org_id)
			return false;
		if (patient_id != other.patient_id)
			return false;
		if (patient_say == null) {
			if (other.patient_say != null)
				return false;
		} else if (!patient_say.equals(other.patient_say))
			return false;
		if (reception_doctor == null) {
			if (other.reception_doctor != null)
				return false;
		} else if (!reception_doctor.equals(other.reception_doctor))
			return false;
		if (reception_section == null) {
			if (other.reception_section != null)
				return false;
		} else if (!reception_section.equals(other.reception_section))
			return false;
		if (state != other.state)
			return false;
		if (status != other.status)
			return false;
		if (symptom == null) {
			if (other.symptom != null)
				return false;
		} else if (!symptom.equals(other.symptom))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
