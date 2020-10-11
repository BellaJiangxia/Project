package com.vastsoft.yingtai.provider.action;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.http.BaseAction;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.provider.FormType;
import com.vastsoft.yingtai.provider.ProviderFee;
import com.vastsoft.yingtai.provider.ProviderItem;
import com.vastsoft.yingtai.provider.ProviderService;
import com.vastsoft.yingtai.provider.exception.ProvideException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HonF on 17/1/17.
 */
public class ProviderAction extends BaseAction {
    private FormType from;
    private String strOrgName;
    private long lOrgId;
    private long lServiceId;

    private String strProductName;

    private String strProductCode;

    private String strUserName;
    private String strUserIDCard;
    private String strUserMobile;
    private Gender userGender;
    private String strComplaint;
    private Date dtReserveTime;
    private String strDocMobile;
    private List<ProviderItem> items;
    private List<ProviderFee> fees;

    private int iPageIdx;
    private int iPageSize;
    private SplitPageUtil spu;

    public String queryServiceOrgList(){
        try {
            this.addElementToData("orgs",ProviderService.instance.getReserveOrgList(this.from));
        } catch (ProvideException e) {
            catchException(e);
        }
        return SUCCESS;
    }

    public String queryServiceListByOrg(){
        try {
            this.addElementToData("services",ProviderService.instance.getServiceListByOrg(this.from,this.lOrgId,null,null,null, CheckServiceStatus.PUBLISH,null));
        } catch (ProvideException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String queryProductInfo(){
        try {
            this.addElementToData("product", ProviderService.instance.getServiceInfoById(this.from,this.lOrgId,this.lServiceId));
        } catch (ProvideException e) {
            catchException(e);
        }
        return SUCCESS;
    }

    public String sendReservation() {
        try {
            ProviderService.instance.sendReservation(this.from, this.lOrgId, this.lServiceId, this.strUserName, this.strUserIDCard, this.strUserMobile,
                    this.userGender, this.dtReserveTime, this.strComplaint,this.strDocMobile, this.items, this.fees);
        } catch (ProvideException e) {
            catchException(e);
        }
        return SUCCESS;
    }

    public void setFrom(int from) {
        this.from =FormType.parseCode(from);
    }

    public void setPageIdx(int iPageIdx) {
        this.iPageIdx = iPageIdx;
    }

    public void setPageSize(int iPageSize) {
        this.iPageSize = iPageSize;
    }

    public void setOrgName(String strOrgName) {
        this.strOrgName = strOrgName;
    }

    public void setOrgId(long lOrgId) {
        this.lOrgId = lOrgId;
    }

    public void setProductName(String strProductName) {
        this.strProductName = strProductName;
    }

    public void setServiceId(long lServiceId) {
        this.lServiceId = lServiceId;
    }

    public void setProductCode(String strProductCode) {
        this.strProductCode = strProductCode;
    }

    public void setUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public void setUserIdCard(String strUserIDCard) {
        this.strUserIDCard = strUserIDCard;
    }

    public void setUserMobile(String strUserMobile) {
        this.strUserMobile = strUserMobile;
    }

    public void setUserGender(int userGender) {
        this.userGender=Gender.parseCode(userGender);
    }

    public void setComplaint(String strUserComplaint) {
        this.strComplaint = strUserComplaint;
    }

    public void setReserveTime(String reserveTime) {
        if(reserveTime==null || reserveTime.isEmpty()) {
            this.dtReserveTime = new Date();
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                this.dtReserveTime=sdf.parse(reserveTime);
            } catch (ParseException e) {
                this.dtReserveTime = new Date();
            }
        }
    }

    public void setItems(String items) {
        List<ProviderItem> list = new ArrayList<ProviderItem>();
        if(items==null || items.isEmpty()){
            this.items=list;
        }else{
            try {
                JSONArray arr=new JSONArray(items);
                for (int i = 0,len=arr.length(); i < len; i++) {
                    JSONObject jo=arr.getJSONObject(i);
                    ProviderItem item=new ProviderItem();
                    item.setItemId(jo.getLong("item_id"));
                    item.setItemCount(jo.optInt("count",1));
                    list.add(item);
                }
                this.items=list;
            } catch (Exception e){
                this.items=list;
            }
        }
    }

    public void setFees(String fees) {
        List<ProviderFee> list = new ArrayList<ProviderFee>();
        if(fees==null || fees.isEmpty()){
            this.fees=list;
        }else{
            try {
                String[] arr=fees.split(",");
                for (int i = 0,len=arr.length; i < len; i++) {
                    if(arr[i]==null || arr[i].isEmpty()) continue;
                    ProviderFee fee=new ProviderFee();
                    fee.setFeeId(Long.parseLong(arr[i]));

                    list.add(fee);
                }
                this.fees=list;
            } catch (NumberFormatException e) {
                this.fees=list;
            }
        }
    }

    public void setDocMobile(String strDocMobile) {
        this.strDocMobile = strDocMobile;
    }
}
