package cn.com.business.calculate;

public abstract class AbstractCalculate {
	private static String description;
	private static int money;
	private static int premoney;

	private static String salesmanID;
	private static int shopNum;
	private static int salsNum;
	private static int leftIntegral;
	private static int rightIntegral;
	private static int calculateProcess;
	private static int tempProcess;
	private static String currentProcessStr;
	private static int monthProcess;
	private static int needSaleNum;
	private static int needShopNum;

	public static String getDescription() {
		return description;
	}

	public static void setDescription(String description) {
		AbstractCalculate.description = description;
	}

	public static int getMoney() {
		return money;
	}

	public static void setMoney(int money) {
		AbstractCalculate.money = money;
	}

	public static int getPremoney() {
		return premoney;
	}

	public static void setPremoney(int premoney) {
		AbstractCalculate.premoney = premoney;
	}

	public static String getSalesmanID() {
		return salesmanID;
	}

	public static void setSalesmanID(String salesmanID) {
		AbstractCalculate.salesmanID = salesmanID;
	}

	public static int getShopNum() {
		return shopNum;
	}

	public static void setShopNum(int shopNum) {
		AbstractCalculate.shopNum = shopNum;
	}

	public static int getSalsNum() {
		return salsNum;
	}

	public static void setSalsNum(int salsNum) {
		AbstractCalculate.salsNum = salsNum;
	}

	public static int getLeftIntegral() {
		return leftIntegral;
	}

	public static void setLeftIntegral(int leftIntegral) {
		AbstractCalculate.leftIntegral = leftIntegral;
	}

	public static int getRightIntegral() {
		return rightIntegral;
	}

	public static void setRightIntegral(int rightIntegral) {
		AbstractCalculate.rightIntegral = rightIntegral;
	}

	public static int getCalculateProcess() {
		return calculateProcess;
	}

	public static void setCalculateProcess(int calculateProcess) {
		AbstractCalculate.calculateProcess = calculateProcess;
	}

	private static CalulateIntegral integral;

	public static CalulateIntegral getIntegral() {
		return integral;
	}

	public static void setIntegral(CalulateIntegral integral) {
		AbstractCalculate.integral = integral;
	}

	public static int getTempProcess() {
		return tempProcess;
	}

	public static void setTempProcess(int tempProcess) {
		AbstractCalculate.tempProcess = tempProcess;

		switch (tempProcess) {
		case 0:
			AbstractCalculate.currentProcessStr = "开始阶段：第一轮第一阶段；";
			break;
		case 1:
			AbstractCalculate.currentProcessStr = "开始阶段：第一轮第二阶段；";
			break;
		case 2:
			AbstractCalculate.currentProcessStr = "开始阶段：第一轮第三阶段；";
			break;
		case 3:
			AbstractCalculate.currentProcessStr = "开始阶段：第一轮第四阶段；";
			break;
		case 4:
			AbstractCalculate.currentProcessStr = "开始阶段：第二轮第一阶段；";
			break;
		case 5:
			AbstractCalculate.currentProcessStr = "开始阶段：第二轮第二阶段；";
			break;
		case 6:
			AbstractCalculate.currentProcessStr = "开始阶段：第二轮第三阶段；";
			break;
		case 7:
			AbstractCalculate.currentProcessStr = "开始阶段：第二轮第四阶段；";
			break;
		default:
			AbstractCalculate.currentProcessStr = "开始阶段：第一轮第一阶段；";
			break;
		}

	}

	public static int getMonthProcess() {
		return monthProcess;
	}

	public static void setMonthProcess(int monthProcess) {
		AbstractCalculate.monthProcess = monthProcess;
	}

	public static String getCurrentProcessStr() {
		return currentProcessStr;
	}

	public static int getNeedSaleNum() {
		return needSaleNum;
	}

	public static void setNeedSaleNum(int needSaleNum) {
		AbstractCalculate.needSaleNum = needSaleNum;
	}

	public static int getNeedShopNum() {
		return needShopNum;
	}

	public static void setNeedShopNum(int needShopNum) {
		AbstractCalculate.needShopNum = needShopNum;
	}

	private AbstractCalculate calculate;

	public AbstractCalculate(AbstractCalculate calculate) {
		this.calculate = calculate;
	}

	public void next() {
		if (calculate != null)
			calculate.calculate();
	}

	public abstract void calculate();
}
