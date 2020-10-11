package com.vastsoft.util.service.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vastsoft.util.service.RunStatus;
import com.vastsoft.util.service.ServiceController;
import com.vastsoft.util.service.Worker;

/**
 * 短消息发送服务
 * 
 * @author JoeRadar
 * 
 */
public class SmsService implements ServiceController {
	private MsgCache cacheMsg = new MsgCache();

	private List<MsgSender> listSmsSender = new ArrayList<MsgSender>();

	private boolean bLocked = true;

	private List<MsgSenderConfig> loadConfig() {
		List<MsgSenderConfig> listConfig = new ArrayList<MsgSenderConfig>();

		// 读取配置文件
		try {
//			String strFilePath = SmsService.class.getResource("/").getPath();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(this.getClass().getClassLoader().getResourceAsStream("sms_config.xml"));
			Element elemRoot = doc.getDocumentElement();

			if (elemRoot.getNodeName().equals("config")) {
				NodeList listElems = elemRoot.getElementsByTagName("sms-sender");
				int iCnt = listElems.getLength();
				for (int i = 0; i < iCnt; i++) {
					Node nodeSender = listElems.item(i);
					NamedNodeMap mapSenderAttrs = nodeSender.getAttributes();
					String strClass = mapSenderAttrs.getNamedItem("class").getTextContent();

					MsgSenderConfig cfgSender = new MsgSenderConfig(strClass);

					Node nodeSenderCfg = nodeSender.getFirstChild();
					if (nodeSenderCfg != null) {
						while (nodeSenderCfg.getAttributes() == null)
							nodeSenderCfg = nodeSenderCfg.getNextSibling();

						NamedNodeMap mapSenderCfgAttrs = nodeSenderCfg.getAttributes();
						int iAttrCnt = mapSenderCfgAttrs.getLength();
						for (int j = 0; j < iAttrCnt; j++) {
							Node nodeAttr = mapSenderCfgAttrs.item(j);
							cfgSender.putConfig(nodeAttr.getNodeName(), nodeAttr.getNodeValue());
						}
					}

					listConfig.add(cfgSender);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listConfig;
	}

	@Override
	public boolean start() {
		List<MsgSenderConfig> listConfig = this.loadConfig();

		for (MsgSenderConfig cfg : listConfig) {
			try {
				MsgSender sender = (MsgSender) Class.forName(cfg.getClassPath()).newInstance();

				sender.init(cfg, this.cacheMsg);

				this.listSmsSender.add(sender);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (MsgSenderException e) {
				e.printStackTrace();
			}
		}

		for (MsgSender sender : this.listSmsSender) {
			if (sender.open() == false)
				System.out.println("[SMSService] MsgSender " + sender + " start fail");
		}

		this.bLocked = false;

		System.out.println("[SMSService] start ");

		return true;
	}

	@Override
	public boolean stop() {
		this.bLocked = true;

		while (this.cacheMsg.isEmpty() == false) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (MsgSender sender : this.listSmsSender) {
			if (sender.close() == false)
				System.out.println("[SMSService] MsgSender " + sender + " stop fail");
		}

		System.out.println("[SMSService] stop ");

		return true;
	}

	/**
	 * 发送短信息
	 * 
	 * @param strPhoneNum
	 * @param strMsg
	 * @return
	 */
	public boolean sendSMS(String strPhoneNum, String strMsg) {
		if (this.bLocked)
			return false;

		Msg msg = new Msg(strPhoneNum, strMsg);
		this.cacheMsg.putMsg(msg);

		return true;
	}

	public static void main(String[] args) throws Exception {
		SmsService ss = new SmsService();
		ss.start();

		// ss.sendSMS("13981849881", "你好1，你好1。【昊游网】");
		ss.sendSMS("18111262633", "尊敬的用户，您的影泰平台账户审核已经通过，请登录平台进行后续操作！【影泰科技】");

		ss.stop();
	}

	@Override
	public RunStatus getStatus() {
		for (MsgSender sender : this.listSmsSender)
			if (sender.getStatus() == RunStatus.LIVE)
				return RunStatus.LIVE;

		return RunStatus.STOP;
	}

	@Override
	public List<Worker> getWorkerList() {
		return new ArrayList<Worker>(this.listSmsSender);
	}

	@Override
	public Worker getWorkerByName(String strWorkerName) {
		for (MsgSender sender : this.listSmsSender)
			if (sender.getName().equals(strWorkerName))
				return sender;

		return null;
	}
}
