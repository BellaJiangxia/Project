package com.vastsoft.yingtai.utils.poi.excel.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;
import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class ExcelSheet {
	private String sheetName;
	private List<ExcelRow> listRow;

	public <T> ExcelSheet(String sheetName, List<T> listObject, String[] fieldNames, Map<Class<?>, DataAdapter> mapAdapter,
			boolean needHead) {
		this.sheetName = sheetName;
		listRow = new ArrayList<ExcelRow>();
		if (needHead) {
			ExcelRow headRow = new ExcelRow(fieldNames);
			this.insertRow(0, headRow);
		}
		for (Object obj : listObject) {
			ExcelRow row = new ExcelRow(obj, fieldNames, mapAdapter);
			this.addRow(row);
		}
	}

	public int addRow(ExcelRow row) {
		listRow.add(row);
		return listRow.size() - 1;
	}

	public int insertRow(int index, ExcelRow row) {
		listRow.add(index, row);
		return index;
	}

	public ExcelRow removeRow(int index) {
		ExcelRow row = listRow.get(index);
		listRow.remove(index);
		return row;
	}

	public ExcelRow getRow(int index) {
		return listRow.get(index);
	}

	public void setCellStyle(int rowIndex, int cellIndex, CellStyleAppler... listAppler) {
		if (rowIndex < 0) {
			for (ExcelRow excelRow : listRow)
				excelRow.setCellStyle(cellIndex, listAppler);
		} else {
			listRow.get(rowIndex).setCellStyle(cellIndex, listAppler);
		}
	}

	public List<ExcelRow> getListRow() {
		return listRow;
	}

	public void setListRow(List<ExcelRow> listRow) {
		this.listRow = listRow;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void installExcel(HSSFWorkbook workBook) {
		HSSFSheet sheet = workBook.createSheet(sheetName);
		if (listRow != null && listRow.size() > 0) {
			for (int i = 0; i < listRow.size(); i++) {
				ExcelRow excelRow = listRow.get(i);
				HSSFRow row = sheet.createRow(i);
				excelRow.installExcel(workBook, row);
			}
		}
	}
}
