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
@RequestMapping("/build_unit")
public class BuildUnitController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String unitname = request.getParameter("unitname");
		String unitsimplename = request.getParameter("unitsimplename");
		String societycode = request.getParameter("societycode");
		String registryasset = request.getParameter("registryasset");
		String registryaddress = request.getParameter("registryaddress");
		String url = request.getParameter("url");
		String services = request.getParameter("services");
		String belongto = request.getParameter("belongto");
		String isfirstcompany = request.getParameter("isfirstcompany");
		String isentity = request.getParameter("isentity");
		String workmannum = request.getParameter("workmannum");
		String floor = request.getParameter("floor");
		String roomno = request.getParameter("roomno");
		String entertime = request.getParameter("entertime");
		String leavetime = request.getParameter("leavetime");
		String area = request.getParameter("area");
		String satisfay = request.getParameter("satisfay");
		String willleave = request.getParameter("willleave");
		String leavereason = request.getParameter("leavereason");
		String telphone = request.getParameter("telphone");
		String mobile = request.getParameter("mobile");
		String buildid = request.getParameter("buildid");
		
		Map properties = new HashMap();
		properties.put("unitname", unitname);
		properties.put("unitsimplename", unitsimplename);
		properties.put("societycode", societycode);
		properties.put("registryasset", registryasset);
		properties.put("registryaddress", registryaddress);
		properties.put("url", url);
		properties.put("services", services);
		properties.put("belongto", belongto);
		properties.put("isfirstcompany", isfirstcompany);
		properties.put("isentity", isentity);
		properties.put("workmannum", workmannum);
		properties.put("floor", floor);
		properties.put("roomno", roomno);
		properties.put("entertime", entertime);
		properties.put("leavetime", leavetime);
		properties.put("area", area);
		properties.put("satisfay", satisfay);
		properties.put("willleave", willleave);
		properties.put("leavereason", leavereason);
		properties.put("telphone", telphone);
		properties.put("mobile", mobile);
		properties.put("buildid", buildid);

		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
	
		long id = mySQLDBHelper.create("t_build_unit", properties);
	
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
		String unitsimplename = request.getParameter("unitsimplename");
		String societycode = request.getParameter("societycode");
		String registryasset = request.getParameter("registryasset");
		String registryaddress = request.getParameter("registryaddress");
		String url = request.getParameter("url");
		String services = request.getParameter("services");
		String belongto = request.getParameter("belongto");
		String isfirstcompany = request.getParameter("isfirstcompany");
		String isentity = request.getParameter("isentity");
		String workmannum = request.getParameter("workmannum");
		String floor = request.getParameter("floor");
		String roomno = request.getParameter("roomno");
		String entertime = request.getParameter("entertime");
		String leavetime = request.getParameter("leavetime");
		String area = request.getParameter("area");
		String satisfay = request.getParameter("satisfay");
		String willleave = request.getParameter("willleave");
		String leavereason = request.getParameter("leavereason");
		String telphone = request.getParameter("telphone");
		String mobile = request.getParameter("mobile");
		String buildid = request.getParameter("buildid");
		
		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;

		Map properties = new HashMap();
		properties.put("unitname", unitname);
		properties.put("unitsimplename", unitsimplename);
		properties.put("societycode", societycode);
		properties.put("registryasset", registryasset);
		properties.put("registryaddress", registryaddress);
		properties.put("url", url);
		properties.put("services", services);
		properties.put("belongto", belongto);
		properties.put("isfirstcompany", isfirstcompany);
		properties.put("isentity", isentity);
		properties.put("workmannum", workmannum);
		properties.put("floor", floor);
		properties.put("roomno", roomno);
		properties.put("entertime", entertime);
		properties.put("leavetime", leavetime);
		properties.put("area", area);
		properties.put("satisfay", satisfay);
		properties.put("willleave", willleave);
		properties.put("leavereason", leavereason);
		properties.put("telphone", telphone);
		properties.put("mobile", mobile);
		properties.put("buildid", buildid);
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		
		return mySQLDBHelper.update("t_build_unit", properties, "id=" + id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String buildname = request.getParameter("buildname");
		String unitname = request.getParameter("unitname");
		String services = request.getParameter("services");
		String belongto = request.getParameter("belongto");
		//String limit = request.getParameter("limit");
		String limit = "10";
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
		String role = "";
		if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
			userType = session.getAttribute("type").toString();
			unitid = session.getAttribute("unitid").toString();
			role = session.getAttribute("role").toString();
		}
		else
			return null;
		
		String sql = "SELECT u.*,b.buildname from t_build_unit u,t_build_basis b where u.statusvalue>=" + role +" and u.buildid=b.id";
		if(userType.equals("1"))
			sql += " and b.rununitid='" + unitid + "'";
		if (buildname != null && !buildname.trim().equals(""))
			sql += " and b.buildname like '%" + buildname + "%'";
		if (unitname != null && !unitname.trim().equals(""))
			sql += " and u.unitname like '%" + unitname + "%'";
		if (services != null && !services.trim().equals(""))
			sql += " and u.services like '%" + services + "%'";
		if (belongto != null && !belongto.trim().equals(""))
			sql += " and u.belongto like '%" + belongto + "%'";
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
		
		String sql = "SELECT u.*,b.buildname from t_build_unit u,t_build_basis b where u.statusvalue<>0 and u.buildid=b.id";
		if(userType.equals("1"))
			sql += " and b.rununitid='" + unitid + "'";
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveBySQL("SELECT u.*,b.buildname buildname from t_build_unit u,t_build_basis b where u.statusvalue<>0 and u.buildid=b.id and u.id=" + id, false, 0,0);
		return result;
	}

	

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
			
		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_build_unit", temp, "id=" + id);
		
	}
}
