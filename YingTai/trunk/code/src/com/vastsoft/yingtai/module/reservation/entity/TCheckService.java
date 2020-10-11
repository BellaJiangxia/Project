package com.vastsoft.yingtai.module.reservation.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;

/**
 * Created by vastsoft on 2016/9/7.
 */
public class TCheckService {
  private long service_id;
  private String service_name;
  private long org_id;
  private long create_user;
  private Date create_time;
  private long publish_user;
  private Date publish_time;
  private long modify_user;
  private Date modify_time;
  private long device_type;
  private double service_price;
  private String description;
  private int calc_type;
  private int service_status;

  private List<TCheckItem> listItem;
  private List<TCheckOtherFee> listFee;

  private String v_device_name;
  private String v_org_name;

  public long getService_id() {
    return service_id;
  }

  public void setService_id(long service_id) {
    this.service_id = service_id;
  }

  public String getService_name() {
    return service_name;
  }

  public void setService_name(String service_name) {
    this.service_name = service_name;
  }

  public long getOrg_id() {
    return org_id;
  }

  public void setOrg_id(long org_id) {
    this.org_id = org_id;
  }

  public long getCreate_user() {
    return create_user;
  }

  public void setCreate_user(long create_user) {
    this.create_user = create_user;
  }

  public Date getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  public long getPublish_user() {
    return publish_user;
  }

  public void setPublish_user(long publish_user) {
    this.publish_user = publish_user;
  }

  public Date getPublish_time() {
    return publish_time;
  }

  public void setPublish_time(Date publish_time) {
    this.publish_time = publish_time;
  }

  public long getModify_user() {
    return modify_user;
  }

  public void setModify_user(long modify_user) {
    this.modify_user = modify_user;
  }

  public Date getModify_time() {
    return modify_time;
  }

  public void setModify_time(Date modify_time) {
    this.modify_time = modify_time;
  }

  public int getService_status() {
    return service_status;
  }

  public void setService_status(int service_status) {
    this.service_status = service_status;
  }

  public long getDevice_type() {
    return device_type;
  }

  public void setDevice_type(long device_type) {
    this.device_type = device_type;
  }

  public double getService_price() {
    return service_price;
  }

  public void setService_price(double service_price) {
    this.service_price = service_price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<TCheckItem> getListItem() {
    return listItem;
  }

  public void setListItem(List<TCheckItem> listItem) {
    this.listItem = listItem;
  }

  public List<TCheckOtherFee> getListFee() {
    return listFee;
  }

  public void setListFee(List<TCheckOtherFee> listFee) {
    this.listFee = listFee;
  }

  public int getCalc_type() {
    return calc_type;
  }

  public void setCalc_type(int calc_type) {
    this.calc_type = calc_type;
  }

  public CheckServiceStatus getServiceStatus(){
    return CheckServiceStatus.parseCode(this.service_status);
  }

  public String getV_device_name() {
    return v_device_name;
  }

  public void setV_device_name(String v_device_name) {
    this.v_device_name = v_device_name;
  }

  public CheckFeeCalcType getFeeCalcType(){
    return CheckFeeCalcType.parseCode(this.calc_type);
  }

  public String getV_org_name() {
    return v_org_name;
  }

  public void setV_org_name(String v_org_name) {
    this.v_org_name = v_org_name;
  }

  public boolean checkItem(long itemId){
    if(this.listItem==null || this.listItem.isEmpty() || itemId<=0) return false;

    for(TCheckItem item:this.listItem){
      if(item.getItem_id()==itemId) return true;
    }

    return false;
  }

  public boolean checkFee(long feeId){
    if(this.listFee==null || this.listFee.isEmpty() || feeId<=0) return false;

    for(TCheckOtherFee fee:this.listFee){
      if(fee.getFee_id()==feeId) return true;
    }

    return false;
  }
}
