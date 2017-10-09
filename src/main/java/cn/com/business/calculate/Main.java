package cn.com.business.calculate;

import cn.com.database.helper.MySQLDBHelper;

public class Main {

	public static void main(String[] args) {
		//AbstractCalculate calculate = new FirstOneCalculate(new FirstTwoCalculate(new FirstThreeCalculate(new FirstFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(new SecondFourCalculate(null))))))));
		AbstractCalculate calculate = new SecondFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(new SecondFourCalculate(new SecondOneCalculate(new SecondTwoCalculate(new SecondThreeCalculate(null))))))));
		calculate.setLeftIntegral(10000000);
		calculate.setRightIntegral(10000000);
		calculate.setSalesmanID("10");
		calculate.setSalsNum(50);
		calculate.setShopNum(5);
		calculate.calculate();
		calculate.setIntegral(new CalulateIntegral(new MySQLDBHelper()));
		int process = 0;
		calculate.setTempProcess(process);
		calculate.calculate();
		
		System.out.println("description:" + calculate.getDescription() + " and money is " + calculate.getMoney() + " left:" + calculate.getLeftIntegral());
	}

}
