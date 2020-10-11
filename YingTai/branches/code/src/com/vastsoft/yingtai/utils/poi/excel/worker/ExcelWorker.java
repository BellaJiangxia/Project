package com.vastsoft.yingtai.utils.poi.excel.worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.vastsoft.util.common.ArrayTools;
import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.yingtai.utils.poi.excel.bean.ExcelBook;
import com.vastsoft.yingtai.utils.poi.excel.bean.ExcelSheet;
import com.vastsoft.yingtai.utils.poi.excel.constants.CellStyleType;
import com.vastsoft.yingtai.utils.poi.interfase.DataAdapter;

public class ExcelWorker {
	/**生成的文件的最大单元格数量*/
	public static final int MAX_CELL_COUNT = 4000;

	public static void createExcel(List<?> listObject, String[] fieldNames,Map<Class<?>, DataAdapter> mapAdapter, OutputStream out) throws IOException {
		if (CollectionTools.isEmpty(listObject))
			return;
		while (wasExceedVolume(listObject.size(), ArrayTools.isEmpty(fieldNames)?0:fieldNames.length))
			listObject.remove(listObject.size()-1);
			//throw new RuntimeException("错误：可以生成的excel文件的最大单元格数量不能超过"+MAX_CELL_COUNT+"个！");
		List<ExcelSheet> listExcelSheet = new ArrayList<ExcelSheet>();
		ExcelSheet heading = new ExcelSheet("内容", listObject, fieldNames,mapAdapter, true);
		listExcelSheet.add(heading);
		ExcelBook book = new ExcelBook(listExcelSheet);
		book.setCellStyle(-1, -1, -1, CellStyleType.TEXT_STYLE.getAppler());
		book.setCellStyle(0, 0, -1, CellStyleType.HEAD_STYLE.getAppler());
		book.createExcelFile(out);
	}
	
	/**判断是否超出容量*/
	public static boolean wasExceedVolume(int rowCount,int columnCount){
		return (rowCount*columnCount)>=4000;
	}

	public static void createExcel(List<T> listObject, String[] fieldNames,Map<Class<?>, DataAdapter> mapAdapter, File file) throws IOException {
		if (CollectionTools.isEmpty(listObject))
			return;
		if (!file.exists())
			file.createNewFile();
		createExcel(listObject, fieldNames,mapAdapter, new FileOutputStream(file));
	}

	public static void createExcel(List<T> listObject, String[] fieldNames,Map<Class<?>, DataAdapter> mapAdapter, String fileName) throws IOException {
		if (CollectionTools.isEmpty(listObject))
			return;
		createExcel(listObject, fieldNames,mapAdapter, new File(fileName));
	}
}
