package cn.com.xtgl.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

public class ScheduleJob {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	private MySQLDBHelper mysqlDBHelperTask;
	@Scheduled(cron="10 0 0 1 * ?")
	public boolean count() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		String date = sdf.format(new Date());
		// 去t_count_flag查询当月统计数据的情况，如果没有数据，则进行统计，如果有数据，需要判断结算的过程是否都正确完成。
		Map result = mysqlDBHelperTask.retriveMapFromSQL("select * from t_count_flag where countdate='" + date + "'");
		if (result != null) {

			// 总共2次结算，分别为河北区和每个楼宇的基础信息结算，里面已经计算了税收的情况，但是这块需要根据地税和国税的情况重新改一下，因为统计的是每个月的情况，也就是应该是地税+国税；另外，在这两张表里，分别本月增加留区税收和本月留区税收两个字段。
			int tjhb_basis = Integer.valueOf(result.get("tjhb_basis").toString());
			int build_basis = Integer.valueOf(result.get("build_basis").toString());
			// int tjhb_benefit =
			// Integer.valueOf(result.get("tjhb_benefit").toString());
			// int build_benefit =
			// Integer.valueOf(result.get("build_benefit").toString());
			// int tjhb_industry =
			// Integer.valueOf(result.get("tjhb_industry").toString());
			// int build_industry =
			// Integer.valueOf(result.get("build_industry").toString());

			// 如果标志位是1，则删除这笔计算，重新计算，以下所有的过程相同。
			// 分别
			if (tjhb_basis == 1) {
				deleteAllCount(1, date);
				tjhb_basis_count(date);
			}
			if (build_basis == 1) {
				deleteAllCount(2, date);
				build_basis_count(date);
			}
			/*
			 * if (tjhb_benefit == 1) { deleteAllCount(3, date);
			 * tjhb_industry_count(date); } if (build_benefit == 1) {
			 * deleteAllCount(4, date); build_industry_count(date); } if
			 * (tjhb_industry == 1) { deleteAllCount(5, date);
			 * tjhb_benefit_count(date); } if (build_industry == 1) {
			 * deleteAllCount(6, date); build_benefit_count(date); }
			 */

		} else {
			// 主要修改以下两个方法里的税收和留区税收的计算。
			tjhb_basis_count(date);
			build_basis_count(date);
			// tjhb_industry_count(date);
			// build_industry_count(date);
			// tjhb_benefit_count(date);
			// build_benefit_count(date);
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
			Map dishuicount = mysqlDBHelperTask.retriveMapFromSQL(
					"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ time + "'");
			Map guoshuicount = mysqlDBHelperTask.retriveMapFromSQL(
					"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ time + "'");
			Map dishuicountlast = mysqlDBHelperTask.retriveMapFromSQL(
					"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ Common.lastMonth(time) + "' ");
			Map guoshuicountlast = mysqlDBHelperTask.retriveMapFromSQL(
					"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
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
			double countstayareatax = 0;
			if (dishuicount != null) {
				double dscount = 0;
				dscount += Double.valueOf(dishuicount.get("salesTax").toString()) * 0.5;
				dscount += Double.valueOf(dishuicount.get("bisnessTax").toString()) * 0.3;
				dscount += Double.valueOf(dishuicount.get("personalTax").toString()) * 0.2;
				dscount += Double.valueOf(dishuicount.get("landaddTax").toString()) * 0.5;
				dscount += Double.valueOf(dishuicount.get("addTax").toString()) * 0.25;
				dscount += Double.valueOf(dishuicount.get("constractionTax").toString());
				dscount += Double.valueOf(dishuicount.get("buildTax").toString());
				dscount += Double.valueOf(dishuicount.get("stampTax").toString());
				dscount += Double.valueOf(dishuicount.get("landuseTax").toString());
				dscount += Double.valueOf(dishuicount.get("vesselTax").toString());
				dscount += Double.valueOf(dishuicount.get("deedTax").toString());
				dscount += Double.valueOf(dishuicount.get("eduTax").toString());
				dscount += Double.valueOf(dishuicount.get("localeduTax").toString());
				countstayareatax += dscount;
			}
			if (guoshuicount != null) {
				double gscount = 0;
				gscount += Double.valueOf(guoshuicount.get("addTax").toString()) * 0.25;
				gscount += Double.valueOf(guoshuicount.get("bisnessTax").toString()) * 0.3;
				gscount += Double.valueOf(guoshuicount.get("personTax").toString()) * 0.2;
				gscount += Double.valueOf(guoshuicount.get("constractionTax").toString());
				countstayareatax += gscount;
			}
			double incountstayareataxlast = 0;
			if (dishuicountlast != null) {
				double dscountlast = 0;
				dscountlast += Double.valueOf(dishuicountlast.get("salesTax").toString()) * 0.5;
				dscountlast += Double.valueOf(dishuicountlast.get("bisnessTax").toString()) * 0.3;
				dscountlast += Double.valueOf(dishuicountlast.get("personalTax").toString()) * 0.2;
				dscountlast += Double.valueOf(dishuicountlast.get("landaddTax").toString()) * 0.5;
				dscountlast += Double.valueOf(dishuicountlast.get("addTax").toString()) * 0.25;
				dscountlast += Double.valueOf(dishuicountlast.get("constractionTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("buildTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("stampTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("landuseTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("vesselTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("deedTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("eduTax").toString());
				dscountlast += Double.valueOf(dishuicountlast.get("localeduTax").toString());
				incountstayareataxlast += dscountlast;
			}
			if (guoshuicountlast != null) {
				double gscountlast = 0;
				gscountlast += Double.valueOf(guoshuicountlast.get("addTax").toString()) * 0.25;
				gscountlast += Double.valueOf(guoshuicountlast.get("bisnessTax").toString()) * 0.3;
				gscountlast += Double.valueOf(guoshuicountlast.get("personTax").toString()) * 0.2;
				gscountlast += Double.valueOf(guoshuicountlast.get("constractionTax").toString());
				incountstayareataxlast += gscountlast;
			}

			properties.put("countstayareatax", countstayareatax);
			properties.put("incountstayareatax", countstayareatax - incountstayareataxlast);
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
					Map dishuicount = mysqlDBHelperTask.retriveMapFromSQL(
							"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ time + "' and b.buildid=" + build.get("id"));
					Map guoshuicount = mysqlDBHelperTask.retriveMapFromSQL(
							"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ time + "' and b.buildid=" + build.get("id"));
					Map dishuicountlast = mysqlDBHelperTask.retriveMapFromSQL(
							"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ Common.lastMonth(time) + "' and b.buildid=" + build.get("id"));
					Map guoshuicountlast = mysqlDBHelperTask.retriveMapFromSQL(
							"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ Common.lastMonth(time) + "' and b.buildid=" + build.get("id"));

					Map properties = new HashMap();
					properties.put("countmonth", time);
					properties.put("buildid", build.get("id"));
					double countstayareatax = 0;
					if (dishuicount != null) {
						double dscount = 0;
						dscount += Double.valueOf(dishuicount.get("salesTax").toString()) * 0.5;
						dscount += Double.valueOf(dishuicount.get("bisnessTax").toString()) * 0.3;
						dscount += Double.valueOf(dishuicount.get("personalTax").toString()) * 0.2;
						dscount += Double.valueOf(dishuicount.get("landaddTax").toString()) * 0.5;
						dscount += Double.valueOf(dishuicount.get("addTax").toString()) * 0.25;
						dscount += Double.valueOf(dishuicount.get("constractionTax").toString());
						dscount += Double.valueOf(dishuicount.get("buildTax").toString());
						dscount += Double.valueOf(dishuicount.get("stampTax").toString());
						dscount += Double.valueOf(dishuicount.get("landuseTax").toString());
						dscount += Double.valueOf(dishuicount.get("vesselTax").toString());
						dscount += Double.valueOf(dishuicount.get("deedTax").toString());
						dscount += Double.valueOf(dishuicount.get("eduTax").toString());
						dscount += Double.valueOf(dishuicount.get("localeduTax").toString());
						countstayareatax += dscount;
					}
					if (guoshuicount != null) {
						double gscount = 0;
						gscount += Double.valueOf(guoshuicount.get("addTax").toString()) * 0.25;
						gscount += Double.valueOf(guoshuicount.get("bisnessTax").toString()) * 0.3;
						gscount += Double.valueOf(guoshuicount.get("personTax").toString()) * 0.2;
						gscount += Double.valueOf(guoshuicount.get("constractionTax").toString());
						countstayareatax += gscount;
					}
					double incountstayareataxlast = 0;
					if (dishuicountlast != null) {
						double dscountlast = 0;
						dscountlast += Double.valueOf(dishuicountlast.get("salesTax").toString()) * 0.5;
						dscountlast += Double.valueOf(dishuicountlast.get("bisnessTax").toString()) * 0.3;
						dscountlast += Double.valueOf(dishuicountlast.get("personalTax").toString()) * 0.2;
						dscountlast += Double.valueOf(dishuicountlast.get("landaddTax").toString()) * 0.5;
						dscountlast += Double.valueOf(dishuicountlast.get("addTax").toString()) * 0.25;
						dscountlast += Double.valueOf(dishuicountlast.get("constractionTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("buildTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("stampTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("landuseTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("vesselTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("deedTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("eduTax").toString());
						dscountlast += Double.valueOf(dishuicountlast.get("localeduTax").toString());
						incountstayareataxlast += dscountlast;
					}
					if (guoshuicountlast != null) {
						double gscountlast = 0;
						gscountlast += Double.valueOf(guoshuicountlast.get("addTax").toString()) * 0.25;
						gscountlast += Double.valueOf(guoshuicountlast.get("bisnessTax").toString()) * 0.3;
						gscountlast += Double.valueOf(guoshuicountlast.get("personTax").toString()) * 0.2;
						gscountlast += Double.valueOf(guoshuicountlast.get("constractionTax").toString());
						incountstayareataxlast += gscountlast;
					}

					properties.put("countstayareatax", countstayareatax);
					properties.put("incountstayareatax", countstayareatax - incountstayareataxlast);
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
}
