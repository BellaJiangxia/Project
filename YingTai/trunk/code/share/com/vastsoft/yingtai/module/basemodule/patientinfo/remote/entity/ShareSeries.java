package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.AbstractShareEntity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareExternalSystemType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareSystemIdentity;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.SharePatientDataType;
import com.vastsoft.yingtai.module.sys.service.SysService;

public class ShareSeries extends AbstractShareEntity {
	private long id;
	private long dicom_img_id;
	private String mark_char;
	private long part_type_id;
	private String thumbnail_uid;
	private byte[] thumbnail_data;
	private int expose_times;
	private Date create_time;

	private String part_type_name;
	
	public ShareSeries(long dicom_img_id, String mark_char) {
		super();
		this.dicom_img_id = dicom_img_id;
		this.mark_char = mark_char;
	}

	public void buildPart_type_id(long device_type_id) throws DicomImgException {
		if (device_type_id <= 0)
			throw new DicomImgException("设备类型ID不合法！");
		try {
			this.part_type_id = SysService.instance.queryAndAddPartType(part_type_name.trim(), device_type_id).getId();
		} catch (BaseException e) {
			e.printStackTrace();
			throw new DicomImgException(e);
		}
	}

	/** 翻译部位 */
	private static Map<String, String> stos = new HashMap<String, String>();
	static {
		stos.put("abdomen", "腹部");
		stos.put("abdmultiphase", "腹部");
		stos.put("abdomencontrast", "腹部");
		stos.put("angio", "静脉");
		stos.put("ankle", "踝关节");
		stos.put("brain", "脑部");
		stos.put("cervical", "颈椎");
		stos.put("chest", "胸部");
		stos.put("cspine", "颈椎");
		stos.put("c-spine", "颈椎");
		stos.put("cspine", "颈椎");
		stos.put("elbow", "手肘");
		stos.put("extremity", "四肢");
		stos.put("femur", "大腿骨");
		stos.put("forearm", "前臂");
		stos.put("foot", "脚部");
		stos.put("hand", "手部");
		stos.put("head", "头部");
		stos.put("hip", "臀部");
		stos.put("humerus", "肱骨");
		stos.put("knee", "膝盖");
		stos.put("leg", "腿部");
		stos.put("lspine", "腰椎");
		stos.put("lumbar", "腰椎");
		stos.put("l-spine", "腰椎");
		stos.put("neck", "颈部");
		stos.put("nasal", "鼻");
		stos.put("pelvis", "骨盆");
		stos.put("phantom", "幻肢 ");
		stos.put("rot", "腐块");
		stos.put("spine", "脊椎");
		stos.put("shoulder", "肩部");
		stos.put("sinus", "鼻窦");
		stos.put("thorax", "胸部");
		stos.put("wrist", "腕部");
		stos.put("skull", "头部");
		stos.put("skull", "头部");
		stos.put("patella", "膝盖骨");
		stos.put("calcaneus", "足部");
		stos.put("coccyx", "尾骨");
		stos.put("fibula", "腓骨");
		stos.put("jaw", "下颌部");
	}

	public static String interpret(String protocol_name) {
		if (StringTools.isEmpty(protocol_name))
			return "未知";
		protocol_name = protocol_name.toLowerCase().trim();
		for (Iterator<String> iterator = stos.keySet().iterator(); iterator.hasNext();) {
			String sttr = (String) iterator.next();
			if (protocol_name.contains(sttr)) {
				return stos.get(sttr);
			}
		}
		return protocol_name;
	}

	public ShareSeries() {
		super();
	}

	public ShareSeries(ShareExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	public ShareSeries(ShareSystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	@Override
	protected JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();
		r.put("expose_times", this.expose_times);
		r.put("mark_char", this.mark_char);
		// r.put("part_name", this.part_name);
		r.put("thumbnail_data", CommonTools.bytesToHexString(this.thumbnail_data));
		r.put("thumbnail_uid", this.thumbnail_uid);
		return r;
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		this.expose_times = jsonObject.optInt("expose_times", 0);
		this.mark_char = jsonObject.optString("mark_char", "");
		this.part_type_name = jsonObject.optString("part_name", "");
		if (StringTools.isEmpty(this.part_type_name))
			throw new JSONException("部位类型必须指定！");
		this.part_type_name = interpret(this.part_type_name);
		this.thumbnail_data = CommonTools.hexStringToBytes(jsonObject.optString("thumbnail_data", ""));
		this.thumbnail_uid = jsonObject.optString("thumbnail_uid", "");
	}

	@Override
	protected void replaceFrom(AbstractShareEntity entity) {
		ShareSeries series = (ShareSeries) entity;
		if (series.expose_times > 0)
			this.expose_times = series.expose_times;
		if (!StringTools.isEmpty(series.mark_char))
			this.mark_char = series.mark_char.trim();
		if (series.part_type_id > 0)
			this.part_type_id = series.part_type_id;
		if (!ArrayTools.isEmpty(series.thumbnail_data))
			this.thumbnail_data = series.thumbnail_data;
		if (!StringTools.isEmpty(series.thumbnail_uid))
			this.thumbnail_uid = series.thumbnail_uid;
	}

	@Override
	public SharePatientDataType getPdType() {
		return SharePatientDataType.DICOM_IMG_SERIES;
	}

	@Override
	protected void mergeFrom(AbstractShareEntity re) {
		ShareSeries series = (ShareSeries) re;
		if (this.expose_times <= 0)
			this.expose_times = series.expose_times;
		if (StringTools.isEmpty(this.mark_char))
			this.mark_char = series.mark_char;
		if (this.part_type_id <= 0)
			this.part_type_id = series.part_type_id;
		if (ArrayTools.isEmpty(this.thumbnail_data))
			this.thumbnail_data = series.thumbnail_data;
		if (StringTools.isEmpty(this.thumbnail_uid))
			this.thumbnail_uid = series.thumbnail_uid;
	}

	public String getMark_char() {
		return mark_char;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public long getPart_type_id() {
		return part_type_id;
	}

	public void setPart_type_id(long part_type_id) {
		this.part_type_id = part_type_id;
	}

	public int getExpose_times() {
		return expose_times;
	}

	public void setExpose_times(int expose_times) {
		this.expose_times = expose_times;
	}

	public long getDicom_img_id() {
		return dicom_img_id;
	}

	public void setDicom_img_id(long dicom_img_id) {
		this.dicom_img_id = dicom_img_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getThumbnail_uid() {
		return thumbnail_uid;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = thumbnail_uid;
	}

	@JSON(serialize = false)
	public byte[] getThumbnail_data() {
		return thumbnail_data;
	}

	public void setThumbnail_data(byte[] thumbnail_data) {
		this.thumbnail_data = thumbnail_data;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
