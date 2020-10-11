package com.vastsoft.yingtai.module.diagnosis2.requestTranfer.action;

import java.util.List;

import com.vastsoft.util.common.TimeLimit;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.assist.SearchRequestTranferParam;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.FRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services.RequestTranferService;
import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class RequestTranferAction extends BaseYingTaiAction {
	private Long diagnosis_id, diagnosis_org_id, diagnosis_product_id, relation_org_id, request_tranfer_id;
	private Long new_device_type_id,new_diagnosis_org_id,new_request_org_id;
	private String new_patient_name,new_case_his_num;
	private SplitPageUtil spu;
	private DiagnosisStatus2 new_request_status;
	private Gender new_patient_gender;
	private TimeLimit timelimit;
	
	/** 搜索转交到用户当前机构的诊断申请 */
	public String searchRequestTranferToThisOrg(){
		try {
			Passport passport = takePassport();
			SearchRequestTranferParam srtp = new SearchRequestTranferParam();
			srtp.setNew_diagnosis_org_id(passport.getOrgId());
			srtp.setSpu(spu);
			srtp.setCreate_org_id(new_request_org_id);
			srtp.setNew_request_status(new_request_status);
			srtp.setNew_case_his_num(new_case_his_num);
			srtp.setNew_device_type_id(new_device_type_id);
			srtp.setNew_patient_gender(new_patient_gender);
			srtp.setNew_patient_name(new_patient_name);
			srtp.setTimeLimit(timelimit);
			List<FRequestTranfer> listRequestTranfer = RequestTranferService.instance.searchRequestTranfer(srtp);
			addElementToData("listRequestTranfer", listRequestTranfer);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "用户再次转交诊断申请")
	public String againTranferRequest() {
		try {
			RequestTranferService.instance.againTranferRequestByRequestTranferId(takePassport(), diagnosis_id,
					diagnosis_org_id, diagnosis_product_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "撤销本机构转交的诊断申请", grade = 5)
	public String cancelTranferRequest() {
		try {
			RequestTranferService.instance.cancelTranferRequestById(takePassport(), request_tranfer_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 搜索用户当前机构转交的诊断申请 */
	public String searchRequestTranferFromThisOrg() {
		try {
			Passport passport = takePassport();
			SearchRequestTranferParam srtp = new SearchRequestTranferParam();
			srtp.setCreate_org_id(passport.getOrgId());
			srtp.setSpu(spu);
			srtp.setNew_request_status(new_request_status);
			srtp.setNew_case_his_num(new_case_his_num);
			srtp.setNew_device_type_id(new_device_type_id);
			srtp.setNew_diagnosis_org_id(new_diagnosis_org_id);
			srtp.setNew_patient_gender(new_patient_gender);
			srtp.setNew_patient_name(new_patient_name);
			srtp.setTimeLimit(timelimit);
			List<FRequestTranfer> listRequestTranfer = RequestTranferService.instance.searchRequestTranfer(srtp);
			addElementToData("listRequestTranfer", listRequestTranfer);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 检查是否对此机构重复转交 */
	public String checkRepeatTranferRequest() {
		try {
			boolean repeat = RequestTranferService.instance.checkRepeatTranferRequest(takePassport(), diagnosis_id,
					diagnosis_org_id);
			addElementToData("repeat", repeat);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 通过诊断ID查询可以移交的好友机构 */
	public String queryFriendOrgCanTranferByDiagnosisId() {
		try {
			List<FOrgRelation> listOrgRelation = RequestTranferService.instance
					.queryFriendOrgCanTranferByDiagnosisId(takePassport(), diagnosis_id);
			addElementToData("listOrgRelation", listOrgRelation);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("接受诊断申请，并转发给其他机构")
	public String acceptAndTranferRequest() {
		try {
			TDiagnosis diagnosis = RequestTranferService.instance.acceptAndTranferRequest(takePassport(), diagnosis_id,
					diagnosis_org_id, diagnosis_product_id);
			super.addElementToData("diagnosis", diagnosis);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosis_id = filterParam(diagnosisId);
	}

	public void setDiagnosis_id(Long diagnosis_id) {
		this.diagnosis_id = filterParam(diagnosis_id);
	}

	public void setDiagnosis_org_id(Long diagnosis_org_id) {
		this.diagnosis_org_id = filterParam(diagnosis_org_id);
	}

	public void setDiagnosis_product_id(Long diagnosis_product_id) {
		this.diagnosis_product_id = filterParam(diagnosis_product_id);
	}

	public void setRelation_org_id(Long relation_org_id) {
		this.relation_org_id = filterParam(relation_org_id);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setNew_request_status(int new_request_status) {
		this.new_request_status = DiagnosisStatus2.parseFrom(new_request_status);
	}

	public void setRequest_tranfer_id(Long request_tranfer_id) {
		this.request_tranfer_id = filterParam(request_tranfer_id);
	}

	public void setNew_patient_gender(int new_patient_gender) {
		this.new_patient_gender = Gender.parseCode(new_patient_gender);
	}

	public void setNew_diagnosis_org_id(Long new_diagnosis_org_id) {
		this.new_diagnosis_org_id = filterParam(new_diagnosis_org_id);
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = TimeLimit.parseCode(timelimit);
	}

	public void setNew_device_type_id(Long new_device_type_id) {
		this.new_device_type_id = filterParam(new_device_type_id);
	}

	public void setNew_patient_name(String new_patient_name) {
		this.new_patient_name = filterParam(new_patient_name);
	}

	public void setNew_case_his_num(String new_case_his_num) {
		this.new_case_his_num = filterParam(new_case_his_num);
	}
	public void setNew_request_org_id(Long new_request_org_id) {
		this.new_request_org_id = filterParam(new_request_org_id);
	}

}
