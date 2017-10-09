package cn.com.business.calculate;

public class FirstOneCalculate extends AbstractCalculate {

	public FirstOneCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if (super.getLeftIntegral() >= 12.5 * 500 && super.getRightIntegral() >= 12.5 * 500) {
			if (super.getShopNum() >= 2) {
				super.setMoney(super.getMoney() + 400);
				
				super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第一阶段");
			}else{
				super.setPremoney(super.getPremoney() + 400);
			}
			/*else {
				super.setDescription("结算第一轮第一阶段：发展的美容院数不足2家或者报单数量没有达到20套");
			}*/
			super.setNeedShopNum(2);
			super.setMonthProcess(1);
			super.setCalculateProcess(1);
			super.next();
		} 
		/*else {
			super.setDescription("结算第一轮第一阶段：左右美容院销售套数不足25套或者左右套数达不到比例的要求");
		}*/

	}

}
