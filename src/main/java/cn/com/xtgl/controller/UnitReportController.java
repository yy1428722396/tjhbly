package cn.com.xtgl.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

//select service,sum(tax) from t_test group by service

@Controller
@RequestMapping("/unit_report")
public class UnitReportController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String unitid = request.getParameter("unitid");
		String reportdate = request.getParameter("reportdate");
		String asset = request.getParameter("asset");
		String income = request.getParameter("income");
		String profit = request.getParameter("profit");
		String tax = request.getParameter("tax");
		String workmannum = request.getParameter("workmannum");
		
		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		
		Map properties = new HashMap();
		properties.put("unitid", unitid);
		properties.put("reportdate", reportdate);
		properties.put("asset", asset);
		properties.put("income", income);
		properties.put("profit", profit);
		properties.put("tax", tax);
		properties.put("workmannum", workmannum);
		
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
	
		Map lreport = mySQLDBHelper.retriveMapFromSQL("select * from t_unit_monthreport where reportdate='" + Common.lastMonth(reportdate) + "' and statusvalue<>0 and unitid=" + unitid);
		
		double incrincome = 0;
		double incrprofit = 0;
		double incrtax = 0;
		int incrworkmannum = 0;
		
		if(lreport != null){
			if(lreport.get("income") != null && !lreport.get("income").equals("")){
				double temp_income = Double.valueOf(lreport.get("income").toString());
				if(income != null && !income.equals(""))
					incrincome = Double.valueOf(income) - temp_income;
			}else{
				incrincome = Double.valueOf(income);
			}
			
			if(lreport.get("profit") != null && !lreport.get("profit").equals("")){
				double temp_profit = Double.valueOf(lreport.get("profit").toString());
				if(profit != null && !profit.equals(""))
					incrprofit = Double.valueOf(profit) - temp_profit;
			}else{
				incrprofit = Double.valueOf(profit);
			}
			
			if(lreport.get("tax") != null && !lreport.get("tax").equals("")){
				double temp_tax = Double.valueOf(lreport.get("tax").toString());
				if(tax != null && !tax.equals(""))
					incrtax = Double.valueOf(tax) - temp_tax;
			}else{
				incrtax = Double.valueOf(tax);
			}
			
			if(lreport.get("workmannum") != null && !lreport.get("workmannum").equals("")){
				int temp_workmannum = Integer.valueOf(lreport.get("workmannum").toString());
				if(workmannum != null && !workmannum.equals(""))
					incrworkmannum = Integer.valueOf(workmannum) - temp_workmannum;
			}else{
				incrworkmannum = Integer.valueOf(workmannum);
			}
		}
		properties.put("incrincome", incrincome);
		properties.put("incrprofit", incrprofit);
		properties.put("incrtax", incrtax);
		properties.put("incrworkmannum", incrworkmannum);
		
		long id = mySQLDBHelper.create("t_unit_monthreport", properties);
	
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String unitid = request.getParameter("unitid");
		String reportdate = request.getParameter("reportdate");
		String asset = request.getParameter("asset");
		String income = request.getParameter("income");
		String profit = request.getParameter("profit");
		String tax = request.getParameter("tax");
		String workmannum = request.getParameter("workmannum");
		
		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		
		Map properties = new HashMap();
		properties.put("unitid", unitid);
		properties.put("reportdate", reportdate);
		properties.put("asset", asset);
		properties.put("income", income);
		properties.put("profit", profit);
		properties.put("tax", tax);
		properties.put("workmannum", workmannum);
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		
Map lreport = mySQLDBHelper.retriveMapFromSQL("select * from t_unit_monthreport where reportdate='" + Common.lastMonth(reportdate) + "' and statusvalue<>0 and unitid=" + unitid);
		
		double incrincome = 0;
		double incrprofit = 0;
		double incrtax = 0;
		int incrworkmannum = 0;
		
		if(lreport != null){
			if(lreport.get("income") != null && !lreport.get("income").equals("")){
				double temp_income = Double.valueOf(lreport.get("income").toString());
				if(income != null && !income.equals(""))
					incrincome = Double.valueOf(income) - temp_income;
			}else{
				incrincome = Double.valueOf(income);
			}
			
			if(lreport.get("profit") != null && !lreport.get("profit").equals("")){
				double temp_profit = Double.valueOf(lreport.get("profit").toString());
				if(profit != null && !profit.equals(""))
					incrprofit = Double.valueOf(profit) - temp_profit;
			}else{
				incrprofit = Double.valueOf(profit);
			}
			
			if(lreport.get("tax") != null && !lreport.get("tax").equals("")){
				double temp_tax = Double.valueOf(lreport.get("tax").toString());
				if(tax != null && !tax.equals(""))
					incrtax = Double.valueOf(tax) - temp_tax;
			}else{
				incrtax = Double.valueOf(tax);
			}
			
			if(lreport.get("workmannum") != null && !lreport.get("workmannum").equals("")){
				int temp_workmannum = Integer.valueOf(lreport.get("workmannum").toString());
				if(workmannum != null && !workmannum.equals(""))
					incrworkmannum = Integer.valueOf(workmannum) - temp_workmannum;
			}else{
				incrworkmannum = Integer.valueOf(workmannum);
			}
		}
		properties.put("incrincome", incrincome);
		properties.put("incrprofit", incrprofit);
		properties.put("incrtax", incrtax);
		properties.put("incrworkmannum", incrworkmannum);
		
		return mySQLDBHelper.update("t_unit_monthreport", properties, "id=" + id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String unitname = request.getParameter("unitname");
		String reportdate = request.getParameter("reportdate");
	
		String limit = "10";//request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null
				&& !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
		}
		else
			return null;
		
		String sql = "select m.* ,b.unitname from t_unit_monthreport m,t_build_unit b,t_build_basis bb where m.unitid=b.id and b.buildid=bb.id and m.statusvalue<>0 and bb.statusvalue<>0 and b.statusvalue<>0";
		if(userType.equals("1"))
			sql += " and bb.rununitid='" + unitid + "'";
		if (unitname != null && !unitname.trim().equals(""))
			sql += " and b.unitname like '%" + unitname + "%'";
		if (reportdate != null && !reportdate.trim().equals(""))
			sql += " and m.reportdate='" + reportdate + "'";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List all(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
		}
		else
			return null;
		
		String sql = "select m.* ,b.unitname from t_unit_monthreport m,t_build_unit b,t_build_basis bb where m.unitid=b.id and b.buildid=bb.id and m.statusvalue<>0 and bb.statusvalue<>0 and b.statusvalue<>0";
		if(userType.equals("1"))
			sql += " and bb.rununitid='" + unitid + "'";
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "select m.* ,b.unitname from t_unit_monthreport m,t_build_unit b,t_build_basis bb where m.unitid=b.id and b.buildid=bb.id and m.statusvalue<>0 and bb.statusvalue<>0 and b.statusvalue<>0 and m.id=" + id;
		Map result = mySQLDBHelper.retriveBySQL(sql,false,0,0);

		return result;
	}

	

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
			
		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_unit_monthreport", temp, "id=" + id);
		
	}
}
