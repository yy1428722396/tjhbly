package cn.com.xtgl.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

	@RequestMapping(value = "/check_count", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean check_count(HttpServletRequest request) {
		String date = request.getParameter("date");
		Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + date + "'");
		if (result != null) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/count", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean count(HttpServletRequest request) {
		String date = request.getParameter("date");

		deleteAllCount(1, date);
		deleteAllCount(2, date);
		tjhb_basis_count(date);
		build_basis_count(date);
		deleteAllCount(7, date);
		qiye_count(date);
		return true;
	}
	/*
	 * @RequestMapping(value = "/count", method = RequestMethod.POST, produces =
	 * "application/json")
	 * 
	 * @ResponseBody public boolean count(HttpServletRequest request) { String
	 * date = request.getParameter("date"); // SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy.MM"); // String date = sdf.format(new Date()); //
	 * 去t_count_flag查询当月统计数据的情况，如果没有数据，则进行统计，如果有数据，需要判断结算的过程是否都正确完成。 Map result
	 * = mySQLDBHelper.
	 * retriveMapFromSQL("select * from t_count_flag where countdate='" + date +
	 * "'"); if (result != null) {
	 * 
	 * //
	 * 总共2次结算，分别为河北区和每个楼宇的基础信息结算，里面已经计算了税收的情况，但是这块需要根据地税和国税的情况重新改一下，因为统计的是每个月的情况
	 * ，也就是应该是地税+国税；另外，在这两张表里，分别本月增加留区税收和本月留区税收两个字段。 int tjhb_basis =
	 * Integer.valueOf(result.get("tjhb_basis").toString()); int build_basis =
	 * Integer.valueOf(result.get("build_basis").toString()); // int
	 * tjhb_benefit = // Integer.valueOf(result.get("tjhb_benefit").toString());
	 * // int build_benefit = //
	 * Integer.valueOf(result.get("build_benefit").toString()); // int
	 * tjhb_industry = //
	 * Integer.valueOf(result.get("tjhb_industry").toString()); // int
	 * build_industry = //
	 * Integer.valueOf(result.get("build_industry").toString());
	 * 
	 * // 如果标志位是1，则删除这笔计算，重新计算，以下所有的过程相同。 if (tjhb_basis == 1) {
	 * deleteAllCount(1, date); tjhb_basis_count(date); } if (build_basis == 1)
	 * { deleteAllCount(2, date); build_basis_count(date); }
	 * 
	 * //if (tjhb_benefit == 1) { // deleteAllCount(3, date); //
	 * tjhb_industry_count(date); //} //if (build_benefit == 1) { //
	 * deleteAllCount(4, date); // build_industry_count(date); //} //if
	 * (tjhb_industry == 1) { // deleteAllCount(5, date); //
	 * tjhb_benefit_count(date); //} //if (build_industry == 1) { //
	 * deleteAllCount(6, date); // build_benefit_count(date); //}
	 * 
	 * } else { // 主要修改以下两个方法里的税收和留区税收的计算。 tjhb_basis_count(date);
	 * build_basis_count(date); // tjhb_industry_count(date); //
	 * build_industry_count(date); // tjhb_benefit_count(date); //
	 * build_benefit_count(date); } return true; }
	 */

	private void deleteAllCount(int flag, String time) {
		if (flag == 1) {
			mySQLDBHelper.delete("t_tjhb_basis_count", "countmonth='" + time + "'");
		} else if (flag == 2) {
			mySQLDBHelper.delete("t_build_basis_count", "countmonth='" + time + "'");
		} else if (flag == 3) {
			mySQLDBHelper.delete("t_tjhb_industry_count", "countdate='" + time + "'");
		} else if (flag == 4) {
			mySQLDBHelper.delete("t_build_industry_count", "countdate='" + time + "'");
		} else if (flag == 5) {
			mySQLDBHelper.delete("t_tjhb_benefit_count", "countdate='" + time + "'");
		} else if (flag == 6) {
			mySQLDBHelper.delete("t_build_benefit_count", "countdate='" + time + "'");
		}else if (flag == 7) {
			mySQLDBHelper.delete("t_unitcount", "datatime='" + time + "'");
		}
	}

	private void tjhb_basis_count(String time) {
		try {
			Map builds = mySQLDBHelper.retriveMapFromSQL(
					"select count(id) num,sum(buildarea) buildarea from t_build_basis where statusvalue<>0");// 楼宇总数
			List units = mySQLDBHelper.retriveBySQL("select id from t_build_unit where statusvalue<>0");// 入驻企业总数

			Map reportMap = mySQLDBHelper.retriveMapFromSQL(
					"select sum(emptyarea) emptyarea ,sum(rentarea) rentarea ,sum(incrrentarea) incrrentarea ,sum(incrbusinessnum) incrbusinessnum, sum(tax) taxtotalnum, sum(workmannum) workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
							+ time + "'");
			Map reportLastMap = mySQLDBHelper.retriveMapFromSQL(
					"select sum(emptyarea) emptyarea ,sum(rentarea) rentarea ,sum(incrbusinessnum) incrbusinessnum, sum(tax) taxtotalnum, sum(workmannum) workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
							+ Common.lastMonth(time) + "'");
			Map dishuicount = mySQLDBHelper.retriveMapFromSQL(
					"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ time + "'");
			Map guoshuicount = mySQLDBHelper.retriveMapFromSQL(
					"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ time + "'");
			Map dishuicountlast = mySQLDBHelper.retriveMapFromSQL(
					"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ Common.lastMonth(time) + "' ");
			Map guoshuicountlast = mySQLDBHelper.retriveMapFromSQL(
					"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
							+ Common.lastMonth(time) + "'");
//			Map zongshuidi = mySQLDBHelper.retriveMapFromSQL(
//					"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_dishui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//							+ time + "'");
//			Map zongshuiguo = mySQLDBHelper.retriveMapFromSQL(
//					"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_guoshui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//							+ time + "'");
//			Map zongshuidilast = mySQLDBHelper.retriveMapFromSQL(
//					"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_dishui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//							+ Common.lastMonth(time) + "'");
//			Map zongshuiguolast = mySQLDBHelper.retriveMapFromSQL(
//					"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_guoshui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//							+ Common.lastMonth(time) + "'");
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
			if (reportMap != null) {

				int lworkmannum = 0;
				double dlworkmannum=0.0;
				double ltaxtotalnum = 0;
				double lrentarea = 0;

				if (reportLastMap != null) {
					if (reportLastMap.get("workmannum") != null && !reportLastMap.get("workmannum").equals(""))
						dlworkmannum = Double.valueOf(reportLastMap.get("workmannum").toString());
					lworkmannum=(int)dlworkmannum;
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
					double workman = Double.valueOf(reportMap.get("workmannum").toString());
					properties.put("workmannum", (int)workman);
					properties.put("incrworkmannum", workman - lworkmannum);
				} else {
					properties.put("workmannum", 0);
					properties.put("incrworkmannum", 0 - lworkmannum);
				}

				// if (reportMap.get("taxtotalnum") != null &&
				// !reportMap.get("taxtotalnum").equals("")) {
				// double taxtotalnum =
				// Double.valueOf(reportMap.get("taxtotalnum").toString());
				// properties.put("taxtotalnum", taxtotalnum);
				// properties.put("taxnum", taxtotalnum - ltaxtotalnum);
				// } else {
				// properties.put("taxtotalnum", 0);
				// properties.put("taxnum", 0 - ltaxtotalnum);
				// }

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

				if (reportMap.get("incrrentarea") != null && !reportMap.get("incrrentarea").equals("")) {
					double incrrentarea = Double.valueOf(reportMap.get("incrrentarea").toString());
					properties.put("incrrentarea", incrrentarea);
				} else {
					properties.put("incrrentarea", 0);
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
			double taxtotalnum = 0; //总税
			double countstayareatax = 0;// 留区税
			if (dishuicount != null) {
				double dscount = 0;
				if (dishuicount.get("salesTax") != null){
					dscount += Double.valueOf(dishuicount.get("salesTax").toString()) * 0.5;
					taxtotalnum+=Double.valueOf(dishuicount.get("salesTax").toString());
				}
				if (dishuicount.get("bisnessTax") != null){
					dscount += Double.valueOf(dishuicount.get("bisnessTax").toString()) * 0.3;
					taxtotalnum+=Double.valueOf(dishuicount.get("bisnessTax").toString());
				}
				if (dishuicount.get("personalTax") != null){
					dscount += Double.valueOf(dishuicount.get("personalTax").toString()) * 0.2;
					taxtotalnum+=Double.valueOf(dishuicount.get("personalTax").toString());
				}
				if (dishuicount.get("landaddTax") != null){
					dscount += Double.valueOf(dishuicount.get("landaddTax").toString()) * 0.5;
					taxtotalnum+=Double.valueOf(dishuicount.get("landaddTax").toString());
				}
				if (dishuicount.get("addTax") != null){
					dscount += Double.valueOf(dishuicount.get("addTax").toString()) * 0.25;
					taxtotalnum+=Double.valueOf(dishuicount.get("addTax").toString());
					
				}
				if (dishuicount.get("constractionTax") != null){
					dscount += Double.valueOf(dishuicount.get("constractionTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("constractionTax").toString());
				}
				if (dishuicount.get("buildTax") != null){
					dscount += Double.valueOf(dishuicount.get("buildTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("buildTax").toString());
				}
				if (dishuicount.get("stampTax") != null){
					dscount += Double.valueOf(dishuicount.get("stampTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("stampTax").toString());
				}
				if (dishuicount.get("landuseTax") != null){
					dscount += Double.valueOf(dishuicount.get("landuseTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("landuseTax").toString());
				}
				if (dishuicount.get("vesselTax") != null){
					dscount += Double.valueOf(dishuicount.get("vesselTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("vesselTax").toString());
				}
				if (dishuicount.get("deedTax") != null){
					dscount += Double.valueOf(dishuicount.get("deedTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("deedTax").toString());
				}
				if (dishuicount.get("eduTax") != null){
					dscount += Double.valueOf(dishuicount.get("eduTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("eduTax").toString());
				}
				if (dishuicount.get("localeduTax") != null){
					dscount += Double.valueOf(dishuicount.get("localeduTax").toString());
					taxtotalnum+=Double.valueOf(dishuicount.get("localeduTax").toString());
					
				}
					
				countstayareatax += dscount;
			}
			if (guoshuicount != null) {
				double gscount = 0;
				if (guoshuicount.get("addTax") != null){
					gscount += Double.valueOf(guoshuicount.get("addTax").toString()) * 0.25;
					taxtotalnum+=Double.valueOf(guoshuicount.get("addTax").toString());
				}
				if (guoshuicount.get("bisnessTax") != null){
					gscount += Double.valueOf(guoshuicount.get("bisnessTax").toString()) * 0.3;
					taxtotalnum+=Double.valueOf(guoshuicount.get("bisnessTax").toString());
				}
				if (guoshuicount.get("personTax") != null){
					gscount += Double.valueOf(guoshuicount.get("personTax").toString()) * 0.2;
					taxtotalnum+=Double.valueOf(guoshuicount.get("personTax").toString());
				}
				if (guoshuicount.get("constractionTax") != null){
					gscount += Double.valueOf(guoshuicount.get("constractionTax").toString());
					taxtotalnum+=Double.valueOf(guoshuicount.get("constractionTax").toString());
				}
					
				countstayareatax += gscount;
			}
			double taxtotalnumlast = 0;  //上月总税
			double incountstayareataxlast = 0; //上月留区税
			if (dishuicountlast != null) {
				double dscountlast = 0;
				if (dishuicountlast.get("salesTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("salesTax").toString()) * 0.5;
				    taxtotalnumlast+=Double.valueOf(dishuicountlast.get("salesTax").toString());
				}
				if (dishuicountlast.get("bisnessTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("bisnessTax").toString()) * 0.3;
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("bisnessTax").toString());
				}
				if (dishuicountlast.get("personalTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("personalTax").toString()) * 0.2;
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("personalTax").toString());
				}
				if (dishuicountlast.get("landaddTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("landaddTax").toString()) * 0.5;
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("landaddTax").toString());
				}
				if (dishuicountlast.get("addTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("addTax").toString()) * 0.25;
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("addTax").toString());
				}
				if (dishuicountlast.get("constractionTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("constractionTax").toString());
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("constractionTax").toString());
				}
				if (dishuicountlast.get("buildTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("buildTax").toString());
					 taxtotalnumlast+=Double.valueOf(dishuicountlast.get("buildTax").toString());
				}
				if (dishuicountlast.get("stampTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("stampTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("stampTax").toString());
				}
				if (dishuicountlast.get("landuseTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("landuseTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("landuseTax").toString());
				}
				if (dishuicountlast.get("vesselTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("vesselTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("vesselTax").toString());
				}
				if (dishuicountlast.get("deedTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("deedTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("deedTax").toString());
				}
				if (dishuicountlast.get("eduTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("eduTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("eduTax").toString());
				}
				if (dishuicountlast.get("localeduTax") != null){
					dscountlast += Double.valueOf(dishuicountlast.get("localeduTax").toString());
					taxtotalnumlast+=Double.valueOf(dishuicountlast.get("localeduTax").toString());
				}
					
				incountstayareataxlast += dscountlast;
			}
			if (guoshuicountlast != null) {
				double gscountlast = 0;
				if (guoshuicountlast.get("addTax") != null){
					gscountlast += Double.valueOf(guoshuicountlast.get("addTax").toString()) * 0.25;
					taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("addTax").toString()) ;
				}
				if (guoshuicountlast.get("bisnessTax") != null){
					gscountlast += Double.valueOf(guoshuicountlast.get("bisnessTax").toString()) * 0.3;
					taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("bisnessTax").toString()) ;
				}
				if (guoshuicountlast.get("personTax") != null){
					gscountlast += Double.valueOf(guoshuicountlast.get("personTax").toString()) * 0.2;
					taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("personTax").toString()) ;
				}
				if (guoshuicountlast.get("constractionTax") != null){
					gscountlast += Double.valueOf(guoshuicountlast.get("constractionTax").toString());
					taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("constractionTax").toString()) ;
				}
					
				incountstayareataxlast += gscountlast;
			}
            //留区税和留区税增量
			properties.put("countstayareatax", countstayareatax);
			properties.put("incountstayareatax", countstayareatax - incountstayareataxlast);
			// 总税收
			
			
			properties.put("taxtotalnum", taxtotalnum);
			properties.put("taxnum", taxtotalnum - taxtotalnumlast);
			if (units != null) {
				properties.put("unitnum", units.size());
			} else {
				properties.put("unitnum", 0);
			}

			

			mySQLDBHelper.create("t_tjhb_basis_count", properties);
			Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_basis", 0);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
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

				mySQLDBHelper.create("t_count_flag", m);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_basis", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_basis_count(String time) {
		try {
			List builds = mySQLDBHelper.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List units = mySQLDBHelper.retriveBySQL(
							"select id from t_build_unit where statusvalue<>0 and buildid=" + build.get("id"));// 入驻企业总数
					Map reportMap = mySQLDBHelper.retriveMapFromSQL(
							"select emptyarea ,rentarea,incrrentarea ,incrbusinessnum, tax taxtotalnum, workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
									+ time + "' and buildid=" + build.get("id"));
					Map reportLastMap = mySQLDBHelper.retriveMapFromSQL(
							"select emptyarea ,rentarea ,incrbusinessnum, tax taxtotalnum, workmannum from t_build_monthreport where statusvalue<>0 and reportdate='"
									+ Common.lastMonth(time) + "' and buildid=" + build.get("id"));
					Map dishuicount = mySQLDBHelper.retriveMapFromSQL(
							"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ time + "' and b.buildid=" + build.get("id"));
					Map guoshuicount = mySQLDBHelper.retriveMapFromSQL(
							"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ time + "' and b.buildid=" + build.get("id"));
					Map dishuicountlast = mySQLDBHelper.retriveMapFromSQL(
							"SELECT SUM(salesTax) salesTax,SUM(bisnessTax) bisnessTax,SUM(personalTax) personalTax,SUM(landaddTax) landaddTax,SUM(addTax) addTax,SUM(constractionTax) constractionTax,SUM(buildTax) buildTax,SUM(stampTax) stampTax,SUM(landuseTax) landuseTax,SUM(vesselTax) vesselTax,SUM(deedTax) deedTax,SUM(eduTax) eduTax,SUM(localeduTax) localeduTax FROM t_build_dishui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ Common.lastMonth(time) + "' and b.buildid=" + build.get("id"));
					Map guoshuicountlast = mySQLDBHelper.retriveMapFromSQL(
							"SELECT SUM(addTax) addTax,SUM(bisnessTax) bisnessTax,SUM(personTax) personTax,SUM(constractionTax) constractionTax FROM t_build_guoshui_count a,t_build_unit b WHERE a.socialCreCode = b.societycode and a.datatime='"
									+ Common.lastMonth(time) + "' and b.buildid=" + build.get("id"));
//					Map zongshuidi = mySQLDBHelper.retriveMapFromSQL(
//							"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_dishui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//									+ time + "' and b.buildid =" + build.get("id"));
//					Map zongshuiguo = mySQLDBHelper.retriveMapFromSQL(
//							"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_guoshui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//									+ time + "' and b.buildid =" + build.get("id"));
//					Map zongshuidilast = mySQLDBHelper.retriveMapFromSQL(
//							"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_dishui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//									+ Common.lastMonth(time) + "' and b.buildid =" + build.get("id"));
//					Map zongshuiguolast = mySQLDBHelper.retriveMapFromSQL(
//							"SELECT SUM(a.countMarney) as taxtotalnum,a.datatime,b.buildid from t_build_guoshui a ,t_build_unit b  where a.socialCreCode=b.societycode and a.datatime='"
//									+ Common.lastMonth(time) + "' and b.buildid =" + build.get("id"));
					Map properties = new HashMap();
					properties.put("countmonth", time);
					properties.put("buildid", build.get("id"));
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

						// if (reportMap.get("taxtotalnum") != null &&
						// !reportMap.get("taxtotalnum").equals("")) {
						// double taxtotalnum =
						// Double.valueOf(reportMap.get("taxtotalnum").toString());
						// properties.put("taxtotalnum", taxtotalnum);
						// properties.put("taxnum", taxtotalnum - ltaxtotalnum);
						// } else {
						// properties.put("taxtotalnum", 0);
						// properties.put("taxnum", 0 - ltaxtotalnum);
						// }

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

						if (reportMap.get("incrrentarea") != null && !reportMap.get("incrrentarea").equals("")) {
							double incrrentarea = Double.valueOf(reportMap.get("incrrentarea").toString());
							properties.put("incrrentarea", incrrentarea);

						} else {
							properties.put("incrrentarea", 0);
						}
						if (reportMap.get("rentarea") != null && !reportMap.get("rentarea").equals("")) {
							double rentarea = Double.valueOf(reportMap.get("rentarea").toString());
							properties.put("rentarea", rentarea);
						} else {
							properties.put("rentarea", 0);
						}

					}else {
						//continue;
						
					}
					double taxtotalnum = 0; //总税
					
					double countstayareatax = 0;//留区税
					if (dishuicount != null) {
						double dscount = 0;

						if (dishuicount.get("salesTax") != null){
							dscount += Double.valueOf(dishuicount.get("salesTax").toString()) * 0.5;
							taxtotalnum+=Double.valueOf(dishuicount.get("salesTax").toString());
						}
						if (dishuicount.get("bisnessTax") != null){
							dscount += Double.valueOf(dishuicount.get("bisnessTax").toString()) * 0.3;
							taxtotalnum+=Double.valueOf(dishuicount.get("bisnessTax").toString());
						}
						if (dishuicount.get("personalTax") != null){
							dscount += Double.valueOf(dishuicount.get("personalTax").toString()) * 0.2;
							taxtotalnum+=Double.valueOf(dishuicount.get("personalTax").toString());
						}
						if (dishuicount.get("landaddTax") != null){
							dscount += Double.valueOf(dishuicount.get("landaddTax").toString()) * 0.5;
							taxtotalnum+=Double.valueOf(dishuicount.get("landaddTax").toString());
						}
						if (dishuicount.get("addTax") != null){
							dscount += Double.valueOf(dishuicount.get("addTax").toString()) * 0.25;
							taxtotalnum+=Double.valueOf(dishuicount.get("addTax").toString());
						}
						if (dishuicount.get("constractionTax") != null){
							dscount += Double.valueOf(dishuicount.get("constractionTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("constractionTax").toString());
						}
						if (dishuicount.get("buildTax") != null){
							dscount += Double.valueOf(dishuicount.get("buildTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("buildTax").toString());
						}
						if (dishuicount.get("stampTax") != null){
							dscount += Double.valueOf(dishuicount.get("stampTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("stampTax").toString());
						}
						if (dishuicount.get("landuseTax") != null){
							dscount += Double.valueOf(dishuicount.get("landuseTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("landuseTax").toString());
						}
						if (dishuicount.get("vesselTax") != null){
							dscount += Double.valueOf(dishuicount.get("vesselTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("vesselTax").toString());
						}
						if (dishuicount.get("deedTax") != null){
							dscount += Double.valueOf(dishuicount.get("deedTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("deedTax").toString());
						}
						if (dishuicount.get("eduTax") != null){
							dscount += Double.valueOf(dishuicount.get("eduTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("eduTax").toString());
						}
						if (dishuicount.get("localeduTax") != null){
							dscount += Double.valueOf(dishuicount.get("localeduTax").toString());
							taxtotalnum+=Double.valueOf(dishuicount.get("localeduTax").toString());
						}
							
						countstayareatax += dscount;
					}
					if (guoshuicount != null) {
						double gscount = 0;
						if (guoshuicount.get("addTax") != null){
							gscount += Double.valueOf(guoshuicount.get("addTax").toString()) * 0.25;
							taxtotalnum+=Double.valueOf(guoshuicount.get("addTax").toString());
						}
						if (guoshuicount.get("bisnessTax") != null){
							gscount += Double.valueOf(guoshuicount.get("bisnessTax").toString()) * 0.3;
							taxtotalnum+=Double.valueOf(guoshuicount.get("bisnessTax").toString());
						}
						if (guoshuicount.get("personTax") != null){
							gscount += Double.valueOf(guoshuicount.get("personTax").toString()) * 0.2;
							taxtotalnum+=Double.valueOf(guoshuicount.get("personTax").toString());
						}
						if (guoshuicount.get("constractionTax") != null){
							gscount += Double.valueOf(guoshuicount.get("constractionTax").toString());
							taxtotalnum+=Double.valueOf(guoshuicount.get("constractionTax").toString());
							
						}
							
						countstayareatax += gscount;
					}
					double incountstayareataxlast = 0;//上个月留区税
					double taxtotalnumlast = 0; //上个月总税
					if (dishuicountlast != null) {
						double dscountlast = 0;
						if (dishuicountlast.get("salesTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("salesTax").toString()) * 0.5;
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("salesTax").toString());
						}
						if (dishuicountlast.get("bisnessTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("bisnessTax").toString()) * 0.3;
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("bisnessTax").toString());
						}
						if (dishuicountlast.get("personalTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("personalTax").toString()) * 0.2;
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("personalTax").toString());
						}
						if (dishuicountlast.get("landaddTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("landaddTax").toString()) * 0.5;
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("landaddTax").toString());
						}
						if (dishuicountlast.get("addTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("addTax").toString()) * 0.25;
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("addTax").toString());
						}
						if (dishuicountlast.get("constractionTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("constractionTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("constractionTax").toString());
						}
						if (dishuicountlast.get("buildTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("buildTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("buildTax").toString());
						}
						if (dishuicountlast.get("stampTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("stampTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("stampTax").toString());
						}
						if (dishuicountlast.get("landuseTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("landuseTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("landuseTax").toString());
						}
						if (dishuicountlast.get("vesselTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("vesselTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("vesselTax").toString());
						}
						if (dishuicountlast.get("deedTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("deedTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("deedTax").toString());
						}
						if (dishuicountlast.get("eduTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("eduTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("eduTax").toString());
						}
						if (dishuicountlast.get("localeduTax") != null){
							dscountlast += Double.valueOf(dishuicountlast.get("localeduTax").toString());
							taxtotalnumlast+=Double.valueOf(dishuicountlast.get("localeduTax").toString());
						}
							
						incountstayareataxlast += dscountlast;
					}
					if (guoshuicountlast != null) {
						double gscountlast = 0;
						if (guoshuicountlast.get("addTax") != null){
							gscountlast += Double.valueOf(guoshuicountlast.get("addTax").toString()) * 0.25;
							taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("addTax").toString());
							
						}
						if (guoshuicountlast.get("bisnessTax") != null){
							gscountlast += Double.valueOf(guoshuicountlast.get("bisnessTax").toString()) * 0.3;
							taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("bisnessTax").toString());
							
						}
						if (guoshuicountlast.get("personTax") != null){
							gscountlast += Double.valueOf(guoshuicountlast.get("personTax").toString()) * 0.2;
							taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("personTax").toString());
						}
						
						if (guoshuicountlast.get("constractionTax") != null){
							gscountlast += Double.valueOf(guoshuicountlast.get("constractionTax").toString());
							taxtotalnumlast+=Double.valueOf(guoshuicountlast.get("constractionTax").toString());
						}
							
						incountstayareataxlast += gscountlast;
					}
                    //留区税收
					properties.put("countstayareatax", countstayareatax);
					properties.put("incountstayareatax", countstayareatax - incountstayareataxlast);
					// 总税收
					properties.put("taxtotalnum", taxtotalnum);
					properties.put("taxnum", taxtotalnum - taxtotalnumlast);
					if (units != null) {
						properties.put("unitnum", units.size());
					} else {
						properties.put("unitnum", 0);
					}

					

					mySQLDBHelper.create("t_build_basis_count", properties);

					Map result = mySQLDBHelper
							.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
					if (result != null) {
						result.put("build_basis", 0);
						mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
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

						mySQLDBHelper.create("t_count_flag", m);
					}
				}
			} else
				return;
		} catch (Exception e) {
			Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_basis", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}

	}

	private void tjhb_industry_count(String time) {
		try {
			List result = mySQLDBHelper.retriveBySQL(
					"select u.services, sum(m.income) income,sum(m.tax) tax,sum(m.workmannum) workmannum ,sum(m.incrincome) incrincome,sum(m.incrtax) incrtax,sum(m.incrworkmannum) incrworkmannum from t_unit_monthreport m,t_build_unit u where m.unitid=u.id and u.statusvalue<>0 and m.statusvalue<>0 and m.reportdate='"
							+ time + "' GROUP BY u.services");
			if (result != null && result.size() > 0) {
				for (int i = 0, l = result.size(); i < l; i++) {
					Map r = (Map) result.get(i);
					r.put("countdate", time);
					mySQLDBHelper.create("t_tjhb_industry_count", r);
				}
			}

			Map result1 = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("tjhb_industry", 0);
				mySQLDBHelper.update("t_count_flag", result1, "countdate='" + time + "'");
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

				mySQLDBHelper.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_industry", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_industry_count(String time) {
		try {
			List builds = mySQLDBHelper
					.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List result = mySQLDBHelper.retriveBySQL(
							"select u.services, sum(m.income) income,sum(m.tax) tax,sum(m.workmannum) workmannum ,sum(m.incrincome) incrincome,sum(m.incrtax) incrtax,sum(m.incrworkmannum) incrworkmannum from t_unit_monthreport m,t_build_unit u where m.unitid=u.id and u.statusvalue<>0 and m.statusvalue<>0 and m.reportdate='"
									+ time + "' and u.buildid=" + build.get("id") + " GROUP BY u.services");
					if (result != null && result.size() > 0) {
						for (int j = 0, s = result.size(); j < s; j++) {
							Map r = (Map) result.get(j);
							r.put("countdate", time);
							mySQLDBHelper.create("t_build_industry_count", r);
						}
					}
				}
			}

			Map result1 = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("build_industry", 0);
				mySQLDBHelper.update("t_count_flag", result1, "countdate='" + time + "'");
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

				mySQLDBHelper.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_industry", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void build_benefit_count(String time) {
		try {
			List builds = mySQLDBHelper
					.retriveBySQL("select id ,buildarea from t_build_basis where statusvalue<>0");
			if (builds != null && builds.size() > 0) {
				for (int i = 0, l = builds.size(); i < l; i++) {
					Map build = (Map) builds.get(i);
					List result = mySQLDBHelper.retriveBySQL(
							"select m1.income/m2.income incomerate ,m1.incrincome/m2.incrincome incrincomerate, m1.tax/m2.tax taxrate,m1.incrtax/m2.incrtax incrtaxrate,m1.workmannum/m2.workmannum workmanrate,m1.incrworkmannum/m2.incrworkmannum incrworkmanrate from t_build_monthreport m1,(select sum(income) income,sum(incrincome) incrincome,sum(tax) tax,sum(incrtax) incrtax,sum(workmannum) workmannum,sum(incrworkmannum) incrworkmannum from t_build_monthreport where reportdate='"
									+ time + "') m2 where m1.buildid=" + build.get("id") + " and m1.reportdate='" + time
									+ "'");
					if (result != null && result.size() > 0) {
						for (int j = 0, s = result.size(); j < s; j++) {
							Map r = (Map) result.get(j);
							r.put("countdate", time);
							r.put("buildid", build.get("id"));
							mySQLDBHelper.create("t_build_industry_count", r);
						}
					}
				}
			}
			Map result1 = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("build_benefit", 0);
				mySQLDBHelper.update("t_count_flag", result1, "countdate='" + time + "'");
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

				mySQLDBHelper.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("build_benefit", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
			}
		}
	}

	private void tjhb_benefit_count(String time) {
		try {
			List result = mySQLDBHelper.retriveBySQL(
					"select m1.income/m2.income incomerate ,m1.incrincome/m2.incrincome incrincomerate, m1.tax/m2.tax taxrate,m1.incrtax/m2.incrtax incrtaxrate,m1.workmannum/m2.workmannum workmanrate,m1.incrworkmannum/m2.incrworkmannum incrworkmanrate from t_build_monthreport m1,(select sum(income) income,sum(incrincome) incrincome,sum(tax) tax,sum(incrtax) incrtax,sum(workmannum) workmannum,sum(incrworkmannum) incrworkmannum from t_build_monthreport where reportdate='"
							+ time + "') m2 where m1.reportdate='" + time + "'");
			if (result != null && result.size() > 0) {
				for (int j = 0, s = result.size(); j < s; j++) {
					Map r = (Map) result.get(j);
					r.put("countdate", time);
					mySQLDBHelper.create("t_tjhb_industry_count", r);
				}
			}
			Map result1 = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result1 != null) {
				result1.put("tjhb_benefit", 0);
				mySQLDBHelper.update("t_count_flag", result1, "countdate='" + time + "'");
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

				mySQLDBHelper.create("t_count_flag", m);
			}
		} catch (Exception e) {
			Map result = mySQLDBHelper
					.retriveMapFromSQL("select * from t_count_flag where countdate='" + time + "'");
			if (result != null) {
				result.put("tjhb_benefit", 1);
				mySQLDBHelper.update("t_count_flag", result, "countdate='" + time + "'");
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

		String[] taxServices = { "", "", "", "", "" };
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

		String[] month = new String[12];
		for (int m = 0; m < 12; m++) {
			month[m] = sdf.format(c.getTime());
			c.add(Calendar.MONTH, 1);
		}

		c.add(Calendar.MONTH, -12);

		String[] taxServices = { "", "", "", "", "" };
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

	// 楼宇在某一年的所有税收，留区税收和可招商面积。可以完成两个图表，楼宇在一年每个月的税收、留区税收的柱状表，可以完成一年每个月的可招商面积的曲线图。
	@RequestMapping(value = "/build_charts", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_charts(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");
		String buildid = request.getParameter("buildid");

		String taxSQL = "select c.*,b.buildname from t_build_basis_count c,t_build_basis b where c.buildid=b.id and c.buildid=" + buildid + " and c.countmonth like '"
				+ countdate + "%' order by c.countmonth asc";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);

		String[] month = new String[12];
		for (int m = 0; m < 12; m++) {
			month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
		}

		double[] taxData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double[] taxStayData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double[] rentareaData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String buildname = "";
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);

				String countDate = "";
				String taxnum = "";
				String countstayareatax = "";
				String rentarea = "";

				if (taxMap.get("countmonth") != null)
					countDate = taxMap.get("countmonth").toString();
				if (taxMap.get("taxtotalnum") != null)
					taxnum = taxMap.get("taxtotalnum").toString();
				if (taxMap.get("countstayareatax") != null)
					countstayareatax = taxMap.get("countstayareatax").toString();
				if (taxMap.get("incrrentarea") != null)
					rentarea = taxMap.get("incrrentarea").toString();

				int index = 0;
				for (int j = 0; j < 12; j++) {
					if (month[j].equals(countDate)) {
						index = j;
						break;
					}
				}
				if (taxnum != null && !taxnum.equals(""))
					taxData[index] = Double.valueOf(taxnum);

				if (countstayareatax != null && !countstayareatax.equals(""))
					taxStayData[index] = Double.valueOf(countstayareatax);

				if (rentarea != null && !rentarea.equals(""))
					rentareaData[index] = Double.valueOf(rentarea);
				
				buildname = taxMap.get("buildname").toString();

			}
		}

		Map result = new HashMap();
		result.put("months", month);
		result.put("taxData", taxData);
		result.put("taxStayData", taxStayData);
		result.put("rentareaData", rentareaData);
		result.put("buildname", buildname);
		return result;
	}

	// 河北区在某一年的所有税收，留区税收和可招商面积。可以完成一个图表，全区在一年每个月的税收、留区税收的柱状表。
	@RequestMapping(value = "/tjhb_charts", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map tjhb_charts(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");

		String taxSQL = "select * from t_tjhb_basis_count where countmonth like '" + countdate
				+ "%' order by countmonth desc";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);

		String[] month = new String[12];
		for (int m = 0; m < 12; m++) {
			month[m] = countdate + "." + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1));
		}

		double[] taxData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double[] taxStayData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double[] rentareaData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);
				String taxnum = "";
				String countstayareatax = "";
				String rentarea = "";
				String buildname = "";
				String countDate = "";

				if (taxMap.get("countmonth") != null)
					countDate = taxMap.get("countmonth").toString();
				if (taxMap.get("taxtotalnum") != null)
					taxnum = taxMap.get("taxtotalnum").toString();
				if (taxMap.get("countstayareatax") != null)
					countstayareatax = taxMap.get("countstayareatax").toString();
				if (taxMap.get("rentarea") != null)
					rentarea = taxMap.get("rentarea").toString();

				int index = 0;
				for (int j = 0; j < 12; j++) {
					if (month[j].equals(countDate)) {
						index = j;
						break;
					}
				}

				if (taxnum != null && !taxnum.equals(""))
					taxData[index] = Double.valueOf(taxnum);

				if (countstayareatax != null && !countstayareatax.equals(""))
					taxStayData[index] = Double.valueOf(countstayareatax);
				if (rentarea != null && !rentarea.equals(""))
					rentareaData[index] = Double.valueOf(rentarea);

			}
		}

		Map result = new HashMap();
		result.put("months", month);
		result.put("taxData", taxData);
		result.put("taxStayData", taxStayData);
		result.put("rentareaData", rentareaData);
		return result;
	}

	// 河北区所有楼宇在某一月的所有税收，留区税收和可招商面积。可以完成两个图表，所有楼宇在某个月的税收、留区税收的饼状表（先做税收一个），所有楼宇在某个月可出租面积的饼状图。
	@RequestMapping(value = "/all_build_charts", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map all_build_charts(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");

		String taxSQL = "select b.taxtotalnum,b.countstayareatax,b.incrrentarea,s.buildname from t_build_basis_count b,t_build_basis s where b.buildid=s.id and b.countmonth='"
				+ countdate + "'";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);

		List buildnameData = new ArrayList();
		List taxData = new ArrayList();
		List taxStayData = new ArrayList();
		List rentareaData = new ArrayList();
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);
				String countDate = "";
				String taxnum = "";
				String countstayareatax = "";
				String rentarea = "";

				if (taxMap.get("countmonth") != null)
					countDate = taxMap.get("countmonth").toString();
				if (taxMap.get("taxtotalnum") != null)
					taxnum = taxMap.get("taxtotalnum").toString();
				if (taxMap.get("countstayareatax") != null)
					countstayareatax = taxMap.get("countstayareatax").toString();
				if (taxMap.get("incrrentarea") != null)
					rentarea = taxMap.get("incrrentarea").toString();

				String buildname = taxMap.get("buildname").toString();

				Map taxDataMap = new HashMap();
				if (taxnum != null && !taxnum.equals("")) {
					taxDataMap.put("value", taxnum);
				} else {
					taxDataMap.put("value", 0);
				}
				taxDataMap.put("name", buildname);
				taxData.add(taxDataMap);

				Map countstayMap = new HashMap();
				if (countstayareatax != null && !countstayareatax.equals("")) {
					countstayMap.put("value", countstayareatax);
				} else {
					countstayMap.put("value", 0);
				}
				countstayMap.put("name", buildname);
				taxStayData.add(countstayMap);

				Map rentareaMap = new HashMap();
				if (rentarea != null && !rentarea.equals("")) {
					rentareaMap.put("value", rentarea);
				} else {
					rentareaMap.put("value", 0);
				}
				rentareaMap.put("name", buildname);
				rentareaData.add(rentareaMap);

				if (buildname != null)
					buildnameData.add(buildname);

			}
		}

		Map result = new HashMap();
		result.put("taxData", taxData);
		result.put("taxStayData", taxStayData);
		result.put("rentareaData", rentareaData);
		result.put("buildnameData", buildnameData);
		return result;
	}

	// 河北区所有楼宇在某一年的所有税收，留区税收。可以完成一个图表，所有楼宇在某年的税收、留区税收的饼状表（先做税收一个）。
	@RequestMapping(value = "/builds_tax_charts", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map builds_tax_charts(HttpServletRequest request) {
		String countdate = request.getParameter("countdate");

		String taxSQL = "select sum(b.taxtotalnum) taxtotalnum,sum(b.countstayareatax) countstayareatax,sum(b.rentarea) rentarea,s.buildname from t_build_basis_count b,t_build_basis s where b.buildid=s.id and b.countmonth like '"
				+ countdate + "%' GROUP BY b.buildid";

		List taxResult = mySQLDBHelper.retriveBySQL(taxSQL);

		List buildnameData = new ArrayList();
		List taxData = new ArrayList();
		List taxStayData = new ArrayList();
		List rentareaData = new ArrayList();
		if (taxResult != null) {
			int count = 0;
			for (int i = 0, l = taxResult.size(); i < l; i++) {
				Map taxMap = (Map) taxResult.get(i);
				String taxnum = "";
				String countstayareatax = "";
				String rentarea = "";
				String buildname = "";
				if (taxMap.get("taxtotalnum") != null)
					taxnum = taxMap.get("taxtotalnum").toString();
				if (taxMap.get("countstayareatax") != null)
					countstayareatax = taxMap.get("countstayareatax").toString();
				if (taxMap.get("rentarea") != null)
					rentarea = taxMap.get("rentarea").toString();
				if (taxMap.get("buildname") != null)
					buildname = taxMap.get("buildname").toString();

				Map taxDataMap = new HashMap();
				if (taxnum != null && !taxnum.equals("")) {
					taxDataMap.put("value", taxnum);
				} else {
					taxDataMap.put("value", 0);
				}
				taxDataMap.put("name", buildname);
				taxData.add(taxDataMap);

				Map countstayMap = new HashMap();
				if (countstayareatax != null && !countstayareatax.equals("")) {
					countstayMap.put("value", countstayareatax);
				} else {
					countstayMap.put("value", 0);
				}
				countstayMap.put("name", buildname);
				taxStayData.add(countstayMap);

				Map rentareaMap = new HashMap();
				if (rentarea != null && !rentarea.equals("")) {
					rentareaMap.put("value", rentarea);
				} else {
					rentareaMap.put("value", 0);
				}
				rentareaMap.put("name", buildname);
				rentareaData.add(rentareaMap);

				if (buildname != null)
					buildnameData.add(buildname);

			}
		}

		Map result = new HashMap();
		result.put("taxData", taxData);
		result.put("taxStayData", taxStayData);
		result.put("rentareaData", rentareaData);
		result.put("buildnameData", buildnameData);
		return result;
	}
	 public void  qiye_count(String time){
		 List qiyedishui = mySQLDBHelper.retriveBySQL("SELECT * from t_build_dishui_count where datatime='"
							+ time + "'");
		 List qiyeguoshui = mySQLDBHelper.retriveBySQL("SELECT * from t_build_guoshui_count where datatime='"
					+ time + "'");
		 int l=qiyedishui.size();
		 if(l>0){
			 for (int i=0;i<l;i++){
				 Map dishuicount=(Map) qiyedishui.get(i);
				 Map properties=new HashMap();
				 String socialCreCode=dishuicount.get("socialCreCode").toString();
				 double quanshui=0;
				 double liuqushui=0;
				 //留区国税
				    if (dishuicount.get("salesTax") != null){
				    	  liuqushui += Double.valueOf(dishuicount.get("salesTax").toString()) * 0.5;
				    	  quanshui+=Double.valueOf(dishuicount.get("salesTax").toString());
				    }
					if (dishuicount.get("bisnessTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("bisnessTax").toString()) * 0.3;
						 quanshui+=Double.valueOf(dishuicount.get("bisnessTax").toString());
					}
					if (dishuicount.get("personalTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("personalTax").toString()) * 0.2;
						 quanshui+=Double.valueOf(dishuicount.get("personalTax").toString());
					}
					if (dishuicount.get("landaddTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("landaddTax").toString()) * 0.5;
						 quanshui+=Double.valueOf(dishuicount.get("landaddTax").toString());
					}
					if (dishuicount.get("addTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("addTax").toString()) * 0.25;
						quanshui+=Double.valueOf(dishuicount.get("addTax").toString());
					}
					if (dishuicount.get("constractionTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("constractionTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("constractionTax").toString());
					}
					if (dishuicount.get("buildTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("buildTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("buildTax").toString());
					}
					if (dishuicount.get("stampTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("stampTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("stampTax").toString());
					}
					if (dishuicount.get("landuseTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("landuseTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("landuseTax").toString());
					}
					if (dishuicount.get("vesselTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("vesselTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("vesselTax").toString());
					}
					if (dishuicount.get("deedTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("deedTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("deedTax").toString());
					}
					if (dishuicount.get("eduTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("eduTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("eduTax").toString());
					}
					if (dishuicount.get("localeduTax") != null){
						liuqushui += Double.valueOf(dishuicount.get("localeduTax").toString());
						quanshui+=Double.valueOf(dishuicount.get("localeduTax").toString());
					}
						
					//留区地税
					Map guoshuicount = mySQLDBHelper.retriveMapFromSQL(
							"SELECT * from t_build_guoshui_count  where socialCreCode='"+socialCreCode+"' and datatime='"+time + "'");	
					if(guoshuicount!=null){
						if (guoshuicount.get("addTax") != null){
							liuqushui += Double.valueOf(guoshuicount.get("addTax").toString()) * 0.25;
							quanshui+=Double.valueOf(guoshuicount.get("addTax").toString());
						}
						if (guoshuicount.get("bisnessTax") != null){
							liuqushui += Double.valueOf(guoshuicount.get("bisnessTax").toString()) * 0.3;
							quanshui+=Double.valueOf(guoshuicount.get("bisnessTax").toString());
						}
						if (guoshuicount.get("personTax") != null){
							liuqushui += Double.valueOf(guoshuicount.get("personTax").toString()) * 0.2;
							quanshui+=Double.valueOf(guoshuicount.get("personTax").toString());
						}
						if (guoshuicount.get("constractionTax") != null){
							liuqushui += Double.valueOf(guoshuicount.get("constractionTax").toString());
							quanshui+=Double.valueOf(guoshuicount.get("constractionTax").toString());
						}
							
					}
					properties.put("socialCreCode", socialCreCode);
					properties.put("quanshui", quanshui);
					properties.put("liuqushui", liuqushui);
					properties.put("datatime", time);
					mySQLDBHelper.create("t_unitcount", properties);
					
			 }
		 }
		 
		 
	 }
}
