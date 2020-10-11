package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity;

import java.util.ArrayList;
import java.util.List;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.module.sys.entity.TDicValue;

public class FDicomImg extends TDicomImg {
	private String device_type_name;
	
	private List<FSeries> listSeries;
	private List<FDicomImgCheckPro> listCheckPro;
	private List<TDicValue> listBodyPartType;
	
	public FDicomImg() {
		super();
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public List<FDicomImgCheckPro> getListCheckPro() {
		return listCheckPro;
	}
	public void setListCheckPro(List<FDicomImgCheckPro> listCheckPro) {
		this.listCheckPro = listCheckPro;
		if (listCheckPro==null||listCheckPro.isEmpty())
			return;
		StringBuilder sbb = new StringBuilder();
		for (FDicomImgCheckPro fDicomImgCheckPro : listCheckPro) {
			if (!fDicomImgCheckPro.isSelected())
				continue;
			sbb.append(fDicomImgCheckPro.getName()).append(',');
		}
		this.setCheck_pro(sbb.toString());
	}
	
	public void buildListCheckProByDicValues(List<TDicValue> listCheckProDic) {
		this.listCheckPro =new ArrayList<FDicomImgCheckPro>();
		List<String> checkProNames = StringTools.splitStrAsList(this.getCheck_pro(), ',');
		if (checkProNames!=null&&!checkProNames.isEmpty()){
			for (String checkProName : checkProNames) {
				FDicomImgCheckPro dicp = new FDicomImgCheckPro();
				dicp.setSelected(true);
				TDicValue dcv = TDicValue.existByValue(listCheckProDic,checkProName);
				if (dcv!=null) {
					dicp.setCustom(false);
					dicp.setId(dcv.getId());
					dicp.setName(dcv.getValue());
				}else {
					dicp.setCustom(true);
					dicp.setId(0);
					dicp.setName(checkProName);
				}
				this.listCheckPro.add(dicp);
			}
		}
		if (listCheckProDic==null||listCheckProDic.isEmpty())
			return ;
		for (TDicValue dv : listCheckProDic) {
			FDicomImgCheckPro dicp = new FDicomImgCheckPro(dv);
			dicp.setSelected(false);
			if(! this.listCheckPro.contains(dicp))
				this.listCheckPro.add(dicp);
		}
	}
	public List<FSeries> getListSeries() {
		return listSeries;
	}
	public void setListSeries(List<FSeries> listSeries) {
		this.listSeries = listSeries;
		super.organize(this.listSeries);
	}
	public List<TDicValue> getListBodyPartType() {
		return listBodyPartType;
	}
	public void setListBodyPartType(List<TDicValue> listBodyPartType) {
		this.listBodyPartType = listBodyPartType;
	}
}
