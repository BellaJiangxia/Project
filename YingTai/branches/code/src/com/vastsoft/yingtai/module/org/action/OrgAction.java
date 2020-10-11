package com.vastsoft.yingtai.module.org.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.util.exception.SystemException;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.core.Constants;
import com.vastsoft.yingtai.core.UserPermission;
import com.vastsoft.yingtai.exception.DbException;
import com.vastsoft.yingtai.exception.NotPermissionException;
import com.vastsoft.yingtai.exception.NullParameterException;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity.TDicomImg;
import com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.service.DicomImgService;
import com.vastsoft.yingtai.module.org.configs.constants.OrgConfigType;
import com.vastsoft.yingtai.module.org.configs.service.OrgConfigService;
import com.vastsoft.yingtai.module.org.constants.OrgChangeStatus;
import com.vastsoft.yingtai.module.org.constants.OrgProperty;
import com.vastsoft.yingtai.module.org.constants.OrgPublicStatus;
import com.vastsoft.yingtai.module.org.constants.OrgStatus;
import com.vastsoft.yingtai.module.org.constants.OrgVisible;
import com.vastsoft.yingtai.module.org.entity.TOrgChange;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.exception.OrgOperateException;
import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;
import com.vastsoft.yingtai.module.org.orgAffix.exception.OrgAffixException;
import com.vastsoft.yingtai.module.org.orgAffix.service.OrgAffixService;
import com.vastsoft.yingtai.module.org.product.assist.SearchOrgProductParam;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductChargeType;
import com.vastsoft.yingtai.module.org.product.constants.OrgProductStatus;
import com.vastsoft.yingtai.module.org.product.entity.FOrgProduct;
import com.vastsoft.yingtai.module.org.product.entity.TOrgProduct;
import com.vastsoft.yingtai.module.org.product.service.OrgProductService;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationPatientInfoShareType;
import com.vastsoft.yingtai.module.org.realtion.constants.OrgRelationStatus;
import com.vastsoft.yingtai.module.org.realtion.constants.PublishReportType;
import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelationConfig;
import com.vastsoft.yingtai.module.org.realtion.exception.OrgRelationException;
import com.vastsoft.yingtai.module.org.realtion.service.OrgRelationService;
import com.vastsoft.yingtai.module.org.realtion.service.SearchOrgRelationConfigParam;
import com.vastsoft.yingtai.module.org.service.OrgService;
import com.vastsoft.yingtai.module.user.constants.OrgUserMapperStatus;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.entity.TOrgUserMapping;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDesc;
import com.vastsoft.yingtai.utils.attributeUtils.AttributeUtils;

/**
 * 
 * @author HonF
 *
 */
public class OrgAction extends BaseYingTaiAction {
	private int iPageIdx;
	private int iPageSize;
	private Long orgId, dicom_img_id, lOrgCode, deviceTypeId, partTypeId;
	private OrgRelationStatus friendStatus;
	private TOrganization org;
	private String strOrgName;
	private boolean isPass, enable, boShare;
//	private List<OrgPermission> ops;
	private String strUserName;
	private String strMobile, jsonDeviceMapper;
	private PublishReportType reportType;
	private TOrgRelationConfig config;
	private OrgProductStatus productStatus;
	private String strProductName;
	private TOrgProduct product;
	private String strNote;
	private TOrgAffix orgAffix;
	private long lOrgAffixId;
	private int isJoin;
	private long lUserId;
	private OrgUserMapperStatus oumStatus;
	private String strDeviceTypeName;
	private String strPartTypeName;
	private String strMarkChar;
	private TBaseUser orgAdminUser;
	private List<TBaseUser> userList;
	private Map<String, TBaseUser> userMap;
	private OrgPublicStatus publicStatus;
	private OrgRelationPatientInfoShareType sharePatientInfo;
	private SplitPageUtil spu;
	private OrgStatus org_status;
	// private Long publish_report_org_id;
	private String org_name;
	private Long relation_id;
	private int customerType;// 1-自己机构，2-对方机构
	private OrgProductChargeType charge_type;
	private OrgProperty orgProperty;
	private OrgRelationPatientInfoShareType share_type;

