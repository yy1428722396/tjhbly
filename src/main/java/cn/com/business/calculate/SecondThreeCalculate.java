package cn.com.business.calculate;

public class SecondThreeCalculate extends AbstractCalculate {

	public SecondThreeCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if ((super.getLeftIntegral() >= 80 * 500 && super.getRightIntegral() >= 80 * 500)
				|| (super.getLeftIntegral() >= 54 * 500 && super.getRightIntegral() >= 106 * 500)
				|| (super.getLeftIntegral() >= 106 * 500 && super.getRightIntegral() >= 54 * 500)) {
			if (super.getShopNum() >= 3 && super.getSalsNum() >= 20) {
				super.setMoney(super.getMoney() + 3000);
				
				if(super.getMoney() > (400 + 1200 + 3000 + 12000))
					super.setDescription(super.getCurrentProcessStr() + ",结算了一轮循环+第二轮循环的第三阶段");
				else
					super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第三阶段");
			} else{
				super.setPremoney(super.getPremoney() + 3000);
			}
			/*else {
				super.setDescription("结算第二轮第三阶段：发展的美容院数不足3家或者报单数量没有达到20套");
			}*/
			super.setNeedShopNum(3);
			super.setNeedSaleNum(20);
			super.setMonthProcess(7);
			super.setCalculateProcess(7);
			super.next();
		} 
		/*else {
			super.setDescription("结算第二轮第三阶段：左右美容院第一轮结算完剩余销售套数不足160套或者左右套数达不到比例的要求");
		}*/

	}

}
