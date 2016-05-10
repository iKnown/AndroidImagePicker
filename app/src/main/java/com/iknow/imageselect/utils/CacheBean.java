package com.iknow.imageselect.utils;

import java.util.HashMap;
import java.util.Map;

public class CacheBean {

	private static Map<String, CacheBean> cacheBeanMap = new HashMap<String, CacheBean>();

	private static Map<String, Map<String, Object>> paramMap = new HashMap<String, Map<String, Object>>();

	public static void putParam(String token, String key, Object value) {
		Map<String, Object> params = paramMap.get(token);
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(key, value);
		paramMap.put(token, params);
	}

	public static Object getParam(String token, String key) {
		Map<String, Object> params = paramMap.get(token);
		if (params == null) {
			return null;
		}
		return params.get(key);
	}

	public static void clean(String token) {
		cacheBeanMap.remove(token);
		paramMap.remove(token);
	}

	public static CacheBean get(String token) {
		CacheBean commonCacheBean = cacheBeanMap.get(token);
		if (commonCacheBean == null) {
			commonCacheBean = new CacheBean();
			cacheBeanMap.put(token, commonCacheBean);
		}
		return commonCacheBean;
	}


}
