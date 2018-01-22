package cn.com.tjly.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/app_build")
public class AppBuildinfoController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map index(HttpServletRequest request) {
		Map result = new HashMap();
		String type = request.getParameter("type");
		String limit = request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "SELECT * from t_news where statusvalue<>0";

		if (type != null && !type.trim().equals("") && !type.trim().equals("0"))
			sql += " and type=" + type;
		sql += " order by createtime desc";
		Map news = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);

		if(news != null){
			if(news.get("data") != null){
				List data = (List) news.get("data");
				if(data != null && data.size() > 0){
					for(int i = 0 , l = data.size() ; i < l ; i++){
						Map temp = (Map) data.get(i);
						if(temp.get("content") != null){
							String content = temp.get("content").toString();
							if(content.indexOf("/tjhbly/attached/") > 0){
								content = content.replaceAll("/tjhbly/attached/", "http://47.104.18.46:8080/tjhbly/attached/");
							}
							temp.put("content", content);
						}
					}
				}
				
			}
		}
		
		List vrlist = mySQLDBHelper
				.retriveBySQL("select id,buildname,vrurl,vrpic from t_build_basis where vrurl is not null and vrurl<>''");

		result.put("news", news);
		result.put("vrs", vrlist);

		return result;
	}

	@RequestMapping(value = "/allbuilds", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map allbuilds(HttpServletRequest request) {
		Map result = new HashMap();
		List baseinfo = new ArrayList();
		List newbuild = new ArrayList();
		List maturebuild = new ArrayList();
		List potentialbuild = new ArrayList();

		String importarea = request.getParameter("importarea");

		String sql = "select id,buildname,lat,lon,classfy1,vrurl from t_build_basis where statusvalue<>0";
		if(importarea != null && !importarea.trim().equals(""))
			sql += " and importarea like '" + importarea + "'";
		
		List buildList = mySQLDBHelper.retriveBySQL(sql);
		if (buildList != null) {
			for (int i = 0, l = buildList.size(); i < l; i++) {
				Map build = (Map) buildList.get(i);
				String buildid = build.get("id").toString();

				String buildname = build.get("buildname").toString();
				String lat = "";
				String lon = "";
				String classfy = "";
				String vrurl = "";
				if(build.get("lat") != null)
					lat = build.get("lat").toString();
				if(build.get("lon") != null)
					lon = build.get("lon").toString();
				if(build.get("classfy1") != null)
					classfy = build.get("classfy1").toString();
				if(build.get("vrurl") != null)
					vrurl = build.get("vrurl").toString();
				
				Map tmp = mySQLDBHelper.retriveMapFromSQL("select * from t_build_img where statusvalue<>0 and buildid=" + buildid + " limit 0,1");		
				String imgurl = "";
				if(tmp != null && tmp.get("imgurl") != null)
					imgurl = tmp.get("imgurl").toString();
				Map temp = new HashMap();
				temp.put("buildid", buildid);
				temp.put("buildname", buildname);
				temp.put("lat", lat);
				temp.put("lon", lon);
				baseinfo.add(temp);

				Map temp1 = new HashMap();
				temp1.put("buildid", buildid);
				temp1.put("buildname", buildname);
				temp1.put("classfy", classfy);
				temp1.put("imgurl", imgurl);
				temp1.put("vrurl", vrurl);
				if (classfy != null && classfy.equals("新建楼宇")) {
					newbuild.add(temp1);
				} else if (classfy != null && classfy.equals("成熟楼宇")) {
					maturebuild.add(temp1);
				} else if (classfy != null && classfy.equals("潜力楼宇")) {
					potentialbuild.add(temp1);
				}

			}
		}

		result.put("baseinfo", baseinfo);
		result.put("newbuild", newbuild);
		result.put("maturebuild", maturebuild);
		result.put("potentialbuild", potentialbuild);
		
		return result;
	}

	@RequestMapping(value = "/contactus", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List contactus(HttpServletRequest request) {

		String sql = "select id,buildname,toucheman,propertytel,email from t_build_basis where statusvalue<>0";
		List buildList = mySQLDBHelper.retriveBySQL(sql);
		
		
		return buildList;
	}
	
	@RequestMapping(value = "/build_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map build_detail(HttpServletRequest request) {
		String id = request.getParameter("buildid");
		Map result = new HashMap();
		
		Map info = mySQLDBHelper.retriveMapFromSQL("select * from t_build_basis where statusvalue<>0 and id=" + id);

		if(info == null){
			return null;
		}
		
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
	    
		if(info.get("rent") != null && !info.get("rent").equals("") && pattern.matcher(info.get("rent").toString()).matches()){
			info.put("rent", info.get("rent") + "元/天 • 平方米");
		}
		if(info.get("rentarea") != null && !info.get("rentarea").equals("") && pattern.matcher(info.get("rentarea").toString()).matches()){
			info.put("rentarea", info.get("rentarea") + "㎡");
		}
		
		DecimalFormat df=new DecimalFormat("#.##");
		if(info.get("buildarea") != null && !info.get("buildarea").equals("") && pattern.matcher(info.get("buildarea").toString()).matches()){
			info.put("buildarea", df.format(Double.valueOf(info.get("buildarea").toString()) / 10000) + "万平方米");
		}
		if(info.get("eartharea") != null && !info.get("eartharea").equals("") && pattern.matcher(info.get("eartharea").toString()).matches()){
			info.put("eartharea", df.format(Double.valueOf(info.get("eartharea").toString()) / 10000) + "万平方米");
		}
		
		
		List imgList = mySQLDBHelper.retriveBySQL("select imgurl from t_build_img where statusvalue<>0 and buildid=" + id);
		result.put("img", imgList);
		
		Map rundep = mySQLDBHelper.retriveMapFromSQL("select unitname from t_rununit where statusvalue<>0 and buildid=" + id);
		
		if(rundep != null){
			info.put("rununit", rundep.get("unitname"));
		}else{
			info.put("rununit","");
		}
		result.put("build", info);
		return result;
	}

	@RequestMapping(value = "/allnews", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map allnews(HttpServletRequest request) {
		String type = request.getParameter("type");
		String limit = request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "SELECT * from t_news where statusvalue<>0";

		if (type != null && !type.trim().equals("") && !type.trim().equals("0"))
			sql += " and type=" + type;
		sql += " order by createtime desc";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		
		if(result != null){
			if(result.get("data") != null){
				List data = (List) result.get("data");
				if(data != null && data.size() > 0){
					for(int i = 0 , l = data.size() ; i < l ; i++){
						Map temp = (Map) data.get(i);
						if(temp.get("content") != null){
							String content = temp.get("content").toString();
							if(content.indexOf("/tjhbly/attached/") > 0){
								content = content.replaceAll("/tjhbly/attached/", "http://47.104.18.46:8080/tjhbly/attached/");
							}
							temp.put("content", content);
						}
					}
				}
				
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/news_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map news_detail(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("t_news", "id", id);
		if(result != null){
			if(result.get("content") != null){
				String content = result.get("content").toString();
				if(content.indexOf("/tjhbly/attached/") > 0){
					content = content.replaceAll("/tjhbly/attached/", "http://47.104.18.46:8080/tjhbly/attached/");
					
					//byte[] temp = Base64.encodeBase64(content.getBytes());
					//content = new String(temp);
					
					
				}
				result.put("content", content);
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/addReport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String unitid = request.getParameter("unitid");
		String reportdate = request.getParameter("reportdate");
		String emptyarea = request.getParameter("emptyarea");
		String rentarea = request.getParameter("rentarea");
		String incrbusinessnum = request.getParameter("incrbusinessnum");
		String decrbusinessnum = request.getParameter("decrbusinessnum");
		String asset = request.getParameter("asset");
		String income = request.getParameter("income");
		String profit = request.getParameter("profit");
		String tax = request.getParameter("tax");
		String areadetail = request.getParameter("areadetail");
		String workmannum = request.getParameter("workmannum");
		String incrrentarea = request.getParameter("incrrentarea");

		HttpSession session = request.getSession();
		String role = "";
		if (session.getAttribute("role") != null && !session.getAttribute("role").equals("")) {
			role = session.getAttribute("role").toString();
		} else
			return false;

		Map properties = new HashMap();
		properties.put("unitid", unitid);
		properties.put("reportdate", reportdate);
		properties.put("emptyarea", emptyarea);
		properties.put("rentarea", rentarea);
		properties.put("incrbusinessnum", incrbusinessnum);
		properties.put("decrbusinessnum", decrbusinessnum);
		properties.put("asset", asset);
		properties.put("income", income);
		properties.put("profit", profit);
		properties.put("tax", tax);
		properties.put("areadetail", areadetail);
		properties.put("workmannum", workmannum);
		properties.put("incrrentarea", incrrentarea);

		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);

		Map lreport = mySQLDBHelper.retriveMapFromSQL("select * from t_build_monthreport where reportdate='"
				+ Common.lastMonth(reportdate) + "' and statusvalue<>0 and unitid=" + unitid);

		double incrincome = 0;
		double incrprofit = 0;
		double incrtax = 0;
		int incrworkmannum = 0;

		if (lreport != null) {
			if (lreport.get("income") != null && !lreport.get("income").equals("")) {
				double temp_income = Double.valueOf(lreport.get("income").toString());
				if (income != null && !income.equals(""))
					incrincome = Double.valueOf(income) - temp_income;
			} else {
				incrincome = Double.valueOf(income);
			}

			if (lreport.get("profit") != null && !lreport.get("profit").equals("")) {
				double temp_profit = Double.valueOf(lreport.get("profit").toString());
				if (profit != null && !profit.equals(""))
					incrprofit = Double.valueOf(profit) - temp_profit;
			} else {
				incrprofit = Double.valueOf(profit);
			}

			if (lreport.get("tax") != null && !lreport.get("tax").equals("")) {
				double temp_tax = Double.valueOf(lreport.get("tax").toString());
				if (tax != null && !tax.equals(""))
					incrtax = Double.valueOf(tax) - temp_tax;
			} else {
				incrtax = Double.valueOf(tax);
			}

			if (lreport.get("workmannum") != null && !lreport.get("workmannum").equals("")) {
				int temp_workmannum = Integer.valueOf(lreport.get("workmannum").toString());
				if (workmannum != null && !workmannum.equals(""))
					incrworkmannum = Integer.valueOf(workmannum) - temp_workmannum;
			} else {
				incrworkmannum = Integer.valueOf(workmannum);
			}
		}
		properties.put("incrincome", incrincome);
		properties.put("incrprofit", incrprofit);
		properties.put("incrtax", incrtax);
		properties.put("incrworkmannum", incrworkmannum);

		long id = mySQLDBHelper.create("t_build_monthreport", properties);
		
		Map p = new HashMap();
		p.put("rentarea", emptyarea);
		p.put("emptyarea", emptyarea);
		//mySQLDBHelper.update("t_build_basis", p, "id=" + buildid);

		if (id != 0)
			return true;
		else
			return false;
	}
	
	@RequestMapping(value = "/report_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String unitid = request.getParameter("unitid");
		String month = request.getParameter("yearmonth");
		String sql = "select m.* ,b.buildname,b.buildarea from t_build_monthreport m,t_build_basis b where m.statusvalue<>0 and b.id=m.buildid and m.unitid="
				+ unitid + " and m.reportdate='" + month + "'";
		Map result = mySQLDBHelper.retriveMapFromSQL(sql);

		return result;
	}
	
	@RequestMapping(value = "/allvrs", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List allvrs(HttpServletRequest request) {
		return mySQLDBHelper.retriveBySQL("select id,buildname,vrurl,vrpic,intruduction from t_build_basis where vrurl is not null");
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map test(HttpServletRequest request) {
		Map result = new HashMap();
		String[] test = new String[12];
		for(int i = 0 ; i < 12 ; i++){
			test[i] = i + "";
		}
		
		Double[] test1 = new Double[12];
		for(int i = 0 ; i < 12 ; i++){
			test1[i] = Double.valueOf(i);
		}
		
		Object[] test2 = new Object[12];
		for(int i = 0 ; i < 12 ; i++){
			if(i % 2 ==0)
				test2[i] = i + 0.1;
			else
				test2[i] = i;
		}
		
		result.put("StringMap", test);
		result.put("DoubleMap", test1);
		result.put("ObjectMap", test2);
		
		return result;
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
	
	public static void main(String[] args){
		double[] test = {100.45,10000.45,0.45,1.45,10.45,1000.45,999.45,999.55,999,99,9.00,99.0};
		Object[] result = filterArray(test);
		
		for(int i = 0 , l  = result.length; i < l ; i++){
			System.out.println(result[i]);
		}
	}
}
