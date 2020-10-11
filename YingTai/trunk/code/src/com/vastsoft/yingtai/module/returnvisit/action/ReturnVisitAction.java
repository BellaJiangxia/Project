package com.vastsoft.yingtai.module.returnvisit.action;

import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.returnvisit.BatchStatus;
import com.vastsoft.yingtai.module.returnvisit.ReturnVisitService;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisit;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitBatch;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplate;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitTemplateQuestion;
import com.vastsoft.yingtai.module.returnvisit.exceptions.ReturnVisitException;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.module.weixin.entity.TPatientExternalRelation;
import com.vastsoft.yingtai.module.weixin.service.WeixinInterfaceService;

public class ReturnVisitAction extends BaseYingTaiAction
{
	private String strTemplateName;

	public void setTemplate_name(String strTemplateName)
	{
		this.strTemplateName = strTemplateName;
	}

	private SplitPageUtil spu = null;

	public void setSpu(SplitPageUtil spu)
	{
		this.spu = spu;
	}

	private boolean bOnlyMe = false;

	public void setOnlyMe(boolean bOnlyMe)
	{
		this.bOnlyMe = bOnlyMe;
	}

	public String searchTemplates()
	{
		Passport passport = this.takePassport();

		try
		{
			List<TReturnVisitTemplate> listTpl = ReturnVisitService.instance.queryTemplate(passport, this.strTemplateName, this.bOnlyMe ? passport.getUserId() : null, this.spu);

			this.addElementToData("templates", listTpl);
			this.addElementToData("spu",this.spu);
		}
		catch (SystemException | NullParameterException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	private long lTemplateId = 0;

	public void setTemplate_id(long lTemplateId)
	{
		this.lTemplateId = lTemplateId;
	}

	public String queryTemplateById()
	{
		Passport passport = this.takePassport();

		try
		{
			TReturnVisitTemplate tpl = ReturnVisitService.instance.queryTemplateById(passport, lTemplateId);

			this.addElementToData("template", tpl);
		}
		catch (SystemException | NullParameterException | ReturnVisitException e)
		{
			this.catchException(e);
		}

		return SUCCESS;

	}

	public List<TReturnVisitTemplateQuestion> listQ = null;

	public void setListQ(List<TReturnVisitTemplateQuestion> listQ)
	{
		this.listQ = listQ;
	}

	public String saveTemplate()
	{
		Passport passport = this.takePassport();

		try
		{
			if (this.lTemplateId == 0)
			{
				ReturnVisitService.instance.createTemplate(passport, this.strTemplateName, this.listQ, true);
			}
			else
			{
				TReturnVisitTemplate tpl = new TReturnVisitTemplate();
				tpl.setTemplate_id(this.lTemplateId);
				tpl.setTemplate_name(this.strTemplateName);
				tpl.setListQ(this.listQ);
				ReturnVisitService.instance.modifyTemplate(passport, tpl, true);
			}
		}
		catch (SystemException | NullParameterException | ReturnVisitException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	private long lBatchId = 0;

	public void setBatch_id(long lBatchId)
	{
		this.lBatchId = lBatchId;
	}

	private String strVisitName = null;

	public void setVisit_name(String strVisitName)
	{
		this.strVisitName = strVisitName;
	}

	private List<Long> listPatientId = null;

	public void setPatientIds(List<Long> listPatientId)
	{
		this.listPatientId = listPatientId;
	}

	private String strPatientName;

	public void setPatient_name(String strPatientName)
	{
		this.strPatientName = strPatientName;

		if (this.strPatientName.isEmpty())
			this.strPatientName = null;
	}

	private BatchStatus status = null;

	public void setStatus(int iStatus)
	{
		this.status = BatchStatus.parseCode(iStatus);
	}

	private Boolean bSended = null;

	public void setSended(boolean bSended)
	{
		this.bSended = bSended;
	}

	public Boolean bReplied = null;

	public void setReplied(boolean bReplied)
	{
		this.bReplied = bReplied;
	}

	public String searchVisits()
	{
		Passport passport = this.takePassport();

		try
		{
			List<TReturnVisit> listRV = ReturnVisitService.instance.queryReturnVisit(passport, this.lBatchId, this.strVisitName, this.strPatientName, this.bOnlyMe ? passport.getUserId() : null, this.bSended, this.bReplied, spu);

			this.addElementToData("visits", listRV);
			this.addElementToData("spu",this.spu);
		}
		catch (NullParameterException | SystemException e)
		{
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String searchBatchs()
	{
		Passport passport = this.takePassport();

		try
		{
			List<TReturnVisitBatch> listBatch = ReturnVisitService.instance.queryReturnVisitBatch(passport, strTemplateName, this.bOnlyMe ? passport.getUserId() : null, this.status, spu);

			this.addElementToData("batchs", listBatch);
			this.addElementToData("spu",this.spu);
		}
		catch (NullParameterException | SystemException e)
		{
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String queryVisitInfoById(){
		Passport passport = this.takePassport();

		try
		{
			TReturnVisit visit=ReturnVisitService.instance.selectVisitInfoById(passport,this.lTemplateId);

			this.addElementToData("visit", visit);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String deleteTmplById(){
		Passport passport = this.takePassport();

		try
		{
			ReturnVisitService.instance.deleteTemplateById(passport,this.lTemplateId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return SUCCESS;
	}

	private Gender gender = null;

	public void setPatient_gender(int iGender)
	{
		this.gender = Gender.parseCode(iGender);
	}

	private String strMobile = null;

	public void setPatient_mobile(String strMobile)
	{
		this.strMobile = strMobile;

		if (this.strMobile.isEmpty())
			this.strMobile = null;
	}

	public String searchWeixinPatients()
	{

		Passport passport = this.takePassport();

		try
		{
			List<TPatientExternalRelation> listR = WeixinInterfaceService.instance.searchPatientsRelation(passport.getOrgId(), this.strPatientName, this.gender, this.strMobile);

			this.addElementToData("patients", listR);
		}
		catch (SystemException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String saveBatch()
	{
		Passport passport = this.takePassport();

//		if (this.lBatchId == 0)
//		{
			try
			{
				long id=ReturnVisitService.instance.createReturnVisitBatch(passport, this.strVisitName, this.listQ, this.listPatientId,this.lBatchId);

				this.addElementToData("batch_id",id);
			}
			catch (SystemException | NullParameterException e)
			{
				this.catchException(e);
			}
//		}
//		else
//		{
//			try
//			{
//				ReturnVisitService.instance.createReturnVisitBatch(passport, this.strVisitName, this.listQ, this.listPatientId);
//			}
//			catch (SystemException | NullParameterException e)
//			{
//				this.catchException(e);
//			}
//		}

		return SUCCESS;
	}

	public String sendBatch()
	{
		Passport passport = this.takePassport();

		try
		{
			if(this.lBatchId==0){
				this.lBatchId=ReturnVisitService.instance.createReturnVisitBatch(passport, this.strVisitName, this.listQ, this.listPatientId,this.lBatchId);
			}

			ReturnVisitService.instance.sendReturnVisitByBatch(passport,this.lBatchId);
		}
		catch (Exception e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String sendVisit(){
		Passport passport = this.takePassport();

		try
		{
			ReturnVisitService.instance.sendReturnVisitByVisit(passport, this.lTemplateId);
		}
		catch (Exception e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String visitFeedbackCount(){
		this.addElementToData("count",0);
		return SUCCESS;
	}

}
