package com.vastsoft.yingtaidicom.search.exception;

import org.dom4j.Element;

import com.vastsoft.util.exception.BaseException;

/** 搜索配置文件解析异常 */
public class SearchConfigFileParseException extends BaseException {
	private static final long serialVersionUID = -3573138655275347755L;

	private Element element;

	/** 搜索配置文件解析异常 */
	public SearchConfigFileParseException(Element e, String strMessage) {
		super(strMessage);
		this.element = e;
	}

	public SearchConfigFileParseException(Element element2, Exception e) {
		this(element2, e.toString());
	}

	public static SearchConfigFileParseException exceptionOf(Element element, Exception e) {
		SearchConfigFileParseException result;
		if (e instanceof SearchConfigFileParseException) {
			result = (SearchConfigFileParseException) e;
			return result;
		}
		e.printStackTrace();
		result = new SearchConfigFileParseException(element, e.getClass().getName() + "：" + e.getMessage());
		return result;
	}

	@Override
	public String toString() {
		return super.toString() + (element == null ? "" : (" [元素]：" + element.asXML()));
	}

	@Override
	public int getCode() {
		return 2130;
	}

}
