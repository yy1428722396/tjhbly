package cn.com.business.calculate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.com.database.helper.MySQLDBHelper;

@Service
public class CalulateIntegral {
	
	private MySQLDBHelper mySQLDBHelper;
	
	public CalulateIntegral(MySQLDBHelper mySQLDBHelper) {
		this.mySQLDBHelper = mySQLDBHelper;
	}

	public void updateThisIntegral(String id, double leftintegral,
			double rightintegral) {
		Map properties = new HashMap();
		properties.put("LeftIntegral", leftintegral);
		properties.put("RightIntegral", rightintegral);
		mySQLDBHelper.update("salesman", properties, "ID=" + id);
	}
	
	public void updateSELFIntegral(String id,double leftintegral,double rightintegral){
		if((int)leftintegral == 0 && (int)rightintegral == 0){
			mySQLDBHelper.executeSQL("update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+LeftIntegral,SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+rightIntegral where ID=" + id);
		}else{
			mySQLDBHelper.executeSQL("update salesman set SELFTotalUsedLeftIntegral=SELFTotalUsedLeftIntegral+" + leftintegral + ",SELFTotalUsedRightIntegral=SELFTotalUsedRightIntegral+" + rightintegral + " where ID=" + id);
		}
	}
	
	public void updateSELFCount(String id){
		mySQLDBHelper.executeSQL("update salesman set SELFTotalCount=SELFTotalCount+1 where ID=" + id);		
	}
}
