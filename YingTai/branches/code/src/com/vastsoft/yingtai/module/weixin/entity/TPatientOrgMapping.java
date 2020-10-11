package com.vastsoft.yingtai.module.weixin.entity;

import java.util.Date;

/**
 * Created by vastsoft on 2016/8/5.
 */
public class TPatientOrgMapping {
  private long relation_id;
  private long patient_id;
  private long org_id;

  public long getRelation_id() {
    return relation_id;
  }

  public void setRelation_id(long relation_id) {
    this.relation_id = relation_id;
  }

  public long getPatient_id() {
    return patient_id;
  }

  public void setPatient_id(long patient_id) {
    this.patient_id = patient_id;
  }

  public long getOrg_id() {
    return org_id;
  }

  public void setOrg_id(long org_id) {
    this.org_id = org_id;
  }

  private String org_name;
  private long logo_id;
  private String card_num;
  private Date last_jz_time;

  public String getOrg_name() {
    return org_name;
  }

  public void setOrg_name(String org_name) {
    this.org_name = org_name;
  }

  public long getLogo_id() {
    return logo_id;
  }

  public void setLogo_id(long logo_id) {
    this.logo_id = logo_id;
  }

  public String getCard_num() {
    return card_num;
  }

  public void setCard_num(String card_num) {
    this.card_num = card_num;
  }

  public Date getLast_jz_time() {
    return last_jz_time;
  }

  public void setLast_jz_time(Date last_jz_time) {
    this.last_jz_time = last_jz_time;
  }
}
