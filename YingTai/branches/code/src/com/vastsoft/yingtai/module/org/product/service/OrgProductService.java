package com.vastsoft.yingtai.module.org.product.service;

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
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.FSeries;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.exception.DicomImgException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.product.assist.SearchOrgProductParam;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductStatus;
import com.vastsoft.yingtai.module.org.product.entity.FOrgProduct;
import com.vastsoft.yingtai.module.org.product.entity.TOrgProduct;
import com.vastsoft.yingtai.module.org.product.mapper.OrgProductMapper;
import com.vastsoft.yingtai.module.sys.constants.DictionaryType;
import com.vastsoft.yingtai.module.sys.constants.SysDataStatus;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;
import com.vastsoft.yingtai.module.sys.service.SysService;
import com.vastsoft.yingtai.module.user.exception.UserOperateException;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

public class OrgProductService {
	public static final OrgProductService instance = new OrgProductService();

	private OrgProductService() {
	}

	public List<FOrgProduct> searchOrgProduct(SearchOrgProductParam sopp)
			throws IllegalArgumentException, IllegalAccessException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.searchOrgProduct(sopp, session);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public List<FOrgProduct> searchOrgProduct(SearchOrgProductParam sopp, SqlSession session)
			throws IllegalArgumentException, IllegalAccessException {
		OrgProductMapper mapper = session.getMapper(OrgProductMapper.class);
		Map<String, Object> mapArg = sopp.buildMap();
		if (sopp.getSpu() != null) {
			int count = mapper.selectOrgProductCount(mapArg);
			sopp.getSpu().setTotalRow(count);
			mapArg.put("minRow", sopp.getSpu().getCurrMinRowNum());
			mapArg.put("maxRow", sopp.getSpu().getCurrMaxRowNum());
		}
		return mapper.selectOrgProduct(mapArg);
	}

	// /**
	// * @throws OrgOperateException
	// */
	// public int queryProductCount(long lOrgId, String strProductName, Long
	// lDeviceTypeId, Long lPartTypeId,
	// OrgProductStatus status) throws OrgOperateException {
	// SqlSession session = null;
	// try {
	// session = SessionFactory.getSession();
	// Map<String, Object> prms = new HashMap<String, Object>();
	// prms.put("org_id", lOrgId);
	// prms.put("product_name", strProductName == null ||
	// strProductName.isEmpty() ? null : strProductName);
	// prms.put("device_type_id", lDeviceTypeId);
	// prms.put("part_type_id", lPartTypeId);
	// prms.put("status", status == null ? null : status.getCode());
	// return
	// session.getMapper(OrgProductMapper.class).selectProductsCount(prms);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new OrgOperateException(e);
	// } finally {
	// SessionFactory.closeSession(session);
	// }
	// }

	public List<FOrgProduct> queryAllProductOfOrgIdAndDPType(long lOrgId, long device_type_id, Long part_type_id)
			throws BaseException, IllegalArgumentException, IllegalAccessException {
		SearchOrgProductParam sopp = new SearchOrgProductParam();
		sopp.setDevice_type_id(device_type_id);
		sopp.setPart_type_id(part_type_id == null ? null : (part_type_id <= 0 ? null : part_type_id));
		sopp.setExclude_statuses(new OrgProductStatus[] { OrgProductStatus.INVALID });
		sopp.setOrg_id(lOrgId);
		return this.searchOrgProduct(sopp);
		// SqlSession session = null;
		// try {
		// session = SessionFactory.getSession();
		// if (passport == null)
		// throw new OrgOperateException("你未登录，请重新登陆！");
		// if (lOrgId <= 0)
		// throw new OrgOperateException("请指定有效的机构ID");
		// if (device_type_id <= 0)
		// throw new OrgOperateException("缺少设备类型！");
		// // if (strPartTypeName==null||strDeviceTypeName.trim().isEmpty())
		// // throw new OrgOperateException("请指定有效的部位类型！");
		// // TDicValue deviceType =
		// // SysService.instance.queryAndAddDicValueByTypeAndValue(passport,
		// // strDeviceTypeName,
		// // null, DictionaryType.DEVICE_TYPE);
		// OrgProductMapper mapper = session.getMapper(OrgProductMapper.class);
		// Map<String, Object> mapArg = new HashMap<String, Object>();
		// mapArg.put("org_id", lOrgId);
		// mapArg.put("device_type_id", device_type_id);
		// if (part_type_id != null && part_type_id > 0) {
		// // TDicValue partType =
		// // SysService.instance.queryAndAddDicValueByTypeAndValue(passport,
		// // strPartTypeName,
		// // deviceType.getId(), DictionaryType.BODY_PART_TYPE);
		// mapArg.put("part_type_id", part_type_id);
		// }
		// mapArg.put("status", OrgProductStatus.VALID.getCode());
		// return mapper.queryAllProductOfOrgIdAndDPType(mapArg);
		// } catch (Exception e) {
		// throw e;
		// } finally {
		// SessionFactory.closeSession(session);
		// }
	}

