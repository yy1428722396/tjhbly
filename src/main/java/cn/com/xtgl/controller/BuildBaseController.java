package cn.com.xtgl.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.com.common.Common;
import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/build_base")
public class BuildBaseController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String name = request.getParameter("name");
		String prename = request.getParameter("prename");
		String rent = request.getParameter("rent");
		String property = request.getParameter("property");
		String buildarea = request.getParameter("buildarea");
		String eartharea = request.getParameter("eartharea");
		String rentarea = request.getParameter("rentarea");
		String emptyarea = request.getParameter("emptyarea");
		String right = request.getParameter("right");
		String classValue = request.getParameter("classValue");
		String type = request.getParameter("type");
		String service = request.getParameter("service");
		String upfloor = request.getParameter("upfloor");
		String totalfloor = request.getParameter("totalfloor");
		String address = request.getParameter("address");
		String postcode = request.getParameter("postcode");
		String buildyear = request.getParameter("buildyear");
		String devname = request.getParameter("devname");
		String street = request.getParameter("street");
		String cbd = request.getParameter("cbd");
		String propertyname = request.getParameter("propertyname");
		String propertytel = request.getParameter("propertytel");
		String downfloor = request.getParameter("downfloor");
		String toucheman = request.getParameter("toucheman");
		String roomno = request.getParameter("roomno");
		String email = request.getParameter("email");
		String carno = request.getParameter("carno");
		String lon = request.getParameter("lon");
		String lat = request.getParameter("lat");
		String decoration = request.getParameter("decoration");
		String niche = request.getParameter("niche");
		String hotel = request.getParameter("hotel");
		String bank = request.getParameter("bank");
		String restaurant = request.getParameter("restaurant");
		String traffic = request.getParameter("traffic");
		String hospital = request.getParameter("hospital");
		String intruduction = request.getParameter("intruduction");
		String importarea = request.getParameter("importarea");
		String classfy = request.getParameter("classfy");
		String runtime = request.getParameter("runtime");
		String belongunit = request.getParameter("belongunit");

		HttpSession session = request.getSession();
		String unitid = "";
		String role = "";
		if (session.getAttribute("username") != null && !session.getAttribute("username").equals("")) {
			unitid = (String) session.getAttribute("unitid");
			role = (String) session.getAttribute("role");
		} else
			return false;

		Map properties = new HashMap();
		properties.put("buildname", name);
		properties.put("prename", prename);
		properties.put("rent", rent);
		properties.put("property", property);
		properties.put("buildarea", buildarea);
		properties.put("eartharea", eartharea);
		properties.put("rentarea", rentarea);
		properties.put("emptyarea", emptyarea);
		properties.put("buildright", right);
		properties.put("classvalue", classValue);
		properties.put("type", type);
		properties.put("service", service);
		properties.put("upfloor", upfloor);
		properties.put("totalfloor", totalfloor);
		properties.put("address", address);
		properties.put("postcode", postcode);
		properties.put("buildyear", buildyear);
		properties.put("devname", devname);
		properties.put("street", street);
		properties.put("cbd", cbd);
		properties.put("propertyname", propertyname);
		properties.put("propertytel", propertytel);
		properties.put("downfloor", downfloor);
		properties.put("toucheman", toucheman);
		properties.put("roomno", roomno);
		properties.put("email", email);
		properties.put("carno", carno);
		properties.put("statusvalue", role);
		properties.put("rununitid", unitid);
		properties.put("lon", lon);
		properties.put("lat", lat);

		properties.put("decoration", decoration);
		properties.put("niche", niche);
		properties.put("hotel", hotel);
		properties.put("bank", bank);
		properties.put("restaurant", restaurant);
		properties.put("traffic", traffic);
		properties.put("hospital", hospital);
		properties.put("intruduction", intruduction);
		properties.put("importarea", importarea);
		properties.put("classfy1", classfy);
		properties.put("runtime", runtime);
		properties.put("belongunit", belongunit);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);

		long id = mySQLDBHelper.create("t_build_basis", properties);

		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String prename = request.getParameter("prename");
		String rent = request.getParameter("rent");
		String property = request.getParameter("property");
		String buildarea = request.getParameter("buildarea");
		String eartharea = request.getParameter("eartharea");
		String rentarea = request.getParameter("rentarea");
		String emptyarea = request.getParameter("emptyarea");
		String right = request.getParameter("right");
		String classValue = request.getParameter("classValue");
		String type = request.getParameter("type");
		String service = request.getParameter("service");
		String upfloor = request.getParameter("upfloor");
		String totalfloor = request.getParameter("totalfloor");
		String address = request.getParameter("address");
		String postcode = request.getParameter("postcode");
		String buildyear = request.getParameter("buildyear");
		String devname = request.getParameter("devname");
		String street = request.getParameter("street");
		String cbd = request.getParameter("cbd");
		String propertyname = request.getParameter("propertyname");
		String propertytel = request.getParameter("propertytel");
		String downfloor = request.getParameter("downfloor");
		String toucheman = request.getParameter("toucheman");
		String roomno = request.getParameter("roomno");
		String email = request.getParameter("email");
		String carno = request.getParameter("carno");
		String lon = request.getParameter("lon");
		String lat = request.getParameter("lat");
		String decoration = request.getParameter("decoration");
		String niche = request.getParameter("niche");
		String hotel = request.getParameter("hotel");
		String bank = request.getParameter("bank");
		String restaurant = request.getParameter("restaurant");
		String traffic = request.getParameter("traffic");
		String hospital = request.getParameter("hospital");
		String intruduction = request.getParameter("intruduction");
		String importarea = request.getParameter("importarea");
		String classfy = request.getParameter("classfy");
		String runtime = request.getParameter("runtime");
		String belongunit = request.getParameter("belongunit");

		HttpSession session = request.getSession();
		String unitid = "";
		String role = "";
		if (session.getAttribute("username") != null && !session.getAttribute("username").equals("")) {
			unitid = (String) session.getAttribute("unitid");
			role = (String) session.getAttribute("role");
		} else
			return false;

		Map properties = new HashMap();
		properties.put("buildname", name);
		properties.put("prename", prename);
		properties.put("rent", rent);
		properties.put("property", property);
		properties.put("buildarea", buildarea);
		properties.put("eartharea", eartharea);
		properties.put("rentarea", rentarea);
		properties.put("emptyarea", emptyarea);
		properties.put("buildright", right);
		properties.put("classvalue", classValue);
		properties.put("type", type);
		properties.put("service", service);
		properties.put("upfloor", upfloor);
		properties.put("totalfloor", totalfloor);
		properties.put("address", address);
		properties.put("postcode", postcode);
		properties.put("buildyear", buildyear);
		properties.put("devname", devname);
		properties.put("street", street);
		properties.put("cbd", cbd);
		properties.put("propertyname", propertyname);
		properties.put("propertytel", propertytel);
		properties.put("downfloor", downfloor);
		properties.put("toucheman", toucheman);
		properties.put("roomno", roomno);
		properties.put("email", email);
		properties.put("carno", carno);
		properties.put("statusvalue", role);
		properties.put("rununitid", unitid);
		properties.put("lon", lon);
		properties.put("lat", lat);
		properties.put("decoration", decoration);
		properties.put("niche", niche);
		properties.put("hotel", hotel);
		properties.put("bank", bank);
		properties.put("restaurant", restaurant);
		properties.put("traffic", traffic);
		properties.put("hospital", hospital);
		properties.put("intruduction", intruduction);
		properties.put("importarea", importarea);
		properties.put("classfy1", classfy);
		properties.put("runtime", runtime);
		properties.put("belongunit", belongunit);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);

		return mySQLDBHelper.update("t_build_basis", properties, "id=" + id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String name = request.getParameter("name");
		String classValue = request.getParameter("classValue");
		String type = request.getParameter("type");
		String right = request.getParameter("right");
		// String limit = request.getParameter("limit");
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

		String sql = "SELECT * from t_build_basis where statusvalue>=" + role;
		if (userType.equals("1"))
			sql += " and rununitid='" + unitid + "'";

		if (name != null && !name.trim().equals(""))
			sql += " and buildname like '%" + name + "%'";
		if (type != null && !type.trim().equals("") && !type.trim().equals("0"))
			sql += " and type='" + type + "'";
		if (classValue != null && !classValue.trim().equals("") && !classValue.trim().equals("0"))
			sql += " and classvalue='" + classValue + "'";
		if (right != null && !right.trim().equals("") && !right.trim().equals("0"))
			sql += " and buildright='" + right + "'";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, limit);
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

		String sql = "select * from t_build_basis where statusvalue<>0";
		if (userType.equals("1"))
			sql += " and rununitid='" + unitid + "'";

		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("t_build_basis", "id", id);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");

		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_build_basis", temp, "id=" + id);

	}

	@RequestMapping(value = "/vrurl", method = RequestMethod.POST)
	@ResponseBody
	public String vrurl(@RequestParam CommonsMultipartFile imgfile, HttpServletRequest request) {
		String id = request.getParameter("id");
		String url = request.getParameter("vrurl");

		HashMap temp = new HashMap();
		temp.put("vrurl", url);

		if (!imgfile.isEmpty()) {
			String fileName = imgfile.getOriginalFilename();

			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

			fileName = new Date().getTime() + "." + extension;

			if (extension != null) {

				String filePath = request.getSession().getServletContext().getRealPath("/") + "attached/" + id + "/";

				System.out.println("--------" + filePath);
				File f = new File(filePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				File savefile = new File(filePath + fileName);
				try {
					imgfile.transferTo(savefile);
				} catch (Exception e) {
					return "fail";
				}

				temp.put("vrpic", fileName);
			}
		}

		if (mySQLDBHelper.update("t_build_basis", temp, "id=" + id))
			return "success";
		else
			return "fail";

	}

	@RequestMapping(value = "/delvrurl", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delvrurl(HttpServletRequest request) {
		String id = request.getParameter("id");
		HashMap temp = new HashMap();
		temp.put("vrurl", "");
		temp.put("vrpic", "");
		return mySQLDBHelper.update("t_build_basis", temp, "id=" + id);

	}

	@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
	@ResponseBody
	public String uploadimg(@RequestParam CommonsMultipartFile imgfile, @RequestParam CommonsMultipartFile imgfile1,
			@RequestParam CommonsMultipartFile imgfile2, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String buildid = request.getParameter("buildid");
		String type = request.getParameter("type");

		if (imgfile.isEmpty() && imgfile1.isEmpty() && imgfile2.isEmpty()) {
			return "fail";
		} else {
			if (!imgfile.isEmpty()) {
				
				mySQLDBHelper.delete("t_build_img", "buildid=" + buildid + " and picorder=1");
				
				String fileName = imgfile.getOriginalFilename();

				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

				fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

				if (extension != null) {

					String filePath = request.getSession().getServletContext().getRealPath("/") + "attached/" + buildid
							+ "/";

					System.out.println("--------" + filePath);
					File f = new File(filePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					File savefile = new File(filePath + fileName);
					imgfile.transferTo(savefile);

					String sql = "insert into t_build_img (buildid,imgurl,type,statusvalue,picorder) values (" + buildid + ",'"
							+ fileName + "'," + type + ",1,1)";
					mySQLDBHelper.executeSQL(sql);
				} 
			}

			if (!imgfile1.isEmpty()) {
				mySQLDBHelper.delete("t_build_img", "buildid=" + buildid + " and picorder=2");
				
				String fileName = imgfile1.getOriginalFilename();

				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

				fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

				if (extension != null) {

					String filePath = request.getSession().getServletContext().getRealPath("/") + "attached/" + buildid
							+ "/";

					System.out.println("--------" + filePath);
					File f = new File(filePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					File savefile = new File(filePath + fileName);
					imgfile1.transferTo(savefile);

					String sql = "insert into t_build_img (buildid,imgurl,type,statusvalue,picorder) values (" + buildid + ",'"
							+ fileName + "'," + type + ",1,2)";
					mySQLDBHelper.executeSQL(sql);
				}
			}

			if (!imgfile2.isEmpty()) {
				
				mySQLDBHelper.delete("t_build_img", "buildid=" + buildid + " and picorder=3");
				
				String fileName = imgfile2.getOriginalFilename();

				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

				fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

				if (extension != null) {

					String filePath = request.getSession().getServletContext().getRealPath("/") + "attached/" + buildid
							+ "/";

					System.out.println("--------" + filePath);
					File f = new File(filePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					File savefile = new File(filePath + fileName);
					imgfile2.transferTo(savefile);

					String sql = "insert into t_build_img (buildid,imgurl,type,statusvalue,picorder) values (" + buildid + ",'"
							+ fileName + "'," + type + ",1,3)";
					mySQLDBHelper.executeSQL(sql);

				} 
			}
		}
		return "success";
	}

	@RequestMapping(value = "/img_get", method = RequestMethod.GET)
	@ResponseBody
	public Map img_get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String buildid = request.getParameter("buildid");
		String type = request.getParameter("type");
		Map result = mySQLDBHelper.retriveBySQL(
				"select * from t_build_img where statusvalue<>0 and buildid=" + buildid + " and type=" + type, false, 0,
				0);
		return result;

	}

	@RequestMapping(value = "/retriveimg", method = RequestMethod.GET)
	@ResponseBody
	public Map retriveimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		String date = sdf.format(new Date());
		Map result = mySQLDBHelper.retriveBySQL(
				"select i.*,b.buildname from t_build_img i,t_build_basis b,t_build_monthreport r where r.buildid=b.id and b.id=i.buildid and r.reportdate='"
						+ Common.lastMonth(date) + "' order by r.tax desc",
				true, 0, 5);
		String ids = "";
		if (result != null) {
			List temp = (List) result.get("data");
			if (temp != null && temp.size() > 0) {
				for (int i = 0, l = temp.size(); i < l; i++) {
					Map tempMap = (Map) temp.get(i);
					String id = tempMap.get("buildid").toString();
					ids += id + ",";
				}
			}
		}
		if (ids.lastIndexOf(",") == (ids.length() - 1)) {
			ids = ids.substring(0, ids.length() - 1);
		}

		List otherBuildList = mySQLDBHelper.retriveBySQL(
				"select i.*,b.buildname from t_build_basis b,t_build_img i where i.buildid=b.id and b.id not in (" + ids
						+ ")");
		if (otherBuildList != null && otherBuildList.size() > 0) {
			result.put("otherBuilds", otherBuildList);
		}
		return result;

	}

	@RequestMapping(value = "/build_detail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map build_detail(HttpServletRequest request) {
		String id = request.getParameter("buildid");
		Map result = mySQLDBHelper.retriveMapFromSQL(
				"select b.*,i.imgurl from t_build_basis b,t_build_img i where b.id=i.buildid and b.statusvalue<>0 and i.statusvalue<>0 and b.id="
						+ id);

		List unitNo = mySQLDBHelper
				.retriveBySQL("select id from t_build_unit where statusvalue <> 0 and buildid=" + id);
		if (unitNo != null)
			result.put("unitno", unitNo.size());
		else
			result.put("unitno", 0);

		List workman = mySQLDBHelper.retriveBySQL(
				"select workmannum ,emptyarea,rentarea from t_build_monthreport where statusvalue <> 0 and buildid="
						+ id + " order by reportdate desc");
		if (workman != null && workman.size() > 0) {
			Map temp = (Map) workman.get(0);
			if (temp.get("workmannum") != null && !temp.get("workmannum").equals(""))
				result.put("workmannum", temp.get("workmannum"));
			else
				result.put("workmannum", 0);

			if (temp.get("emptyarea") != null && !temp.get("emptyarea").equals(""))
				result.put("emptyarea", temp.get("emptyarea"));
			else
				result.put("emptyarea", 0);

			if (temp.get("rentarea") != null && !temp.get("rentarea").equals(""))
				result.put("rentarea", temp.get("rentarea"));
			else
				result.put("rentarea", 0);
		} else {
			result.put("rentarea", 0);
			result.put("emptyarea", 0);
			result.put("workmannum", 0);

		}
		return result;
	}

}
