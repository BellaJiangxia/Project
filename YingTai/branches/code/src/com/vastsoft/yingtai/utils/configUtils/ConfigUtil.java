package com.vastsoft.yingtai.utils.configUtils;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;

public class ConfigUtil {
	private JSONObject jsonObj;
	
	public ConfigUtil(ConfigInf config) {
		jsonObj = config.getJsonObject();
	}

	/**
	 * 仅允许使用.或者,做分割
	 * 
	 * @throws JSONException
	 */
	public void setValue(String path, String value) throws JSONException{
		String[] names = StringTools.splitString(path, '.', ',');
		if (names == null || names.length <= 0)
			throw new JSONException("路径数组必须有数据");
		String[] mewSrs = Arrays.copyOf(names, names.length - 1);
		JSONObject obj = this.getJsonObj(mewSrs);
		obj.put(names[names.length - 1], value);
	}

	private JSONObject getJsonObj(String[] names) throws JSONException {
		JSONObject obj = null;
		for (int i = 0; i < names.length; i++) {
			String pa = names[i];
			if (obj == null) {
				obj = getObjByObj(jsonObj, pa);
			} else {
				obj = getObjByObj(obj, pa);
			}
		}
		return obj;
	}

	/**
	 * 仅允许使用.或者,做分割
	 * 
	 * @throws JSONException
	 */
	public String getValue(String path,String defaultVal){
		try {
			String[] names = StringTools.splitString(path, '.', ',');
			if (names == null || names.length <= 0)
				return null;
			String[] mewSrs = Arrays.copyOf(names, names.length - 1);
			JSONObject ooo = getJsonObj(mewSrs);
			return ooo.getString(names[names.length - 1]);
		} catch (Exception e) {
			return defaultVal;
		}
	}
	
	/**
	 * 仅允许使用.或者,做分割
	 * 
	 * @throws JSONException
	 */
	public String getValue(String path) throws JSONException {
		return getValue(path, "");
	}

	private JSONObject getObjByObj(JSONObject jsonObj, String path) throws JSONException {
		JSONObject result = null;
		String[] sss = null;
		if (path.contains("[") && path.contains("]"))
			sss = StringTools.splitString(path, '[', ']');
		try {
			if (sss != null) {
				JSONArray jarr = jsonObj.getJSONArray(sss[0]);
				result = jarr.getJSONObject(Integer.valueOf(sss[1]));
			} else {
				result = jsonObj.getJSONObject(path);
			}
		} catch (Exception e) {
			if (sss != null) {
				JSONArray ja = new JSONArray();
				jsonObj.put(sss[0], ja);
				int ind = Integer.valueOf(sss[1]);
				JSONObject jo = null;
				for (int i = 0; i < ind; i++) {
					jo = new JSONObject();
					ja.put(jo);
				}
				result = jo;
			}
		}
		if (result == null) {
			result = new JSONObject();
			jsonObj.put(path, result);
		}
		return result;
	}

	public String getJson(String path) throws JSONException {
		String[] names = StringTools.splitString(path, '.', ',');
		if (names == null || names.length <= 0)
			return null;
		JSONObject jsonObj=this.getJsonObj(names);
		return jsonObj.toString();
	}
}
