package com.vastsoft.yingtai.module.stat.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class StatFullType extends SingleClassConstant {
	/**通过时间统计*/
	public static final StatFullType TIME_STATTYPE=new StatFullType(1,"通过时间统计");
	/**通过人员统计*/
	public static final StatFullType USER_STATTYPE=new StatFullType(2, "通过人员统计");
	/**通过机构统计*/
	public static final StatFullType ORG_STATTYPE=new StatFullType(3, "通过机构统计");
	
	private static Map<Integer, StatFullType> mapStatFullType=new HashMap<Integer,StatFullType>();
	static{
		mapStatFullType.put(TIME_STATTYPE.getCode(), TIME_STATTYPE);
		mapStatFullType.put(USER_STATTYPE.getCode(), USER_STATTYPE);
		mapStatFullType.put(ORG_STATTYPE.getCode(), ORG_STATTYPE);
	}
	protected StatFullType(int iCode, String strName) {
		super(iCode, strName);
	}
	
	public static StatFullType parseCode(int iCode) {
		return mapStatFullType.get(iCode);
	}

	public static List<StatFullType> getAll() {
		return new ArrayList<StatFullType>(mapStatFullType.values());
	}
}
