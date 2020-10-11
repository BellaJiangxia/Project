package com.vastsoft.yingtai.module.reservation.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.entity.TCaseHistory;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.service.CaseHistoryService;
import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.entity.TPatient;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.exception.PatientException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.patient.service.PatientService;
import com.vastsoft.yingtai.module.org.realtion.exception.OrgRelationException;
import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;
import com.vastsoft.yingtai.module.reservation.constants.ItemFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.ReservationStatus;
import com.vastsoft.yingtai.module.reservation.entity.TCheckItem;
import com.vastsoft.yingtai.module.reservation.entity.TCheckOtherFee;
import com.vastsoft.yingtai.module.reservation.entity.TCheckService;
import com.vastsoft.yingtai.module.reservation.entity.TReservation;
import com.vastsoft.yingtai.module.reservation.entity.TReservationOtherFee;
import com.vastsoft.yingtai.module.reservation.entity.TReservationService;
import com.vastsoft.yingtai.module.reservation.entity.TReservationServiceItem;
import com.vastsoft.yingtai.module.reservation.exception.ReservationException;
import com.vastsoft.yingtai.module.reservation.mapper.ReservationMapper;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.service.UserService;

/**
 * Created by vastsoft on 2016/9/7.
 */
public class ReservationService {
  public final static ReservationService instance=new ReservationService();

  private ReservationService(){}

  public TCheckService addCheckService(UserService.Passport passport,String strServiceName,long lDeviceType,double dfPrice,String strDesc,CheckFeeCalcType calcType,List<TCheckItem> listItem,List<TCheckOtherFee> listFee) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");
    if(listItem==null || listItem.isEmpty()) throw new ReservationException("检查部位不能为空");

    SqlSession session=null;

    try {

      session= SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);
      List<TCheckItem> items=new ArrayList<>();
      List<TCheckOtherFee> fees=new ArrayList<>();

      TCheckService t=new TCheckService();
      t.setCreate_user(passport.getUserId());
      t.setCreate_time(new Date());
      t.setDevice_type(lDeviceType);
      t.setDescription(strDesc);
      t.setOrg_id(passport.getOrgId());
      t.setService_name(strServiceName);
      t.setService_price(dfPrice);
      t.setService_status(CheckServiceStatus.REGISTER.getCode());
      t.setCalc_type(calcType.getCode());

      mapper.insertService(t);

      for(TCheckItem item:listItem){
        TCheckItem tci=new TCheckItem();
        tci.setItem_id(item.getItem_id());
        tci.setService_id(t.getService_id());
        tci.setIs_required(item.getIs_required());
        tci.setCheck_count(item.getCheck_count());

        mapper.insertServiceItem(tci);
        items.add(tci);
      }

      if(listFee!=null&&!listFee.isEmpty()){
        for(TCheckOtherFee fee:listFee){
          TCheckOtherFee tf=new TCheckOtherFee();
          tf.setService_id(t.getService_id());
          tf.setDescription(fee.getDescription());
          tf.setFee_name(fee.getFee_name());
          tf.setPrice(fee.getPrice());
          tf.setFee_calc_type(fee.getFee_calc_type());
          tf.setIs_required(fee.getIs_required());

          mapper.insertServiceFee(tf);
          fees.add(tf);
        }
      }

      session.commit();

      t.setListItem(items);
      t.setListFee(fees);

