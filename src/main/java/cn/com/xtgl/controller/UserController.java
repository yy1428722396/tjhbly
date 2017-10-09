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
@RequestMapping("/user")
public class UserController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String type = request.getParameter("type");
		String sql = "select * from t_user where statusvalue<>0 and username='" + username + "' and password='"
				+ password + "' and type=" + type;
		List result = mySQLDBHelper.retriveBySQL(sql);
		if (result != null && result.size() > 0) {
			Map user = (Map) result.get(0);
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("role", user.get("role").toString());
			// type:1涓烘ゼ瀹囧姙鐢ㄦ埛锛�2涓烘ゼ瀹囪繍钀ュ崟浣嶇敤鎴�
			session.setAttribute("type", user.get("type").toString());
			session.setAttribute("userid", user.get("id").toString());
			session.setAttribute("unitid", user.get("unitid").toString());
			return true;
		} else
			return false;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		String type = request.getParameter("type");
		String unitid = request.getParameter("unitid");
		
		String sql = "select * from t_user where statusvalue<>0 and username='" + username + "' and type=" + type;
		List result = mySQLDBHelper.retriveBySQL(sql);
		if (result != null && result.size() > 0) {
			return false;
		}

		Map properties = new HashMap();
		properties.put("username", username);
		properties.put("password", "11111111");
		properties.put("role", role);
		properties.put("type", type);
		properties.put("unitid", unitid);
		properties.put("statusvalue", 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);

		long id = mySQLDBHelper.create("t_user", properties);
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		String unitid = request.getParameter("unitid");
		
		Map properties = new HashMap();
		properties.put("role", role);
		properties.put("unitid", unitid);
		return mySQLDBHelper.update("t_user", properties, "username='" + username + "'");
	}

	@RequestMapping(value = "/recovery", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean recovery(HttpServletRequest request) {
		String username = request.getParameter("username");

		Map properties = new HashMap();
		properties.put("password", "11111111");
		return mySQLDBHelper.update("t_user", properties, "username='" + username + "'");
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String username = request.getParameter("username");
		String type = request.getParameter("type");
		String role = request.getParameter("role");
		String page = request.getParameter("page");
		String limit = "10";// request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		HttpSession session = request.getSession();
		String user = "";
		String session_type = "";
		if (session.getAttribute("type") != null && !session.getAttribute("type").equals("")) {
			user = session.getAttribute("username").toString();
			session_type = session.getAttribute("type").toString();
		} else
			return null;
		
		String sql = "";
		if(type.equals("1"))
			sql = "select u.id,u.username,u.role,u.unitid,u.type,u.createtime,r.unitname from t_user u,t_rununit r where u.unitid=r.id and u.statusvalue<>0 and role <> 0 and username<>'no0user'";
		else if(type.equals("2"))
			sql = "select u.id,u.username,u.role,u.unitid,u.type,u.createtime from t_user u where u.statusvalue<>0 and role <> 0 and username<>'no0user'";
		 
		if (username != null && !username.trim().equals(""))
			sql += " and u.username like '%" + username + "%'";
		if (type != null && !type.trim().equals("")){
			sql += " and u.type=" + type;
			if(type.equals(session_type))
				sql += " and u.username<>'" + user + "'";
		}
		if (role != null && !role.trim().equals("") && !role.trim().equals("0"))
			sql += " and u.role=" + role;
	
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveBySQL("select u.username,u.role,u.unitid,u.type,u.createtime,r.unitname from t_user u,t_rununit r where u.username<>'no0user' u.statusvalue<>0 and u.unitid=r.id and u.id=" + id, false, 0, 0);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map properties = new HashMap();
		properties.put("statusvalue", 0);
		return mySQLDBHelper.update("t_user", properties, "id='" + id + "'");

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("type");
		session.removeAttribute("userid");
		session.removeAttribute("role");
		session.removeAttribute("unitid");
		return true;
	}

	@RequestMapping(value = "/pwd", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean pwd(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("userid") != null) {
			String id = session.getAttribute("userid").toString();
			String oldpwd = request.getParameter("oldpwd");
			String newpwd = request.getParameter("newpwd");
			List result = mySQLDBHelper.retriveBySQL(
					"select * from t_user where id=" + id + " and password='" + oldpwd + "' and statusvalue<>0");
			if (result != null && result.size() > 0) {
				Map properties = new HashMap();
				properties.put("password", newpwd);
				return mySQLDBHelper.update("t_user", properties, "id='" + id + "'");
			} else
				return false;
		} else
			return false;
	}

	@RequestMapping(value = "/logininfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userrole(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map result = new HashMap();
		if (session.getAttribute("username") != null) {
			String username = session.getAttribute("username").toString();
			String role = session.getAttribute("role").toString();
			String type = session.getAttribute("type").toString();
			String unitid = session.getAttribute("unitid").toString();
			result.put("session_role", role);
			result.put("session_type", type);
			result.put("session_username", username);
			result.put("session_unitid", unitid);
			result.put("data", "success");
		} else
			result.put("data", "error");

		return result;
	}

}
