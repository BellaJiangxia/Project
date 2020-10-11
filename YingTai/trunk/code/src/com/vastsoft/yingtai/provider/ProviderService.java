package com.vastsoft.yingtai.provider;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.mapper.OrgMapper;
import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;
import com.vastsoft.yingtai.module.reservation.entity.*;
import com.vastsoft.yingtai.module.reservation.exception.ReservationException;
import com.vastsoft.yingtai.module.reservation.mapper.ReservationMapper;
import com.vastsoft.yingtai.module.reservation.service.ReservationService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.provider.exception.ProvideException;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * Created by HonF on 17/1/17.
 */
public final class ProviderService {

    public final static ProviderService instance = new ProviderService();

    private ProviderService() {
    }

    private final static String ORG_LOGO_REF_URL = "org_logo.action?fileId=";

    /**
     * 获取预约机构列表
     *
     * @param formType
     * @return
     * @throws ProvideException
     */
    public List<Map<String, Object>> getReserveOrgList(FormType formType)
            throws ProvideException {

        if (formType == null) throw new ProvideException("不支持的接口类型");

        SqlSession session=null;

        try {
            session=SessionFactory.getSession();
            Map<String,Object> prms=new HashMap<String,Object>();
            prms.put("status", OrgStatus.VALID.getCode());
            List<TOrganization> orgs=session.getMapper(OrgMapper.class).selectReservationOrgList(prms);

            List<Map<String,Object>> list=new ArrayList<>();
            if(orgs!=null&&!orgs.isEmpty()){
                for(TOrganization org:orgs){
                    Map<String, Object> map = new HashMap<>();
                    map.put("orgId",org.getId());
                    map.put("orgName", org.getOrg_name());

                    list.add(map);
                }
            }

            return list;
        }catch (Exception e){
            return null;
        }finally {
            SessionFactory.closeSession(session);
        }
    }

    /**
     * 获取机构预约服务
     *
     * @param formType
     * @param lOrgId
     * @param strServiceName
     * @param lDeviceType
     * @param lPartId
     * @param status
     * @param spu
     * @return
     * @throws ProvideException
     */
    public List<Map<String, Object>> getServiceListByOrg(FormType formType, long lOrgId, String strServiceName, Long lDeviceType
            , Long lPartId, CheckServiceStatus status, SplitPageUtil spu) throws ProvideException {

        if (formType == null) throw new ProvideException("不支持的接口类型");

        if (lOrgId <=0) throw new ProvideException("未指定预约机构");

        SqlSession session=null;

        try {
            Map<String, Object> prms = new HashMap<String, Object>();
            prms.put("service_status",CheckServiceStatus.PUBLISH.getCode());
            prms.put("org_id", lOrgId);
            session=SessionFactory.getSession();
            List<TCheckService> tcs=session.getMapper(ReservationMapper.class).selectListService(prms);

            List<Map<String,Object>> list=new ArrayList<>();
            if(tcs!=null&&!tcs.isEmpty()){
                for(TCheckService service:tcs){
                    Map<String, Object> map = new HashMap<>();
                    map.put("serviceId",service.getService_id());
                    map.put("serviceName", service.getService_name());

                    list.add(map);
                }
            }

            return list;

        }catch (Exception e){
            return null;
        }finally {
            SessionFactory.closeSession(session);
        }
    }

    public TCheckService getServiceInfoById(FormType formType,long lOrgId, long lServiceId) throws ProvideException {

        if (formType == null) throw new ProvideException("不支持的接口类型");

        if (lOrgId <=0) throw new ProvideException("未指定预约机构");

        if (lServiceId <= 0) throw new ProvideException("未指定预约服务");

        SqlSession session=null;

        try {
            Map<String, Object> prms = new HashMap<String, Object>();
            prms.put("service_status",CheckServiceStatus.PUBLISH.getCode());
            prms.put("service_id", lServiceId);
            prms.put("org_id", lOrgId);
            session=SessionFactory.getSession();

            return session.getMapper(ReservationMapper.class).selectExistsServiceByOrg(prms);
        }catch (Exception e){
            return null;
        }finally {
            SessionFactory.closeSession(session);
        }
    }

