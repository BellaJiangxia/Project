package com.vastsoft.yingtai.dataMove.mapper.srcMapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.dataMove.entity.TDiagnosis_V1;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHis;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHisImg;

public interface SrcMedicalHisMapper {

	int selectMedicalHisCount();

	List<TMedicalHis> selectMedicalHis(Map<String, Object> mapArg);

	List<TDiagnosis_V1> selectDiagnosisV1ByMedicalHisId(long medical_his_id);

	List<TMedicalHisImg> selectMedicalHisImgByMedicalHisId(long medical_his_id);
}
