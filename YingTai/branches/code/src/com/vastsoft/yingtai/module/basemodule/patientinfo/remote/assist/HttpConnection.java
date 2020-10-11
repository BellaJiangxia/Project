package com.vastsoft.yingtai.module.basemodule.patientinfo.remote.assist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.common.SystemTools;
import com.vastsoft.util.log.LoggerUtils;

public class HttpConnection {
	public static String requestAsStr(String url, Map<String, String> params) throws IOException {
		BufferedReader reader = null;
		try {
			try {
				InputStream in = HttpConnection.request(url, params);
				if (in == null)
					throw new IOException("连接错误：url："+url + " 参数："+params);
				reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			} catch (Exception e) {
				throw new IOException("连接错误：url："+url + " 参数："+params);
			}
			StringBuilder strResponse = new StringBuilder();
			String sysSplitor = SystemTools.getSeparator();
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				strResponse.append(line).append(sysSplitor);
			}
			LoggerUtils.logger.info("接收到网络请求返回数据，字符串：" + strResponse.toString());
			return strResponse.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static InputStream request(String url, Map<String, String> params) {
		URLConnection connection;
		try {
			StringBuilder urlNameString = new StringBuilder(url);
			if (params != null && params.size()>0){
				urlNameString.append('?');
				boolean isFirth = true;
				for (Iterator<Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
					Entry<String, String> rpType = (Entry<String, String>) iterator.next();
					if (!isFirth)
						urlNameString.append('&');
					urlNameString.append(rpType.getKey()).append('=').append(
							StringTools.isEmpty(rpType.getValue()) ? "" : rpType.getValue().replaceAll(" ", "%20"));
					isFirth = false;
				}
			}
			LoggerUtils.logger.info("发起请求：" + urlNameString.toString());
			URL realUrl = new URL(urlNameString.toString());
			// 打开和URL之间的连接
			connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// // 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// logger.debug(key + "--->" + map.get(key));
			// }
			// List<String> listContentType=map.get("Content-Type");
			// if (listContentType==null||listContentType.isEmpty()) {
			// return null;
			// }
			// for (String contentType : listContentType) {
			// if (contentType!=null&&!contentType.trim().isEmpty()) {
			// if (contentType.contains("application/json")) {
			// return null;
			// }
			// }
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			return connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logger.error("访问远程检索服务器出现异常：URL=" + url + " 错误信息：", e);
			return null;
		}
	}

}
