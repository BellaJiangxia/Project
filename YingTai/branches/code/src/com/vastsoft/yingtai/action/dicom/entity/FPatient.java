package com.vastsoft.yingtai.action.dicom.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.constants.Gender;

public class FPatient implements Serializable{
	private static final long serialVersionUID = -7214743217324244818L;
	private String medical_his_num;
	private String patient_name;
	private String person_identity;
	private Date birthday;
	private String sex;

	private List<FDicom> dicom_list;

	public String getMedical_his_num()
	{
		return medical_his_num;
	}

	public void setMedical_his_num(String medical_his_num)
	{
		this.medical_his_num = medical_his_num;
	}

	public String getPatient_name()
	{
		return patient_name==null?"":patient_name+" ";
	}

	public void setPatient_name(String patient_name)
	{
		this.patient_name = patient_name;
	}

	public String getPerson_identity()
	{
		return person_identity;
	}

	public void setPerson_identity(String person_identity)
	{
		this.person_identity = person_identity;
	}

	public List<FDicom> getDicom_list()
	{
		return dicom_list;
	}

	public void setDicom_list(List<FDicom> dicom_list)
	{
		this.dicom_list = dicom_list;
	}
	public Date getBirthday() {
		return birthday;
	}
	
	public String getBirthdayStr() {
		return DateTools.dateToString(birthday, DateTools.TimeExactType.DAY);
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public void setBirthdayStr(String birthday) {
		this.birthday = DateTools.strToDate(birthday);
	}
	
	public int getAge() {
		return DateTools.getAgeByBirthday(this.birthday);
	}

	public boolean isWhole(){
//		return CommonTools.wasValid(this, false);
		if (this.birthday==null)
			return false;
		if (CollectionTools.isEmpty(this.dicom_list))
			return false;
		if (StringTools.isEmpty(this.medical_his_num))
			return false;
		if (StringTools.isEmpty(this.patient_name))
			return false;
		if (StringTools.wasIdentityId(this.person_identity))
			return false;
		if (Gender.parseString(this.sex, null)==null)
			return false;
		return true;
	}
	public void fillProperty(Patient patient){
		if (patient==null)return;
		if (this.birthday==null)
			this.birthday=DateTools.strToDate(patient.getBirth_date()+"");
		if (!StringTools.isEmpty(this.patient_name))
			this.patient_name=patient.getPtn_name();
		if (!StringTools.isEmpty(this.sex)) {
			this.sex=patient.getSex();
		}
//		if (!CommonTools.wasValid(this.person_identity))
//			this.person_identity=patient.getPtn_id();
	}
	
	public static FPatient fillProperty(List<FPatient> listPatient){
		if (listPatient==null||listPatient.isEmpty())
			return null;
		FPatient ppp=new FPatient();
		for (FPatient fPatient : listPatient) {
			if (ppp.getSex()==null)
				ppp.setSex(fPatient.getSex());
			if (ppp.getBirthday()==null)
				ppp.setBirthday(fPatient.getBirthday());
			if (ppp.getMedical_his_num()==null||ppp.getMedical_his_num().trim().isEmpty())
				ppp.setMedical_his_num(fPatient.getMedical_his_num());
			if (ppp.getPatient_name()==null||ppp.getPatient_name().trim().isEmpty())
				ppp.setPatient_name(fPatient.getPatient_name());
			if (ppp.getPerson_identity()==null||ppp.getPerson_identity().trim().isEmpty())
				ppp.setPerson_identity(fPatient.getPerson_identity());
			if (fPatient.getDicom_list()!=null&&fPatient.getDicom_list().size()>0) {
				if (ppp.getDicom_list()==null)ppp.setDicom_list(new ArrayList<FDicom>());
				for (FDicom fDicom : fPatient.getDicom_list()) {
					if (!ppp.getDicom_list().contains(fDicom)) {
						ppp.getDicom_list().add(fDicom);
					}
				}
			}
		}
		return ppp;
	}

	public void addDicom(FDicom fdicom) {
		if (fdicom==null)return;
		if (this.dicom_list==null)
			this.dicom_list=new ArrayList<FDicom>();
		this.dicom_list.add(fdicom);
	}

	public String getSex() {
		return sex==null?null:sex.toUpperCase();
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
