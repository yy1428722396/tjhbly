package cn.com.xtgl.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/build_imExport")
public class BuildImExport {
	
	@Autowired
	private MySQLDBHelper mySQLDBHelper;
	//导出楼宇基本
	@RequestMapping(value = "/exportbasic", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public void exportExcel(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//String path=request.getContextPath() ;
		//String path=new java.io.File(application.getRealPath(request.getRequestURI())).getParent(); 
	
		String path= request.getSession().getServletContext().getRealPath("/");
		path=path+"tjlyxtgl\\temp\\aaaa.xls";
		
		System.out.println(path);
		String name = request.getParameter("name");
		String classValue = request.getParameter("classValue");
		String type = request.getParameter("type");
		String right = request.getParameter("right");
		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		String role = "";
		if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		}
		
		
		String sql = "SELECT * from t_build_basis where statusvalue>=" + role;
		if(userType.equals("1"))
			sql += " and rununitid='" + unitid + "'";
		
		if (name != null && !name.trim().equals(""))
			sql += " and buildname like '%" + name + "%'";
		if (type != null && !type.trim().equals("") && !type.trim().equals("0"))
			sql += " and type='" + type + "'";
		if (classValue != null && !classValue.trim().equals("") && !classValue.trim().equals("0"))
			sql += " and classvalue='" + classValue + "'";
		if (right != null && !right.trim().equals("") && !right.trim().equals("0"))
			sql += " and buildright='" + right + "'";
		
		
		
		Map result=new HashMap<>();
		List listbuild = mySQLDBHelper.retriveBySQL(sql);
	
		String[] excelHeader = { "序号", "楼宇名称","地址","楼宇业态","招商方向","建筑面积","商务面积","空置面积","租金","物业","产权单位","经营管理单位","物业公司","责任单位","楼宇分类","楼内重点企业","提升改造情况"};
		HSSFWorkbook wb = new HSSFWorkbook();    
        HSSFSheet sheet = wb.createSheet("楼宇基本情况表");    
        HSSFRow row = sheet.createRow((int) 0);    
        HSSFCellStyle style = wb.createCellStyle();    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
        for (int i = 0; i < excelHeader.length; i++) {    
            HSSFCell cell = row.createCell(i);    
            cell.setCellValue(excelHeader[i]);    
            cell.setCellStyle(style);    
            sheet.autoSizeColumn(i);    
        }    
        for (int i = 0; i < listbuild.size(); i++) {    
            row = sheet.createRow(i + 1); 
            HashMap map=new HashMap();
             map=(HashMap<String, Object>) listbuild.get(i);
             row.createCell(0).setCellValue((datauntil.setValue(map.get("id"))).toString());
             row.createCell(1).setCellValue((datauntil.setValue(map.get("buildname"))).toString());
             row.createCell(2).setCellValue((datauntil.setValue(map.get("prename"))).toString());
             row.createCell(3).setCellValue((datauntil.setValue(map.get("rent"))).toString());
             row.createCell(4).setCellValue((datauntil.setValue(map.get("property"))).toString());
             row.createCell(5).setCellValue((datauntil.setValue(map.get("buildarea"))).toString());
             row.createCell(6).setCellValue((datauntil.setValue(map.get("eartharea"))).toString());
             row.createCell(7).setCellValue((datauntil.setValue(map.get("rentarea"))).toString());
             row.createCell(8).setCellValue((datauntil.setValue(map.get("emptyarea"))).toString());
             row.createCell(9).setCellValue((datauntil.setValue(map.get("buildright"))).toString());
             row.createCell(10).setCellValue((datauntil.setValue(map.get("classvalue"))).toString());
             row.createCell(11).setCellValue((datauntil.setValue(map.get("type"))).toString());
             row.createCell(12).setCellValue((datauntil.setValue(map.get("service"))).toString());
             row.createCell(13).setCellValue((datauntil.setValue(map.get("upfloor"))).toString());
             row.createCell(14).setCellValue((datauntil.setValue(map.get("address"))).toString());
             row.createCell(15).setCellValue((datauntil.setValue(map.get("postcode"))).toString());
             row.createCell(16).setCellValue((datauntil.setValue(map.get("buildyear"))).toString());
             row.createCell(17).setCellValue((datauntil.setValue(map.get("devname"))).toString());
             row.createCell(18).setCellValue((datauntil.setValue(map.get("street"))).toString());
             row.createCell(19).setCellValue((datauntil.setValue(map.get("cbd"))).toString());
             row.createCell(20).setCellValue((datauntil.setValue(map.get("propertyname"))).toString());
             row.createCell(21).setCellValue((datauntil.setValue(map.get("propertytel"))).toString());
             row.createCell(22).setCellValue((datauntil.setValue(map.get("createtime"))).toString());
             row.createCell(23).setCellValue((datauntil.setValue(map.get("downfloor"))).toString());
             row.createCell(24).setCellValue((datauntil.setValue(map.get("toucheman"))).toString());
             row.createCell(25).setCellValue((datauntil.setValue(map.get("roomno"))).toString());
             row.createCell(26).setCellValue((datauntil.setValue(map.get("statusvalue"))).toString());
             row.createCell(27).setCellValue((datauntil.setValue(map.get("rununitid"))).toString());
             row.createCell(28).setCellValue((datauntil.setValue(map.get("vrurl"))).toString());
             row.createCell(29).setCellValue((datauntil.setValue(map.get("carno"))).toString());
             row.createCell(30).setCellValue((datauntil.setValue(map.get("totalfloor"))).toString());
             row.createCell(31).setCellValue((datauntil.setValue(map.get("propertyprice"))).toString());
             row.createCell(32).setCellValue((datauntil.setValue(map.get("industryunit"))).toString());
             row.createCell(33).setCellValue((datauntil.setValue(map.get("manageunit"))).toString());
             row.createCell(34).setCellValue((datauntil.setValue(map.get("dutyunit"))).toString());
             row.createCell(35).setCellValue((datauntil.setValue(map.get("buildclass"))).toString());
     
        }    
        response.setContentType("application/vnd.ms-excel");    
        response.setHeader("Content-disposition", "attachment;filename=buildbasic.xls");    
        OutputStream ouputStream = response.getOutputStream();    
        wb.write(ouputStream);    
        ouputStream.flush();    
        ouputStream.close();    
     
	}

