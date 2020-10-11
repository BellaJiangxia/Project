package com.vastsoft.yingtai.module.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.db.SessionFactory;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.constants.TemplateType;
import com.vastsoft.yingtai.module.sys.entity.FReportTemplate;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.entity.TReportTemplate;
import com.vastsoft.yingtai.module.sys.entity.TTemplateStat;
import com.vastsoft.yingtai.module.sys.exception.SysOperateException;
import com.vastsoft.yingtai.module.sys.mapper.SysMapper;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

/**
 * 
 * @author jyb
 *
 */
public class SysService extends SysBaseService {
	public final static SysService instance = new SysService();

	private SysService() {
	}
	// public String QueryDicomServerUrl() throws BaseException {
	// try {
	// if (dicom_server_url == null || (System.currentTimeMillis() -
	// lastQueryTime >= (60 * 1000))) {
	// TDicValue tdv =
	// this.queryDicValueListByType(DictionaryType.DICOM_SERVER_URL).get(0);
	// dicom_server_url = tdv.getValue().trim();
	// dicom_server_url = CommonTools.endWith(dicom_server_url, '/');
	// lastQueryTime = System.currentTimeMillis();
	// }
	// } catch (Exception e) {
	// throw e;
	// }
	// return dicom_server_url;
	// }

