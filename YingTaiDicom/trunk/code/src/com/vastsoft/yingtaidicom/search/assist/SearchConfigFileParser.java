package com.vastsoft.yingtaidicom.search.assist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.vastsoft.util.common.SystemTools;
import com.vastsoft.yingtaidicom.database.DataBaseManager;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.OrgSearchController;

public class SearchConfigFileParser {
	private Element root;

	public SearchConfigFileParser(String config_file) throws SearchConfigFileParseException {
		try {
			this.root = new SAXReader().read(SearchConfigFileParser.class.getClassLoader().getResourceAsStream(SystemTools.getFileSeparator() + config_file))
					.getRootElement();
		} catch (DocumentException e) {
			throw SearchConfigFileParseException.exceptionOf(root, e);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, OrgSearchController> parse() throws SearchConfigFileParseException {
		try {
			Map<String, OrgSearchController> result = new HashMap<String, OrgSearchController>();
			Element config_files = root.element("config_files");
			if (config_files != null) {
				List<Element> listConfigFiles = config_files.elements("file");
				if (listConfigFiles != null && listConfigFiles.size() > 0) {
					for (Element element : listConfigFiles) {
						String config_file = element.attributeValue("src");
						SearchConfigFileParser scfp = new SearchConfigFileParser(config_file.trim());
						Map<String, OrgSearchController> mapTmp = scfp.parse();
						if (mapTmp != null)
							result.putAll(mapTmp);
					}
				}
			}
			Element db_configs = root.element("db_configs");
			if (db_configs == null)
				throw new SearchConfigFileParseException(root, "没有数据库的相关配置！");
			DataBaseManager.instance.init(db_configs);

			Element org_configs = root.element("org_configs");
			if (org_configs == null)
				throw new SearchConfigFileParseException(root, "解析失败,没有找到机构配置！");
			List<Element> listOrgConfig = org_configs.elements("org_config");
			if (listOrgConfig == null || listOrgConfig.size() <= 0)
				throw new SearchConfigFileParseException(org_configs, "解析失败,机构配置数量为0！");
			for (Element element : listOrgConfig) {
				OrgSearchController orgCtrl = new OrgSearchController(element);
				result.put(orgCtrl.getOrg_code(), orgCtrl);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchConfigFileParseException(root, e.getMessage());
		}
	}
}
