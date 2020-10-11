package com.vastsoft.util.citys;

public class PlaceAreaType {

	private PlaceAreaType(String strCode, String strName) {
		this.strCode = strCode;
		this.strName = strName;
	}

	private String strCode;
	private String strName;

	public String getCode() {
		return this.strCode;
	}

	public String getName() {
		return this.strName;
	}

	@Override
	public String toString() {
		return this.strName;
	}

	@Override
	public int hashCode() {
		final int prime = 27;
		int result = 1;
		result = prime * result + ((this.strCode == null) ? 0 : this.strCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		PlaceAreaType other = (PlaceAreaType) obj;

		if (this.strCode == null) {
			if (other.strCode != null)
				return false;
		} else if (!this.strCode.equals(other.strCode))
			return false;

		return true;
	}

	/**
	 * 根据代码获得对象
	 * 
	 * @param strCode
	 * @return
	 */
	public static PlaceAreaType parseFrom(String strCode) {
		if (strCode.equals("1"))
			return PlaceAreaType.COUNTRY;
		else if (strCode.equals("2"))
			return PlaceAreaType.PROVINCE;
		else if (strCode.equals("3"))
			return PlaceAreaType.CITY;
		else if (strCode.equals("4"))
			return PlaceAreaType.DISTRICT;
		else
			return null;
	}

	/**
	 * 国家
	 */
	public final static PlaceAreaType COUNTRY = new PlaceAreaType("1", "COUNTRY");

	/**
	 * 省(州)
	 */
	public final static PlaceAreaType PROVINCE = new PlaceAreaType("2", "PROVINCE");

	/**
	 * 市
	 */
	public final static PlaceAreaType CITY = new PlaceAreaType("3", "CITY");

	/**
	 * 区(县)
	 */
	public final static PlaceAreaType DISTRICT = new PlaceAreaType("4", "DISTRICT");
}