	// 字典
	/**
	 * 添加字典值
	 * 
	 * @param user
	 * @param dic_type
	 * @param div_value
	 * @throws BaseException
	 */
	public TDicValue addDicValue(Passport passport, DictionaryType dicType, Long lParentId, String strValue,
			String sreUnit) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (passport == null || dicType == null || strValue == null || strValue.isEmpty())
				throw new NullParameterException();
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_DIC))
				throw new NotPermissionException();
			if (dicType.getMaxNum() == 0)
				throw new SysOperateException("此类型的字典不允许添加条目！");
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TDicValue> listDicValue = mapper.selectValuesByType(dicType.getCode());
			if (dicType.getMaxNum() != -1) {
				if (listDicValue != null && listDicValue.size() >= dicType.getMaxNum())
					throw new SysOperateException("你要添加的字典类型条数已经达到上限，不可再添加该类型的字典值");
			}
			TDicValue tdv = new TDicValue();
			// TODO
			tdv.setFlag(dicType.getFlag().getCode());

			tdv.setDescription(dicType.getDesc());
			tdv.setDic_type(dicType.getCode());
			if (lParentId != null)
				tdv.setFather_dic_id(lParentId);
			tdv.setValue(strValue);
			tdv.setUnit(sreUnit);
			mapper.insertDicValue(tdv);
			session.commit();
			return tdv;
		} catch (Exception e) {
			if (session != null)
				session.rollback();
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 修改字典
	 * 
	 * @param user
	 * @param div_value
	 * @throws BaseException
	 */
	public void midifyDicValue(Passport passport, long lDicValueId, String strValue) throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_DIC))
			throw new NotPermissionException("你没有字典管理的权限！");

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TDicValue tdv = session.getMapper(SysMapper.class).selectValueByIdForUpdate(lDicValueId);

			if (tdv == null)
				throw new SysOperateException("未能找到的值");
			DictionaryType dtype = DictionaryType.parseFrom(tdv.getDic_type());
			if (!dtype.canModify())
				throw new SysOperateException("此项不能修改");
			tdv.setValue(strValue);
			session.getMapper(SysMapper.class).modifyDicValueyId(tdv);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 删除字典
	 * 
	 * @throws BaseException
	 */
	public void removeDicValue(Passport passport, long lDicValueId) throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_DIC))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();

			TDicValue tdv = session.getMapper(SysMapper.class).selectValueByIdForUpdate(lDicValueId);

			if (tdv == null)
				throw new SysOperateException("未能找到的值");
			DictionaryType dtype = DictionaryType.parseFrom(tdv.getDic_type());
			if (!dtype.canRemove())
				throw new SysOperateException("此项不能删除");

			session.getMapper(SysMapper.class).deleteDicValueById(lDicValueId);

			session.commit();

		} catch (SysOperateException e) {
			session.rollback();
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取验证码失效分钟数
	 * 
	 * @throws BaseException
	 */
	public int takeCheckCodeValidMinutes() throws BaseException {
		try {
			TDicValue dicValue = this.queryByType(DictionaryType.CHECK_CODE_MINUTES);
			return Integer.parseInt(dicValue.getValue().trim());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取文件保存路径
	 * 
	 * @throws BaseException
	 */
	public String takeFilePath(DictionaryType fileType) throws BaseException {
		try {
			return this.queryByType(fileType).getValue();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取系统提成定额
	 * 
	 * @throws BaseException @throws NumberFormatException @throws
	 **/
	public double takeSysDeductQuota() throws BaseException {
		try {
			return Double.parseDouble(queryByType(DictionaryType.SYS_DEDUCT_QUOTA).getValue().trim());
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	/**
	 * 获取默认密码
	 * 
	 * @throws BaseException
	 */
	public String takeDefaultPassword() throws BaseException {
		try {
			TDicValue dicValue = queryByType(DictionaryType.DEFAULT_PASSWORD);
			if (dicValue == null)
				throw new SysOperateException("没有找到这个类型的系统配置！");
			return dicValue.getValue().trim();
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	private TDicValue queryByType(DictionaryType dicType) throws BaseException {
		if (dicType == null) {
			throw new NullParameterException();
		}
		List<TDicValue> listValues = queryDicValueListByType(dicType);
		return listValues == null || listValues.size() <= 0 ? null : listValues.get(0);
	}

	/**
	 * 根据类型获取字典值列表
	 * 
	 * @throws BaseException
	 */
	public List<TDicValue> queryDicValueListByType(DictionaryType dicType) {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TDicValue> listValues = mapper.selectValuesByType(dicType.getCode());
			return listValues;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据ID获取字典值
	 * 
	 * @throws BaseException
	 */
	public TDicValue queryDicValueById(long lDicValueId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return super.queryDicValueById(lDicValueId, session);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据父字典和要查询的字典类型查询子字典列表
	 * @throws SysOperateException 
	 * 
	 * @throws BaseException
	 */
	public List<TDicValue> queryDicValueListByParent(long lParentId, DictionaryType dicType) throws SysOperateException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return super.queryDicValueListByParent(lParentId, dicType, session);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	// 模版
	/**
	 * 新增一个公共模板
	 * 
	 * @param user
	 * @param org
	 * @param tmpl
	 * @return
	 * @throws BaseException
	 */
	public TReportTemplate createPublicTemplate(Passport passport, TReportTemplate template) throws BaseException {
		if (passport == null || template == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_TEMPLATE_MGR))
				throw new NotPermissionException("你没有模板管理权限！");
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TReportTemplate> listTemplate = this.queryPublicTemplateByNameAndType(passport,
					template.getTemplate_name(), template.getDevice_type_id(), template.getPart_type_id());
			if (listTemplate != null && listTemplate.size() > 0) {
				throw new SysOperateException("模板重名，请重新命名！");
			}
			template.setUser_id(passport.getUserId());
			template.setType(TemplateType.PUBLIC.getCode());
			template.setTemplate_name(template.getTemplate_name().trim());
			if (template.getTemplate_name().length() >= 200)
				throw new SysOperateException("模板名称太长，最长不超过200字！");
			template.setPic_conclusion(template.getPic_conclusion().trim());
			if (template.getPic_conclusion().length() >= 1000)
				throw new SysOperateException("诊断意见太长，最长不超过1000字！");
			template.setPic_opinion(template.getPic_opinion().trim());
			if (template.getPic_conclusion().length() >= 1000)
				throw new SysOperateException("影像所见太长，最长不超过1000字！");
			if (template.getNote() != null) {
				template.setNote(template.getNote().trim());
				if (template.getNote().length() >= 1000)
					throw new SysOperateException("模板备注太长，最长不超过1000字！");
			}
			mapper.insertReportTemplate(template);
			session.commit();
			return template;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	/**
	 * 新增一个私有模板
	 * 
	 * @param user
	 * @param org
	 * @param tmpl
	 * @return
	 * @throws BaseException
	 */
	public TReportTemplate createPrivateTemplate(Passport passport, TReportTemplate template) throws BaseException {
		if (passport == null || template == null)
			throw new NullParameterException();
		this.checkReportTemplateObj(template);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!passport.getUserType().equals(UserType.ORG_DOCTOR))
				throw new NotPermissionException("你不是医生，不能添加模板！");
			if (StringTools.isEmpty(template.getTemplate_name()))
				throw new SysOperateException("模板名称必填！");
			template.setTemplate_name(template.getTemplate_name().trim());
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TReportTemplate> listTemplate = this.queryMyPrivateTemplateByNameAndType(passport,
					template.getTemplate_name(), template.getDevice_type_id(), template.getPart_type_id());
			if (!CollectionTools.isEmpty(listTemplate))
				throw new SysOperateException("模板重名，请重新命名！");
			template.setType(TemplateType.PRIVATE.getCode());
			template.setUser_id(passport.getUserId());
			mapper.insertReportTemplate(template);
			session.commit();
			return template;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}

	}

	private void checkReportTemplateObj(TReportTemplate template) throws SysOperateException {
		if (template == null)
			throw new SysOperateException("请指定模板对象");
		if (StringTools.isEmpty(template.getPic_conclusion()))
			throw new SysOperateException("诊断意见必须填写！");
		template.setPic_conclusion(template.getPic_conclusion().trim());
		if (template.getPic_conclusion().length() >= 1000)
			throw new SysOperateException("诊断意见太长，最长不超过1000字！");
		if (StringTools.isEmpty(template.getPic_opinion()))
			throw new SysOperateException("影像所见必须填写！");
		template.setPic_opinion(template.getPic_opinion().trim());
		if (template.getPic_conclusion().length() >= 1000)
			throw new SysOperateException("影像所见太长，最长不超过1000字！");
		if (template.getNote() != null) {
			template.setNote(template.getNote().trim());
			if (template.getNote().length() >= 1000)
				throw new SysOperateException("模板备注太长，最长不超过1000字！");
		}
		if (TemplateType.parseFrom(template.getType()) == null)
			template.setType(TemplateType.PRIVATE.getCode());
	}

	public List<TReportTemplate> queryPublicTemplateByNameAndType(Passport passport, String strTemplateName,
			long lDeviceTypeId, long lPartTypeId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_TEMPLATE_MGR)) {
				throw new SysOperateException("你没有模板管理权限！");
			}
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("template_name", strTemplateName);
			mapArg.put("device_type_id", lDeviceTypeId);
			mapArg.put("part_type_id", lPartTypeId);
			mapArg.put("type", TemplateType.PUBLIC.getCode());
			return session.getMapper(SysMapper.class).searchTemplate(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TReportTemplate> queryMyPrivateTemplateByNameAndType(Passport passport, String strTemplateName,
			long lDeviceTypeId, long lPartTypeId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!passport.getUserType().equals(UserType.ORG_DOCTOR)) {
				throw new SysOperateException("你不是医生，不能执行本操作！");
			}
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("user_id", passport.getUserId());
			mapArg.put("template_name", strTemplateName);
			mapArg.put("device_type_id", lDeviceTypeId);
			mapArg.put("part_type_id", lPartTypeId);
			mapArg.put("type", TemplateType.PRIVATE.getCode());
			return session.getMapper(SysMapper.class).searchTemplate(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 修改一个公共模板
	 * 
	 * @param templId
	 * @param user
	 * @param tmpl
	 * @throws BaseException
	 */
	public void modifyPublicTemplate(Passport passport, TReportTemplate template) throws BaseException {
		if (passport == null || template == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			this.selectPublicTemplateById(passport, template.getId());
			List<TReportTemplate> listTemplate = this.queryPublicTemplateByNameAndType(passport,
					template.getTemplate_name(), template.getDevice_type_id(), template.getPart_type_id());
			if (listTemplate != null && listTemplate.size() > 0) {
				if (listTemplate.size() > 1 || listTemplate.get(0).getId() != template.getId())
					throw new SysOperateException("模板重名，请重新命名！");
			}
			SysMapper mapper = session.getMapper(SysMapper.class);
			mapper.modifyReportTemplate(template);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 修改一个私有模板
	 * 
	 * @param templId
	 * @param user
	 * @param tmpl
	 * @throws BaseException
	 */
	public void modifyPrivateTemplate(Passport passport, TReportTemplate template) throws BaseException {
		if (passport == null || template == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			this.selectPrivateTemplateById(passport, template.getId());
			List<TReportTemplate> listTemplate = this.queryMyPrivateTemplateByNameAndType(passport,
					template.getTemplate_name(), template.getDevice_type_id(), template.getPart_type_id());
			if (listTemplate != null && listTemplate.size() > 0) {
				if (listTemplate.size() > 1 || listTemplate.get(0).getId() != template.getId())
					throw new SysOperateException("模板重名，请重新命名！");
			}
			SysMapper mapper = session.getMapper(SysMapper.class);
			mapper.modifyReportTemplate(template);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 删除
	 * 
	 * @param lTemplId
	 * @param user
	 * @throws BaseException
	 */
	public void deleteTemplate(Passport passport, long lTemplId) throws BaseException {
		if (passport == null)
			throw new NullParameterException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			TReportTemplate trt = session.getMapper(SysMapper.class).selectTemplateById(lTemplId);
			if (trt.getType() == TemplateType.PUBLIC.getCode()) {
				boolean hasPermission = UserService.instance.checkUserPermission(passport,
						UserPermission.ADMIN_TEMPLATE_MGR);
				if (!hasPermission)
					throw new NotPermissionException("你没有权限删除公共模板！");
			} else if (trt.getType() == TemplateType.PRIVATE.getCode()) {
				if (trt.getUser_id() != passport.getUserId())
					throw new NotPermissionException("你不能删除其他人的模板！");
			}
			session.getMapper(SysMapper.class).deleteReportTemplate(lTemplId);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 搜索模版
	 * 
	 * @param lUserId
	 * @param orgId
	 * @param strName
	 * @param lDeviceId
	 * @param lPartId
	 * @param status
	 * @param spu
	 * @return
	 * @throws BaseException
	 */
	public List<TReportTemplate> searchTemplate(Passport passport, String strName, Long lDeviceId, Long lPartId,
			Long lUserId, TemplateType type, SplitPageUtil spu) throws BaseException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("user_id", lUserId);
			prms.put("template_name", strName);
			prms.put("device_type_id", lDeviceId);
			prms.put("part_type_id", lPartId);
			prms.put("type", type == null ? null : type.getCode());
			if (spu != null) {
				int iCount = mapper.searchTemplateCount(prms);
				spu.setTotalRow(iCount);
				if (iCount <= 0)
					return new ArrayList<TReportTemplate>();
				prms.put("minRow", spu.getCurrMinRowNum());
				prms.put("maxRow", spu.getCurrMaxRowNum());
			}
			return mapper.searchTemplate(prms);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 搜索公共模版
	 * 
	 * @param passport
	 * @param strName
	 * @param lDeviceId
	 * @param lPartId
	 * @param lUserId
	 * @param spu
	 * @return
	 * @throws BaseException
	 */
	public List<FReportTemplate> searchPublicFTemplate(Passport passport, String strName, Long lDeviceId, Long lPartId,
			SplitPageUtil spu) throws BaseException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_TEMPLATE_MGR)) {
				throw new NotPermissionException("你没有权限执行本操作！");
			}
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("template_name", strName);
			prms.put("device_type_id", lDeviceId);
			prms.put("part_type_id", lPartId);
			prms.put("type", TemplateType.PUBLIC.getCode());
			if (spu != null) {
				int iCount = mapper.searchFTemplateCount(prms);
				spu.setTotalRow(iCount);
				if (iCount <= 0)
					return new ArrayList<FReportTemplate>();
				prms.put("minRow", spu.getCurrMinRowNum());
				prms.put("maxRow", spu.getCurrMaxRowNum());
			}
			return mapper.searchFTemplate(prms);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 搜索私有模版
	 * 
	 * @param passport
	 * @param strName
	 * @param lDeviceId
	 * @param lPartId
	 * @param lUserId
	 * @param spu
	 * @return
	 * @throws BaseException
	 */
	public List<FReportTemplate> searchPrivateFTemplate(Passport passport, String strName, Long lDeviceId, Long lPartId,
			Long lUserId, SplitPageUtil spu) throws BaseException {
		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			if (passport.getUserType() != UserType.ORG_DOCTOR) {
				throw new NotPermissionException("你不是医生，没有权限执行本操作！");
			}
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("user_id", lUserId);
			prms.put("template_name", strName);
			prms.put("device_type_id", lDeviceId);
			prms.put("part_type_id", lPartId);
			prms.put("type", TemplateType.PRIVATE.getCode());
			if (spu != null) {
				int iCount = mapper.searchFTemplateCount(prms);
				spu.setTotalRow(iCount);
				if (iCount <= 0)
					return new ArrayList<FReportTemplate>();
				prms.put("minRow", spu.getCurrMinRowNum());
				prms.put("maxRow", spu.getCurrMaxRowNum());
			}
			return mapper.searchFTemplate(prms);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据ID获取公共模版
	 * 
	 * @param lTmplId
	 * @return
	 * @throws BaseException
	 */
	public TReportTemplate selectPublicTemplateById(Passport passport, long lTmplId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_TEMPLATE_MGR)) {
				throw new SysOperateException("你没有模板管理权限！");
			}
			SysMapper mapper = session.getMapper(SysMapper.class);
			TReportTemplate reportTemplate = mapper.selectTemplateById(lTmplId);
			if (reportTemplate == null || reportTemplate.getType() != TemplateType.PUBLIC.getCode()) {
				throw new SysOperateException("指定的模板未找到！");
			}
			return reportTemplate;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 根据ID获取模版
	 * 
	 * @param lTmplId
	 * @return
	 * @throws BaseException
	 */
	public FReportTemplate selectPrivateTemplateById(Passport passport, long lTmplId) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!passport.getUserType().equals(UserType.ORG_DOCTOR)) {
				throw new SysOperateException("你不是医生，无权执行本操作！");
			}
			SysMapper mapper = session.getMapper(SysMapper.class);
			FReportTemplate reportTemplate = mapper.selectTemplateById(lTmplId);
			if (reportTemplate == null || reportTemplate.getType() != TemplateType.PRIVATE.getCode()
					|| reportTemplate.getUser_id() != passport.getUserId()) {
				throw new SysOperateException("指定的模板未找到！");
			}
			return reportTemplate;
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 获取最大允许的诊断信息的发送条数
	 * 
	 * @return
	 * @throws BaseException
	 */
	public int queryMaxReportMsgCount() throws BaseException {
		try {
			TDicValue dicValue = queryByType(DictionaryType.MAX_REPORT_MSG_COUNT);
			return Integer.parseInt(dicValue.getValue());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询出需要发送短信的时间间隔分钟数
	 * 
	 * @throws BaseException
	 */
	public int takeRemindTimeMinutes() throws BaseException {
		try {
			TDicValue dicValue = queryByType(DictionaryType.SEND_MSG_MINUTES);
			return Integer.parseInt(dicValue.getValue());
		} catch (Exception e) {
			throw e;
		}
	}

	// /**
	// * 获取系统短信前缀
	// *
	// * @throws BaseException
	// */
	// public String takeSysMsgPrefix() throws BaseException {
	// try {
	// TDicValue dicValue = queryByType(DictionaryType.MSG_PREFIX);
	// return dicValue.getValue();
	// } catch (Exception e) {
	// throw e;
	// }
	// }

	/**
	 * 获取机构编码下一个数值，起始值：111111
	 * 
	 * @throws BaseException
	 */
	public long takeNextOrgCode() throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TDicValue> listValues = mapper.selectValuesByTypeForUpdate(DictionaryType.ORG_CODE.getCode());
			if (listValues == null || listValues.size() <= 0)
				throw new SysOperateException("没有查询到对应类型的字段值列表！");
			TDicValue dicv = listValues.get(0);
			long iCode = Long.parseLong(dicv.getValue());
			dicv.setValue(String.valueOf(iCode + 7));
			mapper.modifyDicValueyId(dicv);
			session.commit();
			return iCode;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询或添加字典值，根据类型和值
	 * 
	 * @throws BaseException
	 */
	public TDicValue queryDicValueByTypeAndValue(String value, Long parent_id, DictionaryType dicType)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (value == null || value.trim().isEmpty())
				throw new SysOperateException("请指定有效的字典值！");
			if (dicType == null)
				throw new SysOperateException("请指定有效的字典类型！");
			value = value.trim();
			SysMapper mapper = session.getMapper(SysMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("dic_type", dicType.getCode());
			mapArg.put("father_dic_id", parent_id == null ? null : parent_id);
			mapArg.put("value", value);
			List<TDicValue> listValues = mapper.selectValuesByTypeAndValue(mapArg);
			TDicValue dicValue = null;
			if (listValues == null || listValues.size() <= 0) {
				return null;
			} else {
				dicValue = listValues.get(0);
			}
			return dicValue;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询或者添加部位类型
	 * 
	 * @throws BaseException
	 */
	public TDicValue queryAndAddPartType(String part_type_name, long device_type_id) throws BaseException {
		return this.queryAndAddDicValueByTypeAndValue(part_type_name, device_type_id, DictionaryType.BODY_PART_TYPE);
	}

	/**
	 * 查询或添加字典值，根据类型和值
	 * 
	 * @throws BaseException
	 */
	private TDicValue queryAndAddDicValueByTypeAndValue(String strValue, Long lParentId, DictionaryType dicType)
			throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (strValue == null || strValue.trim().isEmpty())
				throw new SysOperateException("请指定有效的字典值！");
			if (dicType == null)
				throw new SysOperateException("请指定有效的字典类型！");
			strValue = strValue.trim();
			SysMapper mapper = session.getMapper(SysMapper.class);
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("dic_type", dicType.getCode());
			mapArg.put("father_dic_id", lParentId == null ? null : lParentId);
			mapArg.put("value", strValue);
			List<TDicValue> listValues = mapper.selectValuesByTypeAndValue(mapArg);
			TDicValue dicValue = null;
			if (listValues == null || listValues.size() <= 0) {
				dicValue = new TDicValue();
				dicValue.setDescription(dicType.getName());
				dicValue.setDic_type(dicType.getCode());
				dicValue.setFlag(dicType.getFlag().getCode());
				if (lParentId != null) {
					dicValue.setFather_dic_id(lParentId);
				}
				dicValue.setValue(strValue);
				mapper.insertDicValue(dicValue);
			} else {
				dicValue = listValues.get(0);
			}
			session.commit();
			return dicValue;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 搜索我的模板和公共模板
	 * 
	 * @throws BaseException
	 */
	public List<FReportTemplate> searchMyAndPublicFTemplate(Passport passport, Long lDeviceTypeId, Long lPartTypeId,
			TemplateType templateType) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			if (passport.getUserType() != UserType.ORG_DOCTOR) {
				throw new NotPermissionException("你不是医生，没有权限执行本操作！");
			}
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("type", templateType.getCode());
			mapArg.put("device_type_id", lDeviceTypeId);
			mapArg.put("part_type_id", lPartTypeId);
			return mapper.searchMyAndPublicFTemplate(mapArg);
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 我使用一个模板
	 * 
	 * @param passport
	 * @param template_id
	 * @throws BaseException
	 */
	public void userReportTemplate(Passport passport, long template_id) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			if (!passport.getUserType().equals(UserType.ORG_DOCTOR))
				throw new SysOperateException("你不是医生用户！");
			SysMapper mapper = session.getMapper(SysMapper.class);
			TReportTemplate template = mapper.selectTemplateById(template_id);
			if (template == null)
				throw new SysOperateException("你使用的模板没找到！");
			Map<String, Object> mapArg = new HashMap<String, Object>();
			mapArg.put("user_id", passport.getUserId());
			mapArg.put("template_id", template_id);
			List<TTemplateStat> tempStat = mapper.selectTemplateByUserIdAndTemplateId(mapArg);
			TTemplateStat templateStat = null;
			if (tempStat == null || tempStat.size() <= 0) {
				templateStat = new TTemplateStat();
				templateStat.setLast_use_time(new Date());
				templateStat.setTemplate_id(template_id);
				templateStat.setUse_times(1);
				templateStat.setUser_id(passport.getUserId());
				mapper.addTemplateStat(templateStat);
			} else {
				templateStat = tempStat.get(0);
				templateStat.setLast_use_time(new Date());
				templateStat.setUse_times(templateStat.getUse_times() + 1);
				mapper.updateTemplateStat(templateStat);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 查询我最常用的20个模板
	 * 
	 * @throws BaseException
	 */
	public List<FReportTemplate> queryCommonTemplateList(Passport passport) throws BaseException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			return mapper.queryCommonTemplateList(passport.getUserId());
		} catch (Exception e) {
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 通过多个ID查询出词典值
	 * 
	 * @throws BaseException
	 */
	public List<TDicValue> queryDicValueListByIds(String ids) {
		if (ids == null || ids.trim().isEmpty())
			return null;
		String[] strss = StringTools.splitString(ids, ',', '，');
		if (strss == null || strss.length <= 0)
			return null;
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			SysMapper mapper = session.getMapper(SysMapper.class);
			List<TDicValue> listValue = new ArrayList<TDicValue>();
			for (String string : strss) {
				try {
					long idt = Long.parseLong(string);
					TDicValue value = mapper.selectValueById(idt);
					if (value == null)
						continue;
					listValue.add(value);
				} catch (Exception e) {
					continue;
				}
			}
			return listValue;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<TDicValue> queryCheckProByPartTypeId(long part_type_id) throws SysOperateException {
		return this.queryDicValueListByParent(part_type_id, DictionaryType.DICOM_IMG_CHECK_PRO);
	}

	public void queryAndAddCheckPro(String check_pro, long part_type_id, SqlSession session) throws BaseException {
		if (check_pro == null || check_pro.trim().isEmpty())
			return;
		String[] checkProNames = StringTools.splitString(check_pro, ',');
		if (checkProNames == null || checkProNames.length <= 0)
			return;
		SysMapper mapper = session.getMapper(SysMapper.class);
		List<TDicValue> listCheckPro = mapper.queryDicValueListByTypeAndParentId(
				new TDicValue(DictionaryType.DICOM_IMG_CHECK_PRO.getCode(), part_type_id));
		for (String name : checkProNames) {
			TDicValue tdv = TDicValue.existByValue(listCheckPro, name);
			if (tdv != null)
				continue;
			tdv = new TDicValue(DictionaryType.DICOM_IMG_CHECK_PRO.getCode(), part_type_id);
			tdv.setValue(name);
			mapper.insertDicValue(tdv);
		}
	}

	// public List<TDicValue> queryCheckProByDeviceTypeId(long device_type_id)
	// throws BaseException {
	// List<TDicValue> listPartType =
	// this.queryDicValueListByParent(device_type_id,
	// DictionaryType.BODY_PART_TYPE);
	// if (CommonTools.isEmpty(listPartType))
	// return null;
	// @SuppressWarnings("unchecked")
	// List<TDicValue> result = SetUniqueList.decorate(new ArrayList<>());
	// for (TDicValue tDicValue : listPartType) {
	// List<TDicValue> listTmp =
	// this.queryCheckProByPartTypeId(tDicValue.getId());
	// result.addAll(listTmp);
	// }
	// return result;
	// }

	// public TDicValue queryCheckProByName(String name) {
	//
	// }

	// public List<TDicValue> queryCheckProByNames(long part_type_id, String[]
	// names) throws BaseException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// SysMapper mapper = session.getMapper(SysMapper.class);
	// List<TDicValue> result = mapper.queryDicValueListByTypeAndParentId(
	// new TDicValue(DictionaryType.DICOM_IMG_CHECK_PRO.getCode(),
	// part_type_id));
	// if (result==null||result.isEmpty())
	// return result;
	// for (int i = result.size()-1; i >=0; i--) {
	// TDicValue dv = result.get(i);
	// if (!CommonTools.exist(names, dv.getValue()))
	// result.remove(i);
	// }
	// return result;
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }
}
