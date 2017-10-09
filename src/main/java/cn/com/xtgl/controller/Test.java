package cn.com.xtgl.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.database.helper.MySQLDBHelper;

public class Test {

	@Autowired
	private static MySQLDBHelper mySQLDBHelper;
	
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
	
	public static void main(String[] args) {
		
		/*try {
			Runtime.getRuntime().exec("shutdown -l");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*String datetime = "2016-12-22 12:12:12";
		Calendar starCal = Calendar.getInstance();
		int sMonth = starCal.get(Calendar.MONTH) + 1;
		if(datetime.indexOf("-") > 0){
			String[] time = datetime.split("-");
			int mon = Integer.valueOf(time[1]);
			if(mon != sMonth)
				System.out.println("hello");
		}*/
		try{
			T t = new T();
			int i = t.cc();
			int b = 10;
			System.out.println(b);
		}catch(Exception e){
			System.out.println("-----" + e);
		}finally{
			System.out.println("finally");
		}
	}
	

}
