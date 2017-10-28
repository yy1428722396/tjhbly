package cn.com.xtgl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.gson.annotations.Until;

import java.util.*;
public class datauntil {
public static Object formatterdata(Object ob){
	
	if(ob==null){
		return null;
	}else{
		return ob.toString();
	}
	
}
//从excel单元格读取数据 判断
public static Object getCellValue(Cell cell){  
    Object value = null;  
    DecimalFormat df = new DecimalFormat("0");  //格式化number String字符  
    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化  
    DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字  
      
    switch (cell.getCellType()) {  
    case Cell.CELL_TYPE_STRING:  
        value = cell.getRichStringCellValue().getString();  
        break;  
    case Cell.CELL_TYPE_NUMERIC:  
        if("General".equals(cell.getCellStyle().getDataFormatString())){  
            value = df.format(cell.getNumericCellValue());  
        }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){  
            value = sdf.format(cell.getDateCellValue());  
        }else{  
            value = df2.format(cell.getNumericCellValue());  
        }  
        break;  
    case Cell.CELL_TYPE_BOOLEAN:  
        value = cell.getBooleanCellValue();  
        break;  
    case Cell.CELL_TYPE_BLANK:  
        value = "";  
        break;  
    default:  
        break;  
    }  
    return value;  
}  
//向单元格写数据的数据判断
 public static Object setValue(Object value){
	String textValue = null;
	if(value!=null){
	 if (value instanceof Boolean) {
         boolean bValue = (Boolean) value;
         textValue = "是";
         if (!bValue) {
            textValue ="否";
         }
	 }else if(value instanceof Date){
		 Date date = (Date) value;
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
          textValue = sdf.format(date);
	 }else{
		 textValue = value.toString();
	 }
	   return textValue;
	}else{
		return "";
	}
 }
 //地税种类码表
 public static  int getDS(String Object){
	 String text=Object;
	 int value=0;
	 switch(text){
	 case "营业税":
		 value=1;
		 break;
	 case "企业所得税":
		 value=2;
		 break;
	 case "个人所得税":
		 value=3;
		 break;
	 case "土地增值税":
		 value=4;
		 break;
	 case "增值税":
		 value=5;
		 break;
	 case "城市维护建设税":
		 value=6;
		 break;
	 case "房产税":
		 value=7;
		 break;
	 case "印花税":
		 value=8;
		 break;
	 case "城镇土地使用税":
		 value=9;
		 break;
	 case "车船税":
		 value=10;
		 break;
	 case "契税":
		 value=11;
		 break;
	 case "教育费附加":
		 value=12;
		 break;
	 case "地方教育附加":
		 value=13;
		 break;
	 case "资源税":
		 value=14;
		 break;
	 case "其他收入":
		 value=15;
		 break;
		 
	 case "其他行政事业性收费收入":
		 value=16;
		 break;
	 case "残疾人就业保障金":
		 value=17;
		 break;
	 case "税务部门罚没收入":
		 value=18;
		 break;
	 default:  
	        break;  	 
		 
	        
	 }
 
    return value;
 
 }
 //国税种类码表
 public static  int getGS(String Object){
	 String text=Object;
	 int value=0;
	 switch(text){
	 case "增值税":
		 value=1;
		 break;
	 case "消费税":
		 value=2;
		 break;
	 case "企业所得税":
		 value=3;
		 break;
	 case "城市维护建设税":
		 value=4;
		 break;
	 case "个人所得税":
		 value=5;
		 break;
	 default:  
	        break;  	 
		 
		 
	 }
 
    return value;
 
 }
 /**
  * @param path 模板路径
  * @param list 导出数据
  * @param index 写到模板的行数
  * @param filed  导出的字段
  * @return
  * @throws IOException
  */
 public static Workbook exportmoban(String path,List list,int index,String filed) throws IOException{
	 Workbook wb=null;
	 String[] filedlist =filed.split(",");
	FileInputStream fis = new FileInputStream(path);
    wb= new HSSFWorkbook(fis);
    
    CellStyle cellStyle = wb.createCellStyle();
    //新建font实体
    Font hssfFont = wb.createFont();
    hssfFont.setFontHeightInPoints((short)12);
    hssfFont.setFontName("宋体");
    cellStyle.setFont(hssfFont);
    cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    Sheet sheet = wb.getSheetAt(0);
    for(int i=0;i<list.size();i++){
    	 Row row = sheet.createRow(i+index); 
    	 
    	  HashMap map=new HashMap();
          map=(HashMap<String, Object>) list.get(i);
         for(int j=0;j<filedlist.length;j++){
        	Cell cell= row.createCell(j);
        	cell.setCellValue((datauntil.setValue(map.get(filedlist[j]))).toString());
        	cell.setCellStyle(cellStyle);
         }
          
          
    }
   
	 return wb;
 }
 
}