    public void sendReservation(FormType formType,long lOrgId,long lProductId, String strPatientName
            , String strPatientIDCard, String strPatientMobile, Gender gender, Date dtReserveTime
            , String strComplaint,String strDoctorMobile,List<ProviderItem> listItem,List<ProviderFee> listFee) throws ProvideException {

        if (formType == null) throw new ProvideException("不支持的接口类型");

        if (lOrgId <= 0) throw new ProvideException("未指定预约机构");

        if (lProductId <= 0) throw new ProvideException("未指定预约服务");

        if (dtReserveTime == null) dtReserveTime=new Date();

        if (strPatientMobile == null || strPatientMobile.isEmpty() || strPatientName == null || strPatientName.isEmpty())
            throw new ProvideException("请提供相关的预约信息，如：姓名、手机号码等");

        try {
            TCheckService ts=this.getServiceInfoById(formType,lOrgId,lProductId);

            if (ts == null) throw new ProvideException("未知的预约服务");

            List<TReservationService> list = new ArrayList<TReservationService>();
            TReservationService rs=new TReservationService();

            List<TReservationServiceItem> items = new ArrayList<TReservationServiceItem>();

            if (listItem != null && !listItem.isEmpty()) {
                for (ProviderItem checkItem : listItem) {
                    if(ts.checkItem(checkItem.getItemId())){
                        TReservationServiceItem item = new TReservationServiceItem();
                        item.setItem_id(checkItem.getItemId());
                        if(CheckFeeCalcType.EVER_EXPOSURE.equals(ts.getFeeCalcType())) {
                            item.setItem_count(checkItem.getItemCount());
                        }
                        items.add(item);
                    }

                    else throw new ProvideException("该预约服务包含错误的检查部位");
                }
            }

            List<TReservationOtherFee> fees = new ArrayList<TReservationOtherFee>();

            if(listFee!=null && !listFee.isEmpty()){
                for (ProviderFee otherFee : listFee) {
                    if(ts.checkFee(otherFee.getFeeId())){
                        for (TCheckOtherFee checkOtherFee : ts.getListFee()) {
                            if(checkOtherFee.getFee_id()==otherFee.getFeeId()){
                                TReservationOtherFee fee = new TReservationOtherFee();
                                fee.setFee_id(checkOtherFee.getFee_id());
                                fee.setFee_calc_type(checkOtherFee.getFee_calc_type());
                                fee.setPrice(checkOtherFee.getPrice());

                                fees.add(fee);
                            }
                        }
                    }
                    else throw new ProvideException("该预约服务包含错误的收费项目");
                }
            }

            rs.setService_id(ts.getService_id());
            rs.setService_price(ts.getService_price());
            rs.setCalc_type(ts.getCalc_type());
            rs.setListItem(items);
            rs.setListFee(fees);

            list.add(rs);

            long id=ReservationService.instance.requestReservation(formType.getPassport(), ts.getOrg_id(), 0
                    , strPatientName, strPatientIDCard, strPatientMobile, gender, dtReserveTime, strComplaint,strDoctorMobile, list);

            ReservationService.instance.submitReservation(formType.getPassport(),id);
        } catch (NullParameterException | DbException e) {
            throw new ProvideException("系统错误");
        } catch (ReservationException e) {
            throw new ProvideException(e.getMessage());
        }
    }