	/**
	 * 获取指定机构的机构服务
	 * 
	 * @param lOrgId
	 * @param strProductName
	 * @param lDeviceTypeId
	 * @param lPartTypeId
	 * @param status
	 * @param spu
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws OrgOperateException
	 */
	public List<FOrgProduct> queryProductList(long lOrgId, String strProductName, Long lDeviceTypeId, Long lPartTypeId,
			OrgProductStatus status, SplitPageUtil spu) throws IllegalArgumentException, IllegalAccessException {
		SearchOrgProductParam sopp = new SearchOrgProductParam();
		sopp.setDevice_type_id(lDeviceTypeId == null ? null : (lDeviceTypeId <= 0 ? null : lDeviceTypeId));
		sopp.setPart_type_id(lPartTypeId == null ? null : (lPartTypeId <= 0 ? null : lPartTypeId));
		sopp.setStatus(status);
		sopp.setOrg_id(lOrgId);
		sopp.setLike_name(strProductName);
		sopp.setSpu(spu);
		return this.searchOrgProduct(sopp);
		// SqlSession session = null;
		// try {
		// session = SessionFactory.getSession();
		// Map<String, Object> prms = new HashMap<String, Object>();
		// if (spu != null) {
		// prms.put("begin_idx", spu.getCurrMinRowNum());
		// prms.put("end_idx", spu.getCurrMaxRowNum());
		// }
		// prms.put("org_id", lOrgId);
		// prms.put("product_name", strProductName == null ||
		// strProductName.isEmpty() ? null : strProductName);
		// prms.put("device_type_id", lDeviceTypeId);
		// prms.put("part_type_id", lPartTypeId);
		// prms.put("status", status == null ? null : status.getCode());
		// return
		// session.getMapper(OrgProductMapper.class).selectProductsByOrg(prms);
		// } finally {
		// SessionFactory.closeSession(session);
		// }
	}

