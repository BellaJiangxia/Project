package com.vastsoft.yingtai.utils.mail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vastsoft.util.service.mail.MailService;

public class MailServer {
	private MailService ms = new MailService();

	public static final String MT_ACTIVE_ACCOUNT = "mt_active_account";
	public static final String MT_RESET_PASSWORD = "mt_reset_pwd";
	public static final String MT_REBUNDLE_EMAIL = "mt_rebundle_email";
	public static final String MT_NEW_USER = "mt_new_user";
	public static final String MT_NEW_TRAVELCOM = "mt_new_travelcom";

	public static final String MK_ACCOUNT_NAME = "account_name";
	public static final String MK_ACTIVE_URL = "active_url";
	public static final String MK_RESET_PASSWORD_URL = "reset_url";
	public static final String MK_DATE = "date";
	public static final String MK_TRAVELCOM_NAME = "travelcom_name";

	private HashMap<String, MailTemplate> mapTemplates = new HashMap<String, MailTemplate>();

	public final static MailServer instance = new MailServer();

	private MailServer() {
		// 读取配置文件
		try {
			String strFilePath = MailServer.class.getResource("/").getPath();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new File(strFilePath + "/mail_template.xml"));
			Element elemRoot = doc.getDocumentElement();

			if (elemRoot.getNodeName().equals("config")) {
				NodeList listElems = elemRoot.getElementsByTagName("template");

				for (int i = 0; i < listElems.getLength(); i++) {
					Node nodeTemplate = listElems.item(i);

					NamedNodeMap map = nodeTemplate.getAttributes();
					String strName = map.getNamedItem("name").getTextContent();
					String strDescription = map.getNamedItem("description").getTextContent();
					String strFile = map.getNamedItem("file").getTextContent();

					this.mapTemplates.put(strName, new MailTemplate(strName, strDescription, strFile));
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 启动服务
		this.ms.start();
	}

	public void sendMail(String strDestAddress, String strPersonal, String strTemplateName,
			Map<String, String> mapContent) throws NoMailTemplateException {
		MailTemplate template = this.mapTemplates.get(strTemplateName);

		if (template == null)
			throw new NoMailTemplateException();

		String strContent = template.createText(mapContent);

		this.ms.sendMail(strDestAddress, strPersonal, template.getDescription(), strContent);
	}

	@Override
	protected void finalize() throws Throwable {
		this.ms.stop();
	}

	public static void main(String[] args) {
		HashMap<String, String> mapContent = new HashMap<String, String>();
		mapContent.put("valid_code", "1111111111");
		mapContent.put("account_name", "eeee");
		mapContent.put("valid_time", "99");

		try {
			MailServer.instance.sendMail("aa77420102@126.com", "尊敬的会员", "mt_operat_code", mapContent);
		} catch (NoMailTemplateException e) {
			e.printStackTrace();
		}

		// 如果不延时，有可能发送线程还未将邮件发送出去
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}
