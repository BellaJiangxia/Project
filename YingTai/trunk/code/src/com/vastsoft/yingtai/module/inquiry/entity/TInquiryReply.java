package com.vastsoft.yingtai.module.inquiry.entity;

import java.util.Date;

import com.vastsoft.yingtai.module.inquiry.ReplyType;

/**
 * Created by vastsoft on 2016/7/30.
 */
public class TInquiryReply {
  private long reply_id;
  private long inquiry_id;
  private long org_id;
  private long replier_id;
  private String reply_content;
  private Date reply_time;
  private int reply_type;

  private String replier_name;

  public long getReply_id() {
    return reply_id;
  }

  public void setReply_id(long reply_id) {
    this.reply_id = reply_id;
  }

  public long getInquiry_id() {
    return inquiry_id;
  }

  public void setInquiry_id(long inquiry_id) {
    this.inquiry_id = inquiry_id;
  }

  public long getOrg_id() {
    return org_id;
  }

  public void setOrg_id(long org_id) {
    this.org_id = org_id;
  }

  public long getReplier_id() {
    return replier_id;
  }

  public void setReplier_id(long replier_id) {
    this.replier_id = replier_id;
  }

  public String getReply_content() {
    return reply_content;
  }

  public void setReply_content(String reply_content) {
    this.reply_content = reply_content;
  }

  public Date getReply_time() {
    return reply_time;
  }

  public void setReply_time(Date reply_time) {
    this.reply_time = reply_time;
  }

  public int getReply_type() {
    return reply_type;
  }

  public void setReply_type(int reply_type) {
    this.reply_type = reply_type;
  }

  public String getReplier_name() {
    return replier_name;
  }

  public void setReplier_name(String replier_name) {
    this.replier_name = replier_name;
  }

  public ReplyType getReplyType(){
    return ReplyType.parseCode(this.reply_type);
  }
}
