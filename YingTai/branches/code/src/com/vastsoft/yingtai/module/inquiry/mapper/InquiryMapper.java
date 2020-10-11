package com.vastsoft.yingtai.module.inquiry.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.inquiry.entity.TInquiry;
import com.vastsoft.yingtai.module.inquiry.entity.TInquiryReply;

/**
 * Created by vastsoft on 2016/7/30.
 */
public interface InquiryMapper {

  public void insertInquiry(TInquiry t);

  public List<TInquiry> selectTInquiryList(Map<String,Object> prms);

  public TInquiry selectInquiryById(long id);

  public TInquiry selectInquiryByIdAndLock(long id);

  public void insertReply(TInquiryReply t);

  public List<TInquiry> selectReplyList(Map<String,Object> prms);

  public TInquiryReply selectReplyById(long id);

  public int selectInquiryCount(Map<String,Object> prms);

  public int selectReplyCount(Map<String,Object> prms);

  public void readInquiry(TInquiry t);

  public void modifyInquiry(TInquiry t);
}