	//导入楼宇基本
	@RequestMapping(value="uploadbasic",method = RequestMethod.POST)  
	@ResponseBody
    public  String  uploadExcel(@RequestParam CommonsMultipartFile excelfile,HttpServletRequest request) throws Exception {   
        HttpSession session = request.getSession();
		String unitid = "";
		String role = "";
		if(session.getAttribute("username") != null && !session.getAttribute("username").equals("")){
			 unitid = (String)session.getAttribute("unitid");
			 role = (String)session.getAttribute("role");
		}else
			return "false";
        InputStream in =null;  
        List<List<Object>> listob = null;  
        CommonsMultipartFile file = excelfile;  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        in = file.getInputStream();  
        
       
        HSSFWorkbook wb = new HSSFWorkbook(in);
		 HSSFSheet sheet=wb.getSheetAt(0);
		
		for(int j=3;j<sheet.getLastRowNum();j++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			HSSFRow row=sheet.getRow(j);
			 try {
				
				map.put("buildname", datauntil.formatterdata(row.getCell(1)));
				map.put("address", datauntil.formatterdata(row.getCell(2)));
				map.put("type",datauntil.formatterdata(row.getCell(3)));
				map.put("cbd", datauntil.formatterdata(row.getCell(4)));
				map.put("buildarea", datauntil.formatterdata(row.getCell(5)));
				map.put("eartharea", datauntil.formatterdata(row.getCell(6)));
				map.put("emptyarea",datauntil.formatterdata(row.getCell(7)));
				map.put("rent", datauntil.formatterdata(row.getCell(8)));
				map.put("propertyprice", datauntil.formatterdata(row.getCell(9)));
				map.put("industryunit",datauntil.formatterdata(row.getCell(10)));
				map.put("manageunit",datauntil.formatterdata(row.getCell(11)));
				map.put("propertyname",datauntil.formatterdata(row.getCell(12)));
				map.put("dutyunit",datauntil.formatterdata(row.getCell(13)));
				map.put("buildclass",datauntil.formatterdata(row.getCell(14)));
				map.put("statusvalue", role);
				map.put("rununitid", unitid);
				list.add(map);
	      	
			} catch (Exception e) {
				// TODO: handle exception
			continue;
			
			}
        }  
		wb.close();
		in.close();
		String[] properties = {"buildname","address","type","cbd","buildarea","eartharea","emptyarea","rent","propertyprice","industryunit",
				"manageunit","propertyname","dutyunit","buildclass","statusvalue","rununitid"};
		List<Object[]> valueList = new ArrayList<Object[]>();
		for (int k=0;k<list.size();k++){
			Object[] obj = { list.get(k).get("buildname"), list.get(k).get("address"),
					  list.get(k).get("type"), list.get(k).get("cbd"),list.get(k).get("buildarea").toString(),
					list.get(k).get("eartharea").toString(), list.get(k).get("emptyarea").toString(), list.get(k).get("rent").toString(),
					 list.get(k).get("propertyprice").toString(), list.get(k).get("industryunit"),list.get(k).get("manageunit"), list.get(k).get("propertyname"),
					 list.get(k).get("dutyunit"), list.get(k).get("buildclass"), list.get(k).get("statusvalue"),list.get(k).get("rununitid")};
			valueList.add(obj);
			System.out.println(list.get(k));
		}
		mySQLDBHelper.batchCreate("t_build_basis", properties, valueList);
	
		
		
		
		return "success";
	}
	
