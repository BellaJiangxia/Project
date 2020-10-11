package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.database.DataBaseManager;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.yingtai.ver1.YingTaiPacsVer1Executor;

public class YingTaiPacsVersionAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		try {
			String ver_str = element.attributeValue("version");
			AbstractExternalSystemVersion system_version = YingTaiPacsVersion.parseName(ver_str);
			if (system_version == null)
				throw new SearchConfigFileParseException(element, "不支持的PACS系统 EPS厂商的版本：" + ver_str);
			ExternalSystemSearchExecutor ese = null;
			if (system_version.equals(YingTaiPacsVersion.VER_1)) {
				String db_config_id = element.attributeValue("db_config_id");
				SessionFactory factory = DataBaseManager.instance.takeFactoryById(db_config_id,
						system_version.getIbatis_config_path());
				if (factory == null)
					throw new SearchConfigFileParseException(element, "数据连接池未找到 db_config_id：" + db_config_id);
				String org_ae_num = element.attributeValue("org_ae_num");
				if (org_ae_num == null || org_ae_num.trim().isEmpty())
					throw new SearchConfigFileParseException(element, "YingTaiDSS系统AE号必须存在，且不能为空！");
				ese = new YingTaiPacsVer1Executor(new SystemIdentity(system_version), factory, org_ae_num);
			} else {
				throw new SearchConfigFileParseException(element, "PACS系统YingTai厂商不支持的版本："+system_version.toString());
			}
			return ese;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchConfigFileParseException(element, e);
		}
	}

}
