package com.vastsoft.yingtai.module.user.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.yingtai.module.msg.constants.MsgType;
import com.vastsoft.yingtai.utils.configUtils.ConfigInf;
import com.vastsoft.yingtai.utils.configUtils.ConfigUtil;

/**用户个人配置**/
public class TUserConfig implements ConfigInf{
	private long user_id;
	private String sms_config="{}";
	
	public static class Sms_config{
		private int sms_type_code;
		private String sms_type_name;
		private boolean send;
		private boolean edit;
		private String desc;

		public Sms_config() {
			super();
		}
		public int getSms_type_code() {
			return sms_type_code;
		}
		public String getSms_type_name() {
			return sms_type_name;
		}
		public boolean isSend() {
			return send;
		}
		public void setSms_type_code(int sms_type_code) {
			this.sms_type_code = sms_type_code;
		}
		public void setSms_type_name(String sms_type_name) {
			this.sms_type_name = sms_type_name;
		}
		public void setSend(boolean send) {
			this.send = send;
		}
		public boolean isEdit() {
			return edit;
		}
		public void setEdit(boolean edit) {
			this.edit = edit;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	public boolean needSendMsgByType(int type){
		return needSendMsgByType(MsgType.parseCode(type));
	}
	
	public boolean needSendMsgByType(MsgType type){
		if (type==null)
			return false;
		if (!type.isEdit())
			return true;
		List<Sms_config> listSmsConfig=this.getListSmsConfig();
		if (listSmsConfig==null||listSmsConfig.size()<=0) 
			return true;
		for (Sms_config sms_config : listSmsConfig) {
			if (sms_config.getSms_type_code()==type.getCode())
				return sms_config.isSend();
		}
		return true;
	}
	
	public void setListSmsConfig(List<Sms_config> listSmsConfig) throws JSONException{
		if (listSmsConfig==null||listSmsConfig.size()<=0)
			return;
		ConfigUtil cu=new ConfigUtil(this);
		for (Sms_config sms_config : listSmsConfig) {
			cu.setValue("sms_config."+sms_config.getSms_type_code(), sms_config.isSend()?"true":"false");
		}
		this.sms_config=cu.getJson("sms_config");
	}
	
	public List<Sms_config> getListSmsConfig(){
		List<MsgType> listMsgType=MsgType.getAll();
		if (listMsgType==null||listMsgType.size()<=0)
			return null;
		List<Sms_config> result=new ArrayList<Sms_config>();
		ConfigUtil cu=new ConfigUtil(this);
		for (MsgType msgType : listMsgType) {
			if (!msgType.isShow())
				continue;
			Sms_config sc=new Sms_config();
			sc.setSend("true".equals(cu.getValue("sms_config."+msgType.getCode(), "true")));
			sc.setSms_type_code(msgType.getCode());
			sc.setSms_type_name(msgType.getName());
			sc.setEdit(msgType.isEdit());
			sc.setDesc(msgType.getDesc());
			result.add(sc);
		}
		return result;
	}
	
	@JSON(serialize=false)
	@Override
	public JSONObject getJsonObject() {
		JSONObject root=new JSONObject();
		try {
			JSONObject sms_configObj=new JSONObject((sms_config==null||sms_config.isEmpty())?"{}":sms_config);
			root.put("sms_config", sms_configObj);
			return root;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	@JSON(serialize=false)
	public String getSms_config() {
		return sms_config;
	}
	@JSON(deserialize=false)
	public void setSms_config(String sms_config) {
		this.sms_config = sms_config;
	}
}
