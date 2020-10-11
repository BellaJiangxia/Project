package com.vastsoft.yingtaidicom.search.orgsearch.systems.his;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.constants.HisSystemBrand;

public class HisSysAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		try {
			String his_sys_brand_name = element.attributeValue("brand");
			HisSystemBrand hsb = HisSystemBrand.parseName(his_sys_brand_name);
			if (hsb == null)
				throw new SearchConfigFileParseException(element, "不支持的HIS系统品牌:" + his_sys_brand_name);
			return hsb.getAdapter().parse(element);
		} catch (Exception e) {
			throw SearchConfigFileParseException.exceptionOf(element, e);
		}
	}
}
