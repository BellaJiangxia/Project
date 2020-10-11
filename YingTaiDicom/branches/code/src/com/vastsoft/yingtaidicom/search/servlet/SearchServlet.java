package com.vastsoft.yingtaidicom.search.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.utils.http.BaseServlet;
import com.vastsoft.yingtaidicom.search.SearchService;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

@WebServlet("/searchServlet")
public class SearchServlet extends BaseServlet {
	private static final long serialVersionUID = -3304638178417028233L;

	// @Override
	// public void doPost(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// long ttew=System.currentTimeMillis();
	// LoggerUtils.logger.info("接收到请求查询病人信息，开始查询...");
	// try {
	// } catch (Throwable e) {
	// this.catchException(e);
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
	// LoggerUtils.logger.info("查询病人信息完成，查询耗时："+((System.currentTimeMillis()-ttew)/1000.0)+"秒");
	// }

	@Override
	protected JSONObject doRquest(RemoteParamsManager buildParams, HttpServletRequest req)
			throws SearchExecuteException, SearchConfigFileParseException, JSONException {
		JSONObject result = new JSONObject();
		result.put("rootPatientData", SearchService.getInstance().executeSearch(buildParams).jsonSerialize());
		return result;
	}
}
