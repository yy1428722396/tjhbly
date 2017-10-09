package cn.com.business.calculate;

public class FirstFourCalculate extends AbstractCalculate {

	public FirstFourCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if (super.getLeftIntegral() >= 150 * 500 && super.getRightIntegral() >= 150 * 500) {
			if (super.getShopNum() >= 3 && super.getSalsNum() >= 20) {
				super.setMoney(super.getMoney() + 12000);
				
				super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第四阶段");
			}else{
				super.setPremoney(super.getPremoney() + 12000);
			} 
			super.setNeedShopNum(3);
			super.setNeedSaleNum(20);
			super.getIntegral().updateThisIntegral(super.getSalesmanID(), super.getLeftIntegral() - 150 * 500,
					super.getRightIntegral() - 150 * 500);
			super.getIntegral().updateSELFIntegral(super.getSalesmanID(), 150 * 500, 150 * 500);
			super.getIntegral().updateSELFCount(super.getSalesmanID());
			super.setLeftIntegral(super.getLeftIntegral() - 150 * 500);
			super.setRightIntegral(super.getRightIntegral() - 150 * 500);
			super.setCalculateProcess(4);
			
			super.setMonthProcess(4);
			super.next();
		} 
		/*else {
			super.setDescription("结算第一轮第四阶段：左右美容院销售套数不足300套或者左右套数达不到比例的要求");
		}*/
	}

}
