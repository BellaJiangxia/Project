package com.vastsoft.yingtai.module.diagnosis2.entity;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestUrgentLevel;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.utils.annotation.IgnoreNull;

public class TDiagnosis implements Cloneable {
	private long id;//
	private long case_his_id;// 病例ID
	private long request_user_id;// 申请用户ID
	private long request_org_id;// 申请机构ID
	private long diagnosis_org_id;// 诊断机构ID
	private long diagnosis_product_id;// 诊断产品ID
	private Date handle_time;// 处理时间
	private int status;// DiagnosisStatus
	private Date create_time;// 创建时间
	private String note;// 备注
	private long dicom_img_device_type_id; // 设备ID
	// private long dicom_img_part_type_id;// 部位ID
	private long accept_user_id;// 第一个接受诊断的用户ID
	private long verify_user_id;// 审核用户ID
	private long publish_report_org_id; // 发布报告的机构ID
	private long dicom_img_id;// 病例图像ID
	private long curr_handle_id;// 当前处理
	private String about_case_ids;// 关联的其他病例ID
	
	private String product_name;//诊断产品的名称
	private int product_charge_type;//诊断产品的计费方式
	private int charge_amount;//诊断产品的计费数量
	private double product_unit_price;//产品单价
	
	private double total_price;//涉及金额
	
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
	
	private long patient_id;//病人ID
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

	private int allow_reporter_publish_report;// 允许以诊断机构身份发布报告 1-是，0-否
	
	private int request_class; //申请类别
	private int urgent_level;//紧急程度
	
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
	// public List<String> getPic_conclusions() {
	// if (pic_conclusion == null)
	// return null;
	// String pco = pic_conclusion.replace("\r\n", "<p>");
	// pco = pco.replace("\n", "<p>");
	// return CommonTools.StrArrayTrim(pco.split("<p>"));
	// }
	//
	// public List<String> getPic_opinions() {
	// if (pic_opinion == null)
	// return null;
	// String pco = pic_opinion.replace("\r\n", "<p>");
	// pco = pco.replace("\n", "<p>");
	// return CommonTools.StrArrayTrim(pco.split("<p>"));
	// }

	public void setId(long id) {
		this.id = id;
	}

	public long getRequest_user_id() {
		return request_user_id;
	}

	public void setRequest_user_id(long request_user_id) {
		this.request_user_id = request_user_id;
	}

	public long getRequest_org_id() {
		return request_org_id;
	}

	public void setRequest_org_id(long request_org_id) {
		this.request_org_id = request_org_id;
	}

	public long getDiagnosis_org_id() {
		return diagnosis_org_id;
	}

	public void setDiagnosis_org_id(long diagnosis_org_id) {
		this.diagnosis_org_id = diagnosis_org_id;
	}

