package com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang;

import org.dom4j.Element;

import com.vastsoft.yingtaidicom.database.DataBaseManager;
import com.vastsoft.yingtaidicom.database.factory.SessionFactory;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemAdapter;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.ExternalSystemSearchExecutor;
import com.vastsoft.yingtaidicom.search.orgsearch.interfase.SystemIdentity;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.constants.ZhibangHisVersion;
import com.vastsoft.yingtaidicom.search.orgsearch.systems.his.brand.Zhibang.ver1.HisZhibangVer1Executor;

public class HisZhibangAdapter implements ExternalSystemAdapter {

	@Override
	public ExternalSystemSearchExecutor parse(Element element) throws SearchConfigFileParseException {
		try {
			String hisZhibangVer = element.attributeValue("version");
			ZhibangHisVersion zhv = ZhibangHisVersion.parseName(hisZhibangVer);
			if (zhv == null)
				throw new SearchConfigFileParseException(element, "不支持的智邦HIS系统版本：" + hisZhibangVer);
			String db_config_id = element.attributeValue("db_config_id");
			SessionFactory sessionFactory = DataBaseManager.instance.takeFactoryById(db_config_id,zhv.getIbatis_config_path());
			ExternalSystemSearchExecutor executor = null;
			if (zhv.equals(ZhibangHisVersion.ZHV_1)) {
				executor = new HisZhibangVer1Executor(new SystemIdentity(zhv), sessionFactory);
			} else {
				throw new SearchConfigFileParseException(element, "HIS系统智邦厂商不支持的版本！");
			}
			return executor;
		} catch (Exception e) {
			throw SearchConfigFileParseException.exceptionOf(element, e);
		}
	}
}
