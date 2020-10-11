package com.vastsoft.yingtai.module.reservation.entity;

import java.util.List;

import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;

/**
 * Created by vastsoft on 2016/9/7.
 */
public class TReservationService {
  private long reservation_service_id;
  private long reservation_id;
  private long service_id;
  private String service_name;
  private long device_type;
  private double service_price;
  private String discription;
  private int calc_type;

  private List<TReservationOtherFee> listFee;
  private List<TReservationServiceItem> listItem;

  private String v_device_name;

  public long getReservation_id() {
    return reservation_id;
  }

  public void setReservation_id(long reservation_id) {
    this.reservation_id = reservation_id;
  }

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

  public String getDiscription() {
    return discription;
  }

  public void setDiscription(String discription) {
    this.discription = discription;
  }

  public int getCalc_type() {
    return calc_type;
  }

  public void setCalc_type(int calc_type) {
    this.calc_type = calc_type;
  }

  public List<TReservationOtherFee> getListFee() {
    return listFee;
  }

  public void setListFee(List<TReservationOtherFee> listFee) {
    this.listFee = listFee;
  }

  public List<TReservationServiceItem> getListItem() {
    return listItem;
  }

  public void setListItem(List<TReservationServiceItem> listItem) {
    this.listItem = listItem;
  }

  public long getReservation_service_id() {
    return reservation_service_id;
  }

  public void setReservation_service_id(long reservation_service_id) {
    this.reservation_service_id = reservation_service_id;
  }

  public String getV_device_name() {
    return v_device_name;
  }

  public void setV_device_name(String v_device_name) {
    this.v_device_name = v_device_name;
  }

  public CheckFeeCalcType getServiceCalcType(){
    return CheckFeeCalcType.parseCode(this.calc_type);
  }
}