	/**
	 * 根据ID获取服务
	 * 
	 * @throws NullParameterException
	 * @throws DbException
	 */
	public TOrgProduct searchProductById(long lProductId) {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lProductId);
			return session.getMapper(OrgProductMapper.class).selectProductById(prms);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 检查机构产品
	 * 
	 * @param passport
	 * @param lProductId
	 * @param lOrgId
	 * @return
	 */
	public boolean checkProductByOrg(Passport passport, long lProductId, Long lOrgId) {
		if (passport == null)
			return false;

		long oid = lOrgId == null ? passport.getOrgId() : lOrgId.longValue();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lProductId);
			prms.put("org_id", oid);
			prms.put("status", OrgProductStatus.VALID.getCode());
			TOrgProduct top = session.getMapper(OrgProductMapper.class).selectProductById(prms);
			return top != null;
		} catch (Exception e) {
			return false;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 启/禁用
	 * 
	 * @param passport
	 * @param lProductId
	 * @param isEnable
	 * @throws NullParameterException
	 * @throws UserOperateException
	 * @throws DbException
	 * @throws NotPermissionException
	 * @throws OrgOperateException
	 */
	public void enableProduct(Passport passport, long lProductId, boolean isEnable)
			throws NullParameterException, NotPermissionException, OrgOperateException {
		if (passport == null)
			throw new NullParameterException("passport");
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgProductMapper mapper = session.getMapper(OrgProductMapper.class);
			TOrgProduct tos = mapper.selectProductByIdAndLock(lProductId);

			if (tos == null)
				throw new OrgOperateException("未知的产品");

			if (tos.getOrg_id() != passport.getOrgId())
				throw new NotPermissionException();

			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lProductId);
			prms.put("status", isEnable ? OrgProductStatus.VALID.getCode() : OrgProductStatus.DISABLE.getCode());
			mapper.modifyProductStatus(prms);
			session.commit();
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 删除服务
	 * 
	 * @throws NullParameterException
	 * @throws DbException
	 * @throws NotPermissionException
	 * @throws UserOperateException
	 * @throws OrgOperateException
	 */
	public void deleteProduct(Passport passport, long lProductId)
			throws NullParameterException, NotPermissionException, OrgOperateException {
		if (passport == null)
			throw new NullParameterException("passport");
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException();

		SqlSession session = null;

		try {
			session = SessionFactory.getSession();
			OrgProductMapper mapper = session.getMapper(OrgProductMapper.class);
			TOrgProduct tos = mapper.selectProductByIdAndLock(lProductId);
			if (tos == null)
				throw new OrgOperateException("未知的产品");
			if (tos.getOrg_id() != passport.getOrgId())
				throw new NotPermissionException();

			// session.getMapper(OrgMapper.class).deleteProductById(lProductId);
			Map<String, Object> prms = new HashMap<String, Object>();
			prms.put("id", lProductId);
			prms.put("status", OrgProductStatus.INVALID.getCode());
			mapper.modifyProductStatus(prms);
			session.commit();
		} catch (OrgOperateException | NotPermissionException e) {
			session.rollback(true);
			throw e;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	/**
	 * 新增服务
	 * 
	 * @throws Exception
	 * 
	 * @throws BaseException
	 */
	// Passport passport, String strName, double dfPrice, long lDeviceTypeId,
	// Long lPartTypeId, String strDesc
	public synchronized TOrgProduct createProduct(Passport passport, TOrgProduct orgProduct) throws Exception {
		if (passport == null)
			throw new NullParameterException("passport");
		if (!UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR))
			throw new NotPermissionException("你没有机构管理的权限！");
		orgProduct.setOrg_id(passport.getOrgId());
		this.checkProductObject(orgProduct);
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			OrgProductMapper mapper = session.getMapper(OrgProductMapper.class);
			SearchOrgProductParam sopp = new SearchOrgProductParam();
			sopp.setExclude_statuses(new OrgProductStatus[] { OrgProductStatus.INVALID });
			sopp.setOrg_id(passport.getOrgId());
			sopp.setName(orgProduct.getName());
			sopp.setSpu(new SplitPageUtil(1, 5));
			List<FOrgProduct> listOrgProduct = this.searchOrgProduct(sopp);
			if (listOrgProduct != null && listOrgProduct.size() > 0)
				throw new OrgOperateException("服务名称重名，请重新命名！");
			orgProduct.setCreate_time(new Date());
			orgProduct.setCreator_id(passport.getUserId());
			TDicValue dicValue = SysService.instance.queryDicValueById(orgProduct.getDevice_type_id(), session);
			if (dicValue == null)
				throw new OrgOperateException("指定的设备类型未找到！");
			if (DictionaryType.DEVICE_TYPE.getCode() != dicValue.getDic_type())
				throw new OrgOperateException("指定的设备类型无效！");
			if (orgProduct.getPart_type_id() > 0) {
				dicValue = SysService.instance.queryDicValueById(orgProduct.getPart_type_id(), session);
				if (dicValue == null)
					throw new OrgOperateException("指定的部位类型未找到！");
				if (DictionaryType.BODY_PART_TYPE.getCode() != dicValue.getDic_type())
					throw new OrgOperateException("指定的部位类型无效！");
			}
			orgProduct.setOrg_id(passport.getOrgId());
			orgProduct.setStatus(SysDataStatus.VALID.getCode());
			mapper.insertProduct(orgProduct);
			session.commit();
			return orgProduct;
		} catch (Exception e) {
			session.rollback(true);
			throw e;
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	private void checkProductObject(TOrgProduct orgProduct) throws OrgOperateException {
		if (orgProduct == null)
			throw new OrgOperateException("请指定诊断服务对象！");
		if (OrgProductChargeType.parseCode(orgProduct.getCharge_type()) == null)
			throw new OrgOperateException("请指定有效的服务收费类型！");
		if (orgProduct.getDescription() != null) {
			orgProduct.setDescription(orgProduct.getDescription().trim());
			if (orgProduct.getDescription().length() >= 1000)
				throw new OrgOperateException("描述太长了，最长1000个字！");
		}
		if (orgProduct.getDevice_type_id() <= 0)
			throw new OrgOperateException("请指定有效的设备类型！");
		if (StringTools.isEmpty(orgProduct.getName()))
			throw new OrgOperateException("服务名称必须指定！");
		orgProduct.setName(orgProduct.getName().trim());
		if (orgProduct.getName().length() >= 100)
			throw new OrgOperateException("诊断服务名称太长了，最长100个字！");
		if (orgProduct.getOrg_id() <= 0)
			throw new OrgOperateException("必须指定所属机构ID！");
		if (orgProduct.getPart_type_id() < 0)
			orgProduct.setPart_type_id(0);
		if (orgProduct.getPrice() <= 0)
			throw new OrgOperateException("服务单价必须大于0！");
		if (OrgProductStatus.parseCode(orgProduct.getStatus()) == null)
			throw new OrgOperateException("诊断服务状态不合法！");
	}

	public FOrgProduct queryOrgProductById(long org_product_id)
			throws IllegalArgumentException, IllegalAccessException {
		SqlSession session = null;
		try {
			session = SessionFactory.getSession();
			return this.queryOrgProductById(org_product_id, session);
		} finally {
			SessionFactory.closeSession(session);
		}
	}

	public FOrgProduct queryOrgProductById(long org_product_id, SqlSession session)
			throws IllegalArgumentException, IllegalAccessException {
		SearchOrgProductParam sopp = new SearchOrgProductParam();
		sopp.setId(org_product_id);
		sopp.setExclude_statuses(new OrgProductStatus[] { OrgProductStatus.INVALID });
		List<FOrgProduct> listOrgProduct = this.searchOrgProduct(sopp, session);
		return CollectionTools.isEmpty(listOrgProduct) ? null : listOrgProduct.get(0);
	}

	public double calculatePriceByDicomImgIdAndOrgProductId(long dicom_img_id, int charge_amount,long org_product_id, SqlSession session)
			throws Exception {
		if (charge_amount <= 0)
			throw new DicomImgException("计费次数不能为0！");
		FDicomImg dicomImg = DicomImgService.instance.queryDicomImgById(dicom_img_id, session);
		if (dicomImg == null)
			throw new DicomImgException("指定的影像未找到！");
		FOrgProduct orgProduct = this.queryOrgProductById(org_product_id, session);
		if (orgProduct == null)
			throw new DicomImgException("指定的机构服务未找到！");
		if (orgProduct.getStatus() != OrgProductStatus.VALID.getCode())
			throw new DicomImgException("指定的机构服务未找到！");
		double result = 0.0;
		if (orgProduct.getCharge_type() == OrgProductChargeType.CHARGE_EXPOSE_TIMES_TYPE.getCode()) {
			int expose_times = charge_amount;
//			if (CollectionTools.isEmpty(dicomImg.getListSeries()))
//				throw new DicomImgException("指定的机构服务是按曝光次数计费，但要提交诊断申请的影像没有序列，不" + "能提交！您可以尝试选择按申请次数计费的机构服务！");
//			for (FSeries series : dicomImg.getListSeries())
//				expose_times += series.getExpose_times();
			if (expose_times <= 0)
				throw new DicomImgException("指定的机构服务是按曝光次数计费，但要提交诊断申请的影像曝光次数为0，不" + "能提交！您可以尝试选择按申请次数计费的机构服务！");
			result = orgProduct.getPrice() * expose_times;
		} else if (orgProduct.getCharge_type() == OrgProductChargeType.CHARGE_PART_TYPE.getCode()) {
			if (charge_amount <= 0)
				throw new DicomImgException("指定的机构服务是按序列数计费，但要提交诊断申请的影像没有序列，不" + "能提交！您可以尝试选择按申请次数计费的机构服务！");
			result = orgProduct.getPrice() * charge_amount;
		} else if (orgProduct.getCharge_type() == OrgProductChargeType.CHARGE_REQUEST_TIMES_TYPE.getCode()) {
			result = orgProduct.getPrice();
		} else {
			throw new DicomImgException("不支持的机构服务计费类型，请联系系统管理员！");
		}
		return result;
	}

}