      return t;
    } catch (Exception e) {
      if(session!=null) session.rollback();
      throw new DbException();
    } finally {
      SessionFactory.closeSession(session);
    }
  }

  public void publishCheckService(UserService.Passport passport,long lServiceId) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TCheckService service=mapper.lockServiceById(lServiceId);

      if(service==null) throw new ReservationException("未知的预约服务");

      if(service.getOrg_id()!=passport.getOrgId()) throw new ReservationException("非法的预约服务");

      if(CheckServiceStatus.INVALIDATE.equals(service.getServiceStatus())) throw new ReservationException("无效的预约服务");

      if(CheckServiceStatus.PUBLISH.equals(service.getServiceStatus())) throw new ReservationException("已经发布的服务");

      Map<String,Object> prms=new HashMap<>();
      prms.put("service_id",lServiceId);
      prms.put("status",CheckServiceStatus.PUBLISH.getCode());
      prms.put("publish_user",passport.getUserId());
      prms.put("publish_time",new Timestamp(new Date().getTime()));
      mapper.updateServiceStatus(prms);

      session.commit(true);

    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    } finally{
      SessionFactory.closeSession(session);
    }
  }

  public void deleteCheckService(UserService.Passport passport,long lServiceId) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TCheckService service=mapper.lockServiceById(lServiceId);

      if(service==null) throw new ReservationException("未知的预约服务");

      if(service.getOrg_id()!=passport.getOrgId()) throw new ReservationException("非法的预约服务");

      if(CheckServiceStatus.INVALIDATE.equals(service.getServiceStatus())) throw new ReservationException("无效的预约服务");

      //if(CheckServiceStatus.PUBLISH.equals(service.getServiceStatus())) throw new ReservationException("已经发布的服务不能删除");

      Map<String,Object> prms=new HashMap<>();
      prms.put("service_id",lServiceId);
      prms.put("status", CheckServiceStatus.INVALIDATE.getCode());
      prms.put("modify_user",passport.getUserId());
      prms.put("modify_time",new Timestamp(new Date().getTime()));
      mapper.updateServiceStatus(prms);

      session.commit(true);

    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    } finally{
      SessionFactory.closeSession(session);
    }
  }

  public TCheckService modifyCheckService(UserService.Passport passport,TCheckService service) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");
    if(service==null) throw new ReservationException("无效参数");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TCheckService ts=mapper.lockServiceById(service.getService_id());

      if(ts==null) throw new ReservationException("未知的预约服务");

      if(ts.getOrg_id()!=passport.getOrgId()) throw new ReservationException("非法的预约服务");

      if(CheckServiceStatus.INVALIDATE.equals(ts.getServiceStatus())) throw new ReservationException("无效的预约服务");

