package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.constants.PacsSystenBrand;

public class PacsSysAdapter implements ExternalSystemAdapter {
	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		try {
			String pacs_brand = element.attributeValue("brand");
			PacsSystenBrand brand = PacsSystenBrand.parseName(pacs_brand);
			if (brand == null)
				throw new SearchConfigFileParseException(element, "不支持的PACS系统的品牌：" + String.valueOf(pacs_brand));
			return brand.getAdapter().parse(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchConfigFileParseException(element, e);
		}
	}

}
