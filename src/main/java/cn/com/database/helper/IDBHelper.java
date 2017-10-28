package cn.com.database.helper;

import java.util.List;
import java.util.Map;

public interface IDBHelper {
	public long create(String table,Map properties);
	public boolean update(String table,Map properties,String condition);
	public boolean delete(String table,String condition);
	public Map retriveByID(String table,String primarykey, String keyValue);
	public List retriveBySQL(String sql);
	public Map retriveBySQL(String sql,boolean pagination,int startLine,int maxSize);
	public Map retriveMapFromSQL(String sql);
	public void executeSQL(String sql);
	public int[] batchCreate(String table, String[] properties,List<Object[]> valueList);
}
