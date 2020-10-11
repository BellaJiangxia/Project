package com.vastsoft.yingtaidicom.search.orgsearch.interfase;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;

public interface ExternalSystemAdapter {

	ExternalSystemSearchExecutor parse(Element element)throws SearchConfigFileParseException;

}
