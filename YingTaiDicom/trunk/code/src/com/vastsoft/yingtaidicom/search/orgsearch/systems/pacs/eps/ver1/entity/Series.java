package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity;

public class Series {
	private String series_uid;
	private int series_uid_id;
	private int study_uid_id;
	private int series_number;
	private String modality;
	private String body_part;
	private int series_date;
	private double series_time;
	private String series_desc;
	private String req_proc_id;
	private String sched_proc_id;
	private int pps_date;
	private double pps_time;
	private String protocol_name;
	private String operator_name;
	private String model_name;
	
	private String source_ae;
	private String thrumbnail_uid;// 缩略图UID
	private byte[] thumbnail_data;// 缩略图数据
	private int expose_times;// 曝光次数
	
//	private static Map<String, String> stos=new HashMap<String,String>();
//	static{
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
//	}
//	
//	private static String interpret(String protocol_name){
//		protocol_name=protocol_name.toLowerCase();
//		for (Iterator<String> iterator = stos.keySet().iterator(); iterator.hasNext();) {
//			String sttr = (String) iterator.next();
//			if (protocol_name.contains(sttr)) {
//				return stos.get(sttr);
//			}
//		}
//		return "";
//	}
	
	public String getThrumbnail_uid() {
		return thrumbnail_uid;
	}

	public void setThrumbnail_uid(String thrumbnail_uid) {
		this.thrumbnail_uid = thrumbnail_uid;
	}

	public int getExpose_times() {
		return expose_times;
	}

	public void setExpose_times(int expose_times) {
		this.expose_times = expose_times;
	}

	public String getSeries_uid() {
		return series_uid;
	}

	public void setSeries_uid(String series_uid) {
		this.series_uid = series_uid;
	}

	public int getSeries_uid_id() {
		return series_uid_id;
	}

	public void setSeries_uid_id(int series_uid_id) {
		this.series_uid_id = series_uid_id;
	}

	public int getStudy_uid_id() {
		return study_uid_id;
	}

	public void setStudy_uid_id(int study_uid_id) {
		this.study_uid_id = study_uid_id;
	}

	public int getSeries_number() {
		return series_number;
	}

	public void setSeries_number(int series_number) {
		this.series_number = series_number;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

//	public String getBody_part() {
//		return body_part;
//	}
//	
//	public String getBody_part_type() {
//		return interpret(protocol_name);
//	}
//
//	public void setBody_part(String body_part) {
//		this.body_part = body_part;
//	}

	public int getSeries_date() {
		return series_date;
	}

	public void setSeries_date(int series_date) {
		this.series_date = series_date;
	}

	public double getSeries_time() {
		return series_time;
	}

	public void setSeries_time(double series_time) {
		this.series_time = series_time;
	}

	public String getSeries_desc() {
		return series_desc;
	}

	public void setSeries_desc(String series_desc) {
		this.series_desc = series_desc;
	}

	public String getReq_proc_id() {
		return req_proc_id;
	}

	public void setReq_proc_id(String req_proc_id) {
		this.req_proc_id = req_proc_id;
	}

	public String getSched_proc_id() {
		return sched_proc_id;
	}

	public void setSched_proc_id(String sched_proc_id) {
		this.sched_proc_id = sched_proc_id;
	}

	public int getPps_date() {
		return pps_date;
	}

	public void setPps_date(int pps_date) {
		this.pps_date = pps_date;
	}

	public double getPps_time() {
		return pps_time;
	}

	public void setPps_time(double pps_time) {
		this.pps_time = pps_time;
	}

	public String getProtocol_name() {
		return protocol_name;
	}

	public void setProtocol_name(String protocol_name) {
		this.protocol_name = protocol_name;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getSource_ae() {
		return source_ae;
	}

	public void setSource_ae(String source_ae) {
		this.source_ae = source_ae;
	}

	public byte[] getThumbnail_data() {
		return thumbnail_data;
	}

	public void setThumbnail_data(byte[] thumbnail_data) {
		this.thumbnail_data = thumbnail_data;
	}

	public String getBody_part() {
		return body_part;
	}

	public void setBody_part(String body_part) {
		this.body_part = body_part;
	}

}
