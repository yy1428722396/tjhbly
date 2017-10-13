package cn.com.xtgl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.database.helper.MySQLDBHelper;

@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private MySQLDBHelper mySQLDBHelper;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean add(HttpServletRequest request) {
		String type = request.getParameter("type");
		String title = request.getParameter("title");
		String subtitle = request.getParameter("subtitle");
		String summary = request.getParameter("summary");
		String content = request.getParameter("content");

		HttpSession session = request.getSession();
		String username = "";
		String role = "";
		if(session.getAttribute("username") != null && !session.getAttribute("username").equals("")){
			username = session.getAttribute("username").toString();
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		
		Map properties = new HashMap();
		properties.put("type", type);
		properties.put("title", title);
		properties.put("subtitle", subtitle);
		properties.put("summary", summary);
		properties.put("content", content);

		
		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		properties.put("createuser", username);
	

		long id = mySQLDBHelper.create("t_news", properties);
		if (id != 0)
			return true;
		else
			return false;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String title = request.getParameter("title");
		String subtitle = request.getParameter("subtitle");
		String summary = request.getParameter("summary");
		String content = request.getParameter("content");

		HttpSession session = request.getSession();
		String username = "";
		String role = "";
		if(session.getAttribute("username") != null && !session.getAttribute("username").equals("")){
			username = session.getAttribute("username").toString();
			role = session.getAttribute("role").toString();
		}
		else
			return false;
		
		Map properties = new HashMap();
		properties.put("type", type);
		properties.put("title", title);
		properties.put("subtitle", subtitle);
		properties.put("summary", summary);
		properties.put("content", content);

		properties.put("statusvalue", role);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		properties.put("createtime", now);
		properties.put("createuser", username);
		
		return mySQLDBHelper.update("t_news", properties, "id=" + id);
	}

	@RequestMapping(value = "/listnosession", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map listnosession(HttpServletRequest request) {
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
	
	@RequestMapping(value = "/notice", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map notice(HttpServletRequest request) {
	
		String sql = "SELECT * from t_news where statusvalue<>0 and type=3 and createtime > date_sub(NOW(), INTERVAL 2 DAY) order by createtime desc";

		Map result = mySQLDBHelper.retriveBySQL(sql, false, 0, 0);
		return result;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map list(HttpServletRequest request) {
		String title = request.getParameter("title");
		String type = request.getParameter("type");
		String limit = "10";// request.getParameter("limit");
		String page = request.getParameter("page");
		int startLine = 0;
		int maxSize = Integer.valueOf(limit);
		if (page != null && !page.equals("") && limit != null && !limit.equals("")) {
			startLine = (Integer.valueOf(page) - 1) * Integer.valueOf(limit);
		}

		HttpSession session = request.getSession();
		String username = "";
		String role = "";
		if(session.getAttribute("username") != null && !session.getAttribute("username").equals("")){
			username = session.getAttribute("username").toString();
			role = session.getAttribute("role").toString();
		}
		else
			return null;
		
		
		String sql = "SELECT * from t_news where statusvalue>=" + role;
		if (title != null && !title.trim().equals(""))
			sql += " and title like '%" + title + "%'";
		if (type != null && !type.trim().equals("") && !type.trim().equals("0"))
			sql += " and type=" + type;
		else
			sql += " and type<>3";

		sql += " order by createtime desc";
		
		Map result = mySQLDBHelper.retriveBySQL(sql, true, startLine, maxSize);
		return result;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List all(HttpServletRequest request) {
		String sql = "select * from t_news where statusvalue<>0";
		return mySQLDBHelper.retriveBySQL(sql);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map info(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map result = mySQLDBHelper.retriveByID("t_news", "id", id);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean delete(HttpServletRequest request) {
		String id = request.getParameter("id");

		HashMap temp = new HashMap();
		temp.put("statusvalue", 0);
		return mySQLDBHelper.update("t_news", temp, "id=" + id);

	}

	/*@RequestMapping(value = "/imgUpload", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String imgUpload(HttpServletRequest request,HttpServletResponse response) {

		PrintWriter out = null; // 输出流
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String savePath = request().getServletContext().getRealPath("/") + "attached/";

		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/attached/";

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = 1000000;

		response.setContentType("text/html; charset=UTF-8");

		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(getError("请选择文件。"));
			return null;
		}
		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			out.println(getError("上传目录不存在。"));
			return null;
		}
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			out.println(getError("上传目录没有写权限。"));
			return null;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			out.println(getError("目录名不正确。"));
			return null;
		}
		// 创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
		// 获得上传的文件名
		String fileName = wrapper.getFileNames("imgFile")[0];// imgFile,imgFile,imgFile
		// 获得文件过滤器
		File file = wrapper.getFiles("imgFile")[0];

		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
			out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
			return null;
		}
		// 检查文件大小
		if (file.length() > maxSize) {
			out.println(getError("上传文件大小超过限制。"));
			return null;
		}

		// 重构上传图片的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newImgName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		byte[] buffer = new byte[1024];
		// 获取文件输出流
		FileOutputStream fos;
		// 获取内存中当前文件输入流
		InputStream in;
		try {
			fos = new FileOutputStream(savePath + "/" + newImgName);
			in = new FileInputStream(file);
			int num = 0;
			while ((num = in.read(buffer)) > 0) {
				fos.write(buffer, 0, num);
			}
			in.close();
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 发送给 kindeditor
		JSONObject obj = new JSONObject();
		obj.put("error", 0);
		obj.put("url", saveUrl + "/" + newImgName);
		out.println(obj.toString());
		return null;

	}
	
	 private String getError(String message) {  
	        JSONObject obj = new JSONObject();  
	        obj.put("error", 1);  
	        obj.put("message", message);  
	        return obj.toString();  
	 }*/
}
