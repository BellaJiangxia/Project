package com.vastsoft.yingtai.module.qualityControl.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.vastsoft.util.common.CommonTools;
import com.vastsoft.util.common.ReflectTools;
import com.vastsoft.util.common.StringTools;
import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.module.qualityControl.assist.QualityControlTargetIdTranfer;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlOccasion;
import com.vastsoft.yingtai.module.qualityControl.constants.QualityControlTarget;
import com.vastsoft.yingtai.module.qualityControl.exception.QualityControlException;
import com.vastsoft.yingtai.module.qualityControl.service.QualityControlService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;

/** 质控过滤器 */
public class QualityControlInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = -4525335586233962511L;
	private static Map<String, QualityControlOccasion> mapQualityControlSuffix = new HashMap<String, QualityControlOccasion>();
	static {
//		mapQualityControlSuffix.put("com.vastsoft.yingtai.module.diagnosis2.action.DiagnosisAction.acceptDiagnosis",
//				QualityControlOccasion._ACCEPT_DIAGNOSIS);
		mapQualityControlSuffix.put(
				"com.vastsoft.yingtai.module.basemodule.patientinfo.report.action.ReportAction.printReport",
				QualityControlOccasion._PRINT);
	}

	private QualityControlOccasion needFilter(String actionPath) {
		if (StringTools.isEmpty(actionPath))
			return null;
		actionPath = actionPath.trim();
		if (CommonTools.isEmpty(mapQualityControlSuffix))
			return null;
		for (Iterator<Entry<String, QualityControlOccasion>> iterator = mapQualityControlSuffix.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<String, QualityControlOccasion> entryQualityControlSuffix = (Entry<String, QualityControlOccasion>) iterator
					.next();
			if (StringTools.isEmpty(entryQualityControlSuffix.getKey()))
				continue;
			if (actionPath.equals(entryQualityControlSuffix.getKey()))
				return entryQualityControlSuffix.getValue();
		}
		return null;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy ap = invocation.getProxy();
		Object action = invocation.getAction();
		if (action == null)
			return invocation.invoke();
		String methodName = ap.getMethod();
		if (StringTools.isEmpty(methodName))
			return invocation.invoke();
		QualityControlOccasion occasion = this.needFilter(action.getClass().getName() + "." + methodName);
		if (occasion == null)
			return invocation.invoke();
		QualityControlTargetIdTranfer tranfer = occasion.getTranfer();
		if (tranfer == null)
			throw new QualityControlException("质控时机目标映射转换器未找到！");
		Map<QualityControlTarget, Long> mapTargetEntry = tranfer.tranfer(invocation);
		if (CommonTools.isEmpty(mapTargetEntry))
			return invocation.invoke();
		Passport passport = (Passport) ActionContext.getContext().getSession().get(BaseYingTaiAction.PASSPORT);
//		Set<Long> listQualityControlFormAnswerQCUid = new HashSet<Long>();
//		for (Iterator<Entry<QualityControlTarget, Long>> iterator = mapTargetEntry.entrySet().iterator(); iterator
//				.hasNext();) {
//			Entry<QualityControlTarget, Long> type = (Entry<QualityControlTarget, Long>) iterator.next();
//			List<TQualityControlFormAnswer> listQualityControlFormAnswer;
//			try {
//				listQualityControlFormAnswer = QualityControlService.instance
//						.queryAndAddQualityControlFormAnswer(passport, type.getKey(), type.getValue());
//			} catch (Exception e) {
//				e.printStackTrace();
//				continue;
//			}
//			if (CommonTools.isEmpty(listQualityControlFormAnswer))
//				continue;
//			for (TQualityControlFormAnswer qualityControlFormAnswer : listQualityControlFormAnswer) {
//				if (qualityControlFormAnswer.getStatus() != QualityControlFormAnswerStatus.FINISH.getCode()) {
//					if (!QualityControlService.instance.isIgnoreWithCancelIgnore(qualityControlFormAnswer.getId()))
//						listQualityControlFormAnswerQCUid.add(qualityControlFormAnswer.getId());
//				}
//			}
//		}
//		if (!CommonTools.isEmpty(listQualityControlFormAnswerQCUid)) {
//			String uid = CommonTools.getUUID();
//			QualityControlService.instance.addQualityControlFormAnswerQCUid(uid, listQualityControlFormAnswerQCUid);
		String UID = QualityControlService.instance.queryQualityControlFormUid(passport, mapTargetEntry);
		if (UID==null)
			return invocation.invoke();
		return this.returnNeedQualityControl(invocation, UID);
//		}
		
	}

	private String returnNeedQualityControl(ActionInvocation invocation, String qualityControlUid) throws IOException {
		Object action = invocation.getAction();
		ReflectTools.writeProperty(action, "name", "发现有一张质量控制表需要请您先填写！");
		ReflectTools.writeProperty(action, "code", -9483);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("qualityControlUid", qualityControlUid);
		ReflectTools.writeProperty(action, "mapData", data);
		return "success";
	}

}
