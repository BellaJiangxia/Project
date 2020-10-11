package com.vastsoft.yingtai.module.qualityControl.action;

import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlFormAnswerParam;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlFormParam;
import com.vastsoft.yingtai.module.qualityControl.assist.SearchQualityControlMeasureParam;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormAnswerStatus;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormState;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;
import com.vastsoft.yingtai.module.qualityControl.entity.FQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlFormAnswer;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlMeasure;
import com.vastsoft.yingtai.module.qualityControl.entity.VQualityControlFormAnswer;
import com.vastsoft.yingtai.module.qualityControl.service.QualityControlService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class QualityControlAction extends BaseYingTaiAction {
	private long form_id;
	private Long form_answer_id;
	private QualityControlTarget target_type;
	private QualityControlEnforceable question_enforceable;
	private QualityControlFormState qcf_state;
	private QualityControlFormAnswerStatus qcfa_status;
	private SplitPageUtil spu;
	private TQualityControlForm qualityControlForm;
	private List<TQualityControlMeasure> listQualityControlMeasure;
	private String qualityControlUid;
	private List<VQualityControlFormAnswer> listQualityControlForm;
	private Long target_id;

//	public String queryQualityControlFormAnswerById() {
//		try {
//			VQualityControlFormAnswer qualityControlFormAnswer = QualityControlService.instance
//					.takeQualityControlFormById(takePassport(), form_answer_id);
//			addElementToData("qualityControlFormAnswer", qualityControlFormAnswer);
//		} catch (Exception e) {
//			catchException(e);
//		}
//		return SUCCESS;
//	}
	
	/**通过质控目标类型和质控目标IDD查询是否需要质控*/
	public String needQualityControlByTargetTypeAndTargetId(){
		try {
			qualityControlUid = QualityControlService.instance.queryQualityControlFormUid(takePassport(), target_type, target_id);
			addElementToData("qualityControlUid", qualityControlUid);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
		
	}
	
	public String searchQualityControlFormAnswer() {
		try {
			Passport passport = takePassport();
			SearchQualityControlFormAnswerParam sqcfap = new SearchQualityControlFormAnswerParam();
			sqcfap.setSpu(spu);
			sqcfap.setTarget_type(target_type);
			sqcfap.setStatus(qcfa_status);
			sqcfap.setForm_question_enforceable(question_enforceable);
			sqcfap.setCreate_user_id(passport.getUserId());
			sqcfap.setCreate_org_id(passport.getOrgId());
			List<TQualityControlFormAnswer> listQualityControlFormAnswer = QualityControlService.instance
					.searchQualityControlFormAnswer(sqcfap);
			addElementToData("listQualityControlFormAnswer", listQualityControlFormAnswer);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "忽略本次质控", grade = 5)
	public String ignoreFormsByQcUid() {
		try {
			QualityControlService.instance.ignoreFormsByQcUid(takePassport(), qualityControlUid);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "提交问卷答案", grade = 5)
	public String commitQualityControlFormAnswer() {
		try {
			QualityControlService.instance.commitQualityControlFormAnswer(takePassport(), listQualityControlForm);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String takeQualityControlFormByQcUidOrFormAnswerId() {
		try {
			listQualityControlForm = QualityControlService.instance.takeQualityControlFormByQcUidOrFormAnswerId(takePassport(),
					qualityControlUid,form_answer_id);
			addElementToData("listQualityControlForm", listQualityControlForm);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "管理员删除质控问卷", grade = 5)
	public String validQualityControlFormById() {
		try {
			QualityControlService.instance.disabledQualityControlFormById(takePassport(), form_id, false);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "管理员删除质控问卷", grade = 5)
	public String disableQualityControlFormById() {
		try {
			QualityControlService.instance.disabledQualityControlFormById(takePassport(), form_id, true);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "管理员删除质控问卷", grade = 5)
	public String deleteQualityControlFormById() {
		try {
			QualityControlService.instance.deleteQualityControlFormById(takePassport(), form_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "管理员修改质控问卷", grade = 5)
	public String modifyQualityControlForm() {
		try {
			qualityControlForm = QualityControlService.instance.modifyQualityControlForm(takePassport(),
					qualityControlForm, listQualityControlMeasure);
			addElementToData("qualityControlForm", qualityControlForm);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlMeasureByFormId() {
		try {
			SearchQualityControlMeasureParam sqcmp = new SearchQualityControlMeasureParam();
			sqcmp.setForm_id(form_id);
			listQualityControlMeasure = QualityControlService.instance.searchQualityControlMeasure(sqcmp);
			addElementToData("listQualityControlMeasure", listQualityControlMeasure);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlFormById() {
		try {
			qualityControlForm = QualityControlService.instance.queryQualityControlFormById(form_id);
			addElementToData("qualityControlForm", qualityControlForm);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc(value = "管理员新增质控问卷", grade = 5)
	public String addQualityControlForm() {
		try {
			qualityControlForm = QualityControlService.instance.addQualityControlForm(takePassport(),
					qualityControlForm, listQualityControlMeasure);
			super.addElementToData("qualityControlForm", qualityControlForm);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String searchQualityControlForm() {
		try {
			SearchQualityControlFormParam sqcfp = new SearchQualityControlFormParam();
			sqcfp.setSpu(spu);
			sqcfp.setTarget_type(target_type);
			sqcfp.setQuestion_enforceable(question_enforceable);
			sqcfp.setState(qcf_state);
			List<FQualityControlForm> listQualityControlForm = QualityControlService.instance
					.searchQualityControlForm(sqcfp);
			addElementToData("listQualityControlForm", listQualityControlForm);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setTarget_type(int target_type) {
		this.target_type = QualityControlTarget.parseCode(target_type);
	}

	public void setQuestion_enforceable(int question_enforceable) {
		this.question_enforceable = QualityControlEnforceable.parseCode(question_enforceable);
	}

	public void setQcf_state(int qcf_state) {
		this.qcf_state = QualityControlFormState.parseCode(qcf_state);
	}

	public void setQualityControlForm(TQualityControlForm qualityControlForm) {
		this.qualityControlForm = qualityControlForm;
	}

	public void setListQualityControlMeasure(List<TQualityControlMeasure> listQualityControlMeasure) {
		this.listQualityControlMeasure = listQualityControlMeasure;
	}

	public void setForm_id(long form_id) {
		this.form_id = form_id;
	}

	public void setQualityControlUid(String qualityControlUid) {
		this.qualityControlUid = qualityControlUid;
	}

	public void setListQualityControlForm(List<VQualityControlFormAnswer> listQualityControlForm) {
		this.listQualityControlForm = listQualityControlForm;
	}

	public void setQcfa_status(int qcfa_status) {
		this.qcfa_status = QualityControlFormAnswerStatus.parseCode(qcfa_status);
	}

	public void setForm_answer_id(Long form_answer_id) {
		this.form_answer_id = filterParam(form_answer_id);
	}

	public void setTarget_id(Long target_id) {
		this.target_id = filterParam(target_id);
	}

}
