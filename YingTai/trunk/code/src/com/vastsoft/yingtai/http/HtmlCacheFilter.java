package com.vastsoft.yingtai.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlCacheFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			HttpServletResponse servletResponse = (HttpServletResponse) response;
			String strURL = servletRequest.getRequestURI();
//		String strAppPath = servletRequest.getContextPath();
			if (strURL!=null&&(strURL.toLowerCase().endsWith(".html")||strURL.toLowerCase().endsWith("/"))) {
				servletResponse.setHeader( "Pragma", "no-cache" );//兼容http 1.0
				servletResponse.setHeader( "Cache-Control", "no-cache,no-store,must-revalidate" );
				servletResponse.setDateHeader("Expires", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
