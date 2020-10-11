package com.vastsoft.yingtai.utils.poi.excel.inf;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface CellStyleAppler {
	public void applyCellStyle(Workbook workBook,HSSFCellStyle cellStyle);
}
