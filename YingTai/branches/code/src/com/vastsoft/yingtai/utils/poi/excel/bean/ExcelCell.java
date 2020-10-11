package com.vastsoft.yingtai.utils.poi.excel.bean;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;

public class ExcelCell {
	private String value;
	private List<CellStyleAppler> applers;
	
	public ExcelCell(String value,CellStyleAppler... applers){
		this.value=(value==null?"":value);
		this.applers=CollectionTools.arrayToList(applers);
	}
	public void setCellStyle(CellStyleAppler... listAppler) {
		this.applers=CollectionTools.arrayToList(listAppler);
	}
	public void installExcel(HSSFWorkbook workBook, HSSFCell cell) {
		cell.setCellValue(value);
		if (applers!=null&&applers.size()>0) {
			HSSFCellStyle style=workBook.createCellStyle();
			for (CellStyleAppler al : applers) {
				al.applyCellStyle(workBook, style);
			}
			cell.setCellStyle(style);
		}
	}
	@Override
	public String toString() {
		return "ExcelCell [value=" + value + ", applers=" + applers + "]";
	}
}
