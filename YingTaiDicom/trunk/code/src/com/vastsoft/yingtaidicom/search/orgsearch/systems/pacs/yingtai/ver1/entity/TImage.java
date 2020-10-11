package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity;


public class TImage extends AbstractDicomEntity{
	private String series_uuid;
	private String image_type;// 图像识别特征
	private String sop_instance_uid;// SOP实例UID.
	private String sop_class_uid;///** SOP Class UID */
	/** Uniquely identifies the referenced SOP Instance. */
	private String referenced_sop_instance_uid;
	private String alternate_representation_sequence;
	private String content_date;
	private String content_time;// 影像拍摄的时间.
	private String instance_number;// 图像码：辨识图像的号码
	private String samples_per_pixel;// 图像上的采样率.
	/**
	 * 光度计的解释,对于CT图像，用两个枚举值 MONOCHROME1，MONOCHROME2. 用来判断图像是否是彩色的，
	 * MONOCHROME1/2是灰度图， RGB则是真彩色图，还有其他.
	 */
	private String photometric_interpretation;
	/**
	 * 图像的总行数，行分辨率.
	 */
	private int rows;
	/**
	 * 图像的总列数，列分辨率.
	 */
	private int columns;
	/**
	 * 像素间距.像素中心之间的物理间距.
	 */
	private String pixel_spacing;
	/**
	 * 分配的位数:存储每一个像素值时分配的位数，每一个样本应该拥有相同的这个值.
	 */
	private int bits_allocated; //
	/**
	 * 存储的位数：有12到16列举值.存储每一个像素用的位数.每一个样本应该有相同值.
	 */
	private int bits_stored;
	/**
	 * 高位：最高有效位的像素样本数据。每个样本须有相同的高一点。
	 */
	private int high_bit;
	/**
	 * 像素数据的表现类型：这是一个枚举值，分别为十六进制数0000和0001.0000H = 无符号整数，0001H = 2的补码.
	 */
	private int pixel_representation;
	private String window_center;
	private String window_width;
	/**
	 * 截距：如果表明不同模态的LUT颜色对应表不存在时,则使用方程；Units = m*SV +
	 * b,计算真实的像素值到呈现像素值。其中这个值为表达式中的b。
	 */
	private String rescale_intercept;
	private String rescale_slope;// 斜率：
	private String rescale_type;// 输出值的单位.这是一个枚举值,
	/**(0002,0010) VR=UI VM=1 Transfer Syntax UID*/
	private String transfer_syntax_uid;
	/**(0000,0002) VR=UI VM=1 Affected SOP Class UID*/
	private String affected_sop_class_uid;//
	/** Uniquely identifies the referenced SOP Instance. */
	private String referenced_sop_class_uid;
	private String procedure_code_sequence;
	private String dcm_file_path;// 图像文件保存位置
	private String source_application_entity_title;
	private int key_image;

	public String getSop_instance_uid() {
		return sop_instance_uid;
	}

	public String getContent_date() {
		return content_date;
	}

	public String getContent_time() {
		return content_time;
	}

	public String getInstance_number() {
		return instance_number;
	}

	public void setSop_instance_uid(String sop_instance_uid) {
		this.sop_instance_uid = sop_instance_uid;
	}

	public void setContent_date(String content_date) {
		this.content_date = content_date;
	}

