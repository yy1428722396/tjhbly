package cn.com.tjly.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.business.calculate.AbstractCalculate;
import cn.com.business.calculate.CalulateIntegral;
import cn.com.business.calculate.FirstFourCalculate;
import cn.com.business.calculate.FirstOneCalculate;
import cn.com.business.calculate.FirstThreeCalculate;
import cn.com.business.calculate.FirstTwoCalculate;
import cn.com.business.calculate.SecondFourCalculate;
import cn.com.business.calculate.SecondOneCalculate;
import cn.com.business.calculate.SecondThreeCalculate;
import cn.com.business.calculate.SecondTwoCalculate;
import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/verification")
public class VerificationController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	private MySQLDBHelper mysqlDBHelperTask;

	public MySQLDBHelper getMysqlDBHelperTask() {
		return mysqlDBHelperTask;
	}

	public void setMysqlDBHelperTask(MySQLDBHelper mysqlDBHelperTask) {
		this.mysqlDBHelperTask = mysqlDBHelperTask;
	}

	// 10～19，1000；20～29，1500，30及以上，2000
	public int sumNewCash(int num, int prenum) {
		int money = 0;
		if (prenum < 30 && prenum >= 20) {
			if (num >= 30)
				money = 500;
		} else if (prenum < 20 && prenum >= 10) {
			if (num >= 30)
				money = 1000;
			else if (num < 30 && num >= 20)
				money = 500;
		} else if (prenum < 10 && prenum >= 0) {
			if (num >= 30)
				money = 2000;
			else if (num < 30 && num >= 20)
				money = 1500;
			else if (num < 20 && num >= 10)
				money = 1000;
		}
		return money;
	}

	public void sumIntegral4AllRelativeSalesman(String id, int integral) {
		Map salesman = mySQLDBHelper.retriveByID("salesman", "ID", id);
		String upsalesmanid = salesman.get("UpSalesmanID").toString();
		String place = salesman.get("SalesmanPlace").toString();

		while (!upsalesmanid.equals("0")) {
			Map temp = mySQLDBHelper.retriveByID("salesman", "ID", upsalesmanid);

			Map properties = new HashMap();
			if (place.equals("左")) {
				if (temp.get("LeftIntegral") != null)
					properties.put("LeftIntegral", Integer.valueOf(temp.get("LeftIntegral").toString()) + integral);
				else
					properties.put("LeftIntegral", integral);
				if (temp.get("LeftTotalIntegral") != null)
					properties.put("LeftTotalIntegral",
							Integer.valueOf(temp.get("LeftTotalIntegral").toString()) + integral);
				else
					properties.put("LeftTotalIntegral", integral);
			} else if (place.equals("右")) {
				if (temp.get("RightIntegral") != null)
					properties.put("RightIntegral", Integer.valueOf(temp.get("RightIntegral").toString()) + integral);
				else
					properties.put("RightIntegral", integral);
				if (temp.get("RightTotalIntegral") != null)
					properties.put("RightTotalIntegral",
							Integer.valueOf(temp.get("RightTotalIntegral").toString()) + integral);
				else
					properties.put("RightTotalIntegral", integral);
			}
			mySQLDBHelper.update("salesman", properties, "ID=" + upsalesmanid);
			upsalesmanid = temp.get("UpSalesmanID").toString();
			place = temp.get("SalesmanPlace").toString();
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String ywyid = request.getParameter("ywyid");
		String ywyname = request.getParameter("ywy");
		String mryname = request.getParameter("mryname");
		String mryid = request.getParameter("mryid");
		String ljhxjf = request.getParameter("ljhxjf");
		String hxbdjf = request.getParameter("hxbdjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String lxr = request.getParameter("lxr");
		String salesnum = request.getParameter("salesnum");
		String datetime = request.getParameter("datetime");

		if (salesnum == null || salesnum.equals(""))
			salesnum = "0";
		int totalhxbdjf = Integer.valueOf(salesnum) * 500;
		Map properties = new HashMap();

		int flag = 0;
		if (datetime != null && !datetime.trim().equals("")) {
			properties.put("CreateDate", datetime);
			Calendar starCal = Calendar.getInstance();
			int sMonth = starCal.get(Calendar.MONTH) + 1;
			if (datetime.indexOf("-") > 0) {
				String[] time = datetime.split("-");
				int mon = Integer.valueOf(time[1]);
				if (mon != sMonth)
					flag = 1;
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			properties.put("CreateDate", now);
		}

		properties.put("SalesmanID", ywyid);
		properties.put("SalesmanName", ywyname);
		properties.put("BeautyShopID", mryid);
		properties.put("BeautyShopName", mryname);

		if (hxbdjf != null && !hxbdjf.equals(""))
			properties.put("ThisIntegral", hxbdjf);
		else
			properties.put("ThisIntegral", 0);

		properties.put("salesnum", salesnum);
		if (ljhxjf != null && !ljhxjf.equals(""))
			properties.put("TotalIntegral", Integer.valueOf(ljhxjf));
		else
			properties.put("TotalIntegral", 0);
		properties.put("BeautyShopAddress", address);
		properties.put("BeautyShopCity", cities);
		properties.put("RelationMan", lxr);
		properties.put("Telephone", telphone);

		long id = mySQLDBHelper.create("VerificationBD", properties);

		Map shop = mySQLDBHelper.retriveByID("beautyshop", "ID", mryid);
		Map shopprop = new HashMap();
		shopprop.put("TotalIntegral", ljhxjf);
		shopprop.put("InitIntegral", ljhxjf);
		mySQLDBHelper.update("beautyshop", shopprop, "ID=" + mryid);

		String sql = "select sum(salesnum) as sn,ThisIntegral as integral from verificationbd where BeautyShopID= "
				+ mryid;
		int num = 0;
		List temp = mySQLDBHelper.retriveBySQL(sql);
		if (temp != null && temp.size() > 0) {
			Map m = (Map) temp.get(0);
			if (m.get("sn") != null)
				num = Integer.valueOf(m.get("sn").toString());
			else
				num = 0;
		}

		String devmanid = shop.get("SalesmanID").toString();
		String managerid = shop.get("ManagerSalesmanID").toString();
		String place = shop.get("BeautyShopPlace").toString();

		// 直接开发的店也是自己管理的店
		if (devmanid.equals(managerid)) {
			Map ywy = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);
			Map salesmanprop = new HashMap();
			if (place.equals("左")) {
				if (ywy.get("LeftIntegral") != null)
					salesmanprop.put("LeftIntegral",
							Integer.valueOf(ywy.get("LeftIntegral").toString()) + Integer.valueOf(hxbdjf));
				else
					salesmanprop.put("LeftIntegral", Integer.valueOf(hxbdjf));
				if (ywy.get("LeftTotalIntegral") != null) {
					salesmanprop.put("LeftTotalIntegral",
							Integer.valueOf(ywy.get("LeftTotalIntegral").toString()) + Integer.valueOf(hxbdjf));
					salesmanprop.put("SELFTotalLeftIntegral",
							Integer.valueOf(ywy.get("SELFTotalLeftIntegral").toString()) + Integer.valueOf(hxbdjf));
				} else {
					salesmanprop.put("LeftTotalIntegral", Integer.valueOf(hxbdjf));
					salesmanprop.put("SELFTotalLeftIntegral",
							Integer.valueOf(ywy.get("SELFTotalLeftIntegral").toString()) + Integer.valueOf(hxbdjf));
				}
			} else if (place.equals("右")) {
				if (ywy.get("RightIntegral") != null)
					salesmanprop.put("RightIntegral",
							Integer.valueOf(ywy.get("RightIntegral").toString()) + Integer.valueOf(hxbdjf));
				else
					salesmanprop.put("RightIntegral", Integer.valueOf(hxbdjf));
				if (ywy.get("RightTotalIntegral") != null) {
					salesmanprop.put("RightTotalIntegral",
							Integer.valueOf(ywy.get("RightTotalIntegral").toString()) + Integer.valueOf(hxbdjf));
					salesmanprop.put("SELFTotalRightIntegral",
							Integer.valueOf(ywy.get("SELFTotalRightIntegral").toString()) + Integer.valueOf(hxbdjf));
				} else {
					salesmanprop.put("RightTotalIntegral", Integer.valueOf(hxbdjf));
					salesmanprop.put("SELFTotalRightIntegral",
							Integer.valueOf(ywy.get("SELFTotalRightIntegral").toString()) + Integer.valueOf(hxbdjf));
				}
			}

			if (flag == 0) {
				if (ywy.get("ThisMonthSalesNum") != null)
					salesmanprop.put("ThisMonthSalesNum",
							Integer.valueOf(ywy.get("ThisMonthSalesNum").toString()) + Integer.valueOf(salesnum));
				else
					salesmanprop.put("ThisMonthSalesNum", Integer.valueOf(salesnum));
			}

			mySQLDBHelper.update("salesman", salesmanprop, "ID=" + devmanid);
			// 上级所有业务员都依次增加积分
			sumIntegral4AllRelativeSalesman(managerid, Integer.valueOf(salesnum) * 500);
		} else {

			String ywyid_temp = "";
			// 上级所有业务员都依次增加积分
			/*
			 * if (num <= 10) { if ((Integer.valueOf(salesnum) + num) > 10) {
			 * updateSelfIntegral(devmanid, place, Integer.valueOf(hxbdjf) - (10
			 * - num) * 500); sumIntegral4AllRelativeSalesman(devmanid,
			 * (Integer.valueOf(salesnum) - 10 + num) * 500); } } else {
			 * updateSelfIntegral(devmanid, place, Integer.valueOf(salesnum) *
			 * 500); sumIntegral4AllRelativeSalesman(devmanid,
			 * Integer.valueOf(salesnum) * 500); }
			 */

			// updateSelfIntegral(devmanid, place, Integer.valueOf(salesnum) *
			// 500);
			sumIntegral4AllRelativeSalesman(managerid, Integer.valueOf(salesnum) * 500);

			updateSelfIntegral(managerid, place, Integer.valueOf(hxbdjf));
			/*
			 * sumIntegral4AllRelativeSalesman(devmanid,
			 * Integer.valueOf(salesnum) * 500);
			 */

			Map dywy = mySQLDBHelper.retriveByID("salesman", "ID", managerid);
			Map dsalesmanprop = new HashMap();
			if (flag == 0) {
				if (dywy.get("ThisMonthSalesNum") != null)
					dsalesmanprop.put("ThisMonthSalesNum",
							Integer.valueOf(dywy.get("ThisMonthSalesNum").toString()) + Integer.valueOf(salesnum));
				else
					dsalesmanprop.put("ThisMonthSalesNum", Integer.valueOf(salesnum));

				mySQLDBHelper.update("salesman", dsalesmanprop, "ID=" + managerid);
			}

			Map dywy1 = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);

			if (flag == 0) {
				Map dsalesmanprop1 = new HashMap();
				if (dywy1.get("ThisMonthSalesNum") != null)
					dsalesmanprop1.put("ThisMonthSalesNum",
							Integer.valueOf(dywy1.get("ThisMonthSalesNum").toString()) + Integer.valueOf(salesnum));
				else
					dsalesmanprop1.put("ThisMonthSalesNum", Integer.valueOf(salesnum));

				mySQLDBHelper.update("salesman", dsalesmanprop1, "ID=" + devmanid);
			}

		}

		int isnew = Integer.valueOf(shop.get("IsNew").toString());
		int money = 0;
		if (isnew == 1) {
			int prenum = num - Integer.valueOf(salesnum);
			System.out.println("prenum : " + prenum + " ; this num:" + salesnum);
			money = sumNewCash(num, prenum);
			mySQLDBHelper.executeSQL("update salesman set GiveMoney=GiveMoney+" + money + " where id=" + devmanid);
		}
		if (id != 0)
			return true;
		else
			return false;
	}

	public void updateSelfIntegral(String ywyid_temp, String place, int hxbdjf) {
		System.out.println("---------" + place);
		Map mywy = mySQLDBHelper.retriveByID("salesman", "ID", ywyid_temp);
		Map msalesmanprop = new HashMap();

		if (place.equals("左")) {
			if (mywy.get("LeftIntegral") != null)
				msalesmanprop.put("LeftIntegral", Integer.valueOf(mywy.get("LeftIntegral").toString()) + hxbdjf);
			else
				msalesmanprop.put("LeftIntegral", hxbdjf);
			if (mywy.get("LeftTotalIntegral") != null) {
				msalesmanprop.put("LeftTotalIntegral",
						Integer.valueOf(mywy.get("LeftTotalIntegral").toString()) + hxbdjf);
				msalesmanprop.put("SELFTotalLeftIntegral",
						Integer.valueOf(mywy.get("SELFTotalLeftIntegral").toString()) + hxbdjf);
			} else {
				msalesmanprop.put("LeftTotalIntegral", Integer.valueOf(hxbdjf));
				msalesmanprop.put("SELFTotalLeftIntegral",
						Integer.valueOf(mywy.get("SELFTotalLeftIntegral").toString()) + hxbdjf);
			}
		} else if (place.equals("右")) {
			if (mywy.get("RightIntegral") != null)
				msalesmanprop.put("RightIntegral", Integer.valueOf(mywy.get("RightIntegral").toString()) + hxbdjf);
			else
				msalesmanprop.put("RightIntegral", hxbdjf);
			if (mywy.get("RightTotalIntegral") != null) {
				msalesmanprop.put("RightTotalIntegral",
						Integer.valueOf(mywy.get("RightTotalIntegral").toString()) + hxbdjf);
				msalesmanprop.put("SELFTotalRightIntegral",
						Integer.valueOf(mywy.get("SELFTotalRightIntegral").toString()) + hxbdjf);
			} else {
				msalesmanprop.put("RightTotalIntegral", hxbdjf);
				msalesmanprop.put("SELFTotalRightIntegral",
						Integer.valueOf(mywy.get("SELFTotalRightIntegral").toString()) + hxbdjf);
			}
		}
		mySQLDBHelper.update("salesman", msalesmanprop, "ID=" + ywyid_temp);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String ywyid = request.getParameter("ywyid");
		String ywyname = request.getParameter("ywy");
		String mryname = request.getParameter("mryname");
		String mryid = request.getParameter("mryid");
		String ljhxjf = request.getParameter("ljhxjf");
		String hxbdjf = request.getParameter("hxbdjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String lxr = request.getParameter("lxr");
		String salesnum = request.getParameter("salesnum");
		Map properties = new HashMap();
		/*
		 * properties.put("SalesmanID", ywyid); properties.put("SalesmanName",
		 * ywyname); properties.put("BeautyShopID", mryid);
		 * properties.put("BeautyShopName", mryname);
		 */
		properties.put("ThisIntegral", hxbdjf);
		properties.put("TotalIntegral", ljhxjf);
		properties.put("BeautyShopAddress", address);
		properties.put("BeautyShopCity", cities);
		properties.put("RelationMan", lxr);
		properties.put("Telephone", telphone);
		properties.put("salesnum", salesnum);

		Map ver = mySQLDBHelper.retriveByID("VerificationBD", "ID", id);
		String lhxbdjf = ver.get("ThisIntegral").toString();
		String lsalesnum = ver.get("salesnum").toString();

		String sql = "select sum(salesnum) as sn,ThisIntegral as integral from verificationbd where BeautyShopID= "
				+ id;
		int num = 0;
		List temp = mySQLDBHelper.retriveBySQL(sql);
		if (temp != null && temp.size() > 0) {
			Map m = (Map) temp.get(0);
			if (m.get("sn") != null)
				num = Integer.valueOf(m.get("sn").toString()) - Integer.valueOf(lsalesnum);
			else
				num = 0;
		}

		mySQLDBHelper.update("VerificationBD", properties, "ID=" + id);

		Map shop = mySQLDBHelper.retriveByID("beautyshop", "ID", mryid);
		if (shop != null) {
			Map shopprop = new HashMap();
			shopprop.put("TotalIntegral", Integer.valueOf(shop.get("TotalIntegral").toString())
					+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
			mySQLDBHelper.update("beautyshop", shopprop, "ID=" + mryid);

			String devmanid = shop.get("SalesmanID").toString();
			String managerid = shop.get("ManagerSalesmanID").toString();
			String place = shop.get("BeautyShopPlace").toString();

			if (devmanid.equals(managerid)) {
				Map ywy = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);
				Map salesmanprop = new HashMap();
				if (place.equals("左")) {
					salesmanprop.put("LeftTotalIntegral", Integer.valueOf(ywy.get("LeftTotalIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
					salesmanprop.put("LeftIntegral", Integer.valueOf(ywy.get("LeftIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
				} else if (place.equals("右")) {
					salesmanprop.put("RightTotalIntegral", Integer.valueOf(ywy.get("RightTotalIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
					salesmanprop.put("RightIntegral", Integer.valueOf(ywy.get("RightIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
				}

				// 上级所有业务员都依次增加积分
				sumIntegral4AllRelativeSalesman(managerid,
						Integer.valueOf(salesnum) * 500 - Integer.valueOf(lsalesnum) * 500);

				salesmanprop.put("ThisMonthSalesNum", Integer.valueOf(ywy.get("ThisMonthSalesNum").toString())
						+ Integer.valueOf(salesnum) - Integer.valueOf(lsalesnum));
				mySQLDBHelper.update("salesman", salesmanprop, "ID=" + devmanid);
			} else {
				Map mywy = mySQLDBHelper.retriveByID("salesman", "ID", managerid);
				Map msalesmanprop = new HashMap();

				if (place.equals("左")) {
					msalesmanprop.put("LeftIntegral", Integer.valueOf(mywy.get("LeftIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
					msalesmanprop.put("LeftTotalIntegral", Integer.valueOf(mywy.get("LeftTotalIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
				} else if (place.equals("右")) {
					msalesmanprop.put("RightIntegral", Integer.valueOf(mywy.get("RightIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));
					msalesmanprop.put("RightTotalIntegral", Integer.valueOf(mywy.get("RightTotalIntegral").toString())
							+ Integer.valueOf(hxbdjf) - Integer.valueOf(lhxbdjf));

				}
				mySQLDBHelper.update("salesman", msalesmanprop, "ID=" + managerid);

				// 上级所有业务员都依次增加积分
				if (num <= 10) {
					sumIntegral4AllRelativeSalesman(managerid,
							Integer.valueOf(salesnum) * 500 - Integer.valueOf(lsalesnum) * 500);
				} else {
					sumIntegral4AllRelativeSalesman(managerid,
							(Integer.valueOf(salesnum) - 10) * 500 - (Integer.valueOf(lsalesnum) - 10) * 500);
				}

				Map dywy = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);
				Map dsalesmanprop = new HashMap();
				dsalesmanprop.put("ThisMonthSalesNum", Integer.valueOf(dywy.get("ThisMonthSalesNum").toString())
						+ Integer.valueOf(salesnum) - Integer.valueOf(lsalesnum));
				mySQLDBHelper.update("salesman", dsalesmanprop, "ID=" + devmanid);

				Map dywy1 = mySQLDBHelper.retriveByID("salesman", "ID", managerid);
				Map dsalesmanprop1 = new HashMap();
				dsalesmanprop1.put("ThisMonthSalesNum", Integer.valueOf(dywy1.get("ThisMonthSalesNum").toString())
						+ Integer.valueOf(salesnum) - Integer.valueOf(lsalesnum));
				mySQLDBHelper.update("salesman", dsalesmanprop1, "ID=" + managerid);
			}

			int isnew = Integer.valueOf(shop.get("IsNew").toString());
			int money = 0;
			if (isnew == 1) {
				int delnum = num + Integer.valueOf(lsalesnum);
				int delprenum = num;
				int delmoney = sumNewCash(delnum, delprenum);
				mySQLDBHelper
						.executeSQL("update salesman set GiveMoney=GiveMoney-" + delmoney + " where id=" + devmanid);

				int editnum = num + Integer.valueOf(salesnum);
				int editprenum = num;
				int editmoney = sumNewCash(editnum, editprenum);
				mySQLDBHelper
						.executeSQL("update salesman set GiveMoney=GiveMoney+" + editmoney + " where id=" + devmanid);
			}
		}

		return true;
	}

	@RequestMapping(value = "/verificationlist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userlist(HttpServletRequest request) {
		String ywyname = request.getParameter("ywyname");
		String mryname = request.getParameter("mryname");
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "select * from VerificationBD where 1=1";
		if (ywyname != null && !ywyname.trim().equals(""))
			sql += " and SalesmanName like '%" + ywyname + "%'";
		if (mryname != null && !mryname.trim().equals(""))
			sql += " and BeautyShopName like '%" + mryname + "%'";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userinfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("VerificationBD", "ID", id);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");

		Map ver = mySQLDBHelper.retriveByID("VerificationBD", "ID", id);
		String datetime = ver.get("CreateDate").toString();
		int flag = 0;
		if (datetime != null && !datetime.trim().equals("")) {

			Calendar starCal = Calendar.getInstance();
			int sMonth = starCal.get(Calendar.MONTH) + 1;
			if (datetime.indexOf("-") > 0) {
				String[] time = datetime.split("-");
				int mon = Integer.valueOf(time[1]);
				if (mon != sMonth)
					flag = 1;
			}
		}

		String mryid = ver.get("BeautyShopID").toString();
		String hxbdjf = ver.get("ThisIntegral").toString();
		String salesnum = ver.get("salesnum").toString();

		Map shop = mySQLDBHelper.retriveByID("beautyshop", "ID", mryid);
		if (shop != null) {
			Map shopprop = new HashMap();
			if (shop.get("TotalIntegral") != null && hxbdjf != null) {
				shopprop.put("TotalIntegral",
						Integer.valueOf(shop.get("TotalIntegral").toString()) - Integer.valueOf(hxbdjf));
			}
			mySQLDBHelper.update("beautyshop", shopprop, "ID=" + mryid);

			String sql = "select sum(salesnum) as sn,ThisIntegral as integral from verificationbd where BeautyShopID= "
					+ mryid;
			int num = 0;
			List temp = mySQLDBHelper.retriveBySQL(sql);
			if (temp != null && temp.size() > 0) {
				Map m = (Map) temp.get(0);
				if (m.get("sn") != null)
					num = Integer.valueOf(m.get("sn").toString());
				else
					num = 0;
			}

			String devmanid = shop.get("SalesmanID").toString();
			String managerid = shop.get("ManagerSalesmanID").toString();
			String place = shop.get("BeautyShopPlace").toString();

			if (devmanid.equals(managerid)) {
				Map ywy = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);
				Map salesmanprop = new HashMap();

				if (place.equals("左")) {
					salesmanprop.put("LeftIntegral",
							Integer.valueOf(ywy.get("LeftIntegral").toString()) - Integer.valueOf(hxbdjf));
					salesmanprop.put("LeftTotalIntegral",
							Integer.valueOf(ywy.get("LeftTotalIntegral").toString()) - Integer.valueOf(hxbdjf));
				} else if (place.equals("右")) {
					salesmanprop.put("RightIntegral",
							Integer.valueOf(ywy.get("RightIntegral").toString()) - Integer.valueOf(hxbdjf));
					salesmanprop.put("RightTotalIntegral",
							Integer.valueOf(ywy.get("RightTotalIntegral").toString()) - Integer.valueOf(hxbdjf));
				}

				// 上级所有业务员都依次增加积分
				sumIntegral4AllRelativeSalesman(managerid, -Integer.valueOf(salesnum) * 500);

				if (flag == 0) {
					salesmanprop.put("ThisMonthSalesNum",
							Integer.valueOf(ywy.get("ThisMonthSalesNum").toString()) - Integer.valueOf(salesnum));
				}
				mySQLDBHelper.update("salesman", salesmanprop, "ID=" + devmanid);
			} else {
				Map mywy = mySQLDBHelper.retriveByID("salesman", "ID", managerid);
				Map msalesmanprop = new HashMap();
				/*
				 * msalesmanprop.put("TotalIntegral",
				 * Integer.valueOf(mywy.get("TotalIntegral").toString()) -
				 * Integer.valueOf(hxbdjf));
				 */
				if (place.equals("左"))
					msalesmanprop.put("LeftIntegral",
							Integer.valueOf(mywy.get("LeftIntegral").toString()) - Integer.valueOf(hxbdjf));
				else if (place.equals("右"))
					msalesmanprop.put("RightIntegral",
							Integer.valueOf(mywy.get("RightIntegral").toString()) - Integer.valueOf(hxbdjf));
				mySQLDBHelper.update("salesman", msalesmanprop, "ID=" + managerid);

				// 上级所有业务员都依次增加积分
				/*
				 * if (num <= 10) { sumIntegral4AllRelativeSalesman(devmanid,
				 * -Integer.valueOf(salesnum) * 500); } else {
				 * //sumIntegral4AllRelativeSalesman(devmanid, -10 * 500);
				 * sumIntegral4AllRelativeSalesman(devmanid,
				 * -(Integer.valueOf(salesnum) - 10) * 500); }
				 */

				mySQLDBHelper.update("salesman", msalesmanprop, "ID=" + devmanid);
				sumIntegral4AllRelativeSalesman(managerid, -Integer.valueOf(salesnum) * 500);

				if (flag == 0) {
					Map dywy = mySQLDBHelper.retriveByID("salesman", "ID", devmanid);
					Map dsalesmanprop = new HashMap();
					dsalesmanprop.put("ThisMonthSalesNum",
							Integer.valueOf(dywy.get("ThisMonthSalesNum").toString()) - Integer.valueOf(salesnum));
					mySQLDBHelper.update("salesman", dsalesmanprop, "ID=" + devmanid);
				
					Map dywy1 = mySQLDBHelper.retriveByID("salesman", "ID", managerid);

					Map dsalesmanprop1 = new HashMap();
					dsalesmanprop1.put("ThisMonthSalesNum",
							Integer.valueOf(dywy1.get("ThisMonthSalesNum").toString()) - Integer.valueOf(salesnum));
					mySQLDBHelper.update("salesman", dsalesmanprop1, "ID=" + managerid);
				}

			}

			int isnew = Integer.valueOf(shop.get("IsNew").toString());
			int money = 0;
			if (isnew == 1) {
				int prenum = num - Integer.valueOf(salesnum);
				System.out.println("prenum : " + prenum + " ; this num:" + salesnum);
				money = sumNewCash(num, prenum);
				mySQLDBHelper.executeSQL("update salesman set GiveMoney=GiveMoney-" + money + " where id=" + devmanid);
			}
		}

		return mySQLDBHelper.delete("VerificationBD", "ID=" + id);

	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map all(HttpServletRequest request) {
		String date = request.getParameter("date");
		String person = request.getParameter("person");

		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "select * from resultfinal where 1=1";
		if (date != null && !date.equals(""))
			sql += " and finaldate='" + date + "'";
		if (person != null && !person.equals(""))
			sql += " and workuser like '%" + person + "%'";
		sql += " order by finaldate desc";
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/sum", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean sum(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("realname") == null)
			return false;
		String realname = session.getAttribute("realname").toString();
		Map prop = new HashMap();
		prop.put("workuser", realname);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		prop.put("finaldate", sdf.format(new Date()));
		long id = mySQLDBHelper.create("resultfinal", prop);

		List salesman = mySQLDBHelper.retriveBySQL("select * from salesman where Status <> 1");

		if (salesman != null && salesman.size() > 0) {
			for (int i = 0, l = salesman.size(); i < l; i++) {
				Map sale = (Map) salesman.get(i);

				// 获取业务员的基本信息
				Map properties = new HashMap();
				properties.put("salesmanid", sale.get("ID").toString());
				properties.put("salesmanname", sale.get("salesmanname").toString());
				properties.put("salesmannumber", sale.get("salesmannumber").toString());
				if (sale.get("lefttotalintegral") != null)
					properties.put("lefttotalintegral", sale.get("lefttotalintegral").toString());
				else
					properties.put("lefttotalintegral", 0);
				if (sale.get("righttotalintegral") != null)
					properties.put("righttotalintegral", sale.get("righttotalintegral").toString());
				else
					properties.put("righttotalintegral", 0);
				properties.put("telephone", sale.get("telephone").toString());
				properties.put("createdate", sdf.format(new Date()));
				properties.put("ResultFinalID", id);

				// 查找该业务员左侧美容院的数量
				List leftshop = mySQLDBHelper
						.retriveBySQL("select * from beautyshop where DropBeautyShop<>2 and Status<>1 and salesmanid="
								+ sale.get("id") + " and beautyshopplace='左'");
				if (leftshop != null)
					properties.put("leftbeautyshopnum", leftshop.size());
				else
					properties.put("leftbeautyshopnum", 0);

				// 查找该业务员右侧美容院的数量
				List rightshop = mySQLDBHelper
						.retriveBySQL("select * from beautyshop where DropBeautyShop<>2 and Status<>1 and salesmanid="
								+ sale.get("id") + " and beautyshopplace='右'");
				if (rightshop != null)
					properties.put("rightbeautyshopnum", rightshop.size());
				else
					properties.put("rightbeautyshopnum", 0);

				// 查找该业务员美容院的总数量
				int shopNum = 0;
				if (rightshop != null && leftshop != null) {
					shopNum = rightshop.size() + leftshop.size();
				}

				// 计算该业务员此次计算的销售的套数
				int salesNum = 0;
				if (sale.get("ThisMonthSalesNum") != null) {
					salesNum = Integer.valueOf(sale.get("ThisMonthSalesNum").toString());
				}
				String sqlsum = "select sum(leftthisintegral) as leftsum,sum(rightthisintegral) as rightsum,sum(ThisMoney) as moneysum from resultfinalview where flag=1 and salesmanid="
						+ sale.get("ID").toString();
				List sumList = mySQLDBHelper.retriveBySQL(sqlsum);
				if (sumList != null) {
					Map sum = (Map) sumList.get(0);
					if (sum.get("leftsum") != null)
						properties.put("LeftFinalIntegral", sum.get("leftsum").toString());
					else
						properties.put("LeftFinalIntegral", 0);
					if (sum.get("rightsum") != null)
						properties.put("RightFinalIntegral", sum.get("rightsum").toString());
					else
						properties.put("RightFinalIntegral", 0);
					if (sum.get("moneysum") != null)
						properties.put("TotalMoney", sum.get("moneysum").toString());
					else
						properties.put("TotalMoney", 0);
				}
				if (sale.get("leftintegral") != null)
					properties.put("leftthisintegral", sale.get("leftintegral").toString());
				else
					properties.put("leftthisintegral", 0);

				if (sale.get("rightintegral") != null)
					properties.put("rightthisintegral", sale.get("rightintegral").toString());
				else
					properties.put("rightthisintegral", 0);
				int leftIntegral = Integer.valueOf(sale.get("leftintegral").toString());
				int rightIntegral = Integer.valueOf(sale.get("rightintegral").toString());

				Calendar calendar = Calendar.getInstance();
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				int moneyCount = 0;

				String ids = "";
				if (shopNum >= 5 && salesNum >= 30) {
					String sql = "select id,PreMoney money from resultfinalview where ((needsalenum>=20 and needshopnum>=3) or needshopnum=2) and PreMoney > 0 and moneyflag<>1 and createdate < '"
							+ year + "-" + ((month + 1) >= 10 ? (month + 1) : "0" + (month + 1))
							+ "-01' and createdate >= '" + year + "-" + (month >= 10 ? month : "0" + month)
							+ "-01' and salesmanid='" + sale.get("ID").toString() + "'";
					List money = mySQLDBHelper.retriveBySQL(sql);
					if (money != null && money.size() > 0) {
						for (int j = 0, s = money.size(); j < s; j++) {
							Map m = (Map) money.get(j);
							int money_value = Integer.valueOf(m.get("money").toString());
							moneyCount += money_value;
							String rid = m.get("id").toString();
							ids = ids + "," + rid;
						}
					}
				} else if (shopNum >= 3 && salesNum >= 20) {
					String sql = "select id,PreMoney money from resultfinalview where ((needsalenum=20 and needshopnum=3) or needshopnum=2) and PreMoney > 0 and moneyflag<>1 and createdate < '"
							+ year + "-" + ((month + 1) >= 10 ? (month + 1) : "0" + (month + 1))
							+ "-01' and createdate >= '" + year + "-" + (month >= 10 ? month : "0" + month)
							+ "-01' and salesmanid='" + sale.get("ID").toString() + "'";
					List money = mySQLDBHelper.retriveBySQL(sql);

					if (money != null && money.size() > 0) {
						for (int j = 0, s = money.size(); j < s; j++) {
							Map m = (Map) money.get(j);
							int money_value = Integer.valueOf(m.get("money").toString());
							moneyCount += money_value;
							String rid = m.get("id").toString();
							ids = ids + "," + rid;
						}
					}
				} else if (shopNum >= 2) {
					String sql = "select id,PreMoney money from resultfinalview where needshopnum=2 and PreMoney > 0 and moneyflag<>1 and createdate < '"
							+ year + "-" + ((month + 1) >= 10 ? (month + 1) : "0" + (month + 1))
							+ "-01' and createdate >= '" + year + "-" + (month >= 10 ? month : "0" + month)
							+ "-01' and salesmanid='" + sale.get("ID").toString() + "'";
					List money = mySQLDBHelper.retriveBySQL(sql);

					if (money != null && money.size() > 0) {
						for (int j = 0, s = money.size(); j < s; j++) {
							Map m = (Map) money.get(j);
							int money_value = Integer.valueOf(m.get("money").toString());
							moneyCount += money_value;

							String rid = m.get("id").toString();
							ids = ids + "," + rid;
						}
					}
				}

				if (((rightIntegral >= 250 * 500 && leftIntegral >= 350 * 500)
						|| (rightIntegral >= 350 * 500 && leftIntegral >= 250 * 500)
						|| (rightIntegral >= 300 * 500 && leftIntegral >= 300 * 500))) {
					if (shopNum >= 5 && salesNum >= 30) {
						properties.put("ThisMoney", (400 + 1200 + 3000 + 12000) * 2
								+ Integer.valueOf(sale.get("GiveMoney").toString()) + moneyCount);
						if (Integer.valueOf(sale.get("GiveMoney").toString()) > 0)
							properties.put("ExplainInfo",
									"两轮循环+新店开发:" + Integer.valueOf(sale.get("GiveMoney").toString()));
						else
							properties.put("ExplainInfo", "两轮循环");
						properties.put("PreMoney", 0);

						mySQLDBHelper.create("resultfinalview", properties);

						Map pro = new HashMap();
						pro.put("GiveMoney", 0);
						pro.put("CalculateProcess", 4);
						pro.put("MonthProcess", 4);
						mySQLDBHelper.update("salesman", pro, "ID=" + sale.get("ID").toString());
					} else {
						properties.put("PreMoney", (400 + 1200 + 3000 + 12000) * 2);
						properties.put("ThisMoney", Integer.valueOf(sale.get("GiveMoney").toString()) + moneyCount);
						properties.put("NeedShopNum", 5);
						properties.put("NeedSaleNum", 30);
						mySQLDBHelper.create("resultfinalview", properties);

						Map pro = new HashMap();
						pro.put("GiveMoney", 0);
						pro.put("CalculateProcess", 4);
						pro.put("MonthProcess", 4);

						mySQLDBHelper.update("salesman", pro, "ID=" + sale.get("ID").toString());
					}
					mySQLDBHelper.executeSQL("update salesman set SELFTotalCount=SELFTotalCount+2 where ID="
							+ sale.get("ID").toString());

					CalulateIntegral c = new CalulateIntegral(mySQLDBHelper);
					c.updateSELFIntegral(sale.get("ID").toString(), 0, 0);
					c.updateThisIntegral(sale.get("ID").toString(), 0, 0);

				} else {
					int process = Integer.valueOf(sale.get("CalculateProcess").toString());
					AbstractCalculate calculate = null;
					if (process == 0)
						calculate = new FirstOneCalculate(new FirstTwoCalculate(new FirstThreeCalculate(
								new FirstFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(
										new SecondThreeCalculate(new SecondFourCalculate(null))))))));
					else if (process == 1)
						calculate = new FirstTwoCalculate(new FirstThreeCalculate(new FirstFourCalculate(
								new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(
										new SecondFourCalculate(new SecondOneCalculate(null))))))));
					else if (process == 2)
						calculate = new FirstThreeCalculate(new FirstFourCalculate(new SecondOneCalculate(
								new SecondTwoCalculate(new SecondThreeCalculate(new SecondFourCalculate(
										new SecondOneCalculate(new SecondTwoCalculate(null))))))));
					else if (process == 3)
						calculate = new FirstFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(
								new SecondThreeCalculate(new SecondFourCalculate(new SecondOneCalculate(
										new SecondTwoCalculate(new SecondThreeCalculate(null))))))));
					else if (process == 4)
						calculate = new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(
								new SecondFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(
										new SecondThreeCalculate(new SecondFourCalculate(null))))))));
					else if (process == 5)
						calculate = new SecondTwoCalculate(new SecondThreeCalculate(new SecondFourCalculate(
								new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(
										new SecondFourCalculate(new SecondOneCalculate(null))))))));
					else if (process == 6)
						calculate = new SecondThreeCalculate(new SecondFourCalculate(new SecondOneCalculate(
								new SecondTwoCalculate(new SecondThreeCalculate(new SecondFourCalculate(
										new SecondOneCalculate(new SecondTwoCalculate(null))))))));
					else if (process == 7)
						calculate = new SecondFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(
								new SecondThreeCalculate(new SecondFourCalculate(new SecondOneCalculate(
										new SecondTwoCalculate(new SecondThreeCalculate(null))))))));

					calculate.setLeftIntegral(leftIntegral);
					calculate.setRightIntegral(rightIntegral);
					calculate.setSalesmanID(sale.get("ID").toString());
					calculate.setSalsNum(salesNum);
					calculate.setShopNum(shopNum);
					calculate.setMoney(0);
					calculate.setPremoney(0);
					calculate.setCalculateProcess(process);
					calculate.setTempProcess(process);
					calculate.setMonthProcess(process);
					calculate.setIntegral(new CalulateIntegral(mySQLDBHelper));
					calculate.setDescription("");
					calculate.calculate();

					/*
					 * int initmoney = 0; if(sale.get("InitMoney") != null)
					 * initmoney =
					 * Integer.valueOf(sale.get("InitMoney").toString());
					 * 
					 * int money = calculate.getMoney(); int flag = 0;
					 * switch(initmoney){ case 0: if(salesNum >= 30){ money +=
					 * 2000; flag = 3; } else if(salesNum >= 20 && salesNum <
					 * 30){ money += 1500; flag = 2; } else if(salesNum >= 10 &&
					 * salesNum < 20){ money += 1000; flag = 1; } break; case 1:
					 * if(salesNum >= 30){ money += 1000; flag = 3; } else
					 * if(salesNum >= 20 && salesNum < 30){ money += 500; flag =
					 * 2; }else flag = 1; break; case 2: if(salesNum >= 30){
					 * money += 500; flag = 3; }else flag = 2; break; default:
					 * flag = 3; break; } properties.put("ThisMoney", money);
					 * Map pro = new HashMap(); pro.put("InitMoney", flag);
					 * mySQLDBHelper.update("salesman", pro, "ID=" +
					 * sale.get("ID").toString());
					 */

					properties.put("ThisMoney",
							calculate.getMoney() + Integer.valueOf(sale.get("GiveMoney").toString()) + moneyCount);
					if (Integer.valueOf(sale.get("GiveMoney").toString()) > 0)
						properties.put("ExplainInfo", calculate.getDescription() + "+新店开发:"
								+ Integer.valueOf(sale.get("GiveMoney").toString()));
					else
						properties.put("ExplainInfo", calculate.getDescription());
					if (moneyCount > 0) {
						properties.put("ExplainInfo", "本月预发奖金满足要求的" + moneyCount + calculate.getDescription());
					}
					properties.put("PreMoney", calculate.getPremoney());
					properties.put("NeedSaleNum", calculate.getNeedSaleNum());
					properties.put("NeedShopNum", calculate.getNeedShopNum());
					properties.put("moneyflag", 0);
					mySQLDBHelper.create("resultfinalview", properties);

					// 如果预结算符合条件了，那么更改其下次结算比较条件的状态，也就是说不会重复比较预结算的条件。
					if (!ids.equals("")) {
						Map p = new HashMap();
						p.put("moneyflag", 1);
						String[] rids = ids.split(",");
						for (int m = 0, s = rids.length; m < s; m++) {
							if (rids[m] != null && !rids[m].equals(""))
								mySQLDBHelper.update("resultfinalview", p, "id=" + rids[m]);
						}
					}

					Map pro = new HashMap();
					pro.put("GiveMoney", 0);
					pro.put("CalculateProcess", calculate.getCalculateProcess());
					pro.put("MonthProcess", calculate.getMonthProcess());
					mySQLDBHelper.update("salesman", pro, "ID=" + sale.get("ID").toString());
				}
			}
		}
		return true;
	}

	public void updateThisIntegral(String id, double leftintegral, double rightintegral) {
		Map properties = new HashMap();
		properties.put("LeftIntegral", leftintegral);
		properties.put("rightIntegral", rightintegral);
		mySQLDBHelper.update("salesman", properties, "ID=" + id);
		mySQLDBHelper.executeSQL("update salesman set SELFTotalCount=SELFTotalCount+1 where ID=" + id);

	}

	@RequestMapping(value = "/everydetail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map everydetail(HttpServletRequest request) {
		String id = request.getParameter("id");
		String flag = request.getParameter("f");
		String flag1 = request.getParameter("f1");
		String ywy = request.getParameter("ywy");
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		String sql = "select ID, SalesmanName,TotalIntegral,TotalMoney,ThisMoney,PreMoney,Telephone,CreateDate, (ThisMoney + PreMoney) as ThisTotalMoney ,ExplainInfo from resultfinalview where resultfinalid="
				+ id;

		if (flag1 != null && !flag1.equals("")) {
			if (flag1.equals("0"))
				sql += " and (ThisMoney > 0 or PreMoney > 0)";
		} else if (flag.equals("0")) {
			sql += " and (ThisMoney > 0 or PreMoney > 0)";
		}

		if (ywy != null && !ywy.equals(""))
			sql += " and SalesmanName like '%" + ywy + "%'";
		System.out.println("---ywy----:" + ywy + ";sql:" + sql);
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/shopsum", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map shopsum(HttpServletRequest request) {
		String id = request.getParameter("id");

		Map result = mySQLDBHelper.retriveByID("resultfinalview", "ID", id);
		return result;
	}

	@RequestMapping(value = "/integralinfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map integralinfo(HttpServletRequest request) {
		String id = request.getParameter("id");

		Map result = mySQLDBHelper.retriveByID("salesman", "ID", id);
		return result;
	}

	@RequestMapping(value = "/addinfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean addinfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		String allcount = request.getParameter("allcount");
		String lefttotalintegral = request.getParameter("lefttotalintegral");
		String righttotalintegral = request.getParameter("righttotalintegral");
		String leftfinalintegral = request.getParameter("leftfinalintegral");
		String rightfinalintegral = request.getParameter("rightfinalintegral");

		Map properties = new HashMap();
		properties.put("SELFTotalLeftIntegral", lefttotalintegral);
		properties.put("SELFTotalRightIntegral", righttotalintegral);
		properties.put("SELFTotalCount", allcount);
		properties.put("SELFTotalUsedLeftIntegral", leftfinalintegral);
		properties.put("SELFTotalUsedRightIntegral", rightfinalintegral);

		return mySQLDBHelper.update("salesman", properties, "ID=" + id);
	}

	public static int getMonth(String startDate) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		int monthday = 0;
		try {
			// 开始时间与今天相比较
			Date endDate = new Date();

			Calendar starCal = Calendar.getInstance();
			starCal.setTime(f.parse(startDate));

			int sYear = starCal.get(Calendar.YEAR);
			int sMonth = starCal.get(Calendar.MONTH);
			int sDay = starCal.get(Calendar.DATE);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			int eYear = endCal.get(Calendar.YEAR);
			int eMonth = endCal.get(Calendar.MONTH);
			int eDay = endCal.get(Calendar.DATE);

			monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

			if (sDay > eDay) {
				monthday = monthday - 1;
			}
			return monthday;
		} catch (ParseException e) {
			monthday = 0;
		}
		return monthday;
	}

	public void monthsum() {
		List shops = mysqlDBHelperTask
				.retriveBySQL("select ID,CreateDate from beautyshop where DropBeautyShop<>2 and Status<>1");
		if (shops != null) {
			for (int i = 0, l = shops.size(); i < l; i++) {
				Map shop = (Map) shops.get(i);
				String id = shop.get("ID").toString();
				String date = shop.get("CreateDate").toString();
				List bd = mysqlDBHelperTask.retriveBySQL("select ID,CreateDate from verificationbd where beautyshopid="
						+ id + " ORDER BY CreateDate desc LIMIT 0,1");
				if (bd != null && bd.size() > 0) {
					Map bdMap = (Map) bd.get(0);
					String sdate = bdMap.get("CreateDate").toString();

					int dis = getMonth(sdate);
					Map properties = new HashMap();

					if (dis >= 6) {
						properties.put("DropBeautyShop", 2);
						mysqlDBHelperTask.update("beautyshop", properties, " id=" + id);
					} else if (dis >= 5) {
						properties.put("DropBeautyShop", 1);
						mysqlDBHelperTask.update("beautyshop", properties, " id=" + id);
					}
				} else {
					int dis = getMonth(date);
					Map properties = new HashMap();

					if (dis >= 6) {
						properties.put("DropBeautyShop", 2);
						mysqlDBHelperTask.update("beautyshop", properties, " id=" + id);
					} else if (dis >= 5) {
						properties.put("DropBeautyShop", 1);
						mysqlDBHelperTask.update("beautyshop", properties, " id=" + id);
					}
				}
			}
		}

	}

	@RequestMapping(value = "/schedule_check", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean schedule_check(HttpServletRequest request) {
		Map prop = new HashMap();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String yearmonth = cal.get(Calendar.YEAR) + "" + (month < 10 ? "0" + month : month);
		List result = mySQLDBHelper.retriveBySQL("select * from schedulelog where yearmonth=" + yearmonth);
		if(result != null && result.size() > 0){
			return true;
		}else
			return false;
	}
	
	@RequestMapping(value = "/monthSchedule", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void monthSchedule() {
		Map properties = new HashMap();
		properties.put("ThisMonthSalesNum", 0);
		// properties.put("CalculateProcess", 0);
		mySQLDBHelper.update("salesman", properties, "1=1");
		Map prop = new HashMap();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String yearmonth = cal.get(Calendar.YEAR) + "" + (month < 10 ? "0" + month : month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		prop.put("yearmonth", yearmonth);
		prop.put("createtime", sdf.format(now));
		mySQLDBHelper.create("schedulelog", prop);
		//mysqlDBHelperTask.executeSQL("update salesman set CalculateProcess=MonthProcess");
	}
	
	public void thismonthnum() {
		Map properties = new HashMap();
		properties.put("ThisMonthSalesNum", 0);
		// properties.put("CalculateProcess", 0);
		mysqlDBHelperTask.update("salesman", properties, "1=1");
		Map prop = new HashMap();
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		String yearmonth = cal.get(Calendar.YEAR) + "" + (month < 10 ? "0" + month : month);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		prop.put("yearmonth", yearmonth);
		prop.put("createtime", sdf.format(now));
		mysqlDBHelperTask.create("schedulelog", prop);
		//mysqlDBHelperTask.executeSQL("update salesman set CalculateProcess=MonthProcess");
	}

	public long sum(String realname) {

		Map prop = new HashMap();
		prop.put("workuser", realname);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		prop.put("finaldate", sdf.format(new Date()));
		long id = mySQLDBHelper.create("resultfinal", prop);

		List salesman = mySQLDBHelper.retriveBySQL("select * from salesman where Status <> 1");
		if (salesman != null && salesman.size() > 0) {
			for (int i = 0, l = salesman.size(); i < l; i++) {
				int count = 0;
				double leftInt = 0;
				double rightInt = 0;
				String explain = "";

				Map sale = (Map) salesman.get(i);
				Map properties = new HashMap();
				properties.put("salesmanid", sale.get("ID").toString());
				properties.put("salesmanname", sale.get("salesmanname").toString());
				properties.put("salesmannumber", sale.get("salesmannumber").toString());
				if (sale.get("lefttotalintegral") != null)
					properties.put("lefttotalintegral", sale.get("lefttotalintegral").toString());
				else
					properties.put("lefttotalintegral", 0);
				if (sale.get("righttotalintegral") != null)
					properties.put("righttotalintegral", sale.get("righttotalintegral").toString());
				else
					properties.put("righttotalintegral", 0);

				if (sale.get("leftintegral") == null || sale.get("rightintegral") == null)
					continue;

				properties.put("telephone", sale.get("telephone").toString());
				properties.put("createdate", sdf.format(new Date()));
				List leftshop = mySQLDBHelper
						.retriveBySQL("select * from beautyshop where DropBeautyShop<>2 and Status<>1 and salesmanid="
								+ sale.get("id") + " and beautyshopplace='左'");
				if (leftshop != null)
					properties.put("leftbeautyshopnum", leftshop.size());
				else
					properties.put("leftbeautyshopnum", 0);
				List rightshop = mySQLDBHelper
						.retriveBySQL("select * from beautyshop where DropBeautyShop<>2 and Status<>1 and salesmanid="
								+ sale.get("id") + " and beautyshopplace='右'");
				if (rightshop != null)
					properties.put("rightbeautyshopnum", rightshop.size());
				else
					properties.put("rightbeautyshopnum", 0);

				int leftintegral = Integer.valueOf(sale.get("leftintegral").toString());
				int rightintegral = Integer.valueOf(sale.get("rightintegral").toString());
				properties.put("ResultFinalID", id);
				properties.put("TotalIntegral", leftintegral + rightintegral);

				int shopNum = 0;
				if (rightshop != null && leftshop != null) {
					shopNum = rightshop.size() + leftshop.size();
				}

				int salesNum = 0;
				if (sale.get("ThisMonthSalesNum") != null) {
					salesNum = Integer.valueOf(sale.get("ThisMonthSalesNum").toString());
				}

				if (shopNum < 2 || salesNum < 20) {
					continue;
				}

				if (shopNum >= 2 && salesNum >= 20) {
					if (leftintegral >= 12.5 * 500 && rightintegral >= 12.5 * 500) {
						count += 400;
						leftInt = 12.5 * 500;
						rightInt = 12.5 * 500;
					}

					if (leftintegral >= 27.5 * 500 && rightintegral >= 27.5 * 500) {
						count += 1200;
						leftInt = 27.5 * 500;
						rightInt = 27.5 * 500;
					}
					if (leftintegral >= 80 * 500 && rightintegral >= 80 * 500) {
						count += 3000;
						leftInt = 80 * 500;
						rightInt = 80 * 500;
					}
					if (count == 0)
						explain = "左右店的积分没有达到要求";
				} else {
					if (shopNum < 2)
						explain = "该业务员开发的店铺不够2家";
					if (salesNum < 20)
						explain = "该业务员当月销售业绩没有达到20套";
				}
				if (shopNum >= 3 && salesNum >= 20) {
					if (leftintegral >= 150 * 500 && rightintegral >= 150 * 500) {
						count += 12000;
						leftInt = 150 * 500;
						rightInt = 150 * 500;
						updateThisIntegral(sale.get("id").toString(), leftintegral - leftInt, rightintegral - rightInt);
					}
					// }

					if (count == 16600) {
						if ((leftintegral >= 162.5 * 500 && rightintegral >= 162.5 * 500)) {
							count += 400;
							leftInt += 12.5 * 500;
							rightInt += 12.5 * 500;
						} else if (leftintegral >= 159 * 500 && rightintegral >= 166 * 500) {
							count += 400;
							leftInt += 9 * 500;
							rightInt += 16 * 500;
						} else if (leftintegral >= 166 * 500 && rightintegral >= 159 * 500) {
							count += 400;
							leftInt += 16 * 500;
							rightInt += 9 * 500;
						}

						if ((leftintegral >= 177.5 * 500 && rightintegral >= 177.5 * 500)) {
							count += 1200;
							leftInt += 27.5 * 500;
							rightInt += 27.5 * 500;
						} else if (leftintegral >= 169 * 500 && rightintegral >= 186 * 500) {
							count += 1200;
							leftInt += 19 * 500;
							rightInt += 36 * 500;
						} else if (leftintegral >= 186 * 500 && rightintegral >= 169 * 500) {
							count += 1200;
							leftInt += 36 * 500;
							rightInt += 19 * 500;
						}

						if ((leftintegral >= 230 * 500 && rightintegral >= 230 * 500)) {
							count += 3000;
							leftInt += 80 * 500;
							rightInt += 80 * 500;
						} else if (leftintegral >= 204 * 500 && rightintegral >= 256 * 500) {
							count += 3000;
							leftInt += 54 * 500;
							rightInt += 106 * 500;
						} else if (leftintegral >= 256 * 500 && rightintegral >= 204 * 500) {
							count += 3000;
							leftInt += 106 * 500;
							rightInt += 54 * 500;
						}

						if (shopNum >= 5 && salesNum >= 30) {
							if ((leftintegral >= 300 * 500 && rightintegral >= 300 * 500)) {
								count += 12000;
								leftInt += 300 * 500;
								rightInt += 300 * 500;
								updateThisIntegral(sale.get("id").toString(), 0, 0);
							} else if (leftintegral >= 250 * 500 && rightintegral >= 450 * 500) {
								count += 12000;
								leftInt += 100 * 500;
								rightInt += 200 * 500;
								updateThisIntegral(sale.get("id").toString(), 0, 0);
							} else if (leftintegral >= 450 * 500 && rightintegral >= 250 * 500) {
								count += 12000;
								leftInt += 200 * 500;
								rightInt += 100 * 500;
								updateThisIntegral(sale.get("id").toString(), 0, 0);
							}
						} else {
							if (shopNum < 5)
								explain = "该业务员开发的店铺不够5家";
							if (salesNum < 30)
								explain = "该业务员当月销售业绩没有达到30套";

						}
					}

				} else {
					if (shopNum < 3)
						explain = "该业务员开发的店铺不够3家";
					if (salesNum < 20)
						explain = "该业务员当月销售业绩没有达到20套";
				}

				String sqlsum = "select sum(leftthisintegral) as leftsum,sum(rightthisintegral) as rightsum,sum(ThisMoney) as moneysum from resultfinalview where flag=1 and salesmanid="
						+ sale.get("ID").toString();
				List sumList = mySQLDBHelper.retriveBySQL(sqlsum);
				if (sumList != null) {
					Map sum = (Map) sumList.get(0);
					if (sum.get("leftsum") != null)
						properties.put("LeftFinalIntegral", sum.get("leftsum").toString());
					else
						properties.put("LeftFinalIntegral", 0);
					if (sum.get("rightsum") != null)
						properties.put("RightFinalIntegral", sum.get("rightsum").toString());
					else
						properties.put("RightFinalIntegral", 0);
					if (sum.get("moneysum") != null)
						properties.put("TotalMoney", sum.get("moneysum").toString());
					else
						properties.put("TotalMoney", 0);
				}
				properties.put("ExplainInfo", explain);
				properties.put("leftthisintegral", sale.get("leftintegral").toString());
				properties.put("rightthisintegral", sale.get("rightintegral").toString());
				properties.put("ThisMoney", count);
				mySQLDBHelper.create("resultfinalview", properties);
			}
		}
		return id;
	}

	@RequestMapping(value = "/export2excel", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List export2Excel(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String id = request.getParameter("id");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = sdf.format(new Date());
		// 填充projects数据
		String sql = "select * from resultfinalview where resultfinalid=" + id + " and (ThisMoney > 0 or PreMoney > 0)";
		List list = mySQLDBHelper.retriveBySQL(sql);

		if (list != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtil.createCallCaseTable(list, "业绩结算详细表").write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "utf-8"));
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(out);
				byte[] buff = new byte[2048];
				int bytesRead;
				// Simple read/write loop.
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (final IOException e) {
				throw e;
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		}
		return null;
	}

	/*
	 * @RequestMapping(value = "/sum", method = RequestMethod.GET, produces =
	 * "application/json")
	 * 
	 * @ResponseBody public boolean sum(HttpServletRequest request) {
	 * HttpSession session = request.getSession();
	 * if(session.getAttribute("realname") == null) return false; String
	 * realname = session.getAttribute("realname").toString(); Map prop = new
	 * HashMap(); prop.put("workuser", realname); SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); prop.put("finaldate",
	 * sdf.format(new Date())); long id = mySQLDBHelper.create("resultfinal",
	 * prop);
	 * 
	 * List salesman = mySQLDBHelper.retriveBySQL("select * from salesman");
	 * 
	 * if (salesman != null && salesman.size() > 0) { for (int i = 0, l =
	 * salesman.size(); i < l; i++) { Map sale = (Map) salesman.get(i);
	 * 
	 * int count = 0; double leftInt = 0; double rightInt = 0; String explain =
	 * ""; int flag = 0;
	 * 
	 * //获取业务员的基本信息 Map properties = new HashMap(); properties.put("salesmanid",
	 * sale.get("ID").toString()); properties.put("salesmanname",
	 * sale.get("salesmanname") .toString()); properties.put("salesmannumber",
	 * sale.get("salesmannumber") .toString()); if
	 * (sale.get("lefttotalintegral") != null)
	 * properties.put("lefttotalintegral",
	 * sale.get("lefttotalintegral").toString()); else
	 * properties.put("lefttotalintegral", 0); if
	 * (sale.get("righttotalintegral") != null)
	 * properties.put("righttotalintegral",
	 * sale.get("righttotalintegral").toString()); else
	 * properties.put("righttotalintegral", 0); properties.put("telephone",
	 * sale.get("telephone").toString()); properties.put("createdate",
	 * sdf.format(new Date())); properties.put("ResultFinalID", id); //
	 * 2015-11-23 //
	 * integral是为了计算使用，totalintegral是为了记录历史总积分使用，所以，在结算的时候，只修改integral值。 //
	 * 当前需要计算的积分是否为空，如果是，则不做下面的计算。 if (sale.get("leftintegral") == null ||
	 * sale.get("rightintegral") == null) flag = 1;
	 * 
	 * // 查找该业务员左侧美容院的数量 List leftshop = mySQLDBHelper .retriveBySQL(
	 * "select * from beautyshop where salesmanid=" + sale.get("id") +
	 * " and beautyshopplace='左'"); if (leftshop != null)
	 * properties.put("leftbeautyshopnum", leftshop.size()); else
	 * properties.put("leftbeautyshopnum", 0);
	 * 
	 * // 查找该业务员右侧美容院的数量 List rightshop = mySQLDBHelper .retriveBySQL(
	 * "select * from beautyshop where salesmanid=" + sale.get("id") +
	 * " and beautyshopplace='右'"); if (rightshop != null)
	 * properties.put("rightbeautyshopnum", rightshop.size()); else
	 * properties.put("rightbeautyshopnum", 0);
	 * 
	 * // 查找该业务员美容院的总数量 int shopNum = 0; if (rightshop != null && leftshop !=
	 * null) { shopNum = rightshop.size() + leftshop.size(); }
	 * 
	 * // 计算该业务员此次计算的销售的套数 int salesNum = 0; if (sale.get("ThisMonthSalesNum")
	 * != null) { salesNum = Integer.valueOf(sale.get("ThisMonthSalesNum")
	 * .toString()); } // 计算该业务员此次计算的总积分数 if (flag != 1) { int leftintegral =
	 * Integer.valueOf(sale.get("leftintegral") .toString()); int rightintegral
	 * = Integer.valueOf(sale.get( "rightintegral").toString());
	 * properties.put("TotalIntegral", leftintegral + rightintegral);
	 * 
	 * // 当美容院超过2家，销售的套数多于20套的时候，需要计算的积分。 if (shopNum >= 2 && salesNum >= 20) {
	 * int innerFlag = 0; // 第一阶段计算，总积分需要达到25*500分，左右积分达到1：1 if (leftintegral >=
	 * 12.5 * 500 && rightintegral >= 12.5 * 500) { count += 400; leftInt = 12.5
	 * * 500; rightInt = 12.5 * 500; }
	 * 
	 * // 第二阶段计算，总积分需要达到55*500分，左右积分达到1：1 if (leftintegral >= 27.5 * 500 &&
	 * rightintegral >= 27.5 * 500) { count += 1200; leftInt = 27.5 * 500;
	 * rightInt = 27.5 * 500; }
	 * 
	 * // 第三阶段计算，总积分需要达到160*500分，左右积分达到1：1 if (leftintegral >= 80 * 500 &&
	 * rightintegral >= 80 * 500) { count += 3000; leftInt = 80 * 500; rightInt
	 * = 80 * 500; }
	 * 
	 * // 经过前面三阶段的计算，如果count值还是0，代表三阶段的计算不符合要求 if (count == 0) { explain =
	 * "左右店的积分没有达到要求"; innerFlag = 1; }
	 * 
	 * // 第四阶段积分计算，开发的美容院达到3家以上，销售业绩达到20 if (innerFlag != 1) { if (shopNum >= 3
	 * && salesNum >= 20) { // 总积分需要达到300*500以上，且左右比例为1：1，此时为第一轮结算计算完成，需要减去此次总积分
	 * if (leftintegral >= 150 * 500 && rightintegral >= 150 * 500) { count +=
	 * 12000; leftInt = 150 * 500; rightInt = 150 * 500;
	 * updateThisIntegral(sale.get("id") .toString(), leftintegral - leftInt,
	 * rightintegral - rightInt); mySQLDBHelper.executeSQL(
	 * "update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+"
	 * + leftInt + ",SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+" +
	 * rightInt + " where ID=" + sale.get("id")); } // } //
	 * ---------------------------第一轮积分计算结束-------------------------------------
	 * --
	 * 
	 * //
	 * ---------------------------第二轮积分计算开始-------------------------------------
	 * -- // 如果第一轮结算的总钱数达到了16600，则可以进入第二轮积分的计算 if (count == 16600) { if
	 * ((leftintegral >= 162.5 * 500 && rightintegral >= 162.5 * 500)) { count
	 * += 400; leftInt += 12.5 * 500; rightInt += 12.5 * 500; } else if
	 * (leftintegral >= 159 * 500 && rightintegral >= 166 * 500) { count += 400;
	 * leftInt += 9 * 500; rightInt += 16 * 500; } else if (leftintegral >= 166
	 * * 500 && rightintegral >= 159 * 500) { count += 400; leftInt += 16 * 500;
	 * rightInt += 9 * 500; }
	 * 
	 * if ((leftintegral >= 177.5 * 500 && rightintegral >= 177.5 * 500)) {
	 * count += 1200; leftInt += 27.5 * 500; rightInt += 27.5 * 500; } else if
	 * (leftintegral >= 169 * 500 && rightintegral >= 186 * 500) { count +=
	 * 1200; leftInt += 19 * 500; rightInt += 36 * 500; } else if (leftintegral
	 * >= 186 * 500 && rightintegral >= 169 * 500) { count += 1200; leftInt +=
	 * 36 * 500; rightInt += 19 * 500; }
	 * 
	 * if ((leftintegral >= 230 * 500 && rightintegral >= 230 * 500)) { count +=
	 * 3000; leftInt += 80 * 500; rightInt += 80 * 500; } else if (leftintegral
	 * >= 204 * 500 && rightintegral >= 256 * 500) { count += 3000; leftInt +=
	 * 54 * 500; rightInt += 106 * 500; } else if (leftintegral >= 256 * 500 &&
	 * rightintegral >= 204 * 500) { count += 3000; leftInt += 106 * 500;
	 * rightInt += 54 * 500; }
	 * 
	 * // 如果此次结算，完成了第二轮的第四阶段结算，那么重置该业务员的左右积分从0开始 if (shopNum >= 5 && salesNum >=
	 * 30) { if ((leftintegral >= 300 * 500 && rightintegral >= 300 * 500)) {
	 * count += 12000; leftInt += 300 * 500; rightInt += 300 * 500;
	 * updateThisIntegral(sale.get("id") .toString(), 0, 0);
	 * mySQLDBHelper.executeSQL(
	 * "update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+SELFTotalUsedLeftIntegral,SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+SELFTotalUsedRightIntegral where ID="
	 * + sale.get("id"));
	 * 
	 * } else if (leftintegral >= 250 * 500 && rightintegral >= 350 * 500) {
	 * count += 12000; leftInt += 100 * 500; rightInt += 200 * 500;
	 * updateThisIntegral(sale.get("id") .toString(), 0, 0);
	 * mySQLDBHelper.executeSQL(
	 * "update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+SELFTotalUsedLeftIntegral,SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+SELFTotalUsedRightIntegral where ID="
	 * + sale.get("id"));
	 * 
	 * } else if (leftintegral >= 350 * 500 && rightintegral >= 250 * 500) {
	 * count += 12000; leftInt += 200 * 500; rightInt += 100 * 500;
	 * updateThisIntegral(sale.get("id") .toString(), 0, 0);
	 * mySQLDBHelper.executeSQL(
	 * "update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+SELFTotalUsedLeftIntegral,SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+SELFTotalUsedRightIntegral where ID="
	 * + sale.get("id"));
	 * 
	 * } } else { if (shopNum < 5) explain = "该业务员开发的店铺不够5家"; if (salesNum < 30)
	 * explain = "该业务员当月销售业绩没有达到30套";
	 * 
	 * } }
	 * 
	 * } else { if (shopNum < 3) explain = "该业务员开发的店铺不够3家"; if (salesNum < 20)
	 * explain = "该业务员当月销售业绩没有达到20套"; } } //
	 * ---------------------------第二轮积分计算结束-------------------------------------
	 * -- } else { if (shopNum < 2) explain = "该业务员开发的店铺不够2家"; if (salesNum <
	 * 20) explain = "该业务员当月销售业绩没有达到20套"; } } else { if
	 * (sale.get("leftintegral") != null) properties.put("TotalIntegral",
	 * Integer.valueOf(sale .get("leftintegral").toString())); else if
	 * (sale.get("rightintegral") != null) properties.put("TotalIntegral",
	 * Integer.valueOf(sale .get("rightintegral").toString())); else
	 * properties.put("TotalIntegral", 0);
	 * 
	 * explain = "该业务员左积分或者右积分为0"; }
	 * 
	 * //
	 * ---------------------------第一轮积分计算开始-------------------------------------
	 * --
	 * 
	 * String sqlsum =
	 * "select sum(leftthisintegral) as leftsum,sum(rightthisintegral) as rightsum,sum(ThisMoney) as moneysum from resultfinalview where flag=1 and salesmanid="
	 * + sale.get("ID").toString(); List sumList =
	 * mySQLDBHelper.retriveBySQL(sqlsum); if (sumList != null) { Map sum =
	 * (Map) sumList.get(0); if (sum.get("leftsum") != null)
	 * properties.put("LeftFinalIntegral", sum.get("leftsum") .toString()); else
	 * properties.put("LeftFinalIntegral", 0); if (sum.get("rightsum") != null)
	 * properties.put("RightFinalIntegral", sum .get("rightsum").toString());
	 * else properties.put("RightFinalIntegral", 0); if (sum.get("moneysum") !=
	 * null) properties.put("TotalMoney", sum.get("moneysum") .toString()); else
	 * properties.put("TotalMoney", 0); } properties.put("ExplainInfo",
	 * explain); if (sale.get("leftintegral") != null)
	 * properties.put("leftthisintegral", sale.get("leftintegral") .toString());
	 * else properties.put("leftthisintegral", 0);
	 * 
	 * if (sale.get("rightintegral") != null)
	 * properties.put("rightthisintegral",
	 * sale.get("rightintegral").toString()); else
	 * properties.put("rightthisintegral", 0);
	 * 
	 * properties.put("ThisMoney", count);
	 * mySQLDBHelper.create("resultfinalview", properties); } } return true; }
	 */
}
