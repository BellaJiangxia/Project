package com.vastsoft.yingtai.module.org.orgAffix.mapper;

import java.util.List;

import com.vastsoft.yingtai.module.org.orgAffix.entity.TOrgAffix;

public interface OrgAffixMapper {
//	// 机构
//	public Integer insertOrg(TOrganization org);
//	
//	public Integer insertOrgByAdmin(TOrganization org);
//
//	public void modifyOrg(TOrganization org);
//
//	public List<TOrganization> selectAllOrgs(Map<String, Object> prms);
//
//	public Integer selectAllOrgCount(Map<String, Object> prms);
//
//	public TOrganization selectOrgById(long orgId);
//
//	public TOrganization selectOrgByIdAndLock(long orgId);
//
//	public void approveOrg(Map<String, Object> prms);
//
//	public List<TOrganization> selectOrgsByUserId(long userId);
//	
//	public List<TOrganization> selectMyOrgList(long userId);
	
	public List<TOrgAffix> selectAffixByOrgId(long orgId);
	
//	public void authorizeOrg(Map<String,Object> prms);
//	
//	public void insertOrgChange(TOrgChange toc);
//	
//	public void approvedOrgChange(Map<String,Object> prmss);
//	
//	public void modifyOrgNote(Map<String,Object> prms);
//	
//	public List<TOrgChange> selectChangeListByOrg(Map<String,Object> prms);
//
//	public Integer selectOrgChangeCount(Map<String,Object> prms);
//	
//	public TOrgChange selectChangeByOrg(long id);
//	
//	public TOrgChange selectOrgChangeByIdAndLock(long id);
//	
//	// 产品
//	public Integer insertProduct(TOrgProduct tos);
//
//	public void deleteProductById(long pid);
//
//	public List<TOrgProduct> selectProductsByOrg(Map<String, Object> prms);
//
//	public int selectProductsCount(Map<String, Object> prms);
//
//	public TOrgProduct selectProductById(Map<String, Object> prms);
//
//	public TOrgProduct selectProductByIdAndLock(long pid);
//
//	public void modifyProductStatus(Map<String, Object> prms);
//
//	public List<TOrgAffix> searchOrgAffix(Map<String, Object> mapArg);

	public void insertOrgAffix(TOrgAffix orgAffix);

	public void deleteOrgAffixById(long orgAffixId);

	public void updateOrgAffix(TOrgAffix orgAffix);

	public TOrgAffix selectOrgAffixById(long id);

//	public List<TOrgProduct> queryAllProductOfOrgIdAndDPType(Map<String, Object> mapArg);
//
//	public List<TOrgProduct> searchOrgProduct(Map<String, Object> mapArg);
//
//	public List<TOrganization> selectOrgByName(String org_name);
//
//	public List<TOrganization> queryMyFriendOrgList(Map<String, Object> mapArg);
//
//	public List<TOrganization> queryAllOrgs(Map<String, Object> mapArg);
//
//	public void deleteOrgUserMapperByOrgId(long org_id);
//
//	public void deleteRelationByOrgId(long org_id);
//
//	public void deleteRelationConfigByOrgId(long org_id);
//
//	public List<TOrganization> queryOrgOfUserCreate(long user_id);
//
//	public TOrgConfigs selectOrgConfigByOrgIdForUpdate(long org_id);
//
//	public void updateOrgConfigs(TOrgConfigs orgConfig);
//
//	public void insertOrgConfigs(TOrgConfigs orgConfig);
//
//	public TOrgAffix selectOrgAffixByIdForUpdate(long id);
//
//	public TOrgRelation selectOrgRelationByCoupleOrgId(TOrgRelation tOrgRelation);
//
//	public TOrgRelationConfig selectRelationConfigByOrc(TOrgRelationConfig tOrgRelationConfig);
//
//	public int selectRelationOrgMapperCount(Map<String, Object> mapArg);
//
//	public List<FOrgRelation> selectRelationOrgMapper(Map<String, Object> mapArg);
//
//
//	public List<TOrganization> selectOrgList4WX(Map<String,Object> prms);
}
