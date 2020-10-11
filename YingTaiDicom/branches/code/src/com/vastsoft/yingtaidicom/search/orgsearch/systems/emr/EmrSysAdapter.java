package com.vastsoft.yingtaidicom.search.orgsearch.systems.emr;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;

public class EmrSysAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		return null;
//		try {
//			String db_config_id = element.attributeValue("db_config_id");
//			if (StringTools.isEmpty(db_config_id))
//				throw new SearchConfigFileParseException(element, "没有找到数据库配置！");
//			SessionFactory factory = DataBaseManager.instance.takeFactoryById(db_config_id);
//			if (factory == null)
//				throw new SearchConfigFileParseException(element, "没有找到数据库配置 db_config_id：" + db_config_id);
//			return new EmrSysExecutor(new SystemIdentity(null), factory);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//			throw SearchConfigFileParseException.exceptionOf(element, e);
//		}
	}

}
