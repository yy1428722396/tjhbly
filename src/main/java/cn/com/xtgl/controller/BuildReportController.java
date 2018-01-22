package cn.com.xtgl.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/build_report")
public class BuildReportController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String buildid = request.getParameter("buildid");
		String reportdate = request.getParameter("reportdate");
		String emptyarea = request.getParameter("emptyarea");
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
		properties.put("buildid", buildid);
		properties.put("reportdate", reportdate);
		properties.put("emptyarea", emptyarea);
		properties.put("rentarea", emptyarea);
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
				+ Common.lastMonth(reportdate) + "' and statusvalue<>0 and buildid=" + buildid);

		double incrincome = 0;
		double incrprofit = 0;
		double incrtax = 0;
		int incrworkmannum = 0;

		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		
		if (lreport != null) {
			if (lreport.get("income") != null && !lreport.get("income").equals("")) {
				double temp_income = Double.valueOf(lreport.get("income").toString());
				if (income != null && !income.equals(""))
					incrincome = Double.valueOf(income) - temp_income;
			} else {
				if (income != null && !income.equals(""))
					incrincome = Double.valueOf(income);
				else
					incrincome = 0;
			}

			if (lreport.get("profit") != null && !lreport.get("profit").equals("")) {
				double temp_profit = Double.valueOf(lreport.get("profit").toString());
				if (profit != null && !profit.equals(""))
					incrprofit = Double.valueOf(profit) - temp_profit;
			} else {
				
				if (profit != null && !profit.equals(""))
					incrprofit = Double.valueOf(profit);
				else
					incrprofit = 0;
			}

			if (lreport.get("tax") != null && !lreport.get("tax").equals("")) {
				double temp_tax = Double.valueOf(lreport.get("tax").toString());
				if (tax != null && !tax.equals(""))
					incrtax = Double.valueOf(tax) - temp_tax;
			} else {
				
				if (tax != null && !tax.equals(""))
					incrtax = Double.valueOf(profit);
				else
					incrtax = 0;
			}

			if (lreport.get("workmannum") != null && !lreport.get("workmannum").equals("")) {
				int temp_workmannum = Integer.valueOf(lreport.get("workmannum").toString());
				if (workmannum != null && !workmannum.equals(""))
					incrworkmannum = Integer.valueOf(workmannum) - temp_workmannum;
			} else {
				
				if (workmannum != null && !workmannum.equals(""))
					incrworkmannum = Integer.valueOf(workmannum);
				else
					incrworkmannum = 0;
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
		mySQLDBHelper.update("t_build_basis", p, "id=" + buildid);

		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String buildid = request.getParameter("buildid");
		String reportdate = request.getParameter("reportdate");
		String emptyarea = request.getParameter("emptyarea");
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
		properties.put("buildid", buildid);
		properties.put("reportdate", reportdate);
		properties.put("emptyarea", emptyarea);
		properties.put("rentarea", emptyarea);
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
				+ Common.lastMonth(reportdate) + "' and statusvalue<>0 and buildid=" + buildid);

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

		mySQLDBHelper.update("t_build_monthreport", properties, "id=" + id);
		
		Map p = new HashMap();
		p.put("rentarea", emptyarea);
		p.put("emptyarea", emptyarea);
		mySQLDBHelper.update("t_build_basis", p, "id=" + buildid);
		
		return true;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String buildname = request.getParameter("buildname");
		String reportdate = request.getParameter("reportdate");

		String limit = "10";// request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
		} else
			return null;

		String sql = "select m.* ,b.buildname from t_build_monthreport m,t_build_basis b where m.buildid=b.id and m.statusvalue<>0 ";
		if (userType.equals("1"))
			sql += " and b.rununitid='" + unitid + "'";
		if (buildname != null && !buildname.trim().equals(""))
			sql += " and b.buildname like '%" + buildname + "%'";
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
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
		} else
			return null;

		String sql = "select m.* ,b.buildname from t_build_monthreport m,t_build_basis b where m.statusvalue=0 and b.id=m.buildid";
		if (userType.equals("1"))
			sql += " and b.rununitid='" + unitid + "'";
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "select m.* ,b.buildname,b.buildarea from t_build_monthreport m,t_build_basis b where m.statusvalue<>0 and b.id=m.buildid and m.id="
				+ id;
		Map result = mySQLDBHelper.retriveBySQL(sql, false, 0, 0);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");

		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_build_monthreport", temp, "id=" + id);

	}
	
	@RequestMapping(value = "/listshuiwu", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map listshuiwu(HttpServletRequest request) {
		String buildname = request.getParameter("buildname");
		String reportdate = request.getParameter("reportdate");

		String limit = "10";// request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
		} else
			return null;

		String sql = "select m.* from t_build_dishui m";
		
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}
}
