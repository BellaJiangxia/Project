package com.vastsoft.yingtai.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.ognl.OgnlValueStack;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.sys.action.FileAction;
import com.vastsoft.yingtai.module.sys.constants.LogOperatModule;
import com.vastsoft.yingtai.module.sys.constants.LogStatus;
import com.vastsoft.yingtai.module.sys.entity.TOperationRecord;
import com.vastsoft.yingtai.module.sys.service.LogService;
import com.vastsoft.yingtai.module.user.entity.TBaseUser;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.annotation.ActionDescHandler;

/** 用于写入系统日志 */
public class LogInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 740680705503164040L;
	private static List<String> ignoreList = new ArrayList<String>();
	static {
		ignoreList.add("com.vastsoft.yingtai.action.CommonAction");
		ignoreList.add("");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		return addLogRecord(invocation);
	}

	private boolean beforeExecute(ActionInvocation invocation, TOperationRecord log) {
		try {
			ActionProxy ap = invocation.getProxy();
			Object action = invocation.getAction();
			Map<String, Object> session = ServletActionContext.getContext().getSession();
			log.setCreate_time(new Date());
			String methodName = ap.getMethod();
			String strDescription = ActionDescHandler.getActionDescription(action == null ? null : action.getClass(),
					methodName);
			if (strDescription == null || strDescription.trim().isEmpty())
				return false;
			log.setDescription(strDescription);
			String actionClassName = action.getClass().getName();
			if (ignoreList.contains(actionClassName))
				return false;
			if (LogOperatModule.parseInfName(actionClassName) == null)
				return false;
			log.setInf_name(actionClassName + "." + methodName);
			Passport passport = (Passport) session.get(BaseYingTaiAction.PASSPORT);
			if (passport == null)
				return false;
			log.setOperator_id(passport.getUserId());
			log.setOrg_id(passport.getOrgId() == null ? -1 : passport.getOrgId());
			TBaseUser user = UserService.instance.queryUserById(passport, passport.getUserId());
			log.setOperator_type(user.getUserType().getCode());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String addLogRecord(ActionInvocation invocation) throws Exception {
		ActionProxy ap = invocation.getProxy();
		Object action = ap.getAction();
		if (action.getClass() == FileAction.class)
			return invocation.invoke();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		if (request == null || session == null)
			return invocation.invoke();

		TOperationRecord tor = new TOperationRecord();
		if (!beforeExecute(invocation, tor))
			return invocation.invoke();
		String result = "";
		try {
			result = invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		OgnlValueStack vs = (OgnlValueStack) invocation.getStack();
		int iCode = (Integer) vs.findValue("code");
		String strName = (String) vs.findValue("name");
		// Map<String, Object> mapData = (Map<String, Object>)
		// vs.findValue("data");
		// Map<String, Object> mapRoot = new HashMap<String, Object>();
		// mapRoot.put("code", iCode);
		// mapRoot.put("name", strName);
		// if (mapData != null)
		// mapRoot.put("data", mapData);
		// tor.setReturns(CommonTools.toJsonString(mapRoot));
		if (iCode == 0) {
			tor.setStatus(LogStatus.SUCCESS.getCode());
		} else {
			tor.setStatus(LogStatus.FAIL.getCode());
			tor.setError_msg("[" + iCode + "]" + strName);
		}
		// ValueStack valueStack = invocation.getStack();
		// List<String> ListPropertyName =
		// takeSetPropertyNameList(action.getClass());
		// Map<String, Object> mapParams = new HashMap<String, Object>();
		// for (String propertyName : ListPropertyName)
		// {
		// Object obj = valueStack.findValue(propertyName);
		// mapParams.put(propertyName, obj);
		// }
		// tor.setParams(CommonTools.toJsonString(mapParams));
		try {
			LogService.instance.addOperationRecord(tor);
		} catch (BaseException e) {
		}
		return result;
	}

	// private List<String> takeSetPropertyNameList(Class<?> actionClass)
	// {
	// Method[] arrayMethod = actionClass.getMethods();
	// List<String> listPropertyName = new ArrayList<String>();
	// for (Method method : arrayMethod)
	// {
	// String methodName = method.getName();
	// if (!methodName.startsWith("set"))
	// continue;
	// if (methodName.length() <= 3)
	// continue;
	// String propertyName = methodName.substring(3);
	// propertyName = Character.toLowerCase(propertyName.charAt(0)) +
	// propertyName.substring(1);
	// listPropertyName.add(propertyName);
	// }
	// return listPropertyName;
	// }
}
