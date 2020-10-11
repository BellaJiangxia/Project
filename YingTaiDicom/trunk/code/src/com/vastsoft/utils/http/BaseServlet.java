package com.vastsoft.utils.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 8654667720660358252L;

	private JSONObject catchException(Throwable e) {
		try {
			JSONObject result = new JSONObject();
			LoggerUtils.logger.error(e.getMessage(), e);
			BaseException be = BaseException.exceptionOf(e);
			result.put("code", be.getCode());
			result.put("errorMsg", be.getMessage());
			return result;
		} catch (Exception e2) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject dataObj = null;
		JSONObject rootJsonObj = null;
		long ttew = System.currentTimeMillis();
		LoggerUtils.logger.info("接收到请求查询病人信息，开始查询...");
		try {
			dataObj = this.doRquest(this.buildParams(req), req);
			rootJsonObj = new JSONObject();
			rootJsonObj.put("code", 0);
			rootJsonObj.put("errorMsg", "");
			if (dataObj == null)
				rootJsonObj.put("data", JSONObject.NULL);
			else
				rootJsonObj.put("data", dataObj);
		} catch (Throwable e) {
			rootJsonObj = this.catchException(e);
		} finally {
			resp.addHeader("Content-Type", "text/html;charset=utf-8");
			OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "utf-8");
			try {
				String jsonStr = rootJsonObj.toString();
				writer.write(jsonStr);
				LoggerUtils.logger.info("查询完成，返回数据："+jsonStr);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				writer.flush();
				writer.close();
			}
		}
		LoggerUtils.logger.info("查询病人信息完成，查询耗时：" + ((System.currentTimeMillis() - ttew) / 1000.0) + "秒");
	}

	protected abstract JSONObject doRquest(RemoteParamsManager buildParams, HttpServletRequest req)
			throws Exception;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	private void printRequestMsg(HttpServletRequest req) {
		// Enumeration<String> attributeNames = req.getAttributeNames();
		// if (attributeNames!=null) {
		// while(attributeNames.hasMoreElements()){
		// String attributeName = attributeNames.nextElement();
		// System.out.println(DateTools.dateToString(new Date())+"
		// 属性："+attributeName+"="+req.getAttribute(attributeName));
		// }
		// }
		// Map<String, String[]> mapParams = req.getParameterMap();
		// if (mapParams!=null) {
		// for (Iterator<String> iterator = mapParams.keySet().iterator();
		// iterator.hasNext();) {
		// String type = (String) iterator.next();
		// System.out.println(DateTools.dateToString(new Date())+"
		// 参数："+type+"="+req.getParameter(type));
		// }
		// }
		// Enumeration<String> headerNames = req.getHeaderNames();
		// if (headerNames!=null) {
		// while(headerNames.hasMoreElements()){
		// String headerName = headerNames.nextElement();
		// System.out.println(DateTools.dateToString(new Date())+"
		// 属性："+headerName+"="+req.getHeader(headerName));
		// }
		// }
		// System.out.println(" 请求信息："+String.valueOf(req));
	}

	private RemoteParamsManager buildParams(HttpServletRequest req) throws SearchExecuteException {
		this.printRequestMsg(req);
		RemoteParamsManager result = new RemoteParamsManager();
		List<RemoteParamsType> listRpType = RemoteParamsType.getAll();
		for (RemoteParamsType remoteParamsType : listRpType) {
			String value = req.getParameter(remoteParamsType.getParam_name());
			if (StringTools.isEmpty(value))
				continue;
			result.addParam(remoteParamsType, value);
		}
		LoggerUtils.logger.info("获取到请求，参数：" + String.valueOf(result));
		return result;
	}
}
