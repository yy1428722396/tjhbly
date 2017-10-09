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
@RequestMapping("/build_environment")
public class BuildEnvironmentController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String subway = request.getParameter("subway");
		String station = request.getParameter("station");
		String stopprice = request.getParameter("stopprice");
		String stopno = request.getParameter("stopno");
		String buildstop = request.getParameter("buildstop");
		String restaurant = request.getParameter("restaurant");
		String hotel1 = request.getParameter("hotel1");
		String hotel2 = request.getParameter("hotel2");
		String hlift = request.getParameter("hlift");
		String mlift = request.getParameter("mlift");
		String lift = request.getParameter("lift");
		String workerrestaurant = request.getParameter("workerrestaurant");
		String afforest = request.getParameter("afforest");
		String bankno = request.getParameter("bankno");
		String cbd = request.getParameter("cbd");
		String cbdarea = request.getParameter("cbdarea");
		String buildid = request.getParameter("buildid");
		
		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		
		Map properties = new HashMap();
		properties.put("subway", subway);
		properties.put("station", station);
		properties.put("stopprice", stopprice);
		properties.put("stopno", stopno);
		properties.put("buildstop", buildstop);
		properties.put("restaurant", restaurant);
		properties.put("hotel1", hotel1);
		properties.put("hotel2", hotel2);
		properties.put("hlift", hlift);
		properties.put("mlift", mlift);
		properties.put("lift", lift);
		properties.put("workerrestaurant", workerrestaurant);
		properties.put("afforest", afforest);
		properties.put("bankno", bankno);
		properties.put("cbd", cbd);
		properties.put("cbdarea", cbdarea);
		properties.put("buildid", buildid);
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
	
		long id = mySQLDBHelper.create("t_build_environment", properties);
	
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String subway = request.getParameter("subway");
		String station = request.getParameter("station");
		String stopprice = request.getParameter("stopprice");
		String stopno = request.getParameter("stopno");
		String buildstop = request.getParameter("buildstop");
		String restaurant = request.getParameter("restaurant");
		String hotel1 = request.getParameter("hotel1");
		String hotel2 = request.getParameter("hotel2");
		String hlift = request.getParameter("hlift");
		String mlift = request.getParameter("mlift");
		String lift = request.getParameter("lift");
		String workerrestaurant = request.getParameter("workerrestaurant");
		String afforest = request.getParameter("afforest");
		String bankno = request.getParameter("bankno");
		String cbd = request.getParameter("cbd");
		String cbdarea = request.getParameter("cbdarea");
		String buildid = request.getParameter("buildid");
		
		HttpSession session = request.getSession();
		String role = "";
		if(session.getAttribute("role") != null && !session.getAttribute("role").equals("")){
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		Map properties = new HashMap();
		properties.put("subway", subway);
		properties.put("station", station);
		properties.put("stopprice", stopprice);
		properties.put("stopno", stopno);
		properties.put("buildstop", buildstop);
		properties.put("restaurant", restaurant);
		properties.put("hotel1", hotel1);
		properties.put("hotel2", hotel2);
		properties.put("hlift", hlift);
		properties.put("mlift", mlift);
		properties.put("lift", lift);
		properties.put("workerrestaurant", workerrestaurant);
		properties.put("afforest", afforest);
		properties.put("bankno", bankno);
		properties.put("cbd", cbd);
		properties.put("cbdarea", cbdarea);
		properties.put("statusvalue", role);
		properties.put("buildid", buildid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		
		return mySQLDBHelper.update("t_build_environment", properties, "id=" + id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String subway1 = request.getParameter("subway1");
		String station1 = request.getParameter("station1");
		String restaurant1 = request.getParameter("restaurant1");
		String hotel11 = request.getParameter("hotel11");
		String subway2 = request.getParameter("subway2");
		String station2 = request.getParameter("station2");
		String restaurant2 = request.getParameter("restaurant2");
		String hotel12 = request.getParameter("hotel12");
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
		
		String sql = "SELECT e.*,b.buildname from t_build_environment e,t_build_basis b where e.statusvalue>=" + role + " and e.buildid=b.id";
		if(userType.equals("1"))
			sql += " and b.rununitid='" + unitid + "'";
		
		if (subway1 != null && !subway1.trim().equals(""))
			sql += " and subway>=" + subway1;
		if (subway2 != null && !subway2.trim().equals(""))
			sql += " and subway<=" + subway2;
		if (station1 != null && !station1.trim().equals(""))
			sql += " and station>=" + station1;
		if (station2 != null && !station2.trim().equals(""))
			sql += " and station<=" + station2;
		if (restaurant1 != null && !restaurant1.trim().equals(""))
			sql += " and restaurant>=" + restaurant1;
		if (restaurant2 != null && !restaurant2.trim().equals(""))
			sql += " and restaurant<=" + restaurant2;
		if (hotel11 != null && !hotel11.trim().equals(""))
			sql += " and hotel1>=" + hotel11;
		if (hotel12 != null && !hotel12.trim().equals(""))
			sql += " and hotel1<=" + hotel12;
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List all(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		String userType = "";
		String username = "";
		if(session.getAttribute("type") != null && !session.getAttribute("type").equals("")){
			userType = session.getAttribute("type").toString();
			username = session.getAttribute("username").toString();
		}
		else
			return null;
		
		String sql = "select e.*,b.buildname from t_build_environment e,t_build_basis b where e.statusvalue<>0 and e.buildid=b.id";
		
		if(userType.equals("2"))
			sql += " and b.username='" + username + "'";
		
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveBySQL("select e.*,b.buildname from t_build_environment e,t_build_basis b where e.statusvalue<>0 and e.buildid=b.id and e.id=" + id, false,0,0);
		
		return result;
	}

	

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
			
		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_build_environment", temp, "id=" + id);
		
	}
}
