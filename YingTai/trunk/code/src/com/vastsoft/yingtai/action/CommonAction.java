package com.vastsoft.yingtai.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.io.IOTools;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.vastsoft.util.GUID;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.FileTools;
import com.vastsoft.util.common.TimeLimit;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.casehis.constants.CaseHistoryType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteServerVersion;
import com.vastsoft.yingtai.module.basemodule.patientinfo.report.constants.ReportModifyRequestStatus;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.org.constants.OrgCertificationLevel;
import com.vastsoft.yingtai.module.org.constants.OrgProperty;
import com.vastsoft.yingtai.module.org.constants.OrgPublicStatus;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlEnforceable;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormAnswerStatus;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlFormState;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlMeasureQuestionType;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;
import com.vastsoft.yingtai.module.reservation.constants.CheckFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.CheckServiceStatus;
import com.vastsoft.yingtai.module.reservation.constants.ItemFeeCalcType;
import com.vastsoft.yingtai.module.reservation.constants.ReservationStatus;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.entity.TFile;
import com.vastsoft.yingtai.module.sys.service.FileService;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.constants.Gender;
import com.vastsoft.yingtai.module.user.constants.UserStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.constants.UserTypeDetail;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;

public class CommonAction extends BaseYingTaiAction {
	private List<File> file;
	private List<String> fileFileName;
	// private List<String> fileContentType;
	private DictionaryType fileType;
	private boolean primary;//是否需要原图
	private long org_id;
	private boolean needWaterMark=true;

	public void setFile(List<File> file) {
		this.file = file;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}

	// public void setFileContentType(List<String> fileContentType)
	// {
	// this.fileContentType = fileContentType;
	// }

	public void setFileType(int fileType) {
		this.fileType = DictionaryType.parseFrom(fileType);
	}