//      if(CheckServiceStatus.PUBLISH.equals(ts.getServiceStatus())) throw new ReservationException("已经发布的服务不能修改");

      mapper.modifyService(service);

      List<TCheckOtherFee> listFee=service.getListFee();
      List<TCheckItem> listItem=service.getListItem();

      List<TCheckOtherFee> newListFee=new ArrayList<>();
      List<TCheckItem> newListItem=new ArrayList<>();

      mapper.deleteServiceFeeBySV(ts.getService_id());
      mapper.deleteServiceItemBySV(ts.getService_id());

      if(listFee!=null && !listFee.isEmpty()){
       for(TCheckOtherFee fee:listFee){
         TCheckOtherFee tf=new TCheckOtherFee();
         tf.setFee_calc_type(fee.getFee_calc_type());
         tf.setPrice(fee.getPrice());
         tf.setFee_name(fee.getFee_name());
         tf.setDescription(fee.getDescription());
         tf.setService_id(ts.getService_id());
         tf.setIs_required(fee.getIs_required());

         mapper.insertServiceFee(tf);
         newListFee.add(tf);
       }
      }

      if(listItem!=null && !listItem.isEmpty()){
        for(TCheckItem item:listItem){
          TCheckItem ti=new TCheckItem();
          ti.setService_id(ts.getService_id());
          ti.setItem_id(item.getItem_id());
          ti.setIs_required(item.getIs_required());
          ti.setCheck_count(item.getCheck_count());

          mapper.insertServiceItem(ti);
          newListItem.add(ti);
        }
      }

      session.commit(true);

      service.setListItem(newListItem);
      service.setListFee(newListFee);

      return service;
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    } finally{
      SessionFactory.closeSession(session);
    }
  }

  public List<TCheckService> getListCheckService(UserService.Passport passport,long lOrgId,String strServiceName,Long lDeviceType,Long lPartId,CheckServiceStatus status,SplitPageUtil spu) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);
      Map<String,Object> prms=new HashMap<>();
      prms.put("org_id",lOrgId);
      prms.put("service_name",strServiceName);
      prms.put("device_type",lDeviceType);
      prms.put("part_id",lPartId);
      prms.put("service_status",status==null?null:status.getCode());
      if(spu!=null){
        int count=mapper.selectListServiceCount(prms);
        spu.setTotalRow(count);
        if(count==0) return null;
        prms.put("begin_idx", spu.getCurrMinRowNum());
        prms.put("end_idx",spu.getCurrMaxRowNum());
      }

      return session.getMapper(ReservationMapper.class).selectListService(prms);
    } catch (Exception e) {
      throw new DbException();
    } finally {
      SessionFactory.closeSession(session);
    }
  }

  public TCheckService getCheckServiceById(UserService.Passport passport,long lServiceId) throws NullParameterException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      return session.getMapper(ReservationMapper.class).selectServiceById(lServiceId);
    } catch(Exception e){
      throw new DbException();
    } finally{
      SessionFactory.closeSession(session);
    }
  }

  public void acceptReservation(UserService.Passport passport,long lReservationId,Date dtReserveTime,boolean isAccept,String strReason,String strReserveNo) throws NullParameterException, DbException, ReservationException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TReservation tr=mapper.lockReservationById(lReservationId);

      if(tr==null) throw new ReservationException("未知的预约");

      TReservation reservation=mapper.selectReservationById(tr.getReservation_id());

      if(reservation.getReceive_org_id()!=passport.getOrgId()) throw new ReservationException("非法的预约");

      if(!ReservationStatus.AUDITED.equals(reservation.getReservationStatus())) throw new ReservationException("无效的预约");

      List<TReservationService> listService=reservation.getListService();

      for(TReservationService trs:listService){
        TCheckService tsc=mapper.selectServiceById(trs.getService_id());

        if(tsc==null || tsc.getOrg_id()!=passport.getOrgId()) throw new ReservationException("存在非法的预约服务");
      }

      Map<String,Object> prms=new HashMap<>();
      prms.put("reserve_id",lReservationId);
      prms.put("status", isAccept ? ReservationStatus.ACCEPTED.getCode() : ReservationStatus.UNCHECK.getCode());
      prms.put("reason",strReason);
      prms.put("receive_org_user",passport.getUserId());
      prms.put("receive_time",new Timestamp(new Date().getTime()));
      prms.put("reservation_time",dtReserveTime==null?new Timestamp(tr.getReservation_time().getTime()):new Timestamp(dtReserveTime.getTime()));
      prms.put("reservation_no",strReserveNo);
      mapper.updateReservationStatus(prms);

      session.commit(true);
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }

  }

  public void dealReservation(UserService.Passport passport,long lReservationId,boolean isDeal,String strReason,String strReserveNo) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TReservation reservation=mapper.lockReservationById(lReservationId);

      if(reservation==null) throw new ReservationException("未知的预约");

      if(reservation.getReceive_org_id()!=passport.getOrgId()) throw new ReservationException("非法的预约");

      if(!ReservationStatus.ACCEPTED.equals(reservation.getReservationStatus())) throw new ReservationException("无效的预约");

      Map<String,Object> prms=new HashMap<>();

      if(isDeal&&strReserveNo!=null&&!strReserveNo.isEmpty()){
        TCaseHistory tch=CaseHistoryService.instance.queryCaseHistoryByPatientIdAndPacsNumPlaint(passport,reservation.getPatient_id(),strReserveNo,reservation.getComplaint());
        if(tch==null) throw new ReservationException("未找到检查的影像，请核对预约检查号是否正确");
        prms.put("case_id",tch.getId()<=0?null:tch.getId());
      }

      prms.put("reserve_id",lReservationId);
      prms.put("status", isDeal ? ReservationStatus.CHECKED.getCode() : ReservationStatus.UNCHECK.getCode());
      prms.put("reason",strReason);
      prms.put("receive_org_user",passport.getUserId());
      prms.put("reservation_no",strReserveNo);

      mapper.updateReservationStatus(prms);

      session.commit(true);
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }
  }

  public void submitReservation(UserService.Passport passport,long lReservationId) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TReservation reservation=mapper.lockReservationById(lReservationId);

      if(reservation==null) throw new ReservationException("未知的预约");

      if(reservation.getRequest_org_id()!=passport.getOrgId()) throw new ReservationException("非法的预约");

      if(!ReservationStatus.UNRESERVE.equals(reservation.getReservationStatus())) throw new ReservationException("当前状态无法申请预约");

      Map<String,Object> prms=new HashMap<>();
      prms.put("reserve_id",lReservationId);
      //TODO 提交申请暂时跳过审核流程，默认为审核通过
      prms.put("status", ReservationStatus.AUDITED.getCode());
      mapper.updateReservationStatus(prms);

      session.commit(true);
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }
  }

  public long requestReservation(UserService.Passport passport, long lOrgId,long lPatientId,String strPatientName
      ,String strPatientIDCard,String strPatientMobile,Gender gender,Date dtReserveTime
      ,String strComplaint,String strDocMobile,List<TReservationService> listService)
      throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    if(listService==null || listService.isEmpty()) throw new ReservationException("预约服务不能为空");

    SqlSession session=null;

    try {
//      if(!OrgRelationService.instance.checkFirendOrg(passport.getOrgId(),lOrgId))
//        throw new ReservationException("非合作单位不能预约");

      TPatient patient=new TPatient();
      patient.setId(lPatientId);
      patient.setName(strPatientName);
      patient.setIdentity_id(strPatientIDCard);
      patient.setPhone_num(strPatientMobile);
      patient.setGender(gender==null?Gender.MALE.getCode():gender.getCode());

      TPatient tp=PatientService.instance.addPatientAndOrgMapping(passport,patient);

      if(tp==null) throw new ReservationException("请正确填写预约人信息");

      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);

      TReservation t=new TReservation();
      t.setReservation_no("");
      t.setReservation_time(dtReserveTime);
      t.setPatient_id(tp.getId());
      t.setRequest_org_id(passport.getOrgId());
      t.setRequest_org_user(passport.getUserId());
      t.setReceive_org_id(lOrgId);
      t.setRequest_time(new Date());
      t.setReservation_status(ReservationStatus.UNRESERVE.getCode());
      t.setTotal_price(this.calcReservationFee(listService));
      t.setPatient_gender(patient.getGender());
      t.setPatient_idCard(patient.getIdentity_id());
      t.setPatient_mobile(patient.getMobile());
      t.setPatient_name(patient.getName());
      t.setComplaint(strComplaint);
      t.setDocter_mobile(strDocMobile);

      mapper.insertReservation(t);

      if(listService!=null) {

        for (TReservationService service : listService) {
          List<TReservationServiceItem> listItem = service.getListItem();
          List<TReservationOtherFee> listFee = service.getListFee();

          TCheckService tcs = this.validateCheckService4org(service.getService_id(), lOrgId, mapper);
          if (tcs == null) throw new ReservationException("预约服务中存在未知服务");

          TReservationService trs = new TReservationService();
          trs.setReservation_id(t.getReservation_id());
          trs.setService_id(tcs.getService_id());
          trs.setService_name(tcs.getService_name());
          trs.setService_price(tcs.getService_price());
          trs.setCalc_type(tcs.getCalc_type());
          trs.setDevice_type(tcs.getDevice_type());
          trs.setDiscription(tcs.getDescription());

          mapper.insertReservationService(trs);

          List<TCheckItem> items = tcs.getListItem();
          List<TCheckOtherFee> fees = tcs.getListFee();

          for (TReservationServiceItem item : listItem) {
            boolean isValid = false;
            for (TCheckItem i : items) {
              if (i.getItem_id() == item.getItem_id()) {
                isValid = true;
                TReservationServiceItem trsi = new TReservationServiceItem();
                trsi.setReservation_id(t.getReservation_id());
                trsi.setReservation_service_id(trs.getReservation_service_id());
                trsi.setItem_id(i.getItem_id());
                trsi.setItem_count(item.getItem_count());

                mapper.insertReservationServiceItem(trsi);

                break;
              }
            }
            if (!isValid) throw new ReservationException("不合法的预约检查部位");
          }

          for (TReservationOtherFee fee : listFee) {
            boolean isValid = false;
            for (TCheckOtherFee f : fees) {
              if (f.getFee_id() == fee.getFee_id()) {
                isValid = true;
                TReservationOtherFee trsf = new TReservationOtherFee();
                trsf.setReservation_id(t.getReservation_id());
                trsf.setReservation_service_id(trs.getReservation_service_id());
                trsf.setFee_id(f.getFee_id());
                trsf.setFee_name(f.getFee_name());
                trsf.setFee_calc_type(f.getFee_calc_type());
                trsf.setPrice(f.getPrice());
                trsf.setDescription(f.getDescription());

                mapper.insertReservationOtherFee(trsf);

                break;
              }
            }
            if (!isValid) throw new ReservationException("不合法的预约附件费用");
          }
        }
      }

      session.commit();

      return t.getReservation_id();
    } catch (PatientException | ReservationException e) {
      if(session!=null) session.rollback();
      throw new ReservationException(e.getMessage());
    }catch (Exception e){
      if(session!=null) session.rollback();
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }
  }

  public void modifyReservation(UserService.Passport passport,TReservation reservation) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");
    if(reservation==null) throw new ReservationException("无效的参数");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();

