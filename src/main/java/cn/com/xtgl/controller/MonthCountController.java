package cn.com.xtgl.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

@Controller
@RequestMapping("/month_count")
public class MonthCountController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	private MySQLDBHelper mysqlDBHelperTask;

	public MySQLDBHelper getMysqlDBHelperTask() {
		return mysqlDBHelperTask;
	}

	public void setMysqlDBHelperTask(MySQLDBHelper mysqlDBHelperTask) {
		this.mysqlDBHelperTask = mysqlDBHelperTask;
	}

	@RequestMapping(value = "/count", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean count() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		String date = sdf.format(new Date());
		Map result = mysqlDBHelperTask.retriveMapFromSQL("select * from t_count_flag where countdate='" + date + "'");
		if (result != null) {
			int tjhb_basis = Integer.valueOf(result.get("tjhb_basis").toString());
			int build_basis = Integer.valueOf(result.get("build_basis").toString());
			int tjhb_benefit = Integer.valueOf(result.get("tjhb_benefit").toString());
			int build_benefit = Integer.valueOf(result.get("build_benefit").toString());
			int tjhb_industry = Integer.valueOf(result.get("tjhb_industry").toString());
			int build_industry = Integer.valueOf(result.get("build_industry").toString());
			if (tjhb_basis == 1) {
				deleteAllCount(1, date);
				tjhb_basis_count(date);
			}
			if (build_basis == 1) {
				deleteAllCount(2, date);
				build_basis_count(date);
			}
			if (tjhb_benefit == 1) {
				deleteAllCount(3, date);
				tjhb_industry_count(date);
			}
			if (build_benefit == 1) {
				deleteAllCount(4, date);
				build_industry_count(date);
			}
			if (tjhb_industry == 1) {
				deleteAllCount(5, date);
				tjhb_benefit_count(date);
			}
			if (build_industry == 1) {
				deleteAllCount(6, date);
				build_benefit_count(date);
			}

		} else {
			tjhb_basis_count(date);
			build_basis_count(date);
			tjhb_industry_count(date);
			build_industry_count(date);
			tjhb_benefit_count(date);
			build_benefit_count(date);
		}
		return true;
	}

	private void deleteAllCount(int flag, String time) {
		if (flag == 1) {
			mysqlDBHelperTask.delete("t_tjhb_basis_count", "countmonth='" + time + "'");
		} else if (flag == 2) {
			mysqlDBHelperTask.delete("t_build_basis_count", "countmonth='" + time + "'");
		} else if (flag == 3) {
			mysqlDBHelperTask.delete("t_tjhb_industry_count", "countdate='" + time + "'");
		} else if (flag == 4) {
			mysqlDBHelperTask.delete("t_build_industry_count", "countdate='" + time + "'");
		} else if (flag == 5) {
			mysqlDBHelperTask.delete("t_tjhb_benefit_count", "countdate='" + time + "'");
		} else if (flag == 6) {
			mysqlDBHelperTask.delete("t_build_benefit_count", "countdate='" + time + "'");
		}
	}

	private void tjhb_basis_count(String time) {
		try {
			Map builds = mysqlDBHelperTask.retriveMapFromSQL(
					"select count(id) num,sum(buildarea) buildarea from t_build_basis where statusvalue<>0");// 楼宇总数
			List units = mysqlDBHelperTask.retriveBySQL("select id from t_build_unit where statusvalue<>0");// 入驻企业总数

			Map reportMap = mysqlDBHelperTask.retriveMapFromSQL(
					"select sum(emptyarea) emptyarea ,sum(rentarea) rentarea ,sum(incrbusinessnum) incrbusinessnum, sum(tax) taxtotalnum, sum(workmannum) workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
							+ time + "'");
			Map reportLastMap = mysqlDBHelperTask.retriveMapFromSQL(
					"select sum(emptyarea) emptyarea ,sum(rentarea) rentarea ,sum(incrbusinessnum) incrbusinessnum, sum(tax) taxtotalnum, sum(workmannum) workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
							+ Common.lastMonth(time) + "'");

			Map properties = new HashMap();
			properties.put("countmonth", time);
			if (builds != null) {
				if (builds.get("num") != null && !builds.get("num").equals(""))
					properties.put("buildnum", builds.get("num"));
				else
					properties.put("buildnum", 0);
			} else {
				return;
			}

			if (units != null) {
				properties.put("unitnum", units.size());
			} else {
				properties.put("unitnum", 0);
			}

			if (reportMap != null) {

				int lworkmannum = 0;
				double ltaxtotalnum = 0;
				double lrentarea = 0;

				if (reportLastMap != null) {
					if (reportLastMap.get("workmannum") != null && !reportLastMap.get("workmannum").equals(""))
						lworkmannum = Integer.valueOf(reportLastMap.get("workmannum").toString());
					if (reportLastMap.get("taxtotalnum") != null && !reportLastMap.get("taxtotalnum").equals(""))
						ltaxtotalnum = Double.valueOf(reportLastMap.get("taxtotalnum").toString());
					if (reportLastMap.get("rentarea") != null && !reportLastMap.get("rentarea").equals(""))
						lrentarea = Double.valueOf(reportLastMap.get("rentarea").toString());
				}

				if (reportMap.get("incrbusinessnum") != null && !reportMap.get("incrbusinessnum").equals(""))
					properties.put("incrunitnum", reportMap.get("incrbusinessnum"));
				else
					properties.put("incrunitnum", 0);

				if (reportMap.get("workmannum") != null && !reportMap.get("workmannum").equals("")) {
					int workman = Integer.valueOf(reportMap.get("workmannum").toString());
					properties.put("workmannum", workman);
					properties.put("incrworkmannum", workman - lworkmannum);
				} else {
					properties.put("workmannum", 0);
					properties.put("incrworkmannum", 0 - lworkmannum);
				}

				if (reportMap.get("taxtotalnum") != null && !reportMap.get("taxtotalnum").equals("")) {
					double taxtotalnum = Double.valueOf(reportMap.get("taxtotalnum").toString());
					properties.put("taxtotalnum", taxtotalnum);
					properties.put("taxnum", taxtotalnum - ltaxtotalnum);
				} else {
					properties.put("taxtotalnum", 0);
					properties.put("taxnum", 0 - ltaxtotalnum);
				}

				if (reportMap.get("emptyarea") != null && !reportMap.get("emptyarea").equals("")) {
					double emptyarea = Double.valueOf(reportMap.get("emptyarea").toString());
					double area = Double.valueOf(builds.get("buildarea").toString());
					properties.put("emptyarea", emptyarea);
					if (area != 0)
						properties.put("emptyrate", String.format("%.2f", emptyarea / area));
					else
						properties.put("emptyrate", 0);
				} else {
					properties.put("emptyarea", 0);
					properties.put("emptyrate", 0);
				}

				if (reportMap.get("rentarea") != null && !reportMap.get("rentarea").equals("")) {
					double rentarea = Double.valueOf(reportMap.get("rentarea").toString());
					properties.put("rentarea", rentarea);
					properties.put("incrrentarea", rentarea - lrentarea);
				} else {
					properties.put("rentarea", 0);
					properties.put("incrrentarea", 0 - lrentarea);
				}

			} else {
				return;
			}

			mysqlDBHelperTask.create("t_tjhb_basis_count", properties);
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_basis", 0);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				Map m = new HashMap();
				m.put("countdate", time);
				m.put("tjhb_basis", 0);
				m.put("build_basis", 1);
				m.put("tjhb_benefit", 1);
				m.put("build_benefit", 1);
				m.put("tjhb_industry", 1);
				m.put("build_industry", 1);
				m.put("createdate", date);

				mysqlDBHelperTask.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_basis", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_basis_count(String time) {
		try {
			List builds = mysqlDBHelperTask
					.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List units = mysqlDBHelperTask.retriveBySQL(
							"select id from t_build_unit where statusvalue<>0 and buildid=" + build.get("id"));// 入驻企业总数
					Map reportMap = mysqlDBHelperTask.retriveMapFromSQL(
							"select emptyarea ,rentarea ,incrbusinessnum, tax taxtotalnum, workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
									+ time + "' and buildid=" + build.get("id"));
					Map reportLastMap = mysqlDBHelperTask.retriveMapFromSQL(
							"select emptyarea ,rentarea ,incrbusinessnum, tax taxtotalnum, workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
									+ Common.lastMonth(time) + "' and buildid=" + build.get("id"));

					Map properties = new HashMap();
					properties.put("countmonth", time);
					properties.put("buildid", build.get("id"));

					if (units != null) {
						properties.put("unitnum", units.size());
					} else {
						properties.put("unitnum", 0);
					}

					if (reportMap != null) {

						int lworkmannum = 0;
						double ltaxtotalnum = 0;
						double lrentarea = 0;

						if (reportLastMap != null) {
							if (reportLastMap.get("workmannum") != null && !reportLastMap.get("workmannum").equals(""))
								lworkmannum = Integer.valueOf(reportLastMap.get("workmannum").toString());
							if (reportLastMap.get("taxtotalnum") != null
									&& !reportLastMap.get("taxtotalnum").equals(""))
								ltaxtotalnum = Double.valueOf(reportLastMap.get("taxtotalnum").toString());
							if (reportLastMap.get("rentarea") != null && !reportLastMap.get("rentarea").equals(""))
								lrentarea = Double.valueOf(reportLastMap.get("rentarea").toString());
						}

						if (reportMap.get("incrbusinessnum") != null && !reportMap.get("incrbusinessnum").equals(""))
							properties.put("incrunitnum", reportMap.get("incrbusinessnum"));
						else
							properties.put("incrunitnum", 0);

						if (reportMap.get("workmannum") != null && !reportMap.get("workmannum").equals("")) {
							int workman = Integer.valueOf(reportMap.get("workmannum").toString());
							properties.put("workmannum", workman);
							properties.put("incrworkmannum", workman - lworkmannum);
						} else {
							properties.put("workmannum", 0);
							properties.put("incrworkmannum", 0 - lworkmannum);
						}

						if (reportMap.get("taxtotalnum") != null && !reportMap.get("taxtotalnum").equals("")) {
							double taxtotalnum = Double.valueOf(reportMap.get("taxtotalnum").toString());
							properties.put("taxtotalnum", taxtotalnum);
							properties.put("taxnum", taxtotalnum - ltaxtotalnum);
						} else {
							properties.put("taxtotalnum", 0);
							properties.put("taxnum", 0 - ltaxtotalnum);
						}

						if (reportMap.get("emptyarea") != null && !reportMap.get("emptyarea").equals("")) {
							double emptyarea = Double.valueOf(reportMap.get("emptyarea").toString());
							double area = Double.valueOf(build.get("buildarea").toString());
							properties.put("emptyarea", emptyarea);
							if (area != 0)
								properties.put("emptyrate", String.format("%.2f", emptyarea / area));
							else
								properties.put("emptyrate", 0);
						} else {
							properties.put("emptyarea", 0);
							properties.put("emptyrate", 0);
						}

						if (reportMap.get("rentarea") != null && !reportMap.get("rentarea").equals("")) {
							double rentarea = Double.valueOf(reportMap.get("rentarea").toString());
							properties.put("rentarea", rentarea);
							properties.put("incrrentarea", rentarea - lrentarea);
						} else {
							properties.put("rentarea", 0);
							properties.put("incrrentarea", 0 - lrentarea);
						}

					} else {
						continue;
					}

					mysqlDBHelperTask.create("t_build_basis_count", properties);

					Map result = mysqlDBHelperTask
							.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
					if (result != null) {
						result.put("build_basis", 0);
						mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = sdf.format(new Date());

						Map m = new HashMap();
						m.put("countdate", time);
						m.put("tjhb_basis", 1);
						m.put("build_basis", 0);
						m.put("tjhb_benefit", 1);
						m.put("build_benefit", 1);
						m.put("tjhb_industry", 1);
						m.put("build_industry", 1);
						m.put("createdate", date);

						mysqlDBHelperTask.create("t_count_flag", m);
					}
				}
			} else
				return;
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_basis", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}

	}

	private void tjhb_industry_count(String time) {
		try {
			List result = mysqlDBHelperTask.retriveBySQL(
					"select u.services, sum(m.income) income,sum(m.tax) tax,sum(m.workmannum) workmannum ,sum(m.incrincome) incrincome,sum(m.incrtax) incrtax,sum(m.incrworkmannum) incrworkmannum from t_unit_monthreport m,t_build_unit u where m.unitid=u.id and u.statusvalue<>0 and m.statusvalue<>0 and m.reportdate='"
							+ time + "' GROUP BY u.services");
			if (result != null && result.size() > 0) {
				for (int i = 0, l = result.size(); i < l; i++) {
					Map r = (Map) result.get(i);
					r.put("countdate", time);
					mysqlDBHelperTask.create("t_tjhb_industry_count", r);
				}
			}

			Map result1 = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("tjhb_industry", 0);
				mysqlDBHelperTask.update("t_count_flag", result1, "countdate='" + time + "'");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				Map m = new HashMap();
				m.put("countdate", time);
				m.put("tjhb_basis", 1);
				m.put("build_basis", 1);
				m.put("tjhb_benefit", 1);
				m.put("build_benefit", 1);
				m.put("tjhb_industry", 0);
				m.put("build_industry", 1);
				m.put("createdate", date);

				mysqlDBHelperTask.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_industry", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_industry_count(String time) {
		try {
			List builds = mysqlDBHelperTask
					.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List result = mysqlDBHelperTask.retriveBySQL(
							"select u.services, sum(m.income) income,sum(m.tax) tax,sum(m.workmannum) workmannum ,sum(m.incrincome) incrincome,sum(m.incrtax) incrtax,sum(m.incrworkmannum) incrworkmannum from t_unit_monthreport m,t_build_unit u where m.unitid=u.id and u.statusvalue<>0 and m.statusvalue<>0 and m.reportdate='"
									+ time + "' and u.buildid=" + build.get("id") + " GROUP BY u.services");
					if (result != null && result.size() > 0) {
						for (int j = 0, s = result.size(); j < s; j++) {
							Map r = (Map) result.get(j);
							r.put("countdate", time);
							mysqlDBHelperTask.create("t_build_industry_count", r);
						}
					}
				}
			}

			Map result1 = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("build_industry", 0);
				mysqlDBHelperTask.update("t_count_flag", result1, "countdate='" + time + "'");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				Map m = new HashMap();
				m.put("countdate", time);
				m.put("tjhb_basis", 1);
				m.put("build_basis", 1);
				m.put("tjhb_benefit", 1);
				m.put("build_benefit", 1);
				m.put("tjhb_industry", 1);
				m.put("build_industry", 0);
				m.put("createdate", date);

				mysqlDBHelperTask.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_industry", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_benefit_count(String time) {
		try {
			List builds = mysqlDBHelperTask
					.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List result = mysqlDBHelperTask.retriveBySQL(
							"select m1.income/m2.income incomerate ,m1.incrincome/m2.incrincome incrincomerate, m1.tax/m2.tax taxrate,m1.incrtax/m2.incrtax incrtaxrate,m1.workmannum/m2.workmannum workmanrate,m1.incrworkmannum/m2.incrworkmannum incrworkmanrate from t_build_monthreport m1,(select sum(income) income,sum(incrincome) incrincome,sum(tax) tax,sum(incrtax) incrtax,sum(workmannum) workmannum,sum(incrworkmannum) incrworkmannum from t_build_monthreport where reportdate='"
									+ time + "') m2 where m1.buildid=" + build.get("id") + " and m1.reportdate='" + time
									+ "'");
					if (result != null && result.size() > 0) {
						for (int j = 0, s = result.size(); j < s; j++) {
							Map r = (Map) result.get(j);
							r.put("countdate", time);
							r.put("buildid", build.get("id"));
							mysqlDBHelperTask.create("t_build_industry_count", r);
						}
					}
				}
			}
			Map result1 = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("build_benefit", 0);
				mysqlDBHelperTask.update("t_count_flag", result1, "countdate='" + time + "'");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				Map m = new HashMap();
				m.put("countdate", time);
				m.put("tjhb_basis", 1);
				m.put("build_basis", 1);
				m.put("tjhb_benefit", 1);
				m.put("build_benefit", 0);
				m.put("tjhb_industry", 1);
				m.put("build_industry", 1);
				m.put("createdate", date);

				mysqlDBHelperTask.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_benefit", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void tjhb_benefit_count(String time) {
		try {
			List result = mysqlDBHelperTask.retriveBySQL(
					"select m1.income/m2.income incomerate ,m1.incrincome/m2.incrincome incrincomerate, m1.tax/m2.tax taxrate,m1.incrtax/m2.incrtax incrtaxrate,m1.workmannum/m2.workmannum workmanrate,m1.incrworkmannum/m2.incrworkmannum incrworkmanrate from t_build_monthreport m1,(select sum(income) income,sum(incrincome) incrincome,sum(tax) tax,sum(incrtax) incrtax,sum(workmannum) workmannum,sum(incrworkmannum) incrworkmannum from t_build_monthreport where reportdate='"
							+ time + "') m2 where m1.reportdate='" + time + "'");
			if (result != null && result.size() > 0) {
				for (int j = 0, s = result.size(); j < s; j++) {
					Map r = (Map) result.get(j);
					r.put("countdate", time);
					mysqlDBHelperTask.create("t_tjhb_industry_count", r);
				}
			}
			Map result1 = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("tjhb_benefit", 0);
				mysqlDBHelperTask.update("t_count_flag", result1, "countdate='" + time + "'");
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());

				Map m = new HashMap();
				m.put("countdate", time);
				m.put("tjhb_basis", 1);
				m.put("build_basis", 1);
				m.put("tjhb_benefit", 0);
				m.put("build_benefit", 1);
				m.put("tjhb_industry", 1);
				m.put("build_industry", 1);
				m.put("createdate", date);

				mysqlDBHelperTask.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mysqlDBHelperTask
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_benefit", 1);
				mysqlDBHelperTask.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	@RequestMapping(value = "/tjhb_basis", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_basis(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String page = request.getParameter("page");
		int startLine = 0;
		int limit = 10;
		if (page != null && !page.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * 10;
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		String role = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		} else
			return null;

		String sql = "SELECT * from t_tjhb_basis_count where 1=1";
		if (countdate != null && !countdate.trim().equals(""))
			sql += " and countmonth='" + countdate + "'";

		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, limit);
		return result;
	}

	@RequestMapping(value = "/build_basis", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_basis(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String buildname = request.getParameter("buildname");
		String page = request.getParameter("page");
		int startLine = 0;
		int limit = 10;
		if (page != null && !page.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * 10;
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		String role = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		} else
			return null;

		String sql = "SELECT b.*,bb.buildname from t_build_basis_count b,t_build_basis bb where b.buildid=bb.id";
		if (countdate != null && !countdate.trim().equals(""))
			sql += " and b.countmonth='" + countdate + "'";
		if (buildname != null && !buildname.trim().equals(""))
			sql += " and bb.buildname like '%" + buildname + "%'";

		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, limit);
		return result;
	}

	@RequestMapping(value = "/tjhb_benefit", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_benefit(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String page = request.getParameter("page");
		int startLine = 0;
		int limit = 10;
		if (page != null && !page.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * 10;
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		String role = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		} else
			return null;

		String sql = "SELECT * from t_tjhb_benefit_count where 1=1";
		if (countdate != null && !countdate.trim().equals(""))
			sql += " and countdate='" + countdate + "'";

		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, limit);
		return result;
	}

	@RequestMapping(value = "/build_benefit", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_benefit(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String buildname = request.getParameter("buildname");
		String page = request.getParameter("page");
		int startLine = 0;
		int limit = 10;
		if (page != null && !page.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * 10;
		}

		HttpSession session = request.getSession();
		String userType = "";
		String unitid = "";
		String role = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		} else
			return null;

		String sql = "SELECT b.*,bb.buildname from t_build_benefit_count b,t_build_basis bb where b.buildid=bb.id";
		if (countdate != null && !countdate.trim().equals(""))
			sql += " and b.countdate='" + countdate + "'";
		if (buildname != null && !buildname.trim().equals(""))
			sql += " and bb.buildname like '%" + buildname + "%'";

		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, limit);
		return result;
	}

	@RequestMapping(value = "/tjhb_industry", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_industry(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");

		String taxSQL = "select * from t_tjhb_industry_count where services in (select s.services from (select * from t_tjhb_industry_count where countdate='"
				+ countdate + "' group by services order by incrtax desc limit 0,5) s ) order by countdate desc";

		String incomeSQL = "select * from t_tjhb_industry_count where services in (select s.services from (select * from t_tjhb_industry_count where countdate='"
				+ countdate + "' group by services order by incrincome desc limit 0,5) s ) order by countdate desc";

		String workmannumSQL = "select * from t_tjhb_industry_count where services in (select s.services from (select * from t_tjhb_industry_count where countdate='"
				+ countdate + "' group by services order by incrworkmannum desc limit 0,5) s ) order by countdate desc";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);
		List incomeResult = mySQLDBHelper.retriveBySQL(incomeSQL);
		List workmannumResult = mySQLDBHelper.retriveBySQL(workmannumSQL);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Calendar c = Common.oneYearMonth(countdate);

		String[] month = new String[12];
		for (int m = 0; m < 12; m++) {
			month[m] = sdf.format(c.getTime());
			c.add(Calendar.MONTH, 1);
		}

		c.add(Calendar.MONTH, -12);

		String[] taxServices = {"","","","",""};
		double[][] taxData = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);
				String countDate = taxMap.get("countdate").toString();
				String services = taxMap.get("services").toString();
				String incrtax = taxMap.get("incrtax").toString();

				// 前五名行业的赋值操作
				if (taxServices[0] != null && !taxServices[0].equals("")) {
					if (!services.equals(taxServices[count])) {
						taxServices[count + 1] = services;
						count++;
					}
				} else {
					taxServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrtax != null && !incrtax.equals(""))
					taxData[count][dis] = Double.valueOf(incrtax);

			}
		}

		String[] incomeServices = new String[5];
		double[][] incomeData = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (incomeResult != null) {
			int count = 0;
			for (int i = 0, l = incomeResult.size(); i < l; i++) {
				Map incomeMap = (Map) incomeResult.get(i);
				String countDate = incomeMap.get("countdate").toString();
				String services = incomeMap.get("services").toString();
				String incrincome = incomeMap.get("incrincome").toString();

				// 前五名行业的赋值操作
				if (incomeServices[0] != null && !incomeServices[0].equals("")) {
					if (!services.equals(incomeServices[count])) {
						incomeServices[count + 1] = services;
						count++;
					}
				} else {
					incomeServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrincome != null && !incrincome.equals(""))
					incomeData[count][dis] = Double.valueOf(incrincome);

			}
		}

		String[] workmannumServices = new String[5];
		double[][] workmannumDate = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (workmannumResult != null) {
			int count = 0;
			for (int i = 0, l = workmannumResult.size(); i < l; i++) {
				Map workmannumMap = (Map) workmannumResult.get(i);
				String countDate = workmannumMap.get("countdate").toString();
				String services = workmannumMap.get("services").toString();
				String incrworkmannum = workmannumMap.get("incrworkmannum").toString();

				// 前五名行业的赋值操作
				if (workmannumServices[0] != null && !workmannumServices[0].equals("")) {
					if (!services.equals(workmannumServices[count])) {
						workmannumServices[count + 1] = services;
						count++;
					}
				} else {
					workmannumServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrworkmannum != null && !incrworkmannum.equals(""))
					workmannumDate[count][dis] = Double.valueOf(incrworkmannum);

			}
		}

		Map result = new HashMap();
		result.put("months", month);
		result.put("taxServices", taxServices);
		result.put("taxData", taxData);
		result.put("incomeServices", incomeServices);
		result.put("incomeData", incomeData);
		result.put("workmannumServices", workmannumServices);
		result.put("workmannumDate", workmannumDate);
		return result;
	}

	@RequestMapping(value = "/build_industry", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_industry(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String buildid = request.getParameter("buildid");

		String taxSQL = "select services,incrtax,countdate from t_build_industry_count where services in (select s.services from (select * from t_build_industry_count where countdate='"
				+ countdate + "' and buildid=" + buildid
				+ " group by services order by incrtax desc limit 0,5) s ) and buildid=" + buildid
				+ " order by countdate desc";

		String incomeSQL = "select services,incrincome,countdate from t_build_industry_count where services in (select s.services from (select * from t_build_industry_count where countdate='"
				+ countdate + "' and buildid=" + buildid
				+ " group by services order by incrincome desc limit 0,5) s ) and buildid=" + buildid
				+ " order by countdate desc";

		String workmannumSQL = "select services,incrworkmannum,countdate from t_build_industry_count where services in (select s.services from (select * from t_build_industry_count where countdate='"
				+ countdate + "' and buildid=" + buildid
				+ " group by services order by incrworkmannum desc limit 0,5) s ) and buildid=" + buildid
				+ " order by countdate desc";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);
		List incomeResult = mySQLDBHelper.retriveBySQL(incomeSQL);
		List workmannumResult = mySQLDBHelper.retriveBySQL(workmannumSQL);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Calendar c = Common.oneYearMonth(countdate);

		String[] month =  new String[12];
		for (int m = 0; m < 12; m++) {
			month[m] = sdf.format(c.getTime());
			c.add(Calendar.MONTH, 1);
		}

		c.add(Calendar.MONTH, -12);

		String[] taxServices = {"","","","",""};
		double[][] taxData = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);
				String countDate = taxMap.get("countdate").toString();
				String services = taxMap.get("services").toString();
				String incrtax = taxMap.get("incrtax").toString();

				// 前五名行业的赋值操作
				if (taxServices[0] != null && !taxServices[0].equals("")) {
					if (!services.equals(taxServices[count])) {
						taxServices[count + 1] = services;
						count++;
					}
				} else {
					taxServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrtax != null && !incrtax.equals(""))
					taxData[count][dis] = Double.valueOf(incrtax);

			}
		}

		String[] incomeServices = new String[5];
		double[][] incomeData = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (incomeResult != null) {
			int count = 0;
			for (int i = 0, l = incomeResult.size(); i < l; i++) {
				Map incomeMap = (Map) incomeResult.get(i);
				String countDate = incomeMap.get("countdate").toString();
				String services = incomeMap.get("services").toString();
				String incrincome = incomeMap.get("incrincome").toString();

				// 前五名行业的赋值操作
				if (incomeServices[0] != null && !incomeServices[0].equals("")) {
					if (!services.equals(incomeServices[count])) {
						incomeServices[count + 1] = services;
						count++;
					}
				} else {
					incomeServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrincome != null && !incrincome.equals(""))
					incomeData[count][dis] = Double.valueOf(incrincome);

			}
		}

		String[] workmannumServices = new String[5];
		double[][] workmannumDate = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		if (workmannumResult != null) {
			int count = 0;
			for (int i = 0, l = workmannumResult.size(); i < l; i++) {
				Map workmannumMap = (Map) workmannumResult.get(i);
				String countDate = workmannumMap.get("countdate").toString();
				String services = workmannumMap.get("services").toString();
				String incrworkmannum = workmannumMap.get("incrworkmannum").toString();

				// 前五名行业的赋值操作
				if (workmannumServices[0] != null && !workmannumServices[0].equals("")) {
					if (!services.equals(workmannumServices[count])) {
						workmannumServices[count + 1] = services;
						count++;
					}
				} else {
					workmannumServices[0] = services;
				}

				// 12个月里每个月增加的税收的赋值操作
				int dis = Common.monthDis(sdf.format(c.getTime()), countDate);
				if (incrworkmannum != null && !incrworkmannum.equals(""))
					workmannumDate[count][dis] = Double.valueOf(incrworkmannum);

			}
		}

		Map result = new HashMap();
		result.put("months", month);
		result.put("taxServices", taxServices);
		result.put("taxData", taxData);
		result.put("incomeServices", incomeServices);
		result.put("incomeData", incomeData);
		result.put("workmannumServices", workmannumServices);
		result.put("workmannumDate", workmannumDate);
		return result;
	}
}
