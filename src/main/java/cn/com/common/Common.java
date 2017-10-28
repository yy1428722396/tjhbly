package cn.com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
	public static String lastMonth(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return sdf.format(c.getTime());
	}
	
	public static Calendar oneYearMonth(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -11);
		return c;
	}
	
	public static int monthDis(String beginTime,String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = sdf.parse(beginTime);
			endDate = sdf.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int beginYear = beginDate.getYear();
		int beginMonth = beginDate.getMonth();
		
		int endYear = endDate.getYear();
		int endMonth = endDate.getMonth();
		
		int dis = (endYear - beginYear) * 12 + (endMonth - beginMonth);
		return dis;
	}
	
	
	public static void main(String[] args){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Calendar c = oneYearMonth("2017.10");
		System.out.println(sdf.format(c.getTime()));
		c.add(Calendar.MONTH, 2);
		System.out.println(sdf.format(c.getTime()));
		
		System.out.println(monthDis("2016.09","2017.02"));
	}
}
