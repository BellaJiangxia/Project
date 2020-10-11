package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.config;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.B_BASY;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.M_BRJBXX;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.entity.Z_BAH;

public interface HisZhibangVer1Mapper {

	public List<B_BASY> selectLastB_BASYByCount();

	public List<M_BRJBXX> selectLastM_BRJBXXByCount();

	public List<Z_BAH> selectLastZ_BAHByCount();

	public List<B_BASY> selectB_BASY(Map<String, Object> mapArg);

	public List<M_BRJBXX> selectM_BRJBXX(Map<String, Object> mapArg);

	public List<Z_BAH> selectZ_BAH(Map<String, Object> mapArg);

	public List<Z_BAH> searchZ_BAHByPatientName(Map<String, Object> mapArg);

	public List<B_BASY> searchB_BASYByPatientName(Map<String, Object> mapArg);

	public List<M_BRJBXX> searchM_BRJBXXByByPatientName(Map<String, Object> mapArg);

}
