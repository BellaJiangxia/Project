package com.vastsoft.yingtai.module.sys.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class TemplateType extends SingleClassConstant {
	// TODO 私有、公有
	/** 共有模板 */
	public static final TemplateType PUBLIC = new TemplateType(1, "公有模板");
	/** 私有模板 */
	public static final TemplateType PRIVATE = new TemplateType(2, "私有模板");

	private static Map<Integer, TemplateType> mapTemplateType = new HashMap<Integer, TemplateType>();

	static {
		mapTemplateType.put(PUBLIC.getCode(), PUBLIC);
		mapTemplateType.put(PRIVATE.getCode(), PRIVATE);
	}

	protected TemplateType(int iCode, String strName) {
		super(iCode, strName);
	}

	public static TemplateType parseFrom(int iCode) {
		return mapTemplateType.get(iCode);
	}

	public static List<TemplateType> getAll() {
		return new ArrayList<TemplateType>(mapTemplateType.values());
	}
}
