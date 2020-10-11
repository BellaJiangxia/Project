package com.vastsoft.yingtai.module.org.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.org.entity.TOrgChange;
import com.vastsoft.yingtai.module.org.entity.TOrganization;
import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelationConfig;

public interface OrgMapper {
	// 机构
	public Integer insertOrg(TOrganization org);

	public Integer insertOrgByAdmin(TOrganization org);

	public void modifyOrg(TOrganization org);

	public List<TOrganization> selectAllOrgs(Map<String, Object> prms);

	public Integer selectAllOrgCount(Map<String, Object> prms);

	public TOrganization selectOrgById(long orgId);

	public TOrganization selectOrgByIdAndLock(long orgId);

	public void approveOrg(Map<String, Object> prms);

	public List<TOrganization> selectOrgsByUserId(long userId);

	public List<TOrganization> selectMyOrgList(long userId);

	public void authorizeOrg(Map<String, Object> prms);

	public void insertOrgChange(TOrgChange toc);

	public void approvedOrgChange(Map<String, Object> prmss);

	public void modifyOrgNote(Map<String, Object> prms);

	public List<TOrgChange> selectChangeListByOrg(Map<String, Object> prms);

	public Integer selectOrgChangeCount(Map<String, Object> prms);

	public TOrgChange selectChangeByOrg(long id);

	public TOrgChange selectOrgChangeByIdAndLock(long id);

	public List<TOrganization> selectOrgByName(String org_name);

	public List<TOrganization> queryMyFriendOrgList(Map<String, Object> mapArg);

	public List<TOrganization> queryAllOrgs(Map<String, Object> mapArg);

	public void deleteOrgUserMapperByOrgId(long org_id);

	public void deleteRelationByOrgId(long org_id);

	public void deleteRelationConfigByOrgId(long org_id);

	public List<TOrganization> queryOrgOfUserCreate(long user_id);

	public TOrgRelation selectOrgRelationByCoupleOrgId(TOrgRelation tOrgRelation);

	public TOrgRelationConfig selectRelationConfigByOrc(TOrgRelationConfig tOrgRelationConfig);

	public int selectRelationOrgMapperCount(Map<String, Object> mapArg);

	public List<FOrgRelation> selectRelationOrgMapper(Map<String, Object> mapArg);

	public List<TOrganization> selectOrgList4WX(Map<String, Object> prms);

	public void reSubmit(TOrganization org);

	public void updateCertification(TOrganization org);

	List<TOrganization> selectReservationOrgList(Map<String,Object> prms);
}
