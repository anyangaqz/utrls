package com.sunsheen.cns.edt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AYMapUtils {
	/**
	 * 根据map中的某个key 去除List中重复的map
	 * 
	 * @author shijing
	 * @param list
	 * @param mapKey
	 * @return
	 */
	public static List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>> list, String mapKey) {
		if (null == list || list.size() < 1) {
			return null;
		}
		// 把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
		List<Map<String, Object>> listMap = new ArrayList<>();
		Map<String, Map<String, Object>> msp = new HashMap<>();
		for (int i = list.size() - 1; i >= 0; i--) {
			Map<String, Object> map = list.get(i);
			String id = (String) map.get(mapKey);
			map.remove(mapKey);
			msp.put(id, map);
		}
		// 把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
		Set<String> mspKey = msp.keySet();
		for (String key : mspKey) {
			Map<String, Object> newMap = msp.get(key);
			newMap.put(mapKey, key);
			listMap.add(newMap);
		}
		return listMap;
	}

	/**
	 * 转换成MapMap数据结构
	 * 
	 * @param allDataList
	 *            被转换的list
	 * @param key
	 *            key的名称
	 * @return
	 */
	public static Map<String, Map<String, Object>> mapMap(List<Map<String, Object>> allDataList, String key) {
		Map<String, Map<String, Object>> dataMapMap = new LinkedHashMap<>();
		for (Map<String, Object> dataMap : allDataList) {
			// 添加数据
			Map<String, Object> map = new HashMap<>();
			map.putAll(dataMap);
			dataMapMap.put(map.get(key).toString(), map);
		}
		return dataMapMap;
	}

	/**
	 * 转换成MapList数据结构
	 * 
	 * @param allDataList
	 *            被转换的list
	 * @param key
	 *            key的名称
	 * @return
	 */
	public static Map<String, List<Map<String, Object>>> mapList(List<Map<String, Object>> allDataList, String key) {
		Map<String, List<Map<String, Object>>> dataMapMap = new LinkedHashMap<>();
		for (Map<String, Object> dataMap : allDataList) {
			String id = dataMap.get(key).toString();
			List<Map<String, Object>> list = dataMapMap.get(id);
			if (list != null) {
				list.add(dataMap);
			} else {
				list = new ArrayList<>();
				list.add(dataMap);
				dataMapMap.put(id, list);
			}
		}
		return dataMapMap;

	}
}
