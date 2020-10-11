package com.vastsoft.yingtai.module.weixin.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.weixin.entity.TPatientExternalRelation;
import com.vastsoft.yingtai.module.weixin.entity.TPatientOrgMapping;

public interface PatientExternalRelationMapper
{
	public Long selectMaxId();

	public List<TPatientExternalRelation> select(Map<String,Object> prms);

	public List<TPatientExternalRelation> selectAndLock(TPatientExternalRelation q);

	public void updateState(TPatientExternalRelation s);

	public int insert(TPatientExternalRelation t);

	public List<TPatientExternalRelation> selectByOutSideUser(Map<String,Object> prms);

	public void updateStatus(TPatientExternalRelation t);

	public void intserOrgMapping(TPatientOrgMapping t);

	public void updateMappingState(Map<String,Object> prms);

	public List<TPatientOrgMapping> selectOrgMappingList(Map<String,Object> prms);

}
