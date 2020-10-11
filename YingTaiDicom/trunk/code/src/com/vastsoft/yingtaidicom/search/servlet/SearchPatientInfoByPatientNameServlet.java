package com.vastsoft.yingtaidicom.search.servlet;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vastsoft.util.common.CollectionTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.utils.http.BaseServlet;
import com.vastsoft.yingtaidicom.search.SearchService;
import com.vastsoft.yingtaidicom.search.assist.PatientInfoResult;
import com.vastsoft.yingtaidicom.search.assist.RemoteParamsManager;
import com.vastsoft.yingtaidicom.search.constants.RemoteParamsType;

@WebServlet("/searchPatientInfoByPatientNameServlet")
public class SearchPatientInfoByPatientNameServlet extends BaseServlet {
	private static final long serialVersionUID = -1090025406426165163L;

	@Override
	protected JSONObject doRquest(RemoteParamsManager buildParams, HttpServletRequest req) throws Exception {
		String patient_name = req.getParameter("patient_name");
		if (StringTools.isEmpty(patient_name))
			patient_name = "";
		else
			patient_name = new String(StringTools.hexStringToBytes(patient_name),"UTF-8");
		List<PatientInfoResult> listPatientInfoResult = SearchService.getInstance()
				.searchPatientInfoByPatientNameOrIdentifyId(buildParams, patient_name,req.getParameter(RemoteParamsType.IDENTITY_ID.getParam_name()));
		if (CollectionTools.isEmpty(listPatientInfoResult))
			return null;
		JSONObject result = new JSONObject();
		JSONArray dataJsonArr = new JSONArray();
		for (PatientInfoResult patientInfoResult : listPatientInfoResult) {
			dataJsonArr.put(patientInfoResult.jsonSerialize());
		}
		result.put("listPatientInfoResult", dataJsonArr);
		return result;
	}

}
