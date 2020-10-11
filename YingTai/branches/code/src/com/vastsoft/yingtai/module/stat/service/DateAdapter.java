package com.vastsoft.yingtai.module.stat.service;

import java.util.Date;

import com.vastsoft.util.common.DateTools;
import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class DateAdapter implements DataAdapter {

	@Override
	public String execAdaptive(Object obj) {
		Date dt=(Date) obj;
		return DateTools.dateToString(dt);
	}

}
