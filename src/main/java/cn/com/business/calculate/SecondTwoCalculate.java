package cn.com.business.calculate;

public class SecondTwoCalculate extends AbstractCalculate {

	public SecondTwoCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if ((super.getLeftIntegral() >= 27.5 * 500 && super.getRightIntegral() >= 27.5 * 500)
				|| (super.getLeftIntegral() >= 19 * 500 && super.getRightIntegral() >= 36 * 500)
				|| (super.getLeftIntegral() >= 36 * 500 && super.getRightIntegral() >= 19 * 500)) {
			if (super.getShopNum() >= 3 && super.getSalsNum() >= 20) {
				super.setMoney(super.getMoney() + 1200);
				
				if(super.getMoney() > (400 + 1200 + 3000 + 12000))
					super.setDescription(super.getCurrentProcessStr() + ",结算了一轮循环+第二轮循环的第二阶段");
				else
					super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第二阶段");
			} else{
				super.setPremoney(super.getPremoney() + 1200);
			}
			/*else {
				super.setDescription("结算第二轮第二阶段：发展的美容院数不足3家或者报单数量没有达到20套");
			}*/
			super.setNeedShopNum(3);
			super.setNeedSaleNum(20);
			super.setMonthProcess(6);
			super.setCalculateProcess(6);
			super.next();
		} 
		/*else {
			super.setDescription("结算第二轮第二阶段：左右美容院第一轮结算完剩余销售套数不足55套或者左右套数达不到比例的要求");
		}*/
	}

}