	public String saveOrgAffix() {
		try {
			orgAffix = OrgAffixService.instance.saveOrgAffix(takePassport(), orgAffix);
			addElementToData("orgAffix", orgAffix);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 通过机构ID查询机构配置 */
	public String selectOrgAffixByOrgId() {
		try {
			TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgIdWithDefaultAffix(orgId);
			addElementToData("orgAffix", orgAffix);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String openOrClosePiShare() {
		try {
			OrgRelationService.instance.openOrClosePiShare(takePassport(), orgId, share_type);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 获取对当前机构提供了病例分享的合作机构 */
	public String queryRelationOrgWithPatientInfoShared() {
		try {
			SearchOrgRelationConfigParam orcsp = new SearchOrgRelationConfigParam(takePassport().getOrgId());
			orcsp.setSpu(spu);
			orcsp.setStatus(OrgRelationStatus.VALID);
			orcsp.setIrspi(ArrayTools.buildArray(OrgRelationPatientInfoShareType.INCOMPLETE_SHARED,OrgRelationPatientInfoShareType.SHARED));
			List<FOrgRelation> listRelationOrg = OrgRelationService.instance.searchOrgRelation(orcsp);
			addElementToData("listRelationOrg", listRelationOrg);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/** 通过ID查询机构 */
	public String queryOrgById() {
		try {
			TOrganization org = OrgService.instance.searchOrgById(orgId);
			addElementToData("org", org);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("保存设备显示映射")
	public String saveDeviceMapper() {
		try {
			OrgConfigService.instance.saveDeviceMapper(takePassport(), jsonDeviceMapper);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// /**拉取设备映射关系列表*/
	public String takeDeviceMapper() {
		try {
			List<Map<String, String>> mapDeviceMapper = OrgConfigService.instance.takeOrgConfigMapper(takePassport().getOrgId(),
					OrgConfigType.DEVICE_NAME_2_REPORT_DEVICE_NAME_MAPPER_CONFIG);
			addElementToData("mapDeviceMapper", mapDeviceMapper);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("禁用或启用机构")
	public String enableOrg() {
		try {
			OrgService.instance.enableOrg(takePassport(), orgId, enable);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("删除指定机构ID的机构及其用户关系和好友机构关系")
	public String removeOrgById() {
		try {
			OrgService.instance.removeOrg(takePassport(), orgId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询出所有有效机构")
	public String queryAllValidOrg() {
		try {
			List<TOrganization> listOrg = OrgService.instance.queryAllValidOrg(takePassport());
			String[] attrs = { "id", "org_name", "org_code", "description", "desc" };
			addElementToData("listOrg", AttributeUtils.SerializeList(listOrg, attrs));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取本机构的有效合作机构")
	public String queryMyOrgValidFriends() {
		try {
			Passport passport = this.isOnline();
			if (passport == null)
				return SUCCESS;
			List<TOrganization> listOrg = OrgService.instance.queryMyFriendOrgList(passport, null, OrgStatus.VALID);
			String[] attrs = { "id", "org_name", "org_code", "description", "desc" };
			addElementToData("listOrg", AttributeUtils.SerializeList(listOrg, attrs));
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("通过机构ID和设备、部位类型查询机构中所有可用的服务")
	public String queryAllProductOfOrgIdAndDPType() {
		try {
			SearchOrgProductParam sopp = new SearchOrgProductParam();
			sopp.setOrg_id(orgId);
			sopp.setDevice_type_id(deviceTypeId);
			sopp.setStatus(OrgProductStatus.VALID);
			// sopp.setPart_type_id(partTypeId);//暂时取消部位类型参与匹配机构服务
			List<FOrgProduct> listOrgProduct = OrgProductService.instance.searchOrgProduct(sopp);
			addElementToData("listOrgProduct", listOrgProduct);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("通过图像唯一标识获取配置")
	public String selectOrgAffixByOption() {
		try {
			TDicomImg img = null;
			TOrgAffix orgAffix = null;
			boolean wasLan = false;

			if ((dicom_img_id == null || dicom_img_id <= 0) && (StringTools.isEmpty(strMarkChar) || (lOrgAffixId <=0)))
				throw new OrgAffixException("参数错误，dicom_img_id或者markChar&orgAffixId 必须至少指定一种！");
			try {
				if (dicom_img_id != null && dicom_img_id > 0)
					img = DicomImgService.instance.queryDicomImgById(dicom_img_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (img == null) {
				if (strMarkChar != null)
					img = DicomImgService.instance.queryDicomImgByMarkChar(strMarkChar);
			}
			if (img == null) {
				orgAffix = OrgAffixService.instance.selectOrgAffixById(lOrgAffixId);
			} else {
				orgAffix = OrgAffixService.instance.selectOrgAffixById(img.getAffix_id());
			}
			if (orgAffix == null) {
				orgAffix = OrgAffixService.instance.queryDefaultOrgAffix();
			}
			if (orgAffix != null) {
				String addrIp = (String) takeBySession(Constants.CLIENT_IP);
				if (addrIp != null && !addrIp.trim().isEmpty() && orgAffix.getInternet_ip() != null
						&& !orgAffix.getInternet_ip().trim().isEmpty() && orgAffix.getIntranet_url() != null
						&& !orgAffix.getIntranet_url().trim().isEmpty()) {
					addrIp = addrIp.trim();
					if (addrIp.equals(orgAffix.getInternet_ip().trim()))
						wasLan = true;
				}
			}
			if (img == null)
				throw new OrgAffixException("影像未找到！");
			addElementToData("img", img);
			addElementToData("orgAffix", orgAffix);
			addElementToData("wasLan", wasLan);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取默认配置")
	public String queryDefaultOrgAffix() {
		try {
			TOrgAffix orgAffix = OrgAffixService.instance.queryDefaultOrgAffix();
			addElementToData("orgAffix", orgAffix);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("添加或修改默认配置")
	public String addOrUpdateDefaultOrgAffix() {
		try {
			TOrgAffix oa = OrgAffixService.instance.saveOrgAffix(takePassport(), orgAffix);
			addElementToData("orgAffix", oa);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("通过ID获取配置")
	public String selectOrgAffixById() {
		try {
			TOrgAffix orgAffix = OrgAffixService.instance.selectOrgAffixById(lOrgAffixId);
			addElementToData("orgAffix", orgAffix);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("删除配置")
	public String deleteOrgConfigById() {
		try {
			OrgAffixService.instance.deleteOrgAffixById(takePassport(), lOrgAffixId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("修改机构附加配置")
	public String modifyOrgAffix() {
		try {
			TOrgAffix oa = OrgAffixService.instance.saveOrgAffix(takePassport(), orgAffix);
			addElementToData("orgAffix", oa);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("添加或修改配置")
	public String addOrgAffix() {
		try {
			TOrgAffix oa = OrgAffixService.instance.saveOrgAffix(takePassport(), orgAffix);
			addElementToData("orgAffix", oa);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("查询指定机构的配置")
	public String queryOrgConfigByOrgId() {
		try {
			TOrgAffix orgAffix = OrgAffixService.instance.queryOrgAffixByOrgId(orgId);
			addElementToData("orgAffix", orgAffix);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 机构保存
	 * 
	 * @return
	 */
	@ActionDesc("机构保存")
	public String saveOrg() {
		Passport passport = this.takePassport();
		try {
			if (this.org.getId() == 0) {
				List<Long> scan_file_ids = new ArrayList<Long>(4);
				if (org.getScan_file_ids() != null && !org.getScan_file_ids().isEmpty()) {
					String[] sfs = org.getScan_file_ids().split(",");
					for (String s : sfs) {
						try {
							Long sf = Long.parseLong(s);
							scan_file_ids.add(sf);
						} catch (NumberFormatException e) {
						}
					}
				}

				OrgService.instance.createOrg(passport, org.getOrg_name(), org.getType(), scan_file_ids,
						org.getLogo_file_id(), org.getLevels(), org.getDescription(), org.getNote(),
						OrgPublicStatus.parseCode(org.getIs_public()),OrgProperty.parseCode(org.getOrg_property()));
			} else {
				OrgService.instance.modifyOrg(passport, this.org,false);
			}
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	// @ActionDesc("获取我的机构列表")
	public String queryMyOrgList() {
		try {
			Passport passport = this.isOnline();
			if (passport == null)
				return SUCCESS;
			List<TOrganization> orgList = OrgService.instance.selectMyOrgList(passport);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			for (TOrganization org : orgList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", org.getId());
				map.put("org_name", org.getOrg_name());
				map.put("creator", org.getCreator_name());
				map.put("creator_id", org.getCreator_id());
				map.put("create_date", org.getCreate_time());
				map.put("status", OrgStatus.parseCode(org.getStatus()));
				map.put("note", org.getNote());
				map.put("logo_file_id", org.getLogo_file_id());
				boolean orgMgr = UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR);
				map.put("is_mgr",
						(orgMgr && org.getId() == passport.getOrgId()) || (org.getCreator_id() == passport.getUserId()
								&& OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus()))));
				map.put("can_entry", OrgStatus.VALID.equals(OrgStatus.parseCode(org.getStatus())));
				list.add(map);
			}

			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 审核新建机构
	 * 
	 * @param 必需:orgId,isPass(true/false：通过/拒绝),ops(权限数组：[1,2]),note
	 * @return
	 */
	@ActionDesc("审核新建机构")
	public String approvedOrg() {
		try {
			Passport passport = this.isOnline();
			OrgService.instance.approvedOrg(passport, this.orgId, this.isPass, this.strNote);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 查询机构下所有用户
	 * 
	 * @param 必需：pageIdx,pageSize
	 * @return
	 */
	// @ActionDesc("查询当前机构下所有用户")
	public String queryUserListByOrg() {
		try {
			Passport passport = this.isOnline();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);
			int total = UserService.instance.queryUserListCountByOrg(passport, passport.getOrgId(), this.strUserName,
					this.strMobile, this.oumStatus);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TOrgUserMapping> userList = UserService.instance.queryUserListByOrgId(passport, null, this.strUserName,
					this.strMobile, this.oumStatus, spu);
			boolean isMgr = UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR);
			for (TOrgUserMapping u : userList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", u.getUser_id());
				map.put("user_name", u.getV_user_name());
				map.put("mobile", u.getV_mobile());
				String psn = "";
				if (!u.getPermissionList().isEmpty()) {
					for (UserPermission up : u.getPermissionList()) {
						psn += "," + up.getName();
					}
					psn = psn.substring(1);
				}
				map.put("permission", psn);
				map.put("create_time", u.getCreate_time());
				map.put("status", OrgUserMapperStatus.parseCode(u.getStatus()));
				map.put("note", u.getNote());

				map.put("can_approve",
						isMgr && OrgUserMapperStatus.APPROVING.equals(OrgUserMapperStatus.parseCode(u.getStatus())));
				map.put("is_mgr",
						isMgr && OrgUserMapperStatus.VALID.equals(OrgUserMapperStatus.parseCode(u.getStatus())));

				list.add(map);
			}

			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * 发出机构好友申请
	 * 
	 * @param 必需：orgId,reportType
	 * @return
	 */
	@ActionDesc("发出机构好友申请")
	public String addOrgFriend() {
		try {
			Long publish_report_org_id = null;
			Passport passport = takePassport();
			if (PublishReportType.CanChoose.equals(this.reportType)) {
				if (customerType == 1) {
					publish_report_org_id = passport.getOrgId();
				} else {
					publish_report_org_id = orgId;
				}
			}
			OrgRelationService.instance.createOrgFriend(passport, this.orgId, sharePatientInfo, this.reportType,
					publish_report_org_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String modifyPublishReportOrg() {
		try {
			Passport passport = this.isOnline();
			Long publish_report_org_id = -1L;
			TOrgRelation orgRelation = OrgRelationService.instance.selectRelationById(relation_id);
			if (orgRelation == null)
				throw new OrgRelationException("没有找到指定的合作机构关系！");
			if (orgRelation.getPublish_report_type() == PublishReportType.CanChoose.getCode()) {
				if (customerType == 1) {
					publish_report_org_id = passport.getOrgId();
				} else {
					if (orgRelation.getOrg_id() == passport.getOrgId()) {
						publish_report_org_id = orgRelation.getFriend_org_id();
					} else
						publish_report_org_id = orgRelation.getOrg_id();
				}
			}
			OrgRelationService.instance.modifyFriend(passport, this.relation_id, publish_report_org_id,
					sharePatientInfo);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 机构好友确认
	 * 
	 * @param 必需：orgId,isPass
	 * @return
	 */
	@ActionDesc("机构好友确认")
	public String confirmFriend() {
		try {
			Passport passport = this.isOnline();
			Long publish_report_org_id = null;
			if (PublishReportType.CanChoose.equals(this.reportType)) {
				if (customerType == 1) {
					publish_report_org_id = passport.getOrgId();
				} else {
					TOrgRelation orgRelation = OrgRelationService.instance.selectRelationById(relation_id);
					if (orgRelation == null)
						throw new OrgRelationException("没有找到指定的合作机构关系！");
					if (orgRelation.getOrg_id() == passport.getOrgId()) {
						publish_report_org_id = orgRelation.getFriend_org_id();
					} else
						publish_report_org_id = orgRelation.getOrg_id();
				}
			}
			OrgRelationService.instance.confrimFriend(passport, this.relation_id, this.isPass, publish_report_org_id,
					sharePatientInfo);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 机构好友撤销
	 * 
	 * @param 必需:orgId
	 * @return
	 */
	@ActionDesc("机构好友撤销")
	public String cancelFriend() {
		try {
			OrgRelationService.instance.cancelOrgFriend(takePassport(), this.relation_id);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 根据机构名称/编号查询机构
	 * 
	 * @param 必需：(strOrgName/lOrgCode
	 *            二选一),iPageIdx,iPageSize,可选：isJoin(1:加入机构查询，2:合作机构查询，默认所有)
	 * @return
	 */
	// @ActionDesc("根据机构名称/编号查询机构")
	public String queryOrgListByNameOrCode() {
		try {
			Passport passport = this.isOnline();
			int total = OrgService.instance.searchOrgCount(this.strOrgName, this.lOrgCode, null, null, OrgStatus.VALID,
					null, null,OrgVisible.SHOW);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TOrganization> orgList = OrgService.instance.searchOrgList(this.strOrgName, this.lOrgCode, null, null,
					OrgStatus.VALID, null, OrgVisible.SHOW, null, spu);

			List<TOrganization> tmpList = new ArrayList<TOrganization>(10);
			List<FOrgRelation> tmpRelList = new ArrayList<FOrgRelation>(10);

			if (this.isJoin == 1) {
				tmpList = OrgService.instance.searchOrgListByUser(passport, null);
			} else if (this.isJoin == 2) {
				tmpRelList = OrgRelationService.instance.searchOrgRelationOfSelfOrg(passport, null,
						OrgRelationStatus.VALID, null);
			}

			Iterator<TOrganization> it = orgList.iterator();
			while (it.hasNext()) {
				TOrganization org = it.next();
				if (this.isJoin == 1) {
					for (TOrganization o : tmpList) {
						if (o.getId() == org.getId() || org.getCreator_id() == passport.getUserId()) {
							it.remove();
							total--;
							break;
						}
					}
				} else if (this.isJoin == 2) {
					if (CollectionTools.isEmpty(tmpRelList)) {
						if (org.getId() == passport.getOrgId()) {
							it.remove();
							total--;
							continue;
						}
					} else {
						for (FOrgRelation r : tmpRelList) {
							if (r.getRelation_org_id() == org.getId()) {
								it.remove();
								total--;
								break;
							}
						}
					}
				}
			}

			this.addElementToData("total", total);
			this.addElementToData("list", orgList);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 根据Passport查询机构
	 * 
	 * @return
	 */
	// @ActionDesc("根据Passport查询机构")
	public String queryOrgByPassport() {
		try {
			Passport passport = this.isOnline();
			TOrganization org = OrgService.instance.searchOrgById(passport.getOrgId());
			this.addElementToData("org", org);
			this.addElementToData("is_mgr", UserService.instance.checkUserPermission(passport, UserPermission.ORG_MGR));
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	@ActionDesc("隐藏和现实机构")
	public String showOrg() {
		try {
			OrgService.instance.showOrg(takePassport());
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 查询好友
	 * 
	 * @param 可选:
	 *            iPageIdx,iPageSize,orgName,friendStatus
	 */
	// @ActionDesc("获取合作机构列表")
	public String queryMyFriends() {
		try {
			List<FOrgRelation> list = OrgRelationService.instance.searchOrgRelationOfSelfOrg(takePassport(),
					this.org_name, friendStatus, spu);
			addElementToData("list", list);
			addElementToData("spu", spu);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 获取当前机构合作关系配置
	 * 
	 * @param 必需：orgId
	 * @return
	 */
	// @ActionDesc("获取当前机构合作关系配置")
	public String queryRelationConfig() {
		try {
			Passport passport = this.isOnline();
			TOrgRelationConfig torc = OrgRelationService.instance.selectReportOrg(passport, this.orgId);
			this.addElementToData("config", torc);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 保存关系配置
	 * 
	 * @param 必需：config
	 * @return
	 */
	@ActionDesc("保存关系配置")
	public String saveRelationConfig() {
		try {
			Passport passport = this.isOnline();
			if (this.config.getId() == 0) {
				OrgRelationService.instance.addRelationConfig(passport, this.config);
			} else {
				OrgRelationService.instance.modifyRelationConfig(passport, this.config);
			}
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	@ActionDesc("保存机构产品")
	public String saveProduct() {
		try {
			this.product = OrgProductService.instance.createProduct(takePassport(), this.product);
			addElementToData("product", product);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	@ActionDesc("删除机构产品")
	public String deleteProduct() {
		try {
			Passport passport = this.isOnline();
			OrgProductService.instance.deleteProduct(passport, this.orgId);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String enableProduct() {
		try {
			Passport passport = this.isOnline();
			OrgProductService.instance.enableProduct(passport, this.orgId, this.isPass);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * 查询机构服务
	 * 
	 * @param 可选：
	 *            orgId,productName,pageIdx,pageSize,deviceTypeId,partTypeId,
	 *            productStatus
	 * @return
	 */
	// @ActionDesc("通过机构查询全部产品")
	public String queryProductsByOrg() {
		try {
			Passport passport = super.takePassport();
			SearchOrgProductParam sopp = new SearchOrgProductParam();
			sopp.setCharge_type(charge_type);
			sopp.setDevice_type_id(deviceTypeId);
			sopp.setPart_type_id(partTypeId);
			sopp.setLike_name(strProductName);
			sopp.setSpu(spu);
			sopp.setStatus(productStatus);
			sopp.setExclude_statuses(new OrgProductStatus[] { OrgProductStatus.INVALID });
			sopp.setOrg_id(passport.getOrgId());
			List<FOrgProduct> listOrgProduct = OrgProductService.instance.searchOrgProduct(sopp);
			super.addElementToData("listOrgProduct", listOrgProduct);
			super.addElementToData("spu", spu);
			//
			// int total =
			// OrgProductService.instance.queryProductCount(passport.getOrgId(),
			// this.strProductName, this.deviceTypeId,
			// this.partTypeId, OrgProductStatus.VALID);
			// if (this.iPageSize != 0) {
			// spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			// }
			// List<TOrgProduct> productList =
			// OrgProductService.instance.queryProductList(passport.getOrgId(),
			// this.strProductName,
			// this.deviceTypeId, this.partTypeId, OrgProductStatus.VALID, spu);
			// List<Map<String, Object>> list = new ArrayList<>(10);
			// boolean isMgr =
			// UserService.instance.checkUserPermission(passport,
			// UserPermission.ORG_MGR);
			// for (TOrgProduct p : productList) {
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("id", p.getId());
			// map.put("org_id", p.getOrg_id());
			// map.put("product_name", p.getName());
			// map.put("price", p.getPrice());
			// map.put("create_time", p.getCreate_time());
			// map.put("creator_name", p.getV_creator_name());
			// map.put("device_name", p.getV_device_name());
			// map.put("part_name", p.getV_part_name());
			// map.put("desc", p.getDescription());
			// map.put("status", OrgProductStatus.parseCode(p.getStatus()));
			//
			// map.put("can_delete",
			// isMgr &&
			// !OrgProductStatus.INVALID.equals(OrgProductStatus.parseCode(p.getStatus())));
			// map.put("can_enable",
			// isMgr &&
			// OrgProductStatus.DISABLE.equals(OrgProductStatus.parseCode(p.getStatus())));
			// map.put("can_disable",
			// isMgr &&
			// OrgProductStatus.VALID.equals(OrgProductStatus.parseCode(p.getStatus())));
			//
			// list.add(map);
			// }
			//
			// this.addElementToData("total", total);
			// this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	/**
	 * @param 必需：pageIdx,pageSize;可选：orgName,orgCode
	 * @return
	 */
	// @ActionDesc("管理员获取机构列表")
	public String queryOrgListByAdmin() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			boolean isAdmin = UserService.instance.checkUserPermission(passport, UserPermission.ADMIN_ORG_MGR)
					|| UserType.SUPER_ADMIN.equals(passport.getUserType());
			if (!isAdmin)
				throw new NotPermissionException();

			int total = OrgService.instance.searchOrgCount(this.strOrgName, this.lOrgCode, null, null, this.org_status,
					null, null,null);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TOrganization> orgList = OrgService.instance.searchOrgList(this.strOrgName, this.lOrgCode, null, null,
					this.org_status, null, null, null, spu);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);
			for (TOrganization org : orgList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("org_id", org.getId());
				map.put("org_name", org.getOrg_name());
				map.put("org_code", org.getOrg_code());
				map.put("org_creator", org.getCreator_name());
				map.put("create_time", org.getCreate_time());
				map.put("verifier_name", org.getVerifier_name());
				map.put("varify_time", org.getVarify_time());
				map.put("status", OrgStatus.parseCode(org.getStatus()));
//				String psn = "";
//				if (!org.getPermissionList().isEmpty()) {
//					for (OrgPermission op : org.getPermissionList()) {
//						psn += "," + op.getName();
//					}
//					psn = psn.substring(1);
//				}
//				map.put("org_type", psn);

				map.put("can_approve", OrgStatus.APPROVING.equals(OrgStatus.parseCode(org.getStatus())));

				list.add(map);
			}

			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	/**
	 * @param 必需：orgId
	 * @return
	 */
	// @ActionDesc("管理员获取机构信息")
	public String queryOrgByAdmin() {
		try {
			TOrganization to = OrgService.instance.queryOrgById(this.orgId);
			this.addElementToData("org", to);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

//	/**
//	 * @param 必需：orgId,ops
//	 * @return
//	 */
//	@ActionDesc(value = "管理员修改机构权限")
//	public String modifyOrgPermis() {
//		try {
//			Passport passport = this.isOnline();
//			OrgService.instance.authorizeOrg(passport, this.orgId, this.ops);
//		} catch (Exception e) {
//			catchException(e);
//		}
//		return SUCCESS;
//	}

	/**
	 * @param 必需：userId
	 * @return
	 */
	@ActionDesc(value = "机构删除所属用户")
	public String deleteOrgUserById() {
		try {
			Passport passport = this.isOnline();
			OrgService.instance.deleteUserById(passport, this.lUserId);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	// @ActionDesc("获取机构数量")
	public String approvingOrgCount() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		int count = 0, change_org_count = 0;
		try {
			count = OrgService.instance.searchOrgCount(null, null, null, null, OrgStatus.APPROVING, null, null,null);
			change_org_count = OrgService.instance.selectOrgChangeCount(passport, null, null, null,
					OrgChangeStatus.APPROVING);
		} catch (DbException e) {
			count = 0;
			change_org_count = 0;
		}

		this.addElementToData("approving_org_count", count);
		this.addElementToData("change_org_count", change_org_count);
		return SUCCESS;
	}

	// @ActionDesc("获取加入机构待审核数量")
	public String joinWatingOrgCount() {
		try {
			Passport passport = this.isOnline();
			int count = OrgRelationService.instance.selectMyRequestFriendsCount(passport);
			this.addElementToData("joining_org_count", count);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc("获取机构变更列表")
	public String queryOrgChangeList() {
		try {
			Passport passport = this.isOnline();
			int total = OrgService.instance.selectOrgChangeCount(passport, this.orgId, this.strOrgName, this.lOrgCode,
					null);
			SplitPageUtil spu = new SplitPageUtil(this.iPageIdx, this.iPageSize, total);
			List<TOrgChange> list = OrgService.instance.selectOrgChangeList(passport, this.orgId, this.strOrgName,
					this.lOrgCode, null, spu);

			this.addElementToData("total", total);
			this.addElementToData("list", list);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	// @ActionDesc(value = "管理员获取机构变更信息")
	public String queryOrgChangeByOrg() {
		try {
			Passport passport = this.isOnline();
			TOrgChange to = OrgService.instance.selectOrgChangeByOrg(passport, this.orgId);
			this.addElementToData("org", to);
		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	@ActionDesc("确认机构更改")
	public String confirmChange() {
		try {
			Passport passport = this.isOnline();
			OrgService.instance.confirmChange(passport, this.orgId, this.isPass, this.strNote);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	@ActionDesc("变更机构可见")
	public String changePublicStatus() {
		try {
			Passport passport = this.isOnline();
			OrgService.instance.modifyOrgPublic(passport, this.orgId, this.publicStatus);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	public String queryOrgLite() {
		try {
			Passport passport = this.isOnline();
			TOrganization org = OrgService.instance.getOrgLite(this.orgId);
			addElementToData("org", org);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	public String batCreateUser4OrgByAdmin() {
		Passport passport = this.isOnline();
		if (passport == null)
			return SUCCESS;

		try {
			if (this.userList != null && this.userMap != null) {
				if (this.userList.size() != this.userMap.size()) {
					this.setCode(100);
					this.setName("用户列表中出现重复手机号码");
					return SUCCESS;
				}

				for (TBaseUser tbu : this.userList) {
					if (tbu.getMobile().equals(this.orgAdminUser.getMobile())) {
						this.setCode(100);
						this.setName("机构负责人重复出现在用户列表中");
						return SUCCESS;
					}

					if (UserService.instance.isExistsLoginName(tbu.getMobile())) {
						this.setCode(100);
						this.setName("[" + tbu.getMobile() + "],该号码已经注册");
						return SUCCESS;
					}
				}
			}

//			if (this.org != null && this.ops != null) {
//				this.org.setPermission(PermissionsSerializer.toOrgPermissionString(this.ops));
//			}

			OrgService.instance.batAddUser4Org(passport, this.org, this.orgAdminUser, this.userList);
		} catch (Exception e) {
			catchException(e);
		}

		return SUCCESS;
	}

	public String queryMyOrgInfoById(){
		try {
			TOrganization org=OrgService.instance.getMyOrgInfoById(takePassport(),this.orgId);
			this.addElementToData("org",org);
		} catch (OrgOperateException | SystemException e) {
			catchException(e);
		}

		return SUCCESS;
	}

	public String reSubmit(){
		try {
			List<Long> scan_file_ids = new ArrayList<Long>(4);
			if (org.getScan_file_ids() != null && !org.getScan_file_ids().isEmpty()) {
        String[] sfs = org.getScan_file_ids().split(",");
        for (String s : sfs) {
          try {
            Long sf = Long.parseLong(s);
            scan_file_ids.add(sf);
          } catch (NumberFormatException e) {
          }
        }
      }

			OrgService.instance.reSubmit(takePassport(),org.getId(),org.getOrg_name(), org.getType(), scan_file_ids,
          org.getLogo_file_id(), org.getLevels(), org.getDescription(),OrgProperty.parseCode(org.getOrg_property()));

		} catch (Exception e) {
			catchException(e);
		}
		return SUCCESS;
	}

	public String requestCertification(){
		try {
			OrgService.instance.modifyOrg(takePassport(),this.org,true);
		} catch (NullParameterException | NotPermissionException | OrgOperateException | DbException e) {
			this.catchException(e);
		}

		return SUCCESS;
	}

	public String queryProductsById() {
		return SUCCESS;
	}

	public void setPageIdx(int iPageIdx) {
		this.iPageIdx = iPageIdx;
	}

	public void setPageSize(int iPageSize) {
		this.iPageSize = iPageSize;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = OrgRelationStatus.parseCode(friendStatus);
	}

	public void setOrg_status(int org_status) {
		this.org_status = OrgStatus.parseCode(org_status);
	}

	public void setOrgId(Long lOrgId) {
		this.orgId = lOrgId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = filterParam(deviceTypeId);
	}

	public void setPartTypeId(Long partTypeId) {
		this.partTypeId = filterParam(partTypeId);
	}

	public void setOrg(TOrganization org) {
		this.org = org;
	}

	// public void setOrgInfo(String strOrgInfo) {
	// this.strOrgInfo = strOrgInfo;
	// }

	public void setOrgName(String strOrgName) {
		this.strOrgName = strOrgName;
	}

	public void setOrgCode(String lOrgCode) {
		try {
			this.lOrgCode = Long.parseLong(lOrgCode);
		} catch (NumberFormatException e) {
			this.lOrgCode = null;
		}
	}

	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}

//	public void setOps(String ops) {
//		if (ops == null || ops.isEmpty()) {
////			this.ops = null;
//		} else {
//			try {
//				JSONArray arr = new JSONArray(ops);
////				List<OrgPermission> list = new ArrayList<>(4);
////				for (int i = 0; i < arr.length(); i++) {
////					OrgPermission op = OrgPermission.parseCode(arr.optInt(i));
////					if (op != null)
////						list.add(op);
////				}
//				this.ops = list;
//			} catch (Exception e) {
//				this.ops = null;
//			}
//		}
//	}

	public void setUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public void setMobile(String strMobile) {
		this.strMobile = strMobile;
	}

	public void setReportType(int reportType) {
		this.reportType = PublishReportType.parseFrom(reportType);
	}

	public void setConfig(TOrgRelationConfig config) {
		this.config = config;
	}

	public void setProductStatus(int productStatus) {
		this.productStatus = OrgProductStatus.parseCode(productStatus);
	}

	public void setProductName(String strProductName) {
		this.strProductName = strProductName;
	}

	public void setProduct(TOrgProduct product) {
		this.product = product;
	}

	public void setNote(String strNote) {
		this.strNote = strNote;
	}

	public void setOrgAffix(TOrgAffix orgAffix) {
		this.orgAffix = orgAffix;
	}

	public void setOrgAffixId(long lOrgAffixId) {
		this.lOrgAffixId = lOrgAffixId;
	}

	public void setIsJoin(int isJoin) {
		this.isJoin = isJoin;
	}

	public void setUserId(long lUserId) {
		this.lUserId = lUserId;
	}

	public void setOumStatus(String oumStatus) {
		try {
			int code = Integer.parseInt(oumStatus);
			this.oumStatus = OrgUserMapperStatus.parseCode(code);
		} catch (NumberFormatException e) {
			this.oumStatus = null;
		}
	}

	public void setMarkChar(String strMarkChar) {
		this.strMarkChar = strMarkChar;
	}

	public void setDeviceTypeName(String strDeviceTypeName) {
		this.strDeviceTypeName = filterParam(strDeviceTypeName);
	}

	public void setPartTypeName(String strPartTypeName) {
		this.strPartTypeName = filterParam(strPartTypeName);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setOrgAdminUser(TBaseUser orgAdminUser) {
		this.orgAdminUser = orgAdminUser;
	}

	public void setUserList(String userList) {
		if (userList == null || userList.isEmpty()) {
			this.userList = null;
		} else {
			try {
				JSONArray arr = new JSONArray(userList);
				List<TBaseUser> tbus = new ArrayList<TBaseUser>();
				this.userMap = new HashMap<String, TBaseUser>();
				for (int i = 0, len = arr.length(); i < len; i++) {
					JSONObject jo = arr.getJSONObject(i);
					TBaseUser tbu = new TBaseUser();
					tbu.setMobile(jo.optString("mobile", ""));
					tbu.setUser_name(jo.optString("user_name", ""));
					tbu.setType(jo.optInt("type", 0));

					tbus.add(tbu);
					this.userMap.put(tbu.getMobile(), tbu);
				}
				this.userList = tbus;
			} catch (Exception e) {
				this.userList = null;
			}
		}
	}

	public void setJsonDeviceMapper(String jsonDeviceMapper) {
		this.jsonDeviceMapper = filterParam(jsonDeviceMapper);
	}

	public void setPublicStatus(int publicStatus) {
		this.publicStatus = OrgPublicStatus.parseCode(publicStatus);
	}

	public void setSharePatientInfo(int sharePatientInfo) {
		this.sharePatientInfo = OrgRelationPatientInfoShareType.parseCode(sharePatientInfo);
		if (this.sharePatientInfo == null) {
			this.sharePatientInfo = OrgRelationPatientInfoShareType.NONE;
		}
	}

	public void setSpu(SplitPageUtil spu) {
		this.spu = spu;
	}

	public void setOrg_name(String org_name) {
		this.org_name = filterParam(org_name);
	}

	public void setBoShare(boolean boShare) {
		this.boShare = boShare;
	}

	public void setShare(boolean boShare) {
		this.boShare = boShare;
	}

	public void setRelation_id(Long relation_id) {
		this.relation_id = filterParam(relation_id);
	}

	public void setCustomerType(int customerType) {
		this.customerType = customerType;
	}

	public void setDicom_img_id(Long dicom_img_id) {
		this.dicom_img_id = filterParam(dicom_img_id);
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}

	public void setCharge_type(int charge_type) {
		this.charge_type = OrgProductChargeType.parseCode(charge_type);
	}

	public void setOrgProperty(int orgProperty) {
		this.orgProperty = OrgProperty.parseCode(orgProperty);
	}

	public void setShare_type(int share_type) {
		this.share_type = OrgRelationPatientInfoShareType.parseCode(share_type);
	}
}
