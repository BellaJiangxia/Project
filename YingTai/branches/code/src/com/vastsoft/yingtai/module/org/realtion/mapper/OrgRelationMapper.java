package com.vastsoft.yingtai.module.org.realtion.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.org.realtion.entity.FOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelation;
import com.vastsoft.yingtai.module.org.realtion.entity.TOrgRelationConfig;

public interface OrgRelationMapper {
	// 好友
	public void addFriend(TOrgRelation friend);

	public void deleteFriend(long friendId);
	
	public int selectFriendsCountByOrg(Map<String,Object> prms);

	public List<TOrgRelation> selectFriendsByOrg(Map<String, Object> prms);

	public TOrgRelation selectMyFriendByOrg(Map<String, Object> prms);

	public TOrgRelation selectRelationById(long relationId);

	public TOrgRelation selectRelationByIdAndLock(long relationId);

	public Integer insertRelationConfig(TOrgRelationConfig trc);

	public void updateRelationConfig(TOrgRelationConfig trc);

	public TOrgRelationConfig selectRelationConfigByOrg(Map<String, Object> prms);

	public TOrgRelationConfig selectRelationConfigByIdAndLock(long lConfigId);

	public void deleteRelationConfigByRelation(long lRelationId);

	public TOrgRelation selectOrgRelationByCoupleOrgId(TOrgRelation tOrgRelation);

	public TOrgRelationConfig selectRelationConfigByOrc(TOrgRelationConfig tOrgRelationConfig);

	public int selectRelationOrgMapperCount(Map<String, Object> mapArg);

	public List<FOrgRelation> selectRelationOrgMapper(Map<String, Object> mapArg);

	public void updateOrgRelation(TOrgRelation friendRelation);

	public int selectWaitApproveFriendsCount(Map<String, Object> prms);

	public int selectOrgRelationCount(Map<String, Object> mapArg);

	public List<TOrgRelation> selectOrgRelation(Map<String, Object> mapArg);

	public TOrgRelationConfig selectOrgRelationConfigByRelationIdAndOrgIdForUpdate(
			TOrgRelationConfig tOrgRelationConfig);
}