	//导入企业信息unit
	@RequestMapping(value="uploadqiye",method = RequestMethod.POST)  
	@ResponseBody
    public  String  uploadqiye(@RequestParam CommonsMultipartFile excelfile,HttpServletRequest request) throws Exception {  
        HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return "false";
        InputStream in =null;  
        List<List<Object>> listob = null;  
        CommonsMultipartFile file = excelfile;  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        List listbuildidname = mySQLDBHelper.retriveBySQL("select id,buildname from t_build_basis");
       
        in = file.getInputStream();  
        HSSFWorkbook wb = new HSSFWorkbook(in);
		 HSSFSheet sheet=wb.getSheetAt(0);
		System.out.println(sheet.getLastRowNum());
		for(int j=1;j<sheet.getLastRowNum()+1;j++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			HSSFRow row=sheet.getRow(j);
			
			 try {
				map.put("unitname", datauntil.formatterdata(row.getCell(3)));
				map.put("registryasset", datauntil.formatterdata(row.getCell(5)));
				map.put("belongto", datauntil.formatterdata(row.getCell(6)));
				 for(int l=0;l<listbuildidname.size();l++){
					 HashMap hashmap=(HashMap) listbuildidname.get(l);
						if(hashmap.get("buildname").equals(datauntil.formatterdata(row.getCell(7)))){
							map.put("buildid", hashmap.get("id"));
						}
					}
				
				map.put("societycode", datauntil.formatterdata(row.getCell(9)));
				map.put("registryaddress", datauntil.formatterdata(row.getCell(10)));
				map.put("lawer", datauntil.formatterdata(row.getCell(11)));
				map.put("registertime", datauntil.formatterdata(row.getCell(12)));
				map.put("services", datauntil.formatterdata(row.getCell(13)));
				map.put("statusvalue", role);
				
				list.add(map);
	      	
			} catch (Exception e) {
				// TODO: handle exception
			continue;
			
			}
        }  
		wb.close();
		in.close();
		String[] properties = {"unitname","registryasset","belongto","buildid","societycode","registryaddress","lawer","services","statusvalue"};
		List<Object[]> valueList = new ArrayList<Object[]>();
		for (int k=0;k<list.size();k++){
			Object[] obj = {list.get(k).get("unitname"),list.get(k).get("registryasset"),list.get(k).get("belongto"),list.get(k).get("buildid"),list.get(k).get("societycode"),
					list.get(k).get("registryaddress"),list.get(k).get("lawer"),list.get(k).get("services"),list.get(k).get("statusvalue")};
			valueList.add(obj);
			//System.out.println(list.get(k));
			
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		mySQLDBHelper.batchCreate("t_build_unit", properties, valueList);
		SimpleDateFormat de = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(de.format(new Date()));// new Date()为获取当前系统时间
		
		
		
		return "success";
	}
	
	//导入地税
	@RequestMapping(value="uploaddishui.do",method = RequestMethod.POST)  
	@ResponseBody
    public  String  uploaddishui(HttpServletRequest request) throws Exception {  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        System.out.println("通过传统方式form表单提交方式导入excel文件！"); 
        MultipartFile file = multipartRequest.getFile("updishuifile");  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        String datatime=request.getParameter("datatime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date date=new Date();
		String importtime=df.format(date);
        InputStream in =null;  
        List<List<Object>> listob = null;  
       
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        in=file.getInputStream();
        HSSFWorkbook wb=new HSSFWorkbook(in);
        HSSFSheet sheet=wb.getSheetAt(0);
        
       
		
		for(int j=3;j<sheet.getLastRowNum()+1;j++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			Row row=sheet.getRow(j);
			if(datauntil.formatterdata(row.getCell(0)).toString().length()<10)
				continue;
			 try {
				map.put("socialCreCode", datauntil.formatterdata(row.getCell(0)));
				map.put("name", datauntil.formatterdata(row.getCell(1)));
				map.put("taxableItem",datauntil.getDS( datauntil.formatterdata(row.getCell(2)).toString()));
				map.put("leibie", datauntil.formatterdata(row.getCell(3)));
				map.put("dalei", datauntil.formatterdata(row.getCell(4)));
				map.put("countMarney", datauntil.formatterdata(row.getCell(5)));
				map.put("filed", datauntil.formatterdata(row.getCell(6)));
				map.put("importtime",importtime);
				map.put("datatime", datatime);
				
				list.add(map);
	      	
			} catch (Exception e) {
				// TODO: handle exception
			continue;
			
			}
        }  
		wb.close();
		in.close();
		String[] properties = {"socialCreCode","name","taxableItem","leibie","dalei","countMarney","filed","importtime","datatime"};
		List<Object[]> valueList = new ArrayList<Object[]>();
		for (int k=0;k<list.size();k++){
			Object[] obj = {list.get(k).get("socialCreCode"),list.get(k).get("name"),list.get(k).get("taxableItem"),list.get(k).get("leibie"),list.get(k).get("dalei"),
					list.get(k).get("countMarney"),list.get(k).get("filed"),list.get(k).get("importtime"),list.get(k).get("datatime")};
			valueList.add(obj);
			System.out.println(list.get(k));
		}
		SimpleDateFormat dfdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(dfdr.format(new Date()));// new Date()为获取当前系统时间
		mySQLDBHelper.batchCreate("t_build_dishui", properties, valueList);
		SimpleDateFormat de = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println(de.format(new Date()));// new Date()为获取当前系统时间
		
		
		
		return "result";
	}
	
	//导入国税
		@RequestMapping(value="uploadguoshui.do",method = RequestMethod.POST) 
        @ResponseBody
	    public  String  uploadguoshui(HttpServletRequest request) throws Exception {  
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
	        System.out.println("通过传统方式form表单提交方式导入excel文件！"); 
	        MultipartFile file = multipartRequest.getFile("upguoshuifile");  
	        if(file.isEmpty()){  
	            throw new Exception("文件不存在！");  
	        }  
	        String datatime=request.getParameter("datatime");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			Date date=new Date();
			String importtime=df.format(date);
	        InputStream in =null;  
	        List<List<Object>> listob = null;  
	       
	        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
	        in=file.getInputStream();
	        HSSFWorkbook wb=new HSSFWorkbook(in);
	        Sheet sheet=wb.getSheetAt(0);
	        
	       
			System.out.println(sheet.getLastRowNum());
			for(int j=2;j<sheet.getLastRowNum()+1;j++){
				HashMap<String, Object> map=new HashMap<String, Object>();
				Row row=sheet.getRow(j);
				if(datauntil.formatterdata(row.getCell(0)).toString().length()<10)
					continue;
				
					map.put("socialCreCode", datauntil.formatterdata(row.getCell(0)));
					map.put("name", datauntil.formatterdata(row.getCell(1)));
					map.put("taxableItem",datauntil.getGS(datauntil.formatterdata(row.getCell(2)).toString()));
					map.put("countMarney", datauntil.formatterdata(row.getCell(3)));
					map.put("importtime",importtime);
					map.put("datatime", datatime);
					
					list.add(map);
		      	
			
	        }  
			wb.close();
			in.close();
			String[] properties = {"socialCreCode","name","taxableItem","countMarney","importtime","datatime"};
			List<Object[]> valueList = new ArrayList<Object[]>();
			for (int k=0;k<list.size();k++){
				Object[] obj = {list.get(k).get("socialCreCode"),list.get(k).get("name"),list.get(k).get("taxableItem"),
						list.get(k).get("countMarney"),list.get(k).get("importtime"),list.get(k).get("datatime")};
				valueList.add(obj);
				//System.out.println(list.get(k));
			}
			SimpleDateFormat dfdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			System.out.println(dfdr.format(new Date()));// new Date()为获取当前系统时间
			mySQLDBHelper.batchCreate("t_build_guoshui", properties, valueList);
			SimpleDateFormat de = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			System.out.println(de.format(new Date()));// new Date()为获取当前系统时间
			
			
			
			return "result";
		}
		
		//导出企业信息 用模板
		//
		@RequestMapping(value = "/exportqiye", method = RequestMethod.GET,produces = "application/json")
		@ResponseBody
		public void exportguoshuiExcel(HttpServletRequest request,HttpServletResponse response) throws IOException {
			String buildname = request.getParameter("buildname");
			String unitname = request.getParameter("unitname");
			String services = request.getParameter("services");
			//String belongto = request.getParameter("belongto");
			HttpSession session = request.getSession();
			String userType = "";
			String unitid = "";
			String role = "";
			if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
				userType = session.getAttribute("type").toString();
				unitid = session.getAttribute("unitid").toString();
				role = session.getAttribute("role").toString();
			}
			String sql="SELECT  b.buildname,a.unitname,a.societycode,a.registryasset,a.registryaddress,a.services,a.belongto,a.lawer,a.registertime from t_build_unit a,t_build_basis  b where a.statusvalue>="+role+" and a.buildid=b.id ";
			if(userType.equals("1"))
				sql += " and b.rununitid='" + unitid + "'";
			if (buildname != null && !buildname.trim().equals(""))
				sql += " and b.buildname like '%" + buildname + "%'";
			if (unitname != null && !unitname.trim().equals(""))
				sql += " and a.unitname like '%" + unitname + "%'";
			if (services != null && !services.trim().equals(""))
				sql += " and a.services like '%" + services + "%'";
//			if (belongto != null && !belongto.trim().equals(""))
//				sql += " and a.belongto like '%" + belongto + "%'";
			String path= request.getSession().getServletContext().getRealPath("/");
			path=path+"tjlyxtgl/temp/qiyexinxi.xls";
			List list = mySQLDBHelper.retriveBySQL(sql);
			String filed="buildname,unitname,societycode,registryasset,registryaddress,services,belongto,lawer,registertime";
			Workbook wb=datauntil.exportmoban(path,list, 2, filed);
	        response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=buildbasic.xls");    
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    

		}
		
		
		
}
