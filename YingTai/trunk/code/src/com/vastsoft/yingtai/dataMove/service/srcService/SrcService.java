package com.vastsoft.yingtai.dataMove.service.srcService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.vastsoft.util.db.SplitPageUtil;
import com.vastsoft.yingtai.dataMove.entity.TDiagnosis_V1;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHis;
import com.vastsoft.yingtai.dataMove.entity.TMedicalHisImg;
import com.vastsoft.yingtai.dataMove.mapper.srcMapper.SrcMedicalHisMapper;

public class SrcService {
	public static final SrcService instance = new SrcService();
	private SrcService(){}
	
	public List<TMedicalHis> queryMedicalHis(SplitPageUtil spu, SqlSession srcSession) {
		SrcMedicalHisMapper mapper = srcSession.getMapper(SrcMedicalHisMapper.class);
		Map<String, Object> mapArg = new HashMap<>();
		int count = mapper.selectMedicalHisCount();
		spu.setTotalRow(count);
		mapArg.put("minRow", spu.getCurrMinRowNum());
		mapArg.put("maxRow", spu.getCurrMaxRowNum());
		return mapper.selectMedicalHis(mapArg);
	}

	public List<TDiagnosis_V1> queryDiagnosisByMedicalHisId(long medical_his_id, SqlSession srcSession) {
		SrcMedicalHisMapper mapper = srcSession.getMapper(SrcMedicalHisMapper.class);
		return mapper.selectDiagnosisV1ByMedicalHisId(medical_his_id);
	}

	public List<TMedicalHisImg> queryMedicalHisImgByMedicalHisId(long medical_his_id, SqlSession srcSession) {
		SrcMedicalHisMapper mapper = srcSession.getMapper(SrcMedicalHisMapper.class);
		return mapper.selectMedicalHisImgByMedicalHisId(medical_his_id);
	}
//
//	public List<TDiagnosisMsg> queryDiagnosisMsg(SplitPageUtil spu, SqlSession srcSession) {
//		MedicalHisMapper mapper = srcSession.getMapper(MedicalHisMapper.class);
//		Map<String, Object> mapArg = new HashMap<>();
//		int count = mapper.selectDiagnosisMsgCount();
//		spu.setTotalRow(count);
//		mapArg.put("minRow", spu.getCurrMinRowNum());
//		mapArg.put("maxRow", spu.getCurrMaxRowNum());
//		return mapper.selectDiagnosisMsg(mapArg);
//	}
//
//	public List<TDiagnosisShare> queryDiagnosisShare(SplitPageUtil spu, SqlSession srcSession) {
//		MedicalHisMapper mapper = srcSession.getMapper(MedicalHisMapper.class);
//		Map<String, Object> mapArg = new HashMap<>();
//		int count = mapper.selectDiagnosisShareCount();
//		spu.setTotalRow(count);
//		mapArg.put("minRow", spu.getCurrMinRowNum());
//		mapArg.put("maxRow", spu.getCurrMaxRowNum());
//		return mapper.selectDiagnosisShare(mapArg);
//	}
}
