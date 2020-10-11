package com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.database.DataBaseManager;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.constants.AbstractExternalSystemVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.constants.PascEpsVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.pacs.eps.ver1.PacsEpsVer1Excutor;

public class EpsSysAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		try {
			String ver_str = element.attributeValue("version");
			AbstractExternalSystemVersion system_version = PascEpsVersion.parseName(ver_str);
			if (system_version == null)
				throw new SearchConfigFileParseException(element, "不支持的PACS系统 EPS厂商的版本：" + ver_str);
			ExternalSystemSearchExecutor ese = null;
			if (system_version.equals(PascEpsVersion.V_1)) {
				String org_ae_num = element.attributeValue("org_ae_num");
				if (org_ae_num == null || org_ae_num.trim().isEmpty())
					throw new SearchConfigFileParseException(element, "EPS系统AE号必须存在，且不能为空！");
				String thumbnail_directory = element.attributeValue("thumbnail_directory");
				String thumbnail_suffix = element.attributeValue("thumbnail_suffix");
				String db_config_id = element.attributeValue("db_config_id");
				SessionFactory factory = DataBaseManager.instance.takeFactoryById(db_config_id,
						system_version.getIbatis_config_path());
				if (factory == null)
					throw new SearchConfigFileParseException(element, "数据连接池未找到 db_config_id：" + db_config_id);
				ese = new PacsEpsVer1Excutor(new SystemIdentity(system_version), factory, org_ae_num,
						thumbnail_directory, thumbnail_suffix);
			} else {
				throw new SearchConfigFileParseException(element, "PACS系统EPS厂商不支持的版本");
			}
			return ese;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchConfigFileParseException(element, e);
		}
	}

}
