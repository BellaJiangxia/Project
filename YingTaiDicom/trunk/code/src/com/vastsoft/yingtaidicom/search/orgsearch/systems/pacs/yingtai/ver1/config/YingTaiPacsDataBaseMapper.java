package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.config;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TImage;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TPatient;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TSeries;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.entity.TStudy;

public interface YingTaiPacsDataBaseMapper {

	List<TImage> selectImage(Map<String, Object> map);

	List<TPatient> selectPatient(Map<String, Object> map);

	List<TStudy> selectStudy(Map<String, Object> map);

	List<TSeries> selectSeries(Map<String, Object> map);
	
}
