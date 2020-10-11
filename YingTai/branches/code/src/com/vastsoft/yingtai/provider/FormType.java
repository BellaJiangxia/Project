package com.vastsoft.yingtai.provider;

import com.vastsoft.util.ResourcesUtil;
import com.vastsoft.yingtai.module.user.exception.UserOperateException;
import com.vastsoft.yingtai.module.user.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by HonF on 17/1/17.
 */
public final class FormType {
    private final int iCode;
    private final String strName;
    private final String strDesscription;

    private final static Map<Integer,FormType> mapFormType=new HashMap<>();
    private final static Map<FormType,UserService.Passport> mapPassport=new HashMap<>();

    public final static FormType MEDANY=new FormType(10,"medany_reservation_id","医享");
    public final static FormType WECHAT=new FormType(20,"wechat_reservation_id","微信");

    static{
        mapFormType.put(MEDANY.getCode(),MEDANY);
        mapFormType.put(WECHAT.getCode(), WECHAT);
    }

    public FormType(int iCode, String strName,String strDesscription) {
        this.iCode = iCode;
        this.strName = strName;
        this.strDesscription=strDesscription;
    }

    public int getCode() {
        return iCode;
    }

    public String getName() {
        return strName;
    }

    public UserService.Passport getPassport() {
        UserService.Passport passport=mapPassport.get(this);

        if(passport!=null) return passport;

        try {
            Properties p= ResourcesUtil.getResourceAsProperties("provide_config.properties");
            String configID=p.getProperty(this.getName(),"0");
            long orgId=configID.isEmpty()?0L:Long.parseLong(configID);
            passport = UserService.instance.getPrivodePassport(orgId);
            mapPassport.put(this, passport);
            return passport;
        } catch (IOException e) {
            System.out.println("未找到文件:provide_config.properties");
            return null;
        } catch (UserOperateException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static FormType parseCode(int iCode){
        return mapFormType.get(iCode);
    }
}
