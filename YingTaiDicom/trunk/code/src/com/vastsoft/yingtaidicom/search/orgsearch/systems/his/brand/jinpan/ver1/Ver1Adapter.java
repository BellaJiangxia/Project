package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.jinpan.ver1;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;

/** 金盘his版本1适配器 */
public class Ver1Adapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		return null;
//		String db_config_id = element.attributeValue("db_config_id");
//		if (StringTools.isEmpty(db_config_id))
//			throw new SearchConfigFileParseException(element, "没有找到数据库配置！");
//		SessionFactory factory;
//		try {
//			factory = DataBaseManager.instance.takeFactoryById(db_config_id, ibatis_config_path);
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//			throw new SearchConfigFileParseException(element, e);
//		}
//		if (factory == null)
//			throw new SearchConfigFileParseException(element, "没有找到数据库配置 db_config_id：" + db_config_id);
//		return new JinpanVer1Executor(new SystemIdentity(null), factory);
	}

}
