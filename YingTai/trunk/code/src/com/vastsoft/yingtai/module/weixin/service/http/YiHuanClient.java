package com.vastsoft.yingtai.module.weixin.service.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.yingtai.module.returnvisit.QuestionType;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisit;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQA;
import com.vastsoft.yingtai.module.returnvisit.entity.TReturnVisitQAOpt;

public class YiHuanClient
{
	public final static YiHuanClient instance = new YiHuanClient();

	private final static String YiHuanURL = "http://yingtai.mobimedical.cn/api.php";

	private YiHuanClient()
	{
	}

	public void sendInquiryReply(String strExternalSysUserId, long lInquirySessionId, String strContent) throws IOException, JSONException
	{
		HttpSender sender = new HttpSender();

//		JSONObject jo = new JSONObject();
		Map<String,String> prms=new HashMap<>();

		prms.put("user_union_id", strExternalSysUserId);
		prms.put("inquiry_session_id", lInquirySessionId+"");
		prms.put("inquiry_reply_content", strContent);

		String strRet = sender.Post(YiHuanClient.YiHuanURL+"?act=on_inquiry_reply", prms);

		System.out.println("参数\t"+prms);

		System.out.println("问诊回复访问结果\t"+strRet);
	}

	public void sendReturnVisit(String strExternalSysUserId, TReturnVisit tRV) throws IOException, JSONException
	{
		HttpSender sender = new HttpSender();

		Map<String,String> prms=new HashMap<>();
//		JSONObject jo = new JSONObject();

		prms.put("user_union_id", strExternalSysUserId);
		prms.put("org_id", tRV.getOrg_id()+"");
		prms.put("org_name", tRV.getOrg_name());
		prms.put("case_id", tRV.getCase_id()+"");
		prms.put("case_info", tRV.getCase_info());
		prms.put("visit_doctor_name", tRV.getDoctor_name());
		prms.put("visit_title", tRV.getVisit_name());
		prms.put("visit_id",tRV.getVisit_id()+"");

		JSONArray jarrQ = new JSONArray();
//		List<Map<String,Object>> listQ=new ArrayList<>();

		for (TReturnVisitQA qa : tRV.getListQA())
		{
			JSONObject joQue = new JSONObject();
//			Map<String,Object> mapQA=new HashMap<>();

			joQue.put("question_id", qa.getQuestion_id());
			joQue.put("question_name", qa.getQuestion_name());
			joQue.put("question_type", qa.getQuestion_type());

			QuestionType type = QuestionType.parseCode(qa.getQuestion_type());

			if (type == QuestionType.SINGLE_CHECK || type == QuestionType.MULTI_CHECK)
			{
				JSONArray jarrOpt = new JSONArray();
//				List<Map<String,Object>> listOpt=new ArrayList<>();

				for (TReturnVisitQAOpt opt : qa.getListOpt())
				{
					JSONObject joOpt = new JSONObject();
//					Map<String,Object> mapOpt=new HashMap<>();
					joOpt.put("id", opt.getOption_idx());
					joOpt.put("name", opt.getOption_name());

					jarrOpt.put(joOpt);
				}

				joQue.put("option", jarrOpt);
			}
			else if (type == QuestionType.ANSWER_SCORE)
			{
				joQue.put("min_score", qa.getMin_score());
				joQue.put("max_score", qa.getMax_score());
			}

			jarrQ.put(joQue);
		}

		prms.put("visit_content",jarrQ.toString());

//		String strRet = sender.sendPost(YiHuanClient.YiHuanURL+"?act=return_visit", jo.toString());
		String strRet=sender.Post(YiHuanClient.YiHuanURL+"?act=return_visit",prms);

		System.out.println("参数\t"+prms.toString());

		System.out.println("回访访问结果\t"+strRet);

		if(strRet!=null&&!strRet.isEmpty()){
			JSONObject res=new JSONObject(strRet);
		}
	}
}