	/**
	 * 上传多个文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadFiles() throws Exception {
		if (this.isOnline() == null)
			return SUCCESS;

		List<String> listNewFileName = new ArrayList<String>(4);

		if (fileType == null)
			throw new NullParameterException("fileType");

		String strFilePath = SysService.instance.takeFilePath(this.fileType);

		if (strFilePath == null || strFilePath.isEmpty())
			throw new NullParameterException("strFilePath");

		List<TFile> tfms = new ArrayList<TFile>(4);

		for (int i = 0; i < this.file.size(); i++) {
			String strOldFileName = this.fileFileName.get(i);
			String strGuid = new GUID().toString();
			String strSuffix = strOldFileName.substring(strOldFileName.lastIndexOf(".") + 1);
			String strNewFileName = strGuid + "." + strSuffix;

			listNewFileName.add(strNewFileName);

			InputStream is = new FileInputStream(this.file.get(i));

			File fileDest = new File(strFilePath, strNewFileName);

			File fp = fileDest.getParentFile();
			if (fp == null || !fp.exists()) {
				fp.mkdirs();
			}

			OutputStream os = new FileOutputStream(fileDest);

			byte[] buffer = new byte[4096];

			@SuppressWarnings("unused")
			int length = 0;

			while (-1 != (length = is.read(buffer, 0, buffer.length))) {
				os.write(buffer);
			}

			os.close();
			is.close();

			TFile tfm = new TFile();
			tfm.setOri_name(strOldFileName);
			tfm.setFile_type(this.fileType.getCode());
			tfm.setPath(fileDest.getPath());
			try {
				String thumbnail = strGuid + "_t." + strSuffix;
				FileInputStream intf = new FileInputStream(fileDest.getPath());
				File fileDests = new File(strFilePath, thumbnail);
				tfm.setThumbnail_path(this.saveThumbnail(intf, fileDests));
			} catch (Exception e) {
				tfm.setThumbnail_path("");
			}
			tfms.add(tfm);
		}

		FileService.instance.saveFiles(super.takePassport(), tfms);

		if (tfms != null && !tfms.isEmpty()) {
			List<Long> ids = new ArrayList<Long>(4);
			for (TFile tfm : tfms) {
				ids.add(tfm.getId());
			}
			super.addElementToData("file_id", ids);
			super.addElementToData("tfms", tfms);
		}

		return SUCCESS;
	}

	private String saveThumbnail(InputStream intf, File fileDests)
			throws FileNotFoundException, IOException {
		int newWidth = 0, newHeight = 0;
		if (this.fileType.equals(DictionaryType.USER_LOGO_PATH)) {
			newWidth = 100;
			newHeight = 100;
		} else if (this.fileType.equals(DictionaryType.USER_SCAN_PATH)) {
			newWidth = 320;
			newHeight = 240;
		} else if (this.fileType.equals(DictionaryType.ORG_LOGO_PATH)){
		 newWidth=100;
		 newHeight=100;
		} else if (this.fileType.equals(DictionaryType.ORG_SCAN_PATH)) {
			newWidth = 320;
			newHeight = 240;
		} else {
			return "";
		}
		Thumbnails.of(intf).size(newWidth, newHeight).toFile(fileDests);
		return fileDests.getPath();
	}
	
//	public static void main(String[] args) throws IOException {
//		BufferedImage bi = new BufferedImage(33, 17, BufferedImage.TYPE_INT_ARGB);
//		Graphics g = bi.getGraphics();
//		g.setColor(Color.GREEN);
//		g.setFont(new Font(null, Font.BOLD, 16));
//		g.drawRect(0, 0, 32, 16);
//		g.drawString("YTA", 1, 15);
////		ImageIO.write(bi, "PNG", new File("C:\\Users\\jben\\Pictures\\工作\\影泰\\bg_wm.png"));
//		Watermark wm = new Watermark(Positions.BOTTOM_RIGHT, bi, 1.0f);
//		BufferedImage srcImg = ImageIO.read(new File("C:\\Users\\jben\\Pictures\\工作\\影泰\\bg.jpg"));
//		int srcW = srcImg.getWidth();
//		int srcH = srcImg.getHeight();
//		Thumbnails.of(srcImg).size(srcW, srcH).watermark(wm).toFile("C:\\Users\\jben\\Pictures\\工作\\影泰\\bg_wm.jpg");
//	}

	private Long lImageId = null;

	@JSON(serialize = false)
	public InputStream getImageStream() {
		try {
			if (this.lImageId != null && this.lImageId > 0) {
				TFile tfm = FileService.instance.queryFileById(takePassport(), lImageId);
				if (tfm != null) {
					File file = null;
					if (primary) {
						file = new File(tfm.getPath());
						if (file.exists()){
							if (tfm.getFile_type()==DictionaryType.ORG_LOGO_PATH.getCode()) {
								return this.orgLogoFileWaterMarkStream(tfm,file);
							}else
								return new FileInputStream(file);
						}else
							return getDefaultFile(tfm.getFileType());
					}else if (tfm.getThumbnail_path() != null && !tfm.getThumbnail_path().trim().isEmpty()) {
						file = new File(tfm.getThumbnail_path());
						if (file.exists()){
							if (tfm.getFile_type()==DictionaryType.ORG_LOGO_PATH.getCode()) {
								return this.orgLogoFileWaterMarkStream(tfm,file);
							}else
								return new FileInputStream(file);
						}else
							return getDefaultFile(tfm.getFileType());
					}
					file = new File(tfm.getPath());
					if (file.exists()){
						return new FileInputStream(file);
					}else
						return getDefaultFile(tfm.getFileType());
				}
			}
			return getDefaultFile(this.fileType);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return getDefaultFile(this.fileType);
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	private InputStream orgLogoFileWaterMarkStream(TFile tfm, File file) throws OrgOperateException, IOException {
		if (!this.needWaterMark)
			return new FileInputStream(file);
		TOrganization org = null;
		if (org_id>0)
			org = OrgService.instance.queryOrgById(org_id);
		if (org==null) {
			List<TOrganization> listOrg = OrgService.instance.queryOrgByLogoFileId(tfm.getId());
			if (CollectionTools.isEmpty(listOrg))
				return new FileInputStream(file);
			if (listOrg.size()>1)
				return new FileInputStream(file);
			org = listOrg.get(0);
		}
		if (org==null)
			return new FileInputStream(file);
		if (org.getCertification_level() != OrgCertificationLevel.CERTIFICATIONED.getCode())
			return new FileInputStream(file);
		return this.waterMarkToOrgLogoFile(file);
	}

	private InputStream waterMarkToOrgLogoFile(File srcFile) throws IOException {
		Watermark wm = new Watermark(Positions.BOTTOM_RIGHT, ImageIO.read(this.getAuthenticationImg()), 1.0f);
		BufferedImage srcImg = ImageIO.read(srcFile);
		int srcW = srcImg.getWidth();
		int srcH = srcImg.getHeight();
		String format = FileTools.takeFileNameSuffix(srcFile.getName()).toUpperCase();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Thumbnails.of(srcImg).size(srcW, srcH).watermark(wm).outputFormat(format).toOutputStream(out);
		return new ByteArrayInputStream(out.toByteArray());
	}

	private File getAuthenticationImg(){
		return new File(ServletActionContext.getServletContext().getRealPath("/"),
				"/image/authentication.png");
	}

	private static Map<DictionaryType,String> mapDefaultFileCache=new HashMap<>();
	private InputStream getDefaultFile(DictionaryType fileType) throws IOException {
		File defaultFile = null;
		String uuid = mapDefaultFileCache.get(fileType);
		if (uuid != null){
			byte[] data = FileContentCacheTools.getFileByUuid(uuid);
			if (!ArrayTools.isEmpty(data))
				return IOTools.bytesToStream(data);
		}
		if (DictionaryType.USER_LOGO_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/user_default.png");
		} else if (DictionaryType.ORG_LOGO_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/hospital-default.png");
		} else if (DictionaryType.USER_SCAN_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/diploma_default.png");
		} else if (DictionaryType.ORG_SCAN_PATH.equals(fileType)) {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/diploma_default.png");
		} else {
			defaultFile = new File(ServletActionContext.getServletContext().getRealPath("/"),
					"/image/no_file_record.png");
		}
		if (defaultFile != null && defaultFile.length() >0){
			uuid = FileContentCacheTools.newCacheFile(defaultFile);
			if (uuid != null){
				mapDefaultFileCache.put(fileType,uuid);
			}
		}
		return new FileInputStream(defaultFile);
	}

	public void setImageId(Long strImageFile) {
		this.lImageId = strImageFile;
	}

	/**
	 * 获取图片文件 必须参数 imageId 图片唯一标识; imageSizeType 图片大小类型，取值范围："s", "m", "l"
	 * 
	 * @return
	 */
	public String image() {
		return "image";
	}

