package com.vastsoft.yingtai.utils.poi.excel.constants;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Workbook;

import com.vastsoft.yingtai.utils.poi.excel.inf.CellStyleAppler;

public class TextStyleAppler implements CellStyleAppler {

	@Override
	public void applyCellStyle(Workbook workBook, HSSFCellStyle cellStyle) {
		HSSFFont font=(HSSFFont) workBook.createFont();
		font.setFontHeightInPoints((short) 12); // 字体高度
		font.setFontName("宋体"); // 字体
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
		cellStyle.setFont(font);
	}

}
