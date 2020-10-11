package com.vastsoft.yingtai.module.qualityControl.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.qualityControl.entity.FQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlForm;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlFormAnswer;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlMeasure;
import com.vastsoft.yingtai.module.qualityControl.entity.TQualityControlMeasureAnswer;

public interface QualityControlMapper {
	TQualityControlFormAnswer selectQualityControlFormAnswerByFormIdAndUserIdAndOrgId(
			TQualityControlFormAnswer tQualityControlFormAnswer);

	TQualityControlMeasureAnswer selectQualityControlMeasureAnswerByFormAnswerIdAndMeasureId(
			TQualityControlMeasureAnswer tQualityControlMeasureAnswer);

	int selectQualityControlFormCount(Map<String, Object> mapArg);

	List<FQualityControlForm> selectQualityControlForm(Map<String, Object> mapArg);

	void insertQualityControlForm(TQualityControlForm controlForm);

	void insertQualityControlMeasure(TQualityControlMeasure qualityControlMeasure);

	int selectQualityControlMeasureCount(Map<String, Object> mapArg);

	List<TQualityControlMeasure> selectQualityControlMeasure(Map<String, Object> mapArg);

	TQualityControlForm selectQualityControlFormByIdForUpdate(long id);

	void updateQualityControlForm(TQualityControlForm oldQualityControlForm);

	void deleteQualityControlMeasureByFormId(long form_id);

	void deleteQualityControlFormById(long id);

	int selectQualityControlFormAnswerCount(Map<String, Object> mapArg);

	List<TQualityControlFormAnswer> selectQualityControlFormAnswer(Map<String, Object> mapArg);

	void insertQualityControlFormAnswer(TQualityControlFormAnswer qualityControlFormAnswer);

	void insertQualityControlMeasureAnswer(TQualityControlMeasureAnswer qualityControlMeasureAnswer);

	TQualityControlFormAnswer selectQualityControlFormAnswerById(long id);

	List<TQualityControlMeasureAnswer> selectQualityControlMeasureAnswerByFormId(long form_answer_id);

	TQualityControlFormAnswer selectQualityControlFormAnswerByIdForUpdate(long id);

	void updateQualityControlFormAnswer(TQualityControlFormAnswer oldQualityControlFormAnswer);

	void updateQualityControlMeasureAnswer(TQualityControlMeasureAnswer oldQcma);

}
