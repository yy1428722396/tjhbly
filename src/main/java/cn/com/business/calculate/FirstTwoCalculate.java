package cn.com.business.calculate;

public class FirstTwoCalculate extends AbstractCalculate {

	public FirstTwoCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if (super.getLeftIntegral() >= 27.5 * 500 && super.getRightIntegral() >= 27.5 * 500) {
			if (super.getShopNum() >= 2) {
				super.setMoney(super.getMoney() + 1200);
				super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第二阶段");
			}else{
				super.setPremoney(super.getPremoney() + 1200);
			}
			/*else {
				super.setDescription("结算第一轮第二阶段：发展的美容院数不足2家或者报单数量没有达到20套");
			}*/
			super.setNeedShopNum(2);			
			super.setMonthProcess(2);
			super.setCalculateProcess(2);
			super.next();
		} 
		/*else {
			super.setDescription("结算第一轮第二阶段：左右美容院销售套数不足55套或者左右套数达不到比例的要求");
		}*/

	}

}
