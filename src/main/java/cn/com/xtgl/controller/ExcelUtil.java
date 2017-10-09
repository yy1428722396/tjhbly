package cn.com.xtgl.controller;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

	public static Workbook createCallCaseTable(List caseinfo, String sheetname) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(sheetname);
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。

		// 创建两种单元格格式
		CellStyle cs = wb.createCellStyle();
		CellStyle cs2 = wb.createCellStyle();

		// 创建两种字体
		Font f = (Font) wb.createFont();
		Font f2 = (Font) wb.createFont();

		// 创建第一种字体样式（用于列名）
		f.setFontHeightInPoints((short) 24);
		f.setFontName("黑体");
		f.setColor(IndexedColors.BLACK.getIndex());
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// 创建第二种字体样式（用于值）
		f2.setFontHeightInPoints((short) 14);
		f2.setFontName("楷体_GB2312");
		f2.setColor(IndexedColors.BLACK.getIndex());

		// Font f3=wb.createFont();
		// f3.setFontHeightInPoints((short) 10);
		// f3.setColor(IndexedColors.RED.getIndex());

		// 设置第一种单元格的样式（用于列名）
		cs.setFont(f);

		cs.setAlignment(CellStyle.ALIGN_CENTER);

		// 设置第二种单元格的样式（用于值）
		cs2.setFont(f2);
		cs2.setAlignment(CellStyle.ALIGN_LEFT);

		// 创建第一行
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));

		Row row0 = (Row) sheet.createRow((short) 0);
		row0.createCell(0).setCellValue("业务员名称");
		row0.createCell(1).setCellValue("累计核销积分");
		row0.createCell(2).setCellValue("累计结算金额");
		row0.createCell(3).setCellValue("本次应结金额");
		row0.createCell(4).setCellValue("联系电话");
		row0.createCell(5).setCellValue("结算日期");
		row0.createCell(6).setCellValue("备注");

		row0.setRowStyle(cs2);
		if (caseinfo != null && caseinfo.size() > 0) {
			Map oneValue = (Map) caseinfo.get(0);
			int propNum = oneValue.size();
			for (int i = 0, l = caseinfo.size(); i < l; i++) {
				Row row = (Row) sheet.createRow(i + 2);
				Map record = (Map) caseinfo.get(i);
				for (int j = 0; j < propNum; j++) {
					sheet.addMergedRegion(new CellRangeAddress(i + 2, i + 2, j,
							j));
					sheet.setColumnWidth((short) j, (short) (30 * 200));
				}

				if (record.get("SalesmanName") != null)
					row.createCell(0).setCellValue(
							record.get("SalesmanName").toString());
				if (record.get("TotalIntegral") != null)
					row.createCell(1).setCellValue(
							record.get("TotalIntegral").toString());
				if (record.get("TotalMoney") != null)
					row.createCell(2).setCellValue(
							record.get("TotalMoney").toString());
				if (record.get("ThisMoney") != null)
					row.createCell(3).setCellValue(
							record.get("ThisMoney").toString());
				if (record.get("Telephone") != null)
					row.createCell(4).setCellValue(
							record.get("Telephone").toString());
				if (record.get("CreateDate") != null)
					row.createCell(5).setCellValue(
							record.get("CreateDate").toString());
				if (record.get("ExplainInfo") != null)
					row.createCell(6).setCellValue(
							record.get("ExplainInfo").toString());
			}
		}

		return wb;
	}

	public static Workbook createMRTightTable(List mrlist, String sheetname) {
		// 创建excel工作簿
		Workbook wb = new HSSFWorkbook();
		// 创建第一个sheet（页），并命名
		Sheet sheet = wb.createSheet(sheetname);
		// 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
		sheet.setColumnWidth((short) 0, (short) (30 * 130));
		sheet.setColumnWidth((short) 1, (short) (30 * 130));
		sheet.setColumnWidth((short) 2, (short) (60 * 130));
		sheet.setColumnWidth((short) 3, (short) (60 * 130));
		sheet.setColumnWidth((short) 4, (short) (50 * 130));
		sheet.setColumnWidth((short) 5, (short) (30 * 130));
		sheet.setColumnWidth((short) 6, (short) (30 * 130));
		sheet.setColumnWidth((short) 14, (short) (30 * 130));

		for (int i = 7; i < 14; i++) {
			sheet.setColumnWidth((short) i, (short) (50 * 130));
		}
		sheet.setColumnWidth((short) 2, (short) (60 * 130));
		if (mrlist != null && mrlist.size() > 0) {
			Map oneValue = (Map) mrlist.get(0);
			int propNum = oneValue.size();
			// 创建两种单元格格式
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();

			// 创建两种字体
			Font f = (Font) wb.createFont();
			Font f2 = (Font) wb.createFont();

			// 创建第一种字体样式（用于表名）
			f.setFontHeightInPoints((short) 20);
			f.setFontName("黑体");
			f.setColor(IndexedColors.BLACK.getIndex());
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 创建第二种字体样式（用于列名）
			f2.setFontHeightInPoints((short) 14);
			f2.setFontName("楷体_GB2312");
			f2.setColor(IndexedColors.BLACK.getIndex());
			f2.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 设置第一种单元格的样式（用于列名）
			cs.setFont(f);
			cs.setAlignment(CellStyle.ALIGN_CENTER);

			// 设置第二种单元格的样式（用于值）
			cs2.setFont(f2);
			cs2.setAlignment(CellStyle.ALIGN_CENTER);

			// 创建 表头
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
			Row row0 = (Row) sheet.createRow((short) 0);
			Cell row0cell = row0.createCell(0);
			row0cell.setCellValue("民警维权查询结果");
			row0cell.setCellStyle(cs);
			Row row1 = (Row) sheet.createRow((short) 1);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 10));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 11, 11));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 12));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 14, 14));
			row1.createCell(0).setCellValue("填表日期");
			row1.createCell(1).setCellValue("填报人");
			row1.createCell(2).setCellValue("案（事）件简要经过");
			row1.createCell(3).setCellValue("记录状态");
			row1.createCell(4).setCellValue("侵害人员处理情况");
			row1.createCell(5).setCellValue("慰问情况");
			row1.createCell(6).setCellValue("案件编号");
			row1.createCell(7).setCellValue("案（事）件来源");
			row1.createCell(8).setCellValue("案发单位");
			row1.createCell(9).setCellValue("所属部门");
			row1.createCell(10).setCellValue("执法情景");
			row1.createCell(11).setCellValue("案件类别");
			row1.createCell(12).setCellValue("发生时间");
			row1.createCell(13).setCellValue("是否重点维权案件");
			row1.createCell(14).setCellValue("维权正名");
			for (int i = 0; i < 15; i++) {
				row1.getCell(i).setCellStyle(cs2);
			}

			// 创建表格数据
			for (int i = 0, l = mrlist.size(); i < l; i++) {
				Row row = (Row) sheet.createRow(i + 2);
				Map record = (Map) mrlist.get(i);
				for (int j = 0; j < propNum; j++) {
					sheet.addMergedRegion(new CellRangeAddress(i + 2, i + 2, j,
							j));
					// sheet.setColumnWidth((short) j, (short) (50 * 130));
				}

				// row.createCell(0).setCellValue(record.get("id").toString());
				if (record.get("createdate") != null)
					row.createCell(0).setCellValue(
							record.get("createdate").toString());
				if (record.get("realname") != null)
					row.createCell(1).setCellValue(
							record.get("realname").toString());
				if (record.get("detail") != null)
					row.createCell(2).setCellValue(
							record.get("detail").toString());
				if (record.get("status") != null)
					row.createCell(3).setCellValue(
							record.get("status").toString());
				if (record.get("dealinfo") != null)
					row.createCell(4).setCellValue(
							record.get("dealinfo").toString());
				if (record.get("ww") != null)
					row.createCell(5).setCellValue(record.get("ww").toString());
				if (record.get("orderid") != null)
					row.createCell(6).setCellValue(
							record.get("orderid").toString());
				if (record.get("source") != null)
					row.createCell(7).setCellValue(
							record.get("source").toString());
				if (record.get("wqunit") != null)
					row.createCell(8).setCellValue(
							record.get("wqunit").toString());
				if (record.get("wqdept") != null)
					row.createCell(9).setCellValue(
							record.get("wqdept").toString());
				if (record.get("enforce") != null)
					row.createCell(10).setCellValue(
							record.get("enforce").toString());
				if (record.get("casetype") != null)
					row.createCell(11).setCellValue(
							record.get("casetype").toString());
				if (record.get("htime") != null)
					row.createCell(12).setCellValue(
							record.get("htime").toString());
				if (record.get("keycase") != null)
					row.createCell(13).setCellValue(
							record.get("keycase").toString());
				if (record.get("zm") != null)
					row.createCell(14)
							.setCellValue(record.get("zm").toString());
			}
		}

		return wb;
	}
}
