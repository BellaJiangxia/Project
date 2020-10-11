package com.vastsoft.yingtai.module.qualityControl.entity;

import java.util.Date;
import java.util.List;

import com.vastsoft.util.common.CollectionTools;

/** 质控指标 */
public class TQualityControlMeasure {
	private long id;
	private long form_id;//答卷ID
	private String question;
	private int question_type;
	private long create_user_id;
	private Date create_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
		result = prime * result + (int) (create_user_id ^ (create_user_id >>> 32));
		result = prime * result + (int) (form_id ^ (form_id >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + question_type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TQualityControlMeasure other = (TQualityControlMeasure) obj;
		if (create_time == null) {
			if (other.create_time != null)
				return false;
		} else if (!create_time.equals(other.create_time))
			return false;
		if (create_user_id != other.create_user_id)
			return false;
		if (form_id != other.form_id)
			return false;
		if (id != other.id)
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (question_type != other.question_type)
			return false;
		return true;
	}

	public long getForm_id() {
		return form_id;
	}

	public void setForm_id(long form_id) {
		this.form_id = form_id;
	}

	public static TQualityControlMeasure findByIdFromList(List<TQualityControlMeasure> listMeasure,long id){
		if (CollectionTools.isEmpty(listMeasure))
			return null;
		for (TQualityControlMeasure tQualityControlMeasure : listMeasure) {
			if (tQualityControlMeasure.getId()==id)
				return tQualityControlMeasure;
		}
		return null;
	}

	public int getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(int question_type) {
		this.question_type = question_type;
	}
}
