package com.vastsoft.yingtai.module.qualityControl.entity;

import java.util.List;

import com.vastsoft.util.common.CollectionTools;

public class TQualityControlMeasureAnswer {
	private long id;
	private long form_answer_id;
	private long measure_id;
	private String question;
	private int question_type;
	private String answer;
	private int percent;

	public TQualityControlMeasureAnswer() {
		super();
	}

	public TQualityControlMeasureAnswer(long form_answer_id, long measure_id) {
		super();
		this.form_answer_id = form_answer_id;
		this.measure_id = measure_id;
	}

	public int getScore() {
		return getPercent()*5 / 100 ;
	}

	// public void setScore(int score) {
	// this.percent = score/30*100;
	// if (percent<0)
	// percent = 0;
	// if (percent>100)
	// percent = 100;
	// }
	public long getMeasure_id() {
		return measure_id;
	}

	public void setMeasure_id(long measure_id) {
		this.measure_id = measure_id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getForm_answer_id() {
		return form_answer_id;
	}

	public void setForm_answer_id(long form_answer_id) {
		this.form_answer_id = form_answer_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(int question_type) {
		this.question_type = question_type;
	}

	public int getPercent() {
		if (percent < 0)
			percent = 0;
		if (percent > 100)
			percent = 100;
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public static TQualityControlMeasureAnswer findByIdFromList(List<TQualityControlMeasureAnswer> listQcmaTmp,
			long id) {
		if (CollectionTools.isEmpty(listQcmaTmp))
			return null;
		for (TQualityControlMeasureAnswer tQualityControlMeasureAnswer : listQcmaTmp) {
			if (tQualityControlMeasureAnswer.getId() == id)
				return tQualityControlMeasureAnswer;
		}
		return null;
	}

}
