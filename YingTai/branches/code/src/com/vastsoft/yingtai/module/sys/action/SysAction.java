package com.vastsoft.yingtai.module.sys.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TSeries;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.constants.TemplateType;
import com.vastsoft.yingtai.module.sys.entity.FReportTemplate;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.entity.TReportTemplate;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;

public class SysAction extends BaseYingTaiAction {
	private Long lParentId;
	private Integer iType;
	private String strValue;
	private Long lDicValueId;
	private Long lDeviceTypeId;
	private Long partTypeId;
	private TReportTemplate template;
	private Long lTemplateId;
	private SplitPageUtil spu;
	private Integer iFlag;
	private String strUnit;
	private String partTypeName;
	
	public String addBodyPartType(){
		try {
			TDicValue dicValue = SysService.instance.queryAndAddPartType(partTypeName, lDeviceTypeId);
			addElementToData("bodyPartType", dicValue);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String queryCheckProListByPartTypeName() {
		try {
			partTypeName = TSeries.interpret(partTypeName);
			TDicValue dicValue = SysService.instance.queryAndAddPartType(partTypeName, lDeviceTypeId);
			List<TDicValue> listDicValue = SysService.instance.queryDicValueListByParent(dicValue.getId(),
					DictionaryType.DICOM_IMG_CHECK_PRO);
			addElementToData("listDicValue", listDicValue);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// /** 通过部位ID查询检查项目列表 */
	// public String queryCheckProListByPartTypeId() {
	// try {
	// List<TDicValue> listDv =
	// SysService.instance.queryDicValueListByParent(partTypeId,
	// DictionaryType.DICOM_IMG_CHECK_PRO);
	// addElementToData("listCheckPro",
	// FDicomImgCheckPro.buildListByDvs(listDv));
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	// @ActionDesc("根据设备获取检查项目")
	public String queryCheckProListByDeviceId() {
		try {
			if (lDeviceTypeId == null) {
				List<TDicValue> listDicValue = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
				addElementToData("listDicValue", listDicValue);
			} else if (partTypeId == null || partTypeId <= 0) {
				List<TDicValue> listDicValue = SysService.instance.queryDicValueListByParent(lDeviceTypeId,
						DictionaryType.BODY_PART_TYPE);
				addElementToData("listDicValue", listDicValue);
			} else {
				List<TDicValue> listDicValue = SysService.instance.queryDicValueListByParent(partTypeId,
						DictionaryType.DICOM_IMG_CHECK_PRO);
				addElementToData("listDicValue", listDicValue);
			}
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("使用模板")
	public String useReportTemplate() {
		try {
			SysService.instance.userReportTemplate(takePassport(), lTemplateId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询我最常用的20个模板")
	public String queryCommonTemplateList() {
		try {
			List<FReportTemplate> listTemplate = SysService.instance.queryCommonTemplateList(takePassport());
			addElementToData("listTemplate", listTemplate);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询所有检查项目字典值")
	// public String queryCheckProList(){
	// try {
	// List<TDicValue>
	// listCheckPro=SysService.instance.queryDicValueListByType(DictionaryType.MEDICAL_HIS_IMG_CHECK_PRO);
	// addElementToData("listCheckPro", listCheckPro);
	// } catch (Exception e) {
	// catchException(e);
	// }
	// return SUCCESS;
	// }

	@ActionDesc("添加一个词典值")
	public String addDicValue() {
		try {
			if (iType == null)
				throw new SysOperateException("必须指定字段类型！");
			if (iFlag == null)
				throw new SysOperateException("必须指定字段操作范围标识！");
			if (strValue == null)
				throw new SysOperateException("必须指定字段值！");
			TDicValue dicValue = SysService.instance.addDicValue(takePassport(), DictionaryType.parseFrom(iType),
					lParentId, strValue, strUnit);
			addElementToData("dicValue", dicValue);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("修改一个词典值")
	public String midifyDicValue() {
		try {
			if (strValue == null)
				throw new SysOperateException("必须指定字段值！");
			SysService.instance.midifyDicValue(takePassport(), lDicValueId, strValue);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("删除一个字典值")
	public String removeDicValue() {
		try {
			SysService.instance.removeDicValue(takePassport(), lDicValueId);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据设备类型获取所有部位类型")
	public String queryPartList() {
		try {
			if (lParentId == null)
				throw new NullParameterException("lParentId");
			List<TDicValue> listDicValue = SysService.instance.queryDicValueListByParent(lParentId,
					DictionaryType.BODY_PART_TYPE);
			addElementToData("listDicValue", listDicValue);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取所有设备类型")
	public String queryDeviceList() {
		try {
			List<TDicValue> listDicValue = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
			addElementToData("listDicValue", listDicValue);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询所有字典类型")
	public String querySysConfig() {
		try {
			List<DictionaryType> listDicType = DictionaryType.getAll();
			listDicType.remove(DictionaryType.BODY_PART_TYPE);
			listDicType.remove(DictionaryType.DICOM_IMG_CHECK_PRO);
			Collections.sort(listDicType, new Comparator<DictionaryType>() {
				@Override
				public int compare(DictionaryType o1, DictionaryType o2) {
					int mn = o1.getMaxNum() - o2.getMaxNum();
					if (mn != 0)
						return mn;
					return o1.getCode() - o2.getCode();
				}
			});
			addElementToData("listDicType", listDicType);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据类型获取字典值列表")
	public String queryDicValueListByType() {
		try {
			if (iType == null)
				throw new SysOperateException("必须要指定字典类型！");
			DictionaryType type = DictionaryType.parseFrom(iType);
			if (type == null)
				throw new SysOperateException("指定的字典类型不正确！");
			if (iType.equals(DictionaryType.DICOM_IMG_CHECK_PRO.getCode())) {
				List<TDicValue> listDicValue = SysService.instance.queryDicValueListByType(DictionaryType.DEVICE_TYPE);
				addElementToData("listDicValue", listDicValue);
			} else {
				List<TDicValue> listDicValue = SysService.instance.queryDicValueListByType(type);
				addElementToData("listDicValue", listDicValue);
			}
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据ID获取字典值")
	public String queryDicValueById() {
		try {
			TDicValue dicValue = SysService.instance.queryDicValueById(lDicValueId);
			addElementToData("dicValue", dicValue);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据父字典和要查询的字典类型查询子字典列表")
	public String queryDicValueListByParentId() {// 此字典类型是指本字典类型
		try {
			if (lParentId == null)
				throw new SysOperateException("请指定父字典ID和类型参数！");
			DictionaryType type = null;
			if (iType != null) {
				type = DictionaryType.parseFrom(iType);
				if (type == null)
					throw new SysOperateException("请指定字典类型不正确！");
			}
			List<TDicValue> listDicValue = SysService.instance.queryDicValueListByParent(lParentId, type);
			addElementToData("listDicValue", listDicValue);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("新建一个公共模板")
	public String createPublicTemplate() {
		try {
			TReportTemplate reportTemplate = SysService.instance.createPublicTemplate(takePassport(), template);
			addElementToData("reportTemplate", reportTemplate);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("新增一个私有模板")
	public String createPrivateTemplate() {
		try {
			TReportTemplate reportTemplate = SysService.instance.createPrivateTemplate(takePassport(), template);
			addElementToData("reportTemplate", reportTemplate);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("修改一个公共模板")
	public String modifyPublicTemplate() {
		try {
			SysService.instance.modifyPublicTemplate(takePassport(), template);
			this.lTemplateId = template.getId();
			return this.selectPublicTemplateById();
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("修改一个私有模板")
	public String modifyPrivateTemplate() {
		try {
			SysService.instance.modifyPrivateTemplate(takePassport(), template);
			this.lTemplateId = template.getId();
			return this.selectPrivateTemplateById();
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("删除一个模板")
	public String deleteTemplate() {
		try {
			SysService.instance.deleteTemplate(takePassport(), lTemplateId);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取公共模板")
	public String searchPublicTemplate() {
		try {
			Passport passport = this.takePassport();
			List<FReportTemplate> listTemplate = SysService.instance.searchPublicFTemplate(passport, null,
					lDeviceTypeId, partTypeId, spu);
			addElementToData("listTemplate", listTemplate);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取我的私有模板")
	public String searchMyTemplate() {
		try {
			Passport passport = this.takePassport();
			List<FReportTemplate> listTemplate = SysService.instance.searchPrivateFTemplate(passport, null,
					lDeviceTypeId, partTypeId, passport.getUserId(), spu);
			addElementToData("listTemplate", listTemplate);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("搜索我的模板以及公共")
	public String searchMyAndPublicFTemplate() {
		try {
			Passport passport = this.takePassport();
			List<FReportTemplate> listTemplate = SysService.instance.searchMyAndPublicFTemplate(passport, lDeviceTypeId,
					partTypeId, TemplateType.parseFrom(iType));
			addElementToData("listTemplate", listTemplate);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据ID获取公共模版")
	public String selectPublicTemplateById() {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			TReportTemplate reportTemplate = SysService.instance.selectPublicTemplateById(takePassport(), lTemplateId);
			if (reportTemplate != null) {
				TDicValue deviceType = SysService.instance.queryDicValueById(reportTemplate.getDevice_type_id(),
						session);
				TDicValue partType = SysService.instance.queryDicValueById(reportTemplate.getPart_type_id(), session);
				addElementToData("deviceType", deviceType);
				addElementToData("partType", partType);
			}
			addElementToData("reportTemplate", reportTemplate);
		} catch (BaseException e) {
			catchException(e);
		} finally {
			SessionFactory.closeSession(session);
		}
		return SUCCESS;
	}

	// @ActionDesc("根据ID获取我的模版")
	public String selectPrivateTemplateById() {
		try {
			FReportTemplate reportTemplate = SysService.instance.selectPrivateTemplateById(takePassport(), lTemplateId);
			addElementToData("reportTemplate", reportTemplate);
		} catch (BaseException e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public void setParentId(Long lParentId) {
		this.lParentId = filterParam(lParentId);
	}

	public void setType(Integer iType) {
		this.iType = filterParam(iType);
	}

	public void setValue(String strValue) {
		this.strValue = filterParam(strValue);
	}

	public void setDicValueId(Long lDicValueId) {
		this.lDicValueId = filterParam(lDicValueId);
	}

	public void setDeviceTypeId(Long lDeviceTypeId) {
		this.lDeviceTypeId = filterParam(lDeviceTypeId);
	}

	public void setPartTypeId(Long lPartTypeId) {
		this.partTypeId = filterParam(lPartTypeId);
	}

	public void setPart_type_id(Long part_type_id) {
		this.partTypeId = filterParam(part_type_id);
	}

	public void setTemplate(TReportTemplate template) {
		this.template = template;
	}

	public void setTemplateId(Long lTemplateId) {
		this.lTemplateId = filterParam(lTemplateId);
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setUnit(String strUnit) {
		this.strUnit = filterParam(strUnit);
	}

	public void setPartTypeName(String partTypeName) {
		this.partTypeName = partTypeName;
	}

	public void setFlag(Integer iFlag) {
		this.iFlag = filterParam(iFlag);
	}
}