	public void setContent_time(String content_time) {
		this.content_time = content_time;
	}

	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}

	public String getImage_type() {
		return image_type;
	}

	public String getPhotometric_interpretation() {
		return photometric_interpretation;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public String getPixel_spacing() {
		return pixel_spacing;
	}

	public int getBits_allocated() {
		return bits_allocated;
	}

	public int getBits_stored() {
		return bits_stored;
	}

	public int getHigh_bit() {
		return high_bit;
	}

	public int getPixel_representation() {
		return pixel_representation;
	}

	public String getRescale_intercept() {
		return rescale_intercept;
	}

	public String getRescale_slope() {
		return rescale_slope;
	}

	public String getRescale_type() {
		return rescale_type;
	}

	public int getKey_image() {
		return key_image;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public void setPhotometric_interpretation(String photometric_interpretation) {
		this.photometric_interpretation = photometric_interpretation;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void setPixel_spacing(String pixel_spacing) {
		this.pixel_spacing = pixel_spacing;
	}

	public void setBits_allocated(int bits_allocated) {
		this.bits_allocated = bits_allocated;
	}

	public void setBits_stored(int bits_stored) {
		this.bits_stored = bits_stored;
	}

	public void setHigh_bit(int high_bit) {
		this.high_bit = high_bit;
	}

	public void setPixel_representation(int pixel_representation) {
		this.pixel_representation = pixel_representation;
	}

	public void setRescale_intercept(String rescale_intercept) {
		this.rescale_intercept = rescale_intercept;
	}

	public void setRescale_slope(String rescale_slope) {
		this.rescale_slope = rescale_slope;
	}

	public void setRescale_type(String rescale_type) {
		this.rescale_type = rescale_type;
	}

	public void setKey_image(int key_image) {
		this.key_image = key_image;
	}

	public String getSeries_uuid() {
		return series_uuid;
	}

	public void setSeries_uuid(String series_uuid) {
		this.series_uuid = series_uuid;
	}

//	@Override
//	public DicomEntityClass getDicomEntityClass() {
//		return DicomEntityClass.IMAGE;
//	}

	@Override
	public void writeFatherUuid(String father_uuid) {
		this.series_uuid = father_uuid;
	}

	@Override
	public String readFatherUuid() {
		return this.series_uuid;
	}

//	@Override
//	public void checkEntityWholeness() throws DcmEntityUnWholenessException {
//	}

	public String getWindow_center() {
		return window_center;
	}

	public void setWindow_center(String window_center) {
		this.window_center = window_center;
	}

	public String getWindow_width() {
		return window_width;
	}

	public void setWindow_width(String window_width) {
		this.window_width = window_width;
	}

	public String getReferenced_sop_class_uid() {
		return referenced_sop_class_uid;
	}

	public void setReferenced_sop_class_uid(String referenced_sop_class_uid) {
		this.referenced_sop_class_uid = referenced_sop_class_uid;
	}

	public String getProcedure_code_sequence() {
		return procedure_code_sequence;
	}

	public void setProcedure_code_sequence(String procedure_code_sequence) {
		this.procedure_code_sequence = procedure_code_sequence;
	}

	public String getTransfer_syntax_uid() {
		return transfer_syntax_uid;
	}

	public void setTransfer_syntax_uid(String transfer_syntax_uid) {
		this.transfer_syntax_uid = transfer_syntax_uid;
	}

	public String getAffected_sop_class_uid() {
		return affected_sop_class_uid;
	}

	public void setAffected_sop_class_uid(String affected_sop_class_uid) {
		this.affected_sop_class_uid = affected_sop_class_uid;
	}

	public String getSamples_per_pixel() {
		return samples_per_pixel;
	}

	public void setSamples_per_pixel(String samples_per_pixel) {
		this.samples_per_pixel = samples_per_pixel;
	}

	public String getAlternate_representation_sequence() {
		return alternate_representation_sequence;
	}

	public void setAlternate_representation_sequence(String alternate_representation_sequence) {
		this.alternate_representation_sequence = alternate_representation_sequence;
	}

	public String getSop_class_uid() {
		return sop_class_uid;
	}

	public void setSop_class_uid(String sop_class_uid) {
		this.sop_class_uid = sop_class_uid;
	}

	public String getDcm_file_path() {
		return dcm_file_path;
	}

	public void setDcm_file_path(String dcm_file_path) {
		this.dcm_file_path = dcm_file_path;
	}

	public String getReferenced_sop_instance_uid() {
		return referenced_sop_instance_uid;
	}

	public void setReferenced_sop_instance_uid(String referenced_sop_instance_uid) {
		this.referenced_sop_instance_uid = referenced_sop_instance_uid;
	}

	public String getSource_application_entity_title() {
		return source_application_entity_title;
	}

	public void setSource_application_entity_title(String source_application_entity_title) {
		this.source_application_entity_title = source_application_entity_title;
	}
}
