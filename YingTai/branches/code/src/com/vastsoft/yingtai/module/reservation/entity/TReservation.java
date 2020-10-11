package com.vastsoft.yingtai.module.reservation.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.yingtai.module.reservation.constants.ReservationStatus;
import com.vastsoft.yingtai.module.user.constants.Gender;

/**
 * Created by vastsoft on 2016/9/7.
 */
public class TReservation {
  private long reservation_id;
  private String reservation_no;
  private long patient_id;
  private long request_org_id;
  private long request_org_user;
  private Date request_time;
  private long receive_org_id;
  private long receive_org_user;
  private Date receive_time;
  private Date reservation_time;
  private String reason;
  private int reservation_status;
  private String patient_name;
  private String patient_mobile;
  private int patient_gender;
  private String patient_idCard;

  private long report_id;
  private double total_price;
  private double pay_price;
  private String trade_no;
  private String complaint;
  private long case_id;
  private String docter_mobile;

  private List<TReservationService> listService;

  private String v_request_org;
  private String v_request_user;
  private String v_receive_org;
  private String v_receive_user;

  public long getReservation_id() {
    return reservation_id;
  }

  public void setReservation_id(long reservation_id) {
    this.reservation_id = reservation_id;
  }

  public String getReservation_no() {
    return reservation_no;
  }

  public void setReservation_no(String reservation_no) {
    this.reservation_no = reservation_no;
  }

  public long getRequest_org_id() {
    return request_org_id;
  }

  public void setRequest_org_id(long request_org_id) {
    this.request_org_id = request_org_id;
  }

  public Date getRequest_time() {
    return request_time;
  }

  public void setRequest_time(Date request_time) {
    this.request_time = request_time;
  }

  public long getReceive_org_id() {
    return receive_org_id;
  }

  public void setReceive_org_id(long receive_org_id) {
    this.receive_org_id = receive_org_id;
  }

  public Date getReceive_time() {
    return receive_time;
  }

  public void setReceive_time(Date receive_time) {
    this.receive_time = receive_time;
  }

  public Date getReservation_time() {
    return reservation_time;
  }

  public void setReservation_time(Date reservation_time) {
    this.reservation_time = reservation_time;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public long getReport_id() {
    return report_id;
  }

  public void setReport_id(long report_id) {
    this.report_id = report_id;
  }

  public double getTotal_price() {
    return total_price;
  }

  public void setTotal_price(double total_price) {
    this.total_price = total_price;
  }

  public double getPay_price() {
    return pay_price;
  }

  public void setPay_price(double pay_price) {
    this.pay_price = pay_price;
  }

  public String getTrade_no() {
    return trade_no;
  }

  public void setTrade_no(String trade_no) {
    this.trade_no = trade_no;
  }

  public long getPatient_id() {
    return patient_id;
  }

  public void setPatient_id(long patient_id) {
    this.patient_id = patient_id;
  }

  public long getRequest_org_user() {
    return request_org_user;
  }

  public void setRequest_org_user(long request_org_user) {
    this.request_org_user = request_org_user;
  }

  public long getReceive_org_user() {
    return receive_org_user;
  }

  public void setReceive_org_user(long receive_org_user) {
    this.receive_org_user = receive_org_user;
  }

  public int getReservation_status() {
    return reservation_status;
  }

  public void setReservation_status(int reservation_status) {
    this.reservation_status = reservation_status;
  }

  public List<TReservationService> getListService() {
    return listService;
  }

  public void setListService(List<TReservationService> listService) {
    this.listService = listService;
  }

  public ReservationStatus getReservationStatus(){
    return ReservationStatus.parseCode(this.reservation_status);
  }

  public String getV_request_org() {
    return v_request_org;
  }

  public void setV_request_org(String v_request_org) {
    this.v_request_org = v_request_org;
  }

  public String getV_request_user() {
    return v_request_user;
  }

  public void setV_request_user(String v_request_user) {
    this.v_request_user = v_request_user;
  }

  public String getV_receive_org() {
    return v_receive_org;
  }

  public void setV_receive_org(String v_receive_org) {
    this.v_receive_org = v_receive_org;
  }

  public String getV_receive_user() {
    return v_receive_user;
  }

  public void setV_receive_user(String v_receive_user) {
    this.v_receive_user = v_receive_user;
  }

  public String getPatient_name() {
    return patient_name;
  }

  public void setPatient_name(String patient_name) {
    this.patient_name = patient_name;
  }

  public String getPatient_mobile() {
    return patient_mobile;
  }

  public void setPatient_mobile(String patient_mobile) {
    this.patient_mobile = patient_mobile;
  }

  public int getPatient_gender() {
    return patient_gender;
  }

  public void setPatient_gender(int patient_gender) {
    this.patient_gender = patient_gender;
  }

  public String getPatient_idCard() {
    return patient_idCard;
  }

  public void setPatient_idCard(String patient_idCard) {
    this.patient_idCard = patient_idCard;
  }

  public Gender getPatientGender(){
    return Gender.parseCode(this.patient_gender);
  }

  public String getComplaint() {
    return complaint;
  }

  public void setComplaint(String complaint) {
    this.complaint = complaint;
  }

  public long getCase_id() {
    return case_id;
  }

  public void setCase_id(long case_id) {
    this.case_id = case_id;
  }

  public String getDocter_mobile() {
    return docter_mobile;
  }

  public void setDocter_mobile(String docter_mobile) {
    this.docter_mobile = docter_mobile;
  }
}
