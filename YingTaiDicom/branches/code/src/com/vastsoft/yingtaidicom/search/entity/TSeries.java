package com.vastsoft.yingtaidicom.search.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.search.assist.AbstractRemoteEntity;
import com.vastsoft.yingtaidicom.search.constants.PatientDataType;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.ExternalSystemType;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;

public class TSeries extends AbstractRemoteEntity {
	private String mark_char;
	private String part_name;
	private String thumbnail_uid;
	private byte[] thumbnail_data;
	private int expose_times;

//	/** 翻译部位 */
//	private static Map<String, String> stos = new HashMap<String, String>();
//	static {
//		stos.put("abdomen", "腹部");
//		stos.put("abdmultiphase", "腹部");
//		stos.put("abdomencontrast", "腹部");
//		stos.put("angio", "静脉");
//		stos.put("ankle", "踝关节");
//		stos.put("brain", "脑部");
//		stos.put("cervical", "颈椎");
//		stos.put("chest", "胸部");
//		stos.put("cspine", "颈椎");
//		stos.put("c-spine", "颈椎");
//		stos.put("cspine", "颈椎");
//		stos.put("elbow", "手肘");
//		stos.put("extremity", "四肢");
//		stos.put("femur", "大腿骨");
//		stos.put("forearm", "前臂");
//		stos.put("foot", "脚");
//		stos.put("hand", "手");
//		stos.put("head", "头部");
//		stos.put("hip", "臀部");
//		stos.put("humerus", "肱骨");
//		stos.put("knee", "膝盖");
//		stos.put("leg", "腿");
//		stos.put("lspine", "腰椎");
//		stos.put("lumbar", "腰椎");
//		stos.put("l-spine", "腰椎");
//		stos.put("neck", "颈部");
//		stos.put("nasal", "鼻");
//		stos.put("pelvis", "骨盆");
//		stos.put("phantom", "幻肢 ");
//		stos.put("rot", "腐块");
//		stos.put("spine", "脊椎");
//		stos.put("shoulder", "肩膀");
//		stos.put("sinus", "鼻窦");
//		stos.put("thorax", "胸部");
//		stos.put("wrist", "腕部");
//		stos.put("skull", "头部");
//		stos.put("skull", "头部");
//		stos.put("patella", "膝盖骨");
//		stos.put("calcaneus", "足");
//		stos.put("coccyx", "尾骨");
//		stos.put("fibula", "腓骨");
//		stos.put("jaw", "下颌");
//	}
//
//	private static String interpret(String protocol_name) {
//		if (StringTools.isEmpty(protocol_name))
//			return "未知";
//		protocol_name = protocol_name.toLowerCase().trim();
//		for (Iterator<String> iterator = stos.keySet().iterator(); iterator.hasNext();) {
//			String sttr = (String) iterator.next();
//			if (protocol_name.contains(sttr)) {
//				return stos.get(sttr);
//			}
//		}
//		return protocol_name;
//	}

	public TSeries(SystemIdentity systemIdentity) {
		super(systemIdentity);
	}

	public TSeries(ExternalSystemType systemType, String brand, String version) {
		super(systemType, brand, version);
	}

	@Override
	protected JSONObject serialize() throws JSONException {
		JSONObject r = new JSONObject();
		r.put("expose_times", this.expose_times);
		r.put("mark_char", this.mark_char);
		r.put("part_name", this.part_name);
		r.put("thumbnail_data", CommonTools.bytesToHexString(this.thumbnail_data));
		r.put("thumbnail_uid", this.thumbnail_uid);
		return r;
	}

	@Override
	protected void deserialize(JSONObject jsonObject) throws JSONException {
		this.expose_times = jsonObject.optInt("expose_times", 0);
		this.mark_char = jsonObject.optString("mark_char", "");
		this.part_name = jsonObject.optString("part_name", "");
		this.thumbnail_data = CommonTools.hexStringToBytes(jsonObject.optString("thumbnail_data", ""));
		this.thumbnail_uid = jsonObject.optString("thumbnail_uid", "");
	}

	@Override
	protected void replaceFrom(AbstractRemoteEntity entity) {
		TSeries series = (TSeries) entity;
		if (series.expose_times > 0)
			this.expose_times = series.expose_times;
		if (!StringTools.isEmpty(series.mark_char))
			this.mark_char = series.mark_char.trim();
		if (!StringTools.isEmpty(series.part_name))
			this.part_name = series.part_name.trim();
		if (!ArrayTools.isEmpty(series.thumbnail_data))
			this.thumbnail_data = series.thumbnail_data;
		if (!StringTools.isEmpty(series.thumbnail_uid))
			this.thumbnail_uid = series.thumbnail_uid;
	}

	@Override
	public PatientDataType getPdType() {
		return PatientDataType.DICOM_IMG_SERIES;
	}

	@Override
	protected void mergeFrom(AbstractRemoteEntity re) {
		TSeries series = (TSeries) re;
		if (this.expose_times <= 0)
			this.expose_times = series.expose_times;
		if (StringTools.isEmpty(this.mark_char))
			this.mark_char = series.mark_char;
		if (StringTools.isEmpty(this.part_name))
			this.part_name = series.part_name;
		if (ArrayTools.isEmpty(this.thumbnail_data))
			this.thumbnail_data = series.thumbnail_data;
		if (StringTools.isEmpty(this.thumbnail_uid))
			this.thumbnail_uid = series.thumbnail_uid;
	}

	public String getPart_name() {
		return part_name;
	}

	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}

	public String getMark_char() {
		return mark_char;
	}

	public void setMark_char(String mark_char) {
		this.mark_char = mark_char;
	}

	public int getExpose_times() {
		return expose_times;
	}

	public void setExpose_times(int expose_times) {
		this.expose_times = expose_times;
	}

	public String getThumbnail_uid() {
		return thumbnail_uid;
	}

	public void setThumbnail_uid(String thumbnail_uid) {
		this.thumbnail_uid = thumbnail_uid;
	}

	public byte[] getThumbnail_data() {
		return thumbnail_data;
	}

	public void setThumbnail_data(byte[] thumbnail_data) {
		this.thumbnail_data = thumbnail_data;
	}

}
