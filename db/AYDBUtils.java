package com.sunsheen.cns.edt.common;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.sunsheen.jfids.system.base.composite.data.query.QueryParameterImpl;
import com.sunsheen.jfids.system.database.DBSession;

@SuppressWarnings("unchecked")
public class AYDBUtils {

	// sql查询
	public static List<Map<String, Object>> querySql(String sqlName, Map<String, Object> parMap, DBSession session) {
		List<Map<String, Object>> dataList = session.createDySQLQuery(sqlName, parMap).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return dataList;
		
	}

	// 预编译 查询
	public static List<Map<String, Object>> querySql(String sqlName, Map<String, Object> parMap, DBSession session, Boolean b) {
		Query query = session.createDySQLQuery(sqlName);
		QueryParameterImpl qp = new QueryParameterImpl();
		query = qp.initParameter(query, parMap);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	// 操作数据库
	public static int saveSql(String sqlName, Map<String, Object> parMap, DBSession session) {
		return session.createDySQLQuery(sqlName, parMap).executeUpdate();
	}

	// 预编译 操作数据库
	public static int saveSql(String sqlName, Map<String, Object> parMap, DBSession session, Boolean b) {
		Query query = session.createDySQLQuery(sqlName);
		QueryParameterImpl qp = new QueryParameterImpl();
		query = qp.initParameter(query, parMap);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.executeUpdate();
	}

}
