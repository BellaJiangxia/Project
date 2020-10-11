package com.vastsoft.utils;

import javax.servlet.http.HttpServletRequest;

public class WebTools {
	public static String takeRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		return ip;
	}
}