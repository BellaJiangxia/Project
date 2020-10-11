package com.vastsoft.yingtai.http;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.vastsoft.yingtai.module.msg.service.MsgTimer;

/**
 * 用于控制发送提醒诊断短信的定时器<br>
 * 当服务器启动时启动，停止时停止，运行在独立线程中
 */
public class MessageListener implements ServletContextListener
{
	private MsgTimer messageTimer;

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		messageTimer = new MsgTimer();
		messageTimer.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		if (messageTimer!=null)
			messageTimer.close();
	}
}
