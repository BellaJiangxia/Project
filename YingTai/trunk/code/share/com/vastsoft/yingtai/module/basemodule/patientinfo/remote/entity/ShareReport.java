package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.annotation.IgnoreNull;
import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareSystemIdentity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportFomType;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestUrgentLevel;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class ShareReport extends AbstractShareEntity {
	private long id;//
	private long diagnosis_id;// 诊断ID
	private long case_his_id;// 病例ID
	// private String sys_his_id;//his系统的ID
	private Date create_time;// 创建时间
	private long publish_user_id;// 发布者ID
	private String note;// 备注
	private long dicom_img_device_type_id; // 设备ID
	// private long dicom_img_part_type_id;// 部位ID
	private String pic_opinion;// 影像所见
	private String pic_conclusion;// 诊断意见
	private int f_o_m;// 阴阳性
	private long publish_report_org_id; // 发布报告的机构ID
	private long dicom_img_id;// 图像ID
	private long print_user_id;// 第一次打印报告人
	private int print_times;// 报告被打印次数
	private Date print_time;// 第一次打印时间
	private long view_user_id;// 报告第一次被查看的人的ID;

	private long duty_report_user_id;// 责任报告员--仅当作为诊断报告时有效
	private long duty_verify_user_id;// 责任审核员--仅当作为诊断报告时有效
	private long duty_org_id;// 责任机构--仅当作为诊断报告时有效

	private String case_his_num;// 病历号
	private String case_symptom;// 病例症状
	private Date case_enter_time;// 病例入院时间
	private Date case_leave_time;// 病例出院时间
	private String case_hospitalized_num;// 病例住院号
	private String case_reception_section;// 病例接诊科室
	private String case_reception_doctor;// 病例接诊医生
	private String case_eaf_list;// 病例检查单；文件
	private int case_source_type;// 病例来源类型
	private int case_type;// 病例类型

	private String patient_name;// 病人姓名
	private String patient_identity_id;// 病人身份证号
	private int patient_gender;// 病人性别
	private Date patient_birthday;// 病人生日
	private String patient_home_address;// 病人家庭住址
	private String patient_born_address;// 病人出生地
	private String patient_work;// 病人职业
	private String patient_sick_his;// 病人其他病史
	private int patient_source_type;// 病人内院类型
	// private String dicom_img_thumbnail_uid;// 影像缩略图uid
	private String dicom_img_mark_char;// 影像唯一标识
	private String dicom_img_checklist_num;// 影像检查单号
	private Date dicom_img_check_time;// 影像检查时间
	private String dicom_img_check_pro;// 影像检查项目
	private int type;// 报告类型
	private int request_class;// 申请类别
	private int request_urgent_level;//诊断申请紧急程度

	public ShareReport() {
		super();
	}

	public ShareReport(ShareExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public ShareReport(ShareSystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	@IgnoreNull
	public long getId() {
		return id;
	}

	public String getPatient_genderStr() {
		Gender gen = Gender.parseCode(patient_gender);
		return gen == null ? "" : gen.getName();
	}

	public int getPatient_age() {
		return DateTools.getAgeByBirthday(patient_birthday);
	}

	public List<String> getPic_conclusions() {
		if (pic_conclusion == null)
			return null;
		String pco = pic_conclusion.replace("\r\n", "<p>");
		pco = pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}

	public List<String> getPic_opinions() {
		if (pic_opinion == null)
			return null;
		String pco = pic_opinion.replace("\r\n", "<p>");
		pco = pco.replace("\n", "<p>");
		return StringTools.strArrayTrim(pco.split("<p>"));
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@IgnoreNull
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@IgnoreNull
	public String getPic_opinion() {
		return pic_opinion;
	}

	public void setPic_opinion(String pic_opinion) {
		this.pic_opinion = pic_opinion;
	}

	@IgnoreNull
	public String getPic_conclusion() {
		return pic_conclusion;
	}

	public void setPic_conclusion(String pic_conclusion) {
		this.pic_conclusion = pic_conclusion;
	}

	public int getF_o_m() {
		return f_o_m == 1 || f_o_m == 2 ? f_o_m : 1;
	}

	public String getF_o_mStr() {
		ReportFomType fom = ReportFomType.parseCode(f_o_m);
		return fom == null ? "无" : fom.getName();
	}

	public void setF_o_m(int f_o_m) {
		this.f_o_m = f_o_m;
	}

	public long getPrint_user_id() {
		return print_user_id;
	}

	public void setPrint_user_id(long print_user_id) {
		this.print_user_id = print_user_id;
	}

	public int getPrint_times() {
		return print_times;
	}

	public void setPrint_times(int print_times) {
		this.print_times = print_times;
	}

	public Date getPrint_time() {
		return print_time;
	}

	public String getPrint_timeStr() {
		return DateTools.dateToString(print_time);
	}

	public void setPrint_time(Date print_time) {
		this.print_time = print_time;
	}

	public long getView_user_id() {
		return view_user_id;
	}

	public void setView_user_id(long view_user_id) {
		this.view_user_id = view_user_id;
	}

	public long getCase_his_id() {
		return case_his_id;
	}

	public long getDicom_img_device_type_id() {
		return dicom_img_device_type_id;
	}

	// public long getDicom_img_part_type_id() {
	// return dicom_img_part_type_id;
	// }

	public long getPublish_report_org_id() {
		return publish_report_org_id;
	}

	public long getDicom_img_id() {
		return dicom_img_id;
	}

	public String getCase_his_num() {
		return case_his_num;
	}

	public String getCase_symptom() {
		return case_symptom;
	}

	public Date getCase_enter_time() {
		return case_enter_time;
	}

	public Date getCase_leave_time() {
		return case_leave_time;
	}

	public String getCase_hospitalized_num() {
		return case_hospitalized_num;
	}

	public String getCase_reception_section() {
		return case_reception_section;
	}

	public String getCase_reception_doctor() {
		return case_reception_doctor;
	}

	public String getCase_eaf_list() {
		return case_eaf_list;
	}

	public int getCase_source_type() {
		return case_source_type;
	}

	public int getCase_type() {
		return case_type;
	}

	public String getPatient_name() {
		return patient_name;
	}

	public String getPatient_identity_id() {
		return patient_identity_id;
	}

	public int getPatient_gender() {
		return patient_gender;
	}

	public Date getPatient_birthday() {
		return patient_birthday;
	}

	public String getPatient_home_address() {
		return patient_home_address;
	}

	public String getPatient_born_address() {
		return patient_born_address;
	}

	public String getPatient_work() {
		return patient_work;
	}

	public String getPatient_sick_his() {
		return patient_sick_his;
	}

	public int getPatient_source_type() {
		return patient_source_type;
	}
	//
	// public String getDicom_img_thumbnail_uid() {
	// return dicom_img_thumbnail_uid;
	// }

	public String getDicom_img_mark_char() {
		return dicom_img_mark_char;
	}

	public String getDicom_img_checklist_num() {
		return dicom_img_checklist_num;
	}

	public Date getDicom_img_check_time() {
		return dicom_img_check_time;
	}

	public String getDicom_img_check_pro() {
		return dicom_img_check_pro;
	}

	public void setCase_his_id(long case_his_id) {
		this.case_his_id = case_his_id;
	}

	public void setDicom_img_device_type_id(long dicom_img_device_type_id) {
		this.dicom_img_device_type_id = dicom_img_device_type_id;
	}

	// public void setDicom_img_part_type_id(long dicom_img_part_type_id) {
	// this.dicom_img_part_type_id = dicom_img_part_type_id;
	// }

	public void setPublish_report_org_id(long publish_report_org_id) {
		this.publish_report_org_id = publish_report_org_id;
	}

	public void setDicom_img_id(long dicom_img_id) {
		this.dicom_img_id = dicom_img_id;
	}

	public void setCase_his_num(String case_his_num) {
		this.case_his_num = case_his_num;
	}

	public void setCase_symptom(String case_symptom) {
		this.case_symptom = case_symptom;
	}

	public void setCase_enter_time(Date case_enter_time) {
		this.case_enter_time = case_enter_time;
	}

	public void setCase_leave_time(Date case_leave_time) {
		this.case_leave_time = case_leave_time;
	}

	public void setCase_hospitalized_num(String case_hospitalized_num) {
		this.case_hospitalized_num = case_hospitalized_num;
	}

	public void setCase_reception_section(String case_reception_section) {
		this.case_reception_section = case_reception_section;
	}

	public void setCase_reception_doctor(String case_reception_doctor) {
		this.case_reception_doctor = case_reception_doctor;
	}

	public void setCase_eaf_list(String case_eaf_list) {
		this.case_eaf_list = case_eaf_list;
	}

	public void setCase_source_type(int case_source_type) {
		this.case_source_type = case_source_type;
	}

	public void setCase_type(int case_type) {
		this.case_type = case_type;
	}

	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}

	public void setPatient_identity_id(String patient_identity_id) {
		this.patient_identity_id = patient_identity_id;
	}

	public void setPatient_gender(int patient_gender) {
		this.patient_gender = patient_gender;
	}

	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}

	public void setPatient_home_address(String patient_home_address) {
		this.patient_home_address = patient_home_address;
	}

	public void setPatient_born_address(String patient_born_address) {
		this.patient_born_address = patient_born_address;
	}

	public void setPatient_work(String patient_work) {
		this.patient_work = patient_work;
	}

	public void setPatient_sick_his(String patient_sick_his) {
		this.patient_sick_his = patient_sick_his;
	}

	public void setPatient_source_type(int patient_source_type) {
		this.patient_source_type = patient_source_type;
	}
	//
	// public void setDicom_img_thumbnail_uid(String dicom_img_thumbnail_uid) {
	// this.dicom_img_thumbnail_uid = dicom_img_thumbnail_uid;
	// }

	public void setDicom_img_mark_char(String dicom_img_mark_char) {
		this.dicom_img_mark_char = dicom_img_mark_char;
	}

	public void setDicom_img_checklist_num(String dicom_img_checklist_num) {
		this.dicom_img_checklist_num = dicom_img_checklist_num;
	}

	public void setDicom_img_check_time(Date dicom_img_check_time) {
		this.dicom_img_check_time = dicom_img_check_time;
	}

	public void setDicom_img_check_pro(String dicom_img_check_pro) {
		this.dicom_img_check_pro = dicom_img_check_pro;
	}

	public long getDiagnosis_id() {
		return diagnosis_id;
	}

	public void setDiagnosis_id(long diagnosis_id) {
		this.diagnosis_id = diagnosis_id;
	}

	public long getPublish_user_id() {
		return publish_user_id;
	}

	public void setPublish_user_id(long publish_user_id) {
		this.publish_user_id = publish_user_id;
	}
	//
	// /**通过病人，病例和影像装载报告*/
	// public void buildFromPatientAndCaseAndDicomImg(TPatient patient,
	// TCaseHistory caseHostory, TDicomImg dicomImg) {
	// this.patient_birthday = patient.getBirthday();
	// this.patient_born_address = patient.getBorn_address();
	// this.patient_gender = patient.getGender();
	// this.patient_home_address = patient.getHome_address();
	// this.patient_identity_id = patient.getIdentity_id();
	// this.patient_name = patient.getName();
	// this.patient_sick_his = patient.getSick_his();
	// this.patient_source_type = patient.getSource_type();
	// this.patient_work = patient.getWork();
	//
	// this.case_eaf_list = caseHostory.getEaf_list();
	// this.case_enter_time = caseHostory.getEnter_time();
	// this.case_his_id = caseHostory.getId();
	// this.case_his_num = caseHostory.getCase_his_num();
	// this.case_hospitalized_num = caseHostory.getHospitalized_num();
	// this.case_leave_time = caseHostory.getLeave_time();
	// this.case_reception_doctor = caseHostory.getReception_doctor();
	// this.case_reception_section = caseHostory.getReception_section();
	// this.case_source_type = caseHostory.getSource_type();
	// this.case_symptom = caseHostory.getSymptom();
	// this.case_type = caseHostory.getType();
	//
	// this.setCase_his_id(dicomImg.getCase_id());
	// this.setDicom_img_id(dicomImg.getId());
	// this.setDicom_img_check_pro(dicomImg.getCheck_pro());
	// this.setDicom_img_check_time(dicomImg.getCheck_time());
	// this.setDicom_img_checklist_num(dicomImg.getChecklist_num());
	// this.setDicom_img_device_type_id(dicomImg.getDevice_type_id());
	// this.setDicom_img_mark_char(dicomImg.getMark_char());
	// this.setDicom_img_part_type_id(dicomImg.getPart_type_id());
	// this.setDicom_img_thumbnail_uid(dicomImg.getThumbnail_uid());
	// }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public SharePatientDataType getPdType() {
		return SharePatientDataType.REPORT;
	}

	// public String getSys_his_id() {
	// return sys_his_id;
	// }
	//
	// public void setSys_his_id(String sys_his_id) {
	// this.sys_his_id = sys_his_id;
	// }

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();

		return r;
	}

	@Override
	protected void replaceFrom(AbstractShareEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void mergeFrom(AbstractShareEntity re) {
		ShareReport report = (ShareReport) re;
		if (StringTools.isEmpty(this.case_eaf_list))
			this.case_eaf_list = report.case_eaf_list;
		if (this.case_enter_time == null)
			this.case_enter_time = report.case_enter_time;
		if (this.case_his_id <= 0)
			this.case_his_id = report.case_his_id;
		if (StringTools.isNumber(this.case_his_num))
			this.case_his_num = report.case_his_num;
		if (StringTools.isEmpty(this.case_hospitalized_num))
			this.case_hospitalized_num = report.case_hospitalized_num;
		if (this.case_leave_time == null)
			this.case_leave_time = report.case_leave_time;
		if (StringTools.isEmpty(this.case_reception_doctor))
			this.case_reception_doctor = report.case_reception_doctor;
		if (StringTools.isEmpty(this.case_reception_section))
			this.case_reception_section = report.case_reception_section;
		if (StringTools.isEmpty(this.case_symptom))
			this.case_symptom = report.case_symptom;
		if (this.diagnosis_id <= 0)
			this.diagnosis_id = report.diagnosis_id;
		if (StringTools.isEmpty(this.dicom_img_check_pro))
			this.dicom_img_check_pro = report.dicom_img_check_pro;
		if (this.dicom_img_check_time == null)
			this.dicom_img_check_time = report.dicom_img_check_time;
		if (StringTools.isEmpty(this.dicom_img_checklist_num))
			this.dicom_img_checklist_num = report.dicom_img_checklist_num;
		if (this.dicom_img_device_type_id <= 0)
			this.dicom_img_device_type_id = report.dicom_img_device_type_id;
		if (this.dicom_img_id <= 0)
			this.dicom_img_id = report.dicom_img_id;
		if (StringTools.isEmpty(this.dicom_img_mark_char))
			this.dicom_img_mark_char = report.dicom_img_mark_char;
		// if (this.dicom_img_part_type_id<=0)
		// this.dicom_img_part_type_id = report.dicom_img_part_type_id;
		// if (StringTools.isEmpty(this.dicom_img_thumbnail_uid))
		// this.dicom_img_thumbnail_uid = report.dicom_img_thumbnail_uid;
		if (ReportFomType.parseCode(this.f_o_m) == null)
			this.f_o_m = report.f_o_m;
		if (StringTools.isEmpty(this.note))
			this.note = report.note;
		if (this.patient_birthday == null)
			this.patient_birthday = report.patient_birthday;
		if (StringTools.isEmpty(this.patient_born_address))
			this.patient_born_address = report.patient_born_address;
		if (Gender.parseCode(this.patient_gender) == null)
			this.patient_gender = report.getPatient_gender();
		if (StringTools.isEmpty(this.patient_home_address))
			this.patient_home_address = report.getPatient_home_address();
		if (StringTools.isEmpty(this.patient_identity_id))
			this.patient_identity_id = report.patient_identity_id;
		if (StringTools.isEmpty(this.patient_name))
			this.patient_name = report.patient_name;
		if (StringTools.isEmpty(this.patient_sick_his))
			this.patient_sick_his = report.patient_sick_his;
		if (StringTools.isEmpty(this.patient_work))
			this.patient_work = report.patient_work;
		if (StringTools.isEmpty(this.pic_conclusion))
			this.pic_conclusion = report.pic_conclusion;
		if (StringTools.isEmpty(this.pic_opinion))
			this.pic_opinion = report.pic_opinion;
		if (this.publish_report_org_id <= 0)
			this.publish_report_org_id = report.publish_report_org_id;
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub

	}

	public boolean buildFromDiagnosis(TDiagnosis request) {
		if (request == null)
			return false;
		try {
			this.case_eaf_list = request.getCase_eaf_list();
			this.case_enter_time = request.getCase_enter_time();
			this.case_his_id = request.getCase_his_id();
			this.case_his_num = request.getCase_his_num();
			this.case_hospitalized_num = request.getCase_hospitalized_num();
			this.case_leave_time = request.getCase_leave_time();
			this.case_reception_doctor = request.getCase_reception_doctor();
			this.case_reception_section = request.getCase_reception_section();
			this.case_source_type = request.getCase_source_type();
			this.case_symptom = request.getCase_symptom();
			this.case_type = request.getCase_type();
			this.create_time = new Date();
			this.diagnosis_id = request.getId();
			this.dicom_img_check_pro = request.getDicom_img_check_pro();
			this.dicom_img_check_time = request.getDicom_img_check_time();
			this.dicom_img_checklist_num = request.getDicom_img_checklist_num();
			this.dicom_img_device_type_id = request.getDicom_img_device_type_id();
			this.dicom_img_id = request.getDicom_img_id();
			this.dicom_img_mark_char = request.getDicom_img_mark_char();
			this.note = request.getNote();
			this.patient_birthday = request.getPatient_birthday();
			this.patient_born_address = request.getPatient_born_address();
			this.patient_gender = request.getPatient_gender();
			this.patient_home_address = request.getPatient_home_address();
			this.patient_identity_id = request.getPatient_identity_id();
			this.patient_name = request.getPatient_name();
			this.patient_sick_his = request.getPatient_sick_his();
			this.patient_source_type = request.getPatient_source_type();
			this.patient_work = request.getPatient_work();
			this.publish_report_org_id = request.getPublish_report_org_id();
			this.setSource_type(PatientDataSourceType.YUANZHEN_SYS.getCode());
			this.request_class = request.getRequest_class();
			this.request_urgent_level = request.getUrgent_level();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int getRequest_class() {
		return request_class;
	}

	public void setRequest_class(int request_class) {
		this.request_class = request_class;
	}

	public long getDuty_report_user_id() {
		return duty_report_user_id;
	}

	public void setDuty_report_user_id(long duty_report_user_id) {
		this.duty_report_user_id = duty_report_user_id;
	}

	public long getDuty_verify_user_id() {
		return duty_verify_user_id;
	}

	public void setDuty_verify_user_id(long duty_verify_user_id) {
		this.duty_verify_user_id = duty_verify_user_id;
	}

	public long getDuty_org_id() {
		return duty_org_id;
	}

	public void setDuty_org_id(long duty_org_id) {
		this.duty_org_id = duty_org_id;
	}

	public int getRequest_urgent_level() {
		return request_urgent_level;
	}
	public String getRequest_urgent_level_name() {
		RequestUrgentLevel rul = RequestUrgentLevel.parseCode(request_urgent_level);
		return rul==null?"":rul.getName();
	}
	public void setRequest_urgent_level(int request_urgent_level) {
		this.request_urgent_level = request_urgent_level;
	}
}
