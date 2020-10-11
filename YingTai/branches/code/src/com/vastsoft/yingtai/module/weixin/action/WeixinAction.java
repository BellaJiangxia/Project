package com.vastsoft.yingtai.module.weixin.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.inquiry.exceptions.InquiryException;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.returnvisit.exceptions.ReturnVisitException;
import com.vastsoft.yingtai.module.weixin.entity.InquiryCount;
import com.vastsoft.yingtai.module.weixin.entity.TPatientOrgMapping;
import com.vastsoft.yingtai.module.weixin.service.LastCaseImportantInfo;
import com.vastsoft.yingtai.module.weixin.service.QuestionAnswer;
import com.vastsoft.yingtai.module.weixin.service.WeixinInterfaceService;
import com.vastsoft.yingtai.module.weixin.service.exceptions.ReleationException;

public final class WeixinAction extends BaseYingTaiAction
{
	private final static String ORG_LOGO_REF_URL = "/org_logo.action?fileId=";

	private String strWeixinUnionId;

	public void setWeixin_union_id(String strWeixinUnionId) {
		this.strWeixinUnionId = strWeixinUnionId;
	}

	private long lOrgId;

	public void setOrg_id(long lOrgId)
	{
		this.lOrgId = lOrgId;
	}

	private String strOrgName;

	private Long lInquiryId;

	public void setInquiry_id(Long lInquiryId) {
		this.lInquiryId = lInquiryId;
	}

	// 机构名/机构拼音
	public void setOrg_name(String strName)
	{
		this.strOrgName = strName;
	}

