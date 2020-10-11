package com.vastsoft.yingtai.utils.poi.excel.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.utils.attributeUtils.ObjectUtils;
import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;
import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class ExcelRow {
	private List<ExcelCell> listCell=new ArrayList<ExcelCell>();

	public ExcelRow(String[] headNames) {
		for (String string : headNames) {
			Entry en=new Entry(string);
			ExcelCell cell=new ExcelCell(en.value);
			listCell.add(cell);
		}
	}

	public ExcelRow(Object obj, String[] fieldNames,Map<Class<?>, DataAdapter> mapAdapter) {
		for (String string : fieldNames) {
			Entry en=new Entry(obj,string,mapAdapter);
			ExcelCell cell=new ExcelCell(en.value);
			listCell.add(cell);
		}
	}
	
	private class Entry{
		private String key;
		private String value;
		private Entry(Object obj,String name,Map<Class<?>, DataAdapter> mapAdapter){
			this(name);
			String[] namess=StringTools.splitString(key, '|');
			String houzui="";
			if (namess.length>=2) {
				key=namess[0];
				houzui=namess[1];
			}
			Object result=ObjectUtils.getValueByPath(obj, key);
			if (result==null){
				value = "";
			}else {
				DataAdapter da=mapAdapter.get(result.getClass());
				if (da==null) {
					value=result.toString()+houzui;
				}else {
					value=da.execAdaptive(result)+houzui;
				}
			}
		}
		
		private Entry(String name){
			if (!name.contains(">")) {
				this.key=name;
				this.value=name;
			}else{
				String [] names=StringTools.splitString(name, '>');
				if (names.length<2) {
					this.key=name;
					this.value=name;
				}else{
					this.key=names[0];
					this.value=names[1];
				}
			}
		}
	}
	

	public int addCell(ExcelCell cell){
		listCell.add(cell);
		return listCell.size()-1;
	}
	
	public int insertCell(int index,ExcelCell cell){
		listCell.add(index, cell);
		return index;
	}
	
	public ExcelCell removeCell(int index){
		ExcelCell cell=listCell.get(index);
		listCell.remove(index);
		return cell;
	}
	
	public ExcelCell getCell(int index){
		return listCell.get(index);
	}

	public void setCellStyle(int cellIndex, CellStyleAppler... listAppler) {
		if (cellIndex<0) {
			for (ExcelCell excelCell : listCell) {
				excelCell.setCellStyle(listAppler);
			}
		}else {
			listCell.get(cellIndex).setCellStyle(listAppler);
		}
	}

	public void installExcel(HSSFWorkbook workBook, HSSFRow row) {
		if (listCell!=null&&listCell.size()>0) {
			for (int i = 0; i < listCell.size(); i++) {
				ExcelCell excelCell=listCell.get(i);
				HSSFCell cell= row.createCell(i);
				excelCell.installExcel(workBook,cell);
			}
		}
	}
}
