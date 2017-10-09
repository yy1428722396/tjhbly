package cn.com.tjly.controller;

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
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private MySQLDBHelper mySQLDBHelper;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sql = "select * from xtloginuser where username='" + username + "' and userpwd='" + password + "'";
		List result = mySQLDBHelper.retriveBySQL(sql);
		if(result != null && result.size() > 0)
		{
			Map user = (Map)result.get(0);
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("realname", user.get("RealName"));
			
			return true;
		}
		else
			return false;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String role = request.getParameter("role");
		String realname = request.getParameter("realname");
		String telphone = request.getParameter("telphone");
		
		String sql = "select * from xtloginuser where username='" + username + "'";
		List result = mySQLDBHelper.retriveBySQL(sql);
		if(result != null && result.size() > 0)
		{
			return false;
		}
		
		Map properties = new HashMap();
		properties.put("username", username);
		properties.put("userpwd", password);
		properties.put("sex", sex);
		properties.put("userrole", role);
		properties.put("realname", realname);
		properties.put("telephone", telphone);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createdate", now);
		long id = mySQLDBHelper.create("xtloginuser", properties);
		if(id != 0)
			return true;
		else
			return false;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String role = request.getParameter("role");
		String realname = request.getParameter("realname");
		String telphone = request.getParameter("telphone");
		
		Map properties = new HashMap();
		properties.put("userpwd", password);
		properties.put("sex", sex);
		properties.put("userrole", role);
		properties.put("realname", realname);
		properties.put("telephone", telphone);
		return mySQLDBHelper.update("xtloginuser", properties, "username='" + username + "'");
	}
	
	@RequestMapping(value = "/userlist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userlist(HttpServletRequest request) {
		String username = request.getParameter("username");
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null
				&& !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "select username,sex,realname,userrole,telephone,createdate from xtloginuser";
		if(username != null && !username.trim().equals(""))
			sql += " where username like '%" + username + "%'";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}
	
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userinfo(HttpServletRequest request) {
		String username = request.getParameter("username");
		Map result = mySQLDBHelper.retriveByID("xtloginuser", "username", username);
		return result;
	}
	
	@RequestMapping(value = "/userrole", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userrole(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map result = new HashMap();
		if(session.getAttribute("username") != null){
			String username = session.getAttribute("username").toString();
			String sql = "select userrole from xtloginuser where username='" + username + "'";
			Map roleMap = (Map)mySQLDBHelper.retriveBySQL(sql).get(0);
			String role = roleMap.get("userrole").toString();
			result.put("data", role);
		}else
			result.put("data", "error");
			
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String username = request.getParameter("username");
		return mySQLDBHelper.delete("xtloginuser", "username='" + username + "'");
	}

	@RequestMapping(value = "/addcity", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List addcity(HttpServletRequest request) {
		String cityname = request.getParameter("cityname");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		Map properties = new HashMap();
		properties.put("CityName", cityname);
		properties.put("CreateDate", now);
		mySQLDBHelper.create("xtcity", properties);
		
		return mySQLDBHelper.retriveBySQL("select * from xtcity");
	}
	
	@RequestMapping(value = "/fetchcities", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List fetchcities(HttpServletRequest request) {
		return mySQLDBHelper.retriveBySQL("select * from xtcity");
	}
}