	/**
	 * 检索机构
	 * 
	 * @param weixin_union_id
	 *            微信ID
	 * @param name
	 *            拼音首字母/机构名称
	 * @return
	 */
	public String searchHospital()
	{
		System.out.println("[searchHospital]\t"+this.strWeixinUnionId+"\t"+this.strOrgName);

		try {
			List<TOrganization> orgList=WeixinInterfaceService.instance.queryAllOrg4wx(this.strWeixinUnionId,this.strOrgName,null);
			List<Map<String,Object>> list=new ArrayList<>();
			if(orgList!=null&&!orgList.isEmpty()){
				for(TOrganization org:orgList){
					Map<String,Object> map=new HashMap<>();
					map.put("id",org.getId());
					map.put("name",org.getOrg_name());
					map.put("logo_url",ORG_LOGO_REF_URL+org.getLogo_file_id());

					list.add(map);
				}
			}

			this.addElementToData("org_list",list);

		} catch (OrgOperateException | SystemException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 查询病人绑定过那些医院
	 * 
	 * @param weixin_union_id
	 * @return
	 */
	public String queryHospitalOfPatient()
	{
		System.out.println("[queryHospitalOfPatient]\t"+this.strWeixinUnionId);
		try {
			List<TPatientOrgMapping> orgs=WeixinInterfaceService.instance.queryOrgList4Binder(this.strWeixinUnionId);
			List<Map<String,Object>> list=new ArrayList<>();

			if(orgs!=null&&!orgs.isEmpty()){
				for(TPatientOrgMapping om:orgs){
					Map<String,Object> map=new HashMap<>();
					map.put("id", om.getOrg_id());
					map.put("name", om.getOrg_name());
					map.put("logo_url", ORG_LOGO_REF_URL+om.getLogo_id());
					map.put("card_num", om.getCard_num());
					map.put("last_case_date", om.getLast_jz_time());

					list.add(map);
				}
			}

			this.addElementToData("list_org",list);

		} catch (ReleationException | SystemException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}

	private String strUserRealName;

	// 用户真实姓名
	public void setUser_real_name(String strUserRealName)
	{
		this.strUserRealName = strUserRealName;
	}

	private long lCaseId;

	public void setCase_id(long lCaseId)
	{
		this.lCaseId = lCaseId;
	}

	private String strUserIdentityId;

	// 用户身份证
	public void setIdentity_id(String strUserIdentityId)
	{
		this.strUserIdentityId = strUserIdentityId;
	}

	/**
	 * @param hospital_id
	 *            医院ID
	 * @param identity_id
	 *            身份证号
	 * @return 被屏蔽的病人信息
	 */
	public String requestBindingPatient()
	{
		System.out.println("[requestBindingPatient]\t"+this.lOrgId+"\t"+this.strWeixinUnionId+"\t"+this.strUserRealName+"\t"+this.strUserIdentityId);
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			LastCaseImportantInfo lcii = WeixinInterfaceService.instance.requestBindPatient(this.lOrgId, this.strWeixinUnionId, this.strUserRealName, this.strUserIdentityId);

			this.addElementToData("gender", lcii.getGender().getName());
			this.addElementToData("birthday", lcii.getBirthDate() == null ? "" : sdf.format(lcii.getBirthDate()));
			this.addElementToData("org_id", lcii.getOrgId());
			this.addElementToData("org_name", lcii.getOrgName());
			this.addElementToData("org_log_url", ORG_LOGO_REF_URL + lcii.getOrgLogoFileId());
			this.addElementToData("card_num", lcii.getCardNum());

			this.addElementToData("last_case_type", lcii.getType().getCode());
			this.addElementToData("last_case_date", lcii.getBirthDate() == null ? "" : sdf.format(lcii.getCaseDate()));
			this.addElementToData("last_case_symptom", lcii.getSymptom());
			this.addElementToData("last_case_diagnoses", lcii.getDiagnoses());
		}
		catch (SystemException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 绑定病人身份与微信用户，绑定之前必须先请求绑定，否则无法实现绑定
	 * 
	 * @param weixin_union_id
	 * @return
	 */
	public String bindingPatient()
	{
		System.out.println("[bindingPatient]\t"+this.strWeixinUnionId);
		try
		{
			WeixinInterfaceService.instance.BindPatient(this.strWeixinUnionId);
		}
		catch (SystemException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 获取病人身份信息
	 * 
	 * @param weixin_union_id
	 * @return
	 */
	public String queryPatientInfo()
	{
		System.out.println("[queryPatientInfo]\t"+this.strWeixinUnionId);
		try
		{
			TPatient tPatient = WeixinInterfaceService.instance.queryPatientInfo(this.strWeixinUnionId);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", tPatient.getName());
			map.put("identity_id", tPatient.getIdentity_id());
			map.put("gender", tPatient.getGender());
			map.put("work", tPatient.getWork());
			map.put("born_addr", tPatient.getBorn_address());
			map.put("home_addr", tPatient.getHome_address());
			map.put("postal_addr", tPatient.getHome_address());
			map.put("mobile", tPatient.getMobile());
			map.put("sick_his", tPatient.getSick_his());

			this.addElementToData("patient", map);
		}
		catch (SystemException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 查询病人的病例列表
	 * 
	 * @param weixin_union_id
	 *            必选
	 * @param hospital_id
	 *            可选，为小于或等于0视作null,将不作筛选条件
	 * @return
	 */
	public String queryCaseHistory()
	{
		System.out.println("[queryCaseHistory]\t"+this.strWeixinUnionId+"\t"+this.lOrgId);
		try
		{
			List<TCaseHistory> listCase = WeixinInterfaceService.instance.queryCaseHistory(this.strWeixinUnionId, this.lOrgId, null);

			List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>(listCase.size());
			for (TCaseHistory tCH : listCase)
			{
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("case_id", tCH.getId());
				map.put("case_type", tCH.getType());
				map.put("case_enter_time", tCH.getEnter_time());
				map.put("case_leave_time", tCH.getLeave_time());
				map.put("symptom", tCH.getSymptom());
				map.put("diagnoses", tCH.getDiagnoses());
				map.put("hospital_dept", tCH.getReception_section());
				map.put("hospital_doctor", tCH.getReception_doctor());

				listRet.add(map);
			}

			this.addElementToData("list_case", listRet);
		}
		catch (SystemException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 查询详细的病例信息
	 * 
	 * @param weixin_union_id
	 *            必选
	 * @param case_id
	 *            必选
	 * @return
	 */
	public String queryCaseInfo()
	{
		System.out.println("[queryCaseInfo]\t"+this.strWeixinUnionId+"\t"+this.lCaseId);
		try
		{
			TCaseHistory tCase = WeixinInterfaceService.instance.queryCaseInfo(this.strWeixinUnionId, this.lCaseId);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("case_id", tCase.getId());
			map.put("case_type", tCase.getType());
			map.put("case_enter_time", tCase.getEnter_time());
			map.put("case_leave_time", tCase.getLeave_time());
			map.put("symptom", tCase.getSymptom());
			map.put("diagnoses", tCase.getDiagnoses());
			map.put("hospital_dept", tCase.getReception_section());
			map.put("hospital_doctor", tCase.getReception_doctor());

			this.addElementToData("case", map);
		}
		catch (SystemException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	private String strInquiryContent;

	public void setInquiry_content(String strInquiryContent)
	{
		this.strInquiryContent = strInquiryContent;
	}

	private List<Long> listCaseId;

	public void setList_caseId(List<Long> listCaseId)
	{
		this.listCaseId = listCaseId;
	}

	private List<String> inquiry_images;

	public void setInquiry_images(List<String> inquiry_images) {
		this.inquiry_images = inquiry_images;
	}

	/**
	 * 发起问诊
	 *
	 * @param weixin_union_id
	 *            微信ID
	 * @param hospital_id
	 *            医院ID
	 * @param case_id
	 *            病例ID
	 * @return 会话ID
	 */
	public String inquiry()
	{
		System.out.println("[inquiry]\t"+this.strWeixinUnionId+"\torg_id"+this.lOrgId+"\tinquiry_content"+this.strInquiryContent+"\tinquiry_id"+this.lInquiryId+"\tcase_id"+this.lCaseId+"\tinquiry_images"+this.inquiry_images);
		try
		{
			InquiryCount inquiryCount=new InquiryCount();
			List<Long> cases=null;
			if(this.lCaseId>0){
				cases=new ArrayList<>();
				cases.add(this.lCaseId);
			}
			long lSessionId = WeixinInterfaceService.instance.inquiryInCase(this.strWeixinUnionId, this.lOrgId, this.strInquiryContent,this.lInquiryId, cases,this.inquiry_images,inquiryCount);

			this.addElementToData("session_id", lSessionId);
			this.addElementToData("rest_count",inquiryCount!=null?inquiryCount.getCount():null);
		}catch (ReleationException | InquiryException | SystemException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	private long lReturnVisitSessionId;

	public void setReturn_visit_session_id(long lReturnVisitSessionId)
	{
		this.lReturnVisitSessionId = lReturnVisitSessionId;
	}

	private List<QuestionAnswer> listQuestionAnswer;

	public void setReturn_visit_reply(List<QuestionAnswer> listQuestionAnswer)
	{
		System.out.println("QA\t"+listQuestionAnswer);
		this.listQuestionAnswer = listQuestionAnswer;
	}

	/**
	 * 当用户从微信端反馈回访表
	 * 
	 * @return
	 */
	public String replyReturnVisit()
	{
		System.out.println("[replyReturnVisit]\t"+this.strWeixinUnionId+"\t"+this.lReturnVisitSessionId+"\t"+this.listQuestionAnswer);
		try {
			WeixinInterfaceService.instance.onReturnVisitReply(this.strWeixinUnionId, this.lReturnVisitSessionId, this.listQuestionAnswer);
		} catch (ReleationException | ReturnVisitException | SystemException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 关注
	 */
	public String subscribe()
	{
		try {
			WeixinInterfaceService.instance.onUserSubcribe(this.strWeixinUnionId);
		} catch (ReleationException | SystemException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 取消关注
	 */
	public String unsubscribe()
	{
		try {
			WeixinInterfaceService.instance.onUserUnsubscribe(this.strWeixinUnionId) ;
		} catch (ReleationException | SystemException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}


	//for debug
	/*
	private Long id;
	private String strContent;

	public void setId(Long id) {
		this.id = id;
	}

	public void setContent(String strContent) {
		this.strContent = strContent;
	}

	public String test_inquiry(){
		try
		{
			if(this.strContent==null || this.strContent.isEmpty()) throw new InquiryException("回复内容不能为空！");

			InquiryService.instance.sendInquiryReply(UserService.instance.getDeBug(),this.id,this.strContent);
		}
		catch (SystemException | NullParameterException | InquiryException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String test_visit(){
		try
		{
			TReturnVisitTemplate tmpl=ReturnVisitService.instance.queryTemplateById(UserService.instance.getDeBug(), 5L);
			List<Long> listPatientId=new ArrayList<>();
			listPatientId.add(688L);
			listPatientId.add(833L);

			long lBatchId=ReturnVisitService.instance.createReturnVisitBatch(UserService.instance.getDeBug(), "问卷"+System.currentTimeMillis(),tmpl.getListQ(),listPatientId,0);
			System.out.println(":::::::::::lBatchId\t"+lBatchId);
			ReturnVisitService.instance.sendReturnVisitByBatch(UserService.instance.getDeBug(),lBatchId);
		}
		catch (Exception e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}
	*/

}
