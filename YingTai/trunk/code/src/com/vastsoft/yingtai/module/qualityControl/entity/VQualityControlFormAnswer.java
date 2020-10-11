package com.vastsoft.yingtai.module.qualityControl.entity;

import java.util.ArrayList;
import java.util.List;

public class VQualityControlFormAnswer {
	private TQualityControlFormAnswer qualityControlFormAnswer;
	private List<TQualityControlMeasureAnswer> listMeasureAnswer = new ArrayList<TQualityControlMeasureAnswer>();

	public TQualityControlFormAnswer getQualityControlFormAnswer() {
		return qualityControlFormAnswer;
	}

	public void setQualityControlFormAnswer(TQualityControlFormAnswer qualityControlFormAnswer) {
		this.qualityControlFormAnswer = qualityControlFormAnswer;
	}

	public List<TQualityControlMeasureAnswer> getListMeasureAnswer() {
		return listMeasureAnswer;
	}

	public void setListMeasureAnswer(List<TQualityControlMeasureAnswer> listMeasureAnswer) {
		this.listMeasureAnswer = listMeasureAnswer;
	}
}
