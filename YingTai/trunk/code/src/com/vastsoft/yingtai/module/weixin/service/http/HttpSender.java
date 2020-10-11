package com.vastsoft.yingtai.module.weixin.service.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpSender
{

	public String sendGet(String strUrl, String data)
	{
		String strResult = "";
		BufferedReader in = null;

		try
		{
			String urlNameString = strUrl + "?" + data;
			URL realUrl = new URL(urlNameString);

			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();

			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			// 建立实际的连接
			connection.connect();

			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();

			// 遍历所有的响应头字段
			for (String key : map.keySet())
			{
				System.out.println(key + "--->" + map.get(key));
			}

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				strResult += line;
			}
		}
		catch (Exception e)
		{
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}

		return strResult;
	}

	public String sendPost(String strUrl, String data) throws IOException
	{
		PrintWriter out = null;
		BufferedReader in = null;

		String strResult = "";

		try
		{
			URL realUrl = new URL(strUrl);

			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

			conn.setRequestMethod("POST");

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());

			// 发送请求参数
			out.print(data);

			// flush输出流的缓冲
			out.flush();

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				strResult += line;
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException ex)
			{
				throw ex;
			}
		}

		return strResult;
	}

	public String Post(String urlToRequest, Map<String, String> parameters){
		if (urlToRequest == null || urlToRequest.trim().length() == 0)
			return null;
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}

		String urlParameters = constructURLString(parameters);

		InputStream in = null;
		try {
			URL url = new URL(urlToRequest);
			System.out.println("Reuqest URL:::" + urlToRequest);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			OutputStream out;

			byte[] buff;
			con.setRequestMethod("POST");
//			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

			con.setDoOutput(true);
			con.setDoInput(true);
			con.connect();
			out = con.getOutputStream();
			buff = urlParameters.getBytes("UTF8");
			out.write(buff);
			out.flush();
			out.close();
			in = con.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertStreamToString(in,"UTF8");
	}

	private static String constructURLString(Map<String, String> parameters) {
		StringBuffer url = new StringBuffer();

		boolean first = true;

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			try {
				if ((entry.getValue() == null) || (entry.getKey() == null)) {
					continue;
				}
				if (entry.getValue().length() == 0) {
					continue;
				}

				if (first) {
					first = false;
				} else {
					url.append("&");
				}
				url.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				System.out.println("ERROR:错误的编码");
			}
		}

		return url.toString();
	}

	public static String convertStreamToString(InputStream is, String encoding) {
		try {
			if(encoding==null || encoding.isEmpty()) encoding="UTF-8";

			InputStreamReader input = new InputStreamReader(is, encoding);
			final int CHARS_PER_PAGE = 5000;
			final char[] buffer = new char[CHARS_PER_PAGE];
			StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
			try {
				for (int read = input.read(buffer, 0, buffer.length); read != -1; read = input.read(buffer, 0, buffer.length)) {
					output.append(buffer, 0, read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return output.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}

}
