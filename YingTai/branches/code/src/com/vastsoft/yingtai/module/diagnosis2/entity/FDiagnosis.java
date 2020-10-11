package com.vastsoft.yingtai.module.diagnosis2.entity;

import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.user.constants.UserType;

/** 诊断功能对象 */
public class FDiagnosis extends TDiagnosis {
	// private String ae_title;//AE号
	private String request_user_name;// 申请用户名
	private String accept_user_name;// 接受诊断用户名称
	private String verify_user_name;// 审核员用户名称
	private String curr_headle_user_name;// 当前正在处理的医生名称
	private int request_user_type;// 申请用户类型
	private String device_name;// 设备名称
	private String product_description;// 产品描述
	private double sys_deduct;// 平台提成
	private String publish_org_name;// 发布报告机构名称
	private String request_org_name;// 申请机构名称
	private String diagnosis_org_name;// 诊断机构名称
	private String print_user_name;// 打印报告的人的名称

	private String request_tranfer_org_name_path;// 申请流转的机构路径

	private List<TFile> eaf_file_list;
	private List<FSeries> listSeries;
	private TDicomImg dicomImg;
	private TPatient patient;
	private TCaseHistory caseHistory;

	// public String getAe_title() {
	// return ae_title;
	// }
	public String getRequest_user_name() {
		return request_user_name;
	}

	public String getRequest_user_type() {
		UserType userType = UserType.parseCode(request_user_type);
		return userType == null ? "" : userType.getName();
	}

	public String getProduct_description() {
		return product_description;
	}

	public String getPublish_org_name() {
		return publish_org_name;
	}

	public String getRequest_org_name() {
		return request_org_name;
	}

	public String getDiagnosis_org_name() {
		return diagnosis_org_name;
	}

	// public void setAe_title(String ae_title) {
	// this.ae_title = ae_title;
	// }
	public void setRequest_user_name(String request_user_name) {
		this.request_user_name = request_user_name;
	}

	public void setRequest_user_type(int request_user_type) {
		this.request_user_type = request_user_type;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public void setPublish_org_name(String publish_org_name) {
		this.publish_org_name = publish_org_name;
	}

	public void setRequest_org_name(String request_org_name) {
		this.request_org_name = request_org_name;
	}

	public void setDiagnosis_org_name(String diagnosis_org_name) {
		this.diagnosis_org_name = diagnosis_org_name;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getAccept_user_name() {
		return accept_user_name;
	}

	public void setAccept_user_name(String accept_user_name) {
		this.accept_user_name = accept_user_name;
	}

	public String getVerify_user_name() {
		return verify_user_name;
	}

	public void setVerify_user_name(String verify_user_name) {
		this.verify_user_name = verify_user_name;
	}

	public double getSys_deduct() {
		return sys_deduct;
	}

	public void setSys_deduct(double sys_deduct) {
		this.sys_deduct = sys_deduct;
	}

	public String getPrint_user_name() {
		return print_user_name;
	}

	public void setPrint_user_name(String print_user_name) {
		this.print_user_name = print_user_name;
	}

	public String getCurr_headle_user_name() {
		return curr_headle_user_name;
	}

	public void setCurr_headle_user_name(String curr_headle_user_name) {
		this.curr_headle_user_name = curr_headle_user_name;
	}

	@JSON(deserialize = false)
	public void setEaf_file_list(List<TFile> listFile) {
		this.eaf_file_list = listFile;
	}

	public List<TFile> getEaf_file_list() {
		return eaf_file_list;
	}

	public String getRequest_tranfer_org_name_path() {
		return request_tranfer_org_name_path;
	}

	public void setRequest_tranfer_org_name_path(String request_tranfer_org_name_path) {
		this.request_tranfer_org_name_path = request_tranfer_org_name_path;
	}

	public List<FSeries> getListSeries() {
		return listSeries;
	}

	public void setListSeries(List<FSeries> listSeries) {
		this.listSeries = listSeries;
	}

	public TPatient getPatient() {
		return patient;
	}

	public void setPatient(TPatient patient) {
		this.patient = patient;
	}

	public TCaseHistory getCaseHistory() {
		return caseHistory;
	}

	public void setCaseHistory(TCaseHistory caseHistory) {
		this.caseHistory = caseHistory;
	}

	public TDicomImg getDicomImg() {
		return dicomImg;
	}

	public void setDicomImg(TDicomImg dicomImg) {
		this.dicomImg = dicomImg;
	}
}
