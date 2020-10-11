package com.vastsoft.yingtai.module.reservation.entity;

/**
 * Created by vastsoft on 2016/9/8.
 */
public class TReservationServiceItem {
  private long reservation_item_id;
  private long reservation_service_id;
  private long reservation_id;
  private long item_id;
  private int item_count;

  private String v_item_name;

  public long getReservation_item_id() {
    return reservation_item_id;
  }

  public void setReservation_item_id(long reservation_item_id) {
    this.reservation_item_id = reservation_item_id;
  }

  public long getReservation_id() {
    return reservation_id;
  }

  public void setReservation_id(long reservation_id) {
    this.reservation_id = reservation_id;
  }

  public long getItem_id() {
    return item_id;
  }

  public void setItem_id(long item_id) {
    this.item_id = item_id;
  }

  public int getItem_count() {
    return item_count;
  }

  public void setItem_count(int item_count) {
    this.item_count = item_count;
  }

  public long getReservation_service_id() {
    return reservation_service_id;
  }

  public void setReservation_service_id(long reservation_service_id) {
    this.reservation_service_id = reservation_service_id;
  }

  public String getV_item_name() {
    return v_item_name;
  }

  public void setV_item_name(String v_item_name) {
    this.v_item_name = v_item_name;
  }
}
