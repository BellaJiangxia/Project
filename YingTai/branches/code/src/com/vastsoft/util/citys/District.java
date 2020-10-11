package com.vastsoft.util.citys;

import com.vastsoft.util.PYMGenerator;

public class District implements PlaceArea {
	private String strCode;
	private String strName;
	private String strPinYin;
	private String strPYM;

	District(String strCode, String strName, String strPYM, String strPinYin) {
		this.strCode = strCode;
		this.strName = strName;

		if (strPYM != null)
			if (!strPYM.isEmpty())
				this.strPYM = strPYM.toUpperCase();

		if (strPinYin != null)
			if (!strPinYin.isEmpty())
				this.strPinYin = strPinYin.toUpperCase();

		if (this.strPinYin == null)
			this.strPinYin = PYMGenerator.getPinYin(strName).toUpperCase();

		if (this.strPYM == null)
			this.strPYM = PYMGenerator.getPinYinHeadChar(strName).toUpperCase();
	}

	@Override
	public String getCode() {
		return this.strCode;
	}

	@Override
	public String getName() {
		return this.strName;
	}

	@Override
	public String getPYM() {
		return this.strPYM;
	}

	@Override
	public String getPinYin() {
		return this.strPinYin;
	}

	@Override
	public PlaceAreaType getType() {
		return PlaceAreaType.DISTRICT;
	}
}
