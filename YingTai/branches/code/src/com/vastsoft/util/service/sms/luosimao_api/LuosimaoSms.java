package com.vastsoft.util.service.sms.luosimao_api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import com.vastsoft.util.service.RunStatus;
import com.vastsoft.util.service.sms.Msg;
import com.vastsoft.util.service.sms.MsgCache;
import com.vastsoft.util.service.sms.MsgSender;
import com.vastsoft.util.service.sms.MsgSenderConfig;
import com.vastsoft.util.service.sms.MsgSenderException;

/**
 * @author dan.shan
 * @since 2013-08-12 3:23 PM
 */
public class LuosimaoSms implements MsgSender, Runnable
{
	private static int iInstIdx = 1;

	private boolean bDebugOnly = true;

	private String strName;

	private String strURL;
	private String strApiKey;

	private Date dtCreated = new Date();
	private Date dtLastStart = new Date();

	private MsgCache cacheMsg;

	private boolean bStopFlag = false;
	private Thread threadSend = null;

	public LuosimaoSms()
	{
		this.strName = "Luosimao" + LuosimaoSms.iInstIdx;
	}

	@Override
	public String toString()
	{
		return this.strName;
	}

	@Override
	public void init(MsgSenderConfig cfg, MsgCache cacheMsg) throws MsgSenderException
	{
		if (cfg == null || cacheMsg == null)
			throw new MsgSenderException();

		String strClassPath = cfg.getClassPath();
		if (this.getClass().getName().equals(strClassPath) == false)
			throw new MsgSenderException();

		this.bDebugOnly = Boolean.parseBoolean(cfg.getConfig("debug_only"));

		this.strApiKey = cfg.getConfig("apikey");
		this.strURL = cfg.getConfig("url");

		this.cacheMsg = cacheMsg;
	}

	@Override
	public String getName()
	{
		return this.strName;
	}

	@Override
	public boolean open()
	{
		this.bStopFlag = false;

		this.threadSend = new Thread(this);
		this.threadSend.start();

		this.dtLastStart = new Date();

		System.out.println("[Luosimao] started");

		return true;
	}

	@Override
	public boolean close()
	{
		if (this.threadSend == null)
			return false;

		this.bStopFlag = true;

		try
		{
			this.threadSend.join();
			this.threadSend = null;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("[Luosimao] stopped");

		return true;
	}

	@Override
	public RunStatus getStatus()
	{
		if (this.bStopFlag == false)
			return RunStatus.LIVE;
		else
			return RunStatus.STOP;
	}

	@Override
	public Date getCreatedDate()
	{
		return new Date(this.dtCreated.getTime());
	}

	@Override
	public Date getLastStartDate()
	{
		return new Date(this.dtLastStart.getTime());
	}

	@Override
	public void run()
	{
		//开始不间断的获取待发邮件并发送
		while (this.bStopFlag == false)
		{
			Msg msg = this.cacheMsg.takeMsg();

			if (msg == null)
			{
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				continue;
			}

			System.out.println("[Luosimao] " + msg.getMobile() + "  -  " + msg.getContent());

			if (this.bDebugOnly)
				continue;

			try
			{
				URL urlPost = new URL(this.strURL);
				HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();

				// 打开读写属性，默认均为false 
				connection.setDoOutput(true);
				connection.setDoInput(true);

				// 设置请求方式，默认为POST
				connection.setRequestMethod("POST");

				// Post请求不能使用缓存 
				connection.setUseCaches(false);

				// URLConnection.setFollowRedirects是static 函数，作用于所有的URLConnection对象。 
				// connection.setFollowRedirects(true); 
				// URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数 
				connection.setInstanceFollowRedirects(true);

				// 配置连接参数 
				String strBasic = "api:" + this.strApiKey;
				connection.setRequestProperty("Accept-Encoding", "gzip");
				connection.setRequestProperty("Authorization", "Basic " + Base64.encodeBase64String(strBasic.getBytes("utf-8")));

				// 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成， 
				// 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略 
				//connection.connect(); 
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());

				//组合发送内容
				String content = "mobile=" + msg.getMobile() + "&message=" + msg.getContent();

				// 转成UTF8, 并写入输出流
				out.write(content.getBytes("utf-8"));
				out.flush();
				out.close();

				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null)
					sb.append(line);

				JSONObject jsonObj = new JSONObject(sb.toString());
				String strErr = jsonObj.getString("error");
				String strMsg = jsonObj.getString("msg");
				String strHit = "";

				if (strErr.equals("0") == false)
				{
					strHit = jsonObj.getString("hit");
				}

				System.out.println("[Luosimao] msgRet:" + strMsg + ",  hitErr:" + strHit);

				reader.close();

				connection.disconnect();
			}
			catch (ProtocolException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void finalize()
	{
		this.close();
	}
}
