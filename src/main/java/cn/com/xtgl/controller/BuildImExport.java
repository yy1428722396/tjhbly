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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
		path=path+"tjlyxtgl/temp/basic.xls";
		
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
		
		
		
		
		List listbuild = mySQLDBHelper.retriveBySQL(sql);
		String filed="buildname,address,type,buildarea,eartharea,emptyarea,rent,propertyname,industryunit,manageunit,propertyname,dutyunit,buildclass";
		String[] filedlist=filed.split(",");
		FileInputStream fis = new FileInputStream(path);
		Workbook wb= new HSSFWorkbook(fis);
		 Sheet sheet = wb.getSheetAt(0);
		 Font hssfFont = wb.createFont();
		    hssfFont.setFontHeightInPoints((short)10);
		    hssfFont.setFontName("宋体");
		    CellStyle cellStyle = wb.createCellStyle();
		    cellStyle.setFont(hssfFont);
		    for(int i=0;i<listbuild.size();i++){
		    	 Row row = sheet.createRow(i+3); 
		    	 
		    	  HashMap map=new HashMap();
		          map=(HashMap<String, Object>) listbuild.get(i);
		          row.createCell(0).setCellValue(i+1);
		         for(int j=0;j<filedlist.length;j++){
		        	 
		        	Cell cell= row.createCell(j+1);
		        	cell.setCellValue((datauntil.setValue(map.get(filedlist[j]))).toString());
		        	cell.setCellStyle(cellStyle);
		         }
		          
		          
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
        CommonsMultipartFile file = excelfile;  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        in = file.getInputStream();  
        
       
        String fileName = file.getOriginalFilename(); 
        
        Workbook wb=null;
        if(fileName.endsWith("xls")){  
            //2003  
        	wb = new HSSFWorkbook(in);  
        }else if(fileName.endsWith("xlsx")){  
            //2007  
        	wb = new XSSFWorkbook(in);  
        }  
        Sheet sheet=wb.getSheetAt(0);
		
		for(int j=3;j<sheet.getLastRowNum();j++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			Row row=sheet.getRow(j);
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
        CommonsMultipartFile file = excelfile;  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        List listbuildidname = mySQLDBHelper.retriveBySQL("select id,buildname from t_build_basis");
       
        in = file.getInputStream();  
        String fileName = file.getOriginalFilename(); 
        
        Workbook wb=null;
        if(fileName.endsWith("xls")){  
            //2003  
        	wb = new HSSFWorkbook(in);  
        }else if(fileName.endsWith("xlsx")){  
            //2007  
        	wb = new XSSFWorkbook(in);  
        }  
        Sheet sheet=wb.getSheetAt(0);
		System.out.println(sheet.getLastRowNum());
		for(int j=1;j<sheet.getLastRowNum()+1;j++){
			HashMap<String, Object> map=new HashMap<String, Object>();
			Row row=sheet.getRow(j);
			
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
        
        if(file==null){  
            throw new Exception("文件不存在！");  
        }  
        String fileName = file.getOriginalFilename(); 
        String datatime=request.getParameter("datatime");
        Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + datatime + "'");
		if(result!=null){
			return "exits";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date date=new Date();
		String importtime=df.format(date);
		String sql="SELECT societycode from t_build_unit";
		List<Map<String,Object>> listqiy=mySQLDBHelper.retriveBySQL(sql);
		int size0=listqiy.size();
        InputStream in =null;  
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        in=file.getInputStream();
        Workbook wb=null;
        if(fileName.endsWith("xls")){  
            //2003  
        	wb = new HSSFWorkbook(in);  
        }else if(fileName.endsWith("xlsx")){  
            //2007  
        	wb = new XSSFWorkbook(in);  
        }  
        Sheet sheet=wb.getSheetAt(0);
        
       
		
		for(int j=3;j<sheet.getLastRowNum()+1;j++){
			int flag=0;
			HashMap<String, Object> map=new HashMap<String, Object>();
			Row row=sheet.getRow(j);
			if(datauntil.formatterdata(row.getCell(0)).toString().length()<10)
				continue;
			 try {
				String socid=datauntil.formatterdata(row.getCell(0)).toString();
				for(int i=0;i<size0;i++){
					HashMap<String, Object> mapqiye=(HashMap<String, Object>) listqiy.get(i);
					if(socid.equals(mapqiye.get("societycode").toString())){
						flag=1;
						break;
					}
						
				}
				if(flag==0){
					continue;
				}
				map.put("socialCreCode", datauntil.formatterdata(row.getCell(0)));
				map.put("name", datauntil.formatterdata(row.getCell(1)));
				map.put("taxableItem",datauntil.getDS( datauntil.formatterdata(row.getCell(2)).toString()));
				map.put("countMarney", datauntil.formatterdata(row.getCell(3)));
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
	    public  String  uploadguoshui(/*@RequestParam(value="excelFile")MultipartFile file,*/HttpServletRequest request) throws Exception {  
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
	        System.out.println("通过传统方式form表单提交方式导入excel文件！"); 
	        MultipartFile file = multipartRequest.getFile("upguoshuifile");  
	          
	       
	        if(file==null){  
	            throw new Exception("文件不存在！");  
	        }  
	        String fileName = file.getOriginalFilename();
	        String datatime=request.getParameter("datatime");
	        Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + datatime + "'");
			if(result!=null){
				return "exits";
			}
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			Date date=new Date();
			String importtime=df.format(date);
			String sql="SELECT societycode from t_build_unit";
			List<Map<String,Object>> listqiy=mySQLDBHelper.retriveBySQL(sql);
			int size0=listqiy.size();
	        InputStream in =null;  
	        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
	        in=file.getInputStream();
	        Workbook wb=null;
	        if(fileName.endsWith("xls")){  
                //2003  
	        	wb = new HSSFWorkbook(in);  
            }else if(fileName.endsWith("xlsx")){  
                //2007  
            	wb = new XSSFWorkbook(in);  
            }  
	        
	        Sheet sheet=wb.getSheetAt(0);
	        
	       
			System.out.println(sheet.getLastRowNum());
			for(int j=2;j<sheet.getLastRowNum()+1;j++){
				int flag=0;
				HashMap<String, Object> map=new HashMap<String, Object>();
				Row row=sheet.getRow(j);
				if(datauntil.formatterdata(row.getCell(0)).toString().length()<10)
					continue;
				String socid=datauntil.formatterdata(row.getCell(0)).toString();
				for(int i=0;i<size0;i++){
					HashMap<String, Object> mapqiye=(HashMap<String, Object>) listqiy.get(i);
					if(socid.equals(mapqiye.get("societycode").toString())){
						flag=1;
						break;
					}
						
				}
				if(flag==0){
					continue;
				}
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
		
		//导出企业信息by 楼宇id
				//
      	@RequestMapping(value = "/exportqiyebyid", method = RequestMethod.GET,produces = "application/json")
		@ResponseBody
		public void exportqiyebyid(HttpServletRequest request,HttpServletResponse response) throws IOException {
			String buildid = request.getParameter("id");
			HttpSession session = request.getSession();
			String role = "";
			if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){

			 role = session.getAttribute("role").toString();
			}
			//楼宇面积从哪个表里取	  
			String sql="SELECT b.buildname, a.rentarea ,b.buildarea  from t_build_basis_count a ,t_build_basis b where a.buildid=b.id and a.buildid='"+buildid+"'ORDER BY  a.countmonth DESC";
            
			List list = mySQLDBHelper.retriveBySQL(sql);
			String buildarea="";
			String rentarea="";
			String buildname="";
			if(list.size()!=0){
				Map map=(Map) list.get(0);
				if(map.get("buildarea")!=null)
					buildarea=map.get("buildarea").toString();
				if(map.get("rentarea")!=null)
					rentarea=map.get("rentarea").toString();
				if(map.get("buildname")!=null)
					buildname=map.get("buildname").toString();
			}
			
			String path= request.getSession().getServletContext().getRealPath("/");
			path=path+"tjlyxtgl/temp/qiyebyid.xls";
			FileInputStream fis = new FileInputStream(path);
		    Workbook wb= new HSSFWorkbook(fis);
		    Sheet sheet = wb.getSheetAt(0);
		    Font hssfFont = wb.createFont();
		    hssfFont.setFontHeightInPoints((short)16);
		    hssfFont.setFontName("宋体");
		    CellStyle cellStyle = wb.createCellStyle();
		    cellStyle.setFont(hssfFont);
		    cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    Row row0=sheet.getRow(1);
		    Cell cell00=row0.getCell(0);
		    cell00.setCellValue("       楼宇名称："+buildname);
		    
		    Row row=sheet.getRow(2);
		    Cell cell=row.createCell(3);
		    cell.setCellValue(buildarea+"㎡");
		    cell.setCellStyle(cellStyle);
		    Cell celll=row.getCell((8));
		    celll.setCellValue("楼宇现有可出租面积："+rentarea+"㎡");
		    celll.setCellStyle(cellStyle);
		    //企业信息
		    String sql1="SELECT a.* ,b.address from  t_build_unit a,t_build_basis b where a.buildid=b.id and a.buildid="+buildid;
		    List listqiye = mySQLDBHelper.retriveBySQL(sql1);
		    int size=listqiye.size();
		    if(size!=0){
		    	for(int i=0;i<size;i++ ){
		    		Map qmap=(Map) listqiye.get(i);
		    		Row row1=sheet.createRow(i+5);
		    		Cell cell0=row1.createCell(0);
		    		cell0.setCellValue(i+1);
		    		Cell cell1=row1.createCell(1);
		    		cell1.setCellValue(qmap.get("unitname").toString());
		    		Cell cell2=row1.createCell(2);
		    		cell2.setCellValue(datauntil.setValue(qmap.get("registryasset")).toString());
		    		Cell cell3=row1.createCell(3);
		    		cell3.setCellValue(datauntil.setValue(qmap.get("belongto")).toString());
		    		Cell cell4=row1.createCell(4);
		    		cell4.setCellValue("");
		    		String dizhi= datauntil.setValue(qmap.get("registryaddress")).toString();
		    		Cell cell5=row1.createCell(5);
		    		Cell cell6=row1.createCell(6);
		    	    if(dizhi.contains("河北区")){
		    	    	cell5.setCellValue("√");
		    	    }else{
		    	    	cell6.setCellValue("√");
		    	    }
		    		Cell cell7=row1.createCell(7);
		    		cell7.setCellValue(dizhi);
		    		Cell cell8=row1.createCell(8);
		    		Cell cell9=row1.createCell(9);
		    		cell8.setCellValue("√");
		    		Cell cell10=row1.createCell(10);
		    		cell10.setCellValue(datauntil.setValue(qmap.get("address")).toString());
		    		Cell cell11=row1.createCell(11);
		    		cell11.setCellValue("");
		    		Cell cell12=row1.createCell(12);
		    		cell12.setCellValue(datauntil.setValue(qmap.get("lawer")).toString());
		    		Cell cell13=row1.createCell(13);
		    		cell13.setCellValue(datauntil.setValue(qmap.get("mobile")).toString());
		    		Cell cell14=row1.createCell(14);
		    		cell14.setCellValue(datauntil.setValue(qmap.get("entertime")).toString());
		    		Cell cell15=row1.createCell(15);
		    		cell15.setCellValue("--");
		    		Cell cell16=row1.createCell(16);
		    		cell16.setCellValue("--");
		    		Cell cell17=row1.createCell(17);
		    		cell17.setCellValue("√");
		    		
		    	}
		    }
		    
		    
		    response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename=buildqiye.xls");    
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    
		}
				
				
		
}
