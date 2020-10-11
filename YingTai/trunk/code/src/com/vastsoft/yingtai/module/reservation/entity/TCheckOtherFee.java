package com.vastsoft.yingtai.module.reservation.entity;

import com.vastsoft.yingtai.module.reservation.constants.ItemFeeCalcType;

/**
 * Created by vastsoft on 2016/9/8.
 */
public class TCheckOtherFee {
  private long service_id;
  private long fee_id;
  private String fee_name;
  private double price;
  private String description;
  private int fee_calc_type;
  private int is_required;

  public long getService_id() {
    return service_id;
  }

  public void setService_id(long service_id) {
    this.service_id = service_id;
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

  public ItemFeeCalcType getItemCalcType(){
    return ItemFeeCalcType.parseCode(this.fee_calc_type);
  }

  public int getIs_required() {
    return is_required;
  }

  public void setIs_required(int is_required) {
    this.is_required = is_required;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TCheckOtherFee otherFee = (TCheckOtherFee) o;

    return fee_id == otherFee.fee_id;
  }

  @Override
  public int hashCode() {
    return (int) (fee_id ^ (fee_id >>> 32));
  }
}