    /*
    public List<Map<String, Object>> getReserveInfo(FormType formType, String strMobile, String strPatientName
            , String strPatientIDCard, SplitPageUtil spu) throws ProvideException {
        if (formType == null) throw new ProvideException("不支持的接口类型");

        if (strMobile == null && strMobile.isEmpty() && strPatientIDCard == null && strPatientIDCard.isEmpty() && strPatientName == null && strPatientName.isEmpty())
            throw new ProvideException("请提供相关的查询参数，如：姓名、身份证号码、手机号码等");

        try {
            List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
            List<TReservation> list = ReservationService.instance.getReservationList(formType.getPassport()
                    , formType.getPassport().getOrgId(), null, null, strPatientName, strMobile,strPatientIDCard, null, null, spu);

            if (list != null && !list.isEmpty()) {
                for (TReservation reservation : list) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("patient_name", reservation.getPatient_name());
                    map.put("patient_mobile", reservation.getPatient_mobile());
                    map.put("patient_gender", reservation.getPatientGender().getName());
                    map.put("complaint", reservation.getComplaint());
                    map.put("receive_org", reservation.getV_receive_org());
                    map.put("receive_user", reservation.getV_receive_user());
                    map.put("reserve_time", reservation.getReservation_time());
                    map.put("reserve_status", reservation.getReservationStatus().getName());
                    map.put("total_price", reservation.getTotal_price());
                    map.put("reserve_no",reservation.getReservation_no());
                    map.put("reason", reservation.getReason());

                    res.add(map);
                }
            }

            return res;
        } catch (NullParameterException e) {
            throw new ProvideException("无法识别的访问者");
        } catch (DbException e) {
            throw new ProvideException(e.getMessage());
        }
    }

    public Map<String, Object> getProductInfo(FormType formType, String strProvideCode) throws ProvideException {
        if (formType == null) throw new ProvideException("不支持的接口类型");

        if (strProvideCode == null || strProvideCode.isEmpty()) throw new ProvideException("缺少参数");

        SqlSession session = null;
        Map<String, Object> map = null;

        try {
            session = SessionFactory.getSession();
            Map<String, Object> prms = new HashMap<String, Object>();
            prms.put("code", strProvideCode);
            TReserveConfig info = session.getMapper(ReserveConfigMapper.class).selectReserveInfoByCode(prms);

            if (info != null) {
                map = new HashMap<String, Object>();
                map.put("product_id", info.getProduct_id());
                if (info.getOrg() != null) {
                    map.put("org_name", info.getOrg().getOrg_name());
                    map.put("org_logo_url", this.ORG_LOGO_REF_URL + info.getOrg().getLogo_file_id());
                }

                TCheckService checkService = info.getService();

                if (checkService != null) {
                    map.put("product_name", checkService.getService_name());
                    map.put("device_name", checkService.getV_device_name());
                    map.put("fee_type", checkService.getFeeCalcType().getName());
                    map.put("price", checkService.getService_price());
                    map.put("description", checkService.getDescription());
                    map.put("fees", fillFees(checkService.getListFee()));
                    map.put("items", fillItems(checkService.getListItem()));
                }
            }
        } catch (Exception e) {
            throw new ProvideException("系统错误");
        } finally {
            SessionFactory.closeSession(session);
        }

        return map;
    }

    private List<Map<String, Object>> fillItems(List<TCheckItem> listItem){
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        if(listItem!=null&&!listItem.isEmpty()){
            for (TCheckItem item : listItem) {
                Map<String, Object> mapItem = new HashMap<String, Object>();
                mapItem.put("item_id", item.getItem_id());
                mapItem.put("item_name", item.getV_item_name());

                items.add(mapItem);
            }
        }
        return items;
    }

    private List<Map<String, Object>> fillFees(List<TCheckOtherFee> listFee){
        List<Map<String, Object>> fees = new ArrayList<Map<String, Object>>();
        if(listFee!=null&&!listFee.isEmpty()) {
            for (TCheckOtherFee fee : listFee) {
                Map<String, Object> mapFee = new HashMap<String, Object>();
                mapFee.put("fee_id", fee.getFee_id());
                mapFee.put("fee_name", fee.getFee_name());
                mapFee.put("fee_price", fee.getPrice());
                mapFee.put("calc_type", fee.getItemCalcType().getName());
                mapFee.put("description", fee.getDescription());

                fees.add(mapFee);
            }
        }
        return fees;
    }
    */
}
