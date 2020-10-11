package com.vastsoft.util.service.mail;

import java.io.File;
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

public class MailService implements ServiceController {
	private MailCache cacheMail = new MailCache();

	private List<MailSender> listMailSender = new ArrayList<MailSender>();

	private boolean bLocked = true;

	private List<MailSenderConfig> loadConfig() {
		List<MailSenderConfig> listConfig = new ArrayList<MailSenderConfig>();

		// 读取配置文件
		try {
			String strFilePath = MailService.class.getResource("/").getPath();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(strFilePath + "/mail_config.xml"));
			Element elemRoot = doc.getDocumentElement();

			if (elemRoot.getNodeName().equals("config")) {
				NodeList listElems = elemRoot.getElementsByTagName("mail-sender");
				int iCnt = listElems.getLength();
				for (int i = 0; i < iCnt; i++) {
					Node nodeSender = listElems.item(i);
					NamedNodeMap mapSenderAttrs = nodeSender.getAttributes();
					String strClass = mapSenderAttrs.getNamedItem("class").getTextContent();

					MailSenderConfig cfgSender = new MailSenderConfig(strClass);

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

	public boolean start() {
		List<MailSenderConfig> listConfig = this.loadConfig();

		for (MailSenderConfig cfg : listConfig) {
			try {
				MailSender sender = (MailSender) Class.forName(cfg.getClassPath()).newInstance();
				sender.init(cfg, this.cacheMail);

				this.listMailSender.add(sender);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (MailSenderException e) {
				e.printStackTrace();
			}
		}

		for (MailSender sender : this.listMailSender)
			if (sender.open() == false)
				System.out.println("[MailService] MailSender " + sender.getName() + " start fail");

		this.bLocked = false;

		System.out.println("[MailService] started");

		return true;
	}

	public boolean stop() {
		this.bLocked = true;

		while (this.cacheMail.isEmpty() == false) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (MailSender sender : this.listMailSender)
			if (sender.close() == false)
				System.out.println("[MailService] MailSender " + sender.getName() + " stop fail");

		System.out.println("[MailService] stopped");

		return true;
	}

	public boolean sendMail(String strMailTo, String strPersonal, String strSubject, String strContent) {
		if (this.bLocked)
			return false;

		Mail mail = new Mail(strMailTo, strPersonal, strSubject, strContent);
		this.cacheMail.putMail(mail);

		return true;
	}

	public static void main(String[] args) {
		MailService ms = new MailService();
		ms.start();

		ms.sendMail("joeradar@gmail.com", "尊敬的会员", "测试邮件", "这是一封测试邮件");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		ms.stop();
	}

	@Override
	public RunStatus getStatus() {
		for (MailSender sender : this.listMailSender)
			if (sender.getStatus() == RunStatus.LIVE)
				return RunStatus.LIVE;

		return RunStatus.STOP;
	}

	@Override
	public List<Worker> getWorkerList() {
		return new ArrayList<Worker>(this.listMailSender);
	}

	@Override
	public Worker getWorkerByName(String strWorkerName) {
		for (MailSender sender : this.listMailSender)
			if (sender.getName().equals(strWorkerName))
				return sender;

		return null;
	}
}
