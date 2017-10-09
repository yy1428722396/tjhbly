package cn.com.tjly.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
@RequestMapping("/ywy")
public class YWYController {
	
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
			return true;
		else
			return false;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String sjywyid = request.getParameter("sjywyid");
		String sjywy = request.getParameter("sjywy");
		String ywy = request.getParameter("ywy");
		String ywybh = request.getParameter("ywybh");
		String wzfb = request.getParameter("wzfb");
		//String ywyfl = request.getParameter("ywyfl");
		String zuohxjf = request.getParameter("zuohxjf");
		String youhxjf = request.getParameter("youhxjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String banknum = request.getParameter("banknum");
		String bz = request.getParameter("bz");	
		//String first10 = request.getParameter("first10");
		
		Map properties = new HashMap();
		properties.put("SalesmanName", ywy);
		properties.put("SalesmanNumber", ywybh);
		properties.put("SalesmanPlace", wzfb);
		properties.put("SalesmanAddress", address);
		properties.put("SalesmanCity", cities);
		properties.put("Telephone", telphone);
		properties.put("BankNum", banknum);
		properties.put("Description", bz);
		properties.put("InitLeftIntegral", zuohxjf);
		properties.put("InitRightIntegral", youhxjf);
		properties.put("LeftTotalIntegral", zuohxjf);
		properties.put("RightTotalIntegral",youhxjf);
		properties.put("LeftIntegral", zuohxjf);
		properties.put("RightIntegral",youhxjf);

		properties.put("SELFTotalLeftIntegral",zuohxjf);
		properties.put("SELFTotalRightIntegral",youhxjf);
		properties.put("SELFTotalCount",0);
		properties.put("SELFTotalUsedLeftIntegral",0);
		properties.put("SELFTotalUsedRightIntegral",0);
		properties.put("SELFTotalShop",0);
		properties.put("Status",0);
		
		properties.put("GiveMoney",0);
		properties.put("CalculateProcess", 0);
		properties.put("MonthProcess", 0);
		
		if(sjywyid != null && !sjywyid.equals("") && !sjywyid.equals("无"))
			properties.put("UpSalesmanID", sjywyid);
		else
			properties.put("UpSalesmanID", 0);
	/*	if(first10.equals("true"))
			properties.put("InitMoney", 0);
		else
			properties.put("InitMoney", 3);*/
		properties.put("InitMoney", 0);
		
		properties.put("UpSalesmanName", sjywy);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("CreateDate", now);
		long id = mySQLDBHelper.create("Salesman", properties);
		if(id != 0)
			return true;
		else
			return false;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("ywyid");
		String sjywyid = request.getParameter("sjywyid");
		String sjywy = request.getParameter("sjywy");
		String ywy = request.getParameter("ywy");
		String ywybh = request.getParameter("ywybh");
		String wzfb = request.getParameter("wzfb");
		//String ywyfl = request.getParameter("ywyfl");
		String zuohxjf = request.getParameter("zuohxjf");
		String youhxjf = request.getParameter("youhxjf");
		String initzuohxjf = request.getParameter("initzuohxjf");
		String inityouhxjf = request.getParameter("inityouhxjf");
		String leftjf = request.getParameter("leftjf");
		String rightjf = request.getParameter("rightjf");
		String address = request.getParameter("address");
		String cities = request.getParameter("cities");
		String telphone = request.getParameter("telphone");
		String banknum = request.getParameter("banknum");
		String bz = request.getParameter("bz");	
		//String first10 = request.getParameter("first10");
		Map properties = new HashMap();
		properties.put("SalesmanName", ywy);
		properties.put("SalesmanNumber", ywybh);
		properties.put("SalesmanPlace", wzfb);
		properties.put("SalesmanAddress", address);
		properties.put("SalesmanCity", cities);
		properties.put("Telephone", telphone);
		properties.put("BankNum", banknum);
		properties.put("Description", bz);
		properties.put("Status", 0);
		/*if(leftjf == null || leftjf.equals(""))
			leftjf = "0";
		if(zuohxjf == null || zuohxjf.equals(""))
			zuohxjf = "0";
		if(initzuohxjf == null || initzuohxjf.equals(""))
			initzuohxjf = "0";
		if(rightjf == null || rightjf.equals(""))
			rightjf = "0";
		if(youhxjf == null || youhxjf.equals(""))
			youhxjf = "0";
		if(inityouhxjf == null || inityouhxjf.equals(""))
			inityouhxjf = "0";*/
		//properties.put("InitLeftIntegral", zuohxjf);
		//properties.put("InitRightIntegral", youhxjf);
		//properties.put("LeftTotalIntegral", Integer.valueOf(leftjf) + Integer.valueOf(zuohxjf) - Integer.valueOf(initzuohxjf) );
		//properties.put("RightTotalIntegral",Integer.valueOf(rightjf) + Integer.valueOf(youhxjf) - Integer.valueOf(inityouhxjf) );
		//properties.put("LeftIntegral", Integer.valueOf(leftjf) + Integer.valueOf(zuohxjf) - Integer.valueOf(initzuohxjf) );
		//properties.put("RightIntegral",Integer.valueOf(rightjf) + Integer.valueOf(youhxjf) - Integer.valueOf(inityouhxjf) );
		
		//properties.put("SELFTotalLeftIntegral",Integer.valueOf(leftjf) + Integer.valueOf(zuohxjf) - Integer.valueOf(initzuohxjf));
		//properties.put("SELFTotalRightIntegral",Integer.valueOf(rightjf) + Integer.valueOf(youhxjf) - Integer.valueOf(inityouhxjf));
		
		if(sjywyid != null && !sjywyid.equals("") && !sjywyid.equals("无"))
			properties.put("UpSalesmanID", sjywyid);
		else
			properties.put("UpSalesmanID", 0);
		properties.put("UpSalesmanName", sjywy);
		/*if(first10.equals("true"))
			properties.put("InitMoney", 0);
		else
			properties.put("InitMoney", 3);*/
		return mySQLDBHelper.update("Salesman", properties, "ID=" + id);
	}
	
