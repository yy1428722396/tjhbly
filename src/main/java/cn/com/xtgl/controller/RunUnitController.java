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

import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/rununit")
public class RunUnitController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String unitname = request.getParameter("unitname");
		String telphone = request.getParameter("telphone");
		String address = request.getParameter("address");
		String dutyman = request.getParameter("dutyman");
		String touchman = request.getParameter("touchman");
		
		Map properties = new HashMap();
		properties.put("unitname", unitname);
		properties.put("telphone", telphone);
		properties.put("address", address);
		properties.put("touchman", touchman);
		properties.put("dutyman", dutyman);
		properties.put("statusvalue", 1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
	
		long id = mySQLDBHelper.create("t_rununit", properties);
	
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String unitname = request.getParameter("unitname");
		String telphone = request.getParameter("telphone");
		String address = request.getParameter("address");
		String touchman = request.getParameter("touchman");
		String dutyman = request.getParameter("dutyman");
		
		Map properties = new HashMap();
		properties.put("unitname", unitname);
		properties.put("telphone", telphone);
		properties.put("address", address);
		properties.put("touchman", touchman);
		properties.put("dutyman", dutyman);
		properties.put("statusvalue", 1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		
		return mySQLDBHelper.update("t_rununit", properties, "id=" + id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String unitname = request.getParameter("unitname");
		
		//String limit = request.getParameter("limit");
		String limit = "10";
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null
				&& !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		
		String sql = "SELECT * from t_rununit where statusvalue<>0";
		if (unitname != null && !unitname.trim().equals(""))
			sql += " and unitname like '%" + unitname + "%'";
		
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List all(HttpServletRequest request) {
		String sql = "SELECT * from t_rununit where statusvalue<>0";
	
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveBySQL("SELECT * from t_rununit where statusvalue<>0 and id=" + id, false, 0,0);
		return result;
	}

	

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
			
		HashMap temp = new HashMap();
		temp.put("statusvalue", 1);
		return mySQLDBHelper.update("t_rununit", temp, "id=" + id);
		
	}
}
