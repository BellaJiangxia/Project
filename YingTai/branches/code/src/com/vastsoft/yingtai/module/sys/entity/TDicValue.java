package com.vastsoft.yingtai.module.sys.entity;

import java.util.List;

import com.vastsoft.yingtai.core.EntityBaseInf;
import com.vastsoft.yingtai.module.sys.constants.DictionaryFlag;

public class TDicValue implements EntityBaseInf,Comparable<TDicValue>{
	private long id; 
	private int dic_type; //词典类型
	private long father_dic_id;//父词典id
	private String value; //值
//	private String name;//名称
	private int flag; //权限级别
	private String description; //描述
	private String unit;//单位
	
	public TDicValue(int dic_type, long father_dic_id) {
		super();
		this.dic_type = dic_type;
		this.father_dic_id = father_dic_id;
	}

	public TDicValue() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDic_type() {
		return dic_type;
	}

	public void setDic_type(int dic_type) {
		this.dic_type = dic_type;
	}

	public long getFather_dic_id() {
		return father_dic_id;
	}

	public void setFather_dic_id(long father_dic_id) {
		this.father_dic_id = father_dic_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFlagStr() {
		DictionaryFlag f = DictionaryFlag.parseCode(flag);
		return f==null?"":f.getName();
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public void copyTo(EntityBaseInf entity) {
		if (entity==null)return;
		if (!(entity instanceof TDicValue))return;
		TDicValue oo=(TDicValue)entity;
		oo.setDescription(description);
		oo.setDic_type(dic_type);
		oo.setFather_dic_id(father_dic_id);
		oo.setFlag(flag);
		oo.setId(id);
		oo.setUnit(unit);
		oo.setValue(value);
	}

	@Override
	public int compareTo(TDicValue otherDv) {
		return (this.id - otherDv.id)>0?1:-1;
	}

	public static TDicValue existByValue(List<TDicValue> listDv, String value) {
		if (listDv==null||listDv.isEmpty())
			return null;
		if (value==null||value.trim().isEmpty())
			return null;
		value = value.trim();
		for (TDicValue tDicValue : listDv) {
			if (value.equals(tDicValue.getValue()))
				return tDicValue;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		TDicValue other = (TDicValue) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
