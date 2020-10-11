package com.vastsoft.yingtai.module.user.constants;

import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class GenderAdapter implements DataAdapter {

	@Override
	public String execAdaptive(Object obj) {
		Gender gender = (Gender) obj;
		return gender.getName();
	}

}
