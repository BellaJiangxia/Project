package com.vastsoft.yingtai.module.org.constants;

import com.vastsoft.util.common.SingleClassConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicomViewerType extends SingleClassConstant {
	public static final DicomViewerType EPS =new DicomViewerType(10, "EPS");
	public static final DicomViewerType WPACS =new DicomViewerType(20, "WPACS");

	private static Map<Integer, DicomViewerType> mapOrgVisible=new HashMap<Integer, DicomViewerType>();

	static{
		mapOrgVisible.put(EPS.getCode(), EPS);
		mapOrgVisible.put(WPACS.getCode(), WPACS);
	}
	protected DicomViewerType(int iCode, String strName) {
		super(iCode, strName);
	}
	public static DicomViewerType parseCode(int iCode){
		return mapOrgVisible.get(iCode);
	}

	public List<DicomViewerType> getAll(){
		return new ArrayList<>(mapOrgVisible.values());
	}
}
