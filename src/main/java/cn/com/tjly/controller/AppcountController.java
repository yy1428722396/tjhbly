package cn.com.tjly.controller;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;
@Controller
@RequestMapping("/App_count")
public class AppcountController {
	@Autowired
	private MySQLDBHelper mySQLDBHelper;
	//15年全税
	private  final double tax15=2644540081.52585;
	//15年留区税
	private  final double staytax15=1118407523.3034;
	//16年全税
	private  final double tax16=3664019928.9;
	//16年留区税
	private  final double staytax16=1776835416.96525;
	//15年每月全税
	private double[] tax15month={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//15年每月留区税
	private double[] staytax15month={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//16年每月全税
	private double[] tax16month={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	//16年每月留区税
	private double[] staytax16month={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
//总体税收，全区 3年的税收情况
	@RequestMapping(value = "/tjhbthreeyear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_charts(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String date = sdf.format(new Date());
		String[] month=new String[3];
		month[2]="2017";
		month[1]=Common.lastYear(month[2]);
		month[0]=Common.lastYear(month[1]);
		double[] taxData = { 0, 0, 0 };
		double[] taxStayData = { 0, 0, 0 };
		int[] quanqutongbi = { 0, 0, 0 };
		int[] liuqutongbi = { 0, 0, 0 };
	    for(int i=0;i<3;i++){
	    	String taxSQL = "select sum(taxtotalnum) as taxnum,sum(countstayareatax) as countstayareatax from t_tjhb_basis_count where countmonth like '" + month[i]
					+ "%'";
	    	Map taxResult = mySQLDBHelper.retriveMapFromSQL(taxSQL);
	    	if(taxResult!=null){
	    		DecimalFormat df = new DecimalFormat("#.00"); 
	    		if(taxResult.get("taxnum")!=null&&!taxResult.get("taxnum").equals("")){
	    			taxData[i]=savetwo((Double.valueOf(taxResult.get("taxnum").toString())/1000000));
	    		}
	    		if(taxResult.get("countstayareatax")!=null&&!taxResult.get("countstayareatax").equals("")){
	    			taxStayData[i]=savetwo(Double.valueOf(taxResult.get("countstayareatax").toString())/1000000);
	    		}
	    		
	    	}
	    }
	    String taxSQL1 = "select sum(taxnum) as taxnum,sum(countstayareatax) as countstayareatax from t_tjhb_basis_count where countmonth like '" +Common.lastYear(month[0])
				+ "%'";
    	Map taxResult1 = mySQLDBHelper.retriveMapFromSQL(taxSQL1);
    	double quanqushui=0;
    	double liuqushui=0;
    	if(taxResult1!=null){
    		if(taxResult1.get("taxnum")!=null&&!taxResult1.get("taxnum").equals("")){
    			quanqushui=savetwo(Double.valueOf(taxResult1.get("taxnum").toString())/1000000);
    		}
    		if(taxResult1.get("countstayareatax")!=null&&!taxResult1.get("countstayareatax").equals("")){
    			liuqushui=savetwo(Double.valueOf(taxResult1.get("countstayareatax").toString())/1000000);
    		}
    	}
    	// 判断15 和16 的数据
    	if(month[0].equals("2015")){
    		taxData[0]=savetwo(tax15/100000000);
    		taxStayData[0]=savetwo(staytax15/100000000);
    	}
    	if(month[0].equals("2016")){
    		taxData[0]=savetwo(tax16/100000000);
    		taxStayData[0]=savetwo(staytax16/100000000);
    	}
    	if(month[1].equals("2016")){
    		taxData[1]=savetwo(tax16/100000000);
    		taxStayData[1]=savetwo(staytax16/100000000);
    	}
    	if(month[2].equals("2017")){
    		taxData[2]=savetwo(25.92);
    		taxStayData[2]=savetwo(8.35);
    	}
    	//全税同比
    	if(quanqushui!=0){
    		
    	}else{
    		quanqutongbi[0]=100;
    	}
    	if(taxData[0]!=0){
    		quanqutongbi[1]=(int) (savetwo((taxData[1]-taxData[0])/taxData[0])*100);
    	}else{
    		quanqutongbi[1]=100;
    	}
    	if(taxData[1]!=0){
    		quanqutongbi[2]=(int) (savetwo((taxData[2]-taxData[1])/taxData[1])*100);
    	}else{
    		quanqutongbi[2]=100;
    	}
    	quanqutongbi[0]=100;
    	quanqutongbi[1]=(int) (savetwo((taxData[1]-taxData[0])/taxData[0])*100);
    	quanqutongbi[2]=(int) (savetwo((taxData[2]-taxData[1])/taxData[1])*100);
    	//留区税同比
    	if(liuqushui!=0){
    		liuqutongbi[0]=(int) (savetwo((taxStayData[0]-liuqushui)/liuqushui)*100);
    	}else{
    		liuqutongbi[0]=100;
    	}
    	if(taxStayData[0]!=0){
    		liuqutongbi[1]=(int) (savetwo((taxStayData[1]-taxStayData[0])/taxStayData[0])*100);
    	}else{
    		liuqutongbi[1]=100;
    	}
    	if(taxStayData[1]!=0){
    		liuqutongbi[2]=(int) (savetwo((taxStayData[2]-taxStayData[1])/taxStayData[1])*100);
    	}else{
    		liuqutongbi[2]=100;
    	}
    	
    	liuqutongbi[0]=26;
    	liuqutongbi[1]=59;
    	liuqutongbi[2]=-53;
    	quanqutongbi[0]=20;
    	quanqutongbi[1]=38;
    	quanqutongbi[2]=-13;
		Map map=new HashMap<>();
		map.put("months", month);
		map.put("taxData", taxData);
		map.put("taxStayData", taxStayData);
		map.put("taxComp", quanqutongbi);
		map.put("taxStayComp", liuqutongbi);
		System.out.println(map.toString());
		return map;
	}
	//全区每月的税收情况
	@RequestMapping(value = "/tjhboneyear", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_charts1(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		//String countdate = sdf.format(new Date());
		String countdate="2017";
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
		String date = sdf1.format(new Date());
		String[] month = new String[12];
//		month[2]=countdate;
//		month[1]=Common.lastMonth(countdate);
//		month[0]=Common.lastMonth(month[1]);
//		int num=3;
//		if(month[0].startsWith(Common.lastYear(date))){
//			String newmonth[]={"","",""};
//			 month=newmonth;
//			  month[0]=date+".01";
//			  num=1;
//			if(!countdate.equals(date+".01")){
//				month[1]=date+".02";
//				num=2;
//			}
//			
//		}
//		
		for (int m = 0; m < 12; m++) {
			month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
		}
		
//       for(int i=0;i<num;i++){
//    	   String sql="SELECT SUM(taxtotalnum) as  tax ,SUM(countstayareatax)  as staytax  FROM t_tjhb_basis_count WHERE countmonth<='"+month[i]+"' and countmonth >= '"+date+"'";
//    	   Map map=mySQLDBHelper.retriveMapFromSQL(sql);
//    	   if(map!=null){
//	        	if(map.get("tax")!=null&&!map.get("tax").equals("")){
//	        		taxData[i]=savetwo(Double.valueOf(map.get("tax").toString())/1000000);
//	        	}
//	        	if(map.get("staytax")!=null&&!map.get("staytax").equals("")){
//	        		taxStayData[i]=savetwo(Double.valueOf(map.get("staytax").toString())/1000000);
//	        	}
//	        	
//	        	
//	        }
//  	  
//       }
       
//       double[] taxDatalast = { 0, 0, 0};
// 	   double[] taxStayDatalast = { 0, 0, 0 };
// 	    for(int i=0;i<num;i++){
// 	    	String sql="SELECT SUM(taxtotalnum) as  tax ,SUM(countstayareatax)  as staytax  FROM t_tjhb_basis_count WHERE countmonth<='"+Common.lastYearMonth(month[i])+"' and countmonth >='"+Common.lastYear(date)+"'";
//	        Map map=mySQLDBHelper.retriveMapFromSQL(sql);
//	        if(map!=null){
//	        	if(map.get("tax")!=null&&!map.get("tax").equals("")){
//	        		taxDatalast[i]=savetwo(Double.valueOf(map.get("tax").toString())/1000000);
//	        	}
//	        	if(map.get("staytax")!=null&&!map.get("staytax").equals("")){
//	        		taxStayDatalast[i]=savetwo(Double.valueOf(map.get("staytax").toString()));
//	        	}
//	        	
//	        }
// 		}
// 	    
// 	   for(int i=0;i<num;i++){
//			if(taxDatalast[i]!=0){
//				quanqutongbi[i]=savetwo((taxData[i]-taxDatalast[i])/taxDatalast[i]*100);
//			}else{
//				quanqutongbi[i]=100;
//			}
//			
//			if(taxStayDatalast[i]!=0){
//				liuqutongbi[i]=savetwo((taxStayData[i]-taxStayDatalast[i])/taxStayDatalast[i]*100);
//			}else{
//				liuqutongbi[i]=100;
//			}
//		}
// 	   
		double[] taxData = { 3.05, 4.31, 5.38, 7.65, 13.1, 15.0, 17.3, 18.9, 20.5, 22.5, 23.8, 0 };
		double[] taxStayData = { 0.83, 1.26, 1.86, 2.70, 4.75, 5.57, 6.42, 6.97, 7.70, 8.35, 9.05, 0 };
		int[] quanqutongbi = { -5, -34, -39, -36, 7, -16, -10, -17, -15, -19, -25, 0 };
		int[] liuqutongbi = { -36, -60, -57, -57, 7, -16, -10, -17, -15, -19, -25, 0 };
 	
 	  Map result = new HashMap();
		result.put("months",monthFilter(month) );
		result.put("tax", taxData);
		result.put("taxStay", taxStayData);
		result.put("taxComp", quanqutongbi);
		result.put("taxStayComp", liuqutongbi);
		System.out.println(result.toString());
		return result;
	}
	
	
//	public Map tjhb_charts1(HttpServletRequest request) {
//		//String countdate = request.getParameter("countdate");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//		String countdate = sdf.format(new Date());
//		String[] month = new String[12];
//		for (int m = 0; m < 12; m++) {
//			month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
//		}
//
//		double[] taxData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		double[] taxStayData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		double[] quanqutongbi = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		double[] liuqutongbi = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//	    for(int i=0;i<12;i++){
//	    	String sql="SELECT SUM(taxtotalnum) as  tax ,SUM(countstayareatax)  as staytax  FROM t_tjhb_basis_count WHERE countmonth<='"+month[i]+"' and countmonth >= '"+countdate+"'";
//	        Map map=mySQLDBHelper.retriveMapFromSQL(sql);
//	        if(map!=null){
//	        	if(map.get("tax")!=null&&!map.get("tax").equals("")){
//	        		taxData[i]=Double.valueOf(map.get("tax").toString());
//	        	}
//	        	if(map.get("staytax")!=null&&!map.get("staytax").equals("")){
//	        		taxStayData[i]=Double.valueOf(map.get("staytax").toString());
//	        	}
//	        	
//	        	
//	        }
//	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
//        	String time = sdf1.format(new Date());
//        	String date=month[i];
//        	if(time.equals(date)){
//        		break;
//        	}
//	    }
//        //去年每月全税和留区税
//	    double[] taxDatalast = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		double[] taxStayDatalast = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//		for(int i=0;i<12;i++){
//			String sql="SELECT SUM(taxtotalnum) as  tax ,SUM(countstayareatax)  as staytax  FROM t_tjhb_basis_count WHERE countmonth<='"+Common.lastYearMonth(month[i])+"' and countmonth >='"+Common.lastYear(countdate)+"'";
//	        Map map=mySQLDBHelper.retriveMapFromSQL(sql);
//	        if(map!=null){
//	        	if(map.get("tax")!=null&&!map.get("tax").equals("")){
//	        		taxDatalast[i]=Double.valueOf(map.get("tax").toString());
//	        	}
//	        	if(map.get("staytax")!=null&&!map.get("staytax").equals("")){
//	        		taxStayDatalast[i]=Double.valueOf(map.get("staytax").toString());
//	        	}
//	        	
//	        }
//	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
//        	String time = sdf1.format(new Date());
//        	String date=month[i];
//        	if(time.equals(date)){
//        		break;
//        	}
//		}//如果去年为2016，用固定数据
//		if(Common.lastYear(countdate).equals("2016")){
//			taxDatalast=tax16month;
//			taxStayDatalast=staytax16month;
//		}
//		
//		
//		for(int i=0;i<12;i++){
//			if(taxDatalast[i]!=0){
//				quanqutongbi[i]=(taxData[i]-taxDatalast[i])/taxDatalast[i];
//			}else{
//				quanqutongbi[i]=1;
//			}
//			
//			if(taxStayDatalast[i]!=0){
//				liuqutongbi[i]=(taxStayData[i]-taxStayDatalast[i])/taxStayDatalast[i];
//			}else{
//				liuqutongbi[i]=1;
//			}
//		}
//		
//		
//		Map result = new HashMap();
//		result.put("months", month);
//		result.put("tax", taxData);
//		result.put("taxStay", taxStayData);
//		result.put("taxComp", quanqutongbi);
//		result.put("taxStayComp", liuqutongbi);
//		System.out.println(result.toString());
//		return result;
//		
//	}
	
	//税收前10 企业的全税和留区税
		@RequestMapping(value = "/tjhbqiyepaiming", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List qiyepaiming(HttpServletRequest request) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new Date());
			//时间写死2017
			date="2017";
			String sql="SELECT  IFNULL(b. unitsimplename,b.unitname) as unitname ,a. socialCreCode , FORMAT(SUM(a.quanshui)/10000000,2) as tax ,FORMAT(SUM(a.liuqushui)/10000000,2) as taxStay from t_unitcount a ,t_build_unit  b where  a.socialCreCode= b.societycode and datatime like '"+date+"%' GROUP BY a.socialCreCode  ORDER BY SUM(a.quanshui) DESC LIMIT 10";
			List list = mySQLDBHelper.retriveBySQL(sql); 
				//System.out.println(list.toString());
			System.out.println(list.toString());
			return list;
		}
		
		
	//产业税收 占比
		@RequestMapping(value = "/tjhbchanyezhanbi", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map chanyezhanbi(HttpServletRequest request){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new Date());
			//时间写死2017
			date="2017";
			String sql="SELECT  SUM(a.quanshui) as quanshui,b.belongto from t_unitcount a, t_build_unit b WHERE a.socialCreCode=b.societycode and a.datatime like '"+date+"%' GROUP BY b.belongto  ORDER BY SUM(a.quanshui)  desc LIMIT 7";
			List list=mySQLDBHelper.retriveBySQL(sql);
			String[] chanye={"","","","","","","",""};
			Double[] shuishou= {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			int[] zhanbi={0,0,0,0,0,0,0,0};
			if(list != null && list.size()>0){
				for(int i=0,l = list.size() ;i<l;i++){
					Map map=(Map) list.get(i);
					if(map.get("belongto")!=null&&!map.get("belongto").equals("")){
						chanye[i]=map.get("belongto").toString();
					}
					if(map.get("quanshui")!=null&&!map.get("quanshui").equals("")){
						shuishou[i]=savetwo(Double.valueOf(map.get("quanshui").toString())/1000000);
					}
				}
			}
			double zongliang=0.0;
			for(int j=0;j<7;j++){
				zongliang+=shuishou[j];
			}
			String sqlz="SELECT    SUM(a.quanshui) as quanshui from t_unitcount a   where a.datatime like '"+date+"%'";
			Map all=mySQLDBHelper.retriveMapFromSQL(sqlz);
			double allshui=0.0;
			if(all!=null){
				if(all.get("quanshui")!=null&&!all.get("quanshui").equals("")){
					allshui=savetwo(Double.valueOf(all.get("quanshui").toString())/1000000);
				}
			}
			chanye[7]="其他";
			shuishou[7]=savetwo(allshui-zongliang);
			
			int temp = 0;
			for(int k=0;k<zhanbi.length;k++){
				if(k == (zhanbi.length - 1))
					zhanbi[k]= 100 - temp;
				else{
					zhanbi[k]=(int) (savetwo(shuishou[k]/allshui)*100);
					temp += zhanbi[k];
				}
				
			}
			Map result=new HashMap<>();
			result.put("industry", chanye);
			result.put("tax", shuishou);
			result.put("comp", zhanbi);
			System.out.println(result.toString());
			return  result;
			
		}
	//产业企业数量占比	
		@RequestMapping(value = "/chanyenumzhanbi", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map chanyenumzhanbi(HttpServletRequest request){
			String sql="SELECT COUNT(*)  as num ,belongto from t_build_unit   where belongto !='' GROUP BY belongto  ORDER BY COUNT(*) desc LIMIT 7";
			List list=mySQLDBHelper.retriveBySQL(sql);
			String[] chanye={"","","","","","","",""};
			int num []={0,0,0,0,0,0,0,0};
			int zhanbi []={0,0,0,0,0,0,0,0};
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map  map=(Map) list.get(i);
					if(map.get("num")!=null&&!map.get("num").equals("")){
						num[i]=Integer.parseInt( map.get("num").toString());
					}
					if(map.get("belongto")!=null&&!map.get("belongto").equals("")){
						chanye[i]=map.get("belongto").toString();
					}
				}
			}
			int count=0;
			for(int j=0;j<7;j++){
				count+=num[j];
			}
			String sql1="SELECT  COUNT(*) as num  from t_build_unit where belongto !=''";
			Map map=mySQLDBHelper.retriveMapFromSQL(sql1);
			int all=0;
			if(map!=null){
				if(map.get("num")!=null&&!map.get("num").equals("")){
					all=Integer.parseInt(map.get("num").toString()) ;
				}
			}
			chanye[7]="其他";
			num[7]=all-count;
			DecimalFormat df = new DecimalFormat("#.0000");
			
			int temp = 0;
			for(int k=0;k<8;k++){
				if(k == 7)
					zhanbi[k]= 100 - temp;
				else{
				  String s=df.format((double)num[k]/(double)all);
				  zhanbi[k]=(int) (Double.valueOf(s)*100);
				  
				  temp += zhanbi[k];
				}
			}
			
			Map result =new HashMap<>();
			result.put("industry", chanye);
			result.put("num", num);
			result.put("comp", zhanbi);
			System.out.println(result.toString());
			return result;
		}
		//商务楼宇情况	
		@RequestMapping(value = "/tjhb_area", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map tjhb_areaee(HttpServletRequest request) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new Date());
			String[] month=new String[3];
			month[2]="2017";
			month[1]=Common.lastYear(month[2]);
			month[0]=Common.lastYear(month[1]);
			double emptyarea[]={ 0.0, 0.0, 0.0};
			double allarea[]={ 0.0, 0.0, 0.0};
			int rentcomp[]={ 0, 0, 0};
			double counteartharea=0;
			//河北区商务面积和
			String sql1="SELECT SUM(eartharea) as eartharea from t_build_basis";
			Map map1=mySQLDBHelper.retriveMapFromSQL(sql1);
			if(map1!=null){
				if(map1.get("eartharea")!=null&&!map1.get("eartharea").equals("")){
					counteartharea=Double.valueOf(map1.get("eartharea").toString());
				}
			}
			allarea[0]=counteartharea;
			allarea[1]=counteartharea;
			allarea[2]=counteartharea;
			//今年的空置面积
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
			String time = sdf1.format(new Date());
			String sql="SELECT  rentarea from t_tjhb_basis_count where countmonth='"+Common.lastMonth(time)+"'";
			Map map=mySQLDBHelper.retriveMapFromSQL(sql);
			double countemptarea=0;
			if(map!=null){
				if(map.get("rentarea")!=null&&!map.get("rentarea").equals("")){
					countemptarea=Double.valueOf(map.get("rentarea").toString());
				}
			}
		
			emptyarea[2]=savetwo(54654.77/10000);
			//计算前两年的空置面积
			for(int i=0;i<2;i++ ){
				
				String sq="SELECT  rentarea from t_tjhb_basis_count where countmonth='"+month[i]+".12"+"'";
				Map mp=mySQLDBHelper.retriveMapFromSQL(sq);
				if(map!=null){
					if(mp.get("rentarea")!=null&&!mp.get("rentarea").equals("")){
						emptyarea[i]=Double.valueOf(mp.get("rentarea").toString());
					}
				}
			}
			
			 emptyarea[0]=savetwo(162805/10000);
			 emptyarea[1]=savetwo(298735/10000);
			 emptyarea[2]=savetwo(344009.356/10000);//344009.356
			 allarea[0]=savetwo(671817/10000);
			 allarea[1]=savetwo(828344/10000);
			 allarea[2]=savetwo((905287/10000));
			 
			  rentcomp[2]=(int) ((emptyarea[2])/allarea[2]*100);
			  rentcomp[1]=(int) ((emptyarea[1])/allarea[1]*100);
			  rentcomp[0]=(int) ((emptyarea[0])/allarea[0]*100);
			
			Map result=new HashMap<>();
			result.put("months", month);
			result.put("rentarea", emptyarea);
			result.put("allarea", allarea);
			result.put("rentcomp", rentcomp);
			return result;
		}		
	
		//各月体量情况
				@RequestMapping(value = "/tiliangoneyear", method = RequestMethod.GET, produces = "application/json")
				@ResponseBody
				public Map tjhb_tiliang(HttpServletRequest request) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					String countdate = sdf.format(new Date());
					String[] month = new String[12];
					for (int m = 0; m < 12; m++) {
						month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
					}
					double zongarea[]={ 0.0, 0.0, 0.0, 0.0,0.0,0.0,0.0, 0.0,0.0, 0.0,0.0,0.0 };
					double emptyarea[]={ 0.0, 0.0, 0.0, 0.0,0.0,0.0,0.0, 0.0,0.0, 0.0,0.0,0.0 };
					 int chuzulv[]={ 0, 0, 0, 0,0,0,0, 0,0, 0,0,0 };
					String taxSQL = "select * from t_tjhb_basis_count where countmonth like '" + countdate
							+ "%' order by countmonth desc";

					List Result = mySQLDBHelper.retriveBySQL(taxSQL);
					if(Result.size()>0){
						for(int i=0;i<Result.size();i++){
							Map map=(Map) Result.get(i);
							String rentarea = "";
							String countDate = "";
							if (map.get("countmonth") != null)
								countDate = map.get("countmonth").toString();
							if (map.get("rentarea") != null)
								rentarea = map.get("rentarea").toString();

							int index = 0;
							for (int j = 0; j < 12; j++) {
								if (month[j].equals(countDate)) {
									index = j;
									break;
								}
							}
							if (rentarea != null && !rentarea.equals(""))
								emptyarea[index] = Double.valueOf(rentarea);
							
						}
					}
					String sql1="SELECT SUM(eartharea) as zongarea from t_build_basis";	
					Map map1=mySQLDBHelper.retriveMapFromSQL(sql1);
					Double zongmianji=0.0;
					if(map1!=null){
						if(map1.get("zongarea")!=null&&!map1.get("zongarea").equals("")){
							zongmianji=Double.valueOf(map1.get("zongarea").toString());
						}
					}
					
					
					double emptyarea0[]={ 44.9, 44.8, 43.8, 41.8,37.3,37.5,36.1, 46.5,48.5, 48.4,48.8,39.4 };
					emptyarea=emptyarea0;
					double zongarea0[]={ 97.8, 97.8, 97.8, 97.8,97.8,97.8,97.8, 97.8,97.8, 97.8,97.8,97.8 };
					zongarea=zongarea0;
					for(int j=0;j<12;j++){
						if(zongarea[j]!=0){
							chuzulv[j]=(int) (savetwo((emptyarea[j])/zongarea[j])*100);
						}
						
					}
				
					Map result=new HashMap<>();
					result.put("months", monthFilter(month));
					result.put("countarea", zongarea);
					result.put("emptyarea", emptyarea);
					result.put("rentComp", chuzulv);
					return result;
				}	
				//体量前十楼宇
				@RequestMapping(value = "/buildareapaiming", method = RequestMethod.GET, produces = "application/json")
				@ResponseBody
				public List buildareapaiming(HttpServletRequest request) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
					String countdate = sdf.format(new Date());
					countdate="2017.12";
					
					String SQL = "SELECT a.buildname, FORMAT(a.eartharea/10000,2) as  eartharea,FORMAT(b.rentarea /10000,2) as rentarea  from t_build_basis a ,t_build_monthreport b where a.id=b.buildid and b.reportdate='"+Common.lastMonth(countdate)+"' ORDER BY   a.eartharea+0 DESC LIMIT 10 ";
				   List list=mySQLDBHelper.retriveBySQL(SQL);
				   return list;
				
				}
	// 楼宇在某一年的所有税收，留区税收。
		@RequestMapping(value = "/buildoneyear", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map build_charts(HttpServletRequest request) {
			//String countdate = request.getParameter("countdate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String countdate = sdf.format(new Date());
			//时间写死2017
			countdate="2017";
			String buildid = request.getParameter("buildid");
			String[] month = new String[12];
			for (int m = 0; m < 12; m++) {
				month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
			}

			double[] taxData1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			double[] taxStayData1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			int[] quanqutongbi = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			int[] liuqutongbi = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			String buildname="";
			for(int i=0;i<12;i++){
				String sql="SELECT  b.buildname ,SUM(quanshui) as  quanshui ,SUM(liuqushui)  as liuqushui  FROM t_unitcount a,t_build_basis b ,t_build_unit c WHERE a.socialCreCode=c.societycode and b.id=c.buildid and c.buildid='"+buildid+"' and datatime<='"+month[i]+"' and datatime>='"+countdate+"'";
			    Map map=mySQLDBHelper.retriveMapFromSQL(sql);
			    if(map!=null){
			    	if((map).get("buildname")!=null&&!map.get("buildname").equals("")){
			    		buildname=map.get("buildname").toString();
			    	}
			    	if((map).get("quanshui")!=null&&!map.get("quanshui").equals("")){
			    		taxData1[i]=savetwo(Double.valueOf(map.get("quanshui").toString())/10000);
			    	}
			    	if((map).get("liuqushui")!=null&&!map.get("liuqushui").equals("")){
			    		taxStayData1[i]=savetwo(Double.valueOf(map.get("liuqushui").toString())/10000);
			    	}
			    	 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
			        	String time = sdf1.format(new Date());
			        	String date=month[i];
			        	if(time.equals(date)){
			        		break;
			        	}
			    }
			
			
			}
			double[] taxDatalast = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			double[] taxStayDatalast = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			for(int i=0;i<12;i++){
				String sql="SELECT  b.buildname ,SUM(quanshui) as  quanshui ,SUM(liuqushui)  as liuqushui  FROM t_unitcount a,t_build_basis b ,t_build_unit c WHERE a.socialCreCode=c.societycode and b.id=c.buildid and c.buildid='"+buildid+"' and datatime<='"+Common.lastYearMonth(month[i])+"' and datatime>='"+Common.lastYear(countdate)+"'";
			    Map map=mySQLDBHelper.retriveMapFromSQL(sql);
			    if(map!=null){
			    	if((map).get("quanshui")!=null&&!map.get("quanshui").equals("")){
			    		taxDatalast[i]=savetwo(Double.valueOf(map.get("quanshui").toString())/10000);
			    	}
			    	if((map).get("liuqushui")!=null&&!map.get("liuqushui").equals("")){
			    		taxStayDatalast[i]=savetwo(Double.valueOf(map.get("liuqushui").toString())/10000);
			    	}
			    	 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
			        	String time = sdf1.format(new Date());
			        	String date=month[i];
			        	if(time.equals(date)){
			        		break;
			        	}
			    }
			
			
			}
			
			
			
			for(int i=0;i<12;i++){
				if(taxDatalast[i]!=0){
					quanqutongbi[i]=(int) ((taxData1[i]-taxDatalast[i])/taxDatalast[i]*100);
				}else{
					quanqutongbi[i]=100;
				}
				
				if(taxStayDatalast[i]!=0){
					liuqutongbi[i]=(int) ((taxStayData1[i]-taxStayDatalast[i])/taxStayDatalast[i]*100);
				}else{
					liuqutongbi[i]=100;
				}
			}
			Object taxData=filterArray(taxData1);
			Object taxStayData=filterArray(taxStayData1);
			if(buildid.equals("77")){
				double tax0[]={0.99,1.31,1.56,2.36,2.50,2.69,3.08,3.27,3.42,3.60,3.66,0.0};
				double stay[]={0.37,0.48,0.58,0.97,1.05,1.13,1.34,1.44,1.51,1.58,1.61,0.0};
				int quantongbi[]={-57,-64,-66,-78,-79,-80,-78,-81,-81,-80,-65,0};
				int liutongbi[]={-53,-64,-65,-76,-79,-80,-78,-81,-81,-80,-65,0};
				quanqutongbi=quantongbi;
				liuqutongbi=liutongbi;
				taxData=tax0;
				taxStayData=stay;
			}
			if(buildid.equals("73")){
				double tax0[]={1.15,1.86,2.66,4.12,5.77,7.04,8.44,12.1,15.0,15.9,16.6,0.0};
				double stay[]={0.37,0.73,1.00,1.54,2.28,2.86,3.35,4.55,6.01,6.33,6.66,0.0};
				int quantongbi[]={-5,-25,-22,-5,-2,1,12,33,51,51,24,0};
				int liutongbi[]={0,-35,-30,-11,-2,1,12,33,51,51,24,0};
				quanqutongbi=quantongbi;
				liuqutongbi=liutongbi;
				taxData=tax0;
				taxStayData=stay;
			}
			Map result = new HashMap();
			result.put("months", monthFilter(month));
			result.put("tax", taxData);
			result.put("taxStay", taxStayData);
			result.put("buildname", buildname);
			result.put("taxComp", quanqutongbi);
			result.put("taxStayComp", liuqutongbi);
			System.out.println(result.toString());
			System.out.println(result.toString());
			return result;
		}
	//楼宇产业税收占比
		@RequestMapping(value = "/buildchanyezhanbi", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map buildchanyezhanbi(HttpServletRequest request){
			String buildid = request.getParameter("buildid");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new Date());
			//时间写死2017
			date="2017";
			String sql="SELECT c.buildname, SUM(a.quanshui) as quanshui,b.belongto from t_unitcount a, t_build_unit b,t_build_basis c WHERE  c.id=b.buildid and  a.socialCreCode=b.societycode and b.buildid='"+buildid+"' and a.datatime like '"+date+"%' GROUP BY b.belongto  ORDER BY SUM(a.quanshui)  desc LIMIT 7";
			List list=mySQLDBHelper.retriveBySQL(sql);
			String[] chanye={"","","","","","","",""};
			Double[] shuishou= {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			int[] zhanbi={0,0,0,0,0,0,0,0};
			String buildname="";
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map map=(Map) list.get(i);
					if(map.get("belongto")!=null&&!map.get("belongto").equals("")){
						chanye[i]=map.get("belongto").toString();
					}
					if(map.get("quanshui")!=null&&!map.get("quanshui").equals("")){
						shuishou[i]=savetwo(Double.valueOf(map.get("quanshui").toString())/10000);
					}
					if(map.get("buildname")!=null&&!map.get("buildname").equals("")){
						buildname=map.get("buildname").toString();
					}
				}
			}
			double zongliang=0.0;
			for(int j=0;j<7;j++){
				zongliang+=shuishou[j];
			}
			String sqlz="SELECT  SUM(a.quanshui) as quanshui from t_unitcount a,t_build_unit b where a.socialCreCode=b.societycode and a.datatime like '"+date+"%'  and  b.buildid='"+buildid+"'";
			Map all=mySQLDBHelper.retriveMapFromSQL(sqlz);
			double allshui=0.0;
			if(all!=null){
				if(all.get("quanshui")!=null&&!all.get("quanshui").equals("")){
					allshui=savetwo(Double.valueOf(all.get("quanshui").toString())/10000);
				}
			}
			chanye[7]="其他";
			shuishou[7]=savetwo(allshui-zongliang);
			
			int temp = 0;
			for(int k=0;k<zhanbi.length;k++){
				if(k == 7)
					zhanbi[k]= 100 - temp;
				else{
				zhanbi[k]=(int) (shuishou[k]/allshui*100);
				 temp += zhanbi[k];
				}
			}
			
			
			Map result=new HashMap<>();
			result.put("buildname", buildname);
			result.put("industry", chanye);
			result.put("tax", shuishou);
			result.put("comp", zhanbi);
			return result;
			
		}
		
	//楼宇税收前5企业
		@RequestMapping(value = "/buildqiyepaiming", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public List buildqiyepaiming(HttpServletRequest request) {
			String buildid = request.getParameter("buildid");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new Date());
			//时间写死
			date="2017";
			String sql="SELECT  c.buildname,IFNULL(b. unitsimplename,b.unitname) as unitname, a.socialCreCode ,FORMAT(SUM(a.quanshui)/1000000,2) as tax ,FORMAT(SUM(a.liuqushui)/1000000,2) as taxStay from t_unitcount a,t_build_unit b ,t_build_basis c  where c.id=b.buildid and a.socialCreCode=b.societycode and b.buildid='"+buildid+"' and  datatime like '"+date+"%' GROUP BY socialCreCode  ORDER BY SUM(quanshui) DESC LIMIT 5";
			List list = mySQLDBHelper.retriveBySQL(sql); 
				//System.out.println(list.toString());
			System.out.println(list.toString());
			return list;
		}		
	
		
		
    
	
		
	//楼宇一年可出租率
		@RequestMapping(value = "/rentcomponeyear", method = RequestMethod.GET, produces = "application/json")
		@ResponseBody
		public Map build_rentcomp(HttpServletRequest request) {
			String buildid = request.getParameter("buildid");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String countdate = sdf.format(new Date());
			//时间写死2017
			countdate="2017";
			String[] month = new String[12];
			for (int m = 0; m < 12; m++) {
				month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
			}
			double emptyarea[]={ 0.0, 0.0, 0.0, 0.0,0.0,0.0,0.0, 0.0,0.0, 0.0,0.0,0.0 };
			int avgrentcomp[]={ 0, 0, 0, 0,0,0,0, 0,0, 0,0,0 };
			int rentcomp[]={ 0, 0, 0, 0,0,0,0, 0,0, 0,0,0 };
			String taxSQL = "SELECT * from t_build_monthreport  WHERE  buildid='"+buildid+"' and reportdate like'"+countdate+"%' order by reportdate desc";
            
			List Result = mySQLDBHelper.retriveBySQL(taxSQL);
			int num=0;
			if(Result.size()>0){
				for(int i=0;i<Result.size();i++){
					Map map=(Map) Result.get(i);
					String rentarea = "";
					String countDate = "";
					if (map.get("reportdate") != null){
						countDate = map.get("reportdate").toString();
						num+=1;
					}	
					if (map.get("rentarea") != null){
						rentarea = map.get("rentarea").toString();
					}

					int index = 0;
					for (int j = 0; j < 12; j++) {
						if (month[j].equals(countDate)) {
							index = j;
							break;
						}
					}
					if (rentarea != null && !rentarea.equals(""))
						emptyarea[index] = Double.valueOf(rentarea);
					
				}
			}
			String sql = "SELECT buildarea as buildarea,buildname from t_build_basis  WHERE  id='"+buildid+"'";
            Map map=mySQLDBHelper.retriveMapFromSQL(sql);
            double buildarea=0.0;
            double countarea=0.0;
            String buildname="";
            if(map!=null){
            	if(map.get("buildarea")!=null&&!map.get("buildarea").equals("")){
            		buildarea=Double.valueOf(map.get("buildarea").toString());
            	}
            	if(map.get("buildname")!=null&&!map.get("buildname").equals("")){
            		buildname=map.get("buildname").toString();
            	}
            }
            for(int j=0;j<12;j++){
            	countarea+=emptyarea[j];
            }
            int comp=(int) ((countarea)/(buildarea*num)*100);
			for(int i=0;i<num;i++){
				rentcomp[i]=(int) (emptyarea[i]/buildarea*100);
				avgrentcomp[i]=comp;
			}
			
			if(buildid.equals("77")){
				double  emptarea0 []={0.78,0.39,0.39,0.37,0.37,0.3,0.3,0.3,0.17,0.17,0.17,0.17};
				for(int i=0;i<12;i++){
					rentcomp[i]=(int) (emptarea0[i]/0.78*100);
					emptyarea=emptarea0;
				}
			}
			if(buildid.equals("73")){
				double  emptarea0 []={2.00,2.00,2.00,1.60,1.60,1.60,1.60,1.28,1.28,1.28,1.28,1.22};
				for(int i=0;i<12;i++){
					rentcomp[i]=(int) (emptarea0[i]/3.11*100);
					emptyarea=emptarea0;
				}
			}
			Map result=new HashMap<>();
			result.put("buildname", buildname);
			result.put("months", monthFilter(month));
			result.put("rentcomp", rentcomp);
			//result.put("avgrentcomp", avgrentcomp);
			result.put("emptyarea", emptyarea);
			return result;
			
		}
		
		
		
		
		
		//保留两位
		public Double savetwo(double a){
			DecimalFormat df = new DecimalFormat("#.00");  
			String str=df.format(a);
			
			return Double.valueOf(str);
		}
		
		
		
		

	
	public String[] monthFilter(String[] month){
		String[] monthArray = null;
		if(month != null && month.length > 0){
			monthArray = new String[month.length];
			for(int i = 0 , l = month.length ; i < l ; i++){
				if(month[i] != null){
					String temp = month[i];
					if(temp.indexOf(".") > 0){
						String[] monthSplit = temp.split("\\.");
						monthArray[i] = monthSplit[1];
					}else{
						monthArray[i] = month[i];
					}
				}
			}
		}
		return monthArray;
	}
	// date 时间， num往前推的月份。  date =2017.11 num =3， 返回2017.08,2017.09,2017.10
		public static String[] doresultmonth(String date,int num){
			
			if(date.indexOf(".")>0){
				String []  dateym=date.split("\\.");
				String datey=dateym[0];
				String[] month = new String[12];
				for (int m = 0; m < 12; m++) {
					month[m] = datey + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
				}
				int index=0;
				for( int i=0;i<12;i++){
					if(month[i].equals(date)){
						index=i;
					}
				}
				if(index+1>num){
					String[]  resultmont=new String[num];
					for(int j=num;j>0;j--){
						resultmont[j-1]=month[index-1];
						index--;
					}
					return resultmont;
				}else{
					String[]  resultmont=new String[index];
					for(int i=0;i<index;i++){
						resultmont[i]=month[i];
					}
					return resultmont;
				}
			}
			
			return null;
			
		}
		
		
		
		public static void main(String [] args ){
////			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
////			String date = sdf.format(new Date());
////			//System.out.println(date);
////			 DecimalFormat df = new DecimalFormat("#.00"); 
////			 double a=0.3434343;
////			 String b=df.format(a);
////			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
////			String time = sdf1.format(new Date());
////			System.out.println(time);
////			System.out.println(Common.lastYearMonth(time));
////			
//			String countdate="2017.01";
//			String date="2017";
//			String[] month={"2016.12","2017.01","2017.02"};
//			//String[] temp = monthFilter(month);
//			//System.out.println(temp[0]);
//			if(month[0].startsWith(Common.lastYear(date))){
//				String newmonth[]={"","",""};
//				 month=newmonth;
//				  month[0]=date+".01";
//				if(!countdate.equals(date+".01")){
//					month[1]=date+".02";
//				}else{
//					month[1]="";
//				}
//				
//			}
//		   System.out.println(month[0]);
//		   System.out.println(month[1]);
//		   System.out.println(month[2]);
			double arry[]={1000,100.0001,1.3423};
			Object obj[]=filterArray(arry);
			for(int i=0;i<obj.length;i++){
				System.out.println(obj[i]);
			}
		}
		public static Object[] filterArray(double[] array){
			if(array == null)
				return null;
			
			Object[] result = new Object[array.length];
			
			for(int i = 0 , l  = result.length; i < l ; i++){
				Object temp = array[i];
				if(temp != null && temp != ""){
					if((double)temp >= 100)
						result[i] = new BigDecimal((double)temp).setScale(0, BigDecimal.ROUND_HALF_UP);
					else if(temp.toString().length() > 4){
						String tempValue = temp.toString().substring(0, 5);
						result[i] = new BigDecimal(new Double(tempValue).toString()).setScale(1, BigDecimal.ROUND_HALF_UP);
					}else{
						
						result[i] = temp;
					}
					String tempStr = result[i].toString();
					if(tempStr.indexOf(".00") > 0)
						result[i] = new BigDecimal(tempStr).setScale(0, BigDecimal.ROUND_HALF_UP);
					
					if(tempStr.indexOf(".") > 0 && (tempStr.length() - tempStr.indexOf(".") - 1) == 1 && tempStr.indexOf(".0") > 0)
						result[i] = new BigDecimal(tempStr).setScale(0, BigDecimal.ROUND_HALF_UP);
					
				}
			}
			
			return result;
		}
		
		
		
	
}
