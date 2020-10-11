package com.vastsoft.yingtaidicom.search.orgsearch.systems.ris;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;

public class RisSysAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		return new RisSysExecutor(null, null);
	}

}