//      if(!OrgRelationService.instance.checkFirendOrg(passport.getOrgId(),reservation.getReceive_org_id()))
//        throw new ReservationException("非合作单位不能预约");

      TPatient patient=new TPatient();
      patient.setId(reservation.getPatient_id());
      patient.setName(reservation.getPatient_name());
      patient.setIdentity_id(reservation.getPatient_idCard());
      patient.setPhone_num(reservation.getPatient_mobile());
      patient.setGender(reservation.getPatient_gender());

      TPatient tp=PatientService.instance.addPatientAndOrgMapping(passport,patient);

      if(tp==null) throw new ReservationException("请正确填写预约人信息");

      ReservationMapper mapper=session.getMapper(ReservationMapper.class);
      TReservation tr=mapper.lockReservationById(reservation.getReservation_id());

      if(tr==null) throw new ReservationException("未知的预约");
      if(tr.getRequest_org_id()!=passport.getOrgId()) throw new ReservationException("非法的预约");
      if(!ReservationStatus.UNRESERVE.equals(tr.getReservationStatus())) throw new ReservationException("当前状态下无法进行修改");

      List<TReservationService> trs=reservation.getListService();
      if(trs==null || trs.isEmpty()) throw new ReservationException("未预约任何服务");

      TReservation t=new TReservation();
      t.setReservation_id(reservation.getReservation_id());
      t.setReservation_time(reservation.getReservation_time());
      t.setReceive_org_id(reservation.getReceive_org_id());
      t.setTotal_price(this.calcReservationFee(trs));
      t.setPatient_id(tp.getId());
      t.setPatient_gender(reservation.getPatient_gender());
      t.setPatient_idCard(reservation.getPatient_idCard());
      t.setPatient_mobile(reservation.getPatient_mobile());
      t.setPatient_name(reservation.getPatient_name());
      t.setComplaint(reservation.getComplaint());

      mapper.modifyReservation(t);

      mapper.deleteReservationServiceByOD(tr.getReservation_id());
      mapper.deleteReservationServiceItemByOD(tr.getReservation_id());
      mapper.deleteReservationOtherFeeByOD(tr.getReservation_id());

      for(TReservationService s:trs){
        TCheckService tcs=this.validateCheckService4org(s.getService_id(), tr.getReceive_org_id(), mapper);
        if(tcs==null) throw new ReservationException("预约服务中存在未知服务");

        List<TReservationServiceItem> listItem=s.getListItem();
        List<TReservationOtherFee> listFee=s.getListFee();
        if(listItem==null || listItem.isEmpty()) throw new ReservationException("预约的服务中未指定检查部位");

        s.setReservation_id(tr.getReservation_id());
        mapper.insertReservationService(s);

        for(TReservationServiceItem item:listItem){
          item.setReservation_id(tr.getReservation_id());
          item.setReservation_service_id(s.getReservation_service_id());
          mapper.insertReservationServiceItem(item);
        }

        for(TReservationOtherFee fee:listFee){
          fee.setReservation_id(tr.getReservation_id());
          fee.setReservation_service_id(s.getReservation_service_id());
          mapper.insertReservationOtherFee(fee);
        }
      }

      session.commit(true);
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }

  }

  public void deleteReservationById(UserService.Passport passport,long lReservationId) throws NullParameterException, ReservationException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);
      TReservation tr=mapper.lockReservationById(lReservationId);

      if(tr==null) throw new ReservationException("未知的预约");
      if(tr.getRequest_org_id()!=passport.getOrgId()) throw new ReservationException("非法的预约");
      if(!ReservationStatus.UNRESERVE.equals(tr.getReservationStatus())) throw new ReservationException("当前状态下无法删除");

      mapper.deleteReservationServiceByOD(tr.getReservation_id());
      mapper.deleteReservationServiceItemByOD(tr.getReservation_id());
      mapper.deleteReservationOtherFeeByOD(tr.getReservation_id());
      mapper.deleteReservationById(tr.getReservation_id());

      session.commit(true);
    } catch (ReservationException e) {
      if(session!=null) session.rollback(true);
      throw e;
    } catch(Exception e){
      if(session!=null) session.rollback(true);
      throw new DbException();
    }finally {
      SessionFactory.closeSession(session);
    }

  }

  public List<TReservation> getReservationList(UserService.Passport passport,Long lRequestOrgId,Long lRecevieOrgId,String strOrgName,String strPatientName,String strPatientMoble,String strIDCard,String strReservationNO,ReservationStatus status,SplitPageUtil spu) throws NullParameterException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      ReservationMapper mapper=session.getMapper(ReservationMapper.class);
      Map<String,Object> prms=new HashMap<>();
      prms.put("request_org",lRequestOrgId);
      prms.put("receive_org",lRecevieOrgId);
      prms.put("org_name",strOrgName==null || strOrgName.isEmpty()?null:strOrgName);
      prms.put("patient_name",strPatientName==null || strPatientName.isEmpty()?null:strPatientName);
      prms.put("patient_mobile",strPatientMoble==null || strPatientMoble.isEmpty()?null:strPatientMoble);
      prms.put("patient_idCard",strIDCard==null || strIDCard.isEmpty()?null:strIDCard);
      prms.put("reservation_no",strReservationNO==null || strReservationNO.isEmpty()?null:strReservationNO);
      prms.put("status",status==null?null:status.getCode());

      if(spu!=null){
        int count=mapper.selectListReservationCount(prms);
        spu.setTotalRow(count);
        if(count==0) return null;
        prms.put("begin_idx", spu.getCurrMinRowNum());
        prms.put("end_idx",spu.getCurrMaxRowNum());
      }

      return session.getMapper(ReservationMapper.class).selectListReservation(prms);
    } catch (Exception e) {
      throw new DbException();
    } finally {
      SessionFactory.closeSession(session);
    }
  }

  public TReservation getReservationById(UserService.Passport passport,long lReservationID) throws NullParameterException, DbException {
    if(passport==null) throw new NullParameterException("无效用户");

    SqlSession session=null;

    try {
      session=SessionFactory.getSession();
      return session.getMapper(ReservationMapper.class).selectReservationById(lReservationID);
    } catch(Exception e){
      throw new DbException();
    } finally{
      SessionFactory.closeSession(session);
    }
  }

  private double calcReservationFee(List<TReservationService> listService){
    double total=0d;

    for(TReservationService service:listService){

      List<TReservationServiceItem> listItem=service.getListItem();
      List<TReservationOtherFee> listFee=service.getListFee();

      if(listItem!=null&&!listItem.isEmpty()&&CheckFeeCalcType.EVERY_PART.equals(service.getServiceCalcType())){
        int count=listItem.size();
        total+=count*service.getService_price();
      }else if(listItem!=null&&!listItem.isEmpty()&&CheckFeeCalcType.EVER_EXPOSURE.equals(service.getServiceCalcType())){
        for(TReservationServiceItem item:listItem){
          total+=item.getItem_count()*service.getService_price();
        }
      }else{
        total+=service.getService_price();
      }

      for(TReservationOtherFee fee:listFee){
        if(ItemFeeCalcType.TOTAL.equals(fee.getItemCalcType())){
          total+=fee.getPrice();
        }else if(ItemFeeCalcType.EVEERY_ITEM.equals(fee.getItemCalcType())){
          int count=listItem.size();
          total+=count*fee.getPrice();
        }
      }
    }

    return total;
  }

  private TCheckService validateCheckService4org(long lCheckServiceId, long lOrgId, ReservationMapper mapper){
    if(mapper==null) return null;

    Map<String,Object> prms=new HashMap<>();
    prms.put("service_id",lCheckServiceId);
    prms.put("org_id",lOrgId);
    prms.put("service_status",CheckServiceStatus.PUBLISH.getCode());

    return mapper.selectExistsServiceByOrg(prms);
  }

  private boolean checkReserveService4org(long lOrgId,List<TReservationService> listService,ReservationMapper mapper){
    return false;
  }

  public int getReservationCount(UserService.Passport passport){
    if(passport==null) return 0;

    SqlSession session=null;

    try {
      session= SessionFactory.getSession();
      Map<String,Object> prms=new HashMap<>();

      prms.put("receive_org",passport.getOrgId());
      prms.put("status",ReservationStatus.AUDITED.getCode());

      return session.getMapper(ReservationMapper.class).selectListReservationCount(prms);
    } catch (Exception e) {
      return 0;
    } finally {
      SessionFactory.closeSession(session);
    }
  }

}