	// public Date getRequest_time() {
	// return request_time;
	// }
	// public void setRequest_time(Date request_time) {
	// this.request_time = request_time;
	// }
	@IgnoreNull
	public Date getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(Date handle_time) {
		this.handle_time = handle_time;
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

	@IgnoreNull
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getDiagnosis_product_id() {
		return diagnosis_product_id;
	}

	public void setDiagnosis_product_id(long diagnosis_product_id) {
		this.diagnosis_product_id = diagnosis_product_id;
	}

	@IgnoreNull
	public long getVerify_user_id() {
		return verify_user_id;
	}

	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	// @IgnoreNull
	// public String getPic_opinion() {
	// return pic_opinion;
	// }
	//
	// public void setPic_opinion(String pic_opinion) {
	// this.pic_opinion = pic_opinion;
	// }
	//
	// @IgnoreNull
	// public String getPic_conclusion() {
	// return pic_conclusion;
	// }
	//
	// public void setPic_conclusion(String pic_conclusion) {
	// this.pic_conclusion = pic_conclusion;
	// }
	//
	// @IgnoreNull
	// public Date getReport_time() {
	// return report_time;
	// }
	//
	// public String getReport_timeStr() {
	// return DateTools.dateToString(report_time);
	// }
	//
	// public void setReport_time(Date report_time) {
	// this.report_time = report_time;
	// }

	public String getStatusStr() {
		return DiagnosisStatus2.parseFrom(status).getName();
	}

	public long getCurr_handle_id() {
		return curr_handle_id;
	}

	public void setCurr_handle_id(long curr_handle_id) {
		this.curr_handle_id = curr_handle_id;
	}

	public long getAccept_user_id() {
		return accept_user_id;
	}

	public void setAccept_user_id(long accept_user_id) {
		this.accept_user_id = accept_user_id;
	}

	// public int getF_o_m() {
	// return f_o_m == 1 || f_o_m == 2 ? f_o_m : 1;
	// }
	//
	// public String getF_o_mStr() {
	// DiagnosisFomType2 fom = DiagnosisFomType2.parseFrom(f_o_m);
	// return fom == null ? "无" : fom.getName();
	// }
	//
	// public void setF_o_m(int f_o_m) {
	// this.f_o_m = f_o_m;
	// }
	//
	// public long getPrint_user_id() {
	// return print_user_id;
	// }
	//
	// public void setPrint_user_id(long print_user_id) {
	// this.print_user_id = print_user_id;
	// }
	//
	// public int getPrint_times() {
	// return print_times;
	// }
	//
	// public void setPrint_times(int print_times) {
	// this.print_times = print_times;
	// }
	//
	// public Date getPrint_time() {
	// return print_time;
	// }
	//
	// public String getPrint_timeStr() {
	// return DateTools.dateToString(print_time);
	// }
	//
	// public void setPrint_time(Date print_time) {
	// this.print_time = print_time;
	// }
	//
	// public long getView_user_id() {
	// return view_user_id;
	// }
	//
	// public void setView_user_id(long view_user_id) {
	// this.view_user_id = view_user_id;
	// }


	public long getCase_his_id() {
		return case_his_id;
	}

	public long getDicom_img_device_type_id() {
		return dicom_img_device_type_id;
	}

	// public long getDicom_img_part_type_id() {
	// return dicom_img_part_type_id;
	// }

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getProduct_charge_type() {
		return product_charge_type;
	}
	
	public String getProduct_charge_type_name() {
		OrgProductChargeType opct = OrgProductChargeType.parseCode(this.product_charge_type);
		return opct == null ? "" : opct.getName();
	}

	public void setProduct_charge_type(int product_charge_type) {
		this.product_charge_type = product_charge_type;
	}

	public int getCharge_amount() {
		return charge_amount;
	}

	public void setCharge_amount(int charge_amount) {
		this.charge_amount = charge_amount;
	}

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

	public String getAbout_case_ids() {
		return about_case_ids;
	}

	public void setAbout_case_ids(String about_case_ids) {
		this.about_case_ids = about_case_ids;
	}

	public int getAllow_reporter_publish_report() {
		return allow_reporter_publish_report;
	}

	public void setAllow_reporter_publish_report(int allow_reporter_publish_report) {
		this.allow_reporter_publish_report = allow_reporter_publish_report;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public long getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(long patient_id) {
		this.patient_id = patient_id;
	}

	public int getRequest_class() {
		return request_class;
	}

	public void setRequest_class(int request_class) {
		this.request_class = request_class;
	}

	public int getUrgent_level() {
		return urgent_level;
	}
	
	public String getUrgent_level_name() {
		RequestUrgentLevel rul = RequestUrgentLevel.parseCode(urgent_level);
		return rul==null?"":rul.getName();
	}

	public void setUrgent_level(int urgent_level) {
		this.urgent_level = urgent_level;
	}

	public double getProduct_unit_price() {
		return product_unit_price;
	}

	public void setProduct_unit_price(double product_unit_price) {
		this.product_unit_price = product_unit_price;
	}

	@Override
	public String toString() {
		return "TDiagnosis [id=" + id + ", case_his_id=" + case_his_id + ", request_user_id=" + request_user_id
				+ ", request_org_id=" + request_org_id + ", diagnosis_org_id=" + diagnosis_org_id
				+ ", diagnosis_product_id=" + diagnosis_product_id + ", handle_time=" + handle_time + ", status="
				+ status + ", create_time=" + create_time + ", note=" + note + ", dicom_img_device_type_id="
				+ dicom_img_device_type_id + ", accept_user_id=" + accept_user_id + ", verify_user_id=" + verify_user_id
				+ ", publish_report_org_id=" + publish_report_org_id + ", dicom_img_id=" + dicom_img_id
				+ ", curr_handle_id=" + curr_handle_id + ", about_case_ids=" + about_case_ids + ", product_name="
				+ product_name + ", product_charge_type=" + product_charge_type + ", charge_amount=" + charge_amount
				+ ", product_unit_price=" + product_unit_price + ", case_his_num=" + case_his_num + ", case_symptom="
				+ case_symptom + ", case_enter_time=" + case_enter_time + ", case_leave_time=" + case_leave_time
				+ ", case_hospitalized_num=" + case_hospitalized_num + ", case_reception_section="
				+ case_reception_section + ", case_reception_doctor=" + case_reception_doctor + ", case_eaf_list="
				+ case_eaf_list + ", case_source_type=" + case_source_type + ", case_type=" + case_type
				+ ", patient_id=" + patient_id + ", patient_name=" + patient_name + ", patient_identity_id="
				+ patient_identity_id + ", patient_gender=" + patient_gender + ", patient_birthday=" + patient_birthday
				+ ", patient_home_address=" + patient_home_address + ", patient_born_address=" + patient_born_address
				+ ", patient_work=" + patient_work + ", patient_sick_his=" + patient_sick_his + ", patient_source_type="
				+ patient_source_type + ", dicom_img_mark_char=" + dicom_img_mark_char + ", dicom_img_checklist_num="
				+ dicom_img_checklist_num + ", dicom_img_check_time=" + dicom_img_check_time + ", dicom_img_check_pro="
				+ dicom_img_check_pro + ", allow_reporter_publish_report=" + allow_reporter_publish_report
				+ ", request_class=" + request_class + ", urgent_level=" + urgent_level + "]";
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	
	
}
