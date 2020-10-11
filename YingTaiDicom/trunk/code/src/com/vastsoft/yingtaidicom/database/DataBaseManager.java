package com.vastsoft.yingtaidicom.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Element;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtaidicom.database.constants.DBServerType;
import com.vastsoft.yingtaidicom.database.exception.DataBaseException;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;

public class DataBaseManager {
	public static final DataBaseManager instance = new DataBaseManager();
	private Map<String, Properties> mapProperties = new HashMap<String, Properties>();

	private DataBaseManager() {
		// List<DBServerType> dbsts = DBServerType.getAll();
		// for (DBServerType dbServerType : dbsts) {
		// try {
		// Class.forName(dbServerType.getDriver());
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public SessionFactory takeFactoryById(String db_config_id, String ibatis_config_path) throws DataBaseException {
		return new SessionFactory(mapProperties.get(db_config_id), ibatis_config_path);
	}

	public void init(Element element) throws SearchConfigFileParseException {
		@SuppressWarnings("unchecked")
		List<Element> listDbConfig = element.elements("db-config");
		if (listDbConfig == null || listDbConfig.isEmpty())
			throw new SearchConfigFileParseException(element, "数据库配置数量为0");
		for (Element ele : listDbConfig) {
			String id = ele.attributeValue("id");
			if (StringTools.isEmpty(id))
				throw new SearchConfigFileParseException(ele, "数据库配置ID不能为空");
			if (mapProperties.get(id) != null)
				throw new SearchConfigFileParseException(ele, "数据库配置的ID重复！");
			Properties dpro = new Properties();
			@SuppressWarnings("unchecked")
			List<Element> listElePro = ele.elements("property");
			if (listElePro == null || listElePro.isEmpty())
				throw new SearchConfigFileParseException(ele, "数据库属性配置数量为0");
			for (Element elePro : listElePro) {
				String proName = elePro.attributeValue("name");
				if (StringTools.isEmpty(proName))
					throw new SearchConfigFileParseException(elePro, "数据库属性名为空！");
				String value = elePro.attributeValue("value");
				if ("url".equals(proName))
					dpro.setProperty("url", value);
				if ("username".equals(proName))
					dpro.setProperty("username", value);
				if ("password".equals(proName))
					dpro.setProperty("password", value);
				if ("db_type".equals(proName)) {
					DBServerType dbServerType = DBServerType.parseName(value);
					if (dbServerType == null)
						throw new SearchConfigFileParseException(elePro, "数据库类型无法识别！");
					dpro.setProperty("driver", dbServerType.getDriver());
				}
			}
			mapProperties.put(id, dpro);
		}
	}
}
