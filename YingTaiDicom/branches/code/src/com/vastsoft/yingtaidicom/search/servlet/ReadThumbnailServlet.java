package com.vastsoft.yingtaidicom.search.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.common.CommonTools;
import com.vastsoft.utils.http.BaseServlet;
import com.vastsoft.yingtaidicom.search.SearchService;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.exception.SearchConfigFileParseException;
import com.vastsoft.yingtaidicom.search.exception.SearchExecuteException;

@WebServlet("/readThumbnailServlet")
public class ReadThumbnailServlet extends BaseServlet {
	private static final long serialVersionUID = -8604972615102216013L;

//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		this.doPost(req, resp);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		try {
//			LoggerUtils.logger.info("接收到查询缩略图请求，开始查询...");
//			byte[] thumbnailData = SearchService.getInstance().readThumbnail(this.buildParams(req),req.getParameter("thumbnail_uid"));
//			JSONObject dataJsonObj = new JSONObject();
//			dataJsonObj.put("thumbnail", CommonUtil.bytesToHexString(thumbnailData));
//			super.addJsonObjToData("thumbnailData", dataJsonObj);
//		} catch (Throwable e) {
//			super.catchException(e);
//		} finally {
//			resp.addHeader("Content-Type", "text/html;charset=utf-8");
//			OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "utf-8");
//			try {
//				String jsonStr = super.returnResult();
//				writer.write(jsonStr);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				writer.flush();
//				writer.close();
//			}
//		}
//		LoggerUtils.logger.info("查询缩略图请求执行结束！");
//	}

	@Override
	protected JSONObject doRquest(RemoteParamsManager buildParams, HttpServletRequest req)
			throws SearchExecuteException, SearchConfigFileParseException, JSONException {
		byte[] thumbnailData = SearchService.getInstance().readThumbnail(buildParams,req.getParameter("thumbnail_uid"));
		JSONObject dataJsonObj = new JSONObject();
		dataJsonObj.put("thumbnail", CommonTools.bytesToHexString(thumbnailData));
		return dataJsonObj;
	}
}
