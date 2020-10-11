package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareSystemIdentity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.org.configs.exception.NotFoundDeviceTypeMapperException;
import com.vastsoft.yingtai.module.org.configs.service.OrgConfigService;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;

public class ShareDicomImg extends AbstractShareEntity {
	private long id;
	private long case_id;
	// private long series_num;
	private long device_type_id;
	private long affix_id;
	private String eps_num;
	// private String thumbnail_uid;
	private String mark_char;
	private String ae_title;
	private String checklist_num;
	private Date create_time;
	private Date check_time;// 拍片时间
	private String check_pro;
	// private String thumbnail;
	private int status;
	
	public ShareDicomImg() {
		super();
	}

	public ShareDicomImg(ShareExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public ShareDicomImg(ShareSystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMark_char() {
		return mark_char;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public String getAe_title() {
		return ae_title;
	}

	public void setAe_title(String ae_title) {
		this.ae_title = ae_title;
	}

	public String getChecklist_num() {
		return checklist_num;
	}

	public void setChecklist_num(String checklist_num) {
		this.checklist_num = checklist_num;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCheck_time() {
		return check_time;
	}

	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}

	public long getAffix_id() {
		return affix_id;
	}

	public void setAffix_id(long affix_id) {
		this.affix_id = affix_id;
	}

	public String getCheck_pro() {
		return check_pro;
	}

	public void setCheck_pro(String check_pro) {
		this.check_pro = check_pro;
	}

	public long getCase_id() {
		return case_id;
	}

	public void setCase_id(long case_id) {
		this.case_id = case_id;
	}

	/** 只有当正常找到才返回对象，否则都返回null */
	public static TDicomImg findDicomImgFromListByMarkChar(List<TDicomImg> listDicomImg, String markChar) {
		if (listDicomImg == null || listDicomImg.size() <= 0 || markChar == null || markChar.trim().isEmpty()) {
			return null;
		}
		for (TDicomImg dicomImg : listDicomImg) {
			if (dicomImg.getMark_char().equals(markChar)) {
				return dicomImg;
			}
		}
		return null;
	}

	@Override
	@JSON(serialize = false)
	public SharePatientDataType getPdType() {
		return SharePatientDataType.DICOM_IMG;
	}

	public String getEps_num() {
		return eps_num;
	}

	public void setEps_num(String eps_num) {
		this.eps_num = eps_num;
	}

	@Override
	public String toString() {
		return "TDicomImg [id=" + id + ", case_id=" + case_id + ", part_name=" + affix_id + ", eps_num=" + eps_num
				+ ", mark_char=" + mark_char + ", ae_title=" + ae_title + ", checklist_num=" + checklist_num
				+ ", create_time=" + create_time + ", check_time=" + check_time + ", check_pro=" + check_pro
				+ ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ae_title == null) ? 0 : ae_title.hashCode());
		result = prime * result + (int) (affix_id ^ (affix_id >>> 32));
		result = prime * result + (int) (case_id ^ (case_id >>> 32));
		result = prime * result + ((check_pro == null) ? 0 : check_pro.hashCode());
		result = prime * result + ((check_time == null) ? 0 : check_time.hashCode());
		result = prime * result + ((checklist_num == null) ? 0 : checklist_num.hashCode());
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (device_type_id ^ (device_type_id >>> 32));
		result = prime * result + ((eps_num == null) ? 0 : eps_num.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((mark_char == null) ? 0 : mark_char.hashCode());
		result = prime * result + status;
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
		ShareDicomImg other = (ShareDicomImg) obj;
		if (ae_title == null) {
			if (other.ae_title != null)
				return false;
		} else if (!ae_title.equals(other.ae_title))
			return false;
		if (affix_id != other.affix_id)
			return false;
		if (case_id != other.case_id)
			return false;
		if (check_pro == null) {
			if (other.check_pro != null)
				return false;
		} else if (!check_pro.equals(other.check_pro))
			return false;
		if (check_time == null) {
			if (other.check_time != null)
				return false;
		} else if (!check_time.equals(other.check_time))
			return false;
		if (checklist_num == null) {
			if (other.checklist_num != null)
				return false;
		} else if (!checklist_num.equals(other.checklist_num))
			return false;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (device_type_id != other.device_type_id)
			return false;
		if (eps_num == null) {
			if (other.eps_num != null)
				return false;
		} else if (!eps_num.equals(other.eps_num))
			return false;
		if (id != other.id)
			return false;
		if (mark_char == null) {
			if (other.mark_char != null)
				return false;
		} else if (!mark_char.equals(other.mark_char))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();
		r.put("eps_num", eps_num);
		r.put("mark_char", mark_char);
		r.put("ae_title", ae_title);
		r.put("checklist_num", checklist_num);
		r.put("create_time", DateTools.dateToString(create_time));
		r.put("check_time", DateTools.dateToString(check_time));
		r.put("check_pro", check_pro);
		return r;
	}

	@Override
	protected void replaceFrom(AbstractShareEntity entity) {
		ShareDicomImg dicomImg = (ShareDicomImg) entity;
		if (!StringTools.isEmpty(dicomImg.ae_title))
			this.ae_title = dicomImg.ae_title;
		if (!StringTools.isEmpty(dicomImg.check_pro))
			this.check_pro = dicomImg.check_pro;
		if (dicomImg.check_time != null)
			this.check_time = dicomImg.check_time;
		if (!StringTools.isEmpty(dicomImg.checklist_num))
			this.checklist_num = dicomImg.checklist_num;
		if (dicomImg.create_time != null)
			this.create_time = dicomImg.create_time;
		if (dicomImg.device_type_id>0)
			this.device_type_id = dicomImg.device_type_id;
		if (!StringTools.isEmpty(dicomImg.eps_num))
			this.eps_num = dicomImg.eps_num;
		if (!StringTools.isEmpty(dicomImg.mark_char))
			this.mark_char = dicomImg.mark_char;
	}

	@Override
	protected void mergeFrom(AbstractShareEntity re) {
		ShareDicomImg dicomImg = (ShareDicomImg) re;
		if (StringTools.isEmpty(this.ae_title))
			this.ae_title = dicomImg.ae_title;
		if (StringTools.isEmpty(this.check_pro))
			this.check_pro = dicomImg.check_pro;
		if (this.check_time == null)
			this.check_time = dicomImg.check_time;
		if (StringTools.isEmpty(this.checklist_num))
			this.checklist_num = dicomImg.checklist_num;
		if (this.create_time == null)
			this.create_time = dicomImg.create_time;
		if (device_type_id<=0)
			this.device_type_id = dicomImg.device_type_id;
		if (StringTools.isEmpty(this.eps_num))
			this.eps_num = dicomImg.eps_num;
		if (StringTools.isEmpty(this.mark_char))
			this.mark_char = dicomImg.mark_char;
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws BaseException {
		this.ae_title = jsonObject.optString("ae_title", "");
		this.check_pro = jsonObject.optString("check_pro", "");
		this.check_time = DateTools.strToDate(jsonObject.optString("check_time"),new Date());
		this.checklist_num = jsonObject.optString("checklist_num", "");
		this.create_time = DateTools.strToDate(jsonObject.optString("create_time"),new Date());
		String device_type_name = jsonObject.optString("device_name", "");
			TDicValue dicValue = OrgConfigService.instance.takeDeviceTypeByOrgIdAndDeviceTypeName(super.getOrg_id(), device_type_name);
			if (dicValue==null)
				throw new NotFoundDeviceTypeMapperException(super.getOrg_id(), device_type_name);
		this.device_type_id = dicValue.getId();
		this.eps_num = jsonObject.optString("eps_num", "");
		if (StringTools.isEmpty(this.eps_num))
			this.eps_num = jsonObject.optString("patient_id", "");
		this.mark_char = jsonObject.optString("mark_char", "");
	}

	public long getDevice_type_id() {
		return device_type_id;
	}

	public void setDevice_type_id(long device_type_id) {
		this.device_type_id = device_type_id;
	}
}
