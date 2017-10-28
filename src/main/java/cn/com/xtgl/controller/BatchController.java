package cn.com.xtgl.controller;
import java.io.File;
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
@RequestMapping("/build_batch")
public class BatchController {
	@Autowired
	private MySQLDBHelper mySQLDBHelper;
	
	//地税跑批
	@RequestMapping(value = "/dishuibatch", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public String dodishuishuibatch(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String[] properties = {"socialCreCode","salesTax","bisnessTax","personalTax","landaddTax","addTax","constractionTax","buildTax","stampTax","landuseTax","vesselTax","deedTax","eduTax","localeduTax","resourseTax","otherTax","otheradTas","disableTax","fineTax","importtime","datatime"};
		List<Object[]> valueList = new ArrayList<Object[]>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date date=new Date();
		String importtime=df.format(date);
		String datatime=request.getParameter("datatime");
		String sql="SELECT * from t_build_dishui m where m.datatime='"+datatime+"'"+"ORDER BY m.socialCreCode";
		List listdishui = mySQLDBHelper.retriveBySQL(sql);
		String socitycode="";
		int size=listdishui.size();
		Object obj[]={"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,importtime,""};
		Object obj0[]={"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,importtime,""};
		for(int i=0;i<listdishui.size();i++){
			
			if(size==1){
				HashMap map=(HashMap) listdishui.get(i);
				socitycode=(String) map.get("socialCreCode");
				obj=getfirstobj(map,importtime);
				valueList.add(obj);
			}else{
			if(i==0){
				HashMap map=(HashMap) listdishui.get(i);
				socitycode=(String) map.get("socialCreCode");
				obj=getfirstobj(map,importtime);
				
			}else{
				HashMap map=(HashMap) listdishui.get(i);
				if(socitycode.equals(map.get("socialCreCode"))){
					if(i!=size-1){
						obj0=getbtobj(map,obj);	
				    	obj=obj0;
					}else{
						obj0=getbtobj(map,obj);
						obj=obj0;
						valueList.add(obj);
					}
					
				}else{
					if(i!=size-1){
						valueList.add(obj);
				    	
				    	socitycode=(String) map.get("socialCreCode");
						obj0=getfirstobj(map,importtime);
						obj=obj0;
					}else{
						valueList.add(obj);
						obj=getfirstobj(map,importtime);
						valueList.add(obj);
					}
				}
			}
			}
			
			
		}
		mySQLDBHelper.delete("t_build_dishui_count", "datatime='"+datatime+"'");
		mySQLDBHelper.batchCreate("t_build_dishui_count", properties, valueList);
		Map propertiies=new HashMap<>();
		propertiies.put("name", "地税");
		propertiies.put("status", "成功");
		propertiies.put("datatime", datatime);
		propertiies.put("importtime", importtime);
		mySQLDBHelper.create("t_build_batchstatus", propertiies);
		return "success";
		
	}
	
	public Object[] getfirstobj(HashMap<String, Object> map,String importtime){
		Object obj[]={"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,"",""};
		obj[0]=(String)map.get("socialCreCode");
		int shuizhong= Integer.parseInt( (String) map.get("taxableItem"));
		if(shuizhong==1)
			obj[1]=(double)obj[1]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==2)
			obj[2]=(double)obj[2]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==3)
			obj[3]=(double)obj[3]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==4)
			obj[4]=(double)obj[4]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==5)
			obj[5]=(double)obj[5]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==6)
			obj[6]=(double)obj[6]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==7)
			obj[7]=(double)obj[7]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==8)
			obj[8]=(double)obj[8]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==9)
			obj[9]=(double)obj[9]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==10)
			obj[10]=(double)obj[10]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==11)
			obj[11]=(double)obj[11]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==12)
			obj[12]=(double)obj[12]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==13)
			obj[13]=(double)obj[13]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==14)
			obj[14]=(double)obj[14]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==15)
			obj[15]=(double)obj[15]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==16)
			obj[16]=(double)obj[16]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==17)
			obj[17]=(double)obj[17]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==18)
			obj[18]=(double)obj[18]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		    obj[19]=importtime;
		    obj[20]=map.get("datatime");
		return obj;
		
	}
	
	public Object[] getbtobj(HashMap<String, Object> map,Object obj[]){
		Object obj1[]={"",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,"",""};
		obj1=obj;
		
		obj1[0]=(String)obj[0];
		int shuizhong= Integer.parseInt( (String) map.get("taxableItem"));
		if(shuizhong==1)
			obj1[1]=(double)obj[1]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==2)
			obj1[2]=(double)obj[2]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==3)
			obj1[3]=(double)obj[3]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==4)
			obj1[4]=(double)obj[4]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==5)
			obj1[5]=(double)obj[5]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==6)
			obj1[6]=(double)obj[6]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==7)
			obj1[7]=(double)obj[7]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==8)
			obj1[8]=(double)obj[8]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==9)
			obj1[9]=(double)obj[9]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==10)
			obj1[10]=(double)obj[10]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==11)
			obj1[11]=(double)obj[11]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==12)
			obj1[12]=(double)obj[12]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==13)
			obj1[13]=(double)obj[13]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==14)
			obj1[14]=(double)obj[14]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==15)
			obj1[15]=(double)obj[15]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==16)
			obj1[16]=(double)obj[16]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==17)
			obj1[17]=(double)obj[17]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==18)
			obj1[18]=(double)obj[18]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		
		obj1[19]=obj[19];
		obj1[20]=map.get("datatime");
		return obj1;
		
	}
	//国税跑批
	@RequestMapping(value = "/guoshuibatch", method = RequestMethod.GET,produces = "application/json")
	@ResponseBody
	public String doguoshuishuibatch(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String[] properties = {"socialCreCode","addTax","consumTax","bisnessTax","constractionTax","personTax","importtime","datatime"};
		List<Object[]> valueList = new ArrayList<Object[]>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date date=new Date();
		String importtime=df.format(date);
		String datatime=request.getParameter("datatime");
		String sql="SELECT * from t_build_guoshui m where m.datatime='"+datatime+"'"+"ORDER BY m.socialCreCode";
		List listdishui = mySQLDBHelper.retriveBySQL(sql);
		String socitycode="";
		int size=listdishui.size();
		Object obj[]={"",0.0,0.0,0.0,0.0,0.0,importtime,""};
		Object obj0[]={"",0.0,0.0,0.0,0.0,0.0,importtime,""};
		for(int i=0;i<listdishui.size();i++){
			if(size==1){
				HashMap map=(HashMap) listdishui.get(i);
				socitycode=(String) map.get("socialCreCode");
				obj=getfirstobj1(map,importtime);
				valueList.add(obj);
			}else{
			if(i==0){
				HashMap map=(HashMap) listdishui.get(i);
				socitycode=(String) map.get("socialCreCode");
				obj=getfirstobj1(map,importtime);
				
			}else{
				HashMap map=(HashMap) listdishui.get(i);
				if(socitycode.equals(map.get("socialCreCode"))){
					if(i!=size-1){
						obj0=getbtobj1(map,obj);	
				    	obj=obj0;
					}else{
						obj0=getbtobj1(map,obj);
						obj=obj0;
						valueList.add(obj);
					}
					
				}else{
					if(i!=size-1){
						valueList.add(obj);
				    	
				    	socitycode=(String) map.get("socialCreCode");
						obj0=getfirstobj1(map,importtime);
						obj=obj0;
					}else{
						valueList.add(obj);
						obj=getfirstobj1(map,importtime);
						valueList.add(obj);
					}
				}
			}
			}
			
			
			
		}
		mySQLDBHelper.delete("t_build_guoshui_count", "datatime='"+datatime+"'");
		mySQLDBHelper.batchCreate("t_build_guoshui_count", properties, valueList);
		Map propertiies=new HashMap<>();
		propertiies.put("name", "国税");
		propertiies.put("status", "成功");
		propertiies.put("datatime", datatime);
		propertiies.put("importtime", importtime);
		mySQLDBHelper.create("t_build_batchstatus", propertiies);
		return "success";
		
	}	
	public Object[] getfirstobj1(HashMap<String, Object> map,String importtime){
		Object obj[]={"",0.0,0.0,0.0,0.0,0.0,importtime,""};
		obj[0]=(String)map.get("socialCreCode");
		int shuizhong= Integer.parseInt( (String) map.get("taxableItem"));
		if(shuizhong==1)
			obj[1]=(double)obj[1]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==2)
			obj[2]=(double)obj[2]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==3)
			obj[3]=(double)obj[3]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==4)
			obj[4]=(double)obj[4]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==5)
			obj[5]=(double)obj[5]+Double.valueOf((String) map.get("countMarney")).doubleValue();
	
		    obj[6]=importtime;
		    obj[7]=map.get("datatime");
		return obj;
		
	}
	public Object[] getbtobj1(HashMap<String, Object> map,Object obj[]){
		Object obj1[]={"",0.0,0.0,0.0,0.0,0.0,"",""};
		obj1=obj;
		
		obj1[0]=(String)obj[0];
		int shuizhong= Integer.parseInt( (String) map.get("taxableItem"));
		if(shuizhong==1)
			obj1[1]=(double)obj[1]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==2)
			obj1[2]=(double)obj[2]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==3)
			obj1[3]=(double)obj[3]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==4)
			obj1[4]=(double)obj[4]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		if(shuizhong==5)
			obj1[5]=(double)obj[5]+Double.valueOf((String) map.get("countMarney")).doubleValue();
		obj1[6]=obj[6];
		obj1[7]=map.get("datatime");
		return obj1;
		
	}
}
