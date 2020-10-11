package com.vastsoft.util.citys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Deprecated
public class PlaceService {
	private HashMap<String, PlaceArea> mapAllPlaceArea = new HashMap<String, PlaceArea>();

	private ArrayList<Country> listCountry = new ArrayList<Country>();

	public PlaceService() {
		try {
			String strFilePath = PlaceService.class.getResource("/").getPath() + "place_area.xml";

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(strFilePath));
			Element elemRoot = doc.getDocumentElement();

			if (elemRoot.getNodeName().equals("config")) {
				NodeList listElems = elemRoot.getElementsByTagName("country");
				for (int i = 0; i < listElems.getLength(); i++) {
					Node node = listElems.item(i);

					String strCode = node.getAttributes().getNamedItem("code").getNodeValue();
					String strName = node.getAttributes().getNamedItem("name").getNodeValue();

					String strPYM = null;
					Node nodePYM = node.getAttributes().getNamedItem("pym");
					if (nodePYM != null)
						strPYM = nodePYM.getNodeValue();

					String strPinYin = null;
					Node nodePinYin = node.getAttributes().getNamedItem("pinyin");
					if (nodePinYin != null)
						strPinYin = nodePinYin.getNodeValue();

					Country country = new Country(strCode, strName, strPYM, strPinYin);
					this.mapAllPlaceArea.put(country.getCode(), country);
					this.listCountry.add(country);

					NodeList listProvinces = node.getChildNodes();
					for (int j = 0; j < listProvinces.getLength(); j++) {
						node = listProvinces.item(j);

						if (node.getNodeType() == Node.ELEMENT_NODE) {
							strCode = node.getAttributes().getNamedItem("code").getNodeValue();
							strName = node.getAttributes().getNamedItem("name").getNodeValue();

							strPYM = null;
							nodePYM = node.getAttributes().getNamedItem("pym");
							if (nodePYM != null)
								strPYM = nodePYM.getNodeValue();

							strPinYin = null;
							nodePinYin = node.getAttributes().getNamedItem("pinyin");
							if (nodePinYin != null)
								strPinYin = nodePinYin.getNodeValue();

							Province province = new Province(strCode, strName, strPYM, strPinYin);
							this.mapAllPlaceArea.put(province.getCode(), province);
							country.addProvince(province);

							NodeList listCitys = node.getChildNodes();
							for (int k = 0; k < listCitys.getLength(); k++) {
								node = listCitys.item(k);

								if (node.getNodeType() == Node.ELEMENT_NODE) {
									strCode = node.getAttributes().getNamedItem("code").getNodeValue();
									strName = node.getAttributes().getNamedItem("name").getNodeValue();

									strPYM = null;
									nodePYM = node.getAttributes().getNamedItem("pym");
									if (nodePYM != null)
										strPYM = nodePYM.getNodeValue();

									strPinYin = null;
									nodePinYin = node.getAttributes().getNamedItem("pinyin");
									if (nodePinYin != null)
										strPinYin = nodePinYin.getNodeValue();

									City city = new City(strCode, strName, strPYM, strPinYin);
									this.mapAllPlaceArea.put(city.getCode(), city);
									province.addCity(city);

									NodeList listDistricts = node.getChildNodes();
									for (int l = 0; l < listDistricts.getLength(); l++) {
										node = listDistricts.item(l);

										if (node.getNodeType() == Node.ELEMENT_NODE) {
											strCode = node.getAttributes().getNamedItem("code").getNodeValue();
											strName = node.getAttributes().getNamedItem("name").getNodeValue();

											strPYM = null;
											nodePYM = node.getAttributes().getNamedItem("pym");
											if (nodePYM != null)
												strPYM = nodePYM.getNodeValue();

											strPinYin = null;
											nodePinYin = node.getAttributes().getNamedItem("pinyin");
											if (nodePinYin != null)
												strPinYin = nodePinYin.getNodeValue();

											District district = new District(strCode, strName, strPYM, strPinYin);
											this.mapAllPlaceArea.put(district.getCode(), district);
											city.addDistrict(district);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addCountry(Country country) {
		this.listCountry.add(country);
	}

	public List<Country> getCountries() {
		return new ArrayList<Country>(this.listCountry);
	}

	public PlaceArea getPlaceAreaByCode(String strCode) {
		return this.mapAllPlaceArea.get(strCode);
	}
}
