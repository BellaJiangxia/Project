package com.vastsoft.yingtai.module.diagnosis2.action;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.TimeLimit;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.FCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.diagnosis2.assist.DiagnosisSearchParams2;
import com.vastsoft.yingtai.module.diagnosis2.assist.HandleSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestClass;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestUrgentLevel;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;
import com.vastsoft.yingtai.utils.attributeUtils.AttributeUtils;

public class DiagnosisAction extends BaseYingTaiAction {
	private boolean allow_reporter_publish_report;
	private Long medicalHisId, productId, diagnosisId, handleId, userId, diagnosisUserId, diagnosisOrgId, requestOrgId;
	private Long requestUserId, deviceTypeId, publishReportOrgId, orgId, dicomImgId, next_user_id;
	private Long diagnosisProductId, selectedDicomImgId, caseHistoryId, curr_user_id;
	private String note, medicalHisNum, sickerName, aboutCaseIds;
	private SplitPageUtil spu;
	private DiagnosisStatus2 status;
	private TDiagnosisHandle diagnosisHandle;
	private TimeLimit timelimit;
	private Gender gender;
	private TCaseHistory caseHistory;
	private RequestClass request_class;
	private RequestUrgentLevel request_urgent_level;
	private int charge_amount;

	@ActionDesc(value = "提交诊断申请", grade = 5)
	public String commitDiagnosis() {
		try {
			Passport passport = takePassport();
			if (caseHistoryId == null || caseHistoryId <= 0) {
				if (caseHistory == null)
					throw new RequestException("必须指定要提交诊断申请的病例！");
				caseHistoryId = caseHistory.getId();
			}
			TDiagnosis diagnosis = DiagnosisService2.instance.commitDiagnosis(passport, caseHistoryId,
					selectedDicomImgId, diagnosisOrgId, diagnosisProductId, aboutCaseIds, charge_amount, allow_reporter_publish_report,
					request_class,request_urgent_level);
			addElementToData("diagnosis", diagnosis);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("撤销诊断移交")
	public String cancelTranfar() {
		try {
			DiagnosisService2.instance.cancelHandleTranFar(takePassport(), handleId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// /** 查询带诊断和待处理的流转,新诊断消息的数量 */
	// public String queryDiagnosisCount() {
	// try {
	// Passport passport = takePassport();
	// spu = new SplitPageUtil(1, 2);
	// try {
	// DiagnosisService2.instance.queryTranferToUserDiagnosisHandleByUserIdAndOrgIdAndStatus(
	// passport.getUserId(), passport.getOrgId(),
	// DiagnosisHandleStatus2.TRANFERED, spu);
	// addElementToData("unAcceptHandleCount", spu.getTotalRow());
	// } catch (Exception e) {
	// addElementToData("unAcceptHandleCount", 0);
	// }
	// try {
	// long waitDiagnosisCount =
	// DiagnosisService2.instance.queryWaitDiagnosisCount(passport);
	// addElementToData("waitDiagnosisHandleCount", waitDiagnosisCount);
	// } catch (Exception e) {
	// addElementToData("waitDiagnosisHandleCount", 0);
	// }
	// try {
	// int unreadDiagnosisMsgCount =
	// ReportService.instance.queryUnreadDiagnosisMsgCount(passport,
	// ReportMsgType.TO_DIAGNOSISER);
	// addElementToData("unreadDiagnosisMsgCount", unreadDiagnosisMsgCount);
	// } catch (Exception e) {
	// addElementToData("unreadDiagnosisMsgCount", 0);
	// }
	// try {
	// int unreadRequestMsgCount =
	// ReportService.instance.queryUnreadDiagnosisMsgCount(passport,
	// ReportMsgType.TO_REQUESTER);
	// addElementToData("unreadRequestMsgCount", unreadRequestMsgCount);
	// } catch (Exception e) {
	// addElementToData("unreadRequestMsgCount", 0);
	// }
	// try {
	// TDiagnosisHandle handle =
	// DiagnosisService2.instance.queryUnFinishHandle(passport);
	// addElementToData("unfinish", handle != null);
	// } catch (Exception e) {
	// addElementToData("unfinish", false);
	// }
	// try {
	// spu.setTotalRow(0);
	// HandleSearchParam hsp = new HandleSearchParam();
	// hsp.setOrg_id(passport.getOrgId());
	// hsp.setStatus(DiagnosisHandleStatus2.TRANFER_AUDIT);
	// hsp.setExclude_curr_user_ids(passport.getUserId());
	// DiagnosisService2.instance.searchDiagnosisHandle(hsp);
	// addElementToData("unAuditCount", spu.getTotalRow());
	// } catch (Exception e) {
	// addElementToData("unAuditCount", 0);
	// }
	// } catch (Exception e) {
	// super.catchException(e);
	// }
	// return SUCCESS;
	// }

	// /** "通过申请ID查询分享发言" */
	// public String queryFDiagnosisShareSpeechByShareId() {
	// try {
	// Passport passport = takePassport();
	// if (spu == null)
	// spu = new SplitPageUtil(1, 10);
	// List<FReportShareSpeech> listSpeech =
	// DiagnosisService2.instance.searchDiagnosisShareSpeech(passport,
	// diagnosisShareId, spu);
	// addElementToData("listSpeech", listSpeech);
	// addElementToData("spu", spu);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// // @ActionDesc("获取申请分享详细信息")
	// public String selectDiagnosisShareDetail() {
	// try {
	//
	// Passport passport = takePassport();
	// FReportShare diagnosisShare =
	// DiagnosisService2.instance.selectDiagnosisShareById(passport,
	// diagnosisShareId);
	// FDiagnosis diagnosis =
	// DiagnosisService2.instance.queryDiagnosisById(diagnosisShare.getDiagnosis_id());
	// TCaseHistory medicalHis =
	// CaseHistoryService.instance.queryCaseHistoryById(diagnosis.getCase_his_id());
	// if (spu == null)
	// spu = new SplitPageUtil(1, 10);
	// List<FReportShareSpeech> listSpeech =
	// DiagnosisService2.instance.searchDiagnosisShareSpeech(passport,
	// diagnosisShare.getId(), spu);
	// addElementToData("diagnosisShare", diagnosisShare);
	// addElementToData("diagnosis", diagnosis);
	// addElementToData("medicalHis", medicalHis);
	// addElementToData("listSpeech", listSpeech);
	// addElementToData("spu", spu);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// @ActionDesc("取消申请分享")
	// public String cancelShareMedicalHis() {
	// try {
	// DiagnosisService2.instance.cancelDiagnosisShare(takePassport(),
	// diagnosisId);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// // @ActionDesc("获取本机构的申请分享")
	// public String querySelfOrgShareDiagnosis() {
	// try {
	// Passport passport = takePassport();
	// List<FReportShare> listDiagnosisShare =
	// DiagnosisService2.instance.searchDiagnosisShare(passport, null,
	// null, null, passport.getOrgId(), medicalHisNum, null, null, null,
	// strSymptom, start, end,
	// diagnosisShareStatus, spu);
	// addElementToData("listDiagnosisShare", listDiagnosisShare);
	// addElementToData("spu", spu);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// // @ActionDesc("获取所有申请分享")
	// public String queryDiagnosisShareList() {
	// try {
	// List<FReportShare> listDiagnosisShare =
	// DiagnosisService2.instance.searchDiagnosisShare(takePassport(),
	// null, null, null, null, null, shareOrgName, null, null, strSymptom,
	// start, end,
	// ReportShareStatus.IS_SHARE, spu);
	// addElementToData("listDiagnosisShare", listDiagnosisShare);
	// addElementToData("spu", spu);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// @ActionDesc("分享申请")
	// public String shareDiagnosis() {
	// try {
	// Passport passport = takePassport();
	// TReportShare diagnosisShare =
	// DiagnosisService2.instance.shareDiagnosis(passport, diagnosisId);
	// addElementToData("diagnosisShare", diagnosisShare);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// @ActionDesc("检查病例诊断是否已经存在，是否重复提交")
	public String checkRepeatCommitDiagnosis() {
		try {
			boolean repeat = DiagnosisService2.instance.checkRepeatCommitDiagnosis(takePassport(), medicalHisId,
					dicomImgId, diagnosisOrgId, productId);
			addElementToData("repeat", repeat);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取诊断的详细信息")
	public String queryFDiagnosisDetailById() {
		try {
			FDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(diagnosisId);
			addElementToData("diagnosis", diagnosis);
			// addElementToData("systemSplit",
			// SysService.instance.takeSysDeductQuota());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 获取诊断详情和图像详情 */
	public String queryFDiagnosisAndImgByDiagnosisId() {
		try {
			Passport passport = takePassport();
			FDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(diagnosisId);
			if (diagnosis == null)
				throw new RequestException("指定的诊断申请未找到！");
			TCaseHistory medicalHis = CaseHistoryService.instance.queryCaseHistoryById(diagnosis.getCase_his_id(),true);
			TDicomImg medicalHisImg = DicomImgService.instance.queryDicomImgById(diagnosis.getDicom_img_id());
			if (passport.getOrgId() != null && !passport.getOrgId().equals(medicalHis.getOrg_id())) {
				medicalHis.formatPropertys();
			}
			addElementToData("diagnosis", diagnosis);
			addElementToData("medicalHis", medicalHis);
			if (medicalHisImg != null) {
				// medicalHisImg.setThumbnail_uid(medicalHisImg.getThumbnail_uid());
				addElementToData("medicalHisImg", medicalHisImg);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 获取指定机构的所有医生 */
	public String queryDoctprsInOrgId() {
		try {
			Passport passport = this.takePassport();
			List<TDoctorUser> listDoctor = UserService.instance.queryDoctorsInOrg(passport, orgId, false, false);
			addElementToData("listDoctor", listDoctor);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 查询本机构所有的审核员 */
	public String queryVerityUserInOrg() {
		try {
			Passport passport = this.takePassport();
			List<TDoctorUser> listDoctor = UserService.instance.queryDoctorsInOrg(passport, null, true, true);
			addElementToData("listVerityUser", listDoctor);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 查询本机构下的所有医生 */
	public String queryDoctorsInOrg() {
		try {
			Passport passport = this.takePassport();
			List<TDoctorUser> listDoctor = UserService.instance.queryDoctorsInOrg(passport, null, false, false);
			addElementToData("listDoctor", listDoctor);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 查询所有可以移交诊断的医生 */
	public String queryCanTranferDoctors() {
		try {
			Passport passport = this.takePassport();
			List<TDoctorUser> listDoctor = DiagnosisService2.instance.queryCanTranferDoctors(passport, diagnosisId);
			String[] attrs = { "id", "user_name", "grade", "section" };
			addElementToData("listDoctor", AttributeUtils.SerializeList(listDoctor, attrs));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("取消诊断申请")
	public String cancelDiagnosis() {
		try {
			TDiagnosis diagnosis = DiagnosisService2.instance.cancelDiagnosis(takePassport(), diagnosisId);
			addElementToData("diagnosis", diagnosis);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("诊断前拒绝诊断申请")
	public String rejectDiagnosis() {
		try {
			DiagnosisService2.instance.rejectDiagnosis(takePassport(), diagnosisId, note);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("接受诊断申请，并开始诊断")
	public String acceptDiagnosis() {
		try {
			TDiagnosisHandle handle = DiagnosisService2.instance.acceptDiagnosis(takePassport(), diagnosisId);
			addElementToData("handle", handle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("撤销诊断，退回到未接受诊断状态")
	public String revokeDiagnosis() {
		try {
			DiagnosisService2.instance.revokeDiagnosis(takePassport(), handleId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("保存诊断处理意见")
	public String saveOpinion() {
		try {
			DiagnosisService2.instance.saveOpinion(takePassport(), diagnosisHandle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("移交诊断")
	public String tranferDiagnosis() {
		try {
			DiagnosisService2.instance.tranferDiagnosis(takePassport(), userId, diagnosisHandle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("接受移交诊断")
	public String acceptTranferDiagnosis() {
		try {
			TDiagnosisHandle handle = DiagnosisService2.instance.acceptTranferDiagnosis(takePassport(), handleId);
			addElementToData("handle", handle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("发布报告,并结束整个诊断过程")
	public String publishReport() {
		try {
			TReport report = DiagnosisService2.instance.publishReport(takePassport(), diagnosisHandle);
			addElementToData("report", report);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询自己书写中的处理,以及其历史处理")
	public String queryUnFinishHandle() {
		try {
			Passport passport = takePassport();
			TDiagnosisHandle handle = DiagnosisService2.instance.queryUnFinishHandle(passport);
			if (handle != null) {
				FDiagnosis diagnosis = DiagnosisService2.instance.queryDiagnosisDetailById(handle.getDiagnosis_id());
				addElementToData("diagnosis", diagnosis);
				List<FDiagnosisHandle> listDiagnosisHandle = DiagnosisService2.instance
						.queryDiagnosisHandleByDiagnosisId(handle.getDiagnosis_id());
				addElementToData("listHandle", listDiagnosisHandle);
				List<FCaseHistory> listCaseHistory = CaseHistoryService.instance
						.queryCaseHistoryByIds(diagnosis.getAbout_case_ids());
				addElementToData("listCaseHistory", listCaseHistory);
				TDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(diagnosis.getDicom_img_id());
				addElementToData("dicomImg", dicomImg);
			}
			addElementToData("handle", handle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("索搜我提交的申请")
	public String searchSelfRequestDiagnosis() {
		try {
			Passport passport = takePassport();
			DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
			dsp.setRequest_org_id(passport.getOrgId());
			dsp.setRequest_user_id(passport.getUserId());
			dsp.setPatient_name(sickerName);
			dsp.setPatient_gender(gender);
			dsp.setCase_his_num(medicalHisNum);
			dsp.setDiagnosis_org_id(diagnosisOrgId);
			dsp.setDicom_img_device_type_id(deviceTypeId);
			dsp.setStatus(status);
			dsp.setStart(timelimit == null ? null : timelimit.getStartTime());
			dsp.setEnd(new Date());
			dsp.setSpu(spu);
			List<FDiagnosis> listFDiagnosis = DiagnosisService2.instance.searchDiagnosis(dsp);
			// if (listFDiagnosis != null && listFDiagnosis.size() > 0) {
			// String[] attrs = { "sicker_name", "sicker_gender",
			// "sicker_genderStr", "sicker_age", "medical_his_num",
			// "device_name", "part_name", "diagnosis_org_name", "status",
			// "statusStr", "id", "note",
			// "request_user_name", "medical_his_id", "request_user_id",
			// "newMsgCount", "create_timeStr",
			// "print_user_name", "pic_opinions", "pic_conclusions" };
			// addElementToData("listFDiagnosis",
			// AttributeUtils.SerializeList(listFDiagnosis, attrs));
			// } else
			addElementToData("listFDiagnosis", listFDiagnosis);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索本机构提交的诊断申请")
	public String searchOrgRequestDiagnosis() {
		try {
			Passport passport = takePassport();
			DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
			dsp.setDiagnosis_org_id(diagnosisOrgId);
			dsp.setCase_his_num(medicalHisNum);
			dsp.setRequest_org_id(passport.getOrgId());
			dsp.setDicom_img_device_type_id(deviceTypeId);
			dsp.setStart(timelimit == null ? null : timelimit.getStartTime());
			dsp.setEnd(new Date());
			dsp.setPatient_name(sickerName);
			dsp.setPatient_gender(gender);
			dsp.setStatus(status);
			dsp.setSpu(spu);
			List<FDiagnosis> listFDiagnosis = DiagnosisService2.instance.searchDiagnosis(dsp);
			addElementToData("listFDiagnosis", listFDiagnosis);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索诊断申请")
	public String searchDiagnosis() {
		try {
			DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
			dsp.setDicom_img_device_type_id(deviceTypeId);
			dsp.setDiagnosis_org_id(diagnosisOrgId);
			dsp.setDiagnosis_product_id(productId);
			dsp.setStart(super.getStart());
			dsp.setEnd(super.getEnd());
			dsp.setId(diagnosisId);
			dsp.setCase_his_id(medicalHisId);
			dsp.setCase_his_num(medicalHisNum);
			dsp.setPatient_name(sickerName);
			dsp.setPatient_gender(gender);
			dsp.setRequest_org_id(requestOrgId);
			dsp.setRequest_user_id(requestUserId);
			dsp.setVerify_user_id(diagnosisUserId);
			dsp.setPublish_report_org_id(publishReportOrgId);
			dsp.setStatus(status);
			dsp.setSpu(spu);

			List<FDiagnosis> listFDiagnosis = DiagnosisService2.instance.searchDiagnosis(dsp);
			addElementToData("listFDiagnosis", listFDiagnosis);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索搜索本机构诊断的诊断申请")
	public String searchSelfDiagnosis() {
		try {
			Passport passport = takePassport();
			DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
			dsp.setDiagnosis_org_id(passport.getOrgId());
			dsp.setId(diagnosisId);
			dsp.setPatient_name(sickerName);
			dsp.setPatient_gender(gender);
			dsp.setCase_his_num(medicalHisNum);
			dsp.setCase_his_id(medicalHisId);
			dsp.setRequest_org_id(requestOrgId);
			dsp.setVerify_user_id(diagnosisUserId);
			dsp.setDicom_img_device_type_id(deviceTypeId);
			dsp.setDiagnosis_product_id(productId);
			dsp.setPublish_report_org_id(publishReportOrgId);
			dsp.setStart(super.getStart());
			dsp.setEnd(super.getEnd());
			dsp.setStatus(status);
			dsp.setSpu(spu);
			dsp.setRequest_tranfer_flag(true);
			List<FDiagnosis> listFDiagnosis = DiagnosisService2.instance.searchDiagnosis(dsp);
			addElementToData("listFDiagnosis", listFDiagnosis);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询我已经处理过的诊断处理")
	public String searchTranferOfMeHandled() {
		try {
			Passport passport = takePassport();
			HandleSearchParam hsp = new HandleSearchParam();
			hsp.setCurr_user_id(passport.getUserId());
			hsp.setOrg_id(passport.getOrgId());
			hsp.setSpu(spu);
			hsp.setRequest_org_id(requestOrgId);
			// hsp.setDicom_img_part_type_id(partTypeId);
			hsp.setDicom_img_device_type_id(deviceTypeId);
			hsp.setPatient_gender(gender);
			hsp.setCase_his_num(medicalHisNum);
			hsp.setNext_user_id(next_user_id);
			hsp.setExclude_status(DiagnosisHandleStatus2.WRITING);
			List<FDiagnosisHandle> listHandle = DiagnosisService2.instance.searchDiagnosisHandle(hsp);
			addElementToData("listHandle", listHandle);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询移交给我的未处理的诊断处理")
	public String searchTranferToMeHandle() {
		try {
			Passport passport = takePassport();
			HandleSearchParam hsp = new HandleSearchParam();
			hsp.setNext_user_id(passport.getUserId());
			hsp.setOrg_id(passport.getOrgId());
			hsp.setSpu(spu);
			hsp.setRequest_org_id(requestOrgId);
			// hsp.setDicom_img_part_type_id(partTypeId);
			hsp.setDicom_img_device_type_id(deviceTypeId);
			hsp.setPatient_gender(gender);
			hsp.setCase_his_num(medicalHisNum);
			hsp.setCurr_user_id(curr_user_id);
			hsp.setStatus(DiagnosisHandleStatus2.TRANFERED);
			List<FDiagnosisHandle> listHandle = DiagnosisService2.instance.searchDiagnosisHandle(hsp);
			/*
			 * searchDiagnosisHandle(passport, true, requestOrgId, partTypeId,
			 * deviceTypeId, gender, sickerName, medicalHisNum, diagnosisId,
			 * userId, passport.getUserId(), passport.getOrgId(), start, end,
			 * DiagnosisHandleStatus2.TRANFERED, "TRANSFER_TIME", spu);
			 */
			addElementToData("listHandle", listHandle);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索移交审核的诊断")
	public String searchTranferToAuditHandle() {
		try {
			Passport passport = takePassport();
			HandleSearchParam hsp = new HandleSearchParam();
			hsp.setRequest_org_id(requestOrgId);
			hsp.setDicom_img_device_type_id(deviceTypeId);
			// hsp.setDicom_img_part_type_id(partTypeId);
			hsp.setPatient_gender(gender);
			hsp.setCase_his_num(medicalHisNum);
			hsp.setCurr_user_id(curr_user_id);
			hsp.setSpu(spu);
			hsp.setOrg_id(passport.getOrgId());
			hsp.setStatus(DiagnosisHandleStatus2.TRANFER_AUDIT);
			hsp.setExclude_curr_user_ids(passport.getUserId());
			List<FDiagnosisHandle> listHandle = DiagnosisService2.instance.searchDiagnosisHandle(hsp);
			/*
			 * searchTranferToAuditHandle(passport, requestOrgId, partTypeId,
			 * deviceTypeId, gender, medicalHisNum, userId, spu);
			 */
			addElementToData("listHandle", listHandle);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询指定申请的所有诊断处理")
	public String queryDiagnosisHandleByDiagnosisId() {
		try {
			List<FDiagnosisHandle> listHandle = DiagnosisService2.instance
					.queryDiagnosisHandleByDiagnosisId(diagnosisId);
			// searchDiagnosisHandle(passport, false, null,
			// null, null, null, null, null, diagnosisId, null, null,
			// passport.getOrgId(), null, null, null, null,
			// null);
			addElementToData("listHandle", listHandle);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setMedicalHisId(Long lMedicalHisId) {
		this.medicalHisId = filterParam(lMedicalHisId);
	}

	public void setProductId(Long lProductId) {
		this.productId = filterParam(lProductId);
	}

	public void setDiagnosisId(Long lDiagnosisId) {
		this.diagnosisId = filterParam(lDiagnosisId);
	}

	public void setHandleId(Long lHandleId) {
		this.handleId = filterParam(lHandleId);
	}

	public void setUserId(Long lUserId) {
		this.userId = filterParam(lUserId);
	}

	public void setDiagnosisUserId(Long lDiagnosisUserId) {
		this.diagnosisUserId = filterParam(lDiagnosisUserId);
	}

	public void setDiagnosisOrgId(Long diagnosisOrgId) {
		this.diagnosisOrgId = filterParam(diagnosisOrgId);
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = filterParam(deviceTypeId);
	}

	public void setMedicalHisNum(String medicalHisNum) {
		this.medicalHisNum = filterParam(medicalHisNum);
	}

	public void setSickerName(String sickerName) {
		this.sickerName = filterParam(sickerName);
	}

	public void setGender(int gender) {
		this.gender = Gender.parseCode(gender);
	}

	public void setNote(String note) {
		this.note = filterParam(note);
	}

	public void setDiagnosisHandle(TDiagnosisHandle diagnosisHandle) {
		this.diagnosisHandle = diagnosisHandle;
	}

	public void setRequestOrgId(Long lRequestOrgId) {
		this.requestOrgId = filterParam(lRequestOrgId);
	}

	public void setRequestUserId(Long lRequestUserId) {
		this.requestUserId = filterParam(lRequestUserId);
	}

	public void setPublishReportOrgId(Long lPublishReportOrgId) {
		this.publishReportOrgId = filterParam(lPublishReportOrgId);
	}

	public void setOrgId(Long lOrgId) {
		this.orgId = filterParam(lOrgId);
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = TimeLimit.parseCode(timelimit);
	}

	public void setStatus(int status) {
		this.status = DiagnosisStatus2.parseFrom(status);
	}

	public void setDicomImgId(Long dicomImgId) {
		this.dicomImgId = filterParam(dicomImgId);
	}

	public void setNext_user_id(Long next_user_id) {
		this.next_user_id = filterParam(next_user_id);
	}

	public void setCurr_user_id(Long curr_user_id) {
		this.curr_user_id = filterParam(curr_user_id);
	}

	public void setAboutCaseIds(String aboutCaseIds) {
		this.aboutCaseIds = filterParam(aboutCaseIds);
	}

	public void setDiagnosisProductId(Long diagnosisProductId) {
		this.diagnosisProductId = filterParam(diagnosisProductId);
	}

	public void setCaseHistory(TCaseHistory caseHistory) {
		this.caseHistory = caseHistory;
	}

	public void setSelectedDicomImgId(Long selectedDicomImgId) {
		this.selectedDicomImgId = filterParam(selectedDicomImgId);
	}

	public void setCaseHistoryId(Long caseHistoryId) {
		this.caseHistoryId = filterParam(caseHistoryId);
	}

	public void setAllow_reporter_publish_report(boolean allow_reporter_publish_report) {
		this.allow_reporter_publish_report = allow_reporter_publish_report;
	}

	public void setRequest_class(int request_class) {
		this.request_class = RequestClass.parseCode(request_class);
	}

	public void setRequest_urgent_level(int request_urgent_level) {
		this.request_urgent_level = RequestUrgentLevel.parseCode(request_urgent_level);
	}
	
	public void setCharge_amount(int charge_amount){
		this.charge_amount = charge_amount;
	}
}
