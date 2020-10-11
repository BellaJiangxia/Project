package com.vastsoft.util.citys;

import java.util.ArrayList;
import java.util.List;

import com.vastsoft.util.PYMGenerator;

public class City implements PlaceArea {
	private String strCode;
	private String strName;
	private String strPinYin;
	private String strPYM;

	private ArrayList<District> listDistricts = new ArrayList<District>();

	City(String strCode, String strName, String strPYM, String strPinYin) {
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

	void addDistrict(District district) {
		this.listDistricts.add(district);
	}

	public List<District> getDistricts() {
		return new ArrayList<District>(this.listDistricts);
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
		return PlaceAreaType.CITY;
	}
}