	@RequestMapping(value = "/ywylist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userlist(HttpServletRequest request) {
		String ywyname = request.getParameter("ywyname");
		String del = request.getParameter("del");
		String flag = request.getParameter("f");
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null
				&& !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}
		String sql = "select * from Salesman where Status <> 1";
		if(del != null && !del.equals("") && del.equals("true")){
			sql = "select * from Salesman where Status=1";
		}
		if(flag != null && flag.equals("1"))
			sql = "select * from Salesman where 1=1";
		if(ywyname != null && !ywyname.trim().equals(""))
			sql += " and SalesmanName like '%" + ywyname + "%'";
	/*	if(mryname != null && !mryname.trim().equals(""))
			sql += " and SalesmanName like '%" + mryname + "%'";*/
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}
	
	@RequestMapping(value = "/ywyinfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map userinfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("Salesman", "ID", id);
		return result;
	}
	
	@RequestMapping(value = "/ywychild", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public boolean ywychild(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "select * from Salesman where upsalesmanid=" + id;
		List result = mySQLDBHelper.retriveBySQL(sql);
		if(result != null && result.size() > 0)
			return true;
		else
			return false;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");
		/*Map result = mySQLDBHelper.retriveByID("Salesman", "ID", id);
		String parrentId = result.get("UpSalesmanID").toString();
		String parrentName = result.get("UpSalesmanName").toString();
		
		List downSales = mySQLDBHelper.retriveBySQL("select id from Salesman where UpSalesmanID=" + id);
		if(downSales != null){
			for(int i = 0 , l = downSales.size() ; i < l ; i++){
				Map m = (Map)downSales.get(i);
				String thisid = m.get("id").toString();
				HashMap temp = new HashMap();
				temp.put("UpSalesmanID", parrentId);
				temp.put("UpSalesmanName", parrentName);
				mySQLDBHelper.update("Salesman", temp, "id=" + thisid);
			}
		}*/
		
		List shop = mySQLDBHelper.retriveBySQL("select * from beautyshop s where s.salesmanid=s.managersalesmanid and s.Status<>1 and s.salesmanid='" + id + "'");
		
		if(shop != null && shop.size() > 0){
			return false;
		}
		
		HashMap temp = new HashMap();
		temp.put("Status", 1);
		mySQLDBHelper.update("Salesman", temp, "id=" + id);
		
		mySQLDBHelper.executeSQL("update beautyshop set salesmanid=managersalesmanid,salesmanname=managersalesmanname where salesmanid='" + id + "'");
		mySQLDBHelper.executeSQL("update beautyshop set managersalesmanid=salesmanid,managersalesmanname=salesmanname where managersalesmanid='" + id + "'");
		
		return true;
		
	//	return mySQLDBHelper.delete("Salesman", "ID=" + id);
	}
	
	@RequestMapping(value = "/back", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean back(HttpServletRequest request) {
		String id = request.getParameter("id");
		
		HashMap temp = new HashMap();
		temp.put("Status", 0);
		mySQLDBHelper.update("Salesman", temp, "id=" + id);
		
		return true;
	}
	
	/*@RequestMapping(value = "/jOrgChart/test", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map ywyIntegral(){
		String str = "<li class=\"user\">王素珍<ul>" 
						+ "<li class=\"place\">左<br>150000<ul>"
						+ "<li class=\"user\">王强<br>10000<ul>"
						+ "<li class=\"place\">左<br>10000<ul>"
						+ "<li class=\"BeautyShop\"><img src=\"images/错误.jpg\"  width=\"20\" height=\"20\"/>思聪美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">强强美容院<br>10000</li></ul>"
						+ "</li><li class=\"place\">右<br>10000<ul>"
						+ "<li class=\"BeautyShop\">哈哈美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">青春美容院<br>10000</li></ul></li></ul></li>"
						+ "<li class=\"BeautyShop\"><img src=\"images/告警.jpg\"  width=\"20\" height=\"20\"/>玉芹美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">玉芹美容院<br>10000</li><li class=\"BeautyShop\">成氏养生馆<br>15000</li></ul></li>"
						+ "<li class=\"place\">右<br>100000<ul>"
						+ "<li class=\"user\">伍思芳<br>10000<ul>"
						+ "<li class=\"place\">左<br>10000<ul>"
						+ "<li class=\"BeautyShop\">思聪美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">强强美容院<br>10000</li></ul></li>"
						+ "<li class=\"place\">右<br>10000<ul>"
						+ "<li class=\"BeautyShop\">哈哈美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">青春美容院<br>10000</li></ul></li></ul></li>"
						+ "<li class=\"BeautyShop\">玉芹美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">玉芹美容院<br>10000</li>"
						+ "<li class=\"BeautyShop\">成氏养生馆<br>15000</li></ul></li></ul></li>";
		Map result = new HashMap();
		result.put("data", str);
		return result;
	}*/
	@RequestMapping(value = "/jOrgChart/GroupImage", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map GroupImage(HttpServletRequest request) {
		String strId = request.getParameter("Id");
		StringBuilder values = new StringBuilder();

		String sql = "select a.Id,a.salesmanName,a.salesmanLevel,a.RootSalesmanID,a.salesmanPlace,a.UpSalesmanID,a.LeftIntegral, "
				+ "a.RightIntegral,a.LeftTotalIntegral,a.RightTotalIntegral,"
				+ "b.BeautyShopName,b.BeautyShopPlace,b.TotalIntegral BTotalIntegral,b.DropBeautyShop,b.ManagerSalesmanID,b.salesmanId,a.status,b.status as bStatus"
				+ " from salesman a join beautyshop b on a.Id=b.ManagerSalesmanID and find_in_set(a.id,getChildList("+strId+"))"
				+ "  order by a.UpSalesmanID,a.SalesmanPlace desc,b.BeautyShopPlace desc";
				//+ "  where a.status=0 order by a.UpSalesmanID,a.SalesmanPlace desc,b.BeautyShopPlace desc";
		System.out.println("sql:"+sql);
		List resultNew = mySQLDBHelper.retriveBySQL(sql);


		if(resultNew != null && resultNew.size() > 0)
		{
			String strSalesmanName = "";//判断如果没有右侧美容院的业务员名称
			int BeautyShopIntegralCount =0; //业务员的自己美容院积分相加
			boolean SalesmanBool = false;  //判断只显示一个业务员
			int m = 0;  //记录Id统计
			int n = 0;  //统计比较
			int oldsalesmanID = 0;//去重复多个相同 业务员
			String LeftTotalIntegral = "",RightTotalIntegral = "";
			
			values.append("<li class=\"BeautyShop\">"+"结构图展示\r\n");
			values.append("<ul>\r\n");
			//统计=strId的记录数
			for(int i = 0,l = resultNew.size() ; i < l ; i++)
			{
				Map MapResult= (Map)resultNew.get(i);
				if(strId.equals(MapResult.get("Id").toString()))//如果记录Id与request的id相同
				{
					m++;
				}
			}
			for(int i = 0,l = resultNew.size() ; i < l ; i++)
			{
				Map MapResult= (Map)resultNew.get(i);

				if(strId.equals(MapResult.get("Id").toString()))//如果记录Id与request的id相同
				{
					
					n++;
					if(MapResult.get("LeftTotalIntegral")==null)  LeftTotalIntegral = "";
					else LeftTotalIntegral = MapResult.get("LeftTotalIntegral").toString();
					if(MapResult.get("RightTotalIntegral")==null)  RightTotalIntegral = "";
					else RightTotalIntegral = MapResult.get("RightTotalIntegral").toString();

					BeautyShopIntegralCount = Integer.valueOf(LeftTotalIntegral).intValue()+Integer.valueOf(RightTotalIntegral).intValue();
					int DropBeautyShopId = Integer.valueOf(MapResult.get("DropBeautyShop").toString()).intValue();
					int Mid = Integer.valueOf(MapResult.get("ManagerSalesmanID").toString()).intValue();//下放店
					int Sid = Integer.valueOf(MapResult.get("salesmanId").toString()).intValue();
					
					boolean boolStatus = true;//做删除业务员状态标识
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1) boolStatus = false;
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1 && oldsalesmanID!=Mid )
					{
						//业务员  如果业务员没有右侧美容院
						oldsalesmanID = Mid;
						boolStatus = false;
						values.append("<li class=\"user\">");
						values.append(strSalesmanName+"(该业务员已被删除)"+"\r\n");//查询出第一条记录的业务员名称
						values.append(BeautyShopIntegralCount+"\r\n");
						values = GroupDG( values, resultNew, strId,LeftTotalIntegral,RightTotalIntegral,oldsalesmanID); //调用递归
						values.append("</li>\r\n");
						values.append("</ul>\r\n");
						values.append("</li>\r\n");
					}
					
					if(boolStatus)
					{
						
						if(Integer.valueOf(MapResult.get("bStatus").toString()).intValue()==0)
						{
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								
								strSalesmanName = MapResult.get("SalesmanName").toString();
								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//5个没有报单和下放店的美容院
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//5个没有报单的美容院
									}
								}else{

									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//下放店的美容院
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//美容院
									}
								}

								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");
								if(n==m){//如果没有右侧美容院
									//业务员  如果业务员没有右侧美容院
									SalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(strSalesmanName+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");
									values = GroupDG( values, resultNew, strId,LeftTotalIntegral,RightTotalIntegral,Mid); //调用递归
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
								}


							}else{
								//业务员
								if(!SalesmanBool) //业务员只读取一次
								{
									SalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");
									values = GroupDG( values, resultNew, strId,LeftTotalIntegral,RightTotalIntegral,Mid); //调用递归
									values.append("</li>\r\n");
								}
								//右侧美容院
								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//查询出第一条记录的业务员名称
									}
								}else{

									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//查询出第一条记录的业务员名称
									}
								}

								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");
								if(n==m){
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
								}
							}
						}else{
							/*if(n==m){
								values.append("</ul>\r\n");
								values.append("</li>\r\n");
							}*/
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								if(n==m){//如果没有右侧美容院
									//业务员  如果业务员没有右侧美容院
									SalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(strSalesmanName+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");
									values = GroupDG( values, resultNew, strId,LeftTotalIntegral,RightTotalIntegral,Mid); //调用递归
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
								}
							}else{
								//业务员
								if(!SalesmanBool) //业务员只读取一次
								{
									SalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");
									values = GroupDG( values, resultNew, strId,LeftTotalIntegral,RightTotalIntegral,Mid); //调用递归
									values.append("</li>\r\n");
								}
								
								if(n==m){
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
								}
							}
							
						}
					}
				}
			}
		}
		System.out.println(values);
		Map result = new HashMap();
		result.put("data", values);
		return result;
	}
	
	public StringBuilder  GroupDG(StringBuilder values,List resultNew,String UpId,String strLeftTotalIntegral,String strRightTotalIntegral,int oldsalesman)
	{
		int BeautyShopIntegralCount =0; //业务员的自己美容院积分相加
		boolean leftSalesman = false; //业务员左侧
		boolean rightSalesman = false; //业务员右侧
		boolean leftSalesmanBool = false;  //判断只显示一个左侧业务员
		boolean rightSalesmanBool = false;  //判断只显示一个右侧业务员
		int oldsalesmanID = oldsalesman;//删除业务员后，去重复多个相同 业务员
		int m = 0;  //记录Id统计 业务员左
		int n = 0;  //记录Id统计 业务员右
		int s = 0;  //统计比较 业务员左
		int t = 0;  //统计比较 业务员右
		String LeftTotalIntegral = "",RightTotalIntegral = "";

		for(int i = 0,l = resultNew.size() ; i < l ; i++)
		{
			Map MapResult= (Map)resultNew.get(i);
			if(UpId.equals(MapResult.get("UpSalesmanID").toString()))//如果记录Id与request的id相同
			{
				if(MapResult.get("SalesmanPlace").toString().equals("左"))//业务员分左右显示
				{
					m++;
				}else{

					n++;
				}
			}
		}

		for(int i = 0,l = resultNew.size() ; i < l ; i++)
		{
			Map MapResult= (Map)resultNew.get(i);

			if(UpId.equals(MapResult.get("UpSalesmanID").toString()))//如果记录Id与request的id相同
			{
				if(MapResult.get("LeftTotalIntegral")==null)  LeftTotalIntegral = "";
				else LeftTotalIntegral = MapResult.get("LeftTotalIntegral").toString();
				if(MapResult.get("RightTotalIntegral")==null)  RightTotalIntegral = "";
				else RightTotalIntegral = MapResult.get("RightTotalIntegral").toString();
				
				BeautyShopIntegralCount = Integer.valueOf(LeftTotalIntegral).intValue()+Integer.valueOf(RightTotalIntegral).intValue();
				int DropBeautyShopId = Integer.valueOf(MapResult.get("DropBeautyShop").toString()).intValue();
				int Mid = Integer.valueOf(MapResult.get("ManagerSalesmanID").toString()).intValue();//下放店
				int Sid = Integer.valueOf(MapResult.get("salesmanId").toString()).intValue();

				if(MapResult.get("SalesmanPlace").toString().equals("左"))//业务员分左右显示
				{
					s++;
					
					//删除了业务员，对数据进行过滤
					boolean boolStatus = true;//做删除业务员状态标识
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1) boolStatus = false;
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1 && oldsalesmanID!=Mid )
					{
						boolStatus = false;
						oldsalesmanID = Mid;
						values.append("<ul>\r\n");
						values.append("<li class=\"place\">"+"左\r\n");
						values.append(strLeftTotalIntegral+"\r\n");
						values.append("<ul>\r\n");
						
						values.append("<li class=\"user\">");
						values.append(MapResult.get("salesmanName")+"(该业务员已被删除)"+"\r\n");//查询出第一条记录的业务员名称
						values.append(BeautyShopIntegralCount+"\r\n");						
						values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,oldsalesmanID); //递归
						values.append("</li>\r\n");

						values.append("</ul>\r\n");
						values.append("</li>\r\n");

						if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>
						
					}
				
					if(boolStatus)
					{
						if(Integer.valueOf(MapResult.get("bStatus").toString()).intValue()==0)
						{
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								if(!leftSalesman)
								{
									values.append("<ul>\r\n");
									values.append("<li class=\"place\">"+"左\r\n");
									values.append(strLeftTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									leftSalesman = true;
								}

								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//查询出第一条记录的业务员名称
									}
								}else{

									if(Mid!=Sid){//下放店
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//查询出第一条记录的业务员名称
									}
								}

								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");

								if(s==m)//如果  只有左侧美容院，而没有右侧美容院
								{
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");						
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");

									values.append("</ul>\r\n");
									values.append("</li>\r\n");

									if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>

								}
							}else{
								
								if(!leftSalesman)
								{
									values.append("<ul>\r\n");
									values.append("<li class=\"place\">"+"左\r\n");
									values.append(strLeftTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									leftSalesman = true;
								}

								//业务员
								if(!leftSalesmanBool) //业务员只读取一次
								{
									leftSalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归							
									values.append("</li>\r\n");
								}
								//右侧美容院

								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//查询出第一条记录的业务员名称
									}
								}else{

									if(Mid!=Sid){//下放店
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//查询出第一条记录的业务员名称
									}
								}
								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");

								//结束
								if(s==m)
								{
									values.append("</ul>\r\n");
									values.append("</li>\r\n");

									if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>
								}
							}
						}else{//美容院不可用
							//结束
							/*if(s==m)
							{
								values.append("</ul>\r\n");
								values.append("</li>\r\n");

								if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>
							}*/
							
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								if(!leftSalesman)
								{
									values.append("<ul>\r\n");
									values.append("<li class=\"place\">"+"左\r\n");
									values.append(strLeftTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									leftSalesman = true;
								}
								if(s==m)//如果  只有左侧美容院，而没有右侧美容院
								{
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");						
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");

									values.append("</ul>\r\n");
									values.append("</li>\r\n");

									if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>

								}
							}else{
								
								if(!leftSalesman)
								{
									values.append("<ul>\r\n");
									values.append("<li class=\"place\">"+"左\r\n");
									values.append(strLeftTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									leftSalesman = true;
								}

								//业务员
								if(!leftSalesmanBool) //业务员只读取一次
								{
									leftSalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归							
									values.append("</li>\r\n");
								}
								//结束
								if(s==m)
								{
									values.append("</ul>\r\n");
									values.append("</li>\r\n");

									if(n==0) values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>
								}
							}
						}
					}

				}else{//业务员右侧显示					
					//开始业务员右侧显示
					//删除了业务员，对数据进行过滤
					t++;
					boolean boolStatus = true;//做删除业务员状态标识
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1) boolStatus = false;
					if(Integer.valueOf(MapResult.get("Status").toString()).intValue()==1 && oldsalesmanID!=Sid )
					{
						boolStatus = false;
						oldsalesmanID = Sid;
						values.append("<li class=\"place\">"+"右\r\n");
						values.append(strLeftTotalIntegral+"\r\n");
						values.append("<ul>\r\n");
						
						values.append("<li class=\"user\">");
						values.append(MapResult.get("salesmanName")+"(该业务员已被删除)"+"\r\n");//查询出第一条记录的业务员名称
						values.append(BeautyShopIntegralCount+"\r\n");						
						values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
						values.append("</li>\r\n");

						values.append("</ul>\r\n");
						values.append("</li>\r\n");
						values.append("</ul>\r\n");//如果没有业务员右侧的情况，需要多加一个</ul>
						
					}
					
					if(boolStatus)
					{

						if(Integer.valueOf(MapResult.get("bStatus").toString()).intValue()==0)
						{
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								if(!rightSalesman)
								{
									values.append("<li class=\"place\">"+"右\r\n");
									values.append(strRightTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									rightSalesman = true;
								}					

								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//查询出第一条记录的业务员名称
									}
								}else{

									if(Mid!=Sid){//下放店
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//查询出第一条记录的业务员名称
									}
								}
								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");

								if(t==n)//如果没有右侧美容院的情况下
								{
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");

									values.append("</ul>\r\n");
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
								}

							}else{
								if(!rightSalesman)
								{
									values.append("<li class=\"place\">"+"右\r\n");
									values.append(strRightTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									rightSalesman = true;
								}	
								//业务员
								if(!rightSalesmanBool) //业务员只读取一次
								{
									rightSalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");
								}
								//右侧美容院
								if(DropBeautyShopId==1){//5个月没有报单的美容院
									if(Mid!=Sid){
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单&下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(5个月没有报单)"+"\r\n");//查询出第一条记录的业务员名称
									}
								}else{

									if(Mid!=Sid){//下放店
										values.append("<li class=\"longdateBD\">");
										values.append(MapResult.get("BeautyShopName")+"(下放店)"+"\r\n");//查询出第一条记录的业务员名称
									}else{
										values.append("<li class=\"BeautyShop\">");
										values.append(MapResult.get("BeautyShopName")+"\r\n");//查询出第一条记录的业务员名称
									}
								}
								values.append(MapResult.get("BTotalIntegral")+"\r\n");
								values.append("</li>\r\n");

								//结束
								if(t==n){
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
								}
							}
						}else{
							/*if(t==n){
								values.append("</ul>\r\n");
								values.append("</li>\r\n");
								values.append("</ul>\r\n");
							}*/
							if(MapResult.get("BeautyShopPlace").toString().equals("左"))//美容院分左右显示
							{
								if(!rightSalesman)
								{
									values.append("<li class=\"place\">"+"右\r\n");
									values.append(strRightTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									rightSalesman = true;
								}
								if(t==n)//如果没有右侧美容院的情况下
								{
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");

									values.append("</ul>\r\n");
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
								}

							}else{
								if(!rightSalesman)
								{
									values.append("<li class=\"place\">"+"右\r\n");
									values.append(strRightTotalIntegral+"\r\n");
									values.append("<ul>\r\n");
									rightSalesman = true;
								}	
								//业务员
								if(!rightSalesmanBool) //业务员只读取一次
								{
									rightSalesmanBool = true;
									values.append("<li class=\"user\">");
									values.append(MapResult.get("salesmanName")+"\r\n");//查询出第一条记录的业务员名称
									values.append(BeautyShopIntegralCount+"\r\n");							
									values = GroupDG( values, resultNew,  MapResult.get("Id").toString(),LeftTotalIntegral,RightTotalIntegral,Mid); //递归
									values.append("</li>\r\n");
								}
								//结束
								if(t==n){
									values.append("</ul>\r\n");
									values.append("</li>\r\n");
									values.append("</ul>\r\n");
								}
							}
							
							
						}
					}
				}
			}
		}
		return values;
	}
}
