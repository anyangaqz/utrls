package com.sunsheen.cns.edt.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sunsheen.jfids.commons.collections.CollectionUtils;

public class AYTreeUtils {
	/**
	 *
	 * @param list
	 *            所有元素的平级集合，map包含id和pid
	 * @param pid
	 *            顶级节点的pid，可以为null
	 * @param idName
	 *            id位的名称，一般为id或者code
	 * @param leve
	 *            从第层次开始 默认 传0
	 * @param pidName
	 *            父节点 名称
	 * @param childName
	 *            子节点 名称
	 * @return 树
	 */
	public static List<Map<String, Object>> getTree(List<Map<String, Object>> list, String pid, String idName, int leve, String pidName, String childName) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(list))
			for (Map<String, Object> map : list) {
				if (pid == null || pid == "" && map.get(pidName) == null || map.get(pidName) != null && pid != null && map.get(pidName).equals(pid)) {
					String id = (String) map.get(idName);
					map.put(childName, getTree(list, id, idName, leve + 1, pidName, childName));
					map.put("leve", leve);
					res.add(map);
				}
			}
		return res;
	}

}
