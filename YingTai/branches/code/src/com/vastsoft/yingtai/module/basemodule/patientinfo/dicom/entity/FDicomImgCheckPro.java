package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity;

import java.util.List;

import com.vastsoft.yingtai.module.sys.entity.TDicValue;

public class FDicomImgCheckPro {
	private long id;
	private String name;
	private boolean selected;
	private boolean custom;//是否是自定义
	public FDicomImgCheckPro(TDicValue dv) {
		super();
		this.buildFromDv(dv);
	}
	public FDicomImgCheckPro() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isCustom() {
		return custom;
	}
	public void setCustom(boolean custom) {
		this.custom = custom;
	}
	public String listToString(List<FDicomImgCheckPro> listCheckPro){
		if (listCheckPro==null||listCheckPro.isEmpty())
			return "";
		StringBuilder sbb=new StringBuilder();
		for (FDicomImgCheckPro fDicomImgCheckPro : listCheckPro) {
			if (!fDicomImgCheckPro.selected)
				continue;
			sbb.append(fDicomImgCheckPro.name).append(',');
		}
		return sbb.toString();
	}
//	public static List<FDicomImgCheckPro> buildListByDvs(List<TDicValue> listDv) {
//		if (listDv==null)
//			return null;
//		List<FDicomImgCheckPro> result = new ArrayList<FDicomImgCheckPro>();
//		if (listDv.isEmpty())
//			return result;
//		for (TDicValue dv : listDv) {
//			FDicomImgCheckPro dcp=new FDicomImgCheckPro(dv);
//			result.add(dcp);
//		}
//		return result;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		FDicomImgCheckPro other = (FDicomImgCheckPro) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	private void buildFromDv(TDicValue dv) {
		this.custom = false;
		this.id = dv.getId();
		this.name = dv.getValue();
		this.selected = false;
	}
}
