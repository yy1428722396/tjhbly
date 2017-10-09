package cn.com.business.calculate;

public class FirstThreeCalculate extends AbstractCalculate {

	public FirstThreeCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if (super.getLeftIntegral() >= 80 * 500 && super.getRightIntegral() >= 80 * 500) {
			if (super.getShopNum() >= 2) {
				super.setMoney(super.getMoney() + 3000);
				
				super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第三阶段");
			}else{
				super.setPremoney(super.getPremoney() + 3000);
			}
			/*else {
				super.setDescription("结算第一轮第三阶段：发展的美容院数不足2家或者报单数量没有达到20套");
			}*/
			super.setNeedShopNum(2);
			super.setMonthProcess(3);
			super.setCalculateProcess(3);
			super.next();
		}
		/*else {
			super.setDescription("结算第一轮第三阶段：左右美容院销售套数不足160套或者左右套数达不到比例的要求");
		}*/
	}

}
