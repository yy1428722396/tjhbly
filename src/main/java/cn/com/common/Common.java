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
	public static String lastYear(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -1);
		return sdf.format(c.getTime());
	}
	public static String lastYearMonth(String time) {
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
		c.add(Calendar.YEAR, -1);
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
		String countdate = "2016.01.01";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(countdate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*int index = c.getTime().getMonth() + 1;
		String[] month =  new String[index];
		System.out.println(index);
		if(index == 1){
			month[0] = sdf.format(c.getTime());
		}else{
			c.add(Calendar.MONTH, 1-index);
			for (int m = 0; m < index; m++) {
				month[m] = sdf.format(c.getTime());
				c.add(Calendar.MONTH, 1);
			}
		}*/
		
		int index = c.getTime().getMonth() + 1;
		String[] month =  new String[12];
		c.add(Calendar.MONTH, 1-index);
			for (int m = 0; m < 12; m++) {
				month[m] = sdf.format(c.getTime());
				c.add(Calendar.MONTH, 1);
			}
		for (int m = 0; m < 12; m++) {
			System.out.println(month[m]);
		}
		
	}
}
