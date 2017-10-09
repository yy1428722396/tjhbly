package cn.com.business.calculate;

public class SecondFourCalculate extends AbstractCalculate {

	public SecondFourCalculate(AbstractCalculate calculate) {
		super(calculate);
	}

	@Override
	public void calculate() {

		if ((super.getLeftIntegral() >= 150 * 500 && super.getRightIntegral() >= 150 * 500)
				|| (super.getLeftIntegral() >= 100 * 500 && super.getRightIntegral() >= 200 * 500)
				|| (super.getLeftIntegral() >= 200 * 500 && super.getRightIntegral() >= 100 * 500)) {
			if (super.getShopNum() >= 5 && super.getSalsNum() >= 30) {
				super.setMoney(super.getMoney() + 12000);
				
				/*if(super.getTempProcess() == 0){
					super.getIntegral().updateSELFIntegral(super.getSalesmanID(), 0, 0);
					super.getIntegral().updateThisIntegral(super.getSalesmanID(), 0, 0);
				}*/
				
				
				
				if(super.getMoney() > (400 + 1200 + 3000 + 12000))
					super.setDescription(super.getCurrentProcessStr() + ",结算了一轮循环+第二轮循环的第四阶段");
				else
					super.setDescription(super.getCurrentProcessStr() + ",结算了第一轮循环的第四阶段");
			} else{
				super.setPremoney(super.getPremoney() + 12000);
			}
			super.setNeedShopNum(5);
			super.setNeedSaleNum(30);
			if(super.getLeftIntegral() >= 150 * 500 && super.getRightIntegral() >= 150 * 500)
			{
				super.getIntegral().updateThisIntegral(super.getSalesmanID(), super.getLeftIntegral() - 150 * 500,
						super.getRightIntegral() - 150 * 500);
				super.setLeftIntegral(super.getLeftIntegral() - 150 * 500);
				super.setRightIntegral(super.getRightIntegral() - 150 * 500);
				super.getIntegral().updateSELFIntegral(super.getSalesmanID(), 150 * 500, 150 * 500);
			}else if(super.getLeftIntegral() >= 100 * 500 && super.getRightIntegral() >= 200 * 500)
			{
				super.getIntegral().updateThisIntegral(super.getSalesmanID(), super.getLeftIntegral() - 100 * 500,
						super.getRightIntegral() - 200 * 500);
				super.setLeftIntegral(super.getLeftIntegral() - 100 * 500);
				super.setRightIntegral(super.getRightIntegral() - 200 * 500);
				super.getIntegral().updateSELFIntegral(super.getSalesmanID(), 100 * 500, 200 * 500);
			}else if(super.getLeftIntegral() >= 200 * 500 && super.getRightIntegral() >= 100 * 500)
			{
				super.getIntegral().updateThisIntegral(super.getSalesmanID(), super.getLeftIntegral() - 200 * 500,
						super.getRightIntegral() - 100 * 500);
				super.setLeftIntegral(super.getLeftIntegral() - 200 * 500);
				super.setRightIntegral(super.getRightIntegral() - 100 * 500);
				super.getIntegral().updateSELFIntegral(super.getSalesmanID(), 200 * 500, 100 * 500);
			}
			
			super.getIntegral().updateSELFCount(super.getSalesmanID());
			super.setCalculateProcess(4);
			super.setMonthProcess(4);
			super.next();
		}
		/*else {
			super.setDescription("结算第二轮第三阶段：左右美容院第一轮结算完剩余销售套数不足300套或者左右套数达不到比例的要求");
		}*/

	}

}
