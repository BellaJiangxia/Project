package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TPatient extends AbstractDicomEntity{
	private String patient_id; // 病人ID
	private String patient_name; // 病人姓名
	private String patient_sex; // 性别
	private String patient_birthdate;// 生日
	private String patient_birth_time; 
	private String patient_comments;//注释
	/**
	 * Identifier of the Assigning Authority (system, organization, agency,or
	 * department) that issued the Patient ID. Note Equivalent to HL7 v2 CX
	 * component 4 subcomponent 1.
	 */
	private String issuer_of_patient_id;
	/**
	 * Uniquely identifies the Patient SOP Instance that relates to the Visit
	 * SOP Instance. Only a single Item shall be included in this Sequence.
	 */
	private String referenced_patient_sequence;
	/** Other identification numbers or codes used to identify the patient. */
	private String other_patient_ids;
	/** 曾用名 */
	private String other_patient_names;
	/** 民族 */
	private String ethnic_group;
	/** 病人的其他病史 Additional information about the Patient's medical history. */
	private String additional_patient_history;
	
	private List<TStudy> listStudy = new ArrayList<TStudy>();
	
	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public String getPatient_sex() {
		return patient_sex;
	}

	public void setPatient_sex(String patient_sex) {
		this.patient_sex = patient_sex;
	}

	public String getPatient_birthdate() {
		return patient_birthdate;
	}

	public void setPatient_birthdate(String patient_birthdate) {
		this.patient_birthdate = patient_birthdate;
	}

	public String getPatient_birth_time() {
		return patient_birth_time;
	}

	public void setPatient_birth_time(String patient_birth_time) {
		this.patient_birth_time = patient_birth_time;
	}

	public String getPatient_comments() {
		return patient_comments;
	}

	public void setPatient_comments(String patient_comments) {
		this.patient_comments = patient_comments;
	}

	@Override
	public void writeFatherUuid(String father_uuid) {
	}

	@Override
	public String readFatherUuid() {
		return null;
	}

//	@Override
//	public void valueOf(Attributes attrs) throws DcmStorageException {
//		this.patient_id = attrs.getString(Tag.PatientID,"");
//		this.patient_name = attrs.getString(Tag.PatientName,"");
//		this.issuer_of_patient_id = attrs.getString(Tag.IssuerOfPatientID);
//		this.referenced_patient_sequence = attrs.getString(Tag.ReferencedPatientSequence,"");
//		this.patient_birthdate = attrs.getString(Tag.PatientBirthDate,"");
//		this.patient_birth_time = attrs.getString(Tag.PatientBirthTime,"");
//		this.patient_sex = attrs.getString(Tag.PatientSex, "M");
//		this.patient_comments = attrs.getString(Tag.PatientComments,"");
//		this.other_patient_ids = attrs.getString(Tag.OtherPatientIDs,"");
//		this.other_patient_names = attrs.getString(Tag.OtherPatientNames,"");
//		this.ethnic_group = attrs.getString(Tag.EthnicGroup,"");
//		this.additional_patient_history = attrs.getString(Tag.AdditionalPatientHistory,"");
//	}
//
//	@Override
//	public Attributes toAttrs() throws DcmStorageException {
//		Attributes result = new Attributes();
//		result.setString(Tag.PatientID, VR.LO, this.patient_id);
//		result.setString(Tag.PatientName, VR.PN, this.patient_name);
//		result.setString(Tag.IssuerOfPatientID, VR.LO, this.issuer_of_patient_id);
//		result.setString(Tag.ReferencedPatientSequence, VR.SQ, this.referenced_patient_sequence);
//		result.setString(Tag.PatientBirthDate, VR.DA, this.patient_birthdate);
//		result.setString(Tag.PatientBirthTime, VR.TM, this.patient_birth_time);
//		result.setString(Tag.PatientSex, VR.CS, this.patient_sex);
//		result.setString(Tag.PatientComments, VR.LT, this.patient_comments);
//		result.setString(Tag.OtherPatientIDs, VR.LO, this.other_patient_ids);
//		result.setString(Tag.OtherPatientNames, VR.PN, this.other_patient_names);
//		result.setString(Tag.EthnicGroup, VR.SH, this.ethnic_group);
//		result.setString(Tag.AdditionalPatientHistory, VR.LT, this.additional_patient_history);
//		return result;
//	}

	public String getIssuer_of_patient_id() {
		return issuer_of_patient_id;
	}

	public String getReferenced_patient_sequence() {
		return referenced_patient_sequence;
	}

	public String getOther_patient_ids() {
		return other_patient_ids;
	}

	public String getOther_patient_names() {
		return other_patient_names;
	}

	public String getEthnic_group() {
		return ethnic_group;
	}

	public String getAdditional_patient_history() {
		return additional_patient_history;
	}

	public void setIssuer_of_patient_id(String issuer_of_patient_id) {
		this.issuer_of_patient_id = issuer_of_patient_id;
	}

	public void setReferenced_patient_sequence(String referenced_patient_sequence) {
		this.referenced_patient_sequence = referenced_patient_sequence;
	}

	public void setOther_patient_ids(String other_patient_ids) {
		this.other_patient_ids = other_patient_ids;
	}

	public void setOther_patient_names(String other_patient_names) {
		this.other_patient_names = other_patient_names;
	}

	public void setEthnic_group(String ethnic_group) {
		this.ethnic_group = ethnic_group;
	}

	public void setAdditional_patient_history(String additional_patient_history) {
		this.additional_patient_history = additional_patient_history;
	}

	public void addStudy(TStudy study) {
		if (study == null)return;
		this.listStudy.add(study);
	}

	public List<TStudy> getListStudy() {
		return this.listStudy;
	}
}
