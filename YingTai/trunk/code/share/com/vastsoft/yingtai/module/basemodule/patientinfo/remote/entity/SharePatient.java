package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareSystemIdentity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.user.constants.Gender;

public class SharePatient extends AbstractShareEntity {
	private long id;
	private String name;
	private String phone_num;
	private Date birthday;
	private String identity_id;
	private String home_address;
	private String mingzu;//民族
	private String marital_status;//婚姻状况 1-未婚 2-已婚 3 - 未知
	private int gender;
	private String contact_name;//联系人姓名
	private String contact_rela;//与病人关系
	private String contact_addr;//联系人地址
	private String contact_phone_num;//联系人电话
	private String born_address;
	private String work;
	private String sick_his;// 病史
	private Date create_time;
	private long create_user_id;
	private int status;
	
	
	
	public SharePatient(ShareExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public SharePatient(ShareSystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	public SharePatient() {
		super();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public int getAge() {
		return DateTools.getAgeByBirthday(birthday);
	}

	public String getHome_address() {
		return home_address;
	}

	public int getGender() {
		return gender;
	}

	public String getBorn_address() {
		return born_address;
	}

	public String getWork() {
		return work;
	}

	public String getSick_his() {
		return sick_his;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = StringTools.cutFHchar(name);
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setBorn_address(String born_address) {
		this.born_address = born_address;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setSick_his(String sick_his) {
		this.sick_his = sick_his;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getIdentity_id() {
		return identity_id;
	}

	public void setIdentity_id(String identity_id) {
		this.identity_id = identity_id;
	}

	public String getGenderStr() {
		Gender ug = Gender.parseCode(gender);
		return ug == null ? "" : ug.getName();
	}

	@Override
	@JSON(serialize = false)
	public SharePatientDataType getPdType() {
		return SharePatientDataType.PATIENT_DA;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();
		r.put("name", name);
		r.put("birthday", DateTools.dateToString(birthday));
		r.put("identity_id", identity_id);
		r.put("home_address", home_address);
		r.put("gender", gender);
		r.put("born_address", born_address);
		r.put("work", work);
		r.put("sick_his", sick_his);
		r.put("create_time", DateTools.dateToString(create_time));
		return r;
	}

	@Override
	protected void replaceFrom(AbstractShareEntity entity) {
		SharePatient patient = (SharePatient) entity;
		if (patient.birthday != null)
			this.birthday = patient.birthday;
		if (!StringTools.isEmpty(patient.born_address))
			this.born_address = patient.born_address;
		if (!StringTools.isEmpty(patient.contact_addr))
			this.contact_addr = patient.contact_addr;
		if (!StringTools.isEmpty(patient.contact_name))
			this.contact_name = patient.contact_name;
		if (!StringTools.isEmpty(patient.contact_phone_num))
			this.contact_phone_num = patient.contact_phone_num;
		if (!StringTools.isEmpty(patient.contact_rela))
			this.contact_rela = patient.contact_rela;
		if (patient.create_time != null)
			this.create_time = patient.create_time;
		if (Gender.parseCode(patient.gender) != null)
			this.gender = patient.gender;
		if (!StringTools.isEmpty(patient.home_address))
			this.home_address = patient.home_address;
		if (!StringTools.isEmpty(patient.identity_id))
			this.identity_id = patient.identity_id;
		if (!StringTools.isEmpty(patient.marital_status))
			this.marital_status = patient.marital_status;
		if (!StringTools.isEmpty(patient.mingzu))
			this.mingzu = patient.mingzu;
		if (!StringTools.isEmpty(patient.name))
			this.name = patient.name;
		if (!StringTools.isEmpty(patient.sick_his))
			this.sick_his = patient.sick_his;
		if (!StringTools.isEmpty(patient.work))
			this.work = patient.work;
	}

	@Override
	protected void mergeFrom(AbstractShareEntity re) {
		SharePatient patient = (SharePatient) re;
		if (this.birthday == null)
			this.birthday = patient.birthday;
		if (StringTools.isEmpty(this.born_address))
			this.born_address = patient.born_address;
		if (StringTools.isEmpty(this.contact_addr))
			this.contact_addr = patient.contact_addr;
		if (StringTools.isEmpty(this.contact_name))
			this.contact_name = patient.contact_name;
		if (StringTools.isEmpty(this.contact_phone_num))
			this.contact_phone_num = patient.contact_phone_num;
		if (StringTools.isEmpty(this.contact_rela))
			this.contact_rela = patient.contact_rela;
		if (this.create_time == null)
			this.create_time = patient.create_time;
		if (Gender.parseCode(this.gender) == null)
			this.gender = patient.gender;
		if (StringTools.isEmpty(this.home_address))
			this.home_address = patient.home_address;
		if (StringTools.isEmpty(this.identity_id))
			this.identity_id = patient.identity_id;
		if (StringTools.isEmpty(this.marital_status))
			this.marital_status = patient.marital_status;
		if (StringTools.isEmpty(this.mingzu))
			this.mingzu = patient.mingzu;
		if (StringTools.isEmpty(this.name))
			this.name = patient.name;
		if (StringTools.isEmpty(this.sick_his))
			this.sick_his = patient.sick_his;
		if (StringTools.isEmpty(this.work))
			this.work = patient.work;
	}
	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		this.birthday = DateTools.strToDate(jsonObject.optString("birthday", ""));
		this.born_address = jsonObject.optString("born_address", "");
		this.contact_addr = jsonObject.optString("contact_addr", "");
		this.contact_name = jsonObject.optString("contact_name", "");
		this.contact_phone_num = jsonObject.optString("contact_phone_num", "");
		this.contact_rela = jsonObject.optString("contact_rela", "");
		this.create_time = DateTools.strToDate(jsonObject.optString("create_time", ""));
		this.gender = jsonObject.optInt("gender", 1);
		this.home_address = jsonObject.optString("home_address", "");
		this.identity_id = jsonObject.optString("identity_id", "");
		this.marital_status = jsonObject.optString("marital_status", "未知");
		this.mingzu = jsonObject.optString("mingzu", "未知");
		this.name = jsonObject.optString("name", "");
		this.phone_num = jsonObject.optString("phone_num", "");
		this.sick_his = jsonObject.optString("sick_his", "");
		this.work = jsonObject.optString("work", "");
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_rela() {
		return contact_rela;
	}

	public void setContact_rela(String contact_rela) {
		this.contact_rela = contact_rela;
	}

	public String getContact_addr() {
		return contact_addr;
	}

	public void setContact_addr(String contact_addr) {
		this.contact_addr = contact_addr;
	}

	public String getContact_phone_num() {
		return contact_phone_num;
	}

	public void setContact_phone_num(String contact_phone_num) {
		this.contact_phone_num = contact_phone_num;
	}

	public String getMingzu() {
		return mingzu;
	}

	public void setMingzu(String mingzu) {
		this.mingzu = mingzu;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}
	@JSON(serialize=false)
	public String getMobile() {
		return this.phone_num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((born_address == null) ? 0 : born_address.hashCode());
		result = prime * result + ((contact_addr == null) ? 0 : contact_addr.hashCode());
		result = prime * result + ((contact_name == null) ? 0 : contact_name.hashCode());
		result = prime * result + ((contact_phone_num == null) ? 0 : contact_phone_num.hashCode());
		result = prime * result + ((contact_rela == null) ? 0 : contact_rela.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (create_user_id ^ (create_user_id >>> 32));
		result = prime * result + gender;
		result = prime * result + ((home_address == null) ? 0 : home_address.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((identity_id == null) ? 0 : identity_id.hashCode());
		result = prime * result + ((marital_status == null) ? 0 : marital_status.hashCode());
		result = prime * result + ((mingzu == null) ? 0 : mingzu.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone_num == null) ? 0 : phone_num.hashCode());
		result = prime * result + ((sick_his == null) ? 0 : sick_his.hashCode());
		result = prime * result + status;
		result = prime * result + ((work == null) ? 0 : work.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SharePatient other = (SharePatient) obj;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (born_address == null) {
			if (other.born_address != null)
				return false;
		} else if (!born_address.equals(other.born_address))
			return false;
		if (contact_addr == null) {
			if (other.contact_addr != null)
				return false;
		} else if (!contact_addr.equals(other.contact_addr))
			return false;
		if (contact_name == null) {
			if (other.contact_name != null)
				return false;
		} else if (!contact_name.equals(other.contact_name))
			return false;
		if (contact_phone_num == null) {
			if (other.contact_phone_num != null)
				return false;
		} else if (!contact_phone_num.equals(other.contact_phone_num))
			return false;
		if (contact_rela == null) {
			if (other.contact_rela != null)
				return false;
		} else if (!contact_rela.equals(other.contact_rela))
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (create_user_id != other.create_user_id)
			return false;
		if (gender != other.gender)
			return false;
		if (home_address == null) {
			if (other.home_address != null)
				return false;
		} else if (!home_address.equals(other.home_address))
			return false;
		if (id != other.id)
			return false;
		if (identity_id == null) {
			if (other.identity_id != null)
				return false;
		} else if (!identity_id.equals(other.identity_id))
			return false;
		if (marital_status == null) {
			if (other.marital_status != null)
				return false;
		} else if (!marital_status.equals(other.marital_status))
			return false;
		if (mingzu == null) {
			if (other.mingzu != null)
				return false;
		} else if (!mingzu.equals(other.mingzu))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone_num == null) {
			if (other.phone_num != null)
				return false;
		} else if (!phone_num.equals(other.phone_num))
			return false;
		if (sick_his == null) {
			if (other.sick_his != null)
				return false;
		} else if (!sick_his.equals(other.sick_his))
			return false;
		if (status != other.status)
			return false;
		if (work == null) {
			if (other.work != null)
				return false;
		} else if (!work.equals(other.work))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TPatient [id=" + id + ", name=" + name + ", phone_num=" + phone_num + ", birthday=" + birthday
				+ ", identity_id=" + identity_id + ", home_address=" + home_address + ", mingzu=" + mingzu
				+ ", marital_status=" + marital_status + ", gender=" + gender + ", contact_name=" + contact_name
				+ ", contact_rela=" + contact_rela + ", contact_addr=" + contact_addr + ", contact_phone_num="
				+ contact_phone_num + ", born_address=" + born_address + ", work=" + work + ", sick_his=" + sick_his
				+ ", create_time=" + create_time + ", create_user_id=" + create_user_id + ", status=" + status + "]";
	}

}
