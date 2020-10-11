package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;

import java.util.ArrayList;
import java.util.List;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;

public class TSeries extends AbstractDicomEntity{
	private String study_uuid;
	private String series_number;// 序列号：识别不同检查的号码.
	private String series_instance_uid;//序列实例号：唯一标记不同序列的号码.
	private String modality;// 检查模态(MRI/CT/CR/DR)
	private String series_description;// 检查描述和说明
	private String series_date;//<yyyymmdd>
	private String series_time;// 检查时间
	/** 图像位置：图像的左上角在空间坐标系中的x,y,z坐标,单位是毫米. 如果在检查中,则指该序列中第一张影像左上角的坐标. */
	private String image_position_patient;//
	/** 图像位置：图像的左上角在空间坐标系中的x,y,z坐标,单位是毫米.如果在检查中,则指该序列中第一张影像左上角的坐标. */
	private String image_orientation_patient;// 图像方位：第一行和第一列对病人的方向余弦。
	/** 层厚.Nominal slice thickness, in mm. */
	private String slice_thickness;// 层厚.单位（mm）
	/** 层与层之间的间距,单位为mm */
	private String spacing_between_slices;//
	private String slice_location;// 实际的相对位置，单位为mm.
	private String mr_acquisition;
	/**
	 * 身体部位. Text description of the part of the body examined. Enumerated
	 * Values:EYE Note Some IODs support the Anatomic Region Sequence
	 * (0008,2218), which can provide a more comprehensive mechanism for
	 * specifying the body part being examined. If matched value(s) in the
	 * EXAMPLE-QUERY-RETRIEVE-SERVER exam type database then it will be saved to
	 * the database as an exam type.
	 */
	private String body_part_examined;// 身体部位
	private String protocol_name;// 协议名称
	
	
	private List<TImage> listImage = new ArrayList<TImage>();
	public String getSeries_instance_uid() {
		return series_instance_uid;
	}

	public String getBody_part_examined() {
		return body_part_examined;
	}

	public String getSeries_date() {
		return series_date;
	}

	public String getSeries_time() {
		return series_time;
	}

	public String getSeries_description() {
		return series_description;
	}

	public String getImage_position_patient() {
		return image_position_patient;
	}

	public String getImage_orientation_patient() {
		return image_orientation_patient;
	}

	public String getSpacing_between_slices() {
		return spacing_between_slices;
	}

	public String getMr_acquisition() {
		return mr_acquisition;
	}

	public void setSeries_instance_uid(String series_instance_uid) {
		this.series_instance_uid = series_instance_uid;
	}

	public void setBody_part_examined(String body_part_examined) {
		this.body_part_examined = body_part_examined;
	}

	public void setSeries_date(String series_date) {
		this.series_date = series_date;
	}

	public void setSeries_time(String series_time) {
		this.series_time = series_time;
	}

	public void setSeries_description(String series_description) {
		this.series_description = series_description;
	}

	public void setImage_position_patient(String image_position_patient) {
		this.image_position_patient = image_position_patient;
	}

	public void setImage_orientation_patient(String image_orientation_patient) {
		this.image_orientation_patient = image_orientation_patient;
	}

	public void setSpacing_between_slices(String spacing_between_slices) {
		this.spacing_between_slices = spacing_between_slices;
	}

	public void setMr_acquisition(String mr_acquisition) {
		this.mr_acquisition = mr_acquisition;
	}

	public String getSeries_number() {
		return series_number;
	}

	public String getModality() {
		return modality;
	}

	public String getSlice_thickness() {
		return slice_thickness;
	}

	public String getSlice_location() {
		return slice_location;
	}

	public String getProtocol_name() {
		return protocol_name;
	}

	public void setSeries_number(String series_number) {
		this.series_number = series_number;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public void setSlice_thickness(String slice_thickness) {
		this.slice_thickness = slice_thickness;
	}

	public void setSlice_location(String slice_location) {
		this.slice_location = slice_location;
	}

	public void setProtocol_name(String protocol_name) {
		this.protocol_name = protocol_name;
	}

	public String getStudy_uuid() {
		return study_uuid;
	}

	public void setStudy_uuid(String study_uuid) {
		this.study_uuid = study_uuid;
	}

	@Override
	public void writeFatherUuid(String father_uuid) {
		this.study_uuid = father_uuid;
	}

	@Override
	public String readFatherUuid() {
		return this.study_uuid;
	}

	public void addImage(TImage tImage) {
		if (tImage==null)
			return;
		this.listImage.add(tImage);
	}

	public String getThrumbnail_uid() {
		if (CollectionTools.isEmpty(this.listImage))
			return null;
		for (TImage tImage : this.listImage) {
			if (!StringTools.isEmpty(tImage.getSop_instance_uid()))
				return tImage.getSop_instance_uid();
		}
		return null;
	}

	public byte[] getThumbnail_data() {
		if (CollectionTools.isEmpty(this.listImage))
			return null;
		for (TImage tImage : this.listImage) {
			if (!ArrayTools.isEmpty(tImage.getThumbnail()))
				return tImage.getThumbnail();
		}
		return null;
	}

	public int getExpose_times() {
		if (CollectionTools.isEmpty(this.listImage))
			return 0;
		return this.listImage.size();
	}

}
