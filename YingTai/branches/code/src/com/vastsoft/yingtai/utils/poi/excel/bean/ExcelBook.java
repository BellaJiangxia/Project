package com.vastsoft.yingtai.utils.poi.excel.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;


public class ExcelBook {
	private List<ExcelSheet> listSheet;
	private HSSFWorkbook workBook;
	
	public ExcelBook(List<ExcelSheet> listSheet){
		this.listSheet=listSheet;
	}
	
	
	
	public void setCellStyle(int sheetIndex,int rowIndex,int cellIndex,CellStyleAppler... listAppler) {
		if (sheetIndex<0) {
			for (ExcelSheet excelSheet : listSheet) {
				excelSheet.setCellStyle(rowIndex, cellIndex, listAppler);
			}
		}else {
			ExcelSheet excelSheet=listSheet.get(0);
			excelSheet.setCellStyle(rowIndex, cellIndex, listAppler);
		}
	}

	public void createExcelFile(OutputStream out) throws IOException{
		if (workBook==null)
			workBook=new HSSFWorkbook();
		this.installExcel();
		workBook.write(out);
		out.close();
	}
	
	private void installExcel(){
		if (listSheet==null||listSheet.size()<=0)
			throw new NullPointerException("listSheet不可以为空");
		for (ExcelSheet excelSheet : listSheet) {
			excelSheet.installExcel(workBook);
		}
	}
	
	public void createExcelFile(File file) throws IOException{
		if (!file.exists())
			file.createNewFile();
		createExcelFile(new FileOutputStream(file));
	}
	
	public void createExcelFile(List<String> listHeadCell,List<Object> listEntity,String [] attrs,String fileName) throws IOException{
		createExcelFile(new File(fileName));
	}
}
