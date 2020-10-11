package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;

import java.util.ArrayList;
import java.util.List;

public class TStudy extends AbstractDicomEntity{
	private String patient_uuid;// 外键，病人ID
	private String study_id;// 医院内部编码，不具备任何意义 （study_id）检查ID.
	private String study_instance_uid; // 检查实例号：唯一标记不同检查的号码.
	private String study_date;// 拍片时间
	private String study_time;// 拍片时间
	private String referring_physician_name;
	private String accession_number;// 检查号：RIS的生成序号,用以标识做检查的次序.
	private String study_description;// 检查描述
	private String referenced_study_sequence;
	private String modalities_in_study; // 检查的部位.
	private String patient_age;// 病人检查时的年龄
	/** 病人身高单位米 Patient's height or length in meters */
	private String patient_size;//病人检查时的身高
	private String patient_weight;//病人检查时的体重
	private int pregnancy_status;//病人检查时的结婚状况
	private String occupation;//病人检查时的职业
	private String sop_classes_in_study;//
	private String other_study_numbers;//
	private String name_of_physicians_reading_study;//Names of the physician(s) reading the Study. 
	private String admitting_diagnoses_description;//Description of the admitting diagnosis (diagnoses) 
	
	private List<TSeries> listSeries = new ArrayList<TSeries>();
	public String getStudy_instance_uid() {
		return study_instance_uid;
	}

	public String getPatient_size() {
		return patient_size;
	}

	public String getPatient_weight() {
		return patient_weight;
	}

	public int getPregnancy_status() {
		return pregnancy_status;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getSop_classes_in_study() {
		return sop_classes_in_study;
	}

	public String getOther_study_numbers() {
		return other_study_numbers;
	}

	public String getName_of_physicians_reading_study() {
		return name_of_physicians_reading_study;
	}

	public String getAdmitting_diagnoses_description() {
		return admitting_diagnoses_description;
	}

	public void setPatient_size(String patient_size) {
		this.patient_size = patient_size;
	}

	public void setPatient_weight(String patient_weight) {
		this.patient_weight = patient_weight;
	}

	public void setPregnancy_status(int pregnancy_status) {
		this.pregnancy_status = pregnancy_status;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setSop_classes_in_study(String sop_classes_in_study) {
		this.sop_classes_in_study = sop_classes_in_study;
	}

	public void setOther_study_numbers(String other_study_numbers) {
		this.other_study_numbers = other_study_numbers;
	}

	public void setName_of_physicians_reading_study(String name_of_physicians_reading_study) {
		this.name_of_physicians_reading_study = name_of_physicians_reading_study;
	}

	public void setAdmitting_diagnoses_description(String admitting_diagnoses_description) {
		this.admitting_diagnoses_description = admitting_diagnoses_description;
	}

	public String getStudy_id() {
		return study_id;
	}

	public String getStudy_date() {
		return study_date;
	}

	public String getStudy_time() {
		return study_time;
	}

	public String getReferring_physician_name() {
		return referring_physician_name;
	}

	public String getReferenced_study_sequence() {
		return referenced_study_sequence;
	}

	public String getModalities_in_study() {
		return modalities_in_study;
	}

	public String getStudy_description() {
		return study_description;
	}

	public void setStudy_instance_uid(String study_instance_uid) {
		this.study_instance_uid = study_instance_uid;
	}

	public void setStudy_id(String study_id) {
		this.study_id = study_id;
	}

	public void setStudy_date(String study_date) {
		this.study_date = study_date;
	}

	public void setStudy_time(String study_time) {
		this.study_time = study_time;
	}

	public void setReferring_physician_name(String referring_physician_name) {
		this.referring_physician_name = referring_physician_name;
	}

	public void setReferenced_study_sequence(String referenced_study_sequence) {
		this.referenced_study_sequence = referenced_study_sequence;
	}

	public void setModalities_in_study(String modalities_in_study) {
		this.modalities_in_study = modalities_in_study;
	}

	public void setStudy_description(String study_description) {
		this.study_description = study_description;
	}

	public String getAccession_number() {
		return accession_number;
	}

	public void setAccession_number(String accession_number) {
		this.accession_number = accession_number;
	}

	public String getPatient_age() {
		return patient_age;
	}

	public void setPatient_age(String patient_age) {
		this.patient_age = patient_age;
	}

	public String getPatient_uuid() {
		return patient_uuid;
	}

	public void setPatient_uuid(String patient_uuid) {
		this.patient_uuid = patient_uuid;
	}

	@Override
	public void writeFatherUuid(String father_uuid) {
		this.patient_uuid = father_uuid;
	}

	@Override
	public String readFatherUuid() {
		return patient_uuid;
	}

	public void addSeries(TSeries tSeries) {
		if (tSeries == null)
			return;
		this.listSeries.add(tSeries);
	}

	public List<TSeries> getSerieses() {
		return this.listSeries;
	}
}
