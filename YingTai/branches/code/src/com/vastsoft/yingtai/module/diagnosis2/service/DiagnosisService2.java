package com.vastsoft.yingtai.module.diagnosis2.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.db.VsSqlSession;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.PatientDataSourceType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.entity.TReport;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.service.ReportService;
import com.vastsoft.yingtai.module.diagnosis2.assist.DiagnosisSearchParams2;
import com.vastsoft.yingtai.module.diagnosis2.assist.HandleSearchParam;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisHandleStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.contants.HandleCanTranfer;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestClass;
import com.vastsoft.yingtai.module.diagnosis2.contants.RequestUrgentLevel;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosisHandle;
import com.vastsoft.yingtai.module.diagnosis2.exception.RequestException;
import com.vastsoft.yingtai.module.diagnosis2.mapper.DiagnosisMapper;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.assist.SearchRequestTranferParam;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.entity.FRequestTranfer;
import com.vastsoft.yingtai.module.diagnosis2.requestTranfer.services.RequestTranferService;
import com.vastsoft.yingtai.module.financel.service.FinanceService;
import com.vastsoft.yingtai.module.msg.constants.MsgType;
import com.vastsoft.yingtai.module.msg.service.MsgService;
import com.vastsoft.yingtai.module.msg.service.MsgTimer;
import com.vastsoft.yingtai.module.org.constants.OrgProperty;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.product.entity.TOrgProduct;
import com.vastsoft.yingtai.module.org.product.service.OrgProductService;
import com.vastsoft.yingtai.module.org.realtion.service.OrgRelationService;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.service.FileService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.entity.TDoctorUser;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class DiagnosisService2 extends DiagnosisBaseService2 {
	public static final DiagnosisService2 instance = new DiagnosisService2();

	private DiagnosisService2() {
	}

	/**
	 * 提交诊断申请
	 * 
	 * @param passport
	 *            身份
	 * @param lMedicalHisId
	 *            病例ID
	 * @param lImgId
	 *            图像ID
	 * @param lOrgId
	 *            机构ID
	 * @param lProductId
	 *            产品ID
	 * @param charge_amount
	 *            计费的数量
	 * @return 产生的诊断对象
	 * @throws BaseException
	 */
	private static final String COMMITDIAGNOSISLOCK = "添加诊断申请锁";

	public TDiagnosis commitDiagnosis(Passport passport, long caseHistoryId, long dicomImgId, long diagnosisOrgId,
			long lProductId, String aboutCaseIds,int charge_amount, boolean allow_reporter_publish_report, RequestClass request_class,
			RequestUrgentLevel request_urgent_level) throws Exception {
		if (request_class == null)
			throw new RequestException("提交诊断申请必须指定申请类别！");
		if (request_urgent_level == null)
			throw new RequestException("提交诊断申请必须指定诊断申请紧急程度！");
		synchronized (COMMITDIAGNOSISLOCK) {
			SqlSession session = null;
			try {
				session = SessionFactory.getSession();
				// 验证用户权限
				if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
					throw new NotPermissionException();
				// 检查诊断机构权限
				TOrganization org = OrgService.instance.searchOrgById(diagnosisOrgId);
				if (org == null)
					throw new RequestException("提交的诊断机构不存在！");
				// if
				// (!PermissionsSerializer.fromOrgPermissionString(org.getPermission())
				// .contains(OrgPermission.DIAGNOSISER)) {
				// throw new RequestException("提交的机构没有诊断权限，不能诊断你的病例！");
				// }
				if (request_class.equals(RequestClass.DIAGNOSIS)) {
					if (org.getOrg_property() == OrgProperty.TEAM.getCode())
						throw new RequestException("诊断机构是一个‘" + OrgProperty.TEAM.getName() + "’,只能接受咨询申请！");
				}
				// 验证病历有效性和是否在机构下//检查图像
				if (!CaseHistoryService.instance.checkCaseHistoryAndDicomImgOfOrg(passport, caseHistoryId, dicomImgId))
					throw new RequestException("病例验证未通过：病例不属于本机构或者病例图像不属于指定病例！");
				// 验证服务是否在机构下，且有效
				if (!OrgProductService.instance.checkProductByOrg(passport, lProductId, diagnosisOrgId))
					throw new RequestException("机构服务验证失败！");
				TOrgProduct orgProduct = OrgProductService.instance.queryOrgProductById(lProductId, session);
				if (orgProduct == null)
					throw new RequestException("机构服务验证失败！");
				FDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(dicomImgId, session);
				if (dicomImg == null)
					throw new RequestException("提交诊断的影像未找到！");
				if (CollectionTools.isEmpty(dicomImg.getListCheckPro()))
					throw new RequestException("要提交的影像的检查项目必须填写！");
				TCaseHistory caseHistory = CaseHistoryService.instance.queryCaseHistoryById(caseHistoryId, session);
				if (caseHistory == null)
					throw new RequestException("指定的病例未找到！");
				TPatient patient = PatientService.instance.queryPatientById(caseHistory.getPatient_id());
				if (patient == null)
					throw new RequestException("指定的病例所属病人未找到！");
				TOrgProduct product = OrgProductService.instance.searchProductById(lProductId);
				if (product == null)
					throw new RequestException("指定的诊断服务未找到！");
				// 添加病历诊断申请
				DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);

				TDiagnosis diagnosis = new TDiagnosis();
				diagnosis.setProduct_charge_type(orgProduct.getCharge_type());
				diagnosis.setProduct_name(orgProduct.getName());
				diagnosis.setCharge_amount(charge_amount);
				diagnosis.setProduct_unit_price(orgProduct.getPrice());
				diagnosis.setUrgent_level(request_urgent_level.getCode());
				diagnosis.setAbout_case_ids(aboutCaseIds);
				diagnosis.setCase_eaf_list(caseHistory.getEaf_list());
				diagnosis.setCase_enter_time(caseHistory.getEnter_time());
				diagnosis.setCase_his_id(caseHistoryId);
				diagnosis.setCase_his_num(caseHistory.getCase_his_num());
				diagnosis.setCase_hospitalized_num(caseHistory.getHospitalized_num());
				diagnosis.setCase_leave_time(caseHistory.getLeave_time());
				diagnosis.setCase_reception_doctor(caseHistory.getReception_doctor());
				diagnosis.setCase_reception_section(caseHistory.getReception_section());
				diagnosis.setCase_source_type(caseHistory.getSource_type());
				diagnosis.setCase_symptom(caseHistory.getSymptom());
				diagnosis.setCase_type(caseHistory.getType());
				diagnosis.setCreate_time(new Date());
				diagnosis.setDiagnosis_org_id(diagnosisOrgId);
				diagnosis.setDiagnosis_product_id(lProductId);
				diagnosis.setDicom_img_check_pro(dicomImg.getCheck_pro());
				diagnosis.setDicom_img_check_time(dicomImg.getCheck_time());
				diagnosis.setDicom_img_checklist_num(dicomImg.getChecklist_num());
				diagnosis.setDicom_img_device_type_id(dicomImg.getDevice_type_id());
				diagnosis.setDicom_img_id(dicomImgId);
				diagnosis.setDicom_img_mark_char(dicomImg.getMark_char());
				diagnosis.setPatient_id(patient.getId());
				diagnosis.setPatient_birthday(patient.getBirthday());
				diagnosis.setPatient_born_address(patient.getBorn_address());
				diagnosis.setPatient_gender(patient.getGender());
				diagnosis.setPatient_home_address(patient.getHome_address());
				diagnosis.setPatient_identity_id(patient.getIdentity_id());
				diagnosis.setPatient_name(patient.getName());
				diagnosis.setPatient_sick_his(patient.getSick_his());
				diagnosis.setPatient_source_type(patient.getSource_type());
				diagnosis.setPatient_work(patient.getWork());
				if (request_class.equals(RequestClass.DIAGNOSIS)) {
					TOrganization publishOrg = OrgRelationService.instance
							.queryPublishOrgIdByRequestOrgIdAndDiagnosisOrgId(passport.getOrgId(), diagnosisOrgId);
					if (publishOrg == null)
						throw new RequestException("发布报告机构未找到！");
					diagnosis.setPublish_report_org_id(publishOrg.getId());
				} else if (request_class.equals(RequestClass.CONSULT)) {
					diagnosis.setPublish_report_org_id(diagnosisOrgId);
				} else
					throw new RequestException("暂时不支持此类型的申请类别！");
				diagnosis.setRequest_org_id(passport.getOrgId());
				diagnosis.setRequest_user_id(passport.getUserId());
				diagnosis.setStatus(DiagnosisStatus2.NOTDIAGNOSIS.getCode());
				diagnosis.setAllow_reporter_publish_report(allow_reporter_publish_report ? 1 : 0);
				diagnosis.setRequest_class(request_class.getCode());
				
				// 冻结资金
				double total_price = OrgProductService.instance.calculatePriceByDicomImgIdAndOrgProductId(dicomImgId,charge_amount,
						lProductId, session);
				if (total_price <= 0)
					throw new RequestException("计算得到的费用为0，提交失败！");
				diagnosis.setTotal_price(total_price);
				
				this.checkDiagnosisObject(diagnosis);
				mapper.insertDiagnosis(diagnosis);
				MsgService.instance.addWaitSendPlan(passport, diagnosis, null, MsgType.DIAGNOSIS_REMIND, session);
				
				FinanceService.instance.getFinanceTrade().freezePrice(passport, diagnosis, total_price, session);
				CaseHistoryService.instance.commitRequest(passport, caseHistoryId, session);
				session.commit();
				return diagnosis;
			} catch (Exception e) {
				session.rollback(true);
				throw e;
			} finally {
				SessionFactory.closeSession(session);
			}
		}
	}

	private void checkDiagnosisObject(TDiagnosis request) throws RequestException {
		if (request == null)
			throw new RequestException("必须指定诊断申请对象！");
		if (StringTools.isEmpty(request.getProduct_name()))
			throw new RequestException("必须指定诊断产品的名称！");
		if (OrgProductChargeType.parseCode(request.getProduct_charge_type()) == null)
			throw new RequestException("必须指定有效的产品计费方式！");
		if (request.getCharge_amount() <=0) 
			throw new RequestException("必须指定有效的产品计费数量！");
		if (request.getProduct_unit_price() <= 0)
			throw new RequestException("产品的单价必须指定！");
		if (request.getTotal_price() <= 0)
			throw new RequestException("申请的涉及总金额必须指定！");
		
		if (request.getAbout_case_ids() != null) {
			request.setAbout_case_ids(request.getAbout_case_ids().trim());
			if (request.getAbout_case_ids().length() >= 2000)
				throw new RequestException("关联的历史病例太多了，请减少关联的病例数量！");
		}
		if (request.getCase_eaf_list() != null) {
			request.setCase_eaf_list(request.getCase_eaf_list().trim());
			if (request.getCase_eaf_list().length() >= 1000)
				throw new RequestException("电子申请单太多了，请减少电子申请单数量！");
		}
		if (request.getCase_his_id() <= 0)
			throw new RequestException("必须指定要提交申请的病例！");
		if (StringTools.isEmpty(request.getCase_his_num()))
			throw new RequestException("病例的病例编号必须指定！");
		request.setCase_his_num(request.getCase_his_num().trim());
		if (request.getCase_his_num().length() >= 256)
			throw new RequestException("指定的病例的病例编号太长了，最长256个字！");
		if (request.getCase_hospitalized_num() != null) {
			request.setCase_hospitalized_num(request.getCase_hospitalized_num().trim());
			if (request.getCase_hospitalized_num().length() >= 256)
				throw new RequestException("指定的病例的住院号太长了，最长256个字！");
		}
		if (request.getCase_reception_doctor() != null) {
			request.setCase_reception_doctor(request.getCase_reception_doctor().trim());
			if (request.getCase_reception_doctor().length() >= 64)
				throw new RequestException("指定的病例的接诊医生太长了，最长64个字！");
		}
		if (request.getCase_reception_section() != null) {
			request.setCase_reception_section(request.getCase_reception_section().trim());
			if (request.getCase_reception_section().length() >= 128)
				throw new RequestException("指定的病例的接诊医生太长了，最长128个字！");
		}
		if (PatientDataSourceType.parseCode(request.getCase_source_type()) == null)
			throw new RequestException("指定的病例的来源类型不正确！");
		if (request.getCase_symptom() != null) {
			request.setCase_symptom(request.getCase_symptom().trim());
			if (request.getCase_symptom().length() > 1024)
				throw new RequestException("指定的病例的症状太长了，最长1024个字！");
		}
		if (CaseHistoryType.parseCode(request.getCase_type()) == null)
			throw new RequestException("指定的病例的类型不正确！");
		if (request.getCreate_time() == null)
			request.setCreate_time(new Date());
		if (request.getDiagnosis_org_id() <= 0)
			throw new RequestException("提交诊断申请必须指定有效的诊断机构！");
		if (request.getDiagnosis_product_id() <= 0)
			throw new RequestException("提交诊断申请必须指定有效的诊断机构的产品服务！");
		if (request.getDicom_img_check_pro() != null) {
			request.setDicom_img_check_pro(request.getDicom_img_check_pro().trim());
			if (request.getDicom_img_check_pro().length() >= 1000)
				throw new RequestException("提交病例的图像的检查项目太长了，最长1000个字！");
		}
		if (request.getDicom_img_check_time() == null)
			throw new RequestException("提交诊断申请的影像的检查时间无效！");
		if (request.getDicom_img_checklist_num() != null) {
			request.setDicom_img_checklist_num(request.getDicom_img_checklist_num().trim());
			if (request.getDicom_img_checklist_num().length() >= 128)
				throw new RequestException("提交病例的图像的检查单号太长了，最长128个字！");
		}
		if (request.getDicom_img_device_type_id() <= 0)
			throw new RequestException("影像的检查设备必须指定！");
		if (request.getDicom_img_id() <= 0)
			throw new RequestException("影像必须指定！");
		if (StringTools.isEmpty(request.getDicom_img_mark_char()))
			throw new RequestException("影像的唯一标识必须指定！");
		// if (diagnosis.getDicom_img_part_type_id() <= 0)
		// throw new DiagnosisException("影像的检查部位必须指定！");
		// if (StringTools.isEmpty(diagnosis.getDicom_img_thumbnail_uid()))
		// throw new DiagnosisException("影像的缩略图UID必须指定！");
		if (request.getNote() != null) {
			request.setNote(request.getNote().trim());
			if (request.getNote().length() >= 2000)
				throw new RequestException("诊断申请备注太长了，最长2000个字！");
		}
		if (request.getPatient_id() <= 0)
			throw new RequestException("病人ID必须指定！");
		if (request.getPatient_birthday() == null)
			throw new RequestException("提交诊断申请的病人的生日必须指定！");
		if (request.getPatient_birthday().getTime() >= System.currentTimeMillis())
			throw new RequestException("提交诊断申请的病人的生日必须是过去的某个时间！");
		if (request.getPatient_born_address() != null) {
			request.setPatient_born_address(request.getPatient_born_address().trim());
			if (request.getPatient_born_address().length() >= 256)
				throw new RequestException("病人的出生地太长了，最长256个字！");
		}
		if (Gender.parseCode(request.getPatient_gender()) == null)
			throw new RequestException("病人的性别无效！");
		if (request.getPatient_home_address() != null) {
			request.setPatient_home_address(request.getPatient_home_address().trim());
			if (request.getPatient_home_address().length() >= 256)
				throw new RequestException("病人的居住地太长了，最长256个字！");
		}
		if (!StringTools.isEmpty(request.getPatient_identity_id())) {
			request.setPatient_identity_id(request.getPatient_identity_id());
			if (!StringTools.wasIdentityId(request.getPatient_identity_id()))
				throw new RequestException("病人的身份证号不是有效的身份证号码！");
		}
		if (StringTools.isEmpty(request.getPatient_name()))
			throw new RequestException("病人的姓名必须指定！");
		request.setPatient_name(request.getPatient_name().trim());
		if (request.getPatient_name().length() >= 64)
			throw new RequestException("病人的姓名太长了，最长64个字！");
		if (request.getPatient_sick_his() != null) {
			request.setPatient_sick_his(request.getPatient_sick_his().trim());
			if (request.getPatient_sick_his().length() >= 1024)
				throw new RequestException("病人的病史太长了，最长1024个字！");
		}
		if (PatientDataSourceType.parseCode(request.getPatient_source_type()) == null)
			throw new RequestException("病人的来源类型必须指定！");
		if (request.getPatient_work() != null) {
			request.setPatient_work(request.getPatient_work().trim());
			if (request.getPatient_work().length() >= 256)
				throw new RequestException("病人的工作单位太长了，最长256个字！");
		}
		if (request.getPublish_report_org_id() <= 0)
			throw new RequestException("发布报告的机构必须指定！");
		if (request.getRequest_org_id() <= 0)
			throw new RequestException("诊断申请的申请机构必须指定！");
		if (request.getRequest_user_id() <= 0)
			throw new RequestException("诊断申请的申请用户必须指定！");
		if (DiagnosisStatus2.parseFrom(request.getStatus()) == null)
			throw new RequestException("诊断申请的申请状态必须指定！");
		if (RequestClass.parseCode(request.getRequest_class()) == null)
			throw new RequestException("申请类别必须指定！");
		if (RequestUrgentLevel.parseCode(request.getUrgent_level()) == null)
			throw new RequestException("诊断申请不许指定紧急程度！");
	}

	/**
	 * 申请撤销
	 * 
	 * @param passport
	 *            身份认证
	 * @param lDiagnosisId
	 *            诊断ID
	 * @throws Exception
	 */
	public TDiagnosis cancelDiagnosis(Passport passport, long lDiagnosisId) throws Exception {
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MEDICAL_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(lDiagnosisId);
			if (diagnosis == null)
				throw new RequestException("诊断申请不存在！");
			if (diagnosis.getRequest_org_id() != passport.getOrgId())
				throw new RequestException("此诊断申请不属于你所在机构提交的申请，不能撤销！");
			if (diagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode())
				throw new RequestException("此申请状态下不能撤销！");
			diagnosis.setStatus(DiagnosisStatus2.CANCELED.getCode());
			mapper.updateDiagnosis(diagnosis);
			FinanceService.instance.getFinanceTrade().unFreezePrice(passport, diagnosis, session);

			List<FDiagnosis> listDiagnosis = this.queryDiagnosisByCaseHisId(diagnosis.getCase_his_id(), session);

			// Map<String, Object> mapArg = new HashMap<String, Object>();
			// mapArg.put("case_his_id", diagnosis.getCase_his_id());
			// List<FDiagnosis2> listDiagnosis = mapper.selectDiagnosis(mapArg);
			boolean isCanCancel = true;
			if (listDiagnosis != null && listDiagnosis.size() > 0) {
				for (TDiagnosis tDiagnosis : listDiagnosis) {
					if (diagnosis.getId() == tDiagnosis.getId())
						continue;
					if (tDiagnosis.getStatus() != DiagnosisStatus2.BACKED.getCode()
							&& tDiagnosis.getStatus() != DiagnosisStatus2.CANCELED.getCode()) {
						isCanCancel = false;
					}
				}
			}
			if (isCanCancel)
				CaseHistoryService.instance.cancelRequest(passport, diagnosis.getCase_his_id(), session);
			session.commit();
			return diagnosis;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过病历号查询诊断列表
	 * 
	 * @throws Exception
	 */
	public List<FDiagnosis> queryDiagnosisByCaseHisId(long case_his_id) throws Exception {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setCase_his_id(case_his_id);
		return this.searchDiagnosis(dsp);
	}

	/**
	 * 通过病历号查询诊断列表
	 * 
	 * @throws Exception
	 */
	public List<FDiagnosis> queryDiagnosisByCaseHisId(long case_his_id, SqlSession session) throws Exception {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setCase_his_id(case_his_id);
		return super.searchDiagnosis(dsp, session);
	}

	/**
	 * 撤销诊断申请！此方法只能指定类调用
	 * 
	 * @throws Exception
	 */
	public TDiagnosis cancelDiagnosis(long lDiagnosisId) throws Exception {
		if (!CommonTools.judgeCaller(MsgTimer.class))
			throw new RequestException("你无权调用此方法！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(lDiagnosisId);
			if (diagnosis == null)
				throw new RequestException("诊断申请不存在！");
			if (diagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode())
				throw new RequestException("此申请状态下不能撤销！");
			diagnosis.setStatus(DiagnosisStatus2.CANCELED.getCode());
			mapper.updateDiagnosis(diagnosis);
			FinanceService.instance.getFinanceTrade().unFreezePrice(null, diagnosis, session);
			List<FDiagnosis> listDiagnosis = this.queryDiagnosisByCaseHisId(diagnosis.getCase_his_id(), session);
			boolean isCanCancel = true;
			if (listDiagnosis != null && listDiagnosis.size() > 0) {
				for (TDiagnosis tDiagnosis : listDiagnosis) {
					if (diagnosis.getId() == tDiagnosis.getId())
						continue;
					if (tDiagnosis.getStatus() != DiagnosisStatus2.BACKED.getCode()
							&& tDiagnosis.getStatus() != DiagnosisStatus2.CANCELED.getCode()) {
						isCanCancel = false;
					}
				}
			}
			if (isCanCancel)
				CaseHistoryService.instance.cancelRequest(null, diagnosis.getCase_his_id(), session);
			session.commit();
			return diagnosis;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 诊断前拒绝诊断申请
	 * 
	 * @param passport
	 *            身份认证
	 * @param lDiagnosisId
	 *            诊断ID
	 * @param strNote
	 *            备注
	 * @throws Exception
	 */
	public void rejectDiagnosis(Passport passport, long lDiagnosisId, String strNote) throws Exception {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!(UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)
					|| UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)))
				throw new NotPermissionException();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(lDiagnosisId);
			if (diagnosis == null)
				throw new RequestException("指定的诊断申请不存在！");
			if (diagnosis.getDiagnosis_org_id() != passport.getOrgId())
				throw new RequestException("该申请不是提交给你所在单位的诊断，你不能执行此操作！");
			if (diagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode()) {
				throw new RequestException("你不能拒绝已经开始诊断或诊断完成的申请！");
			}
			List<FDiagnosis> listDiagnosis = DiagnosisService2.instance
					.queryDiagnosisByCaseHisId(diagnosis.getCase_his_id());
			boolean isCanCancel = true;
			if (listDiagnosis != null && listDiagnosis.size() > 0) {
				for (TDiagnosis tDiagnosis : listDiagnosis) {
					if (tDiagnosis.getId() == diagnosis.getId())
						continue;
					if (tDiagnosis.getStatus() != DiagnosisStatus2.BACKED.getCode()
							&& tDiagnosis.getStatus() != DiagnosisStatus2.CANCELED.getCode()) {
						isCanCancel = false;
					}
				}
			}
			if (isCanCancel)
				CaseHistoryService.instance.cancelRequest(passport, diagnosis.getCase_his_id(), session);
			FinanceService.instance.getFinanceTrade().unFreezePrice(passport, diagnosis, session);
			diagnosis.setStatus(DiagnosisStatus2.BACKED.getCode());
			diagnosis.setNote(strNote);
			mapper.updateDiagnosis(diagnosis);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 接受诊断申请，并开始诊断
	 * 
	 * @param passport
	 *            身份认证
	 * @param lDiagnosisId
	 *            诊断申请ID
	 * @throws BaseException
	 */
	private static final String addDiagnosisHandleLock = "添加诊断处理锁";

	public TDiagnosisHandle acceptDiagnosis(Passport passport, long lDiagnosisId) throws Exception {
		SqlSession session = null;
		try {
			synchronized (addDiagnosisHandleLock) {
				session = SessionFactory.getSession();
				if (!(UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)
						|| UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)))
					throw new NotPermissionException();
				TDiagnosisHandle handle = super.queryUnFinishHandle(passport);
				if (handle != null)
					throw new RequestException("你只能同时处理一个申请！");
				DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
				TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(lDiagnosisId);
				if (diagnosis == null)
					throw new RequestException("没有找到该申请！");
				if (diagnosis.getDiagnosis_org_id() != passport.getOrgId())
					throw new RequestException("该申请不是提交给你当前登录的机构的申请！");
				if (diagnosis.getStatus() != DiagnosisStatus2.NOTDIAGNOSIS.getCode())
					throw new RequestException("该申请不是处于待诊断状态！");
				handle = new TDiagnosisHandle();
				handle.setCreate_time(new Date());
				handle.setCurr_user_id(passport.getUserId());
				handle.setDiagnosis_id(lDiagnosisId);
				handle.setOrg_id(passport.getOrgId());
				handle.setCan_tranfer(HandleCanTranfer.CAN_TRANFER.getCode());
				handle.setStatus(DiagnosisHandleStatus2.WRITING.getCode());
				mapper.addDiagnosisHandle(handle);
				diagnosis.setAccept_user_id(passport.getUserId());
				diagnosis.setStatus(DiagnosisStatus2.DIAGNOSISING.getCode());
				diagnosis.setCurr_handle_id(handle.getId());
				diagnosis.setHandle_time(new Date());
				mapper.updateDiagnosis(diagnosis);
				MsgService.instance.cancelSendMessage(passport, lDiagnosisId, MsgType.DIAGNOSIS_REMIND, session);
				session.commit();
				return handle;
			}
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 撤销诊断，退回到未接受诊断状态
	 * 
	 * @param passport
	 *            身份认证
	 * @param lHandleId
	 *            诊断处理ID
	 * @throws BaseException
	 */
	public void revokeDiagnosis(Passport passport, long lHandleId) throws BaseException {
		// 只能第一个
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!(UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)
					|| UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)))
				throw new NotPermissionException();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			TDiagnosisHandle handle = mapper.queryHandleById(lHandleId);
			if (handle == null)
				throw new RequestException("没有找到该申请处理记录！");
			if (handle.getCurr_user_id() != passport.getUserId())
				throw new RequestException("此处理不是当前用户的处理记录！");
			if (handle.getStatus() != DiagnosisHandleStatus2.WRITING.getCode())
				throw new RequestException("当前处理记录不是书写中状态");

			TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(handle.getDiagnosis_id());
			if (diagnosis == null)
				throw new RequestException("没有找到该申请！");
			if (diagnosis.getDiagnosis_org_id() != passport.getOrgId())
				throw new RequestException("该申请不是提交给你当前登录的机构的申请！");
			if (diagnosis.getStatus() != DiagnosisStatus2.DIAGNOSISING.getCode()) {
				throw new RequestException("该申请不是处于正在诊断状态！");
			}
			if (diagnosis.getAccept_user_id() != passport.getUserId()) {
				throw new RequestException("你无权撤回不属于你接受的诊断处理！");
			}
			diagnosis.setStatus(DiagnosisStatus2.NOTDIAGNOSIS.getCode());
			diagnosis.setAccept_user_id(0);
			mapper.updateDiagnosis(diagnosis);
			mapper.deleteHandleById(handle.getId());
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 保存诊断处理意见
	 * 
	 * @param passport
	 *            身份认证
	 * @param lHandleId
	 *            申请处理ID
	 * @param strPic_opinion
	 *            影像所见
	 * @param strPic_diagnosis
	 *            诊断意见
	 * @param needWhole
	 *            是否需要检查完整性
	 * @throws BaseException
	 */
	public TDiagnosisHandle saveOpinion(Passport passport, TDiagnosisHandle diagnosisHandle) throws BaseException {
		return this.saveOpinion(passport, diagnosisHandle, false);
	}

	/**
	 * 保存诊断处理意见
	 * 
	 * @param passport
	 *            身份认证
	 * @param lHandleId
	 *            申请处理ID
	 * @param strPic_opinion
	 *            影像所见
	 * @param strPic_diagnosis
	 *            诊断意见
	 * @param needWhole
	 *            是否需要检查完整性
	 * @throws BaseException
	 */
	private TDiagnosisHandle saveOpinion(Passport passport, TDiagnosisHandle diagnosisHandle, boolean needWhole)
			throws BaseException {
		if (diagnosisHandle == null)
			throw new NullParameterException("diagnosisHandle");
		// 验证用户权限
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!(UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)
					|| UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)))
				throw new NotPermissionException();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			TDiagnosisHandle oldDiagnosisHandle = mapper.queryHandleByIdForUpdate(diagnosisHandle.getId());
			if (oldDiagnosisHandle == null)
				throw new RequestException("指定的处理记录不存在！");
			if (diagnosisHandle.equals(oldDiagnosisHandle))
				return oldDiagnosisHandle;
			if (oldDiagnosisHandle.getStatus() != DiagnosisHandleStatus2.WRITING.getCode())
				throw new RequestException("此诊断处理不是正在书写状态！");
			if (oldDiagnosisHandle.getCurr_user_id() != passport.getUserId()) {
				throw new RequestException("你不能修改他人的处理记录！");
			}
			TDiagnosis diagnosis = mapper.selectDiagnosisById(oldDiagnosisHandle.getDiagnosis_id());
			if (diagnosis == null)
				throw new RequestException("当前处理记录的诊断申请不存在！");
			if (diagnosis.getDiagnosis_org_id() != passport.getOrgId()) {
				throw new RequestException("此处理记录的申请不是你当前所登录机构的诊断申请！");
			}
			diagnosisHandle.setPic_conclusion(StringTools.strTrim(diagnosisHandle.getPic_conclusion()));
			diagnosisHandle.setPic_opinion(StringTools.strTrim(diagnosisHandle.getPic_opinion()));
			if (!StringTools.checkStr(diagnosisHandle.getPic_conclusion(), 1000, true))
				throw new SysOperateException("诊断意见不可长于1000个字！");
			if (!StringTools.checkStr(diagnosisHandle.getPic_opinion(), 1000, true))
				throw new SysOperateException("影像所见不可长于1000个字！");
			if (needWhole) {
				if (StringTools.isEmpty(diagnosisHandle.getPic_conclusion())
						|| StringTools.isEmpty(diagnosisHandle.getPic_opinion())) {
					throw new RequestException("‘影像所见’和‘诊断意见’必须填写！");
				}
			}
			oldDiagnosisHandle.setPic_conclusion(diagnosisHandle.getPic_conclusion());
			oldDiagnosisHandle.setPic_opinion(diagnosisHandle.getPic_opinion());
			oldDiagnosisHandle.setF_o_m(diagnosisHandle.getF_o_m());
			mapper.updateHandle(oldDiagnosisHandle);
			session.commit();
			return oldDiagnosisHandle;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 移交诊断
	 * 
	 * @param passport
	 * @param lUserId
	 * @param lHandleId
	 * @param strPicOpinion
	 * @param strPicConclusion
	 * @throws Exception
	 */
	public void tranferDiagnosis(Passport passport, Long lUserId, TDiagnosisHandle diagnosisHandle) throws Exception {
		diagnosisHandle = this.saveOpinion(passport, diagnosisHandle, true);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			boolean isAudit = UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT);
			boolean isReport = UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT);
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			diagnosisHandle = mapper.queryHandleByIdForUpdate(diagnosisHandle.getId());
			if (diagnosisHandle==null)
				throw new RequestException("没有找到诊断申请处理记录！");
			if (diagnosisHandle.getStatus() != DiagnosisHandleStatus2.WRITING.getCode())
				throw new RequestException("只有正在书写的诊断申请处理才可以被移交！");
			if (diagnosisHandle.getCan_tranfer() == HandleCanTranfer.NONE.getCode())
				throw new RequestException("你当前处理的是审核请求，不可以再移交，你只能直接发布报告！");
			if (!isAudit && !isReport)
				throw new RequestException("你没有权限移交诊断！");
			if (lUserId != null && lUserId > 0) {
				Set<UserPermission> listUserPermission = UserService.instance.getAllPermissionByUser(lUserId,
						passport.getOrgId());
				if (listUserPermission == null || listUserPermission.size() <= 0)
					throw new RequestException("你不能将诊断移交给该用户，因为其没有权限诊断！");
				if (isAudit) {
					if (!listUserPermission.contains(UserPermission.ORG_AUDIT))
						throw new RequestException("你是审核员用户，你只能将诊断移交给审核员用户！");
				} else {
					if (!listUserPermission.contains(UserPermission.ORG_REPORT))
						if (!listUserPermission.contains(UserPermission.ORG_AUDIT))
							throw new RequestException("要移交的目标用户没有处理诊断的权限");
				}
				HandleSearchParam hsp = new HandleSearchParam();
				hsp.setDiagnosis_id(diagnosisHandle.getDiagnosis_id());
				hsp.setCurr_user_id(lUserId);
				hsp.setOrg_id(passport.getOrgId());
				List<FDiagnosisHandle> listHandle = this.searchDiagnosisHandle(hsp);
				if (listHandle != null && listHandle.size() > 0)
					throw new RequestException("你要移交的医生已经诊断过此病例了！");
				diagnosisHandle.setStatus(DiagnosisHandleStatus2.TRANFERED.getCode());
			} else {
				lUserId = 0L;
				List<TDoctorUser> listDoctor = queryCanTranferDoctors(passport, diagnosisHandle.getDiagnosis_id());
				boolean bodoctorUser = false;
				for (TDoctorUser doctorUser : listDoctor) {
					Set<UserPermission> listUserPermission = UserService.instance
							.getAllPermissionByUser(doctorUser.getId(), passport.getOrgId());
					if (listUserPermission == null || listUserPermission.size() <= 0)
						continue;
					if (listUserPermission.contains(UserPermission.ORG_AUDIT))
						bodoctorUser = true;
				}
				if (!bodoctorUser)
					throw new RequestException("你的机构中缺少未诊断过此报告的审核员，请直接结束诊断并发布报告！");
				diagnosisHandle.setStatus(DiagnosisHandleStatus2.TRANFER_AUDIT.getCode());
			}
			TDiagnosis tmpDiagnosis = mapper.selectDiagnosisByIdForUpdate(diagnosisHandle.getDiagnosis_id());
			if (lUserId > 0)
				MsgService.instance.addWaitSendPlan(passport, tmpDiagnosis, lUserId, MsgType.NEW_TRANFAR_REMIND,
						session);
			diagnosisHandle.setNext_user_id(lUserId);
			diagnosisHandle.setTransfer_time(new Date());
			mapper.updateHandle(diagnosisHandle);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 接受移交的诊断
	 * 
	 * @param passport
	 * @param lHandleId
	 * @return
	 * @throws Exception
	 */
	public TDiagnosisHandle acceptTranferDiagnosis(Passport passport, long lHandleId) throws Exception {
		SqlSession session = null;
		try {
			synchronized (addDiagnosisHandleLock) {
				session = SessionFactory.getSession();
				DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
				boolean isReport = UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT);
				boolean isAudit = UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT);
				if (!isReport && !isAudit)
					throw new RequestException("你没有处理诊断的权限");
				if (queryUnFinishHandle(passport) != null)
					throw new RequestException("你只能同时处理一个申请！");
				TDiagnosisHandle lastHandle = mapper.queryHandleByIdForUpdate(lHandleId);
				if (lastHandle == null)
					throw new RequestException("你要接手的处理不存在！");
				int lastHandleStatus = lastHandle.getStatus();
				if (!isAudit) {
					if (lastHandle.getStatus() != DiagnosisHandleStatus2.TRANFERED.getCode())
						throw new RequestException("你不能接受此诊断移交！");
					if (lastHandle.getNext_user_id() != passport.getUserId())
						throw new RequestException("此诊断处理不是移交给你的！");
					lastHandle.setStatus(DiagnosisHandleStatus2.TRANFER_ACCEPTED.getCode());
				} else {
					if (lastHandle.getStatus() != DiagnosisHandleStatus2.TRANFER_AUDIT.getCode()) {
						if (lastHandle.getNext_user_id() != passport.getUserId())
							throw new RequestException("此诊断处理不是移交给你的！");
						lastHandle.setStatus(DiagnosisHandleStatus2.TRANFER_ACCEPTED.getCode());
					}
					lastHandle.setStatus(DiagnosisHandleStatus2.TRANFER_AUDIT_ACCEPTED.getCode());
				}
				TDiagnosis diagnosis = mapper.selectDiagnosisByIdForUpdate(lastHandle.getDiagnosis_id());
				if (diagnosis == null)
					throw new RequestException("没有找到该诊断申请！");
				if (diagnosis.getDiagnosis_org_id() != passport.getOrgId())
					throw new RequestException("该诊断申请不是提交给你当前登录的机构的申请！");
				if (diagnosis.getStatus() != DiagnosisStatus2.DIAGNOSISING.getCode()) {
					throw new RequestException("该诊断申请不是处于正在诊断状态！");
				}
				TDiagnosisHandle handle = new TDiagnosisHandle();
				handle.setCreate_time(new Date());
				handle.setCurr_user_id(passport.getUserId());
				handle.setDiagnosis_id(diagnosis.getId());
				handle.setOrg_id(passport.getOrgId());
				handle.setStatus(DiagnosisHandleStatus2.WRITING.getCode());
				if (lastHandleStatus == DiagnosisHandleStatus2.TRANFER_AUDIT.getCode())
					handle.setCan_tranfer(HandleCanTranfer.NONE.getCode());
				else
					handle.setCan_tranfer(HandleCanTranfer.CAN_TRANFER.getCode());
				mapper.addDiagnosisHandle(handle);
				mapper.updateHandle(lastHandle);
				diagnosis.setCurr_handle_id(handle.getId());
				mapper.updateDiagnosis(diagnosis);
				MsgService.instance.cancelSendMessage(passport, diagnosis.getId(), MsgType.NEW_TRANFAR_REMIND, session);
				session.commit();
				return handle;
			}
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 创建报告,并结束整个诊断过程
	 * 
	 * @param passport
	 * @param lHandleId
	 * @param lTemplateId
	 * @return
	 * @throws BaseException
	 */
	public TReport publishReport(Passport passport, TDiagnosisHandle diagnosisHandle) throws Exception {
		diagnosisHandle = this.saveOpinion(passport, diagnosisHandle, true);
		String sessionKey = StringTools.getUUID();
		VsSqlSession session = null;
		try {
			session = SessionFactory.getSession(sessionKey);
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT))
				throw new NotPermissionException();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			diagnosisHandle = mapper.queryHandleByIdForUpdate(diagnosisHandle.getId());
			TDiagnosis diagnosisTmp = mapper.selectDiagnosisByIdForUpdate(diagnosisHandle.getDiagnosis_id());
			if (diagnosisTmp == null)
				throw new RequestException("诊断未找到");
			if (diagnosisTmp.getDiagnosis_org_id() != passport.getOrgId())
				throw new RequestException("当前工作单位和诊断单位不符！");
			if (diagnosisTmp.getStatus() != DiagnosisStatus2.DIAGNOSISING.getCode())
				throw new RequestException("此申请现在不能发布报告！");
			if (diagnosisTmp.getCurr_handle_id() != diagnosisHandle.getId())
				throw new RequestException("此申请当前不是你在处理，不能发布报告！");
			if (diagnosisHandle.getStatus() != DiagnosisHandleStatus2.WRITING.getCode())
				throw new RequestException("诊断流程出错！");

			diagnosisHandle.setStatus(DiagnosisHandleStatus2.AUDITED.getCode());
			mapper.updateHandle(diagnosisHandle);
			TReport newReport = this.publishReport(passport, diagnosisTmp, diagnosisHandle, session);
			this.publishRequestTranferReport(passport, diagnosisTmp.getId(), diagnosisHandle, session);
			session.commit(sessionKey);
			return newReport;
		} catch (Exception e) {
			session.rollback(true, sessionKey);
			throw e;
		} finally {
			SessionFactory.closeSession(session, sessionKey);
		}
	}

	private TReport publishReport(Passport passport, TDiagnosis diagnosis, TDiagnosisHandle diagnosisHandle,
			VsSqlSession session) throws BaseException {
		TReport newReport = ReportService.instance.publishReport(passport, diagnosis, diagnosisHandle, session);
		MsgService.instance.addWaitSendPlan(passport, diagnosis, null, MsgType.DIAGNOSISED_REMIND, session);
		FinanceService.instance.getFinanceTrade().cashIn(passport, diagnosis, session);
		diagnosis.setStatus(DiagnosisStatus2.DIAGNOSISED.getCode());
		diagnosis.setVerify_user_id(passport.getUserId());
		session.getMapper(DiagnosisMapper.class).updateDiagnosis(diagnosis);
		return newReport;
	}

	private void publishRequestTranferReport(Passport passport, long new_request_id, TDiagnosisHandle diagnosisHandle,
			VsSqlSession session) throws IllegalArgumentException, IllegalAccessException, BaseException {
		DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
		SearchRequestTranferParam srtp = new SearchRequestTranferParam();
		srtp.setNew_request_id(new_request_id);
		List<FRequestTranfer> listRequestTranfer = RequestTranferService.instance.searchRequestTranfer(srtp, session);
		if (CollectionTools.isEmpty(listRequestTranfer))
			return;
		for (FRequestTranfer fRequestTranfer : listRequestTranfer) {
			TDiagnosis diagnosisTmp = mapper.selectDiagnosisByIdForUpdate(fRequestTranfer.getOld_request_id());
			if (diagnosisTmp == null)
				throw new RequestException("诊断未找到");
			if (diagnosisTmp.getStatus() != DiagnosisStatus2.ACCEPT_TRANFER.getCode())
				throw new RequestException("此申请现在不是处在已接受且已转交的状态，不能发布报告！");
			this.publishReport(passport, diagnosisTmp, diagnosisHandle, session);
			this.publishRequestTranferReport(passport, fRequestTranfer.getOld_request_id(), diagnosisHandle, session);
		}
	}

	/**
	 * 查询一个申请的详细信息，包括电子申请单信息
	 * 
	 * @param lDiagnosisId
	 * @return
	 * @throws OrgOperateException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws BaseException
	 * @throws Exception
	 */
	public FDiagnosis queryDiagnosisDetailById(long diagnosisId)
			throws IllegalArgumentException, IllegalAccessException, BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			FDiagnosis result = this.queryDiagnosisById(diagnosisId, true, session);
			if (result != null) {
				TPatient patient = PatientService.instance.queryPatientById(result.getPatient_id());
				result.setPatient(patient);
				TCaseHistory caseHistory = CaseHistoryService.instance.queryCaseHistoryById(result.getCase_his_id(),
						session);
				result.setCaseHistory(caseHistory);
				TDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(result.getDicom_img_id());
				result.setDicomImg(dicomImg);
			}
			return result;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询一个申请的详细信息，包括电子申请单信息
	 * 
	 * @param lDiagnosisId
	 * @return
	 * @throws OrgOperateException
	 * @throws RequestException 
	 * @throws DicomImgException 
	 * @throws Exception
	 */
	public FDiagnosis queryDiagnosisDetailById(long lDiagnosisId, SqlSession session)
			throws DicomImgException, RequestException {
		FDiagnosis diagnosis = this.queryDiagnosisById(lDiagnosisId, true, session);
		if (diagnosis == null)
			return null;
		if (diagnosis.getCase_eaf_list() == null || diagnosis.getCase_eaf_list().trim().isEmpty())
			return diagnosis;
		long[] fileIds = StringTools.StrToLongArray(diagnosis.getCase_eaf_list(), ',');
		List<TFile> listFile = FileService.instance.queryFileByIdList(fileIds);
		diagnosis.setEaf_file_list(listFile);
		return diagnosis;
	}

	/**
	 * 查询出所有处理过指定诊断的用户ID列表
	 * 
	 * @throws BaseException
	 */
	public List<Long> queryUserIdsByDiagnosisId(Passport passport, Long lDiagnosisId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (lDiagnosisId == null)
				throw new NullParameterException("diagnosisHandle");
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)) {
				if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)) {
					throw new NotPermissionException();
				}
			}
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);
			return mapper.queryUserIdByDiagnosisId(lDiagnosisId);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取当前用户可以移交的医生列表
	 * 
	 * @throws BaseException
	 */
	public List<TDoctorUser> queryCanTranferDoctors(Passport passport, Long lDiagnosisId) throws BaseException {
		try {
			List<TDoctorUser> listDoctor = UserService.instance.queryCanTranferDoctors(passport);
			List<Long> listDoctorIds = CommonTools
					.listToLongList(this.queryUserIdsByDiagnosisId(passport, lDiagnosisId));
			for (int i = listDoctor.size() - 1; i >= 0; i--) {
				TDoctorUser doctorUser = listDoctor.get(i);
				if (listDoctorIds.contains(doctorUser.getId())) {
					listDoctor.remove(i);
				}
			}
			return listDoctor;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 在被接手之间撤销诊断移交
	 * 
	 * @param takePassport
	 * @param lHandleId
	 * @throws Exception
	 * @throws BaseException
	 */
	public void cancelHandleTranFar(Passport passport, long handleId) throws Exception {
		TDiagnosisHandle handle = this.queryUnFinishHandle(passport);
		if (handle != null)
			throw new RequestException("你还有诊断未处理完成，请先处理完成才可以执行此操作！");
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			DiagnosisMapper mapper = session.getMapper(DiagnosisMapper.class);

			if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_REPORT)) {
				if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_AUDIT)) {
					throw new NotPermissionException("你没有审核员或者报告员权限，不能撤销！");
				}
			}
			handle = mapper.queryHandleByIdForUpdate(handleId);
			if (handle == null)
				throw new RequestException("指定的诊断处理不存在！");
			if (handle.getCurr_user_id() != passport.getUserId())
				throw new RequestException("指定的诊断处理不是你的处理，你无权撤销！");
			if (handle.getStatus() != DiagnosisHandleStatus2.TRANFERED.getCode()
					&& handle.getStatus() != DiagnosisHandleStatus2.TRANFER_AUDIT.getCode())
				throw new RequestException("指定的诊断处理已经被接受或者不能撤销！");
			handle.setStatus(DiagnosisHandleStatus2.WRITING.getCode());
			mapper.updateHandle(handle);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 检查申请是否重复提交
	 * 
	 * @throws Exception
	 */
	public boolean checkRepeatCommitDiagnosis(Passport passport, long caseHisId, long dicomImgId, long diagnosisOrgId,
			long productId) throws Exception {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setRequest_org_id(passport.getOrgId());
		dsp.setCase_his_id(caseHisId);
		dsp.setDicom_img_id(dicomImgId);
		dsp.setDiagnosis_org_id(diagnosisOrgId);
		// dsp.setDiagnosis_product_id(productId);
		List<FDiagnosis> listDiagnosis = super.searchDiagnosis(dsp);
		return !(listDiagnosis == null || listDiagnosis.isEmpty());
	}

	/**
	 * 查询提交给当前机构的诊断申请的待诊断的申请数量
	 * 
	 * @throws Exception
	 */
	public long queryWaitDiagnosisCount(Passport passport) throws Exception {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setDiagnosis_org_id(passport.getOrgId());
		dsp.setStatus(DiagnosisStatus2.NOTDIAGNOSIS);
		dsp.setSpu(new SplitPageUtil());
		super.searchDiagnosis(dsp);
		return dsp.getSpu().getTotalRow();
	}

	public List<FDiagnosis> queryDiagnosisByStatus(DiagnosisStatus2 status) throws Exception {
		DiagnosisSearchParams2 dsp = new DiagnosisSearchParams2();
		dsp.setStatus(status);
		return super.searchDiagnosis(dsp);
	}

	/**
	 * 获取在指定机构中转交给指定用户的诊断处理
	 * 
	 * @param userId
	 *            指定的被转交的用户
	 * @param orgId
	 *            指定的所在机构
	 * @param status
	 *            指定的状态，可选
	 * @return
	 * @throws BaseException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public List<FDiagnosisHandle> queryTranferToUserDiagnosisHandleByUserIdAndOrgIdAndStatus(long userId, long orgId,
			DiagnosisHandleStatus2 status, SplitPageUtil spu) throws IllegalArgumentException, IllegalAccessException {
		HandleSearchParam hsp = new HandleSearchParam();
		hsp.setNext_user_id(userId);
		hsp.setOrg_id(orgId);
		hsp.setStatus(status);
		hsp.setSpu(spu);
		return DiagnosisService2.instance.searchDiagnosisHandle(hsp);
	}

	/**
	 * 检查指定用户在指定机构中是否已经完成工作
	 * 
	 * @throws BaseException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public boolean checkFinishHandledByUserIdAndOrgId(long userId, long orgId)
			throws IllegalArgumentException, IllegalAccessException {
		if (DiagnosisService2.instance.queryUnFinishHandle(userId, userId) != null)
			return false;
		List<FDiagnosisHandle> listHandle = this.queryTranferToUserDiagnosisHandleByUserIdAndOrgIdAndStatus(userId,
				orgId, DiagnosisHandleStatus2.TRANFERED, null);
		if (listHandle != null && listHandle.size() > 0)
			return false;
		return true;
	}

	public List<FDiagnosisHandle> queryDiagnosisHandleByDiagnosisId(long diagnosis_id)
			throws IllegalArgumentException, IllegalAccessException {
		HandleSearchParam hsp = new HandleSearchParam();
		hsp.setDiagnosis_id(diagnosis_id);
		return DiagnosisService2.instance.searchDiagnosisHandle(hsp);
	}

	// /** 移交到审核的处理 */
	// public List<FDiagnosisHandle2>
	// searchTranferToAuditHandle(Passport passport, Long
	// requestOrgId, Long partTypeId, Long deviceTypeId, Gender
	// gender, String medicalHisNum, Long userId, SplitPageUtil
	// spu) { // TODO Auto-generated method stub return null; }

}
