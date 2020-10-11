package com.vastsoft.yingtai.module.reservation.entity;

import com.vastsoft.yingtai.module.reservation.constants.ItemFeeCalcType;

/**
 * Created by vastsoft on 2016/9/8.
 */
public class TReservationOtherFee {
  private long reservation_fee_id;
  private long reservation_service_id;
  private long reservation_id;
  private long fee_id;
  private String fee_name;
  private double price;
  private String description;
  private int fee_calc_type;

  public long getReservation_id() {
    return reservation_id;
  }

  public void setReservation_id(long reservation_id) {
    this.reservation_id = reservation_id;
  }

  public long getReservation_fee_id() {
    return reservation_fee_id;
  }

  public void setReservation_fee_id(long reservation_fee_id) {
    this.reservation_fee_id = reservation_fee_id;
  }

  public long getFee_id() {
    return fee_id;
  }

  public void setFee_id(long fee_id) {
    this.fee_id = fee_id;
  }

  public String getFee_name() {
    return fee_name;
  }

  public void setFee_name(String fee_name) {
    this.fee_name = fee_name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getFee_calc_type() {
    return fee_calc_type;
  }

  public void setFee_calc_type(int fee_calc_type) {
    this.fee_calc_type = fee_calc_type;
  }

  public long getReservation_service_id() {
    return reservation_service_id;
  }

  public void setReservation_service_id(long reservation_service_id) {
    this.reservation_service_id = reservation_service_id;
  }

  public ItemFeeCalcType getItemCalcType(){
    return ItemFeeCalcType.parseCode(this.fee_calc_type);
  }
}
