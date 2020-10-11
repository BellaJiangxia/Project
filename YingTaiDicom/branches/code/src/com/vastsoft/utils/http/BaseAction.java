package com.vastsoft.utils.http;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.vastsoft.util.exception.BaseException;

public abstract class BaseAction
{
	public BaseAction(){
		
	}
	
	protected static final String SUCCESS = "success";
	protected static final String ERROR = "error";

	protected int iCode = 0;
	protected String strName = "操作成功";
	private Map<String, Object> mapData = null;

	public int getCode()
	{
		return this.iCode;
	}

	public String getName()
	{
		return this.strName;
	}
	
	public String getErrorMsg()
	{
		return this.strName;
	}

	public Map<String, Object> getData()
	{
		return this.mapData;
	}

	protected void addElementToData(String name, Object value)
	{
		if (mapData == null)
			mapData = new HashMap<String, Object>();
		mapData.put(name, value);
	}

	protected void addToSession(String key, Object value)
	{
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(key, value);
	}

	protected void removeFromSession(String key)
	{
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.remove(key);
	}

	protected void catchException(Exception e)
	{
		BaseException be = BaseException.exceptionOf(e);
		iCode = be.getCode();
		strName = "操作失败！"+be.getMessage();
	}

	protected Object takeBySession(String key)
	{
		return ActionContext.getContext().getSession().get(key);
	}

//	protected Passport takePassport()
//	{
//		Passport p=(Passport) takeBySession(Constants.PASSPORT);
//		return p!=null&&p.getValid()?p:null;
//	}
//	
//	protected Passport isOnline() {
//		Passport passport=(Passport) this.takeBySession(Constants.PASSPORT);
//		if(passport==null){
//			this.iCode=400;
//			this.strName="未登录";
//			return null;
//		}
//		
//		return passport;
//	}
	

	/** Action过滤参数 */
	public static String filterParam(String str) {
		if (str == null)
			return null;
		if (str.trim().isEmpty()) {
			return null;
		}
		return str;
	}

	public static Integer filterParam(Integer in) {
		if (in == null)
			return null;
		if (in > 0) {
			return in;
		}
		return null;
	}

	public static Long filterParam(Long lo) {
		if (lo == null)
			return null;
		if (lo > 0) {
			return lo;
		}
		return null;
	}

	public static Double filterParam(Double dou) {
		if (dou == null)
			return null;
		if (dou > 0) {
			return dou;
		}
		return null;
	}
}
