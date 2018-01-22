package cn.com.tjly.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/front_page")
public class FrontPageController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map index(HttpServletRequest request) {
		Map result = new HashMap();

		Map vrMap = mySQLDBHelper
				.retriveBySQL("select id,buildname,vrurl,vrpic from t_build_basis where vrpic is not null", true, 0, 8);
		List vrList = new ArrayList();
		if (vrMap != null)
			vrList = (List) vrMap.get("data");

		Map buildMap = mySQLDBHelper.retriveBySQL(
				"select id,buildname,lon,lat from t_build_basis where lon is not null and lat is not null", false, 0,
				10);
		List buildList = new ArrayList();
		if (vrMap != null)
			buildList = (List) buildMap.get("data");

		Map newsMap = mySQLDBHelper
				.retriveBySQL("select id,title,summary,createtime from t_news order by createtime desc", true, 0, 3);
		List newsList = new ArrayList();
		if (vrMap != null)
			newsList = (List) newsMap.get("data");

		String datetime = request.getParameter("datetime");
		String year = datetime.substring(0, datetime.indexOf("."));
		String month = datetime.substring(datetime.indexOf(".") + 1, datetime.length());

		List numList = new ArrayList();
		List numList1 = mySQLDBHelper.retriveBySQL(
				"select b.taxtotalnum btaxtotalnum,b.countstayareatax bcountstayareatax from t_tjhb_basis_count b where b.countmonth ='" + datetime + "'");

		List numList2 = mySQLDBHelper.retriveBySQL(
				"select sum(taxtotalnum) ttaxtotalnum,sum(countstayareatax) tcountstayareatax,sum(incrrentarea) tincrrentarea from t_tjhb_basis_count where countmonth like '" + year + "%'");
		if(numList1 != null)
			numList.addAll(numList1);
		if(numList2 != null)
			numList.addAll(numList2);
		
		result.put("vrList", vrList);
		result.put("buildList", buildList);
		result.put("newsList", newsList);
		result.put("numList", numList);
		return result;
	}

	@RequestMapping(value = "/allbuilds", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List allbuilds(HttpServletRequest request) {
		
		
		String sql = "select b.id,b.buildname,b.address,i.imgurl from t_build_basis b,t_build_img i where b.statusvalue<>0 and b.id=i.buildid and i.statusvalue<>0 and i.imgurl is not null";
		
		return mySQLDBHelper.retriveBySQL(sql);
	}
	
	@RequestMapping(value = "/allvrs", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List allvrs(HttpServletRequest request) {
		return mySQLDBHelper.retriveBySQL("select id,buildname,vrurl,vrpic from t_build_basis where vrpic is not null");
	}
	
	@RequestMapping(value = "/build_detail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_detail(HttpServletRequest request) {
		String id = request.getParameter("buildid");
		Map result = mySQLDBHelper.retriveMapFromSQL("select * from t_build_basis where statusvalue<>0 and id=" + id);
		
		Map imgMap = mySQLDBHelper.retriveMapFromSQL("select imgurl from t_build_img where statusvalue<>0 and buildid=" + id);
		
		if(imgMap != null)
		{
			result.put("imgurl", imgMap.get("imgurl"));
		}else{
			result.put("imgurl", "");
		}

		List unitNo = mySQLDBHelper.retriveBySQL("select id from t_build_unit where statusvalue <> 0 and buildid=" + id);
		if(unitNo != null)
			result.put("unitno", unitNo.size());
		else
			result.put("unitno", 0);
		
		List workman = mySQLDBHelper.retriveBySQL("select workmannum ,emptyarea,rentarea from t_build_monthreport where statusvalue <> 0 and buildid=" + id + " order by reportdate desc");
		if(workman != null && workman.size() > 0){
			Map temp = (Map)workman.get(0);
			if(temp.get("workmannum") != null && !temp.get("workmannum").equals(""))
				result.put("workmannum", temp.get("workmannum"));
			else
				result.put("workmannum",0);
			
			if(temp.get("emptyarea") != null && !temp.get("emptyarea").equals(""))
				result.put("emptyarea", temp.get("emptyarea"));
			else
				result.put("emptyarea",0);
			
			if(temp.get("rentarea") != null && !temp.get("rentarea").equals(""))
				result.put("rentarea", temp.get("rentarea"));
			else
				result.put("rentarea",0);
		}else{
			result.put("rentarea",0);
			result.put("emptyarea",0);
			result.put("workmannum",0);
			
		}
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		String datetime = year + "." + month;
		List numList = mySQLDBHelper.retriveBySQL(
				"select b.taxtotalnum btaxtotalnum,b.countstayareatax bcountstayareatax,b.incrrentarea incrrentarea from t_build_basis_count b where b.buildid=" + id + " and b.countmonth ='" + datetime + "'");
		System.out.println("------" + id + "-----" + datetime);
		result.put("numList",numList);
		return result;
	}
	
	@RequestMapping(value = "/newslist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map newslist(HttpServletRequest request) {
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
		return result;
	}
}
