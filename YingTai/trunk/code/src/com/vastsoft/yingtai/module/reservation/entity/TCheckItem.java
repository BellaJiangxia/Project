package com.vastsoft.yingtai.module.reservation.entity;

/**
 * Created by vastsoft on 2016/9/7.
 */
public class TCheckItem {
  private long service_id;
  private long item_id;
  private int is_required;
  private int check_count;

  private String v_item_name;

  public long getService_id() {
    return service_id;
  }

  public void setService_id(long service_id) {
    this.service_id = service_id;
  }

  public long getItem_id() {
    return item_id;
  }

  public void setItem_id(long item_id) {
    this.item_id = item_id;
  }

  public String getV_item_name() {
    return v_item_name;
  }

  public void setV_item_name(String v_item_name) {
    this.v_item_name = v_item_name;
  }

  public int getIs_required() {
    return is_required;
  }

  public void setIs_required(int is_required) {
    this.is_required = is_required;
  }

  public int getCheck_count() {
    return check_count;
  }

  public void setCheck_count(int check_count) {
    this.check_count = check_count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TCheckItem that = (TCheckItem) o;

    return item_id == that.item_id;
  }

  @Override
  public int hashCode() {
    return (int) (item_id ^ (item_id >>> 32));
  }
}
