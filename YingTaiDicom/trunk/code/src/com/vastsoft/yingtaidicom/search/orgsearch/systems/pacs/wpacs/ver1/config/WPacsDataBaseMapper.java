package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.config;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Image;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Series;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.wpacs.ver1.entity.Study;

public interface WPacsDataBaseMapper {

	List<Image> selectImage(Map<String, Object> map);

//	List<TPatient> selectPatient(Map<String, Object> map);

	List<Study> selectStudy(Map<String, Object> map);

	List<Series> selectSeries(Map<String, Object> map);
	
}
