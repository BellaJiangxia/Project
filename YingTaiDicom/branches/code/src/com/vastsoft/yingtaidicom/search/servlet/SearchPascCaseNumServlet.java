package com.vastsoft.yingtaidicom.search.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.common.StringTools;
import com.vastsoft.util.log.LoggerUtils;
import com.vastsoft.utils.http.BaseServlet;
import com.vastsoft.yingtaidicom.search.SearchService;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums;
import com.vastsoft.yingtaidicom.search.assist.LastCaseNums.DoEach;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;

@WebServlet("/searchLastCaseNumServlet")
public class SearchPascCaseNumServlet extends BaseServlet {
	private static final long serialVersionUID = 4462394152307896258L;

	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// this.doPost(req, resp);
	// }
	//
	// @Override
	// protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// try {
	//
	// } catch (Throwable e) {
	// super.catchException(e);
	// } finally {
	// resp.addHeader("Content-Type", "text/html;charset=utf-8");
	// OutputStreamWriter writer = new
	// OutputStreamWriter(resp.getOutputStream(), "utf-8");
	// try {
	// String jsonStr = super.returnResult();
	// writer.write(jsonStr);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// writer.flush();
	// writer.close();
	// }
	// }
	// LoggerUtils.logger.info("执行查询最近病人号请求完成");
	// }

	@Override
	protected JSONObject doRquest(RemoteParamsManager buildParams, HttpServletRequest req)
			throws Exception {
		LoggerUtils.logger.info("接收到查询最近病人号请求，开始查询...");
		LastCaseNums mapResult = SearchService.getInstance().searchLastCaseNums(buildParams);
		final JSONObject result = new JSONObject();
		mapResult.itera(new DoEach() {
			@Override
			public void run(RemoteParamsType paramsType, String caseNum, String patient_name) throws Exception {
				JSONArray jsonArr = result.optJSONArray(paramsType.getParam_name());
				if (jsonArr==null) {
					jsonArr = new JSONArray();
					result.put(paramsType.getParam_name(), jsonArr);
				}
				jsonArr.put(caseNum+(StringTools.isEmpty(patient_name)?"":("["+patient_name+"]")));
			}
		});
		return result;
	}
}
