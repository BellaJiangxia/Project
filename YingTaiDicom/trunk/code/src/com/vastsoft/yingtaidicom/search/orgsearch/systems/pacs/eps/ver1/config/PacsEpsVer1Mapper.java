package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.config;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Image;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Patient;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Series;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.entity.Study;

public interface PacsEpsVer1Mapper {

	public List<Patient> selectPatientByPtnId(Map<String, Object> mapArg);

	public List<Study> selectStudyByPtn_id_id(int ptn_id_id);

	public List<Series> selectSeriesByStudy_uid_id(int study_uid_id);

//	public List<Image> selectImageBySeries_uid_id(int series_uid_id);

//	public List<Image> selectImageBySeries_uid_idAndSource_ae(Map<String, Object> mapArg);

//	public List<Image> selectImageByInstance_uid(String instance_uid);

	public List<Patient> selectLastPatient(Map<String, Object> mapArg);

	public int selectImageCount(Map<String, Object> mapArg2);

	public List<Image> selectImage(Map<String, Object> mapArg);
	
}
