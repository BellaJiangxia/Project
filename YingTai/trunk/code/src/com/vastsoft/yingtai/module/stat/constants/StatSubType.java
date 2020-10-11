package com.vastsoft.yingtai.module.stat.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.common.SingleClassConstant;

public class StatSubType extends SingleClassConstant {
	private StatFullType fullType;
	/**通过年份统计*/
	public static final StatSubType YEAR_SUBTYPE=new StatSubType(StatFullType.TIME_STATTYPE, 1, "通过年份统计");
	/**通过月份统计*/
	public static final StatSubType MONTH_SUBTYPE=new StatSubType(StatFullType.TIME_STATTYPE, 2, "通过月份统计");
	/**通过日期统计*/
	public static final StatSubType DAY_SUBTYPE=new StatSubType(StatFullType.TIME_STATTYPE, 3, "通过日期统计");
	
	private static Map<Integer, StatSubType> mapStatSubType=new HashMap<Integer,StatSubType>();
	static{
		mapStatSubType.put(MONTH_SUBTYPE.getCode(), MONTH_SUBTYPE);
		mapStatSubType.put(YEAR_SUBTYPE.getCode(), YEAR_SUBTYPE);
		mapStatSubType.put(DAY_SUBTYPE.getCode(), DAY_SUBTYPE);
	}
	protected StatSubType(StatFullType fullType,int iCode, String strName) {
		super(iCode, strName);
		this.fullType=fullType;
	}
	
	public StatFullType getFullType(){
		return this.fullType;
	}
	public static StatSubType takeDefaultSubType(){
		return YEAR_SUBTYPE;
	}
	
	public static StatSubType parseCode(int iCode) {
		return mapStatSubType.get(iCode);
	}

	public static List<StatSubType> getAll() {
		return new ArrayList<StatSubType>(mapStatSubType.values());
	}
}
