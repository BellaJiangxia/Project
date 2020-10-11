package com.vastsoft.yingtai.module.reservation.action;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;
import com.vastsoft.yingtai.module.reservation.constants.ReservationStatus;
import com.vastsoft.yingtai.module.reservation.entity.TCheckService;
import com.vastsoft.yingtai.module.reservation.entity.TReservation;
import com.vastsoft.yingtai.module.reservation.entity.TReservationService;
import com.vastsoft.yingtai.module.reservation.exception.ReservationException;
import com.vastsoft.yingtai.module.reservation.service.ReservationService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService;

public class ReservationAcion extends BaseYingTaiAction {

  private TCheckService service;
  private TReservation reserve;
  private long id;
  private boolean isPass;
  private SplitPageUtil spu;
  private TPatient patient;
  private List<TReservationService> listService;

  private String strServiceName;
  private Long lDeviceType;
  private Long lPartId;
  private CheckServiceStatus serviceStatus;

  private String strOrgName;
  private String strPatientName;
  private String strPatientMobile;
  private String strPatientIDCard;
  private String strReserveNo;
  private ReservationStatus reserveStatus;
  private String strReason;
  private Date dtReserveTime;

  public String saveService() {

    try {
      TCheckService tcs = null;
      if (service.getService_id() == 0) {
        tcs = ReservationService.instance.addCheckService(this.takePassport(), service.getService_name(), service.getDevice_type(), service.getService_price(), service.getDescription(), CheckFeeCalcType.parseCode(service.getCalc_type()), service.getListItem(), service.getListFee());
      } else {
        tcs = ReservationService.instance.modifyCheckService(this.takePassport(), this.service);
      }
      this.addElementToData("service", tcs);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String deleteService() {

    try {
      ReservationService.instance.deleteCheckService(takePassport(), this.id);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String publishService() {

    try {
      TCheckService tcs = null;
      if (service.getService_id() == 0) {
        tcs = ReservationService.instance.addCheckService(this.takePassport(), service.getService_name(), service.getDevice_type(), service.getService_price(), service.getDescription(), CheckFeeCalcType.parseCode(service.getCalc_type()), service.getListItem(), service.getListFee());
      } else {
        if(this.isPass==true) {
          tcs = ReservationService.instance.modifyCheckService(this.takePassport(), this.service);
        }else{
          tcs=this.service;
        }
      }

      if (tcs != null) ReservationService.instance.publishCheckService(this.takePassport(), tcs.getService_id());

    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryMyServiceList() {
    try {
      UserService.Passport passport = this.takePassport();
      List<TCheckService> list = ReservationService.instance.getListCheckService(passport, passport.getOrgId(), this.strServiceName, this.lDeviceType, this.lPartId, this.serviceStatus, this.spu);
      this.addElementToData("list", list);
      this.addElementToData("spu", spu);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryValidateService() {

    try {
      List<TCheckService> list = ReservationService.instance.getListCheckService(this.takePassport(), this.id, null, null, null, CheckServiceStatus.PUBLISH, this.spu);
      this.addElementToData("list", list);
      this.addElementToData("spu", spu);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryServiceById() {
    try {
      TCheckService service = ReservationService.instance.getCheckServiceById(this.takePassport(), this.id);
      this.addElementToData("service", service);
    } catch (NullParameterException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String saveReservation() {
    try {
      long id;
      if (this.reserve.getReservation_id() == 0) {
        id = ReservationService.instance.requestReservation(this.takePassport(), this.reserve.getReceive_org_id()
            , this.reserve.getPatient_id(), this.reserve.getPatient_name(), this.reserve.getPatient_idCard()
            , this.reserve.getPatient_mobile(), Gender.parseCode(this.reserve.getPatient_gender())
            , this.reserve.getReservation_time(), this.reserve.getComplaint(),this.reserve.getDocter_mobile()
            , this.reserve.getListService());
      } else {
        id = this.reserve.getReservation_id();
        ReservationService.instance.modifyReservation(this.takePassport(), this.reserve);
      }
      this.addElementToData("id", id);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String submitReservation() {

    try {
      long rid;
      if (this.id == 0) {
        if (this.reserve.getReservation_id() == 0) {
          rid = ReservationService.instance.requestReservation(this.takePassport(), this.reserve.getReceive_org_id()
              , this.reserve.getPatient_id(), this.reserve.getPatient_name(), this.reserve.getPatient_idCard()
              , this.reserve.getPatient_mobile(), Gender.parseCode(this.reserve.getPatient_gender())
              , this.reserve.getReservation_time(), this.reserve.getComplaint(),this.reserve.getDocter_mobile()
              , this.reserve.getListService());
        } else {
          rid = this.reserve.getReservation_id();
          ReservationService.instance.modifyReservation(this.takePassport(), this.reserve);
        }
      } else rid = this.id;

      ReservationService.instance.submitReservation(this.takePassport(), rid);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String cancelReservation() {

    try {
      ReservationService.instance.deleteReservationById(this.takePassport(), this.id);
    } catch (NullParameterException | ReservationException | DbException e) {
      this.catchException(e);
    }
    return SUCCESS;
  }

  public String acceptReservation() {

    try {
      ReservationService.instance.acceptReservation(this.takePassport(), this.id, this.dtReserveTime, this.isPass, this.strReason,this.strReserveNo);
    } catch (NullParameterException | DbException | ReservationException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String dealReservation() {

    try {
      ReservationService.instance.dealReservation(this.takePassport(), this.id, this.isPass, this.strReason,this.strReserveNo);
    } catch (NullParameterException | DbException | ReservationException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryRequestReserveList() {
    try {
      UserService.Passport passport = this.takePassport();

      List<TReservation> list = ReservationService.instance.getReservationList(passport, passport.getOrgId(), null, this.strOrgName, this.strPatientName, this.strPatientMobile, this.strPatientIDCard, this.strReserveNo, this.reserveStatus, this.spu);
      this.addElementToData("list", list);
      this.addElementToData("spu", spu);
    } catch (NullParameterException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryReceiveReserveList() {
    try {
      UserService.Passport passport = this.takePassport();

      List<TReservation> list = ReservationService.instance.getReservationList(passport, null, passport.getOrgId(), this.strOrgName, this.strPatientName, this.strPatientMobile, this.strPatientIDCard, this.strReserveNo, this.reserveStatus, this.spu);
      this.addElementToData("list", list);
      this.addElementToData("spu", spu);
    } catch (NullParameterException | DbException e) {
      this.catchException(e);
    }

    return SUCCESS;
  }

  public String queryReservationById() {
    try {
      TReservation reserve = ReservationService.instance.getReservationById(this.takePassport(), id);
      this.addElementToData("reserve", reserve);
    } catch (NullParameterException | DbException e) {
      this.catchException(e);
    }
    return SUCCESS;
  }

  public void setService(TCheckService service) {
    this.service = service;
  }

  public void setReserve(TReservation reserve) {
    this.reserve = reserve;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setSpu(SplitPageUtil spu) {
    this.spu = spu;
  }

  public void setIsPass(boolean isPass) {
    this.isPass = isPass;
  }

  public void setPatient(TPatient patient) {
    this.patient = patient;
  }

  public void setListService(List<TReservationService> listService) {
    this.listService = listService;
  }

  public void setServiceName(String strServiceName) {
    this.strServiceName = strServiceName;
  }

  public void setDeviceType(Long lDeviceType) {
    this.lDeviceType = lDeviceType;
  }

  public void setPartId(Long lPartId) {
    this.lPartId = lPartId;
  }

  public void setServiceStatus(int serviceStatus) {
    this.serviceStatus = CheckServiceStatus.parseCode(serviceStatus);
  }

  public void setOrgName(String strOrgName) {
    this.strOrgName = strOrgName;
  }

  public void setPatientName(String strPatientName) {
    this.strPatientName = strPatientName;
  }

  public void setPatientMobile(String strPatientMoble) {
    this.strPatientMobile = strPatientMoble;
  }

  public void setPatientIDCard(String strPatientIDCard) {
    this.strPatientIDCard = strPatientIDCard;
  }

  public void setReserveNo(String strReserveNo) {
    this.strReserveNo = strReserveNo;
  }

  public void setReserveStatus(Integer code) {
    this.reserveStatus = code == null ? null : ReservationStatus.parseCode(code);
  }

  public void setReason(String strReason) {
    this.strReason = strReason;
  }

  public void setReserveTime(Date dtReserveTime) {
    this.dtReserveTime = dtReserveTime;
  }
}