	public String queryDiagnosisStatus() {
		try {
			addElementToData("listDiagnosisStatus", DiagnosisStatus2.getAll());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryTimeLimit() {
		try {
			addElementToData("listTimeLimit", TimeLimit.getAll());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryUserType() {
		this.addElementToData("user_type", UserType.getRegType());
		return SUCCESS;
	}

	public String queryUserTypeDetail() {
		this.addElementToData("list", UserTypeDetail.getAll());
		return SUCCESS;
	}

	public String queryUserGender() {
		this.addElementToData("user_gender", Gender.getAll());
		return SUCCESS;
	}

	public String queryUserPermission() {
		this.addElementToData("user_perms", UserPermission.getAllUserPower());
		return SUCCESS;
	}

	public String queryAdminPermission() {
		this.addElementToData("admin_perms", UserPermission.getAllAdminPower());
		return SUCCESS;
	}

	public String queryReportType() {
		this.addElementToData("report_type", PublishReportType.getAllPublishReportType());
		return SUCCESS;
	}

	public String queryOrgType() {
		this.addElementToData("org_types", new ArrayList<String>());
		return SUCCESS;
	}

	public String queryUserStatus() {
		this.addElementToData("user_status", UserStatus.getAll());
		return SUCCESS;
	}

	public String queryPublicStatus() {
		this.addElementToData("status", OrgPublicStatus.getAllStatus());
		return SUCCESS;
	}

	public String queryModifyReportRequestStatus() {
		try {
			addElementToData("listMrr", ReportModifyRequestStatus.getAll());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryRemoteServerVersion() {
		try {
			addElementToData("listRSV", ShareRemoteServerVersion.getAll());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryRemoteParamType() {
		try {
			addElementToData("listRemoteParamsType", ShareRemoteParamsType.getAll());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryDeviceType() {
		try {
			List<TDicValue> listDeviceType = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
			addElementToData("listDeviceType", listDeviceType);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlTarget() {
		try {
			List<QualityControlTarget> listQualityControlTarget = QualityControlTarget.getAll();
			addElementToData("listQualityControlTarget", listQualityControlTarget);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlEnforceable() {
		try {
			List<QualityControlEnforceable> listQualityControlEnforceable = QualityControlEnforceable.getAll();
			addElementToData("listQualityControlEnforceable", listQualityControlEnforceable);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlFormState() {
		try {
			List<QualityControlFormState> listQualityControlFormState = QualityControlFormState.getAll();
			addElementToData("listQualityControlFormState", listQualityControlFormState);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlMeasureQuestionType() {
		try {
			List<QualityControlMeasureQuestionType> listQualityControlMeasureQuestionType = QualityControlMeasureQuestionType
					.getAll();
			addElementToData("listQualityControlMeasureQuestionType", listQualityControlMeasureQuestionType);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryQualityControlFormAnswerStatus() {
		try {
			List<QualityControlFormAnswerStatus> listQualityControlFormAnswerStatus = QualityControlFormAnswerStatus
					.getAll();
			addElementToData("listQualityControlFormAnswerStatus", listQualityControlFormAnswerStatus);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryOrgProductChargeType() {
		try {
			List<OrgProductChargeType> listOrgProductChargeType = OrgProductChargeType.getAll();
			super.addElementToData("listOrgProductChargeType", listOrgProductChargeType);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}
	
	public String queryCaseHistoryType() {
		try {
			List<CaseHistoryType> listCaseHistoryType = CaseHistoryType.getAll();
			super.addElementToData("listCaseHistoryType", listCaseHistoryType);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryFeeCalcType(){
		this.addElementToData("list", CheckFeeCalcType.getAll());
		return SUCCESS;
	}

	public String queryItemFeeCalcType(){
		this.addElementToData("list", ItemFeeCalcType.getAll());
		return SUCCESS;
	}

	public String queryReservationStatus(){
		this.addElementToData("list", ReservationStatus.getAll());
		return SUCCESS;
	}

	public String queryServiceStatus(){
		this.addElementToData("list", CheckServiceStatus.getAll());
		return SUCCESS;
	}

	public String queryOrgProperty(){
		this.addElementToData("list", OrgProperty.getAllOrgProperty());
		return SUCCESS;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}

	public void setNeedWaterMark(boolean needWaterMark) {
		this.needWaterMark = needWaterMark;
	}
}
