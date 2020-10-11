package com.vastsoft.yingtai.module.inquiry.action;

import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.inquiry.InquiryService;
import com.vastsoft.yingtai.module.inquiry.entity.TInquiry;
import com.vastsoft.yingtai.module.inquiry.exceptions.InquiryException;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.weixin.service.exceptions.ReleationException;

public class InquiryAction extends BaseYingTaiAction
{
	private String strKeyword;
	private boolean isMine;
	private SplitPageUtil spu;
	private Long id;
	private String strContent;

	public String queryNewInquiryList(){
		UserService.Passport passport = this.takePassport();

		try
		{
			List<TInquiry> list=InquiryService.instance.getInquiryList(passport,this.strKeyword,true,this.spu);

			this.addElementToData("list", list);
			this.addElementToData("spu",this.spu);
		}
		catch (SystemException | NullParameterException e)
		{
			this.catchException(e);
		}
		return SUCCESS;
	}

	public String queryReplyList(){
		try
		{
			UserService.Passport passport = this.takePassport();

			List<TInquiry> list=InquiryService.instance.getReplyList(passport,this.strKeyword,this.isMine?passport.getUserId():null,this.spu);

			this.addElementToData("list", list);
			this.addElementToData("spu",this.spu);
		}
		catch (SystemException | NullParameterException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String queryInquiryById(){
		try
		{
			TInquiry inquiry=InquiryService.instance.getInquiryById(this.takePassport(),this.id);

			this.addElementToData("inquiry", inquiry);
		}
		catch (SystemException | NullParameterException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String sendReply4Inquiry(){
		try
		{
			if(this.strContent==null || this.strContent.isEmpty()) throw new InquiryException("回复内容不能为空！");

			InquiryService.instance.sendInquiryReply(this.takePassport(),this.id,this.strContent);
		}
		catch (SystemException | NullParameterException | InquiryException | ReleationException e)
		{
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String newInquiryCount(){
		int count=InquiryService.instance.getNewInquiryCount(this.takePassport());
		this.addElementToData("count",count);
		return SUCCESS;
	}

	public void setKeyword(String strKeyword) {
		this.strKeyword = strKeyword;
	}

	public void setIsMine(String isMine) {
		this.isMine = Boolean.parseBoolean(isMine);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setContent(String strContent) {
		this.strContent = strContent;
	}
}
