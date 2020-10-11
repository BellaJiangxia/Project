package com.vastsoft.yingtai.utils.poi.excel.constants;

import com.vastsoft.util.common.SingleClassConstant;
import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;

public class CellStyleType extends SingleClassConstant {
	private CellStyleAppler appler;
	/**内容样式*/
	public static final CellStyleType TEXT_STYLE =new CellStyleType(10, "内容样式",new TextStyleAppler());
	/**表头样式*/
	public static final CellStyleType HEAD_STYLE =new CellStyleType(20, "表头样式",new HeadStyleAppler());
	
	protected CellStyleType(int iCode, String strName,CellStyleAppler appler) {
		super(iCode, strName);
		this.appler=appler;
	}
	public CellStyleAppler getAppler() {
		return appler;
	}
}
