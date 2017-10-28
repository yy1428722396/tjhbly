package cn.com.database.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MySQLDBHelper implements IDBHelper {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public long create(String table, Map properties) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ");
		sql.append(table);
		sql.append(" (");

		Set set = properties.keySet();
		Iterator key = set.iterator();
		StringBuilder values = new StringBuilder();
		while (key.hasNext()) {
			String k = key.next().toString();
			values.append("'");
			values.append(properties.get(k));
			values.append("',");
			sql.append(k);
			sql.append(",");
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(") values(");
		values.delete(values.length() - 1, values.length());
		sql.append(values.toString());
		sql.append(")");

		final String sqlClause = sql.toString();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		System.out.println("-------" + sql);
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sqlClause, Statement.RETURN_GENERATED_KEYS);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public boolean update(String table, Map properties, String condition) {

		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(table);
		sql.append(" set ");

		Set set = properties.keySet();
		Iterator key = set.iterator();
		StringBuilder values = new StringBuilder();
		while (key.hasNext()) {
			String k = key.next().toString();
			sql.append(k);
			sql.append("='");
			sql.append(properties.get(k));
			sql.append("',");
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(" where ");
		sql.append(condition);

		jdbcTemplate.update(sql.toString());
		return true;
	}

	public boolean delete(String table, String condition) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(table);
		sql.append(" where ");
		sql.append(condition);
		jdbcTemplate.update(sql.toString());
		return true;
	}

	public Map retriveByID(String table, String primarykey, String keyValue) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ");
		sql.append(table);
		sql.append(" where ");
		sql.append(primarykey);
		sql.append("='");
		sql.append(keyValue);
		sql.append("'");
		List result = jdbcTemplate.queryForList(sql.toString());
		Map map = null;
		if (result.size() > 0)
			map = (Map) result.get(0);
		return map;
	}

	public static Object getObj(Class clazz, ResultSet rs) throws Exception {
		// 閹碉拷婀侀惃鍕潣閹拷
		Field[] field = clazz.getDeclaredFields();
		Object info = clazz.newInstance();
		for (int i = 0; i < field.length; i++) {
			String name = field[i].getName().toUpperCase();
			// 瀵版鍩岄弬瑙勭《閸氾拷
			name = "set" + name.charAt(0) + name.substring(1).toLowerCase();
			// 瀵版鍩岀猾璇茬��
			Class c = field[i].getType();
			// 瀵版鍩岄弬瑙勭《
			Method method = clazz.getMethod(name, c);
			// 鐎圭偟骞囬弬瑙勭《
			method.invoke(info, rs.getObject(i + 1));
		}
		return info;
	}

	public Map retriveBySQL(String sql, boolean pagination, int startLine, int maxSize) {
		StringBuilder execsql = new StringBuilder();

		execsql.append(sql);

		if (pagination) {

			execsql.append(" limit ");
			execsql.append(startLine);
			execsql.append(",");
			execsql.append(maxSize);
		}
		List result = jdbcTemplate.queryForList(execsql.toString());
		Map map = new HashMap();
		if (pagination) {
			if (sql.indexOf("from") > 0) {
				String sql4count = "select count(*) from " + sql.substring(sql.indexOf("from") + 4, sql.length());
				int size = jdbcTemplate.queryForObject(sql4count, Integer.class);
				map.put("totalCount", size);
			} else {
				map.put("totalCount", 0);
			}
		} else {
			map.put("totalCount", result.size());
		}
		map.put("data", result);
		return map;
	}

	public List retriveBySQL(String sql) {

		List result = jdbcTemplate.queryForList(sql);

		return result;
	}

	public Map retriveMapFromSQL(String sql) {
		List result = jdbcTemplate.queryForList(sql);
		if (result != null && result.size() > 0)
			return (Map) result.get(0);
		else
			return null;
	}

	public void executeSQL(String sql) {
		jdbcTemplate.update(sql);
	}

	public int[] batchCreate(String table, String[] properties,List<Object[]> valueList) {

		if(properties == null || properties.length == 0)
			return null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ");
		sql.append(table);
		sql.append(" (");
		
		String valueTemp = "(";
		for(int i = 0, l = properties.length ; i < l ; i++) {
			sql.append(properties[i]);
			valueTemp += "?";
			if(i != l - 1) {
				sql.append(",");
				valueTemp += ",";
			}
		}
		sql.append(") values ").append(valueTemp + ")");
		
		final String sqlClause = sql.toString();
		
		return jdbcTemplate.batchUpdate(sqlClause, valueList);
	}

}
