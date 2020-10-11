package com.vastsoft.yingtai.module.basemodule.patientinfo.dicom.entity;

import java.util.List;

import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist.ShareRemoteNumEntry;
import com.vastsoft.yingtai.module.basemodule.patientinfo.remote.constants.ShareRemoteParamsType;

public class FDicomImgNumObj {
	private int code;
	private String name;
	private List<ShareRemoteNumEntry> listDicomImgNum;
	
	public FDicomImgNumObj() {
		super();
	}
	public FDicomImgNumObj(ShareRemoteParamsType type, List<ShareRemoteNumEntry> listDicomImgNum) {
		super();
		this.code = type.getCode();
		this.name = type.getName();
		this.listDicomImgNum = listDicomImgNum;
	}
	public FDicomImgNumObj(int code, String name, List<ShareRemoteNumEntry> listDicomImgNum) {
		super();
		this.code = code;
		this.name = name;
		this.listDicomImgNum = listDicomImgNum;
	}
	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public List<ShareRemoteNumEntry> getListDicomImgNum() {
		return listDicomImgNum;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setListDicomImgNum(List<ShareRemoteNumEntry> listDicomImgNum) {
		this.listDicomImgNum = listDicomImgNum;
	}
	
}
