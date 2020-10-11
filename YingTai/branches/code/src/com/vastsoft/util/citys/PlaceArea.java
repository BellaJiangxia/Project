package com.vastsoft.util.citys;

public interface PlaceArea {
	/**
	 * 获取代码
	 * 
	 * @return
	 */
	public String getCode();

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 获取拼音码（拼音首字母）
	 * 
	 * @return
	 */
	public String getPYM();

	/**
	 * 获取拼音（全拼字母）
	 * 
	 * @return
	 */
	public String getPinYin();

	/**
	 * 获取位置区域类型
	 * 
	 * @return
	 */
	public PlaceAreaType getType();
}
