package com.vastsoft.yingtai.module.reservation.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.reservation.entity.TCheckItem;
import com.vastsoft.yingtai.module.reservation.entity.TCheckOtherFee;
import com.vastsoft.yingtai.module.reservation.entity.TCheckService;
import com.vastsoft.yingtai.module.reservation.entity.TReservation;
import com.vastsoft.yingtai.module.reservation.entity.TReservationOtherFee;
import com.vastsoft.yingtai.module.reservation.entity.TReservationService;
import com.vastsoft.yingtai.module.reservation.entity.TReservationServiceItem;

public interface ReservationMapper {

  public void insertService(TCheckService t);

  public void modifyService(TCheckService t);

  public void deleteServiceById(long id);

  public void updateServiceStatus(Map<String, Object> prms);

  public List<TCheckService> selectListService(Map<String, Object> prms);

  public int selectListServiceCount(Map<String, Object> prms);

  public TCheckService selectServiceById(long id);

  public TCheckService lockServiceById(long id);

  public void insertServiceItem(TCheckItem t);

//  public void updateServiceItem(TCheckItem t);

  public void deleteServiceItemBySV(long id);

  public void insertServiceFee(TCheckOtherFee t);

//  public void updateServiceFee(TCheckOtherFee t);

  public void deleteServiceFeeBySV(long id);

  public List<TReservation> selectListReservation(Map<String, Object> prms);

  public int selectListReservationCount(Map<String, Object> prms);

  public TReservation selectReservationById(long id);

  public TReservation lockReservationById(long id);

  public void insertReservation(TReservation t);

  public void modifyReservation(TReservation t);

  public void deleteReservationById(long id);

  public void updateReservationStatus(Map<String, Object> prms);

  public void insertReservationService(TReservationService t);

  public void deleteReservationServiceByOD(long id);

  public void insertReservationServiceItem(TReservationServiceItem t);

  public void deleteReservationServiceItemByOD(long id);

  public void insertReservationOtherFee(TReservationOtherFee t);

  public void deleteReservationOtherFeeByOD(long id);

  public TCheckService selectExistsServiceByOrg(Map<String, Object> prms);
}
