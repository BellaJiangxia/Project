package com.vastsoft.yingtai.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vastsoft.yingtai.core.BaseYingTaiAction;
import com.vastsoft.yingtai.core.Constants;
import com.vastsoft.yingtai.module.user.constants.UserType;
import com.vastsoft.yingtai.module.user.service.UserService.Passport;
import com.vastsoft.yingtai.utils.WebTools;

public class LoginFilter implements Filter {
	/** 过滤的请求后缀列表 */
	private static final List<String> listFilterUrl = new ArrayList<String>();
	/** 忽略的请求后缀列表 */
	private static final List<String> listIgnoreUrl = new ArrayList<String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		listFilterUrl.add("/");// 等于 "/index.html"
		listFilterUrl.add(".html");
		listFilterUrl.add(".action");

		listIgnoreUrl.add("login.html");
		listIgnoreUrl.add("login/index.html");
		listIgnoreUrl.add("login/findPwd.html");
		listIgnoreUrl.add("register/index.html");
		listIgnoreUrl.add("loginByPwd.action");
		listIgnoreUrl.add("uploadFile.action");
		listIgnoreUrl.add("register.action");
		listIgnoreUrl.add("downloadFile.action");
		listIgnoreUrl.add("file_upload_test.html");
		listIgnoreUrl.add("vc.action");
		listIgnoreUrl.add("register/");
		listIgnoreUrl.add("login/");
		listIgnoreUrl.add("reqValidateCode.action");
		listIgnoreUrl.add("isExistsLogName.action");
		listIgnoreUrl.add("resetPwd.action");
		listIgnoreUrl.add("checkBbsToken.action");
		listIgnoreUrl.add("thumbnail.action");

		// for weixin debug
		listIgnoreUrl.add("bindingPatient.action");
		listIgnoreUrl.add("inquiry.action");
		listIgnoreUrl.add("queryCaseHistory.action");
		listIgnoreUrl.add("queryCaseInfo.action");
		listIgnoreUrl.add("queryHospitalOfPatient.action");
		listIgnoreUrl.add("queryPatientInfo.action");
		listIgnoreUrl.add("replyReturnVisit.action");
		listIgnoreUrl.add("requestBindingPatient.action");
		listIgnoreUrl.add("searchHospital.action");
		listIgnoreUrl.add("subscribe.action");
		listIgnoreUrl.add("unsubscribe.action");
		listIgnoreUrl.add("org_logo.action");
		
		listIgnoreUrl.add("legitimate!verifyLegitimateForViewImage.action");

		//for provider
		listIgnoreUrl.add("provider!queryProductInfo.action");
		listIgnoreUrl.add("provider!sendReservation.action");
		listIgnoreUrl.add("provider!queryServiceOrgList.action");
		listIgnoreUrl.add("provider!queryServiceListByOrg.action");
	}

	private boolean needFilter(String url) {
		for (String string : listFilterUrl) {
			if (url.endsWith(string)) {
				for (String str : listIgnoreUrl) {
					if (url.endsWith(str))
						return false;
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		String strURL = servletRequest.getRequestURI();
		String strAppPath = servletRequest.getContextPath();
		// 获取Passport以验证是否已经登录
		Passport passport = checkLogin(servletRequest, servletResponse);

		if (passport != null) // 登录后，验证过滤规则
		{
			if (!passport.getValid() && needFilter(strURL)) {
				servletResponse.setCharacterEncoding("UTF8");
				servletResponse.setContentType("application/json");
				servletResponse.getWriter().write("{\"code\":999999999}");
				return;

			} else if (strURL.endsWith("/") || strURL.endsWith(".html") || strURL.endsWith(".action")) // 这两个请求必须进行验证用户有类型
			{
				if (passport.getUserType() == UserType.ADMIN || passport.getUserType() == UserType.SUPER_ADMIN) {
					if (!needFilter(strURL) || this.isAdminPath(strAppPath, strURL)) {
						chain.doFilter(request, response);
					} else {
						servletResponse.sendRedirect(strAppPath + "/admin");
					}
				} else {
					if (this.isAdminPath(strAppPath, strURL) == false) {
						chain.doFilter(request, response);
					} else {
						servletResponse.sendRedirect(strAppPath + "/");
					}
				}
			} else// 其它的请求不用验证
			{
				chain.doFilter(request, response);
			}
		} else// 未登录时验证过滤规则
		{
			if (needFilter(strURL)) {
				if (strURL.endsWith(strAppPath + "/index.html") || strURL.endsWith(strAppPath + "/")) {
					servletResponse.sendRedirect(strAppPath + "/login");
					return;
				}
				if (strURL.toLowerCase().endsWith(".action")) {
					servletResponse.setCharacterEncoding("UTF8");
					servletResponse.setContentType("application/json");
					StringBuilder sbb = new StringBuilder("{");
					sbb.append("\"name\":\"");
					sbb.append("你还未登录或会话已经过期，请重新登陆\",");
					sbb.append("\"data\":null,");
					sbb.append("\"code\":22221");
					sbb.append("}");
					servletResponse.getWriter().write(sbb.toString());
					return;
				}
				// request.getRequestDispatcher(appName+"/login/index.html").forward(request,
				// response);
				servletResponse.sendRedirect(strAppPath + "/unlogin.html");
				return;
			}

			chain.doFilter(request, response);
		}
	}

	/** 检查用户是否登录 */
	private Passport checkLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String clentIp = WebTools.takeRequestIp(request);
		session.setAttribute(Constants.CLIENT_IP, clentIp);
		Passport pp = (Passport) session.getAttribute(BaseYingTaiAction.PASSPORT);
		return pp != null && pp.getValid() ? pp : null;
	}

	/**
	 * 检查是否是后台管理路径
	 * 
	 * @param strAppPath
	 * @param strUrl
	 * @return
	 */
	private boolean isAdminPath(String strAppPath, String strUrl) {
		if (strUrl.startsWith(strAppPath + "/admin"))
			return true;
		else
			return false;
	}

	@Override
	public void destroy() {

	}

}
