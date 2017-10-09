package cn.com.tjly.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String ywyid = request.getParameter("ywyid");
		String ywyname = request.getParameter("ywyname");
		String mryname = request.getParameter("mryname");
		String mrybh = request.getParameter("mrybh");
		String wzfb = request.getParameter("wzfb");
		//String ljhxjf = request.getParameter("ljhxjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String banknum = request.getParameter("banknum");
		String bz = request.getParameter("bz");
		String lylx = request.getParameter("lylx");
		String lxr = request.getParameter("lxr");
		String initintegral = request.getParameter("initintegral");
		String isnew = request.getParameter("isnew");
		
		Map properties = new HashMap();
		properties.put("SalesmanID", ywyid);
		properties.put("SalesmanName", ywyname);
		properties.put("BeautyShopName", mryname);
		properties.put("BeautyShopNumber", mrybh);
		properties.put("BeautyShopPlace", wzfb);
		properties.put("SourceType", lylx);
		properties.put("Description", bz);
		//properties.put("TotalIntegral", ljhxjf);
		//properties.put("InitIntegral", ljhxjf);
		properties.put("TotalIntegral", initintegral);
		properties.put("BeautyShopAddress", address);
		properties.put("BeautyShopCity", cities);
		properties.put("RelationMan", lxr);
		properties.put("Telephone", telphone);
		properties.put("BankNum", banknum);
		properties.put("InitIntegral", initintegral);
		properties.put("DropBeautyShop", 0);

		properties.put("ManagerSalesmanID", ywyid);
		properties.put("ManagerSalesmanName", ywyname);
		properties.put("Status", 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("CreateDate", now);
		
		if(isnew.equals("true"))
			properties.put("IsNew", 1);
		else
			properties.put("IsNew", 0);
		long id = mySQLDBHelper.create("BeautyShop", properties);
		
		/*Map ywy = mySQLDBHelper.retriveByID("salesman", "ID", ywyid);
		Map salesmanprop = new HashMap();
		
		if(wzfb.equals("左"))
		{
			if(ywy.get("LeftIntegral") != null)
				salesmanprop.put("LeftIntegral", Integer.valueOf(ywy.get("LeftIntegral").toString()) + Integer.valueOf(initintegral));
			else
				salesmanprop.put("LeftIntegral", Integer.valueOf(initintegral));
			if(ywy.get("LeftTotalIntegral") != null)
			{
				salesmanprop.put("LeftTotalIntegral", Integer.valueOf(ywy.get("LeftTotalIntegral").toString()) + Integer.valueOf(initintegral));
				salesmanprop.put("SELFTotalLeftIntegral", Integer.valueOf(ywy.get("LeftTotalIntegral").toString()) + Integer.valueOf(initintegral));
			}
			else{
				salesmanprop.put("LeftTotalIntegral", Integer.valueOf(initintegral));
				salesmanprop.put("SELFTotalLeftIntegral", Integer.valueOf(initintegral));
				
			}
		}
		else if(wzfb.equals("右")){
			if(ywy.get("RightIntegral") != null)
				salesmanprop.put("RightIntegral", Integer.valueOf(ywy.get("RightIntegral").toString()) + Integer.valueOf(initintegral));
			else
				salesmanprop.put("RightIntegral", Integer.valueOf(initintegral));
			if(ywy.get("RightTotalIntegral") != null){
				salesmanprop.put("RightTotalIntegral", Integer.valueOf(ywy.get("RightTotalIntegral").toString()) + Integer.valueOf(initintegral));
				salesmanprop.put("SELFTotalRightIntegral", Integer.valueOf(ywy.get("RightTotalIntegral").toString()) + Integer.valueOf(initintegral));
			}
			else
			{
				salesmanprop.put("RightTotalIntegral", Integer.valueOf(initintegral));
				salesmanprop.put("SELFTotalRightIntegral", Integer.valueOf(initintegral));
			}
		}
		
		mySQLDBHelper.update("salesman", salesmanprop, "ID=" + ywyid);*/
		mySQLDBHelper.executeSQL("update salesman set SELFTotalShop=SELFTotalShop+1 where ID=" + ywyid);
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String ywyid = request.getParameter("ywyid");
		String ywyname = request.getParameter("ywyname");
		String mryname = request.getParameter("mryname");
		String mrybh = request.getParameter("mrybh");
		String wzfb = request.getParameter("wzfb");
		//String cshxjf = request.getParameter("cshxjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String bz = request.getParameter("bz");
		String lylx = request.getParameter("lylx");
		String lxr = request.getParameter("lxr");
		String banknum = request.getParameter("banknum");
		String ywyid1 = request.getParameter("ywyid1");
		String ywyname1 = request.getParameter("ywyname1");
		String wzfb1 = request.getParameter("wzfb1");
		String initintegral = request.getParameter("initintegral");
		
		Map result = mySQLDBHelper.retriveByID("BeautyShop", "ID", id);
		String salesmanID = result.get("SalesmanID").toString();
		if(!ywyid.equals(salesmanID)){
			mySQLDBHelper.executeSQL("update salesman set SELFTotalShop=SELFTotalShop+1 where ID=" + ywyid);
		}
		Map properties = new HashMap();
		properties.put("SalesmanID", ywyid);
		properties.put("SalesmanName", ywyname);
		properties.put("ManagerSalesmanID", ywyid);
		properties.put("ManagerSalesmanName", ywyname);
		
		properties.put("BeautyShopName", mryname);
		properties.put("BeautyShopNumber", mrybh);
		properties.put("BeautyShopPlace", wzfb);
		properties.put("SourceType", lylx);
		properties.put("Description", bz);
		properties.put("InitIntegral", initintegral);
		properties.put("TotalIntegral", initintegral);
		//properties.put("InitIntegral", 0);
		/*List result = mySQLDBHelper.retriveBySQL("select (TotalIntegral - InitIntegral) as tInt,InitIntegral as initInt from BeautyShop  where ID=" + id);
		int initIntegral = 0;
		if(result != null && result.size() > 0)
		{
			initIntegral = Integer.valueOf(((Map)result.get(0)).get("initInt").toString());
			properties.put("TotalIntegral",Integer.valueOf(((Map)result.get(0)).get("tInt").toString()) +  Integer.valueOf(initintegral));
		}else
		{
			properties.put("TotalIntegral", initintegral);
		}*/
		
		properties.put("BeautyShopAddress", address);
		properties.put("BeautyShopCity", cities);
		properties.put("RelationMan", lxr);
		properties.put("Telephone", telphone);
		properties.put("BankNum", banknum);
		String ywy = ywyid;
		if (ywyid1 != null && !ywyid1.equals("") && ywyname1 != null
				&& !ywyname1.equals("")) {
			properties.put("ManagerSalesmanID", ywyid1);
			properties.put("ManagerSalesmanName", ywyname1);
			properties.put("BeautyShopPlace", wzfb1);
			
			ywy = ywyid1;
		}
		
		/*Map ywyMap = mySQLDBHelper.retriveByID("salesman", "ID", ywy);
		Map salesmanprop = new HashMap();
		
		if(wzfb.equals("左")){
			salesmanprop.put("LeftIntegral", Integer.valueOf(ywyMap.get("LeftIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
			salesmanprop.put("LeftTotalIntegral", Integer.valueOf(ywyMap.get("LeftTotalIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
			salesmanprop.put("SELFTotalLeftIntegral", Integer.valueOf(ywyMap.get("LeftTotalIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
		}
		else if(wzfb.equals("右")){
			salesmanprop.put("RightIntegral", Integer.valueOf(ywyMap.get("RightIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
			salesmanprop.put("RightTotalIntegral", Integer.valueOf(ywyMap.get("RightTotalIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
			salesmanprop.put("SELFTotalRightIntegral", Integer.valueOf(ywyMap.get("RightTotalIntegral").toString()) - initIntegral + Integer.valueOf(initintegral));
		}
		mySQLDBHelper.update("salesman", salesmanprop, "ID=" + ywy);*/
		
		return mySQLDBHelper.update("BeautyShop", properties, "ID=" + id);
	}

	@RequestMapping(value = "/shoplist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userlist(HttpServletRequest request) {
		String ywyname = request.getParameter("ywyname");
		String mryname = request.getParameter("mryname");
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null
				&& !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "SELECT b.*,v.CreateDate as cDate from beautyshop as b LEFT JOIN (select * from verificationbd ORDER BY CreateDate desc limit 0,1) as v on b.ID=v.BeautyShopID where b.DropBeautyShop<>2 and b.Status<>1 ";
		if (ywyname != null && !ywyname.trim().equals(""))
			sql += " and (b.SalesmanName like '%" + ywyname + "%' or b.ManagerSalesmanName like '%" + ywyname + "%')";
		if (mryname != null && !mryname.trim().equals(""))
			sql += " and b.BeautyShopName like '%" + mryname + "%'";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/allshop", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List allshop(HttpServletRequest request) {
		String sql = "select * from BeautyShop where DropBeautyShop<>2 and Status<>1";
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/ywyallshop", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List ywyallshop(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "select * from BeautyShop where DropBeautyShop<>2 and Status<>1 and managersalesmanid=" + id;
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/shopinfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map shopinfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("BeautyShop", "ID", id);
		List l = mySQLDBHelper.retriveBySQL("select id from BeautyShop where DropBeautyShop<>2 and Status<>1 and SalesmanID=" + result.get("SalesmanID"));
		if(l != null)
			result.put("shopnum", l.size());
		else
			result.put("shopnum", 0);
		return result;
	}

	@RequestMapping(value = "/shopjf", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map shopjf(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (id != null && !id.equals("") && !id.equals("0")) {
			Map result = mySQLDBHelper.retriveByID("BeautyShop", "ID", id);
			String sql = "select sum(salesnum) as sn,ThisIntegral as integral from verificationbd where BeautyShopID= "
					+ id;
			List temp = mySQLDBHelper.retriveBySQL(sql);
			if (temp != null && temp.size() > 0) {
				Map m = (Map) temp.get(0);
				if (m.get("sn") != null)
					result.put("lastsalesnum", m.get("sn"));
				else
					result.put("lastsalesnum", 0);
				result.put("integral", m.get("integral"));
			}

			return result;
		} else
			return null;
	}

	@RequestMapping(value = "/shopchild", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean shopchild(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "select * from BeautyShop where DropBeautyShop<>2 and Status<>1 and salesmanid=" + id;
		List result = mySQLDBHelper.retriveBySQL(sql);
		if (result != null && result.size() > 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
		mySQLDBHelper.executeSQL("update salesman set SELFTotalShop=SELFTotalShop-1 where ID=(select salesmanid from beautyshop where ID=" + id + ")");
		
		HashMap temp = new HashMap();
		temp.put("Status", 1);
		return mySQLDBHelper.update("BeautyShop", temp, "ID=" + id);
		
		//return mySQLDBHelper.delete("BeautyShop", "ID=" + id);
	}
}
