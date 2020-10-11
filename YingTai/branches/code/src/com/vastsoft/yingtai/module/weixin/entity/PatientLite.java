package com.vastsoft.yingtai.module.weixin.entity;

import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;

/**
 * Created by vastsoft on 2016/8/4.
 */
public class PatientLite {
  private String name;
  private String identity_id;
  private int gender;
  private String work;
  private String born_addr;
  private String home_addr;
  private String postal_addr;
  private String mobile;
  private String sick_his;

  public PatientLite(TPatient tp) {
    this.name = tp.getName();
    this.identity_id = tp.getIdentity_id();
    this.gender = tp.getGender();
    this.work = tp.getWork();
    this.born_addr = tp.getBorn_address();
    this.home_addr = tp.getHome_address();
//    this.postal_addr = tp.get;
    this.mobile = tp.getMobile();
    this.sick_his = tp.getSick_his();
  }
}
